package com.sy.dao;


import com.sy.entity.Person;
import com.sy.entity.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface WorkDao extends JpaRepository<Work,Integer>, JpaSpecificationExecutor {

    Page<Work> getAllByPerson(Person person, Pageable pageable);


    @Query(value =" select * from work where create_time <= :s_time and machine_id = :machineId  ORDER BY create_time limit 1",nativeQuery = true)
    Work getLastWorkByTime(@Param("s_time")String s_time,@Param("machineId")Integer machineId);

}
