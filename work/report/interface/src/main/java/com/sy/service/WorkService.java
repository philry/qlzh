package com.sy.service;

import com.sy.entity.Work;
import org.springframework.data.domain.Page;

import java.util.Date;

public interface WorkService {

    Work startWork(Work work,Integer personId,Integer taskId,Integer machineId);

    Page<Work> getAllWork(Integer page, Integer pageSize, Integer personId, Date beginTime,Date endTime);

}
