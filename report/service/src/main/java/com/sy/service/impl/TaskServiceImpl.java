package com.sy.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sy.constant.TaskStatus;
import com.sy.dao.*;
import com.sy.entity.*;
import com.sy.service.MessageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sy.service.TaskService;

@Service
@Transactional
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskMapper taskMapper;
	
	@Autowired
	private TaskDao taskDao;
	
	@Autowired
	private DeptMapper deptMapper;
	
	@Autowired
	private PersonMapper personMapper;

	@Autowired
	private MessageDataService messageDataService;

	@Autowired
	private MessageDataDao messageDataDao;

	@Autowired
	private WorkDao workDao;

	@Override
	public List<Task> selectTaskList(Task task) {
		List<Task> list = taskMapper.selectTaskList(task);
		setTask(list);
		return list;
	}

	@Override
	public Task selectTaskById(Integer id) {
		Task task = taskMapper.selectTaskById(id);
		setTask(task);
		Task task2 = new Task();
		task2.setPid(task.getId());
		List<Task> sTasks = taskMapper.selectTaskList(task2);
		if(sTasks!=null) {
			task.setsTasks(sTasks);
			setTask(sTasks);
		}
		return task;
	}

	@Override
	@Transactional
	public int insertTask(Task task) {
		Integer deptId = task.getDeptId();
		Dept dept  =deptMapper.selectDeptById(deptId);
		Integer leader  =dept.getLeader();
		String flag = dept.getFlag();

		/*if("0".equals(flag)&&personId!=leader){
			throw new RuntimeException("新建与审核为同一人时只有总经理才能新建最高级派工单");
		}*/
		task.setCreateTime(new Timestamp(new Date().getTime()));
		task.setUpdateTime(task.getCreateTime());

		task.setChecker(leader);
		if("0".equals(flag)){
			task.setCheckingStatus("0");
		}
		return taskMapper.insertTask(task);
	}

	@Override
	@Transactional
	public void deleteTaskById(Integer id) {
		/*Task task = new Task();
		task.setPid(id);
		List<Task> sTask = taskMapper.selectTaskList(task);
		List<Work> work = workDao.getWorkByTaskId(id);
		if (sTask != null && sTask.size() > 0) {
			throw new RuntimeException("请先删除下级派工单");
		} else if(work != null && work.size() > 0){
			throw new RuntimeException("该派工单已经开工生产，无法删除");
		}else {
//			task.setId(id);
//			task.setStatus("1");
//			task.setPid(null);
//			return taskMapper.updateTask(task);
			taskMapper.deleteTaskById(id);
		}*/


		Task d2 = new Task();
		List<Task> sTasks = new ArrayList<>();
		d2.setPid(id);
		sTasks = taskMapper.selectTaskList(d2);
		if(sTasks!=null&sTasks.size()>0) {
			for (Task task1 : sTasks) {
				d2.setPid(task1.getId());
				List<Task> sTasks2 = taskMapper.selectTaskList(d2);
				if(sTasks2!=null&sTasks2.size()>0) {
					for (Task task2 : sTasks2) {
						d2.setPid(task2.getId());
						List<Task> sTasks3 = taskMapper.selectTaskList(d2);
						if(sTasks3!=null&&sTasks3.size()>0) {
							for(Task task3 : sTasks3){
								d2.setPid(task3.getId());
								List<Task> sTasks4 = taskMapper.selectTaskList(d2);
								if(sTasks4!=null&&sTasks4.size()>0){
									for(Task task4 : sTasks4){
										List<Work> work4 = workDao.getWorkByTaskId(task4.getId());
										if(work4 != null && work4.size() > 0){
											throw new RuntimeException("该派工单已经开工生产，无法删除");
										}
										task4.setStatus("1");

										messageDataService.sendMessageToPersonsByTask(task4);
										taskMapper.deleteTaskById(task4.getId());
									}
								}
								List<Work> work3 = workDao.getWorkByTaskId(task3.getId());
								if(work3 != null && work3.size() > 0){
									throw new RuntimeException("该派工单已经开工生产，无法删除");
								}
								task3.setStatus("1");

								messageDataService.sendMessageToPersonsByTask(task3);
								taskMapper.deleteTaskById(task3.getId());
							}
						}
						List<Work> work2 = workDao.getWorkByTaskId(task2.getId());
						if(work2 != null && work2.size() > 0){
							throw new RuntimeException("该派工单已经开工生产，无法删除");
						}
						task2.setStatus("1");

						messageDataService.sendMessageToPersonsByTask(task2);
						taskMapper.deleteTaskById(task2.getId());
					}
				}
				List<Work> work1 = workDao.getWorkByTaskId(task1.getId());
				if(work1 != null && work1.size() > 0){
					throw new RuntimeException("该派工单已经开工生产，无法删除");
				}
				task1.setStatus("1");

				messageDataService.sendMessageToPersonsByTask(task1);
				taskMapper.deleteTaskById(task1.getId());
			}
		}
		taskMapper.deleteTaskById(id);
	}

	@Override
	public int modifyTaskById(Task task, Integer id) {
		task.setUpdateTime(new Timestamp(new Date().getTime()));
		Integer pid = taskMapper.selectPidById(id);
		Task fTask = taskMapper.selectTaskById(pid);
		Double count = task.getCount();
		Task t1 = new Task();
		List<Task> taskList = taskMapper.selectTaskList(t1);
		Double totalCount = 0.0;
		Date startTime = fTask.getBeginTime();
		Date endTime = fTask.getEndTime();
		Date startTime1 = task.getBeginTime();
		Date endTime1 = task.getEndTime();
		if(startTime1.before(startTime)){
			throw new RuntimeException("开工时间超出了上级任务的开工时间，请重新修改开工时间");
		}
		if(endTime1.after(endTime)){
			throw new RuntimeException("完工时间超出了上级任务的完工时间，请重新修改完工时间");
		}
		if(fTask!=null){
			Integer id1 = fTask.getId();
			Double fcount = fTask.getCount();
			Double restCount = fcount;
			for(Task t2: taskList){
				if(t2.getPid().equals(id1) && t2.getId() != id){
					restCount = new BigDecimal(restCount).subtract(new BigDecimal(t2.getCount())).doubleValue();
				}
			}
			if(count.compareTo(restCount)>0){
				throw new RuntimeException("数量超出了上级任务的可分配数量，请重新修改数量");
			}

		}
		for(Task t2: taskList){
			if(t2.getPid().equals(id)){
				totalCount =  new BigDecimal(totalCount).add(new BigDecimal(t2.getCount())).doubleValue();
			}
		}
		if(count.compareTo(totalCount)<0){
			throw new RuntimeException("数量小于下级任务数量总和，请重新修改数量");
		}

		return taskMapper.updateTask(task);
	}

	@Override
	@Transactional
	public int stopTaskById(Integer id) {

        /*
        Task task = new Task();
        task.setPid(id);
        List<Task> sTask = taskMapper.selectTaskList(task);
        for(Task task2 : sTask){
            if (task2.getStatus() != "2") {
                throw new RuntimeException("请先终止下级派工单");
            }
        }
        task.setId(id);
        task.setStatus("2");
        task.setPid(null);
        return taskMapper.updateTask(task);*/



        Task d = taskMapper.selectTaskById(id);

		Task d2 = new Task();
		List<Task> sTasks = new ArrayList<>();
		d2.setPid(id);
		sTasks = taskMapper.selectTaskList(d2);
		if(sTasks!=null&sTasks.size()>0) {
			for (Task task : sTasks) {
				d2.setPid(task.getId());
				List<Task> sTasks2 = taskMapper.selectTaskList(d2);
				if(sTasks2!=null&sTasks2.size()>0) {
					for (Task task2 : sTasks2) {
						d2.setPid(task2.getId());
						List<Task> sTasks3 = taskMapper.selectTaskList(d2);
						if(sTasks3!=null&&sTasks3.size()>0) {
							for(Task task3 : sTasks3){
							    d2.setPid(task3.getId());
                                List<Task> sTasks4 = taskMapper.selectTaskList(d2);
                                if(sTasks4!=null&&sTasks4.size()>0){
                                    for(Task task4 : sTasks4){
                                        if(task4.getStatus() == "3"){
                                            throw new RuntimeException("任务已处于完工状态，不能被终止(0)");
                                        }
                                        task4.setStatus("2");
                                        taskMapper.updateTask(task4);


										messageDataService.sendMessageToPersonsByTask(task4);
                                    }
                                }
                                if(task3.getStatus() == "3"){
                                    throw new RuntimeException("任务已处于完工状态，不能被终止(1)");
                                }
								task3.setStatus("2");
								taskMapper.updateTask(task3);


								messageDataService.sendMessageToPersonsByTask(task3);
								}
							task2.setsTasks(sTasks3);
							}
                        if(task2.getStatus() == "3"){
                            throw new RuntimeException("任务已处于完工状态，不能被终止(2)");
                        }
						task2.setStatus("2");
						taskMapper.updateTask(task2);


						messageDataService.sendMessageToPersonsByTask(task2);
						}
					task.setsTasks(sTasks2);
					}
                if(task.getStatus() == "3"){
                    throw new RuntimeException("任务已处于完工状态，不能被终止(3)");
                }
				task.setStatus("2");
				taskMapper.updateTask(task);


				messageDataService.sendMessageToPersonsByTask(task);
				}
			d.setsTasks(sTasks);
			}
        /*if(d.getStatus() == "3"){
            throw new RuntimeException("任务已处于完工状态，不能被终止(4)");
        }
		d.setStatus("2");*/
		return taskMapper.updateTask(d);
	}



    @Override
    @Transactional
    public int endTaskById(Integer id) {


        Task d = taskMapper.selectTaskById(id);

        Task d2 = new Task();
        List<Task> sTasks = new ArrayList<>();
        d2.setPid(id);
        sTasks = taskMapper.selectTaskList(d2);
        if(sTasks!=null&sTasks.size()>0) {
            for (Task task : sTasks) {
                d2.setPid(task.getId());
                List<Task> sTasks2 = taskMapper.selectTaskList(d2);
                if(sTasks2!=null&sTasks2.size()>0) {
                    for (Task task2 : sTasks2) {
                        d2.setPid(task2.getId());
                        List<Task> sTasks3 = taskMapper.selectTaskList(d2);
                        if(sTasks3!=null&&sTasks3.size()>0) {
                            for(Task task3 : sTasks3){
                                d2.setPid(task3.getId());
                                List<Task> sTasks4 = taskMapper.selectTaskList(d2);
                                if(sTasks4!=null&&sTasks4.size()>0){
                                    for(Task task4 : sTasks4){
                                        if(task4.getStatus() == "2"){
                                            throw new RuntimeException("任务已处于终止状态，不能被完工(0)");
                                        }
                                        task4.setStatus("3");
                                        taskMapper.updateTask(task4);
										messageDataService.sendMessageToPersonsByTask(task4);
                                    }
                                }
                                if(task3.getStatus() == "2"){
                                    throw new RuntimeException("任务已处于终止状态，不能被完工(1)");
                                }
                                task3.setStatus("3");
                                taskMapper.updateTask(task3);
								messageDataService.sendMessageToPersonsByTask(task3);
                            }
                            task2.setsTasks(sTasks3);
                        }
                        if(task2.getStatus() == "2"){
                            throw new RuntimeException("任务已处于终止状态，不能被完工(2)");
                        }
                        task2.setStatus("3");
                        taskMapper.updateTask(task2);
						messageDataService.sendMessageToPersonsByTask(task2);
                    }
                    task.setsTasks(sTasks2);
                }
                if(task.getStatus() == "2"){
                    throw new RuntimeException("任务已处于终止状态，不能被完工(3)");
                }
                task.setStatus("3");
                taskMapper.updateTask(task);
				messageDataService.sendMessageToPersonsByTask(task);
            }
            d.setsTasks(sTasks);
        }
        if(d.getStatus() == "2"){
            throw new RuntimeException("任务已处于终止状态，不能被完工(4)");
        }
        d.setStatus("3");
        return taskMapper.updateTask(d);
    }

	@Override
	@Transactional
	public int unEndTaskById(Integer id) {


		Task d = taskMapper.selectTaskById(id);

		Task d2 = new Task();
		List<Task> sTasks = new ArrayList<>();
		d2.setPid(id);
		sTasks = taskMapper.selectTaskList(d2);
		if(sTasks!=null&sTasks.size()>0) {
			for (Task task : sTasks) {
				d2.setPid(task.getId());
				List<Task> sTasks2 = taskMapper.selectTaskList(d2);
				if(sTasks2!=null&sTasks2.size()>0) {
					for (Task task2 : sTasks2) {
						d2.setPid(task2.getId());
						List<Task> sTasks3 = taskMapper.selectTaskList(d2);
						if(sTasks3!=null&&sTasks3.size()>0) {
							for(Task task3 : sTasks3){
								d2.setPid(task3.getId());
								List<Task> sTasks4 = taskMapper.selectTaskList(d2);
								if(sTasks4!=null&&sTasks4.size()>0){
									for(Task task4 : sTasks4){
										/*if(task4.getStatus() == "3"){
											throw new RuntimeException("任务已处于终止状态，不能被反完工(0)");
										}*/
										task4.setStatus(TaskStatus.NORMAL);
										taskMapper.updateTask(task4);

										messageDataService.sendMessageToPersonsByTask(task4);
									}
								}
								/*if(task3.getStatus() == "3"){
									throw new RuntimeException("任务已处于终止状态，不能被完工(1)");
								}*/
								task3.setStatus("0");
								taskMapper.updateTask(task3);
								messageDataService.sendMessageToPersonsByTask(task3);
							}
							task2.setsTasks(sTasks3);
						}
						/*if(task2.getStatus() == "3"){
							throw new RuntimeException("任务已处于终止状态，不能被完工(0)");
						}*/
						task2.setStatus("0");
						taskMapper.updateTask(task2);
						messageDataService.sendMessageToPersonsByTask(task2);
					}
					task.setsTasks(sTasks2);
				}
				/*if(task.getStatus() == "3"){
					throw new RuntimeException("任务已处于终止状态，不能被完工(3)");
				}*/
				task.setStatus("0"); // task的status字段为0表示任务正常
				taskMapper.updateTask(task);
				messageDataService.sendMessageToPersonsByTask(task);
			}
			d.setsTasks(sTasks);
		}
		/*if(d.getStatus() == "3"){
			throw new RuntimeException("任务已处于终止状态，不能被完工(4)");
		}*/
		d.setStatus("0");
		return taskMapper.updateTask(d);
	}

    @Override
	@Transactional
	public int insertSonTask(Task task, Integer pid) {
		Integer deptId = task.getDeptId();
		Dept sonDept  =deptMapper.selectDeptById(deptId);
		Integer leader = sonDept.getLeader();
		Integer operator = sonDept.getOperator();
		String flag = sonDept.getFlag();

		/*if("0".equals(flag)&&personId!=leader){
			throw new RuntimeException("新建与审核为同一人时只有总经理才能新建最高级派工单");
		}*/
		task.setCreateTime(new Timestamp(new Date().getTime()));
		task.setUpdateTime(task.getCreateTime());

		task.setChecker(leader);
		if("0".equals(flag)){
			task.setCheckingStatus("0");
		}

		Task fTask = taskMapper.selectTaskById(pid);
		Integer id1 = fTask.getId();
		Double fcount = fTask.getCount();
		Double count  =task.getCount();
		Double restCount = fcount;
		Task t1 =new Task();
		t1.setStatus("0");
		List<Task> taskList = taskMapper.selectTaskList(t1);
		for(Task t2: taskList){
			if(t2.getPid().equals(id1)){
				restCount = new BigDecimal(restCount).subtract(new BigDecimal(t2.getCount())).doubleValue();
			}
		}
		if(count.compareTo(restCount)>0){
			throw new RuntimeException("数量超出了上级任务的可分配数量，请重新修改数量");
		}

		Dept dept = deptMapper.selectDeptById(fTask.getDeptId());
		if(dept!=null) {
			if(dept.getLevel()==4)
				throw new RuntimeException("不能再添加下级派工单了");
		}
		if ("1".equals(fTask.getCheckingStatus())) {
		//	throw new RuntimeException("派工单未审核");
            throw new RuntimeException("派工单未同意");
		} else {

			MessageData messageData = new MessageData();
			MessageType messageType = new MessageType(3);
			messageData.setAccpetId(leader);
			messageData.setContext("你接到新的任务："+task.getProjectName()+"，请下派该任务");
			messageData.setMessageType(messageType);
			messageData.setCreateTime(new Timestamp(new Date().getTime()));
			messageData.setUpdateTime(new java.sql.Date(new Date().getTime()));
			messageData.setStatus("0");
			messageDataDao.save(messageData);

			/*Task task2 = taskMapper.selectTaskByProjectName(task.getProjectName());
			if(task2!=null) {
				throw new RuntimeException("项目名称不能重复");
			}*/
			task.setWorkCode(fTask.getWorkCode());
		//	task.setProjectName(fTask.getProjectName());
            task.setProjectName(task.getProjectName());
			task.setPid(pid);
			task.setCreateTime(new Timestamp(new Date().getTime()));
			task.setUpdateTime(task.getCreateTime());
			task.setProcess(task.getProcess());
			return taskMapper.insertTask(task);
		}
	}

	@Override
	@Transactional
	public int splitTask(Task task, Integer id, String personIds) {
		int rows = 0;

		Task pTask = taskMapper.selectTaskById(id);

		Dept dept = deptMapper.selectDeptById(pTask.getDeptId());
		if(dept.getLevel()!=4) {
			throw new RuntimeException("该派工单不能被分解");
		}

		if("1".equals(pTask.getCheckingStatus())) {
		//	throw new RuntimeException("派工单未审核");
            throw new RuntimeException("派工单未同意");
		}
		/*Task task3 = taskMapper.selectTaskByProjectName(task.getProjectName());
		if(task3!=null) {
			throw new RuntimeException("项目名称不能重复");
		}*/
		String[] ids = personIds.split(",");
		task.setWorkCode(pTask.getWorkCode());
		task.setCount(pTask.getCount());
		task.setPid(id);
		task.setCheckingStatus("0");
		task.setDeptId(null);
		task.setCreateTime(new Timestamp(new Date().getTime()));
		task.setUpdateTime(task.getCreateTime());
		
		Task task2 = new Task();
		List<Task> list = new ArrayList<>();
		for(int i=0;i<ids.length;i++) {

			task2.setWorkCode(task.getWorkCode());
			task2.setProjectName(task.getProjectName());
			task2.setPersonId(Integer.valueOf(ids[i]));
			task2.setStatus("0");
			list = taskMapper.selectTaskList(task2);
			if(list==null||list.size()==0) {

				MessageData messageData = new MessageData();
				MessageType messageType = new MessageType(3);
				messageData.setAccpetId(Integer.valueOf(ids[i]));
				messageData.setContext("你接到新的任务："+task.getProjectName()+"，请完成该任务");
				messageData.setMessageType(messageType);
				messageData.setCreateTime(new Timestamp(new Date().getTime()));
				messageData.setUpdateTime(new java.sql.Date(new Date().getTime()));
				messageData.setStatus("0");
				messageDataDao.save(messageData);

				task.setPersonId(Integer.valueOf(ids[i]));
				rows+=taskMapper.insertTask(task);
			}
		}
		if(rows>0) {
			return rows;
		}else {
			throw new RuntimeException("不能重复分解派工单");
		}
	}
	
	private void setTask(Task task) {
		Person person = personMapper.selectPersonById(task.getChecker());
		if(person!=null)
			task.setPerson(person);
		Dept dept = deptMapper.selectDeptById(task.getDeptId());
		if(dept!=null)
			task.setDept(dept);
		Person workingPerson = personMapper.selectPersonById(task.getPersonId());
		if(workingPerson!=null)
		task.setWorkingPerson(workingPerson);
	}

	
	private void setTask(List<Task> list) {
		for (Task task : list) {
			setTask(task);
		}
	}

	@Override
	public int checkTask(Integer id,Integer type) {
		Task task = taskMapper.selectTaskById(id);
		if("1".equals(task.getCheckingStatus())) {
		//	throw new RuntimeException("派工单未审核!");
            throw new RuntimeException("派工单未同意");
		}else {
			Dept dept = deptMapper.selectDeptById(task.getDeptId());
			if(type==1) {
				if(dept.getLevel()==4) {
					throw new RuntimeException("不能再新建下级派工单了");
				}
			}else {
				if(dept.getLevel()!=4) {
					throw new RuntimeException("该派工单不能被分解");
				}
			}
		}
		
		return 1;
	}

	@Override
	@Transactional
	public int changeCheckStatus(Integer id, String type) {
		if("0".equals(type)) {
			Task task = taskMapper.selectTaskById(id);
			task.setCheckingStatus("0");
		//	return taskMapper.updateTask(task);
			int i = taskMapper.updateTask(task);
			Integer deptId =  task.getDeptId();
			Dept dept = deptMapper.selectDeptById(deptId);

			if(dept.getOperator() != null){
				MessageData messageData = new MessageData();
				MessageType messageType = new MessageType(3);

				messageData.setAccpetId(dept.getOperator());
				messageData.setContext("施工项目："+task.getProjectName()+"已审核完成");
				messageData.setMessageType(messageType);
				messageData.setCreateTime(new Timestamp(new Date().getTime()));
				messageData.setUpdateTime(new java.sql.Date(new Date().getTime()));
				messageData.setStatus("0");
				messageDataDao.save(messageData);
			}
			return i;
		}else {
			Task t = taskMapper.selectTaskById(id);
			System.out.println(t);
			if("0".equals(t.getStatus())) {
				//throw new RuntimeException("已审核过的派工单不能再被反审核");
                throw new RuntimeException("已同意过的派工单不能再被更改为不同意");
			}else {
				Task fTask = taskMapper.selectTaskById(t.getPid());
				if(fTask!=null) {
					fTask.setCheckingStatus("1");
					taskMapper.updateTask(fTask);
				}
				Task task = new Task();
				task.setId(id);
				task.setStatus("1");
				return taskMapper.updateTask(task);
			}
		}
	}

	@Override
	public List<Task> selectTaskByDeptIds(String workCode,List<Integer> deptIds) {
		List<Task> list = taskMapper.selectTaskByDeptIds(workCode,deptIds);
		setTask(list);
		return list;
	}

	@Override
	public List<Task> selectTaskLists(Task task) {

		List<Task> list = taskMapper.selectTaskLists(task);
		setTask(list);
		return list;
	}
}
