package com.sy.service.impl;

import cn.hutool.core.convert.Convert;
import com.sy.dao.ProcessConditionMapper;
import com.sy.entity.ProcessCondition;
import com.sy.service.IProcessConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProcessConditionServiceImpl implements IProcessConditionService
{
    @Autowired
    private ProcessConditionMapper processConditionMapper;

    @Override
    public ProcessCondition selectProcessConditionById(Long id)
    {
        return processConditionMapper.selectProcessConditionById(id);
    }


    @Override
    public List<ProcessCondition> selectProcessConditionList(ProcessCondition processCondition)
    {
        return processConditionMapper.selectProcessConditionList(processCondition);
    }


    @Override
    public int insertProcessCondition(ProcessCondition processCondition)
    {

        return processConditionMapper.insertProcessCondition(processCondition);
    }


    @Override
    public int updateProcessCondition(ProcessCondition processCondition)
    {

        return processConditionMapper.updateProcessCondition(processCondition);
    }


    @Override
    public int deleteProcessConditionByIds(String ids)
    {
        return processConditionMapper.deleteProcessConditionByIds(Convert.toStrArray(ids));
    }

    @Override
    public int deleteProcessConditionById(Long id)
    {
        return processConditionMapper.deleteProcessConditionById(id);
    }
}
