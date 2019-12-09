package com.sy.service.impl;

import com.sy.dao.MessageDataDao;
import com.sy.entity.MessageData;
import com.sy.entity.MessageType;
import com.sy.exception.SysException;
import com.sy.service.MessageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessageDataServiceImpl implements MessageDataService {

    @Autowired
    private MessageDataDao messageDataDao;

    @Override
    public MessageData sendMessage(MessageData messageData,Integer type) throws SysException {

        if (type==null){
            throw new SysException("请选择消息类型");
        }
        MessageType messageType = new MessageType(type);
        messageData.setMessageType(messageType);

        return messageDataDao.save(messageData);
    }

    @Override
    public List<MessageData> getAcceptMessage(Integer acceptId) {

        return messageDataDao.getMessageDataByAccpetId(acceptId);
    }

    @Override
    public List<MessageData> getSendMessage(Integer sendId) {
        return messageDataDao.getMessageDataBySendId(sendId);
    }


}
