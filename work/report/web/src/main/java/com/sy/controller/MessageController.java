package com.sy.controller;


import com.sy.constant.HttpStatusConstant;
import com.sy.entity.MessageData;
import com.sy.entity.MessageType;
import com.sy.service.MessageDataService;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("messageData")
public class MessageController {

    @Autowired
    private MessageDataService messageDataService;


    @RequestMapping(value = "create",method = RequestMethod.POST)
    public JsonResult postMessage(MessageData messageData,Integer messgaeType){

        System.out.println(messageData);

        MessageType messageType = new MessageType();
        messageType.setId(messgaeType);
        messageData.setMessageType(messageType);

        return JsonResult.buildSuccess(HttpStatusConstant.SUCCESS,messageDataService.saveMessage(messageData));
    }

}
