package com.sy.dao;

import com.sy.entity.Netty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface NettyDao extends JpaRepository<Netty,Integer>, JpaSpecificationExecutor {


    List<Netty> getAllByCreateTimeBetween(Date beginTime,Date endTime);
    
    @Query(value = "select * from netty  where xpg = ?1 ORDER BY create_time desc",nativeQuery = true)
    List<Netty> getNettyByXpg(String xpg);

}
