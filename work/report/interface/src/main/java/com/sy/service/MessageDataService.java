package com.sy.service;


import com.sy.entity.MessageData;
import com.sy.entity.Task;

import java.util.List;

public interface MessageDataService {

    MessageData sendMessage(MessageData messageData, Integer type) throws Exception;

    List<MessageData> getAcceptMessage(Integer acceptId);

    List<MessageData> getSendMessage(Integer sendId);

    int deleteByStatus(String status);
    
    int updateStatus(int id,String status);

    void sendMessageToPersonsByTask(Task task);
}
