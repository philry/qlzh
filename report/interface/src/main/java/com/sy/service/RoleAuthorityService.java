package com.sy.service;

import java.util.List;

import com.sy.entity.Authority;
import com.sy.entity.RoleAuthority;

public interface RoleAuthorityService {

	int deleteRoleAuthorityByRoleId(Integer roleId);

	int insertRoleAuthority(RoleAuthority roleAuthority);
	
	List<RoleAuthority> selectRoleAuthorityListByRoleId(Integer roleId);
	
	List<Authority> selectPcAuthorityListByRoleId(Integer roleId);

	Integer selectAppAuthorityListByRoleId(Integer roleId);

	int insertRoleAuthoritys(Integer roleId, List<Authority> list);

	int deletePcRoleAuthorityByRoleId(Integer roleId);
	
}
