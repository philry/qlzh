package com.sy.dao;


import com.sy.entity.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageTypeDao extends JpaRepository<MessageType,Integer>, JpaSpecificationExecutor {

    MessageType getById(Integer id);

}
