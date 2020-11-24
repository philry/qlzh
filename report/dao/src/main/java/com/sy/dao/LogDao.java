package com.sy.dao;

import com.sy.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LogDao extends JpaRepository<Log,Integer>, JpaSpecificationExecutor {



}
