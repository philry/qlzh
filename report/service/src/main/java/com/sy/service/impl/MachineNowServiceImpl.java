package com.sy.service.impl;

import com.sy.dao.MachineNowDao;
import com.sy.entity.Machine;
import com.sy.entity.MachineNow;
import com.sy.entity.Person;
import com.sy.service.MachineNowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Service
public class MachineNowServiceImpl implements MachineNowService {

    @Autowired
    private MachineNowDao machineNowDao;

    @Override
    @Transactional
    public MachineNow userMachine(MachineNow machineNow,Integer personId,Integer machineId) {

        machineNow.setPerson(new Person(personId));
        machineNow.setMachine(new Machine(machineId));
        machineNow.setCreateTime(new Timestamp(new Date().getTime()));
        machineNow.setUpdateTime(new Timestamp(new Date().getTime()));

        return machineNowDao.save(machineNow);
    }

    @Override
    @Transactional
    public int closeMachine(Integer personId, Integer machineId) {

        return machineNowDao.deleteByPersonAndMachine(personId,machineId);
    }

    @Override
    public List<MachineNow> getNowMachine() {

        return machineNowDao.findAll();
    }

    @Override
    public List<MachineNow> getNowMachineByPerson(int personId) {

        return machineNowDao.getByPersonId(personId);
    }
}
