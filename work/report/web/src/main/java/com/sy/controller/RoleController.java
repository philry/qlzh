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
import com.sy.entity.Role;
import com.sy.service.RoleService;
import com.sy.vo.JsonResult;
import com.sy.vo.PageResult;

@RequestMapping("/role")
@RestController
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@RequestMapping(value = "/querybyid/{id}", method = RequestMethod.GET)
	public JsonResult selectById(@PathVariable("id")Integer id) {
		return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, roleService.selectRoleById(id));
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public PageResult getList(Role role) {
		List<Role> list = roleService.selectRoleList(role);
		return PageResult.getPageResult(list,null);
	}
	
	@RequestMapping(value = "/lists/{yema}", method = RequestMethod.GET)
	public PageResult getLists(Role role,@PathVariable("yema")Integer yema) {
		Page<Object> startPage = PageHelper.startPage(yema, 10);
		List<Role> list = roleService.selectRoleList(role);
		return PageResult.getPageResult(list,startPage.getTotal());
	}
}
