package com.sy.controller;


import com.sy.constant.HttpStatusConstant;
import com.sy.entity.Dept;
import com.sy.entity.Engineering;
import com.sy.service.DeptService;
import com.sy.service.EngineeringService;
import com.sy.utils.DateUtils;
import com.sy.vo.EngineeringResult;
import com.sy.vo.EngineeringVo;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

@RestController
@RequestMapping("engineering")
public class EngineeringController {

    @Autowired
    private EngineeringService engineeringService;
    
    @Autowired
    private DeptService deptService;

    @RequestMapping(value = "info",method = RequestMethod.GET)
    public JsonResult initData(String beginTime,String endTime){

        List<EngineeringVo> list = null;

        try{
            list = engineeringService.getInitData(beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime));
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }

        return JsonResult.buildSuccess(200,list);
    }

    @RequestMapping(value = "/workshop",method = RequestMethod.GET)
    public JsonResult workshop(String beginTime,String endTime,String deptName) {
    	List<EngineeringVo> list = null;
    	List<EngineeringResult> list2 = null;
    	try {
			list = engineeringService.getInitData(beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime));
			if(list!=null&&list.size()>0) {
				HashMap<String, EngineeringResult> hashMap = new HashMap<>();
				list2=new ArrayList<>();
				for (EngineeringVo e : list) {
					EngineeringResult engineeringResult = new EngineeringResult();
					engineeringResult.setName(e.getName_1());
					engineeringResult.setWorkTime(e.getWorkTime_1());
					engineeringResult.setTime(e.getTime_1());
					BigDecimal workTime = new BigDecimal(e.getWorkTime_1());
					BigDecimal time = new BigDecimal(e.getTime_1());
					BigDecimal divide = workTime.divide(time,2,RoundingMode.HALF_UP);
					Double efficiency = divide.doubleValue();
					engineeringResult.setEfficiency(efficiency);
					if(hashMap.get(engineeringResult.getName())==null) {
						hashMap.put(engineeringResult.getName(), engineeringResult);
					}
				}
				for (Entry<String, EngineeringResult> e : hashMap.entrySet()) {
					list2.add(e.getValue());
				}
			}
			if(deptName==null||deptName=="") {
				return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, list2);
			}else {
				List<EngineeringResult> list3 = new ArrayList<>();
				for (EngineeringResult e : list2) {
					if(deptName.equals(e.getName())) {
						list3.add(e);
					}
				}
				return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, list3);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
		}
    }
    
    @RequestMapping(value = "/team", method = RequestMethod.GET)
    public JsonResult team(String beginTime,String endTime,String deptName,String pName) {
    	List<EngineeringVo> list = null;
    	List<EngineeringResult> list2 = null;
    	try {
			list = engineeringService.getInitData(beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime));
			if(list!=null&&list.size()>0) {
				HashMap<String, EngineeringResult> hashMap = new HashMap<>();
				list2 = new ArrayList<>();
				for (EngineeringVo e : list) {
					EngineeringResult engineeringResult = new EngineeringResult();
					engineeringResult.setpName(e.getName_1());
					engineeringResult.setName(e.getName_2());
					engineeringResult.setTime(e.getTime_2());
					engineeringResult.setWorkTime(e.getWorkTime_2());
					BigDecimal workTime = new BigDecimal(e.getWorkTime_2());
					BigDecimal time = new BigDecimal(e.getTime_2());
					BigDecimal divide = workTime.divide(time,2,RoundingMode.HALF_UP);
					Double efficiency = divide.doubleValue();
					engineeringResult.setEfficiency(efficiency);
					if(hashMap.get(engineeringResult.getName())==null) {
						hashMap.put(engineeringResult.getName(), engineeringResult);
					}
				}
				for (Entry<String, EngineeringResult> e : hashMap.entrySet()) {
					list2.add(e.getValue());
				}
			}
			
			for (EngineeringResult e : list2) {
				Dept dept = new Dept();
				dept.setName(e.getName());
				List<Dept> deptList = deptService.getDeptList(dept);
				e.setDeptId(deptList.get(0).getId());
			}
			
			List<EngineeringResult> list3 = new ArrayList<>();
			if(pName!=null&&pName!="") {
				for (EngineeringResult e : list2) {
					if(deptName!=null&&deptName!="") {
						if(deptName.equals(e.getName())&&pName.equals(e.getpName())) {
							list3.add(e);
						}
					}else {
						if(pName.equals(e.getpName())) {
							list3.add(e);
						}
					}
				}
				return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, list3);
			}else {
				if(deptName!=null&&deptName!="") {
					for (EngineeringResult e : list2) {
						if(deptName.equals(e.getName())) {
							list3.add(e);
						}
					}
					return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, list3);
				}else {
					return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, list2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
		}
    }
}
