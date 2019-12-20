package com.sy.controller;


import com.sy.entity.MachineNow;
import com.sy.service.MachineNowService;
import com.sy.service.WorkService;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("index")
public class IndexController {


    @Autowired
    private MachineNowService machineNowService;

    @Autowired
    private WorkService workService;

    @RequestMapping(value = "data",method = RequestMethod.GET)
    public JsonResult getData(){

        //在岗人数(查询work表,查询当日扫码的人员的id的个数)
        //环比(查询work表,查询昨日扫码的人员的id的个数)
        //总人数(根据人员表获取总人数)

        //今日工人工效(调用工效查询接口，直接获取最上级数据)
        //实时焊机数(查询machineNow表,获取个数)
        //实时焊机工作数(根据实时打开的焊机的xpg,获取最新的netty同步数据,根据电流判定是否在工具)

        //用电量(调用工程查询接口)




        return null;
    }

}