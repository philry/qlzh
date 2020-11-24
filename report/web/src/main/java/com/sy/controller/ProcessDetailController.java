package com.sy.controller;


import com.sy.entity.ProcessCondition;
import com.sy.entity.ProcessDetail;
import com.sy.json.JSONObject;
import com.sy.service.IProcessConditionService;
import com.sy.service.IProcessDetailService;
import com.sy.vo.JsonResult;
import com.sy.vo.ProcessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/detail")
public class ProcessDetailController {


    @Autowired
    private IProcessDetailService detailService;


    @Autowired
    private IProcessConditionService conditionService;


    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public JsonResult addProcess(@RequestBody ProcessVo info){

        List<ProcessDetail> details = info.getLiuchengList();

        String tableType = details.get(0).getTableType();

        if(detailService.selectTableType(tableType)>0){
            return JsonResult.buildFailure(404,"此表单类型的流程已经存在，请删除后再重新添加");
        }

        List<ProcessCondition> conditions = info.getTiaojianList();

        for (int i = 0; i <details.size() ; i++) {

            details.get(i).setCreateTime(new Date());
            details.get(i).setStatus("0");

            detailService.insertProcessDetail(details.get(i));

        }

        for (ProcessCondition condition : conditions) {

            condition.setCreateTime(new Date());
            condition.setStatus("0");
            conditionService.insertProcessCondition(condition);

        }



        return JsonResult.buildSuccess(200,"操作成功");
    }

}
