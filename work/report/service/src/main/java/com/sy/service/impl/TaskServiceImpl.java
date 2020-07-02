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
		if("0".equals(flag)){//新建与审核为同一人时默认审核通过
			task.setCheckingStatus("0");//CheckingStatus为0表示审核通过
		}
		return taskMapper.insertTask(task);
	}

	@Override
	@Transactional
	public void deleteTaskById(Integer id) {
		/*Task task = new Task();
		task.setPid(id);
		List<Task> sTask = taskMapper.selectTaskList(task);
		List<Work> work = workDao.getWorkByTaskId(id);//上工表里有该任务说明被引用过了就不能删除
		if (sTask != null && sTask.size() > 0) {
			throw new RuntimeException("请先删除下级派工单");
		} else if(work != null && work.size() > 0){
			throw new RuntimeException("该派工单已经开工生产，无法删除");//第二种方法，可以状态改为1，名义上的删除
		}else {
//			task.setId(id);
//			task.setStatus("1");
//			task.setPid(null);
//			return taskMapper.updateTask(task);
			taskMapper.deleteTaskById(id);//建派工单时操作失误要删除，事实上的删除
		}*/

		//从上往下一键删
		Task d2 = new Task();
		List<Task> sTasks = new ArrayList<>();
		d2.setPid(id);
		sTasks = taskMapper.selectTaskList(d2);
		if(sTasks!=null&sTasks.size()>0) {
			for (Task task1 : sTasks) {//最高车间级任务
				d2.setPid(task1.getId());
				List<Task> sTasks2 = taskMapper.selectTaskList(d2);
				if(sTasks2!=null&sTasks2.size()>0) {
					for (Task task2 : sTasks2) {//最高工程队级任务
						d2.setPid(task2.getId());
						List<Task> sTasks3 = taskMapper.selectTaskList(d2);
						if(sTasks3!=null&&sTasks3.size()>0) {
							for(Task task3 : sTasks3){//最高班组级任务
								d2.setPid(task3.getId());
								List<Task> sTasks4 = taskMapper.selectTaskList(d2);
								if(sTasks4!=null&&sTasks4.size()>0){
									for(Task task4 : sTasks4){//最高焊工级任务
										List<Work> work4 = workDao.getWorkByTaskId(task4.getId());//上工表里有该任务说明被引用过了就不能删除
										if(work4 != null && work4.size() > 0){
											throw new RuntimeException("该派工单已经开工生产，无法删除");
										}
										taskMapper.deleteTaskById(task4.getId());//最高级到个人
									}
								}
								List<Work> work3 = workDao.getWorkByTaskId(task3.getId());
								if(work3 != null && work3.size() > 0){
									throw new RuntimeException("该派工单已经开工生产，无法删除");
								}
								taskMapper.deleteTaskById(task3.getId());//最高级到班组
							}
						}
						List<Work> work2 = workDao.getWorkByTaskId(task2.getId());
						if(work2 != null && work2.size() > 0){
							throw new RuntimeException("该派工单已经开工生产，无法删除");
						}
						taskMapper.deleteTaskById(task2.getId());//最高级到工程队
					}
				}
				List<Work> work1 = workDao.getWorkByTaskId(task1.getId());
				if(work1 != null && work1.size() > 0){
					throw new RuntimeException("该派工单已经开工生产，无法删除");
				}
				taskMapper.deleteTaskById(task1.getId());//最高级到车间
			}
		}
		taskMapper.deleteTaskById(id);//最高级到生产部
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
		Date startTime = fTask.getBeginTime();//上级任务开工时间
		Date endTime = fTask.getEndTime();//上级任务完工时间
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
				if(t2.getPid().equals(id1) && t2.getId() != id){ //上级任务数量减去所有的直接下级任务数量后的剩余数量，填的数量与剩余数量对比
					restCount = new BigDecimal(restCount).subtract(new BigDecimal(t2.getCount())).doubleValue();
				}
			}
			if(count.compareTo(restCount)>0){ //count比restCount大
				throw new RuntimeException("数量超出了上级任务的可分配数量，请重新修改数量");
			}

		}
		for(Task t2: taskList){
			if(t2.getPid().equals(id)){
				totalCount =  new BigDecimal(totalCount).add(new BigDecimal(t2.getCount())).doubleValue();
			}
		}
		if(count.compareTo(totalCount)<0){ //count比totalCount小
			throw new RuntimeException("数量小于下级任务数量总和，请重新修改数量");
		}

		return taskMapper.updateTask(task);
	}

	@Override
	@Transactional
	public int stopTaskById(Integer id) {

        /*//这段逻辑有问题（从下级往上级终止）
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


        // 从上级往下级终止，Status为2表示终止
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
                                        taskMapper.updateTask(task4);//最高级到个人

										//发送消息给派到或审核该任务的人
										messageDataService.sendMessageToPersonsByTask(task4);
                                    }
                                }
                                if(task3.getStatus() == "3"){
                                    throw new RuntimeException("任务已处于完工状态，不能被终止(1)");
                                }
								task3.setStatus("2");
								taskMapper.updateTask(task3);//最高级到班组

								//发送消息给派到或审核该任务的人
								messageDataService.sendMessageToPersonsByTask(task3);
								}
							task2.setsTasks(sTasks3);
							}
                        if(task2.getStatus() == "3"){
                            throw new RuntimeException("任务已处于完工状态，不能被终止(2)");
                        }
						task2.setStatus("2");
						taskMapper.updateTask(task2);//最高级到工程队

						//发送消息给派到或审核该任务的人
						messageDataService.sendMessageToPersonsByTask(task2);
						}
					task.setsTasks(sTasks2);
					}
                if(task.getStatus() == "3"){
                    throw new RuntimeException("任务已处于完工状态，不能被终止(3)");
                }
				task.setStatus("2");   //task的status字段为2表示任务停止
				taskMapper.updateTask(task);//最高级到车间

				//发送消息给派到或审核该任务的人
				messageDataService.sendMessageToPersonsByTask(task);
				}
			d.setsTasks(sTasks);
			}
        if(d.getStatus() == "3"){
            throw new RuntimeException("任务已处于完工状态，不能被终止(4)");
        }
//		d.setStatus("2"); //本身一级状态应产品要求不更改
		return taskMapper.updateTask(d);//最高级到生产部
	}



    @Override
    @Transactional
    public int endTaskById(Integer id) {

        // 从上级往下级完工，Status为3表示完工
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
                                        taskMapper.updateTask(task4);//最高级到个人
                                    }
                                }
                                if(task3.getStatus() == "2"){
                                    throw new RuntimeException("任务已处于终止状态，不能被完工(1)");
                                }
                                task3.setStatus("3");
                                taskMapper.updateTask(task3);//最高级到班组
                            }
                            task2.setsTasks(sTasks3);
                        }
                        if(task2.getStatus() == "2"){
                            throw new RuntimeException("任务已处于终止状态，不能被完工(2)");
                        }
                        task2.setStatus("3");
                        taskMapper.updateTask(task2);//最高级到工程队
                    }
                    task.setsTasks(sTasks2);
                }
                if(task.getStatus() == "2"){
                    throw new RuntimeException("任务已处于终止状态，不能被完工(3)");
                }
                task.setStatus("3");   //task的status字段为3表示任务完工
                taskMapper.updateTask(task);//最高级到车间
            }
            d.setsTasks(sTasks);
        }
        if(d.getStatus() == "2"){
            throw new RuntimeException("任务已处于终止状态，不能被完工(4)");
        }
        d.setStatus("3");
        return taskMapper.updateTask(d);//最高级到生产部
    }

	@Override
	@Transactional
	public int unStoporEndTaskById(Integer id) {

		// 从上级往下级反完工，Status为0表示正常
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
										task4.setStatus(TaskStatus.NORMAL);//0正常 1删除 2终止 3完工
										taskMapper.updateTask(task4);//最高级到个人
									}
								}
								/*if(task3.getStatus() == "3"){
									throw new RuntimeException("任务已处于终止状态，不能被完工(1)");
								}*/
								task3.setStatus("0");
								taskMapper.updateTask(task3);//最高级到班组
							}
							task2.setsTasks(sTasks3);
						}
						/*if(task2.getStatus() == "3"){
							throw new RuntimeException("任务已处于终止状态，不能被完工(0)");
						}*/
						task2.setStatus("0");
						taskMapper.updateTask(task2);//最高级到工程队
					}
					task.setsTasks(sTasks2);
				}
				/*if(task.getStatus() == "3"){
					throw new RuntimeException("任务已处于终止状态，不能被完工(3)");
				}*/
				task.setStatus("0"); // task的status字段为0表示任务正常
				taskMapper.updateTask(task);//最高级到车间
			}
			d.setsTasks(sTasks);
		}
		/*if(d.getStatus() == "3"){
			throw new RuntimeException("任务已处于终止状态，不能被完工(4)");
		}*/
		d.setStatus("0");
		return taskMapper.updateTask(d);//最高级到生产部
	}

    @Override
	@Transactional
	public int insertSonTask(Task task, Integer pid) {//下派任务
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
		if("0".equals(flag)){//新建与审核为同一人时默认审核通过
			task.setCheckingStatus("0");//CheckingStatus为0表示审核通过
		}

		Task fTask = taskMapper.selectTaskById(pid);
		Integer id1 = fTask.getId();
		Double fcount = fTask.getCount();
		Double count  =task.getCount();
		Double restCount = fcount;
		Task t1 =new Task();
		List<Task> taskList = taskMapper.selectTaskList(t1);
		for(Task t2: taskList){
			if(t2.getPid().equals(id1)){ //上级任务数量减去所有的直接下级任务数量后的剩余数量，填的数量这个数量对比
				restCount = new BigDecimal(restCount).subtract(new BigDecimal(t2.getCount())).doubleValue();
			}
		}
		if(count.compareTo(restCount)>0){ //count比restCount大
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
			//给任务下派部门的领导发送消息提醒
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
			task.setWorkCode(fTask.getWorkCode()); //工号是唯一的
		//	task.setProjectName(fTask.getProjectName());
            task.setProjectName(task.getProjectName());//项目名称不能重复
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
		// 上级派工单
		Task pTask = taskMapper.selectTaskById(id);
		// 判断是否是最下级的部门,不是则不能分解
		Dept dept = deptMapper.selectDeptById(pTask.getDeptId());
		if(dept.getLevel()!=4) {
			throw new RuntimeException("该派工单不能被分解");
		}
		// 判断派工单是否审核,未审核不能分解
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
			// 同工号同项目名称同员工不能重复插入
			task2.setWorkCode(task.getWorkCode());
			task2.setProjectName(task.getProjectName());
			task2.setPersonId(Integer.valueOf(ids[i]));
			list = taskMapper.selectTaskList(task2);
			if(list==null||list.size()==0) { //list里要是有数据表示派工单重复派给同一人,这里就跳过
				//给分到任务的焊工发送消息提醒
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
		}else { //rows为0表示派工单重复了
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
		task.setWorkingPerson(workingPerson);//任务分解给哪个焊工了
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
			task.setCheckingStatus("0"); //0同意
		//	return taskMapper.updateTask(task);
			int i = taskMapper.updateTask(task);
			Integer deptId =  task.getDeptId();
			Dept dept = deptMapper.selectDeptById(deptId);
			//给操作员发送消息提醒
			if(dept.getOperator() != null){ //审核和派工单操作员不是同一人
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
				task.setStatus("1"); //status为1表示删除，不同意的就把派工单设为删除状态
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
