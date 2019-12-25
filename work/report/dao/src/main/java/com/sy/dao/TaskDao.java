package com.sy.dao;

import com.sy.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskDao extends JpaRepository<Task,Integer> {


    @Query("select t from Task t where t.pid=?1")
    List<Task> getByPid(Integer pid);

    @Query("select t from Task t where t.pid=?1 and t.deptId is null ")
    List<Task> getPersonBaseByPid(Integer pid);

    @Query("select t from Task t where t.pid=?1 and t.personId is null ")
    List<Task> getDeptBaseByPid(Integer pid);

    Task getById(Integer id);

    @Query("select t.id from Task t where t.projectName = ?1")
    Integer getIdByName(String name);


    @Query("select t from Task t where t.projectName = ?1")
    Task getByName(String name);

    @Query("select distinct t.pid from Task t where t.projectName=?1")
    int getPidByName(String name);

    @Query("select t.projectName from Task t where t.id = ?1")
    String getNameById(int id);

    @Query("select t.workCode from Task t where t.projectName = ?1")
    String getWorkNoByName(String name);

    @Query("select t.projectName from Task t where t.workCode=?1")
    String getNameByWorkNo(String workNo);

}
