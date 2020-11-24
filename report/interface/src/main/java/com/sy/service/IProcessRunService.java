package com.sy.service;


import com.sy.entity.ProcessRun;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IProcessRunService 
{

    public ProcessRun selectProcessRunById(Long id);


    public List<ProcessRun> selectProcessRunList(ProcessRun processRun);


    public int insertProcessRun(ProcessRun processRun);

    public int updateProcessRun(ProcessRun processRun);

    public int deleteProcessRunByIds(String ids);

    public int deleteProcessRunById(Long id);

    int updateResult(int id,String result,int resultType);
}
