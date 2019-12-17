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
import com.sy.entity.Machine;
import com.sy.service.MachineService;
import com.sy.vo.JsonResult;
import com.sy.vo.PageResult;

@RequestMapping("/machine")
@RestController
public class MachineController {

	@Autowired
	private MachineService machineService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public PageResult getList(Machine machine) {
		List<Machine> list = machineService.selectMachineList(machine);
		return PageResult.getPageResult(list,null);
	}
	
	@RequestMapping(value = "/lists/{yema}", method = RequestMethod.GET)
	public PageResult getLists(Machine machine,@PathVariable("yema")Integer yema) {
		Page<Object> startPage = PageHelper.startPage(yema, 30);
		List<Machine> list = machineService.selectMachineList(machine);
		return PageResult.getPageResult(list,startPage.getTotal());
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(Machine machine) {
		return JsonResult.getJson(machineService.insertMachine(machine));
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public JsonResult edit(Machine machine) {
		return JsonResult.getJson(machineService.updateMachine(machine));
	}
	
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public JsonResult removeById(@PathVariable("id")Integer id) {
		return JsonResult.getJson(machineService.removeMachineById(id));
	}
	
	@RequestMapping(value = "/querybyid/{id}", method = RequestMethod.GET)
	public JsonResult selectMachineById(@PathVariable("id")Integer id) {
		return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, machineService.selectMachineById(id));
	}
}
