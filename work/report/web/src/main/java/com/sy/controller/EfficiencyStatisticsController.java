package com.sy.controller;


import com.sy.dao.TaskDao;
import com.sy.dao.WorkDao;
import com.sy.entity.EfficiencyStatistics;
import com.sy.service.EfficiencyStatisticsService;
import com.sy.utils.DateUtils;
import com.sy.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("efficiencyStatistics")
public class EfficiencyStatisticsController {


    @Autowired
    private EfficiencyStatisticsService statisticsService;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private WorkDao workDao;

    @RequestMapping(value = "info",method = RequestMethod.GET)
    public JsonResult getData(String taskName,String beginTime,String endTime){

        List<EfficiencyStatisticsVo> list = null;

        if(!"".equals(taskName)){
            taskName = taskDao.getNameByWorkNo(taskName);
            if(taskName==null){
                return JsonResult.buildFailure(404,"工号不存在");
            }
        }

        try {
            list = statisticsService.getAllData(taskName, beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime));

        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }


        if(list==null){
            return JsonResult.buildFailure(404,"计算失败，稍后再试");
        }else {
            if(list.isEmpty()){
                return JsonResult.buildFailure(404,"无数据");
            }else {
                return JsonResult.buildSuccess(200,list);
            }
        }
    }

    @RequestMapping(value = "app/info/all",method = RequestMethod.GET)
    public JsonResult getAppData(String taskName,String beginTime,String endTime){

        List<EfficiencyStatisticsVo> list = null;

        try {
            list = statisticsService.getAllData(taskName, beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }
        System.out.println(list);
        if(list==null){
            return JsonResult.buildFailure(404,"计算失败，稍后再试");
        }else {
            if(list.isEmpty()){
                return JsonResult.buildFailure(404,"无数据");
            }else {
                Set<AppVo> appVos = new HashSet<>();
                for (EfficiencyStatisticsVo vo : list) {
                    AppVo appVo = new AppVo();
                    appVo.setWorkNo(taskDao.getWorkNoByName(vo.getName()));
                    appVo.setTime(vo.getTime());
                    appVo.setWorkTime(vo.getWorkTime());
                    appVo.setPower(String.valueOf(vo.getPower()));
                    appVo.setEfficiency(String.format("%.2f", (double)vo.getWorkTime()/vo.getTime()*100));
                    System.out.println(appVo);
                    appVos.add(appVo);
                }
                return JsonResult.buildSuccess(200,appVos);
            }
        }
    }

    @RequestMapping(value = "app/info/select",method = RequestMethod.GET)
    public JsonResult getAppDataOne(String taskName,String beginTime,String endTime){

        //获取指定日期（前一天）
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        List<EfficiencyStatisticsVo> list = null;

        if("".equals(taskName)||taskName==null){
            return JsonResult.buildFailure(404,"请输出项目工号");
        }

        taskName = taskDao.getNameByWorkNo(taskName);

        if(taskName==null){
            return JsonResult.buildFailure(404,"工号不存在");
        }

        try {
            list = statisticsService.getAllData(taskName, beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }

        if(list==null){
            return JsonResult.buildFailure(404,"计算失败，稍后再试");
        }else {
            if(list.isEmpty()){
                return JsonResult.buildFailure(404,"无数据");
            }else {
                ProjectVo projectVo = new ProjectVo();
                List<AppVo> appVos = new ArrayList<>();
                for (EfficiencyStatisticsVo vo : list) {
                    AppVo appVo = new AppVo();
                    appVo.setWorkNo(vo.getSonName());
                    appVo.setTime(vo.getSonTime());
                    appVo.setWorkTime(vo.getSonWorkTime());
                    appVo.setPower(String.valueOf(vo.getSonPower()));
                    appVo.setEfficiency(String.format("%.2f", (double)vo.getSonWorkTime()/vo.getSonTime()*100));
                    appVos.add(appVo);
                }
                projectVo.setPower(String.valueOf(list.get(0).getPower()));
                projectVo.setWorkNo(taskDao.getWorkNoByName(list.get(0).getName()));
                projectVo.setAppVos(appVos);
                //获取当前项目的昨天工作人数和今日工作人数
                List<EfficiencyStatistics> tempList = null;
                try {
                    tempList = statisticsService.getEfficiencyStatistics(taskName, beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime));
                } catch (Exception e) {
                    e.printStackTrace();
                    return JsonResult.buildFailure(404,e.getMessage());
                }

                if(tempList!=null&&!tempList.isEmpty()){

                    Integer task_id = taskDao.getIdByName(taskName);
                    if(task_id==null){
                        return JsonResult.buildFailure(404,"查询的项目不存在/当日该项目并未施工");
                    }

                    int counts = getPersonCounts(taskName, today);
                    int prevCounts = getPersonCounts(taskName,day);
                    projectVo.setWorkerNo(counts);
                    if(prevCounts==0){
                        projectVo.setProportion(String.format("%.2f", (double)(counts-prevCounts)*100));
                    }else {
                        try{
                            projectVo.setProportion(String.format("%.2f", (double)(counts-prevCounts)/prevCounts*100));}
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }

                return JsonResult.buildSuccess(200,projectVo);
            }
        }
    }

    private int getPersonCounts(String taskName, String today) {
        int counts = 0;
        List<Integer> ids = workDao.getTaskIds(today);
        System.out.println(ids);
        for (Integer id : ids) {
            String name = statisticsService.getFirstTaskName(taskDao.getNameById(id));
            if(taskName.equals(name)){
                counts++;
            }
        }
        return counts;
    }
}
