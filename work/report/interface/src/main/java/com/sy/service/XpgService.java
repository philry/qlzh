package com.sy.service;

import java.util.List;

import com.sy.entity.Machine;
import com.sy.entity.Xpg;

public interface XpgService {

	Xpg selectXpgById(Integer id);

	Xpg insertXpg(Xpg xpg);

	List<Xpg> selectXpgList(Xpg xpg);

	int updateXpg(Xpg xpg);

	Xpg selectXpgByMachineId(Integer machineId);

	int edit(Machine machine);
}
