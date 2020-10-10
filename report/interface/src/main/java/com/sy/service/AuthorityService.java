package com.sy.service;

import java.util.List;

import com.sy.entity.Authority;

public interface AuthorityService {

	List<Authority> selectAuthorityTree();

	List<Authority> selectAppAuthorityList();

}
