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

    /*//这种写法表能直接写表名
    @Query(value="delete from efficiency_statistics_new where  date = ?1",nativeQuery = true)
    @Modifying*/  //标准名称的方法可以不用写sql，Spring Data JPA框架会按照标准名称自动生成对应sql
    int deleteByDate(Date time);

    //这种写法表只能用实体类EfficiencyStatisticsNew来表示
    @Query("select distinct e.name from EfficiencyStatisticsNew e where  e.taskId = ?1")
    String getNameByTaskId(int taskId);

}
