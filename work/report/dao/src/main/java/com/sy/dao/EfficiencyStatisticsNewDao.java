package com.sy.dao;

import com.sy.entity.EfficiencyStatistics;
import com.sy.entity.EfficiencyStatisticsNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface EfficiencyStatisticsNewDao extends JpaRepository<EfficiencyStatisticsNew,Integer>, JpaSpecificationExecutor {

    EfficiencyStatistics getById(int id);

    //这种写法表能直接写表名
    @Query(value="delete from efficiency_statistics_new where  date = ?1",nativeQuery = true)
    @Modifying
    int deleteByDate(Date time);

    //这种写法表只能用实体类EfficiencyStatistics来表示
    @Query("select e.name from EfficiencyStatisticsNew e where  e.taskId = ?1")
    String getNameByTaskId(int taskId);

}
