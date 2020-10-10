package com.sy.dao;

import java.util.List;

import com.sy.entity.Authority;

public interface AuthorityMapper {

	List<Authority> selectAuthorityList(Authority authority);
	
	Authority selectAuthorityById(Integer id);

}
