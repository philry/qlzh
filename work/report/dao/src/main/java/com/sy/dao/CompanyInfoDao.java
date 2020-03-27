package com.sy.dao;

import com.sy.entity.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyInfoDao extends JpaRepository<CompanyInfo,Integer> {

    CompanyInfo getCompanyInfoByCompanyName(String name);

}
