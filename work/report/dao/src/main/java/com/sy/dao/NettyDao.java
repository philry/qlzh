package com.sy.dao;

import com.sy.entity.Netty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NettyDao extends JpaRepository<Netty,Integer> {



}
