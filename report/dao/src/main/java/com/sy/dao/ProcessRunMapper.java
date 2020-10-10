package com.sy.dao;

import com.sy.entity.ProcessRun;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessRunMapper 
{

    public ProcessRun selectProcessRunById(Long id);

    public List<ProcessRun> selectProcessRunList(ProcessRun processRun);

    public int insertProcessRun(ProcessRun processRun);

    public int updateProcessRun(ProcessRun processRun);

    public int deleteProcessRunById(Long id);

    public int deleteProcessRunByIds(String[] ids);

    List<String> getNeedRunRemark();

    ProcessRun getOperationStep(String remark);

    int updateResult(@Param("id")int id,@Param("result")String result);

    List<Integer> selectOperationById(int id);
}
