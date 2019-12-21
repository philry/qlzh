package com.sy.controller;


import com.sy.constant.HttpStatusConstant;
import com.sy.entity.MessageData;
import com.sy.entity.MessageType;
import com.sy.service.MessageDataService;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("messageData")
public class MessageController {

    @Autowired
    private MessageDataService messageDataService;


    @RequestMapping(value = "sendMessage",method = RequestMethod.POST)
    public JsonResult postMessage(MessageData messageData,Integer messgaeType){

        MessageData message=null;

        try {
            message = messageDataService.sendMessage(messageData,messgaeType);
        } catch (Exception e) {
            return JsonResult.buildFailure(HttpStatusConstant.FAIL,e.getMessage());
        }

        if(message==null){
            return JsonResult.buildFailure(HttpStatusConstant.FAIL,"发送失败,请稍后再试");
        }else {
            return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS,message);
        }

    }

    @RequestMapping(value = "getAcceptMessage",method = RequestMethod.GET)
    public JsonResult getAcceptMessage(Integer acceptId){

        List<MessageData> list = messageDataService.getAcceptMessage(acceptId);

        if (list.isEmpty()){
            return JsonResult.buildFailure(HttpStatusConstant.FAIL,"暂无消息");
        }

        return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS,list);
    }
    
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public JsonResult updateMessage(@PathVariable("id")Integer id) {
    	int rows = messageDataService.updateStatus(id, "1");
    	return JsonResult.getJson(rows);
    }
}
