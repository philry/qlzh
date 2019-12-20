package com.sy.controller;


import com.sy.entity.EfficiencyStatistics;
import com.sy.service.EfficiencyStatisticsService;
import com.sy.utils.DateUtils;
import com.sy.vo.EfficiencyStatisticsVo;
import com.sy.vo.JsonResult;
import com.sy.vo.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("efficiencyStatistics")
public class EfficiencyStatisticsController {


    @Autowired
    private EfficiencyStatisticsService statisticsService;

    @RequestMapping(value = "info",method = RequestMethod.GET)
    public JsonResult getData(String taskName,String beginTime,String endTime){

        List<EfficiencyStatisticsVo> list = null;

        try {
            list = statisticsService.getAllData(taskName, beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }

        if(list==null){
            return JsonResult.buildFailure(404,"计算失败，稍后再试");
        }else {
            if(list.isEmpty()){
                return JsonResult.buildFailure(404,"无数据");
            }else {
                return JsonResult.buildSuccess(200,list);
            }
        }
    }



}
