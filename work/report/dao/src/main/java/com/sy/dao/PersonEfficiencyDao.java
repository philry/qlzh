package com.sy.dao;

import com.sy.entity.PersonEfficiency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonEfficiencyDao extends JpaRepository<PersonEfficiency,Integer>, JpaSpecificationExecutor {



}
