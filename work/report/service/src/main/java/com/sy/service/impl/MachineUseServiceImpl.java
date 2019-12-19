package com.sy.service.impl;

import com.sy.dao.MachineUseDao;
import com.sy.entity.MachineUse;
import com.sy.service.MachineUseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MachineUseServiceImpl implements MachineUseService {

    @Autowired
    private MachineUseDao machineUseDao;

    @Override
    public MachineUse startWork(int personId, int machineId) {

        MachineUse machineUse = new MachineUse();



        return null;
    }
}
