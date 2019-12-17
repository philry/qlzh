package com.sy.service;

import com.sy.entity.EfficiencyStatistics;
import com.sy.vo.Unit;

import java.util.Date;
import java.util.List;

public interface EfficiencyStatisticsService {

    List<Unit> getAllData(String taskName, Date beginTime, Date endTime) throws Exception;

}
