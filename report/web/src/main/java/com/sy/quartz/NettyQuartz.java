package com.sy.quartz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.sy.dao.*;
import com.sy.entity.*;
import com.sy.service.WorkService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.sy.core.netty.tcp.NettyServerHandler;
import com.sy.service.MessageDataService;


/**
 *
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
	private PersonMapper personMapper;

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


		List<MachineNow> list = machineNowDao.findAll();
		if (list != null && list.size() > 0) {
			Xpg xpg = null;
			Netty last = null;
			double maxA = 0;
			double minA = 0;
			for (MachineNow machineNow : list) {

				maxA = machineNow.getMachine().getMaxA();
				minA = machineNow.getMachine().getMinA();
				Integer machineId = machineNow.getMachine().getId();

				xpg = xpgMapper.selectXpgByMachineId(machineNow.getMachine().getId());
				Integer personId = machineNow.getPerson().getId();
				Date openTime = workService.getLastOpenTimeByMachine(machineId);

				last = nettyMapper.getLastNettyByXpgAndOpenTime(xpg.getName(),openTime);



				/*
				boolean flag = true;/
				if(last == null){
					try {
						Thread.sleep(60*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				String[] currents = last.getCurrents().split(",");
				for (String s : currents) {
					if (Double.valueOf(s) < maxA) {
				//	if(new BigDecimal(s).compareTo(new BigDecimal(maxA)) < 0){
						flag = false;
						break;
					}
				}*/


				boolean flag = false;
				Date now = new Date();
				long jgtime = now.getTime()-openTime.getTime();
				if(jgtime <= 60*1000){
					if(last == null){
						//	while(last == null){
						/*try {
							//TODO
							Thread.sleep(60*1000);
							break;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}*/
						continue;
					}
				}
				if(last == null){
					/*try {
						nettyServerHandler.controlMachine(xpg.getName(), false);
						Integer taskId = workService.selectTaskIdByPersonAndMachine(personId, machineId);
						workService.endWork(personId, taskId, machineId);
						machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
					} catch (Exception e) {
						e.printStackTrace();
					}*/
					continue;
				}

				if(last != null) {

					String[] currents = last.getCurrents().split(",");
					/*for (String s : currents) {
						if (Double.valueOf(s) > maxA) {
							//	if(new BigDecimal(s).compareTo(new BigDecimal(maxA)) < 0){
							flag = true;
							break;
						}
					}*/
					/*for(int j = 0; j < currents.length; j++) {
						if(j < (currents.length-2)){
							double i = Double.parseDouble(currents[j]);
							if (i > maxA) {
								double i2 = Double.parseDouble(currents[j+1]);
								if(i2 > maxA) {
									double i3 = Double.parseDouble(currents[j+2]);
									if (i3 > maxA) {
										flag = true;
										break;
									}
								}
							}
						}
					}*/


					for (int j = 0; j < (currents.length - 2); j++) {
						double i = Double.parseDouble(currents[j]);
						if (i > maxA) {
							double i2 = Double.parseDouble(currents[j + 1]);
							double i3 = Double.parseDouble(currents[j + 2]);
							if (i2 > maxA && i3 > maxA) {
								flag = true;
								break;
							}
						}
					}


					if (flag) {
//
//						MessageData messageData = new MessageData();
//						messageData.setSendId(0);
//					/*Integer leader = deptMapper.selectDeptById(machineNow.getMachine().getDept().getId()).getLeader();
//					messageData.setAccpetId(leader);*/
//						messageData.setAccpetId(personId);
//						messageData.setContext(String.valueOf(machineNow.getMachine().getId()));
//						try {
//
//							messageDataService.sendMessage(messageData, 2);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					/*
//                    Person person = personMapper.selectPersonById(personId);
//					Dept dept = deptMapper.selectDeptById(person.getDeptId());
//					Integer leader = dept.getLeader();*/
//
//						MessageData messageData2 = new MessageData();
//						messageData2.setSendId(0);
//						Integer leader = deptMapper.selectDeptById(machineNow.getMachine().getDept().getId()).getLeader();
//						messageData2.setAccpetId(leader);
//						messageData2.setContext(String.valueOf(machineNow.getMachine().getId()));
//						try {
//
//							messageDataService.sendMessage(messageData2, 2);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						//	machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
//						try {
//							nettyServerHandler.controlMachine(xpg.getName(), false);
//							Integer taskId = workService.selectTaskIdByPersonAndMachine(personId, machineId);
//							workService.endWork(personId, taskId, machineId);
//							machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
					}  else {

						boolean flag2 = true;
						for (String s : currents) {
							if (Double.valueOf(s) > minA) {
								//	if(new BigDecimal(s).compareTo(new BigDecimal(minA))>0){
								flag2 = false;
								break;
							}
						}
						if (flag2) {
							List<Energy> energyList = energyMapper.selectEnergyList();
							Integer time = energyList.get(0).getTime();
							Netty pre = nettyMapper.selectNettyByXpgAndTime(xpg.getName(), time);

							if ((new Date().getTime() / 1000L / 60 - last.getCreateTime().getTime() / 1000L / 60) <= 2) {
								String[] currents2 = pre.getCurrents().split(",");
								boolean flag3 = true;
								for (String s : currents2) {
									if (Double.valueOf(s) > minA) {
										//		if(new BigDecimal(s).compareTo(new BigDecimal(minA))>0){
										flag3 = false;
										break;
									}
								}
								if (flag3) {

									List<Netty> lists = nettyMapper.selectAllNettyByXpgAndTime(xpg.getName(), time);
									if (lists.size() >= (time - 1)) {
										boolean flag4 = true;

										long preTime = (lists.get(0).getCreateTime().getTime()) / 1000L / 60;
										outer:
										for (Netty n : lists) {
											String[] currents3 = n.getCurrents().split(",");
											if (preTime - n.getCreateTime().getTime() / 1000L / 60 <= 1) {
												preTime = n.getCreateTime().getTime() / 1000L / 60;
												for (String s : currents3) {
													if (Double.valueOf(s) > minA) {
														//		if(new BigDecimal(s).compareTo(new BigDecimal(minA))>0){
														flag4 = false;
														break outer;
													}
												}
											} else {
												flag4 = false;
												break;
											}
										}
										if (flag4) {
											//		machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
											try {
												nettyServerHandler.controlMachine(xpg.getName(), false);
												Integer taskId = workService.selectTaskIdByPersonAndMachine(personId, machineId);
												workService.endWork(personId, taskId, machineId);
												machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
											} catch (Exception e) {
												e.printStackTrace();
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
	}
}
