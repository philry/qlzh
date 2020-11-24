package com.sy.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sy.constant.HttpStatusConstant;
import com.sy.entity.Dept;
import com.sy.entity.MessageData;
import com.sy.entity.Person;
import com.sy.entity.Task;
import com.sy.service.DeptService;
import com.sy.service.MessageDataService;
import com.sy.service.PersonService;
import com.sy.service.TaskService;
import com.sy.vo.MessageNumber;

@RequestMapping("/messagenum")
@RestController
public class MessageNumberController {
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private DeptService deptService;
	
	@Autowired
	private MessageDataService messageDateService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public MessageNumber getNum(@PathVariable("id")Integer id) {
		MessageNumber m = new MessageNumber();
		Person person = personService.selectPersonById(id);//当前登录的人员
		Task task = new Task();
		task.setChecker(person.getId());
		task.setStatus("0");
		task.setCheckingStatus("1");
		List<Task> checkList = taskService.selectTaskList(task);
		if(checkList==null||checkList.size()==0) {
			m.setCheck(0);
		}else {
			m.setCheck(checkList.size());
		}
		task.setCheckingStatus("0");
		task.setChecker(null);
		task.setPersonId(person.getId());
		List<Task> missionList = taskService.selectTaskList(task);
				if(missionList==null||missionList.size()==0) {
			m.setMission(0);
		}else {
			List<Task> list = new ArrayList<>();
			for (Task task2 : missionList) {
				if(task2.getActualEndTime()==null) {
					list.add(task2);
				}
			}
			m.setMission(list.size());
		}
		List<Integer> deptIds = new ArrayList<>();
		List<Dept> deptList = deptService.getDeptList(null);
		/*for (Dept d : deptList) {
			if(d.getLeader()!=null) {
				if(d.getLeader()==person.getId()) {
					deptIds.add(d.getId());
				}
			}
		}*/
		for (Dept dept : deptList) {
			if(dept.getLeader()!=null) {
				if(dept.getOperator()==null) {//operator为null表示新建与审核是同一人
					//Integer超过128就不能用==判断相等了，所以用String判断
					if (dept.getLeader().toString().equals(Integer.toString(person.getId()))) {
						deptIds.add(dept.getId());
					}
				}else{ //operatorb不为null表示新建与审核不是同一人
					if(dept.getOperator().toString().equals(Integer.toString(person.getId()))){
						deptIds.add(dept.getId());
					}
				}
			}
		}
		List<Task> todoList =new ArrayList<>();
		if(deptIds.size()==0) {
			m.setTodo(0);
		}else {
			task.setPersonId(null);
			for (Integer deptId : deptIds) {
				task.setDeptId(deptId);
				List<Task> todos = taskService.selectTaskList(task);
				if(todos!=null&&todos.size()>0) {
					for (Task t : todos) {
						todoList.add(t);
					}
				}
			}
		}
		if(todoList==null||todoList.size()==0) {
			m.setTodo(0);
		}else {
			m.setTodo(todoList.size());
		}
		List<MessageData> messageDataList = messageDateService.getAcceptMessage(person.getId());
		if(messageDataList==null|messageDataList.size()==0) {
			m.setWarn(0);
		}else {
			m.setWarn(messageDataList.size());
		}
		m.setTotal(m.getCheck()+m.getMission()+m.getTodo()+m.getWarn());
		m.setCode(HttpStatusConstant.SUCCESS);
		return m;
	}
}
