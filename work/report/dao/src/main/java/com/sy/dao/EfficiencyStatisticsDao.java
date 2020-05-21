package com.sy.dao;

import com.sy.entity.EfficiencyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EfficiencyStatisticsDao extends JpaRepository<EfficiencyStatistics,Integer>, JpaSpecificationExecutor {

    EfficiencyStatistics getById(int id);

    @Query("delete from EfficiencyStatistics where  date = ?1")
    @Modifying
    int deleteByDate(Date time);



}
