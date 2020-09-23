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

    @Query("select t.id from Task t where t.projectName = ?1 and pid = 0")
    Integer getIdByName(String name);


    @Query("select t from Task t where t.projectName = ?1")
    Task getByName(String name);

    @Query("select distinct t.pid from Task t where t.projectName=?1")
    int getPidByName(String name);

    @Query("select distinct t.pid from Task t where t.id=?1")
    Integer getPidById(Integer id);

    @Query("select distinct t.id from Task t where t.pid=?1")
    int getIdByPid(Integer pid);

    @Query("select t.id from Task t where t.pid=?1")
    List<Integer> getIdsByPid(Integer pid);

    @Query(value = "select id from task where person_id=?1 order by create_time desc",nativeQuery=true)
    List<Integer> getIdsByPersonIdDesc(Integer pid);

    @Query(value = "select id from task where pid=?1 order by create_time desc",nativeQuery=true)
    List<Integer> getIdsByPidDesc(Integer pid);

    @Query("select t.projectName from Task t where t.id = ?1")
    String getNameById(int id);

    @Query("select t.workCode from Task t where t.projectName = ?1 and pid = 0")
    String getWorkNoByName(String name);

    @Query("select t.workCode from Task t where t.id = ?1 and pid = 0")
    String getWorkNoById(Integer id);

    //这种写法写映设的实体类的名称和属性名
    @Query("select t.projectName from Task t where t.workCode=?1 and pid = 0")
    String getNameByWorkNo(String workNo);

    //这种写法直接写数据库的表名和数据库里的字段
    @Query(value = "select dept_id from task where id=?1",nativeQuery = true)
    Integer getDeptIdById(Integer id);

}
