package com.sy.controller;


import com.sy.entity.ProcessCondition;
import com.sy.entity.ProcessDetail;
import com.sy.service.IProcessRunService;
import com.sy.vo.JsonResult;
import com.sy.vo.ProcessVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/run")
public class ProcessRunController {

    @Autowired
    private IProcessRunService runService;


    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public JsonResult addProcess(int runId,String result,int resultType){

        //此处乃任务接口的调用，需要配合orderCheck中消息和任务功能，传入指定的数据
        try {

            runService.updateResult(runId,result,resultType);

        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }

        return JsonResult.buildSuccess(200,"操作成功");
    }


}
