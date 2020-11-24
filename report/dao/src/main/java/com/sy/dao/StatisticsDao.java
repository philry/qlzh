package com.sy.dao;

import com.sy.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface StatisticsDao extends JpaRepository<Statistics,Integer>, JpaSpecificationExecutor {

//    Statistics getById(int id);

    /*@Query("delete from Statistics where  date = ?1")
    @Modifying*/
    int deleteByDate(Date time);

    @Query("select e.name from Statistics e where  e.taskId = ?1")
    String getNameByTaskId(int taskId);

}
