package com.sy.dao;

import java.util.List;

import com.sy.entity.Role;

public interface RoleMapper {

	Role selectRoleById(Integer id);

	List<Role> selectRoleList(Role role);

	int insertRole(Role role);

	int updateRole(Role role);

	int deleteRoleByIds(String[] ids);

}
