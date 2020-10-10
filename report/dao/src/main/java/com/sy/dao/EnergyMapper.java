package com.sy.dao;

import java.util.List;

import com.sy.entity.Energy;

public interface 	EnergyMapper {

	List<Energy> selectEnergyList();

	int updateEnergy(Energy energy);

}
