package com.sy.dao;

import com.sy.entity.ProcessCondition;

import java.util.List;


public interface ProcessConditionMapper 
{

    public ProcessCondition selectProcessConditionById(Long id);


    public List<ProcessCondition> selectProcessConditionList(ProcessCondition processCondition);

    public int insertProcessCondition(ProcessCondition processCondition);

    public int updateProcessCondition(ProcessCondition processCondition);

    public int deleteProcessConditionById(Long id);

    public int deleteProcessConditionByIds(String[] ids);

    List<ProcessCondition> selectConditionByType(String tableType);
}
