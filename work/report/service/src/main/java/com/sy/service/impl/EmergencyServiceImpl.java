package com.sy.service.impl;

import com.sy.dao.EmergencyMapper;
import com.sy.entity.Emergency;
import com.sy.service.EmergencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class EmergencyServiceImpl implements EmergencyService {

	@Autowired
	private EmergencyMapper emergencyMapper;

	@Override
	public List<Emergency> getEmergencyList() {
		return emergencyMapper.selectEmergencyList();
	}

	@Override
	@Transactional
	public int updateEmergency(Emergency emergency) {
		emergency.setUpdateTime(new Timestamp(new Date().getTime()));
		return emergencyMapper.updateEmergency(emergency);
	}
}
