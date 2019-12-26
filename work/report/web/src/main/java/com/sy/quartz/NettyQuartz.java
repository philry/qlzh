package com.sy.quartz;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.sy.core.netty.tcp.NettyServerHandler;
import com.sy.dao.EnergyMapper;
import com.sy.dao.MachineMapper;
import com.sy.dao.MachineNowMapper;
import com.sy.dao.NettyMapper;
import com.sy.dao.XpgMapper;
import com.sy.entity.Energy;
import com.sy.entity.Machine;
import com.sy.entity.Netty;
import com.sy.entity.Xpg;

@Component
public class NettyQuartz extends QuartzJobBean{
	
	private static NettyServerHandler nettyServerHandler = new NettyServerHandler();
	
	@Autowired
	private NettyMapper nettyMapper;
	
	@Autowired
	private EnergyMapper energyMapper;
	
	@Autowired
	private XpgMapper xpgMapper;
	
	@Autowired
	private MachineMapper machineMapper;
	
	@Autowired
	private MachineNowMapper machineNowMapper;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// 获取定时关机设定时间
		List<Energy> energyList = energyMapper.selectEnergyList();
		Integer time = energyList.get(0).getTime();
		// 查询netty表,根据xpg分组
		List<Netty> nettys = nettyMapper.selectAllNettyByTime(time+1);
		Xpg xpg = null;
		Machine machine = null;
		for (Netty netty : nettys) {
			// 获取这个xpg的最后一条记录
			Netty last = nettyMapper.getLastNettyByXpg(netty.getXpg());
			// 判断是否在工作
			boolean flag = true;
			xpg = xpgMapper.selectXpgByName(netty.getXpg());
			machine = machineMapper.selectMachineByXpgId(xpg.getId());
			String[] currents = last.getCurrents().split(",");
			for (String str : currents) {
				if(flag&Double.valueOf(str)>machine.getMinA()) {
					flag=false;
				}
			}
			if(flag) {
				if(last.getCreateTime().getTime()-netty.getCreateTime().getTime()>=(time-1)*60*1000) {
					boolean flag2 = true;
					String[] currents2 = netty.getCurrents().split(",");
					for (String str : currents2) {
						if(flag2&Double.valueOf(str)>machine.getMinA()) {
							flag2=false;
						}
					}
					if(flag2) {
						List<Netty> list = nettyMapper.selectNettyByXpgAndTime(xpg.getName(), time+1);
						boolean flag3 = true;
						outer:
						for(int i = 1; i<list.size()-1; i++) {
							String[] currents3 = list.get(i).getCurrents().split(",");
							for(int j = 0; j<currents3.length; j++) {
								if(Double.valueOf(currents3[j])>machine.getMinA()) {
									flag3 = false;
									break outer;
								}
							}
						}
						if(flag3) {
							nettyServerHandler.controlMachine(xpg.getName(), false);
							machineNowMapper.deleteMachineNowByMachineId(machine.getId());
						}
					}
				}
				
			}
		}
	}

}
