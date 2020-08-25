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
    public AjaxResult getTodayWorkCount(Integer deptId, Integer personId){ //今日上班人数(显示本级部门级及以下部门的扫码人员人数)

        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        List<Integer> personids = new ArrayList<>();

        //最高到生产部级
        List<Integer> personids0 = personDao.getPersonIdByDeptId(deptId);
        for(Integer personId1 : personids0){
            personids.add(personId1);
        }

        //下级部门(最高到车间级)
        List<Integer> deptIds1 = deptDao.getIdByPid(deptId);
        for(Integer deptId1 : deptIds1){
            List<Integer> personids1 = personDao.getPersonIdByDeptId(deptId1);
            for(Integer personId1 : personids1){
                personids.add(personId1);//最高到车间级的部门所有人员放入personids
            }

            //每个部门的下级部门(最高到工程队级)
            List<Integer> deptIds2 = deptDao.getIdByPid(deptId1);
            for(Integer deptId2 : deptIds2){
                List<Integer> personids2 = personDao.getPersonIdByDeptId(deptId2);
                for(Integer personId1 : personids2){
                    personids.add(personId1);//最高到工程队级的部门所有人员放入personids
                }

                //每个部门的下级部门(最高到班组级)
                List<Integer> deptIds3 = deptDao.getIdByPid(deptId2);
                for(Integer deptId3 : deptIds3){
                    List<Integer> personids3 = personDao.getPersonIdByDeptId(deptId3);
                    for(Integer personId1 : personids3){
                        personids.add(personId1); //最高到班组级的部门所有人员放入personids
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
        result.put("todayWorkCount", work_day_counts);//今日上班人数(显示本级部门级及以下部门的扫码人员人数)
        return result;
    }

    @RequestMapping(value = "nowMachineCount",method = RequestMethod.GET)
    public AjaxResult getNowMachineCount(Integer deptId, Integer personId) {

        /*List<Integer> machineids = new ArrayList<>();

        //最高到生产部级
        List<Integer> machineids0 = machineDao.getMachineIdByDeptId(deptId);
        for(Integer machineId : machineids0){
            machineids.add(machineId);
        }

        //下级部门(最高到车间级)
        List<Integer> deptIds1 = deptDao.getIdByPid(deptId);
        for(Integer deptId1 : deptIds1){
            List<Integer> machineids1 = machineDao.getMachineIdByDeptId(deptId1);
            for(Integer machineId : machineids1){
                machineids.add(machineId);//最高到车间级的部门所有焊机放入machineids
            }

            //每个部门的下级部门(最高到工程队级)
            List<Integer> deptIds2 = deptDao.getIdByPid(deptId1);
            for(Integer deptId2 : deptIds2){
                List<Integer> machineids2 = machineDao.getMachineIdByDeptId(deptId2);
                for(Integer machineId : machineids2){
                    machineids.add(machineId);//最高到工程队级的部门所有焊机放入machineids
                }

                //每个部门的下级部门(最高到班组级)
                List<Integer> deptIds3 = deptDao.getIdByPid(deptId2);
                for(Integer deptId3 : deptIds3){
                    List<Integer> machineids3 = machineDao.getMachineIdByDeptId(deptId3);
                    for(Integer machineId : machineids3){
                        machineids.add(machineId); //最高到班组级的部门所有焊机放入machineids
                    }
                }
            }
        }*/

        //实时焊机数(查询machineNow表,获取个数)
        int machineNowCounts = machineNowDao.findAll().size();
//        int machineNowCounts = machineNowDao.getCountByMachineids(machineids);//实时焊机开机台数(按不同部门不同展示)


        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("nowMachineCount", machineNowCounts);   //实时焊机开机台数
        return result;
    }

    @RequestMapping(value = "todayRate",method = RequestMethod.GET)
    public AjaxResult getTodayRate(Integer deptId, Integer personId) { //今日工效(按不同部门不同展示)
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        EngineeringVo engineeringVo = null;
        double rateValue = 0.00;
        double newRateValue = 0.00;
        Dept dept = deptService.selectDeptById(deptId);
        Integer leader = dept.getLeader();
        if(!personId.equals(leader) && personId != 1){ //不是部门负责人并且不是admin就只显示自己的工效
            List<PersonEfficiency> list = personEfficiencyService.getDataByIdAndTime(personId, DateUtils.parseDate(today),DateUtils.getNextDay(today));
            if(list != null && list.size() != 0){
                PersonEfficiency personEfficiency = list.get(0);
                if(personEfficiency.getTime() != 0){
//                    rateValue = (double)(personEfficiency.getWorkingTime())/personEfficiency.getTime()*100;//方法一
                    rateValue = Double.valueOf(personEfficiency.getEfficiency());//方法二
                }
            }

        }else{
            try {
                engineeringVo = engineeringService.getInitDataByDeptId(deptId,DateUtils.parseDate(today), DateUtils.getNextDay(today));
                if(engineeringVo.getTime() != 0){
                    rateValue = (double)(engineeringVo.getWorkTime())/engineeringVo.getTime()*100;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        double rateValueValue = new BigDecimal(rateValue).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); //四舍五入保留两位小数的值
        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayRate", String.format("%.2f",rateValue));//保留两位小数方法一
//        result.put("todayRate", rateValueValue);//保留两位小数方法二
        return result;
    }


    @RequestMapping(value = "todayPower",method = RequestMethod.GET)
    public AjaxResult getTodayPower(Integer deptId, Integer personId) { //今日能耗(按不同部门不同展示)
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        EngineeringVo engineeringVo = null;
        double todayPower = 0.00;


        Dept dept = deptService.selectDeptById(deptId);
        Integer leader = dept.getLeader();
        if(!personId.equals(leader)  && personId != 1){ //不是部门负责人并且不是admin就只显示自己的能耗
            List<PersonEfficiency> list = personEfficiencyService.getDataByIdAndTime(personId, DateUtils.parseDate(today), DateUtils.getNextDay(today));
            if(list != null && list.size() != 0){
                for(PersonEfficiency personEfficiency : list){
                    todayPower +=  Double.valueOf(personEfficiency.getNoloadingPower()).doubleValue();
                    todayPower +=  Double.valueOf(personEfficiency.getWorkingPower()).doubleValue();
                }
            }
        }else{
            try {
                engineeringVo = engineeringService.getInitDataByDeptId(deptId,DateUtils.parseDate(today), DateUtils.getNextDay(today));
                todayPower = Double.valueOf(engineeringVo.getPower()).doubleValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayPower", String.format("%.2f",todayPower));
        return result;
    }
}
