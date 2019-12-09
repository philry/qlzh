package com.sy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.dao.MachineTypeMapper;
import com.sy.service.MachineTypeService;

@Service
public class MachineTypeServiceImpl implements MachineTypeService {

	@Autowired
	private MachineTypeMapper machineTypeMapper;
}
