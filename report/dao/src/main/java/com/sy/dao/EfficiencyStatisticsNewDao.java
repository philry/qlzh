package com.sy.dao;

import com.sy.entity.EfficiencyStatisticsNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EfficiencyStatisticsNewDao extends JpaRepository<EfficiencyStatisticsNew,Integer>, JpaSpecificationExecutor {

    /*
    @Query(value="delete from efficiency_statistics_new where  date = ?1",nativeQuery = true)
    @Modifying*/
    int deleteByDate(Date time);


    @Query("select distinct e.name from EfficiencyStatisticsNew e where  e.taskId = ?1")
    String getNameByTaskId(int taskId);

}
