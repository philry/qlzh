package com.sy.service;

import com.sy.entity.CompanyInfo;

public interface CompanyInfoService {

    CompanyInfo getIpByName(String name) throws Exception;

}
