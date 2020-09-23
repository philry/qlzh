package com.sy.dao;

import com.sy.entity.Machine;
import com.sy.entity.MachineNow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MachineDao extends JpaRepository<Machine,Integer> {

    Machine getById(Integer id);

    @Query(value = "select name from machine where id=?1",nativeQuery = true)
    String getNameById(Integer id);
}
