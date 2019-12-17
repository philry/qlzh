package com.sy.quartz;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.sy.dao.MachineMapper;
import com.sy.entity.Machine;

@Component
public class MachineQuartz extends QuartzJobBean{
	
	@Autowired
	private MachineMapper machineMapper;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		List<Machine> list = machineMapper.selectMachineList(null);
		
	}

}
