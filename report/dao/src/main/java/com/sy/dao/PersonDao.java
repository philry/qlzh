package com.sy.dao;

import com.sy.entity.Dept;
import com.sy.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PersonDao extends JpaRepository<Person,Integer>, JpaSpecificationExecutor {

    Person getById(Integer id);

    @Query("select p.id from Person p where p.name = ?1")
    Integer getIdByName(String name);

    @Query(value = "select p.* from person p where p.name = ?1",nativeQuery = true)
    Person getByName(String name);

    @Query("select p.id from Person p where p.dept.id = ?1")
    List<Integer> getPersonIdByDeptId(int deptId);

    @Query("select p.pileCounts from Person p where p.id = ?1")
    Integer getPileCounts (int personId);

}
