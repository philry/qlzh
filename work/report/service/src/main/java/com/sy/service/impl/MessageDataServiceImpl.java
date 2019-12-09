package com.sy.service.impl;

import com.sy.dao.MessageDataDao;
import com.sy.entity.MessageData;
import com.sy.service.MessageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageDataServiceImpl implements MessageDataService {

    @Autowired
    private MessageDataDao messageDataDao;

    @Override
    public MessageData saveMessage(MessageData messageData) {



        return messageDataDao.save(messageData);
    }



}
