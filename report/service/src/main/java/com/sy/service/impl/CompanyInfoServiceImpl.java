package com.sy.service.impl;

import com.sy.dao.CompanyInfoDao;
import com.sy.entity.CompanyInfo;
import com.sy.service.CompanyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyInfoServiceImpl implements CompanyInfoService {

    @Autowired
    private CompanyInfoDao companyInfoDao;

    @Override
    public CompanyInfo getIpByName(String name) throws Exception {


        CompanyInfo companyInfo = companyInfoDao.getCompanyInfoByCompanyName(name);

        if(companyInfo==null){
            throw new Exception("公司不存在");
        }

        return companyInfo;
    }
}
