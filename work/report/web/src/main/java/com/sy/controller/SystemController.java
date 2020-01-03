package com.sy.controller;


import com.sy.service.SystemService;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sys")
public class SystemController {

    @Autowired
    private SystemService systemService;

    @RequestMapping(value = "reset",method = RequestMethod.GET)
    public JsonResult resetData(){

        systemService.resetData();

        return JsonResult.buildSuccess(200,"操作成功");
    }

}
