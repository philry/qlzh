package com.sy.dao;

import com.sy.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineDao extends JpaRepository<Machine,Integer> {

    Machine getById(Integer id);

}
