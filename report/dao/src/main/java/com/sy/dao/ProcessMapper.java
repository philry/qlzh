package com.sy.dao;


import com.sy.entity.Process;

import java.util.List;

public interface ProcessMapper {
    List<Process> selectProcessList(Process process);

    Process selectProcessById(Integer id);

    int insertProcess(Process process);

    void updateProcess(Process process);

    void deleteProcessById(Integer id);
}
