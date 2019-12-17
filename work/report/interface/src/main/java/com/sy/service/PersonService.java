package com.sy.service;

import java.util.List;

import com.sy.entity.Dept;
import com.sy.entity.Person;

public interface PersonService {

	List<Person> selectPersonList(Person person);
	
	int insertPerson(Person person);

	int updatePerson(Person person);

	Person selectPersonById(Integer id);

	int deletePersonById(Integer id);

	List<Person> selectDeptLeader(List<Dept> deptList);

	List<Person> selectPersonByDept(List<Integer> deptIds);

}
