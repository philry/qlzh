package com.sy.service;

import com.sy.entity.DataManage;
import com.sy.entity.Netty;

import java.util.Date;
import java.util.List;

public interface ManageDataService {

    List<DataManage> getAllByData(Date beginTime,Date endTime);

}
