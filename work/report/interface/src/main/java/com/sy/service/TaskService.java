package com.sy.service;

import java.util.List;

import com.sy.entity.Task;


public interface TaskService {

	List<Task> selectTaskList(Task task);

	Task selectTaskById(Integer id);

	int insertTask(Task task);

	int deleteTaskById(Integer id);

	int insertSonTask(Task task, Integer pid);

	int splitTask(Task task, Integer id, String personIds);

	int checkTask(Integer id, Integer type);

}
