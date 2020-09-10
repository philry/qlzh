package com.sy.service;

import com.sy.entity.Netty;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface NettyService {

    Netty insertData(Netty netty);

    List<Netty> getAllByDate(Date beginTime,Date endTime);

    List<String> getAllXpgsByDate(Date beginTime,Date endTime);

    List<Netty> getAllByDateAndXpgId(String xpgId, Date beginTime,Date endTime);

    Page<Netty> getAllByName(String xpg, int page, int pageSize,Date beginTime,Date endTime);

}
