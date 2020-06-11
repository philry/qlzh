package com.sy.quartz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.sy.service.WorkService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.sy.core.netty.tcp.NettyServerHandler;
import com.sy.dao.DeptMapper;
import com.sy.dao.EnergyMapper;
import com.sy.dao.MachineNowDao;
import com.sy.dao.MachineNowMapper;
import com.sy.dao.NettyMapper;
import com.sy.dao.XpgMapper;
import com.sy.entity.Energy;
import com.sy.entity.MachineNow;
import com.sy.entity.MessageData;
import com.sy.entity.Netty;
import com.sy.entity.Xpg;
import com.sy.service.MessageDataService;


/**
 * 超限警告及定时关机
 */
@Component
public class NettyQuartz extends QuartzJobBean {

	@Autowired
	private NettyServerHandler nettyServerHandler;

	@Autowired
	private NettyMapper nettyMapper;

	@Autowired
	private EnergyMapper energyMapper;

	@Autowired
	private XpgMapper xpgMapper;

	@Autowired
	private DeptMapper deptMapper;

	@Autowired
	private MessageDataService messageDataService;

	@Autowired
	private MachineNowMapper machineNowMapper;

	@Autowired
	private MachineNowDao machineNowDao;

	@Autowired
	private WorkService workService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		System.out.println("开始查询netty");
		// 查询出当前正在工作的所以焊机
		List<MachineNow> list = machineNowDao.findAll();
		if (list != null && list.size() > 0) {
			Xpg xpg = null;
			Netty last = null;
			double maxA = 0;
			double minA = 0;
			for (MachineNow machineNow : list) {
				// 获取焊机的工作最大电流和非工作最大电流
				maxA = machineNow.getMachine().getMaxA();
				minA = machineNow.getMachine().getMinA();
				Integer machineId = machineNow.getMachine().getId();
				// 获取xpg信息
				xpg = xpgMapper.selectXpgByMachineId(machineNow.getMachine().getId());
				// 获取最新的netty数据
				last = nettyMapper.getLastNettyByXpg(xpg.getName());
				Integer personId = machineNow.getPerson().getId();
				// 判断是否超限
				/*原来的
				boolean flag = true;
				String[] currents = last.getCurrents().split(",");
				for (String s : currents) {
					if (Double.valueOf(s) < maxA) { //原来的对比方式
				//	if(new BigDecimal(s).compareTo(new BigDecimal(maxA)) < 0){ //我改的对比方式
						flag = false;
						break;
					}
				}*/

				//我写的
				boolean flag = false;
				String[] currents = last.getCurrents().split(",");
				for (String s : currents) {
					if (Double.valueOf(s) > maxA) {
						//	if(new BigDecimal(s).compareTo(new BigDecimal(maxA)) < 0){ //我改的对比方式
						flag = true;
						break;
					}
				}

				if (flag) {
					// 如果超限,发送超限警告,并关闭焊机,删除machine_now中该焊机的数据
					MessageData messageData = new MessageData();
					messageData.setSendId(0);
					Integer leader = deptMapper.selectDeptById(machineNow.getMachine().getDept().getId()).getLeader();
			//		messageData.setAccpetId(leader);//原来的

					messageData.setAccpetId(personId);//我改的
					messageData.setContext(String.valueOf(machineNow.getMachine().getId()));
					try {
						// 发送超限警告信息
						messageDataService.sendMessage(messageData, 2);
					} catch (Exception e) {
						e.printStackTrace();
					}
				//	machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
					try {
						nettyServerHandler.controlMachine(xpg.getName(), false);
						Integer taskId = workService.selectTaskIdByPersonAndMachine(personId,machineId);
						workService.endWork(personId,taskId,machineId);
						machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println(machineNow.getMachine().getId() + "号焊机已超限");
				// 如果没有超限,则判断是否在工作,如果处于非工作状态,判断其未工作时间是否达到设定的定时关机时间
				} else {

					boolean flag2 = true;//flag2为true表示处于非工作状态
					for (String s : currents) {
						if (Double.valueOf(s) > minA) { //有一个电流大于最小电流就表示在工作，处于非工作状态就为false
					//	if(new BigDecimal(s).compareTo(new BigDecimal(minA))>0){ //我改的对比方式
							flag2 = false;
							break;
						}
					}
					if (flag2) {
						// 获取定时关机设定时间
						List<Energy> energyList = energyMapper.selectEnergyList();
						Integer time = energyList.get(0).getTime(); //time单位为min
						Netty pre = nettyMapper.selectNettyByXpgAndTime(xpg.getName(), time); //当前正在工作的焊机当前时间往前{time}个的底表数据
						//距该xpg对应最新包的时间未超过2分钟内
						if ((new Date().getTime()/1000L/60-last.getCreateTime().getTime()/1000L/60) <= 2) { //Todo 是否影响实际自动关机时间？待确认
							String[] currents2 = pre.getCurrents().split(",");
							boolean flag3 = true;
							for (String s : currents2) {
								if (Double.valueOf(s) > minA) { //有一个电流大于最小电流就表示在工作，处于非工作状态就为false
						//		if(new BigDecimal(s).compareTo(new BigDecimal(minA))>0){ //我改的对比方式
									flag3 = false;
									break;
								}
							}
							if (flag3) {
								// 判断是否持续未工作(原来的方式)
								List<Netty> lists = nettyMapper.selectAllNettyByXpgAndTime(xpg.getName(), time);
								if (lists.size() >= (time - 1)) { //该xpg对应的netty表中的包有了time-1个了，表示到了设定的关机时间了
									boolean flag4 = true; // flag4为true表示已经到了设定关机的时间
									//原来的方式
									long preTime = (lists.get(0).getCreateTime().getTime())/1000L/60;
									outer: for (Netty n : lists) {
										String[] currents3 = n.getCurrents().split(",");
										if(preTime-n.getCreateTime().getTime()/1000L/60 <= 1) {
											preTime = n.getCreateTime().getTime()/1000L/60;
											for (String s : currents3) {
												if (Double.valueOf(s) > minA) {//原来的对比方式
										//		if(new BigDecimal(s).compareTo(new BigDecimal(minA))>0){//我改的对比方式
													flag4 = false;
													break outer;
												}
											}
										}else {
											flag4=false;
											break;
										}
									}
									if (flag4) {
								//		machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
										try {
											nettyServerHandler.controlMachine(xpg.getName(), false);
											Integer taskId = workService.selectTaskIdByPersonAndMachine(personId,machineId);
											workService.endWork(personId,taskId,machineId);
											machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
										} catch (Exception e) {
											e.printStackTrace();
										}
										System.out.println(machineNow.getMachine().getId() + "号焊机已自动关机");
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
