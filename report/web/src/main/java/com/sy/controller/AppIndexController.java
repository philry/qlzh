package com.sy.controller;


import com.sy.dao.*;
import com.sy.entity.*;
import com.sy.service.EfficiencyStatisticsService;
import com.sy.service.EngineeringService;
import com.sy.service.MachineNowService;
import com.sy.service.NettyService;
import com.sy.utils.DateUtils;
import com.sy.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("appIndex")
public class AppIndexController {

    @Autowired
    private MachineNowDao machineNowDao;

    @Autowired
    private WorkDao workDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private EngineeringService engineeringService;

    @Autowired
    private EfficiencyStatisticsService statisticsService;

    @Autowired
    private DeptDao deptDao;

    @RequestMapping(value = "todayWorkCount",method = RequestMethod.GET)
    public AjaxResult getTodayWorkCount(Integer deptId){ //今日在岗人数(显示本级部门级及以下部门的扫码人员人数)

        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        List<Integer> personids = new ArrayList<>();

        //最高到生产部级
        List<Integer> personids0 = personDao.getPersonIdByDeptId(deptId);
        for(Integer personId : personids0){
            personids.add(personId);
        }

        //下级部门(最高到车间级)
        List<Integer> deptIds1 = deptDao.getIdByPid(deptId);
        for(Integer deptId1 : deptIds1){
            List<Integer> personids1 = personDao.getPersonIdByDeptId(deptId1);
            for(Integer personId : personids1){
                personids.add(personId);//最高到车间级的部门所有人员放入personids
            }

            //每个部门的下级部门(最高到工程队级)
            List<Integer> deptIds2 = deptDao.getIdByPid(deptId1);
            for(Integer deptId2 : deptIds2){
                List<Integer> personids2 = personDao.getPersonIdByDeptId(deptId2);
                for(Integer personId : personids2){
                    personids.add(personId);//最高到工程队级的部门所有人员放入personids
                }

                //每个部门的下级部门(最高到班组级)
                List<Integer> deptIds3 = deptDao.getIdByPid(deptId2);
                for(Integer deptId3 : deptIds3){
                    List<Integer> personids3 = personDao.getPersonIdByDeptId(deptId3);
                    for(Integer personId : personids3){
                        personids.add(personId); //最高到班组级的部门所有人员放入personids
                    }
                }
            }
        }

        //今日在岗人数(显示本级部门级及以下部门的扫码人员人数)
        List<Integer> work_day = workDao.getPersonIdsByDateAndPersonids(today,personids);
        int work_day_counts = work_day.size();

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayWorkCount", work_day_counts);//今日在岗人数(显示本级部门级及以下部门的扫码人员人数)
        return result;
    }

    @RequestMapping(value = "nowMachineCount",method = RequestMethod.GET)
    public AjaxResult getNowMachineCount() {

        //实时焊机数(查询machineNow表,获取个数)
        int machineNowCounts = machineNowDao.findAll().size();

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("nowMachineCount", machineNowCounts);   //实时焊机开机台数
        return result;
    }

    @RequestMapping(value = "todayRate",method = RequestMethod.GET)
    public AjaxResult getTodayRate() { //今日工效
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        //两周图表
        List<String> dayStrings = new ArrayList<>();
        int length = 14;
        String tempDay = today;
        dayStrings.add(tempDay);
        for (int i = 0; i <length-1 ; i++) {
            tempDay = DateUtils.getPrevDay(tempDay);
            dayStrings.add(tempDay);
        }

        List<ChartVo> chartResult = new ArrayList<>();
        for (String dayString : dayStrings) {
            ChartVo vo = new ChartVo();
            vo.setDate(dayString);
            List<EngineeringVo> engineeringVos = engineeringService.getInitData(DateUtils.parseDate(dayString),DateUtils.parseDate(dayString));
            if(!engineeringVos.isEmpty()){
                EngineeringVo engineeringVo = engineeringVos.get(0);
                vo.setRateValue(String.format("%.2f", (double)(engineeringVo.getWorkTime())/engineeringVo.getTime()*100));
            }
            chartResult.add(vo);
        }

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayRate", chartResult.get(0).getRateValue());
        return result;
    }

    @RequestMapping(value = "todayPower",method = RequestMethod.GET)
    public AjaxResult getTodayPower() { //今日能耗
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        //两周图表
        List<String> dayStrings = new ArrayList<>();
        int length = 14;
        String tempDay = today;
        dayStrings.add(tempDay);
        for (int i = 0; i <length-1 ; i++) {
            tempDay = DateUtils.getPrevDay(tempDay);
            dayStrings.add(tempDay);
        }

        List<ChartVo> chartResult = new ArrayList<>();
        for (String dayString : dayStrings) {
            ChartVo vo = new ChartVo();
            vo.setDate(dayString);
            try {
                List<EfficiencyStatisticsVo> vos = statisticsService.getAllData("",DateUtils.parseDate(dayString),DateUtils.parseDate(dayString));
                double power = 0.0;
                if(!vos.isEmpty()){
                    for (EfficiencyStatisticsVo efficiencyStatisticsVo : vos) {
//                        power = efficiencyStatisticsVo.getPower(); //方法一
                        power += efficiencyStatisticsVo.getSonPower();//方法二
                    }
                }
                vo.setPowerValue(String.format("%.2f", power));//保留两位小数
            } catch (Exception e) {
                e.printStackTrace();
            }
            chartResult.add(vo);
        }

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayPower", chartResult.get(0).getPowerValue());
        return result;
    }
}
