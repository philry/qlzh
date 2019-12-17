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
import com.sy.entity.Dept;
import com.sy.service.DeptService;
import com.sy.vo.JsonResult;
import com.sy.vo.PageResult;

@RequestMapping("/dept")
@RestController
public class DeptController {

	@Autowired
	private DeptService deptService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public PageResult getList(Dept dept) {
		List<Dept> list = deptService.getDeptList(dept);
		return PageResult.getPageResult(list,null);
	}

	@RequestMapping(value = "/lists/{yema}", method = RequestMethod.GET)
	public PageResult getList(Dept dept, @PathVariable("yema") Integer yema) {
		Page<Object> startPage = PageHelper.startPage(yema, 30);
		List<Dept> list = deptService.getDeptList(dept);
		return PageResult.getPageResult(list,startPage.getTotal());
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(Dept dept) {
		return JsonResult.getJson(deptService.insertDept(dept));
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public JsonResult edit(Dept dept) {
		return JsonResult.getJson(deptService.updateDept(dept));
	}

	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public JsonResult remove(@PathVariable("id") Integer id) {
		int rows = deptService.deleteDeptById(id);
		return JsonResult.getJson(rows);

	}

	@RequestMapping(value = "/querybyid/{id}")
	public JsonResult selectById(@PathVariable("id") Integer id) {
		return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, deptService.selectDeptById(id));
	}

	@RequestMapping(value = "/depttree", method = RequestMethod.GET)
	public PageResult findDeptTree() {
		List<Dept> list = deptService.selectDeptTree();
		return PageResult.getPageResult(list,null);
	}
	
	@RequestMapping(value = "/higherdept", method = RequestMethod.GET)
	public PageResult selectHigherDept() {
		List<Dept> list = deptService.getHigherDept();
		return PageResult.getPageResult(list,null);
	}
	
}
