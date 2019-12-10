package com.sy.service.impl;

import com.sy.dao.EfficiencyStatisticsDao;
import com.sy.entity.EfficiencyStatistics;
import com.sy.service.EfficiencyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class EfficiencyStatisticsServiceImpl implements EfficiencyStatisticsService {

    @Autowired
    private EfficiencyStatisticsDao efficiencyStatisticsDao;

    @Override
    public List<EfficiencyStatistics> getAllData(Date date) {

        return efficiencyStatisticsDao.findAll();
    }
}
