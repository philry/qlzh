package com.sy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sy.constant.HttpStatusConstant;
import com.sy.entity.Authority;
import com.sy.entity.Person;
import com.sy.entity.RoleAuthority;
import com.sy.service.PersonService;
import com.sy.service.RoleAuthorityService;
import com.sy.vo.LoginResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/login")
@RestController
public class LoginController {

	@Autowired
	private PersonService personService;
	
	@Autowired
	private RoleAuthorityService roleAuthorityService;
	
	@RequestMapping(value = "/{type}", method = RequestMethod.POST)
	public LoginResult login(Person person, @PathVariable("type")Integer type) {
		LoginResult loginResult = new LoginResult();

		/*response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization,token, content-type"); //这里要加上content-type
		response.setHeader("Access-Control-Allow-Credentials", "true");
		loginResult.setResponse(response);*/

		if(person.getPhone()==null||person.getPhone()==""||person.getPassword()==null||person.getPassword()=="") {
			loginResult.setCode(HttpStatusConstant.FAIL);
			loginResult.setMessage("用户名或密码不能为空");
			return loginResult;
		}
		List<Person> p = personService.selectPersonList(person);
		if(p==null||p.size()==0) {
			loginResult.setCode(HttpStatusConstant.FAIL);
			loginResult.setMessage("用户名或密码错误");
			return loginResult;
		}else {
			loginResult.setCode(HttpStatusConstant.SUCCESS);
			loginResult.setMessage("登陆成功");
			loginResult.setPerson(p.get(0));
			// 权限信息
			if(type==0) {
				List<Authority> list = roleAuthorityService.selectPcAuthorityListByRoleId(p.get(0).getRoleId());
				loginResult.setObj(list);
			}else {
				Integer id = roleAuthorityService.selectAppAuthorityListByRoleId(p.get(0).getRoleId());
				loginResult.setObj(id);
			}
			
			return loginResult;
		}
	}
}
