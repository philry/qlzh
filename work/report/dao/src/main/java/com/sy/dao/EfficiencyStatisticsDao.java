package com.sy.dao;

import com.sy.entity.EfficiencyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EfficiencyStatisticsDao extends JpaRepository<EfficiencyStatistics,Integer> {
}
