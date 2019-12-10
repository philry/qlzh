package com.sy.controller;


import com.sy.constant.HttpStatusConstant;
import com.sy.service.PersonEfficiencyService;
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
    public PageJsonResult getAllData(Integer page,Integer pageSize){

        return PageJsonResult.buildSuccessPage(HttpStatusConstant.SUCCESS,personEfficiencyService.initAllData(page,pageSize));
    }

}
