package com.sy.dao;

import com.sy.entity.Xpg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XpgDao extends JpaRepository<Xpg,Integer> {

    Xpg getByName(String name);



}
