package com.sy.dao;

import com.sy.entity.MachineNow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineNowDao extends JpaRepository<MachineNow,Integer> {


    @Query("delete from MachineNow m where m.person.id=?1 and m.machine.id = ?2")
    @Modifying
    int deleteByPersonAndMachine(Integer personId,Integer machineId);

}
