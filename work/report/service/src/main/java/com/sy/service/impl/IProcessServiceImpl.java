package com.sy.service.impl;

import com.sy.dao.ProcessConditionMapper;
import com.sy.dao.ProcessDetailMapper;
import com.sy.entity.ProcessCondition;
import com.sy.entity.ProcessDetail;
import com.sy.service.IProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IProcessServiceImpl implements IProcessService {

    @Autowired
    private ProcessConditionMapper conditionMapper;

    @Autowired
    private ProcessDetailMapper detailMapper;

    @Override
    public void insertData(ProcessDetail detail, List<ProcessCondition> conditions) {

        detailMapper.insertProcessDetail(detail);

        for (ProcessCondition condition : conditions) {

            conditionMapper.insertProcessCondition(condition);

        }

    }
}
