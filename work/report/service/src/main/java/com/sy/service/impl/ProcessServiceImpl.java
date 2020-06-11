package com.sy.service.impl;

import com.sy.dao.ProcessMapper;
import com.sy.entity.Process;
import com.sy.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    private ProcessMapper processMapper;

    @Override
    public List<Process> selectProcessList(Process process) {
        return processMapper.selectProcessList(process);
    }

    @Override
    public Process selectProcessById(Integer id) {
        return processMapper.selectProcessById(id);
    }

    @Override
    public int addProcess(Process process) {
        return processMapper.insertProcess(process);
    }

    @Override
    public int changeProcess(Process process) {
        processMapper.updateProcess(process);
        return 1;
    }

    @Override
    public void deleteProcessById(Integer id) {
        processMapper.deleteProcessById(id);
    }
}
