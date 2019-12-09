package com.sy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sy.dao.PersonMapper;
import com.sy.entity.Person;
import com.sy.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonMapper personMapper;

	@Override
	public List<Person> selectPersonList(Person person) {
		return personMapper.selectPersonList(person);
	}

	@Override
	public int insertPerson(Person person) {
		return personMapper.insertPerson(person);
	}

	@Override
	public int updatePerson(Person person) {
		return personMapper.updatePerson(person);
	}

	@Override
	public Person selectPersonById(Integer id) {
		return personMapper.selectPersonById(id);
	}

	@Override
	public int deletePersonById(Integer id) {
		return personMapper.deletePersonById(id);
	}


}
