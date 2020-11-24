package com.sy.controller;


import com.sy.dao.*;
import com.sy.entity.*;
import com.sy.service.*;
import com.sy.service.impl.PersonServiceImpl;
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
    private MachineNowService machineNowService;

    @Autowired
    private MachineService machineService;

    @Autowired
    private MachineNowDao machineNowDao;

    @Autowired
    private WorkDao workDao;

    @Autowired
    private PersonEfficiencyService personEfficiencyService;

    @Autowired
    private MachineDao machineDao;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private EngineeringService engineeringService;

    @Autowired
    private EfficiencyStatisticsService statisticsService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private DeptDao deptDao;

    @RequestMapping(value = "todayWorkCount",method = RequestMethod.GET)
    public AjaxResult getTodayWorkCount(Integer personId){

        Integer deptId = personDao.getById(personId).getDept().getId();

        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);

        List<Integer> personids = new ArrayList<>();


        List<Integer> personids0 = personDao.getPersonIdByDeptId(deptId);
        for(Integer personId1 : personids0){
            personids.add(personId1);
        }


        List<Integer> deptIds1 = deptDao.getIdByPid(deptId);
        for(Integer deptId1 : deptIds1){
            List<Integer> personids1 = personDao.getPersonIdByDeptId(deptId1);
            for(Integer personId1 : personids1){
                personids.add(personId1);
            }


            List<Integer> deptIds2 = deptDao.getIdByPid(deptId1);
            for(Integer deptId2 : deptIds2){
                List<Integer> personids2 = personDao.getPersonIdByDeptId(deptId2);
                for(Integer personId1 : personids2){
                    personids.add(personId1);
                }


                List<Integer> deptIds3 = deptDao.getIdByPid(deptId2);
                for(Integer deptId3 : deptIds3){
                    List<Integer> personids3 = personDao.getPersonIdByDeptId(deptId3);
                    for(Integer personId1 : personids3){
                        personids.add(personId1);
                    }
                }
            }
        }


        List<Integer> work_day = workDao.getPersonIdsByDateAndPersonids(today,personids);
        int work_day_counts = work_day.size();

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayWorkCount", work_day_counts);
        return result;
    }

    @RequestMapping(value = "nowMachineCount",method = RequestMethod.GET)
    public AjaxResult getNowMachineCount(Integer personId) {

        Integer deptId = personDao.getById(personId).getDept().getId();


        List<Integer> personids = new ArrayList<>();


        List<Integer> personids0 = personDao.getPersonIdByDeptId(deptId);
        for(Integer personId1 : personids0){
            personids.add(personId1);
        }


        List<Integer> deptIds1 = deptDao.getIdByPid(deptId);
        for(Integer deptId1 : deptIds1){
            List<Integer> personids1 = personDao.getPersonIdByDeptId(deptId1);
            for(Integer personId1 : personids1){
                personids.add(personId1);
            }


            List<Integer> deptIds2 = deptDao.getIdByPid(deptId1);
            for(Integer deptId2 : deptIds2){
                List<Integer> personids2 = personDao.getPersonIdByDeptId(deptId2);
                for(Integer personId1 : personids2){
                    personids.add(personId1);
                }


                List<Integer> deptIds3 = deptDao.getIdByPid(deptId2);
                for(Integer deptId3 : deptIds3){
                    List<Integer> personids3 = personDao.getPersonIdByDeptId(deptId3);
                    for(Integer personId1 : personids3){
                        personids.add(personId1);
                    }
                }
            }
        }


        int machineNowCounts = machineNowDao.getCountByPersonids(personids);

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("nowMachineCount", machineNowCounts);
        return result;
    }

    @RequestMapping(value = "todayRate",method = RequestMethod.GET)
    public AjaxResult getTodayRate(Integer personId) {
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);

        Integer deptId = personDao.getById(personId).getDept().getId();

        EngineeringVo engineeringVo = null;
        double rateValue = 0.00;
            try {
                engineeringVo = engineeringService.getInitDataByDeptId(deptId,DateUtils.parseDate(today), DateUtils.getNextDay(today));
                if(engineeringVo.getTime() != 0){
                    rateValue = (double)(engineeringVo.getWorkTime())/engineeringVo.getTime()*100;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayRate", String.format("%.2f",rateValue));
        return result;
    }


    @RequestMapping(value = "todayPower",method = RequestMethod.GET)
    public AjaxResult getTodayPower(Integer personId) {
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);

        EngineeringVo engineeringVo = null;
        double todayPower = 0.00;

        Integer deptId = personDao.getById(personId).getDept().getId();

            try {
                engineeringVo = engineeringService.getInitDataByDeptId(deptId,DateUtils.parseDate(today), DateUtils.getNextDay(today));
                todayPower = Double.valueOf(engineeringVo.getPower()).doubleValue();
            } catch (Exception e) {
                e.printStackTrace();
            }

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayPower", String.format("%.2f",todayPower));
        return result;
    }
}
