package com.sy.service;

import com.sy.entity.Machine;
import com.sy.entity.MachineUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface MachineUseService {

    List<MachineUse> getDataByIdAndTime(int machineId,Date beginTime, Date endTime);

}
