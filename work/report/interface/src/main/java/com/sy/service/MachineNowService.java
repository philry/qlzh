package com.sy.service;


import com.sy.entity.MachineNow;

import java.util.List;

public interface MachineNowService {

    MachineNow userMachine(MachineNow machineNow,Integer personId,Integer machineId);

    int closeMachine(Integer personId,Integer machineId);

    List<MachineNow> getNowMachine();

}
