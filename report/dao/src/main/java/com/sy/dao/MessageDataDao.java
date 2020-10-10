package com.sy.dao;

import com.sy.entity.MessageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageDataDao extends JpaRepository<MessageData,Integer>, JpaSpecificationExecutor {


    List<MessageData> getMessageDataByAccpetId(Integer acceptId);
    
    List<MessageData> getMessageDataByAccpetIdAndStatus(Integer acceptId,String status);
    
    List<MessageData> getMessageDataBySendId(Integer sendId);

    int deleteByStatus(String status);

    @Query("update MessageData m set m.status=?2 where m.id=?1")
    @Modifying
    int updateStatus(int id,String status);

}
