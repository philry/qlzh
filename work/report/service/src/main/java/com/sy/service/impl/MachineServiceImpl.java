package com.sy.service.impl;

import java.util.List;

import com.sy.dao.*;
import com.sy.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.service.MachineService;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public int insertMachine(Machine machine) throws Exception {
		Machine machine1 = machineMapper.selectMachineByXpgId(machine.getXpgId());
		if(machine1!=null){
			throw new Exception("该注册码已绑定其他焊机,请优先取消绑定或者修改注册码");
		}
		if(machineMapper.insertMachine(machine)>=0){
			Xpg xpg = new Xpg();
			xpg.setId(machine.getXpgId());
			xpg.setMachineId(machine.getId());
			xpgMapper.updateXpg(xpg);
		}else {
			return 0;
		}
		return 1;
	}

	@Override
	@Transactional
	public int updateMachine(Machine machine) {
		return machineMapper.updateMachine(machine);
	}

	@Override
	@Transactional
	public int removeMachineById(Integer id) {
		return machineMapper.deleteMachineById(id);
	}

	@Override
	public Machine selectMachineById(Integer id) {
		Machine machine = machineMapper.selectMachineById(id);
		setMachine(machine);
		return machine;
	}

	@Override
	public List<Machine> selectMachineByIdAndName(Integer id ,String name) {
		Machine machine = new Machine();
		if(id != null){
			machine.setId(id);
		}
		machine.setName(name);
		List<Machine> list = machineMapper.selectMachineByIdAndName(machine);
		setMachine(list);
		return list;
	}

	@Override
	public Machine selectMachineByXpgId(Integer xpgId) {
		return machineMapper.selectMachineByXpgId(xpgId);
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
