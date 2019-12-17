package com.sy.service;

import java.util.List;

import com.sy.entity.MachineType;

public interface MachineTypeService {

	List<MachineType> selectMachineTypeList(MachineType machineType);

	int insertMachineType(MachineType machineType);

	int updateMachineType(MachineType machineType);

	int deleteMachineTypeById(Integer id);

	MachineType selectMachineTypeById(Integer id);

}
