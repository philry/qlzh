package com.sy.dao;

import com.sy.entity.MessageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageDataDao extends JpaRepository<MessageData,Integer> {


}
