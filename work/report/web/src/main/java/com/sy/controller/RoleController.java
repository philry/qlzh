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
import com.sy.service.RoleAuthorityService;
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
		return PageResult.getPageResult(list);
	}
	
	@RequestMapping(value = "/lists/{yema}", method = RequestMethod.GET)
	public PageResult getLists(Role role,@PathVariable("yema")Integer yema) {
		Page<Object> startPage = PageHelper.startPage(yema, 15);
		List<Role> list = roleService.selectRoleList(role);
		return PageResult.getPageResult(list,startPage.getTotal());
	}
	
	@RequestMapping(value = "/add/{appId}", method = RequestMethod.POST)
	public JsonResult add(Role role,@PathVariable("appId")Integer appId) {
		return JsonResult.getJson(roleService.insertRole(role,appId));
	}
	
	@RequestMapping(value = "/edit/{appId}", method = RequestMethod.POST)
	public JsonResult edit(Role role,@PathVariable("appId")Integer appId) {
		return JsonResult.getJson(roleService.updateRole(role,appId));
	}
	
	@RequestMapping(value = "/remove/{ids}", method = RequestMethod.GET)
	public JsonResult remove(@PathVariable("ids")String ids) {
		return JsonResult.getJson(roleService.deleteRoleByIds(ids));
	}
}
