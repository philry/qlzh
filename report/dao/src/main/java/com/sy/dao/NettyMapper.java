package com.sy.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sy.entity.Netty;

public interface NettyMapper {

	Netty selectNettyByXpgAndTime(@Param("xpg")String xpg,@Param("time")Integer time);
	
	List<Netty> selectAllXpg();
	
	Netty getLastNettyByXpg(String xpg);

	Netty getLastNettyByXpgAndOpenTime(String xpg, Date openTime);
	
	List<Netty> selectAllNettyByXpgAndTime(@Param("xpg")String xpg,@Param("time")Integer time);
}
