package com.sy.service.impl;

import com.sy.dao.NettyDao;
import com.sy.entity.Netty;
import com.sy.service.NettyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;


@Service
public class NettyServiceImpl implements NettyService {

    @Autowired
    private NettyDao nettyDao;


    @Override
    public Netty insertData(Netty netty) {

        netty.setCreateTime(new Timestamp(new Date().getTime()));
        netty.setUpdateTime(new Timestamp(new Date().getTime()));

        return nettyDao.save(netty);
    }
}
