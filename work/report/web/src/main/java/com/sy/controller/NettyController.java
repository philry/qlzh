package com.sy.controller;


import com.sy.entity.Netty;
import com.sy.service.NettyService;
import com.sy.vo.JsonResult;
import com.sy.vo.PageJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("netty")
public class NettyController {


    @Autowired
    private NettyService nettyService;


    @RequestMapping("select")
    public JsonResult getSelect(String xpg,int page,int pageSize){

        Page<Netty> pages =  nettyService.getAllByName(xpg,page,pageSize);

        List<Netty> list = pages.getContent();

        for (Netty netty : list) {
            List<String> strings = Arrays.asList(netty.getCurrents().split(","));
            netty.setList(strings);
        }

        return PageJsonResult.buildSuccessPage(200,pages);
    }

}
