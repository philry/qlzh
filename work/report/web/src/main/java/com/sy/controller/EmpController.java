package com.sy.controller;


import com.sy.entity.Emp;
import com.sy.service.IEmpService;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/emp")
public class EmpController {

    @Autowired
    private IEmpService empService;

    @RequestMapping(value = "/{roleId}",method = RequestMethod.GET)
    public JsonResult getEmpByRole(@PathVariable("roleId")int roleId){

        List<Emp> list = null;

        try {
            list = empService.getEmpByRoleId(roleId);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }

        return JsonResult.buildSuccess(200,list);
    }

}
