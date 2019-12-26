package com.sy.dao;

import org.apache.ibatis.annotations.Delete;

public interface MachineNowMapper {

	@Delete("delete from machine_now where machine_id = #{machineId}")
	int deleteMachineNowByMachineId(Integer machineId);
}
