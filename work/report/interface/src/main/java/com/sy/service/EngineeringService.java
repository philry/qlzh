package com.sy.service;

import com.sy.entity.Engineering;
import com.sy.vo.EngineeringVo;
import com.sy.vo.Unit;

import java.util.Date;
import java.util.List;

public interface EngineeringService {

    List<Engineering> getAllData(Date beginTime,Date endTime);

    List<EngineeringVo> getInitData(Date beginTime, Date endTime);

    List<Engineering> getData(int pid,Date beginTime, Date endTime);

    List<Engineering> getDataByLevel(int level,Date beginTime, Date endTime);

}
