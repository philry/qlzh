package com.sy.service;


import com.sy.entity.ProcessCondition;

import java.util.List;

public interface IProcessConditionService 
{

    public ProcessCondition selectProcessConditionById(Long id);

    public List<ProcessCondition> selectProcessConditionList(ProcessCondition processCondition);

    public int insertProcessCondition(ProcessCondition processCondition);

    public int updateProcessCondition(ProcessCondition processCondition);

    public int deleteProcessConditionByIds(String ids);

    public int deleteProcessConditionById(Long id);
}
