package com.sy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sy.entity.Netty;

public interface NettyMapper {

	//@Select("select * from (select * from (select * from netty ORDER BY create_time DESC LIMIT #{time})as t ORDER BY create_time) as t2 GROUP BY xpg")
	List<Netty> selectAllNettyByTime(Integer time);
	
	Netty getLastNettyByXpg(String xpg);
	
	List<Netty> selectNettyByXpgAndTime(@Param("xpg")String xpg,@Param("time")Integer time);
}
