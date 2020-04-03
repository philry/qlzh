package com.sy.controller;

import com.sy.service.IRoleService;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zrole")
public class IRoleController {

    @Autowired
    private IRoleService roleService;

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public JsonResult getAllRoles(){

        try {
            return JsonResult.buildSuccess(200,roleService.getAllRole(0));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }
    }


}
