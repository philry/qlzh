package com.sy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.dao.AuthorityMapper;
import com.sy.entity.Authority;
import com.sy.service.AuthorityService;

@Service
public class AuthorityServiceImpl implements AuthorityService {
	
	@Autowired
	private AuthorityMapper authorityMapper;

	@Override
	public List<Authority> selectAuthorityTree() {
		Authority authority = new Authority();
		authority.setPid(0);
		List<Authority> fList = authorityMapper.selectAuthorityList(authority);
		List<Authority> sList = null;
		List<Authority> ssList = null;
		for (Authority a : fList) {
			authority.setPid(a.getId());
			sList = authorityMapper.selectAuthorityList(authority);
			if(sList!=null) {
				for (Authority a2 : sList) {
					authority.setPid(a2.getId());
					ssList = authorityMapper.selectAuthorityList(authority);
					if(ssList!=null&&ssList.size()>0) {
						a2.setsList(ssList);
					}
				}
				a.setsList(sList);
			}
		}
		return fList;
	}

	@Override
	public List<Authority> selectAppAuthorityList() {
		Authority authority = new Authority();
		authority.setPid(17);
		return authorityMapper.selectAuthorityList(authority);
	}

}
