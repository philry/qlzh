package com.sy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.dao.DeptMapper;
import com.sy.entity.Dept;
import com.sy.service.DeptService;

@Service
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptMapper deptMapper;
	
	@Override
	public List<Dept> getDeptList(Dept dept) {
		return deptMapper.selectDeptList(dept);
	}

	@Override
	public int insertDept(Dept dept) {
		return deptMapper.insertDept(dept);
	}

	@Override
	public int updateDept(Dept dept) {
		return deptMapper.updateDept(dept);
	}

	@Override
	public int deleteDeptById(Integer id) {
		return deptMapper.deleteDeptById(id);
	}

	@Override
	public Dept selectDeptById(Integer id) {
		return deptMapper.selectDeptById(id);
	}

}
