package com.sy.dao;

import java.util.List;
import java.util.Map;

import com.sy.entity.Machine;

public interface MachineMapper {

	List<Machine> selectMachineList(Machine machine);

	int insertMachine(Machine machine);

	int updateMachine(Machine machine);

	int deleteMachineById(Integer id);

	Machine selectMachineById(Integer id);
	
	Machine selectMachineByXpgId(Integer xpgId);

	Machine selectLastMachine();
}
