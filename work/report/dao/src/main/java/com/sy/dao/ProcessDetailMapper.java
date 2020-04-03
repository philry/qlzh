package com.sy.dao;

import com.sy.entity.ProcessDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProcessDetailMapper 
{

    public ProcessDetail selectProcessDetailById(Long id);

    public List<ProcessDetail> selectProcessDetailList(ProcessDetail processDetail);

    public int insertProcessDetail(ProcessDetail processDetail);

    public int updateProcessDetail(ProcessDetail processDetail);

    public int deleteProcessDetailById(Long id);

    public int deleteProcessDetailByIds(String[] ids);

    int selectTableType(String tableType);

    List<ProcessDetail> selectDetailsByRemarkAndType(@Param("remark")long remark,@Param("tableType")String tableType);


}
