package com.sy.service;

import com.sy.entity.Work;
import org.springframework.data.domain.Page;

import java.util.Date;

public interface WorkService {

    Work startWork(int personId, int taskId, int machineId) throws Exception;

    Page<Work> getAllWork(Integer page, Integer pageSize, String personName, Date beginTime,Date endTime) throws Exception;

    boolean endWork(int personId, int taskId, int machineId) throws Exception;
}
