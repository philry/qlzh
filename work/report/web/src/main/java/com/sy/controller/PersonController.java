package com.sy.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sy.constant.HttpStatusConstant;
import com.sy.entity.Dept;
import com.sy.entity.Person;
import com.sy.service.DeptService;
import com.sy.service.PersonService;
import com.sy.vo.JsonResult;
import com.sy.vo.PageResult;

@RequestMapping("/person")
@RestController
public class PersonController {

	@Autowired
	private PersonService personService;
	
	@Autowired
	private DeptService deptService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public PageResult getList(Person person) {
		List<Person> list = personService.selectPersonList(person);
		return PageResult.getPageResult(list,null);
	}
	
	@RequestMapping(value = "/lists/{yema}/{size}", method = RequestMethod.GET)
	public PageResult getLists(Person person,@PathVariable("yema")Integer yema,@PathVariable("size")Integer size) {
		Page<Object> startPage = PageHelper.startPage(yema, size);
		List<Person> list = personService.selectPersonList(person);
		long total = startPage.getTotal();
		return PageResult.getPageResult(list,total);
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(Person person) {
		try {
			int rows = personService.insertPerson(person);
			return JsonResult.getJson(rows);
		} catch (RuntimeException e) {
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());

		}
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public JsonResult edit(Person person) {
		try {
			int rows = personService.updatePerson(person);
			return JsonResult.getJson(rows);
		} catch (RuntimeException e) {
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());

		}
	}
	
	@RequestMapping(value = "/querybyid/{id}", method = RequestMethod.GET)
	public JsonResult selectById(@PathVariable("id")Integer id) {
		Person person = personService.selectPersonById(id);
		return JsonResult.buildSuccess(200, person);
	}
	
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public JsonResult removeById(@PathVariable("id")Integer id) {
		return JsonResult.getJson(personService.deletePersonById(id));
	}
	
	@RequestMapping(value = "/selectbydept/{deptId}/{yema}/{size}", method = RequestMethod.GET)
	public PageResult selectByDeptById(@PathVariable("deptId")Integer deptId,@PathVariable("yema")Integer yema,@PathVariable("size")Integer size) {
		// 查询出该部门及所有子部门的id
		List<Integer> deptIds = new ArrayList<>();
		Dept dept = new Dept();
		dept.setPid(deptId);
		List<Dept> list = deptService.getDeptList(dept);
		if(list!=null&&list.size()>=0) {
			for (Dept dept2 : list) {
				deptIds.add(dept2.getId());
				dept.setPid(dept2.getId());
				List<Dept> list2 = deptService.getDeptList(dept);
				if(list2!=null&&list2.size()>0) {
					for (Dept dept3 : list2) {
						deptIds.add(dept3.getId());
					}
				}
			}
		}
		if(deptIds.size()==0||deptIds==null) {
			deptIds.add(deptId);
		}
		if(size!=0) {
			// 分页
			Page<Object> startPage = PageHelper.startPage(yema, size);
			List<Person> persons = personService.selectPersonByDept(deptIds);
			return PageResult.getPageResult(persons,startPage.getTotal());
		}else {
			// 不分页
			List<Person> persons = personService.selectPersonByDept(deptIds);
			return PageResult.getPageResult(persons,null);
		}
	}
	
	@RequestMapping(value = "/deptleader/{yema}", method = RequestMethod.GET)
	public PageResult selectDeptLeader(@PathVariable("yema")Integer yema) {
		List<Dept> deptList = deptService.getDeptList(null);
		Page<Object> startPage = PageHelper.startPage(yema, 10);
		List<Person> list = personService.selectDeptLeader(deptList);
		return PageResult.getPageResult(list,startPage.getTotal());
	}
	
	@RequestMapping(value = "/reset/{id}", method = RequestMethod.POST)
	public JsonResult reset(@PathVariable("id")Integer id) {
		return JsonResult.getJson(personService.resetPasswordById(id));
	}
	
	@RequestMapping(value = "/changepassword/{id}/{password}")
	public JsonResult changePassword(@PathVariable("id")Integer id, @PathVariable("password")String password) {
		return JsonResult.getJson(personService.updatePassword(id,password));
	}
}
