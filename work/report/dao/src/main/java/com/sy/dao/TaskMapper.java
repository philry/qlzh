package com.sy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sy.entity.Task;

public interface TaskMapper {

	List<Task> selectTaskList(Task task);

	Task selectTaskById(Integer id);

	int insertTask(Task task);

	int deleteTaskById(Integer id);

	int updateTask(Task task);

	List<Task> selectTaskByDeptIds(@Param("workCode")String workCode, @Param("list")List<Integer> list);

	Task selectTaskByProjectName(String projectName);

	List<Task> selectTaskLists(Task task);

	Integer selectPidById(Integer id);

}
