package com.sy.controller;


import com.sy.service.IProcessTypeService;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/type")
public class TypeController {

    @Autowired
    private IProcessTypeService processTypeService;

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public JsonResult getAll(){


        try {
            return JsonResult.buildSuccess(200,processTypeService.getType());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }

    }

}
