package com.sy.service;

import java.util.List;

import com.sy.entity.Machine;

public interface MachineService {

	List<Machine> selectMachineList(Machine machine);

	int insertMachine(Machine machine) throws Exception;

	int updateMachine(Machine machine);

	int removeMachineById(Integer id);

	Machine selectMachineById(Integer id);

	
}
