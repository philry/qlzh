package com.sy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sy.constant.HttpStatusConstant;
import com.sy.entity.Energy;
import com.sy.service.EnergyService;
import com.sy.vo.JsonResult;

@RequestMapping("/energy")
@RestController
public class EnergyController {

	@Autowired
	private EnergyService energyService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public JsonResult getEnergy() {
		List<Energy> energy = energyService.getEnergyList();
		return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS, energy.get(0));
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public JsonResult edit(Energy energy) {
		return JsonResult.getJson(energyService.updateEnergy(energy));
	}
}
