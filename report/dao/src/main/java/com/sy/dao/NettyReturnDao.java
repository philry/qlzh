package com.sy.dao;

import com.sy.entity.Netty;
import com.sy.entity.NettyReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NettyReturnDao extends JpaRepository<NettyReturn,Integer>, JpaSpecificationExecutor {

    List<NettyReturn> getAllByCreateTimeBetween(Date beginTime, Date endTime);
    
    @Query(value = "select * from netty_return  where xpg = ?1 ORDER BY create_time desc",nativeQuery = true)
    List<NettyReturn> getNettyByXpg(String xpg);

    @Query(value = "select distinct xpg from netty_return where create_time between ?1 and ?2",nativeQuery = true)
    List<String> findAllXpgs(Date beginTime, Date endTime);

    void deleteById(Integer id);

//    @Query(value = "delete from netty_return where date < ?1",nativeQuery = true)
    @Query(value = "delete from netty_return where create_time < ?1",nativeQuery = true)
    @Modifying()
    void deleteByDate(Date time);

    @Query(value = "select * from netty_return where xpg = ?1 and create_time >= ?2 ORDER BY create_time desc LIMIT 1",nativeQuery = true)
    NettyReturn getLastNettyByXpgAndOpenTime(String xpg, Date openTime);
}
