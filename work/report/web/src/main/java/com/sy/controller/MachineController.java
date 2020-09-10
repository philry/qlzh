package com.sy.controller;

import java.util.List;

import com.sy.core.netty.tcp.NettyServerHandler;
import com.sy.entity.*;
import com.sy.service.EnergyService;
import com.sy.service.XpgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sy.constant.HttpStatusConstant;
import com.sy.service.MachineService;
import com.sy.vo.JsonResult;
import com.sy.vo.PageResult;

@RequestMapping("/machine")
@RestController
public class MachineController {

	@Autowired
	private MachineService machineService;

	@Autowired
	private XpgService xpgService;

	@Autowired
	private EnergyService energyService;

	@Autowired
	private NettyServerHandler nettyServerHandler;
	
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

	@RequestMapping(value = "onEmergencyState",method = RequestMethod.GET) //打开应急状态
	public JsonResult onEmergencyState(){
		try {
			Xpg xpg = new Xpg();
			List<Xpg> list = xpgService.selectXpgList(xpg);
			for(Xpg xpg1 : list){
				String xpgName = xpg1.getName();
				nettyServerHandler.controlMachine(xpgName,true);//应急状态打开所有焊机
			}
			Energy energy = new Energy();
			energy.setTime(120000);
			energyService.updateEnergy(energy);
		}catch (Exception e){
			e.printStackTrace();
			return JsonResult.buildFailure(404,e.getMessage());
		}

		return JsonResult.buildSuccess(200,"操作成功");
	}

	@RequestMapping(value = "offEmergencyState",method = RequestMethod.GET) //关闭应急状态
	public JsonResult offEmergencyState(){
		try {
			Energy energy = new Energy();
			energy.setTime(15);
			energyService.updateEnergy(energy);
		}catch (Exception e){
			e.printStackTrace();
			return JsonResult.buildFailure(404,e.getMessage());
		}

		return JsonResult.buildSuccess(200,"操作成功");
	}
}
