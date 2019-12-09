package com.sy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.sy.entity.Dept;

@Mapper
public interface DeptMapper {

	List<Dept> selectDeptList(Dept dept);

	int insertDept(Dept dept);

	int updateDept(Dept dept);

	int deleteDeptById(Integer id);

	Dept selectDeptById(Integer id);
}
