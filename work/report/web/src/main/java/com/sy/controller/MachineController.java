package com.sy.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.sy.core.netty.tcp.NettyServerHandler;
import com.sy.dao.NettyMapper;
import com.sy.dao.TaskDao;
import com.sy.dao.XpgMapper;
import com.sy.entity.*;
import com.sy.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sy.constant.HttpStatusConstant;
import com.sy.vo.JsonResult;
import com.sy.vo.PageResult;

@RequestMapping("/machine")
@RestController
public class MachineController {

	@Autowired
	private MachineService machineService;

	@Autowired
	private MachineNowService machineNowService;

	@Autowired
	private XpgService xpgService;

	@Autowired
	private WorkService workService;

	@Autowired
	private EnergyService energyService;

	@Autowired
	private EmergencyService emergencyService;

	@Autowired
	private NettyServerHandler nettyServerHandler;

	@Autowired
	private TaskDao taskDao;

	@Autowired
	private NettyMapper nettyMapper;

	@Autowired
	private XpgMapper xpgMapper;

	Logger logger = Logger.getLogger(MachineController.class);

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public PageResult getList(Machine machine) {
		List<Machine> list = machineService.selectMachineList(machine);
		return PageResult.getPageResult(list,null);
	}

	@RequestMapping(value = "/lists/{yema}", method = RequestMethod.GET)
	public PageResult getLists(Machine machine,@PathVariable("yema")Integer yema) {
		Page<Object> startPage = PageHelper.startPage(yema, 30);
		List<Machine> list = machineService.selectMachineList(machine);
		return PageResult.getPageResult(list,startPage.getTotal());
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(Machine machine) {
		Machine machine1 = machineService.selectMachineByXpgId(machine.getXpgId());
		if(machine1 != null){
			throw new RuntimeException("该2G码已经和其他焊机绑定，请选择其他2G码");
		}
		try {
			return JsonResult.getJson(machineService.insertMachine(machine));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.buildFailure(404,e.getMessage());
		}
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public JsonResult edit(Machine machine) {
		//	xpgService.edit(machine);//这是用于焊机换采集器做数据迁移功能
		return JsonResult.getJson(machineService.updateMachine(machine));
	}

	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public JsonResult removeById(@PathVariable("id")Integer id) {
		return JsonResult.getJson(machineService.removeMachineById(id));
	}

	@RequestMapping(value = "/querybyid/{id}", method = RequestMethod.GET)
	public JsonResult selectMachineById(@PathVariable("id")Integer id) {
		return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, machineService.selectMachineById(id));
	}

	@RequestMapping(value = "/queryByIdAndName", method = RequestMethod.GET)
	public PageResult selectMachineByName(Integer id,String name) {
	//	return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, machineService.selectMachineByIdAndName(id,name));
		List<Machine> list = machineService.selectMachineByIdAndName(id,name);
		return PageResult.getPageResult(list,null);
	}

	@RequestMapping(value = "/onEmergencyState",method = RequestMethod.GET) //打开应急状态
	//	public JsonResult onEmergencyState(Integer personId,Integer taskId){
	public JsonResult onEmergencyState(Integer personId){//谁点的应急状态就传谁的id
		try {
			Energy energy = new Energy();
			energy.setTime(120000);
			energyService.updateEnergy(energy);

			/*Xpg xpg = new Xpg();
			List<Xpg> list = xpgService.selectXpgList(xpg);*/
			Machine machine = new Machine();
			List<MachineNow> machineNowList = machineNowService.getNowMachine();
			List<Machine> machineList = machineService.selectMachineList(machine);
			List<Machine> machineNowList1 = new ArrayList<>();
			for(MachineNow machineNow : machineNowList){
				machineNowList1.add(machineNow.getMachine());
			}
			HashSet h1 = new HashSet(machineList);
			HashSet h2 = new HashSet(machineNowList1);
			h1.removeAll(h2);
			machineList.removeAll(machineNowList1);
			machineList.clear();
			machineList.addAll(h1); //去除已经开机的焊机

			/*for(MachineNow machineNow : machineNowList){
				String xpgName = machineNow.getMachine().getXpg().getName();
				nettyServerHandler.controlMachine(xpgName, false);//关闭所有焊机
				workService.endWork(personId,taskId,machineNow.getMachine().getId());
			}*/
			Integer taskId = null;

			List<Integer> taskIds = taskDao.getIdsByPersonIdDesc(personId);
			if(taskIds != null && !taskIds.isEmpty()){
				taskId = taskIds.get(0);//这个人有派到的任务就取时间最新的任务id
			}else{
				taskId = taskDao.getIdsByPidDesc(0).get(0);//这个人没有派到的任务就取整船级的时间最新的任务id
			}

			for(Machine machine1: machineList){
			/*for(Xpg xpg1 : list) {
				String xpgName = xpg1.getName();*/
				try{
					nettyServerHandler.controlMachine(machine1.getXpg().getName(),true);//打开所有焊机
//					workService.startWork(personId, taskId, machine1.getId());//原来的
				}catch (Exception e){
					e.printStackTrace();
				}
				/*Thread.sleep(2*60*1000);
				String xpgName = xpgMapper.selectXpgByMachineId(machine1.getId()).getName();
				Netty netty = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 2*60*1000));//往前1分钟之后有没包
				if(netty == null){ //没包说明开机失败
					System.out.println("---------------开机失败----------------");
					throw new RuntimeException("开机失败");
				}else { //有包说明开机成功
					workService.startWork(personId, taskId, machine1.getId());
				}*/
			}

			//TODO machineList  重新统计遍历

			/*//新增的start
			Thread.sleep(1*60*1000);
			for(Machine machine1: machineList){
				String xpgName = xpgMapper.selectXpgByMachineId(machine1.getId()).getName();
				Netty netty = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
				if(netty == null){ //没包说明开机失败
					System.out.println(xpgName+"---------------开机失败----------------");
				}else { //有包说明开机成功
					try{
						workService.startWork(personId, taskId, machine1.getId());
					}catch (Exception e){
						e.printStackTrace();
					}
				}
			}
			//新增的end*/

			//新增的start2
			Thread.sleep(1*60*1000);
			for(Machine machine1: machineList){
				String xpgName = xpgMapper.selectXpgByMachineId(machine1.getId()).getName();
				Netty netty = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
				if(netty == null){ //没包说明开机失败
//					System.out.println(xpgName+"---------------第一次开机失败，尝试第二次开机-----------------");
					logger.info(xpgName+"---------------第一次开机失败，尝试第二次开机-----------------");
					nettyServerHandler.controlMachine(machine1.getXpg().getName(),true);
			//		Thread.sleep(1*60*1000);
					Netty netty2 = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
					if(netty2 == null){ //还没包说明第二次开机失败
//						System.out.println(xpgName+"---------------第二次开机失败，尝试第三次开机----------------");
						logger.info(xpgName+"---------------第二次开机失败，尝试第三次开机----------------");
						nettyServerHandler.controlMachine(machine1.getXpg().getName(),true);
				//		Thread.sleep(1*60*1000);
						Netty netty3 = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
						if(netty3 == null){ //还没包说明第三次开机失败
//							System.out.println(xpgName+"---------------第三次开机失败----------------");
							logger.info(xpgName+"---------------第三次开机失败----------------");
						}
					}
				}else { //有包说明开机成功
					workService.startWork(personId, taskId, machine1.getId());
				}
			}
			//新增的end2

			Emergency emergency = new Emergency();
			emergency.setStatus("1");//设置成处于应急状态打开的状态
			emergencyService.updateEmergency(emergency);
		}catch (Exception e){
			e.printStackTrace();
			return JsonResult.buildFailure(404,e.getMessage());
		}

		return JsonResult.buildSuccess(200,"操作成功");
	}

	@RequestMapping(value = "/offEmergencyState",method = RequestMethod.GET) //关闭应急状态
//	public JsonResult offEmergencyState(Integer personId,Integer taskId){
	public JsonResult offEmergencyState(Integer personId){	//不能统一关机，因为work表的person_id和task_id不能为null,不然报表会出错
		try {
			Energy energy = new Energy();
			energy.setTime(15);
			energyService.updateEnergy(energy);

			/*Xpg xpg = new Xpg();
			List<Xpg> list = xpgService.selectXpgList(xpg);*/
			/*Machine machine = new Machine();
			List<MachineNow> machineNowList = machineNowService.getNowMachine();
			List<Machine> list = machineService.selectMachineList(machine);
			for(MachineNow machineNow : machineNowList){
				String xpgName = machineNow.getMachine().getXpg().getName();
				nettyServerHandler.controlMachine(xpgName, false);//关闭所有焊机
				workService.endWork(personId,taskId,machineNow.getMachine().getId());
			}*/
			/*for(Machine machine1: list){
			 *//*for(Xpg xpg1 : list) {
				String xpgName = xpg1.getName();*//*
				nettyServerHandler.controlMachine(machine1.getXpg().getName(), false);//关闭所有焊机
				workService.endWork(personId,taskId,machine1.getId());
			}*/

			Emergency emergency = new Emergency();
			emergency.setStatus("0");//设置成处于应急状态关闭的状态
			emergencyService.updateEmergency(emergency);
		}catch (Exception e){
			e.printStackTrace();
			return JsonResult.buildFailure(404,e.getMessage());
		}

		return JsonResult.buildSuccess(200,"操作成功");
	}
}
