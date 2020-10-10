package com.sy.service.impl;


import cn.hutool.core.convert.Convert;
import com.sy.dao.ProcessRunMapper;
import com.sy.entity.ProcessRun;
import com.sy.service.IProcessRunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class ProcessRunServiceImpl implements IProcessRunService
{
    @Autowired
    private ProcessRunMapper processRunMapper;

    @Override
    public ProcessRun selectProcessRunById(Long id)
    {
        return processRunMapper.selectProcessRunById(id);
    }

    @Override
    public List<ProcessRun> selectProcessRunList(ProcessRun processRun)
    {
        return processRunMapper.selectProcessRunList(processRun);
    }

    @Override
    @Transactional
    public int insertProcessRun(ProcessRun processRun)
    {

        return processRunMapper.insertProcessRun(processRun);
    }


    @Override
    public int updateProcessRun(ProcessRun processRun)
    {
        return processRunMapper.updateProcessRun(processRun);
    }

    @Override
    public int deleteProcessRunByIds(String ids)
    {
        return processRunMapper.deleteProcessRunByIds(Convert.toStrArray(ids));
    }

    @Override
    public int deleteProcessRunById(Long id)
    {
        return processRunMapper.deleteProcessRunById(id);
    }

    @Override
    @Transactional
    public int updateResult(int id, String result,int resultType) {

        ProcessRun run = processRunMapper.selectProcessRunById((long) id);
        //TODO 此处需要调用消息接口对结果进行通知(从run中获取到对应的人员和部门数据)
        //结果类型 0 为同意和已完成  1 为拒绝和未完成
        if(resultType==0){
            return processRunMapper.updateResult(id,result);
        }else {
            //当结果为拒绝时，需要将相对应的remark的单据全部取消，以防止进行下一级任务的派发
            List<Integer> ids = processRunMapper.selectOperationById(id);
            int a = 0;
            for (Integer integer : ids) {
                processRunMapper.updateResult(integer,result);
                a++;
            }
            return a;
        }

    }
}
