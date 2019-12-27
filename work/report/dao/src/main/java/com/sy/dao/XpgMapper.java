package com.sy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sy.entity.Xpg;

public interface XpgMapper {

	Xpg selectXpgById(Integer id);
	
	Xpg selectXpgByName(String name);

	int insertXpg(Xpg xpg);

	List<Xpg> selectXpgList(Xpg xpg);
	
	int updateXpg(Xpg xpg);
	
	Xpg selectXpgByMachineId(Integer machineId);

}
