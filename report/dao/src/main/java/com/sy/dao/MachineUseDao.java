package com.sy.dao;

import com.sy.entity.MachineUse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineUseDao extends JpaRepository<MachineUse,Integer>, JpaSpecificationExecutor {

    int deleteByRemark(String date);

}
