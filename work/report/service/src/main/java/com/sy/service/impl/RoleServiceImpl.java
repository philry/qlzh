package com.sy.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sy.dao.AuthorityMapper;
import com.sy.dao.RoleAuthorityMapper;
import com.sy.dao.RoleMapper;
import com.sy.entity.Authority;
import com.sy.entity.Role;
import com.sy.entity.RoleAuthority;
import com.sy.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private RoleAuthorityMapper roleAuthorityMapper;
	
	@Autowired
	private AuthorityMapper authorityMapper;

	@Override
	public Role selectRoleById(Integer id) {
		Role role = roleMapper.selectRoleById(id);
		setRole(role);
		return role;
	}

	@Override
	public List<Role> selectRoleList(Role role) {
		List<Role> list = roleMapper.selectRoleList(role);
		setRole(list);
		return list;
	}

	@Override
	@Transactional
	public int insertRole(Role role, Integer appId) {
		role.setCreateTime(new Timestamp(new Date().getTime()));
		role.setUpdateTime(role.getCreateTime());
		roleMapper.insertRole(role);
		RoleAuthority roleAuthority = new RoleAuthority();
		roleAuthority.setRoleId(role.getId());
		roleAuthority.setAuthorityId(appId);
		roleAuthority.setStatus("0");
		roleAuthority.setCreateTime(new Timestamp(new Date().getTime()));
		roleAuthority.setUpdateTime(roleAuthority.getCreateTime());
		return roleAuthorityMapper.insertRoleAuthority(roleAuthority);
	}

	@Override
	@Transactional
	public int updateRole(Role role, Integer appId) {
		List<RoleAuthority> list = roleAuthorityMapper.selectRoleAuthorityListByRoleId(role.getId());
		boolean flag = false;
		for (RoleAuthority roleAuthority : list) {
			Authority a = authorityMapper.selectAuthorityById(roleAuthority.getAuthorityId());
//			if(a.getPid()==17) {
//				roleAuthority.setAuthorityId(appId);
//				roleAuthority.setUpdateTime(new Timestamp(new Date().getTime()));
//				roleAuthorityMapper.updateRoleAuthority(roleAuthority);
//			}
			if(a.getPid()==17) {
				flag=true;
				roleAuthorityMapper.deleteRoleAuthorityById(roleAuthority.getId());
				RoleAuthority r = new RoleAuthority();
				r.setRoleId(role.getId());
				r.setAuthorityId(appId);
				r.setStatus("0");
				r.setCreateTime(new Timestamp(new Date().getTime()));
				r.setUpdateTime(r.getCreateTime());
				roleAuthorityMapper.insertRoleAuthority(r);
			}
			
		}
		if(!flag) {
			RoleAuthority r = new RoleAuthority();
			r.setRoleId(role.getId());
			r.setAuthorityId(appId);
			r.setStatus("0");
			r.setCreateTime(new Timestamp(new Date().getTime()));
			r.setUpdateTime(r.getCreateTime());
			roleAuthorityMapper.insertRoleAuthority(r);
		}
		role.setUpdateTime(new Timestamp(new Date().getTime()));
		return roleMapper.updateRole(role);
	}

	@Override
	@Transactional
	public int deleteRoleByIds(String ids) {
		String[] roleIds = ids.split(",");
		for (int i = 0; i < roleIds.length; i++) {
			roleAuthorityMapper.deleteRoleAuthorityByRoleId(Integer.valueOf(roleIds[i]));
		}
		return roleMapper.deleteRoleByIds(roleIds);
	}
	
	private void setRole(List<Role> list) {
		for (Role role : list) {
			setRole(role);
		}
	}
	
	private void setRole(Role role) {
		List<RoleAuthority> list = roleAuthorityMapper.selectRoleAuthorityListByRoleId(role.getId());
		for (RoleAuthority roleAuthority : list) {
			Authority a = authorityMapper.selectAuthorityById(roleAuthority.getAuthorityId());
			if(a.getPid()==17) {
				role.setApp(a);
			}
		}
	}
}
