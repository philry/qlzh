package com.sy.service;

import com.sy.entity.EfficiencyStatistics;
import com.sy.vo.EfficiencyStatisticsVo;
import com.sy.vo.Unit;

import java.util.Date;
import java.util.List;

public interface EfficiencyStatisticsService {

    List<EfficiencyStatisticsVo> getAllData(String taskName, Date beginTime, Date endTime) throws Exception;

}
