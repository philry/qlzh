package com.sy.controller;


import com.sy.service.NettyService;
import com.sy.vo.JsonResult;
import com.sy.vo.PageJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("netty")
public class NettyController {


    @Autowired
    private NettyService nettyService;


    @RequestMapping("select")
    public JsonResult getSelect(String xpg,int page,int pageSize){


        return PageJsonResult.buildSuccessPage(200,nettyService.getAllByName(xpg,page,pageSize));
    }

}
