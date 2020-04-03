package com.sy.service;

import com.sy.entity.ProcessCondition;
import com.sy.entity.ProcessDetail;

import java.util.List;

public interface IProcessService {

    void insertData(ProcessDetail detail, List<ProcessCondition> conditions);

}
