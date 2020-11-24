package com.sy.dao;

import com.sy.entity.ScanWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScanWorkDao extends JpaRepository<ScanWork,Integer> {

    

}
