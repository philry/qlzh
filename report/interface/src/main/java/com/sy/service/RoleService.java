package com.sy.service;

import java.util.List;

import com.sy.entity.Role;

public interface RoleService {

	Role selectRoleById(Integer id);

	List<Role> selectRoleList(Role role);

	int insertRole(Role role, Integer appId);

	int deleteRoleByIds(String ids);

	int updateRole(Role role, Integer appId);

}
