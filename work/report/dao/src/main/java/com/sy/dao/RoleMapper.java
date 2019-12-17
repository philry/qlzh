package com.sy.dao;

import java.util.List;

import com.sy.entity.Role;

public interface RoleMapper {

	Role selectRoleById(Integer id);

	List<Role> selectRoleList(Role role);

}
