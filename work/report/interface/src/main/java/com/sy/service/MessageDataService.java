package com.sy.service;


import com.sy.entity.MessageData;

import java.util.List;

public interface MessageDataService {

    MessageData sendMessage(MessageData messageData,Integer type) throws Exception;

    List<MessageData> getAcceptMessage(Integer acceptId);

    List<MessageData> getSendMessage(Integer sendId);

}
