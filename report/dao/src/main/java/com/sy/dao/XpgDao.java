package com.sy.dao;

import com.sy.entity.Xpg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface XpgDao extends JpaRepository<Xpg,Integer> {

    @Query("select x from Xpg x where x.name=?1 and status = 0")
//    @Query(value = "select * from xpg where name=?1 and status = 0",nativeQuery = true)
    Xpg getByName(String name);



}
