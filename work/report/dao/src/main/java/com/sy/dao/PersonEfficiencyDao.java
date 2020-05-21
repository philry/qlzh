package com.sy.dao;

import com.sy.entity.PersonEfficiency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonEfficiencyDao extends JpaRepository<PersonEfficiency,Integer>, JpaSpecificationExecutor {

    @Query("delete from PersonEfficiency ")
    @Modifying
    int deleteAllData();


    @Query("select p from PersonEfficiency p where p.deptId = ?1 ")
    List<PersonEfficiency> selectDeptData(Integer deptId);
}
