package com.sy.service.impl;

import com.sy.dao.*;
import com.sy.entity.Engineering;
import com.sy.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SystemServiceImpl implements SystemService {

    //导入所有数据相关表的数据库接口,删除所有数据

    @Autowired //工程表
    private EfficiencyStatisticsDao efficiencyStatisticsDao;

    @Autowired //工效表
    private EngineeringDao engineeringDao;

    @Autowired //设备使用表
    private MachineUseDao machineUseDao;

    @Autowired //数据同步表
    private NettyDao nettyDao;

    @Autowired //人员工效表
    private PersonEfficiencyDao personEfficiencyDao;

    @Autowired //人员上工表
    private WorkDao workDao;

    @Autowired //数据处理表
    private DataManageDao dataManageDao;

    @Override
    public void resetData() {

        efficiencyStatisticsDao.deleteAll();
        engineeringDao.deleteAll();
        machineUseDao.deleteAll();
        nettyDao.deleteAll();
        personEfficiencyDao.deleteAll();
        workDao.deleteAll();
        dataManageDao.deleteAll();

    }

}
