package com.sy.dao;

import com.sy.entity.MessageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageDataDao extends JpaRepository<MessageData,Integer>, JpaSpecificationExecutor {


    List<MessageData> getMessageDataByAccpetId(Integer acceptId);

    List<MessageData> getMessageDataBySendId(Integer sendId);

}