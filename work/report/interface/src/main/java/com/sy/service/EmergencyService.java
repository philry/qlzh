package com.sy.service;

import com.sy.entity.Emergency;

import java.util.List;

public interface EmergencyService {

	List<Emergency> getEmergencyList();

	int updateEmergency(Emergency emergency);

}
