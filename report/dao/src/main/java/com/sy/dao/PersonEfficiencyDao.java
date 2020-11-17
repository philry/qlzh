package com.sy.dao;

import com.sy.entity.PersonEfficiency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PersonEfficiencyDao extends JpaRepository<PersonEfficiency,Integer>, JpaSpecificationExecutor {

    @Query("delete from PersonEfficiency ")
    @Modifying
    int deleteAllData();

    @Query("select p from PersonEfficiency p where p.deptId = ?1 ")
    List<PersonEfficiency> selectDeptData(Integer deptId);

    int deleteByDate(Date time);

    @Query(value = "select p from person_efficiency p where p.person_id in ?1 ",nativeQuery = true)
    List<PersonEfficiency> selectDataByPersonIds(List personIds);

    @Query("select p from PersonEfficiency p where p.personId = ?1 ")//查对象只能用这个查询方式，另一种value =前缀从数据库的表查的方式不行
    List<PersonEfficiency> selectDataByPersonId(Integer personId);


    @Query("select p from PersonEfficiency p where p.personId = ?1 and p.date between ?2 and ?3")//查对象只能用这个查询方式，另一种value =前缀从数据库的表查的方式不行
    List<PersonEfficiency> selectDataByPersonIdAndDate(Integer personId,Date beginTime,Date endTime);

}
