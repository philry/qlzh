package com.sy.dao;


import com.sy.entity.DataManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface DataManageDao extends JpaRepository<DataManage,Integer>, JpaSpecificationExecutor {

    int deleteByCreateTime(Date time);

    //标准名称的方法Spring Data JPA框架会自动生成对应sql
    int deleteByDate(Date time);

}
