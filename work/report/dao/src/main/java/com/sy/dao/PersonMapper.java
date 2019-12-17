package com.sy.dao;

import java.util.List;

import com.sy.entity.Person;

public interface PersonMapper{

	List<Person> selectPersonList(Person person);

	int insertPerson(Person person);

	int updatePerson(Person person);

	Person selectPersonById(Integer id);

	int deletePersonById(Integer id);

	List<Person> selectPersonByDeptIds(List<Integer> ids);

	List<Person> selectDeptLeaderByIds(List<Integer> ids);

}
