package com.sy.controller;


import com.sy.service.MachineNowService;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("machineNow")
public class MachineNowController {

    @Autowired
    private MachineNowService machineNowService;


    @RequestMapping("use/{personId}")
    public JsonResult getUsedMachineByPersonId(@PathVariable("personId") int personId){

        return JsonResult.buildSuccess(200,machineNowService.getNowMachineByPerson(personId));
    }

}
