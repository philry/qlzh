package com.sy.service;

import com.sy.entity.Netty;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface NettyService {

    Netty insertData(Netty netty);

    List<Netty> getAllByDate(Date beginTime,Date endTime);

    Page<Netty> getAllByName(String xpg, int page, int pageSize);

}
