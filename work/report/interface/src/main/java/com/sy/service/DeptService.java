package com.sy.service;

import java.util.List;

import com.sy.entity.Dept;

public interface DeptService {

	List<Dept> getDeptList(Dept dept);

	int insertDept(Dept dept);

	int updateDept(Dept dept);

	int deleteDeptById(Integer id);

	Dept selectDeptById(Integer id);

	List<Dept> selectDeptTree();

	List<Dept> getHigherDept();

}
