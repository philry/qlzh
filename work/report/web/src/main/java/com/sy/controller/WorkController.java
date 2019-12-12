package com.sy.controller;

import com.sy.constant.HttpStatusConstant;
import com.sy.entity.Work;
import com.sy.service.WorkService;
import com.sy.utils.DateUtils;
import com.sy.vo.PageJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("work")
public class WorkController {

    @Autowired
    private WorkService workService;


    @RequestMapping(value = "all",method = RequestMethod.GET)
    public PageJsonResult getAllWorks(Integer pageNum, Integer pageSize, Integer personId, String beginTime, String endTime){

        System.out.println(beginTime);
        System.out.println(endTime);

        Page<Work> workPages = workService.getAllWork(pageNum,pageSize,personId, DateUtils.parseDate(beginTime),DateUtils.getNextDay(endTime));

        return PageJsonResult.buildSuccessPage(HttpStatusConstant.SUCCESS,workPages);
    }

}
