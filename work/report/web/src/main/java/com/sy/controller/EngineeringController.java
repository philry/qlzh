package com.sy.controller;


import com.sy.entity.Engineering;
import com.sy.service.EngineeringService;
import com.sy.utils.DateUtils;
import com.sy.vo.EngineeringVo;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("engineering")
public class EngineeringController {

    @Autowired
    private EngineeringService engineeringService;

    @RequestMapping(value = "info",method = RequestMethod.GET)
    public JsonResult initData(String beginTime,String endTime){

        List<EngineeringVo> list = null;

        try{
            list = engineeringService.getInitData(beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime));
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }

        return JsonResult.buildSuccess(200,list);
    }


}
