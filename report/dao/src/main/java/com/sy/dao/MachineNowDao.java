package com.sy.dao;

import com.sy.entity.MachineNow;
import com.sy.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineNowDao extends JpaRepository<MachineNow,Integer> {

    @Query("delete from MachineNow m where m.person.id=?1 and m.machine.id = ?2")
    @Modifying
    int deleteByPersonAndMachine(Integer personId,Integer machineId);

    @Query(value = "select * from machine_now where person_id=?1",nativeQuery = true)
    List<MachineNow> getByPersonId(int personId);

    @Query(value = "select * from machine_now where machine_id=?1",nativeQuery = true)
    List<MachineNow> getDataByMachineId(int machineId);

    @Query(value = "delete from machine_now where machine_id=?1",nativeQuery = true)
    @Modifying
    int deleteByMachineId(int machineId);

}
