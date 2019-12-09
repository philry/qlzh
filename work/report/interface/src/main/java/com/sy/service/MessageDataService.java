package com.sy.service;


import com.sy.entity.MessageData;
import com.sy.exception.SysException;

import java.util.List;

public interface MessageDataService {

    MessageData sendMessage(MessageData messageData,Integer type) throws SysException;

    List<MessageData> getAcceptMessage(Integer acceptId);

    List<MessageData> getSendMessage(Integer sendId);

}
