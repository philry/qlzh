package com.sy.dao;

import com.sy.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeptDao extends JpaRepository<Dept,Integer> {

    Dept getById(int id);

    @Query("update Dept d set d.pid = ?2 where d.id = ?1 ")
    @Modifying
    int updatePid(Integer id,Integer pid);

    @Query("select d.id from Dept d where d.pid = ?1")
    List<Integer> getIdByPid(int pid);

}
