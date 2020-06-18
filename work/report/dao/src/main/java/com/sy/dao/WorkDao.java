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
import java.util.List;

@Repository
public interface WorkDao extends JpaRepository<Work,Integer>, JpaSpecificationExecutor {

    Page<Work> getAllByPerson(Person person, Pageable pageable);


    @Query(value =" select * from work where create_time <= :s_time and machine_id = :machineId  ORDER BY create_time desc limit 1",nativeQuery = true)
    Work getLastWorkByTime(@Param("s_time")String s_time,@Param("machineId")Integer machineId);

    @Query(value = "select distinct person_id from work where date(create_time) = ?1",nativeQuery = true)
    List<Integer> getPersonIdsByDate(String time);

    @Query(value = "select distinct person_id from work where date(create_time) = ?1 and person_id in ?2",nativeQuery = true)
    List<Integer> getPersonIdsByDateAndPersonids(String today,List<Integer> personids);

    @Query(value = "select distinct task_id from work where date(create_time)=?1 ",nativeQuery = true)
    List<Integer> getTaskIds(String b_time);

    @Query(value = "select distinct machine_id from work where date(create_time)=?1 ",nativeQuery = true)
    List<Integer> getMachineId(String b_time);

    @Query(value = "select * from work where machine_id=?1 ",nativeQuery = true)
    List<Work> getWorkByMachineId(int machineId);

    @Query(value = "select task_id from work where person_id=?1 and machine_id=?2 and operate=0 order by create_time desc limit 1 ",nativeQuery = true)
    Integer selectTaskIdByPersonAndMachine(Integer personId, Integer machineId);
}
