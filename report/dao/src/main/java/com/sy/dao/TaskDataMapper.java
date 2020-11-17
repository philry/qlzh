package com.sy.dao;

import com.sy.entity.Task;
import com.sy.entity.TaskData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskDataMapper {

	int insertTaskData(TaskData taskData);

}
