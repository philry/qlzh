package com.sy.controller;


import com.sy.core.netty.tcp.NettyServerHandler;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("control")
public class ControlController {

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @RequestMapping(value = "open",method = RequestMethod.POST)
    public JsonResult openMachine(){

        nettyServerHandler.controlMachine("11911273590002",true);

        return JsonResult.buildSuccess(200,"操作成功");
    }

    @RequestMapping(value = "close",method = RequestMethod.POST)
    public JsonResult closeMachine(){

        nettyServerHandler.controlMachine("11911273590002",false);

        return JsonResult.buildSuccess(200,"操作成功");
    }

}
