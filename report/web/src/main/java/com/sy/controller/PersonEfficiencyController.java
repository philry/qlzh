package com.sy.controller;


import com.sy.constant.HttpStatusConstant;
import com.sy.dao.DeptDao;
import com.sy.dao.PersonDao;
import com.sy.entity.PersonEfficiency;
import com.sy.service.DeptService;
import com.sy.service.PersonEfficiencyService;
import com.sy.utils.DateUtils;
import com.sy.vo.JsonResult;
import com.sy.vo.PageJsonResult;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("personEfficiency")
public class PersonEfficiencyController {

    @Autowired
    private PersonEfficiencyService personEfficiencyService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private DeptDao deptDao;

    @Autowired
    private PersonDao personDao;

    /**
     *
     * @param personName 输入框查询的人员
     * @param deptId    默认当前登录人员所属部门，选了左边部门列表就是所选部门Id
     * @param page
     * @param pageSize
     * @param beginTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "all",method = RequestMethod.GET)
    public JsonResult getAllData(String personName,int deptId,Integer page,Integer pageSize,String beginTime,String endTime){
       /* if(deptId!=0&&!"".equals(personName)){
            Integer leader = deptDao.getLeaderById(deptId);
            Integer personId = personDao.getIdByName(personName);

            if(leader==personId){
                personName="";
            }
        }*/

        /*try {
            personEfficiencyService.calculateData(personName,deptId,beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.getNextDay(endTime));
        } catch (Exception e) {
            e.printStackTrace();
            return PageJsonResult.buildFailurePage(404,e.getMessage());
        }

        return PageJsonResult.buildSuccessPage(HttpStatusConstant.SUCCESS,personEfficiencyService.initAllData(page,pageSize));//原来的*/

        try {

            return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, personEfficiencyService.initDeptData(personName, deptId,beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime)));
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }
    }

    @RequestMapping(value = "select",method = RequestMethod.GET)
    public JsonResult getAllDataBySelect(String personName,int deptId,Integer page,Integer pageSize,String beginTime,String endTime){

       /* try {
            personEfficiencyService.calculateData(personName,deptId,beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.getNextDay(endTime));
        } catch (Exception e) {
            e.printStackTrace();
            return PageJsonResult.buildFailurePage(404,e.getMessage());
        }

        return PageJsonResult.buildSuccessPage(HttpStatusConstant.SUCCESS,personEfficiencyService.initAllData(page,pageSize));*/

        try {
            return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, personEfficiencyService.initDeptData(personName, deptId,beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime)));
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }
    }


}
