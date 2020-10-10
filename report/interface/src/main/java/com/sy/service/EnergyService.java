package com.sy.service;

import java.util.List;

import com.sy.entity.Energy;

public interface EnergyService {

	List<Energy> getEnergyList();

	int updateEnergy(Energy energy);

}
