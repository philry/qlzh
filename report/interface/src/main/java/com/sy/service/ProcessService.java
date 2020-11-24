package com.sy.service;

import com.sy.entity.Process;

import java.util.List;

public interface ProcessService {
    List<Process> selectProcessList(Process process);

    Process selectProcessById(Integer id);

    int addProcess(Process process);

    int changeProcess(Process process);

    void deleteProcessById(Integer id);
}
