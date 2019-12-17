package com.sy.dao;

import java.util.List;

import com.sy.entity.Task;

public interface TaskMapper {

	List<Task> selectTaskList(Task task);

	Task selectTaskById(Integer id);

	int insertTask(Task task);

	int deleteTaskById(Integer id);

}
