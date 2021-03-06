package com.sy.dao;

import com.sy.entity.Work;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkMapper {
    List<Work> getCloseWorkByCreateTime1(String time);

    Work getLastWorkByTime(@Param("createTime")String createTime, @Param("machineId")Integer machineId);
}
