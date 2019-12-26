package com.sy.quartz;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.sy.dao.DeptMapper;
import com.sy.dao.MachineMapper;
import com.sy.entity.Dept;
import com.sy.entity.Machine;
import com.sy.entity.MessageData;
import com.sy.service.MessageDataService;

@Component
public class MachineQuartz extends QuartzJobBean{
	
	public static int SERVICE_INTERVAL = 3;
	
	@Autowired
	private MachineMapper machineMapper;
	
	@Autowired
	private DeptMapper deptMapper;
	
	@Autowired
	private MessageDataService messageDataService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		// 先删除消息表中的数据
		messageDataService.deleteByStatus("1");
		messageDataService.deleteByStatus("0");
		
		List<Machine> list = machineMapper.selectMachineList(null);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 上次维护日期
		Date lastMaintenanceTime = null;
		// 下次维护日期
		Date nextMaintenanceTime = null;
		String date =null;
		String now = null;
		Calendar s = Calendar.getInstance();
		MessageData messageData = null;
		Dept dept = null;
		for (Machine machine : list) {
			// 如果没有上次维护日期,则上次维护日期就是购买日期
			if(machine.getLastMaintenanceTime()==null&&machine.getPayTime()==null) {
				break;
			}else {
				if(machine.getLastMaintenanceTime()!=null) {
					lastMaintenanceTime=machine.getLastMaintenanceTime();
				}else{
					lastMaintenanceTime = machine.getPayTime();
				}
				s.setTime(lastMaintenanceTime);
				// 计算下次维护日期
				s.add(Calendar.MONTH, SERVICE_INTERVAL);
				nextMaintenanceTime = s.getTime();
				
				date = format.format(nextMaintenanceTime);
				now = format.format(new Date());
				if(date.compareTo(now)<0) {
					messageData = new MessageData();
					messageData.setSendId(0);
					dept = deptMapper.selectDeptById(machine.getDeptId());
					messageData.setAccpetId(dept.getLeader());
					messageData.setContext(String.valueOf(machine.getId()));
					try {
						messageDataService.sendMessage(messageData, 1);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		}
	}

}
