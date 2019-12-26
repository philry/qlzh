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
		System.out.println("开始查询netty表");
		List<Energy> energyList = energyMapper.selectEnergyList();
		Integer time = energyList.get(0).getTime();
		List<Netty> nettys = nettyMapper.selectAllNettyByTime(time+1);
		Xpg xpg = null;
		Machine machine = null;
		for (Netty netty : nettys) {
			xpg = xpgMapper.selectXpgByName(netty.getXpg());
			machine = machineMapper.selectMachineByXpgId(xpg.getId());
			Netty last = nettyMapper.getLastNettyByXpg(netty.getXpg());
			if(last.getCreateTime().getTime()-netty.getCreateTime().getTime()>=(time-1)*60) {
				boolean flag = true;
				String[] currents = netty.getCurrents().split(",");
				for (String str : currents) {
					if(flag&Double.valueOf(str)>machine.getMinA()) {
						flag=false;
					}
				}
				if(flag) {
					nettyServerHandler.controlMachine(xpg.getName(), false);
					machineNowMapper.deleteMachineNowByMachineId(machine.getId());
					System.out.println(machine.getId()+"号焊机已自动关机");
				}
			}
		}
	}

}
