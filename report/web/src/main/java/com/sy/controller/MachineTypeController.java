package com.sy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sy.constant.HttpStatusConstant;
import com.sy.entity.MachineType;
import com.sy.service.MachineTypeService;
import com.sy.vo.JsonResult;
import com.sy.vo.PageResult;

@RequestMapping("machinetype")
@RestController
public class MachineTypeController {

	@Autowired
	private MachineTypeService machineTypeService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public PageResult getList(MachineType machineType) {
		List<MachineType> list = machineTypeService.selectMachineTypeList(machineType);
		return PageResult.getPageResult(list,null);
	}
	
	@RequestMapping(value = "/lists/{yema}/{size}", method = RequestMethod.GET)
	public PageResult getLists(MachineType machineType,@PathVariable("yema")Integer yema,@PathVariable("size")Integer size) {
		Page<Object> startPage = PageHelper.startPage(yema, size);
		List<MachineType> list = machineTypeService.selectMachineTypeList(machineType);
		return PageResult.getPageResult(list,startPage.getTotal());
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(MachineType machineType) {
		return JsonResult.getJson(machineTypeService.insertMachineType(machineType));
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public JsonResult edit(MachineType machineType) {
		return JsonResult.getJson(machineTypeService.updateMachineType(machineType));
	}
	
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public JsonResult removeById(@PathVariable("id")Integer id) {
		return JsonResult.getJson(machineTypeService.deleteMachineTypeById(id));
	}
	
	@RequestMapping(value = "/querybyid/{id}")
	public JsonResult selectMachineTypeById(@PathVariable("id")Integer id) {
		return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, machineTypeService.selectMachineTypeById(id));
	}
}
