package com.sy.service.impl;

import com.sy.constant.TaskStatus;
import com.sy.dao.*;
import com.sy.entity.*;
import com.sy.service.MessageDataService;
import com.sy.service.TaskDataService;
import com.sy.service.TaskService;
import com.sy.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class TaskDataServiceImpl implements TaskDataService {

	@Autowired
	TaskService taskService;

	@Autowired
	TaskDao taskDao;

	@Autowired
	TaskMapper taskMapper;

	@Autowired
	TaskDataMapper taskDataMapper;

	@Autowired
	WorkDao workDao;

	@Autowired
	DeptDao deptDao;

	@Autowired
	WorkMapper workMapper;

	@Autowired
	DataManageDao dataManageDao;

	@Autowired
	EfficiencyStatisticsNewDao efficiencyStatisticsNewDao;

	@Override
	public void insertTaskData(int id){
		Task task = taskService.selectTaskById(id);
		Dept dept = deptDao.getById(task.getDeptId());
		if(dept.getLevel() != 4){ //是班组级别的完工才往下运行
			return;
		}
		Timestamp timeStamp = new Timestamp(new Date().getTime());

		List<Integer> taskIds = new ArrayList<>();
		taskIds.add(id);
		List<Task> tasks0 = taskMapper.selectTaskByPid(id);
		for(Task task0 : tasks0){
			Integer taskId = task0.getId();
			taskIds.add(taskId);
		}
		Timestamp firstWorkDate = workMapper.getFirstWorkTimeByTaskIds(taskIds).getCreateTime();
		if(firstWorkDate == null){
		    throw new RuntimeException("该任务还未开始焊接");
        }
		String timeStampStr = String.valueOf(timeStamp);
		String firstWorkDateStr = String.valueOf(firstWorkDate);
		Double count = task.getCount();//派工单物量
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date beginDate;
		long day = 0;
		try {
			beginDate = format.parse(firstWorkDateStr);
			Date endDate= format.parse(timeStampStr);
			day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
			System.out.println("相隔的天数="+day);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<EfficiencyStatisticsNew> efficiencyStatisticsNews = efficiencyStatisticsNewDao.selectDataByTaskId(id);
		int time = 0;
		for(EfficiencyStatisticsNew efficiencyStatisticsNew : efficiencyStatisticsNews){
			time += efficiencyStatisticsNew.getTime();//该任务所有日期的所有焊工电焊时间总和
		}
		Double countPerSecond = count/time; //该任务每秒完成的物量

		//该任务所有派到的人员
		List<Integer> personIds = new ArrayList<>();
		List<Task> tasks = taskMapper.selectTaskByPid(id);
		for(Task task1 : tasks){
			Integer personId = task1.getPersonId();
			personIds.add(personId);
		}

		//按天分组
		Date now = new Date();
		String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
		//该任务统计数据按日期分组
		List<String> dayStrings = new ArrayList<>();
		int length = (int) day;
		String tempDay = today;
		dayStrings.add(tempDay);
		for (int i = 0; i <length-1 ; i++) {
			tempDay = DateUtils.getPrevDay(tempDay);
			dayStrings.add(tempDay);
		}
		for (String dayString : dayStrings) { // 按天分组
			int personTime = 0;
			for(Integer personId : personIds){ // 按人员分组
				List<DataManage> dataManages = dataManageDao.getByPersonIdAndDate(personId,dayString);//该任务每人每天的所有数据
				for(DataManage dataManage : dataManages){
					personTime += dataManage.getWorkingTime();//该人员每天该任务的焊接时间
				}
				TaskData taskData = new TaskData();
				taskData.setTaskId(id);
				taskData.setCount(countPerSecond*personTime);//每天该员工焊接的吨数
				taskData.setTime(personTime);
				taskData.setPersonId(personId);
				taskData.setProcess(task.getProcess());
				taskData.setDeptId(task.getDeptId());
				taskData.setDate(DateUtils.parseDate(dayString));
				taskData.setCreateTime(new Timestamp(new Date().getTime()));
				taskDataMapper.insertTaskData(taskData);
			}
		}
	}
}
