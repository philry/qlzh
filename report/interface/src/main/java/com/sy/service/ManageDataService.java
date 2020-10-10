package com.sy.service;

import com.sy.entity.DataManage;
import com.sy.entity.Netty;

import java.util.Date;
import java.util.List;

public interface ManageDataService {

    List<DataManage> getAllByData(int personId,Date beginTime,Date endTime);

    List<DataManage> getAllData(Date beginTime,Date endTime);

    List<DataManage> getDataByWork(int workId,Date beginTime,Date endTime);

}
