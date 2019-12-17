package com.sy.controller;


import com.sy.constant.HttpStatusConstant;
import com.sy.service.PersonEfficiencyService;
import com.sy.utils.DateUtils;
import com.sy.vo.PageJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("personEfficiency")
public class PersonEfficiencyController {

    @Autowired
    private PersonEfficiencyService personEfficiencyService;


    @RequestMapping(value = "all",method = RequestMethod.GET)
    public PageJsonResult getAllData(String personName,String deptIds,Integer page,Integer pageSize,String beginTime,String endTime){

        try {
            personEfficiencyService.calculateData(personName,deptIds,beginTime ==null ?null:DateUtils.parseDate(beginTime),endTime ==null ?null:DateUtils.getNextDay(endTime));
        } catch (Exception e) {
            return PageJsonResult.buildFailurePage(404,e.getMessage());
        }


        return PageJsonResult.buildSuccessPage(HttpStatusConstant.SUCCESS,personEfficiencyService.initAllData(page,pageSize));
    }

}
