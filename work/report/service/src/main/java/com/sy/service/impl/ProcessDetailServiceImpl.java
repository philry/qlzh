package com.sy.service.impl;


import cn.hutool.core.convert.Convert;
import com.sy.dao.ProcessDetailMapper;
import com.sy.entity.ProcessDetail;
import com.sy.service.IProcessDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ProcessDetailServiceImpl implements IProcessDetailService
{
    @Autowired
    private ProcessDetailMapper processDetailMapper;

    @Override
    public ProcessDetail selectProcessDetailById(Long id)
    {
        return processDetailMapper.selectProcessDetailById(id);
    }

    @Override
    public List<ProcessDetail> selectProcessDetailList(ProcessDetail processDetail)
    {
        return processDetailMapper.selectProcessDetailList(processDetail);
    }


    @Override
    @Transactional
    public int insertProcessDetail(ProcessDetail processDetail)
    {

        return processDetailMapper.insertProcessDetail(processDetail);
    }

    @Override
    @Transactional
    public int updateProcessDetail(ProcessDetail processDetail)
    {
        return processDetailMapper.updateProcessDetail(processDetail);
    }

    @Override
    public int deleteProcessDetailByIds(String ids)
    {
        return processDetailMapper.deleteProcessDetailByIds(Convert.toStrArray(ids));
    }

    @Override
    public int deleteProcessDetailById(Long id)
    {
        return processDetailMapper.deleteProcessDetailById(id);
    }

    @Override
    public int selectTableType(String tableType) {

        return processDetailMapper.selectTableType(tableType);
    }
}
