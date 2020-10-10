package com.sy.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sy.dao.EnergyMapper;
import com.sy.entity.Energy;
import com.sy.service.EnergyService;

@Service
public class EnergyServiceImpl implements EnergyService {

	@Autowired
	private EnergyMapper energyMapper;

	@Override
	public List<Energy> getEnergyList() {
		return energyMapper.selectEnergyList();
	}

	@Override
	@Transactional
	public int updateEnergy(Energy energy) {
		energy.setUpdateTime(new Timestamp(new Date().getTime()));
		return energyMapper.updateEnergy(energy);
	}
}
