package com.sy.controller;

import com.sy.entity.CompanyInfo;
import com.sy.service.CompanyInfoService;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("company")
public class CompanyInfoController {


    @Autowired
    private CompanyInfoService companyInfoService;

    @RequestMapping(value = "info",method = RequestMethod.GET)
    public JsonResult getCompanyInfo(String name){
        Map<String,Object> map = new HashMap<>();
        try {
            CompanyInfo companyInfo = companyInfoService.getIpByName(name);
            map.put("companyName",name);
            map.put("ip",companyInfo.getServerIp());
            map.put("port",companyInfo.getServerPort());
            return JsonResult.buildSuccess(200,map);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }
    }

}
