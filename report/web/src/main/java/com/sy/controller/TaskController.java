package com.sy.controller;

import java.util.ArrayList;
import java.util.List;

import com.sy.entity.Person;
import com.sy.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sy.constant.HttpStatusConstant;
import com.sy.entity.Dept;
import com.sy.entity.Task;
import com.sy.service.DeptService;
import com.sy.service.TaskService;
import com.sy.vo.JsonResult;
import com.sy.vo.PageResult;


@RequestMapping("/task")
@RestController
public class TaskController {

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private DeptService deptService;

	@Autowired
	private PersonService personService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public PageResult getList(Task task) {
		List<Task> list = taskService.selectTaskList(task);
		return PageResult.getPageResult(list);
	}
	
	@RequestMapping(value = "/lists/{yema}", method = RequestMethod.GET)
//	public PageResult getLists(Task task, @PathVariable("yema")Integer yema) {
	public PageResult getLists(Integer deptId, @PathVariable("yema")Integer yema) {
		Page<Object> startPage = PageHelper.startPage(yema, 15);
		Task task = new Task();
		task.setDeptId(deptId);
		List<Task> list = taskService.selectTaskLists(task);

		/*
		Person person = personService.selectPersonById(personId);
		List<Task> list1 =new ArrayList<>();
		for(Task task1 :list){
			if(task1.getDeptId()!=null){
				if(task1.getDeptId().equals(person.getDeptId())){
					list1.add(task1);
				}
			}
		}*/
		return PageResult.getPageResult(list,startPage.getTotal());
	}
	
	@RequestMapping(value = "/querybyid/{id}", method = RequestMethod.GET)
	public JsonResult selectTaskById(@PathVariable("id")Integer id) {
		return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, taskService.selectTaskById(id));
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(Task task) {
		return JsonResult.getJson(taskService.insertTask(task));
	}
	
	@RequestMapping(value = "/removebyid/{id}", method = RequestMethod.GET)
	public JsonResult removeById(@PathVariable("id")Integer id) {
		try {
			taskService.deleteTaskById(id);
			return JsonResult.getJson(1);
		} catch (Exception e) {
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
		}
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
	public JsonResult removeById(Task task,@PathVariable("id")Integer id) {
		try {
			taskService.modifyTaskById(task,id);
			return JsonResult.getJson(1);
		} catch (Exception e) {
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
		}
	}

	@RequestMapping(value = "/addson/{pid}", method = RequestMethod.POST)
	public JsonResult addSonTask(Task task,@PathVariable("pid")Integer pid) {
		try {
			int rows = taskService.insertSonTask(task,pid);
			return JsonResult.getJson(rows);
		} catch (RuntimeException e) {
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
		}
		
	}

	@RequestMapping(value ="/split/{id}/{personIds}", method = RequestMethod.POST)
	public JsonResult splitTask(Task task,@PathVariable("id")Integer id,@PathVariable("personIds")String personIds){
		try {
			int rows = taskService.splitTask(task,id,personIds);
			return JsonResult.getJson(rows);
		} catch (RuntimeException e) {
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/check/{type}/{id}", method = RequestMethod.GET)
	public JsonResult checkTask(@PathVariable("type")Integer type,@PathVariable("id")Integer id) {
		try {
			int rows = taskService.checkTask(id,type);
			return JsonResult.getJson(rows);
		} catch (Exception e) {
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/checkstatus/{id}/{type}", method = RequestMethod.GET)
	public JsonResult changeCheckStatus(@PathVariable("id")Integer id,@PathVariable("type")String type) {
		try {
			int rows = taskService.changeCheckStatus(id,type);
			return JsonResult.getJson(rows);
		} catch (RuntimeException e) {
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/todo/{id}", method = RequestMethod.GET)
	public PageResult getTodoList(@PathVariable("id")Integer id,Task task) {
		List<Integer> deptIds = new ArrayList<>();
		List<Dept> deptList = deptService.getDeptList(null);
		for (Dept dept : deptList) {
			if(dept.getLeader()!=null) {
				if(dept.getOperator()==null) {

					if (dept.getLeader().toString().equals(id.toString())) {
						deptIds.add(dept.getId());
					}
				}else{
					if(dept.getOperator().toString().equals(id.toString())){
						deptIds.add(dept.getId());
					}
				}
			}
		}
		if(deptIds.size()==0) {
			return PageResult.getPageResult(null);
		}else {
			List<Task> list = taskService.selectTaskByDeptIds(task.getWorkCode(),deptIds);
			if(list.size()==0||list==null) {
				return PageResult.getPageResult(null);
			}else {
				return PageResult.getPageResult(list);
			}
		}
	}


	@RequestMapping(value = "/stopTask/{id}",method = RequestMethod.GET)
	public JsonResult stopTask(@PathVariable("id") Integer id){
		try {
			taskService.stopTaskById(id);
			return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, taskService.selectTaskById(id));
		} catch (RuntimeException e) {
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
		}
	}

	@RequestMapping(value = "/unStopTask/{id}",method = RequestMethod.GET)
	public JsonResult unStopTask(@PathVariable("id") Integer id){
		try {
			taskService.unEndTaskById(id);
			return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, taskService.selectTaskById(id));
		} catch (RuntimeException e) {
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
		}
	}

	@RequestMapping(value = "/endTask/{id}",method = RequestMethod.GET)
	public JsonResult endTask(@PathVariable("id") Integer id){
		try {
			taskService.endTaskById(id);
			return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, taskService.selectTaskById(id));
		} catch (RuntimeException e) {
			return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
		}
	}

    @RequestMapping(value = "/unEndTask/{id}",method = RequestMethod.GET)
    public JsonResult unEndTask(@PathVariable("id") Integer id){
        try {
            taskService.unEndTaskById(id);
            return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, taskService.selectTaskById(id));
        } catch (RuntimeException e) {
            return JsonResult.buildFailure(HttpStatusConstant.FAIL, e.getMessage());
        }
    }
}
