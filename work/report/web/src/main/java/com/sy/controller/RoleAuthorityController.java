package com.sy.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sy.entity.Authority;
import com.sy.entity.RoleAuthority;
import com.sy.service.RoleAuthorityService;
import com.sy.vo.JsonResult;

@RequestMapping("roleauthority")
@RestController
public class RoleAuthorityController {

	@Autowired
	private RoleAuthorityService roleAuthorityService;
	
	@RequestMapping(value = "/add/{roleId}", method = RequestMethod.POST)
	public JsonResult add(@PathVariable("roleId")Integer roleId,@RequestBody List<Authority> list) {
		//roleAuthorityService.deleteRoleAuthorityByRoleId(roleId);
		int row = roleAuthorityService.deletePcRoleAuthorityByRoleId(roleId);
		int rows = roleAuthorityService.insertRoleAuthoritys(roleId,list);
		if(list==null||list.size()==0) {
			return JsonResult.getJson(row);
		}
//		int rows = 0;
//		if(list!=null&&list.size()>0) {
//			RoleAuthority roleAuthority = new RoleAuthority();
//			roleAuthority.setRoleId(roleId);
//			for (Authority authority : list) {
//				roleAuthority.setAuthorityId(authority.getId());
//				roleAuthority.setRemark(authority.getRemark());
//				roleAuthority.setStatus("0");
//				roleAuthority.setCreateTime(new Timestamp(new Date().getTime()));
//				roleAuthority.setUpdateTime(roleAuthority.getCreateTime());
//				rows += roleAuthorityService.insertRoleAuthority(roleAuthority);
//			}
//		}
		return JsonResult.getJson(rows);
	}
}
