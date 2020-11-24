package com.sy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.dao.MachineTypeMapper;
import com.sy.entity.MachineType;
import com.sy.service.MachineTypeService;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MachineTypeServiceImpl implements MachineTypeService {

	@Autowired
	private MachineTypeMapper machineTypeMapper;

	@Override
	public List<MachineType> selectMachineTypeList(MachineType machineType) {
		return machineTypeMapper.selectMachineTypeList(machineType);
	}

	@Override
	@Transactional
	public int insertMachineType(MachineType machineType) {
		return machineTypeMapper.insertMachineType(machineType);
	}

	@Override
	@Transactional
	public int updateMachineType(MachineType machineType) {
		return machineTypeMapper.updateMachineType(machineType);
	}

	@Override
	@Transactional
	public int deleteMachineTypeById(Integer id) {
		return machineTypeMapper.deleteMachineTypeById(id);
	}

	@Override
	public MachineType selectMachineTypeById(Integer id) {
		return machineTypeMapper.selectMachineTypeById(id);
	}
}
