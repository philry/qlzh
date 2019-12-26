package com.sy.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sy.dao.DeptMapper;
import com.sy.dao.PersonMapper;
import com.sy.dao.TaskDao;
import com.sy.dao.TaskMapper;
import com.sy.entity.Dept;
import com.sy.entity.Person;
import com.sy.entity.Task;
import com.sy.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskMapper taskMapper;
	
	@Autowired
	private TaskDao taskDao;
	
	@Autowired
	private DeptMapper deptMapper;
	
	@Autowired
	private PersonMapper personMapper;

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
	public int insertTask(Task task) {
		task.setCreateTime(new Timestamp(new Date().getTime()));
		task.setUpdateTime(task.getCreateTime());
		return taskMapper.insertTask(task);
	}

	@Override
	public int deleteTaskById(Integer id) {
		Task task = new Task();
		task.setPid(id);
		List<Task> sTask = taskMapper.selectTaskList(task);
		if (sTask != null && sTask.size() > 0) {
			throw new RuntimeException("请先删除下级派工单");
		} else {
			return taskMapper.deleteTaskById(id);
		}
	}

	@Override
	@Transactional
	public int insertSonTask(Task task, Integer pid) {
		Task fTask = taskMapper.selectTaskById(pid);
		Dept dept = deptMapper.selectDeptById(fTask.getDeptId());
		if(dept!=null) {
			if(dept.getLevel()==3)
				throw new RuntimeException("不能再添加下级派工单了");
		}
		if ("1".equals(fTask.getCheckingStatus())) {
			throw new RuntimeException("派工单未审核");
		} else {
			task.setWorkCode(fTask.getWorkCode());
			task.setProjectName(fTask.getProjectName());
			task.setPid(pid);
			task.setCreateTime(new Timestamp(new Date().getTime()));
			task.setUpdateTime(task.getCreateTime());
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
		if(dept.getLevel()!=3) {
			throw new RuntimeException("该派工单不能被分解");
		}
		// 判断派工单是否审核,未审核不能分解
		if("1".equals(pTask.getCheckingStatus())) {
			throw new RuntimeException("派工单未审核");
		}
		String[] ids = personIds.split(",");
		task.setWorkCode(pTask.getWorkCode());
		task.setProjectName(pTask.getProjectName());
		task.setCount(pTask.getCount());
		task.setPid(id);
		task.setCheckingStatus("0");
		task.setDeptId(null);
		task.setCreateTime(new Timestamp(new Date().getTime()));
		task.setUpdateTime(task.getCreateTime());
		
		Task task2 = new Task();
		List<Task> list = new ArrayList<>();
		for(int i=0;i<ids.length;i++) {
			// 同工号同员工不能重复插入
			task2.setWorkCode(task.getWorkCode());
			task2.setPersonId(Integer.valueOf(ids[i]));
			list = taskMapper.selectTaskList(task2);
			if(list==null||list.size()==0) {
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
			throw new RuntimeException("派工单未审核!");
		}else {
			Dept dept = deptMapper.selectDeptById(task.getDeptId());
			if(type==1) {
				if(dept.getLevel()==3) {
					throw new RuntimeException("不能再新建下级派工单了");
				}
			}else {
				if(dept.getLevel()!=3) {
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
			return taskMapper.updateTask(task);
		}else {
			Task t = taskMapper.selectTaskById(id);
			Task fTask = taskMapper.selectTaskById(t.getPid());
			if(fTask!=null) {
				fTask.setCheckingStatus("1");
				taskMapper.updateTask(fTask);
			}
			return taskMapper.deleteTaskById(id);
		}
	}

	@Override
	public List<Task> selectTaskByDeptIds(String workCode,List<Integer> deptIds) {
		List<Task> list = taskMapper.selectTaskByDeptIds(workCode,deptIds);
		setTask(list);
		return list;
	}
}
