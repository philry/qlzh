package com.sy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.dao.RoleMapper;
import com.sy.entity.Role;
import com.sy.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleMapper roleMapper;

	@Override
	public Role selectRoleById(Integer id) {
		return roleMapper.selectRoleById(id);
	}

	@Override
	public List<Role> selectRoleList(Role role) {
		return roleMapper.selectRoleList(role);
	}
}
