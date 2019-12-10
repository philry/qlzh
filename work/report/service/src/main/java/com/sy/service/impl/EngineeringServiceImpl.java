package com.sy.service.impl;

import com.sy.dao.EngineeringDao;
import com.sy.entity.Engineering;
import com.sy.service.EngineeringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class EngineeringServiceImpl implements EngineeringService {

    @Autowired
    private EngineeringDao engineeringDao;


    @Override
    public List<Engineering> getAllData(Date date) {

        return engineeringDao.findAll();
    }
}
