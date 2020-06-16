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

    @Query(value = "update dept d set d.pid = ?2 where d.id = ?1 ",nativeQuery=true)
    @Modifying
    int updatePid(Integer id,Integer pid);

    @Query(value = "select d.id from dept d where d.pid = ?1",nativeQuery=true)
    List<Integer> getIdByPid(int pid);

    @Query(value = "select leader from dept where id = ?1",nativeQuery = true)
    Integer getLeaderById(int id);


}
