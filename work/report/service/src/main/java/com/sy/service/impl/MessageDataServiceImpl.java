package com.sy.service.impl;

import com.sy.dao.MachineMapper;
import com.sy.dao.MessageDataDao;
import com.sy.dao.MessageTypeDao;
import com.sy.entity.Machine;
import com.sy.entity.MessageData;
import com.sy.entity.MessageType;
import com.sy.service.MessageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Service
public class MessageDataServiceImpl implements MessageDataService {

    @Autowired
    private MessageDataDao messageDataDao;

    @Autowired
    private MessageTypeDao messageTypeDao;
    
    @Autowired
    private MachineMapper machineMapper;

    @Override
    @Transactional
    public MessageData sendMessage(MessageData messageData,Integer type) throws Exception {

        if (type==null){
            throw new Exception("请选择消息类型");
        }
        MessageType messageType = new MessageType(type);
        messageData.setMessageType(messageType);
        messageData.setCreateTime(new Timestamp(new Date().getTime()));
        messageData.setUpdateTime(new java.sql.Date(new Date().getTime()));
        messageData.setStatus("0");
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

    @Override
    @Transactional
    public int deleteByStatus(String status) {
    	return messageDataDao.deleteByStatus(status);
    }

	@Override
	@Transactional
	public int updateStatus(int id, String status) {
		MessageData messageData = messageDataDao.getOne(id);
		if(messageData.getMessageType().getId()==1) {
			Machine machine = machineMapper.selectMachineById(Integer.valueOf(messageData.getContext()));
			machine.setLastMaintenanceTime(new java.sql.Date(new Date().getTime()));
			machine.setUpdateTime(new Timestamp(new Date().getTime()));
			machineMapper.updateMachine(machine);
		}else {
			// TODO 
		}
		return messageDataDao.updateStatus(id, status);
	}
}
