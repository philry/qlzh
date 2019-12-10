package com.sy.dao;

import com.sy.entity.Engineering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EngineeringDao extends JpaRepository<Engineering,Integer> {
}
