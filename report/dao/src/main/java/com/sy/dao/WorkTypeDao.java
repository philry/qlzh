package com.sy.dao;

import com.sy.entity.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkTypeDao extends JpaRepository<WorkType,Integer> {

    @Query("select w from WorkType w where w.id=?1")
    List<WorkType> getByid(Integer id);

    @Query("select w from WorkType w where w.name=?1")
    WorkType getByName(String name);

    @Query("select w from WorkType w")
    List<WorkType> getAll();

    @Query(value = "update WorkType w set w.name = ?2 where w.id = ?1 ")
    @Modifying
    int update(Integer id,String name);
}
