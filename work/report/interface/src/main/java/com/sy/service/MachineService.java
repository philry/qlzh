package com.sy.service;

import java.util.List;

import com.sy.entity.Machine;
import com.sy.vo.JsonResult;

public interface MachineService {

	List<Machine> selectMachineList(Machine machine);

	int insertMachine(Machine machine) throws Exception;

	int updateMachine(Machine machine);

	int removeMachineById(Integer id);

	Machine selectMachineById(Integer id);

	List<Machine> selectMachineByIdAndName(Integer id ,String name);

	Machine selectMachineByXpgId(Integer xpgId);
}
