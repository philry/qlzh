package com.sy.controller;

import com.sy.constant.HttpStatusConstant;
import com.sy.entity.Emergency;
import com.sy.service.EmergencyService;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/emergency")
@RestController
public class EmergencyController {

	@Autowired
	private EmergencyService emergencyService;
	
	@RequestMapping(value = "/showState", method = RequestMethod.GET)
	public JsonResult getEmergency() {
		List<Emergency> emergency = emergencyService.getEmergencyList();
		return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, emergency.get(0));
	}
}
