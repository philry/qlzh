package com.sy.controller;


import com.sy.dao.PersonDao;
import com.sy.dao.WorkDao;
import com.sy.entity.Person;
import com.sy.service.MachineNowService;
import com.sy.utils.DateUtils;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("index")
public class IndexController {


    @Autowired
    private MachineNowService machineNowService;

    @Autowired
    private WorkDao workDao;

    @Autowired
    private PersonDao personDao;

    @RequestMapping(value = "data",method = RequestMethod.GET)
    public JsonResult getData(){

        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);
        //在岗人数(查询work表,查询当日扫码的人员的id的个数)
        List<Integer> work_day = workDao.getPersonIdsByDate(today);
        int work_day_counts = work_day.size();
        //环比(查询work表,查询昨日扫码的人员的id的个数)
        int pre_work_day_counts = workDao.getPersonIdsByDate(day).size();
        //总人数(根据人员表获取总人数)
        int person_counts = personDao.findAll().size();
        //今日工人工效(调用工效查询接口，直接获取最上级数据)
        //实时焊机数(查询machineNow表,获取个数)
        //实时焊机工作数(根据实时打开的焊机的xpg,获取最新的netty同步数据,根据电流判定是否在工具)

        //用电量(调用工程查询接口)

        //本月总用电量(调用工程查询接口)


        //耗能表(调用工程查询接口)

        //工效表(调用工效查询接口)

        //工程耗能(调用工程查询接口)

        //班组工效(调用工效查询接口)

        return null;
    }

}
