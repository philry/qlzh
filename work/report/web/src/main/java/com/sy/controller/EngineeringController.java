package com.sy.controller;


import com.sy.constant.HttpStatusConstant;
import com.sy.entity.Dept;
import com.sy.entity.Engineering;
import com.sy.service.DeptService;
import com.sy.service.EngineeringService;
import com.sy.utils.DateUtils;
import com.sy.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

	@RequestMapping(value = "info", method = RequestMethod.GET)
	public JsonResult initData(String beginTime, String endTime) {

		List<EngineeringVo> list = null;

		try {
			list = engineeringService.getInitData(beginTime == "" ? null : DateUtils.parseDate(beginTime), endTime == "" ? null : DateUtils.parseDate(endTime));
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.buildFailure(404, e.getMessage());
		}

		return JsonResult.buildSuccess(200, list);
	}

	@RequestMapping(value = "/workshop", method = RequestMethod.GET) //车间级数据
	public JsonResult workshop(String beginTime, String endTime, String deptName) {
		List<EngineeringVo> list = null;
		List<EngineeringResult> list2 = null;
		try {
			list = engineeringService.getInitData(beginTime == "" ? null : DateUtils.parseDate(beginTime), endTime == "" ? null : DateUtils.parseDate(endTime));
			if (list != null && list.size() > 0) {
				HashMap<String, EngineeringResult> hashMap = new HashMap<>();
				list2 = new ArrayList<>();
				// 获取车间的工效
				for (EngineeringVo e : list) {
					EngineeringResult engineeringResult = new EngineeringResult();
					engineeringResult.setName(e.getName_1());
					engineeringResult.setWorkTime(e.getWorkTime_1());
					engineeringResult.setTime(e.getTime_1());
					engineeringResult.setPower(e.getPower_1());
					BigDecimal workTime = new BigDecimal(e.getWorkTime_1());
					BigDecimal time = new BigDecimal(e.getTime_1());
					BigDecimal divide = workTime.divide(time, 4, RoundingMode.HALF_UP);
					Double efficiency = divide.doubleValue();
					engineeringResult.setEfficiency(efficiency);
					// 去除重复数据
					if (hashMap.get(engineeringResult.getName()) == null) {
						hashMap.put(engineeringResult.getName(), engineeringResult);
					}
				}
				for (Entry<String, EngineeringResult> e : hashMap.entrySet()) {
					list2.add(e.getValue());
				}
			}

			// 查出车间的部门id
			for (EngineeringResult e : list2) {
				Dept dept = new Dept();
				dept.setName(e.getName());
				List<Dept> deptList = deptService.getDeptList(dept);
				e.setDeptId(deptList.get(0).getId());
			}

			// 根据部门名称筛选
			// 如果没有传入部门名称则返回全部
			if (deptName == null || deptName == "") {
				return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, list2);
			} else {
				// 如果有则按部门名称进行筛选
				List<EngineeringResult> list3 = new ArrayList<>();
				for (EngineeringResult e : list2) {
					if (deptName.equals(e.getName())) {
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

	@RequestMapping(value = "/workshopChart", method = RequestMethod.GET) //车间级一周图表数据
	public AjaxResult workshopChart(String beginTime,String endTime,String deptName) {
		String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date());

		List<Engineering> list = null;
		List<EngineeringResult> list2 = null;

		//一周图表
		List<String> dayStrings = new ArrayList<>();
		if((beginTime == null && endTime == null) || (beginTime == "" && endTime == "")){
            int length = 7;
            String tempDay = today;
            dayStrings.add(tempDay);
            for (int i = 0; i < length - 1; i++) {
                tempDay = DateUtils.getPrevDay(tempDay);
                dayStrings.add(tempDay);
            }
        }else{
			//按时间查询
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			long sub = 0;
			try {
				sub = Math.abs(dateFormat.parse(endTime).getTime() - dateFormat.parse(beginTime).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			int length  = (int)(sub/1000/60/60/24);
			String tempDay = endTime;
			dayStrings.add(tempDay);
			for (int i = 0; i < length; i++) {
				tempDay = DateUtils.getPrevDay(tempDay);
				dayStrings.add(tempDay);
			}
		}

		Map<String,List<ChartVo>>  chartResultMap = new HashMap<>();
		List<ChartVo> chartResult = new ArrayList<>();;


		for (String dayString : dayStrings) {
			ChartVo vo = new ChartVo();
			vo.setDate(dayString);

			list = engineeringService.getDataByLevel(2,DateUtils.parseDate(dayString), DateUtils.parseDate(dayString));
			Integer id = null;
			String name = null;
			for(Engineering engieering: list){
				id = engieering.getId();
				name = engieering.getName();

				vo.setPowerValue(String.valueOf(engieering.getPower()));
				vo.setRateValue(engieering.getEfficency());
			}
			if(deptName == null || deptName == ""){
				chartResult.add(vo);
				if (id != null) {
					chartResultMap.put(name,chartResult);
				}
			}
			if(deptName.equals(name)){
				chartResult.add(vo);
				chartResultMap.put(name,chartResult);
			}

		}
		AjaxResult result = new AjaxResult();
		result.put("chartResultMap", chartResultMap);
		return result;
	}

    
    @RequestMapping(value = "/team", method = RequestMethod.GET) //工程队级数据图表
    public JsonResult team(String beginTime,String endTime,String deptName,String pName) {
    	List<EngineeringVo> list = null;
    	List<EngineeringResult> list2 = null;
    	try {
			list = engineeringService.getInitData(beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime));
			if(list!=null&&list.size()>0) {
				HashMap<String, EngineeringResult> hashMap = new HashMap<>();
				list2 = new ArrayList<>();
				// 获取工程队的工效
				for (EngineeringVo e : list) {
					EngineeringResult engineeringResult = new EngineeringResult();
					engineeringResult.setpName(e.getName_1());
					engineeringResult.setName(e.getName_2());
					engineeringResult.setTime(e.getTime_2());
					engineeringResult.setPower(e.getPower_2());
					engineeringResult.setWorkTime(e.getWorkTime_2());
					// 计算工效
					BigDecimal workTime = new BigDecimal(e.getWorkTime_2());
					BigDecimal time = new BigDecimal(e.getTime_2());
					BigDecimal divide = workTime.divide(time,4,RoundingMode.HALF_UP);
					Double efficiency = divide.doubleValue();
					engineeringResult.setEfficiency(efficiency);
					// 去除重复数据
					if(hashMap.get(engineeringResult.getName())==null) {
						hashMap.put(engineeringResult.getName(), engineeringResult);
					}
				}
				for (Entry<String, EngineeringResult> e : hashMap.entrySet()) {
					list2.add(e.getValue());
				}
			}
			// 查出车间的部门id
			for (EngineeringResult e : list2) {
				Dept dept = new Dept();
				dept.setName(e.getName());
				List<Dept> deptList = deptService.getDeptList(dept);
				e.setDeptId(deptList.get(0).getId());
			}
			List<EngineeringResult> list3 = new ArrayList<>();
			// 根据上级部门名称筛选
			if(pName!=null&&pName!="") {
				for (EngineeringResult e : list2) {
					// 根据上级部门及车间名称筛选
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
				// 如果没有传入上级部门名称则判断有没有传入车间名称
				if(deptName!=null&&deptName!="") {
					for (EngineeringResult e : list2) {
						if(deptName.equals(e.getName())) {
							list3.add(e);
						}
					}
					return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, list3);
				}else {
					// 如果都没有则全部返回
					return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, list2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
		}
    }

	@RequestMapping(value = "/teamChart", method = RequestMethod.GET) //工程队级一周图表数据
	public AjaxResult teamChart(String beginTime,String endTime,String deptName) {
		String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date());

		List<Engineering> list = null;
		List<EngineeringResult> list2 = null;

		//一周图表
		List<String> dayStrings = new ArrayList<>();
		if((beginTime == null && endTime == null) || (beginTime == "" && endTime == "")){
			int length = 7;
			String tempDay = today;
			dayStrings.add(tempDay);
			for (int i = 0; i < length - 1; i++) {
				tempDay = DateUtils.getPrevDay(tempDay);
				dayStrings.add(tempDay);
			}
		}else{
			//按时间查询
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			long sub = 0;
			try {
				sub = Math.abs(dateFormat.parse(endTime).getTime() - dateFormat.parse(beginTime).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			int length  = (int)(sub/1000/60/60/24);
			String tempDay = endTime;
			dayStrings.add(tempDay);
			for (int i = 0; i < length; i++) {
				tempDay = DateUtils.getPrevDay(tempDay);
				dayStrings.add(tempDay);
			}
		}

		Map<String,List<ChartVo>>  chartResultMap = new HashMap<>();
		List<ChartVo> chartResult = new ArrayList<>();;


		for (String dayString : dayStrings) {
			ChartVo vo = new ChartVo();
			vo.setDate(dayString);

			list = engineeringService.getDataByLevel(3,DateUtils.parseDate(dayString), DateUtils.parseDate(dayString));
			Integer id = null;
			String name =null;
			for(Engineering engieering: list){
				id = engieering.getId();
				name = engieering.getName();
				vo.setPowerValue(String.valueOf(engieering.getPower()));
				vo.setRateValue(engieering.getEfficency());
			}
			if(deptName == null || deptName == ""){
				chartResult.add(vo);
				if (id != null) {
					chartResultMap.put(name,chartResult);
				}
			}
			if(deptName.equals(name)){ //查询时部门名称对比
				chartResult.add(vo);
				chartResultMap.put(name,chartResult);
			}
		}
		AjaxResult result = new AjaxResult();
		result.put("chartResultMap", chartResultMap);
		return result;
	}

	@RequestMapping(value = "/banzu", method = RequestMethod.GET) //班组级数据图表
	public JsonResult banzu(String beginTime,String endTime,String deptName,String pName) {
		List<EngineeringVo> list = null;
		List<EngineeringResult> list2 = null;
		try {
			list = engineeringService.getInitData(beginTime =="" ?null:DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime));
			if(list!=null&&list.size()>0) {
				HashMap<String, EngineeringResult> hashMap = new HashMap<>();
				list2 = new ArrayList<>();
				// 获取班组的工效
				for (EngineeringVo e : list) {
					EngineeringResult engineeringResult = new EngineeringResult();
					engineeringResult.setpName(e.getName_2());
					engineeringResult.setName(e.getName_3());
					engineeringResult.setTime(e.getTime_3());
					engineeringResult.setPower(e.getPower_3());
					engineeringResult.setWorkTime(e.getWorkTime_3());
					// 计算工效
					BigDecimal workTime = new BigDecimal(e.getWorkTime_3());
					BigDecimal time = new BigDecimal(e.getTime_3());
					BigDecimal divide = workTime.divide(time,4,RoundingMode.HALF_UP);
					Double efficiency = divide.doubleValue();
					engineeringResult.setEfficiency(efficiency);
					// 去除重复数据
					if(hashMap.get(engineeringResult.getName())==null) {
						hashMap.put(engineeringResult.getName(), engineeringResult);
					}
				}
				for (Entry<String, EngineeringResult> e : hashMap.entrySet()) {
					list2.add(e.getValue());
				}
			}
			// 查出工程队的部门id
			for (EngineeringResult e : list2) {
				Dept dept = new Dept();
				dept.setName(e.getName());
				List<Dept> deptList = deptService.getDeptList(dept);
				e.setDeptId(deptList.get(0).getId());
			}
			List<EngineeringResult> list3 = new ArrayList<>();
			// 根据上级部门名称筛选
			if(pName!=null&&pName!="") {
				for (EngineeringResult e : list2) {
					// 根据上级部门及工程队名称筛选
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
				// 如果没有传入上级部门名称则判断有没有传入工程队名称
				if(deptName!=null&&deptName!="") {
					for (EngineeringResult e : list2) {
						if(deptName.equals(e.getName())) {
							list3.add(e);
						}
					}
					return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, list3);
				}else {
					// 如果都没有则全部返回
					return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, list2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
		}
	}

	@RequestMapping(value = "/banzuChart", method = RequestMethod.GET) //工程队级一周图表数据
	public AjaxResult banzuChart(String beginTime,String endTime,String deptName) {
		String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, new Date());

		List<Engineering> list = null;
		List<EngineeringResult> list2 = null;

		//一周图表
		List<String> dayStrings = new ArrayList<>();
		if((beginTime == null && endTime == null) || (beginTime == "" && endTime == "")){
			int length = 7;
			String tempDay = today;
			dayStrings.add(tempDay);
			for (int i = 0; i < length - 1; i++) {
				tempDay = DateUtils.getPrevDay(tempDay);
				dayStrings.add(tempDay);
			}
		}else{
			//按时间查询
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			long sub = 0;
			try {
				sub = Math.abs(dateFormat.parse(endTime).getTime() - dateFormat.parse(beginTime).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			int length  = (int)(sub/1000/60/60/24);
			String tempDay = endTime;
			dayStrings.add(tempDay);
			for (int i = 0; i < length; i++) {
				tempDay = DateUtils.getPrevDay(tempDay);
				dayStrings.add(tempDay);
			}
		}
		Map<String,List<ChartVo>>  chartResultMap = new HashMap<>();
		List<ChartVo> chartResult = new ArrayList<>();;


		for (String dayString : dayStrings) {
			ChartVo vo = new ChartVo();
			vo.setDate(dayString);

			list = engineeringService.getDataByLevel(4,DateUtils.parseDate(dayString), DateUtils.parseDate(dayString));
			Integer id = null;
			String name = null;
			for(Engineering engieering: list){
				id = engieering.getId();
				name = engieering.getName();
				vo.setPowerValue(String.valueOf(engieering.getPower()));
				vo.setRateValue(engieering.getEfficency());
			}
			if(deptName == null || deptName == ""){
				chartResult.add(vo);
				if (id != null) {
					chartResultMap.put(name,chartResult);
				}
			}
			if(deptName.equals(name)){
				chartResult.add(vo);
				chartResultMap.put(name,chartResult);
			}
		}
		AjaxResult result = new AjaxResult();
		result.put("chartResultMap", chartResultMap);
		return result;
	}
}
