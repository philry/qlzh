package com.sy.dao;

import java.util.List;

import com.sy.entity.RoleAuthority;

public interface RoleAuthorityMapper {

	int deleteRoleAuthorityByRoleId(Integer roleId);

	int insertRoleAuthority(RoleAuthority roleAuthority);
	
	int updateRoleAuthority(RoleAuthority roleAuthority);

	List<RoleAuthority> selectRoleAuthorityListByRoleId(Integer roleId);
	
	int deleteRoleAuthorityById(Integer id);

}
