package com.sy.service;

import com.sy.entity.Engineering;
import com.sy.vo.Unit;

import java.util.Date;
import java.util.List;

public interface EngineeringService {

    List<Engineering> getAllData(Date beginTime,Date endTime);

    List<Engineering> getInitData(Date beginTime,Date endTime);

}
