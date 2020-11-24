package com.sy.controller;


import com.sy.service.IDataTypeService;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataTypeController {

    @Autowired
    private IDataTypeService dataTypeService;

    @RequestMapping(value = "/all",method = RequestMethod.GET)
    public JsonResult getAllField(){

        try {
            return JsonResult.buildSuccess(200,dataTypeService.getField());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }


    }

}
