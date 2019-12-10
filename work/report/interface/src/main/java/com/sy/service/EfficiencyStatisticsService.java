package com.sy.service;

import com.sy.entity.EfficiencyStatistics;

import java.util.Date;
import java.util.List;

public interface EfficiencyStatisticsService {

    List<EfficiencyStatistics> getAllData(Date date);

}
