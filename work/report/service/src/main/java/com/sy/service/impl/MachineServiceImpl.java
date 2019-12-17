package com.sy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.dao.DeptMapper;
import com.sy.dao.MachineMapper;
import com.sy.dao.MachineTypeMapper;
import com.sy.dao.XpgMapper;
import com.sy.entity.Dept;
import com.sy.entity.Machine;
import com.sy.entity.MachineType;
import com.sy.entity.Xpg;
import com.sy.service.MachineService;

@Service
public class MachineServiceImpl implements MachineService{

	@Autowired
	private MachineMapper machineMapper;
	
	@Autowired
	private MachineTypeMapper machineTypeMapper;
	
	@Autowired
	private DeptMapper deptMapper;
	
	@Autowired
	private XpgMapper xpgMapper;

	@Override
	public List<Machine> selectMachineList(Machine machine) {
		List<Machine> list = machineMapper.selectMachineList(machine);
		setMachine(list);
		return list;
	}

	@Override
	public int insertMachine(Machine machine) {
		Xpg xpg = new Xpg();
		xpg.setId(machine.getXpgId());
		xpg.setMachineId(machine.getId());
		xpgMapper.updateXpg(xpg);
		return machineMapper.insertMachine(machine);
	}

	@Override
	public int updateMachine(Machine machine) {
		return machineMapper.updateMachine(machine);
	}

	@Override
	public int removeMachineById(Integer id) {
		return machineMapper.deleteMachineById(id);
	}

	@Override
	public Machine selectMachineById(Integer id) {
		Machine machine = machineMapper.selectMachineById(id);
		setMachine(machine);
		return machine;
	}
	
	private void setMachine(List<Machine> list) {
		for (Machine machine : list) {
			setMachine(machine);
		}
	}
	
	private void setMachine(Machine machine) {
		Dept dept = deptMapper.selectDeptById(machine.getDeptId());
		if(dept!=null)
			machine.setDept(dept);
		MachineType machineType = machineTypeMapper.selectMachineTypeById(machine.getTypeId());
		if(machineType!=null) {
			machine.setMachineType(machineType);
		}
		Xpg xpg = xpgMapper.selectXpgById(machine.getXpgId());
		if(xpg!=null) {
			machine.setXpg(xpg);
		}
	}
}
