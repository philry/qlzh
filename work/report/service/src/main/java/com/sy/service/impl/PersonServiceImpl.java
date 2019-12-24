package com.sy.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern.Flag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.sy.dao.DeptMapper;
import com.sy.dao.PersonMapper;
import com.sy.dao.RoleMapper;
import com.sy.entity.Dept;
import com.sy.entity.Person;
import com.sy.entity.Role;
import com.sy.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {

	@Autowired
	private PersonMapper personMapper;
	
	@Autowired
	private DeptMapper deptMapper;
	
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public List<Person> selectPersonList(Person person) {
		List<Person> list = personMapper.selectPersonList(person);
		setPerson(list);
		return list;
	}

	@Override
	public int insertPerson(Person person) {
		Person p = new Person();
		p.setPhone(person.getPhone());
		List<Person> list = personMapper.selectPersonList(p);
		if(list==null||list.size()==0) {
			person.setPassword("123456");
			person.setCreateTime(new Timestamp(new Date().getTime()));
			person.setUpdateTime(person.getCreateTime());
			return personMapper.insertPerson(person);
		}else {
			throw new RuntimeException("该手机号已存在!");
		}
	}

	@Override
	public int updatePerson(Person person) {
		Person p = new Person();
		p.setPhone(person.getPhone());
		List<Person> list = personMapper.selectPersonList(p);
		if(list==null||list.size()==0) {
			person.setUpdateTime(new Timestamp(new Date().getTime()));
			return personMapper.updatePerson(person);
		}else {
			boolean flag = true;
			for (Person person2 : list) {
				if(person2.getId()!=person.getId());
				flag=false;
			}
			if(flag) {
				throw new RuntimeException("该手机号已存在!");
			}else {
				person.setUpdateTime(new Timestamp(new Date().getTime()));
				return personMapper.updatePerson(person);
			}
//			if(list.get(0).getId()==person.getId()) {
//				person.setUpdateTime(new Timestamp(new Date().getTime()));
//				return personMapper.updatePerson(person);
//			}else {
//				throw new RuntimeException("该手机号已存在!");
//			}
		}
		
	}

	@Override
	public Person selectPersonById(Integer id) {
		Person person = personMapper.selectPersonById(id);
		setPerson(person);
		return person;
	}

	@Override
	public int deletePersonById(Integer id) {
		return personMapper.deletePersonById(id);
	}

	private void setPerson(List<Person> list) {
		for (Person person : list) {
			setPerson(person);
		}
	}
	
	private void setPerson(Person person) {
		Dept dept = deptMapper.selectDeptById(person.getDeptId());
		if(dept!=null)
			person.setDept(dept);
		Role role = roleMapper.selectRoleById(person.getRoleId());
		if(role!=null)
			person.setRole(role);
	}

	@Override
	public List<Person> selectPersonByDept(List<Integer> deptIds){
		List<Person> list = personMapper.selectPersonByDeptIds(deptIds);
		setPerson(list);
		return list;
	}

	@Override
	public List<Person> selectDeptLeader(List<Dept> deptList) {
		List<Integer> ids = new ArrayList<>();
		for (Dept dept : deptList) {
			if(dept.getLeader()!=null)
				ids.add(dept.getLeader());
		}
		List<Person> list = personMapper.selectDeptLeaderByIds(ids);
		setPerson(list);
		return list;
	}

	@Override
	public int resetPasswordById(Integer id) {
		Person person = new Person();
		person.setId(id);
		person.setPassword("123456");
		person.setUpdateTime(new Timestamp(new Date().getTime()));
		return personMapper.updatePerson(person);
	}

	@Override
	public int updatePassword(Integer id, String password) {
		Person person = new Person();
		person.setId(id);
		person.setPassword(password);
		person.setUpdateTime(new Timestamp(new Date().getTime()));
		return personMapper.updatePerson(person);
	}
}
