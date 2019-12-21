package com.sy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sy.constant.HttpStatusConstant;
import com.sy.entity.Authority;
import com.sy.service.AuthorityService;
import com.sy.vo.JsonResult;

@RequestMapping("/authority")
@RestController
public class AuthorityController {

	@Autowired
	private AuthorityService authorityService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public JsonResult getAuthorityTree() {
		List<Authority> list = authorityService.selectAuthorityTree();
		return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, list);
	}
	
	@RequestMapping(value = "/app", method = RequestMethod.GET)
	public JsonResult getAppAuthorityList() {
		List<Authority> list = authorityService.selectAppAuthorityList();
		return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, list);
	}
}
