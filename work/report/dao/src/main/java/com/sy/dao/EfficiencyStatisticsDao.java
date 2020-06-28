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

    /*@Query("delete from EfficiencyStatistics where  date = ?1")
    @Modifying*/  //Spring Data JPA框架可以不用写sql，框架会按照标准名称自动生成对应sql
    int deleteByDate(Date time);

    @Query("select e.name from EfficiencyStatistics e where  e.taskId = ?1")
    String getNameByTaskId(int taskId);

}
