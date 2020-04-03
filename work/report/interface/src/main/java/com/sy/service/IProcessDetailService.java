package com.sy.service;


import com.sy.entity.ProcessDetail;

import java.util.List;

public interface IProcessDetailService 
{
    public ProcessDetail selectProcessDetailById(Long id);

    public List<ProcessDetail> selectProcessDetailList(ProcessDetail processDetail);

    public int insertProcessDetail(ProcessDetail processDetail);

    public int updateProcessDetail(ProcessDetail processDetail);

    public int deleteProcessDetailByIds(String ids);

    public int deleteProcessDetailById(Long id);

    int selectTableType(String tableType);
}
