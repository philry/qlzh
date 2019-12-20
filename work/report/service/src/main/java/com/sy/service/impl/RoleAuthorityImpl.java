package com.sy.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.dao.AuthorityMapper;
import com.sy.dao.RoleAuthorityMapper;
import com.sy.entity.Authority;
import com.sy.entity.RoleAuthority;
import com.sy.service.RoleAuthorityService;

@Service
public class RoleAuthorityImpl implements RoleAuthorityService{

	@Autowired
	private RoleAuthorityMapper roleAuthorityMapper;
	
	@Autowired
	private AuthorityMapper authorityMapper;

	@Override
	public int deleteRoleAuthorityByRoleId(Integer roleId) {
		return roleAuthorityMapper.deleteRoleAuthorityByRoleId(roleId);
	}

	@Override
	public int insertRoleAuthority(RoleAuthority roleAuthority) {
		return roleAuthorityMapper.insertRoleAuthority(roleAuthority);
	}

	@Override
	public List<RoleAuthority> selectRoleAuthorityListByRoleId(Integer roleId) {
		List<RoleAuthority> list = roleAuthorityMapper.selectRoleAuthorityListByRoleId(roleId);
		setRoleAuthority(list);
		return list;
	}
	
	private void setRoleAuthority(List<RoleAuthority> list) {
		for (RoleAuthority roleAuthority : list) {
			setRoleAuthority(roleAuthority);
		}
	}
	
	private void setRoleAuthority(RoleAuthority roleAuthority) {
		Authority a = authorityMapper.selectAuthorityById(roleAuthority.getAuthorityId());
		if(a!=null) {
			roleAuthority.setAuthority(a);
		}
	}

	@Override
	public List<Authority> selectPcAuthorityListByRoleId(Integer roleId) {
		List<RoleAuthority> list = roleAuthorityMapper.selectRoleAuthorityListByRoleId(roleId);
		List<Authority> authoritys = new ArrayList<>();
		List<Authority> pAuthoritys = new ArrayList<>();
		List<Authority> sAuthoritys = new ArrayList<>();
		List<Authority> ssAuthoritys = new ArrayList<>();
		for (RoleAuthority r : list) {
			Authority authority = authorityMapper.selectAuthorityById(r.getAuthorityId());
			authority.setRemark(r.getRemark());
			authoritys.add(authority);
		}
		for (Authority a : authoritys) {
			if(a.getLevel()==1&&a.getId()!=17) {
				pAuthoritys.add(a);
			}else if(a.getLevel()==2&&a.getPid()!=17) {
				sAuthoritys.add(a);
			}else {
				ssAuthoritys.add(a);
			}
		}
		for (Authority a : pAuthoritys) {
			if(!("0".equals(a.getRemark()))) {
				List<Authority> sList = new ArrayList<>();
				for (Authority s : sAuthoritys) {
					if(s.getPid()==a.getId()) {
						sList.add(s);
					}
					if(s.getId()==14&&ssAuthoritys!=null&&ssAuthoritys.size()>0) {
						s.setsList(ssAuthoritys);
					}
					a.setsList(sList);
				}
			}
		}
		return pAuthoritys;
	}

	@Override
	public Integer selectAppAuthorityListByRoleId(Integer roleId) {
		List<RoleAuthority> list = roleAuthorityMapper.selectRoleAuthorityListByRoleId(roleId);
		setRoleAuthority(list);
		Integer id = null;
		for (RoleAuthority roleAuthority : list) {
			if(roleAuthority.getAuthority().getPid()==17) {
				id=roleAuthority.getAuthorityId();
			}
		}
		return id;
	}

	@Override
	public int insertRoleAuthoritys(Integer roleId, List<Authority> list) {
		RoleAuthority roleAuthority = new RoleAuthority();		
		roleAuthority.setRoleId(roleId);
		Authority a = null;
		for (Authority authority : list) {
			a = authorityMapper.selectAuthorityById(authority.getId());
			if(a!=null) {
				authority.setPid(a.getPid());
				authority.setLevel(a.getLevel());
			}
		}
		HashSet<Authority> fSet = new HashSet<>();
		for (Authority authority : list) {
			if(authority.getLevel()==2){
				Authority fAuthority = authorityMapper.selectAuthorityById(authority.getPid());
				fAuthority.setRemark("2");
				fSet.add(fAuthority);
			}
		}
		List<Authority> authorities = new ArrayList<>();
		for (Authority authority : fSet) {
			authorities.add(authority);
		}
		for (Authority authority : list) {
			if(authority.getLevel()!=1) {
				authorities.add(authority);
			}
		}
		int rows = 0;
		for (Authority authority : authorities) {
			roleAuthority.setAuthorityId(authority.getId());
			roleAuthority.setRemark(authority.getRemark());
			roleAuthority.setCreateTime(new Timestamp(new Date().getTime()));
			roleAuthority.setUpdateTime(roleAuthority.getCreateTime());
			roleAuthority.setStatus("0");
			rows += roleAuthorityMapper.insertRoleAuthority(roleAuthority);
		}
		return rows;
	}

	@Override
	public int deletePcRoleAuthorityByRoleId(Integer roleId) {
		int rows=0;
		List<RoleAuthority> list = roleAuthorityMapper.selectRoleAuthorityListByRoleId(roleId);
		for (RoleAuthority roleAuthority : list) {
			Authority a = authorityMapper.selectAuthorityById(roleAuthority.getAuthorityId());
			if(a.getId()!=17&&a.getPid()!=17) {
				rows += roleAuthorityMapper.deleteRoleAuthorityById(roleAuthority.getId());
			}
		}
		return rows;
	}
}
