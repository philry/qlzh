package com.sy.dao;

import com.sy.entity.Emergency;
import com.sy.entity.Emergency;

import java.util.List;

public interface EmergencyMapper {

	List<Emergency> selectEmergencyList();

	int updateEmergency(Emergency emergency);

}
