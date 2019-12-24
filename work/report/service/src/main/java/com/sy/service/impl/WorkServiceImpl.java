package com.sy.service.impl;


import com.google.common.collect.Lists;
import com.sy.dao.MachineNowDao;
import com.sy.dao.PersonDao;
import com.sy.dao.WorkDao;
import com.sy.entity.*;
import com.sy.service.MachineNowService;
import com.sy.service.WorkService;
import com.sy.task.NettyDataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Service
public class WorkServiceImpl implements WorkService {

    @Autowired
    private WorkDao workDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private MachineNowDao machineNowDao;


    @Override
    @Transactional
    public Work startWork(int personId, int taskId, int machineId) throws Exception {
        //判定当前焊机是否已被别人打开
        List<MachineNow> nowList = machineNowDao.getDataByMachineId(machineId);
        if(!nowList.isEmpty()){
            throw new Exception("此焊机已被打开,请使用其他焊机操作");
        }

        //判定用户有否有开焊机的权利
        Person person = personDao.getById(personId);
        int counts = person.getPileCounts();

        //获取当前角色已开启的焊机数量
        List<MachineNow> list = machineNowDao.getByPersonId(personId);
        int openCounts = list.size();

        if(openCounts>=counts){
            throw new Exception("可开焊机达到最大数量,请关闭其他焊机后在尝试");
        }

        //TODO 控制合闸


        //插入工作表
        inseretWork(personId, taskId, machineId,"0");

        //插入焊机使用表
        MachineNow machineNow = new MachineNow();
        machineNow.setPerson(new Person(personId));
        machineNow.setMachine(new Machine(machineId));
        machineNow.setBeginTime(new Timestamp(new Date().getTime()));
        machineNow.setStatus("0");
        machineNow.setCreateTime(new Timestamp(new Date().getTime()));
        machineNow.setRemark(String.valueOf(taskId));
        machineNowDao.save(machineNow);

        return null;
    }

    @Override
    @Transactional
    public boolean endWork(int personId, int taskId, int machineId) throws Exception {
        //判定当前焊机是否已被别人打开
        List<MachineNow> nowList = machineNowDao.getDataByMachineId(machineId);
        if(nowList.isEmpty()){
            throw new Exception("此焊机并未被打开,请核实后再尝试");
        }

        //TODO 控制分闸


        //插入工作表
        inseretWork(personId, taskId, machineId,"1");

        //删除焊机使用表相关数据
        machineNowDao.deleteByPersonAndMachine(personId,machineId);

        return false;
    }


    @Override
    public Page<Work> getAllWork(Integer page, Integer pageSize,String personName, Date beginTime,Date endTime) throws Exception {

        Pageable pageable = PageRequest.of(page,pageSize);

        if("".equals(personName)){
            Specification querySpeci = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = Lists.newArrayList();
                    if (beginTime!=null&&endTime!=null){
                        predicates.add(criteriaBuilder.between(root.get("createTime"),beginTime,endTime));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            };

            return workDao.findAll(querySpeci,pageable);
        }else {
            Integer personId = personDao.getIdByName(personName);

            if(personId==null){
                throw new Exception("没有该员工");
            }

            Specification querySpeci = new Specification() {
                @Override
                public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    List<Predicate> predicates = Lists.newArrayList();
                    predicates.add(criteriaBuilder.equal(root.get("person").get("id"),personId));
                    if (beginTime!=null&&endTime!=null){
                        predicates.add(criteriaBuilder.between(root.get("createTime"),beginTime,endTime));
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                }
            };

            return workDao.findAll(querySpeci,pageable);
        }




    }

    private void inseretWork(Integer personId, Integer taskId, Integer machineId,String operate) {
        Work work = new Work();
        work.setPerson(new Person(personId));
        work.setTask(new Task(taskId));
        work.setMachine(new Machine(machineId));
        work.setCreateTime(new Timestamp(new Date().getTime()));
        work.setUpdateTime(new Timestamp(new Date().getTime()));
        work.setOperate(operate);
        work.setStatus("0");

        workDao.save(work);
    }



}
