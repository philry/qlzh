package com.sy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sy.constant.HttpStatusConstant;
import com.sy.entity.Xpg;
import com.sy.service.XpgService;
import com.sy.vo.JsonResult;
import com.sy.vo.PageResult;

@RequestMapping("/xpg")
@RestController
public class XpgController {

	@Autowired
	private XpgService xpgService;
	
	@RequestMapping(value = "/querybyid/{id}", method = RequestMethod.GET)
	public JsonResult selectXpgById(@PathVariable("id")Integer id) {
		return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, xpgService.selectXpgById(id));
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(Xpg xpg) {
		return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, xpgService.insertXpg(xpg));
	}
	
	@RequestMapping(value = "/list/{yema}/{size}", method = RequestMethod.GET)
	public PageResult getList(Xpg xpg,@PathVariable("yema")Integer yema,@PathVariable("size")Integer size) {
		Page<Object> startPage = PageHelper.startPage(yema, size);
		List<Xpg> list = xpgService.selectXpgList(xpg);
		return PageResult.getPageResult(list, startPage.getTotal());
	}
}
