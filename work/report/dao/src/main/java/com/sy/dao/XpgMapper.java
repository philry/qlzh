package com.sy.dao;

import java.util.List;

import com.sy.entity.Xpg;

public interface XpgMapper {

	Xpg selectXpgById(Integer id);

	int insertXpg(Xpg xpg);

	List<Xpg> selectXpgList(Xpg xpg);
	
	int updateXpg(Xpg xpg);

}
