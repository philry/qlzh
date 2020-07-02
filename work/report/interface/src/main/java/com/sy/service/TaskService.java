package com.sy.service;

import java.util.List;

import com.sy.entity.Task;


public interface TaskService {

	List<Task> selectTaskList(Task task);

	Task selectTaskById(Integer id);

	int insertTask(Task task);

	void deleteTaskById(Integer id);

	int modifyTaskById(Task task, Integer id);

	int stopTaskById(Integer id);

	int endTaskById(Integer id);

	int unStoporEndTaskById(Integer id);

	int insertSonTask(Task task, Integer pid);

	int splitTask(Task task, Integer id, String personIds);

	int checkTask(Integer id, Integer type);

	int changeCheckStatus(Integer id, String type);

	List<Task> selectTaskByDeptIds(String string, List<Integer> deptIds);

	List<Task> selectTaskLists(Task task);

}
