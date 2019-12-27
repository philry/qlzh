package com.sy.quartz;

import java.util.List;

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

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		System.out.println("开始查询netty");
		List<MachineNow> list = machineNowDao.findAll();
		Xpg xpg = null;
		Netty last = null;
		double maxA = 0;
		double minA = 0;
		for (MachineNow machineNow : list) {
			maxA = machineNow.getMachine().getMaxA();
			minA = machineNow.getMachine().getMinA();
			xpg = xpgMapper.selectXpgByMachineId(machineNow.getMachine().getId());
			last = nettyMapper.getLastNettyByXpg(xpg.getName());
			boolean flag = true;
			String[] currents = last.getCurrents().split(",");
			for (String s : currents) {
				if(Double.valueOf(s)<maxA) {
					flag = false;
					break;
				}
			}
			if(flag) {
				MessageData messageData = new MessageData();
				messageData.setSendId(0);
				Integer leader = deptMapper.selectDeptById(machineNow.getMachine().getDept().getId()).getLeader();
				messageData.setAccpetId(leader);
				messageData.setContext(String.valueOf(machineNow.getMachine().getId()));
				try {
					messageDataService.sendMessage(messageData, 2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
				nettyServerHandler.controlMachine(xpg.getName(), false);
				System.out.println(machineNow.getMachine().getId()+"号焊机已超限");
			}else {
				boolean flag2 = true;
				for (String s : currents) {
					if(Double.valueOf(s)>minA) {
						flag2 = false;
						break;
					}
				}
				if(flag2) {
					// 获取定时关机设定时间
					List<Energy> energyList = energyMapper.selectEnergyList();
					Integer time = energyList.get(0).getTime();
					Netty pre = nettyMapper.selectNettyByXpgAndTime(xpg.getName(), time);
					if(last.getCreateTime().getTime()-pre.getCreateTime().getTime()>=(time-1)*60*1000) {
						String[] currents2 = pre.getCurrents().split(",");
						boolean flag3 = true;
						for (String s : currents2) {
							if(Double.valueOf(s)>minA) {
								flag3 = false;
								break;
							}
						}
						if(flag3) {
							List<Netty> lists = nettyMapper.selectAllNettyByXpgAndTime(xpg.getName(), time);
							if(lists.size()>=(time-1)) {
								boolean flag4 = true;
								outer:
								for (Netty n : lists) {
									String[] currents3 = n.getCurrents().split(",");
									for (String s : currents3) {
										if(Double.valueOf(s)>minA) {
											flag4 = false;
											break outer;
										}
									}
								}
								if(flag4) {
									System.out.println(machineNow.getMachine().getId() + "号焊机已自动关机");
									machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
									nettyServerHandler.controlMachine(xpg.getName(), false);
								}
							}
						}
					}
				}
			}
			
		}
	}
}
