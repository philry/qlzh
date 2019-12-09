package com.sy.service.impl;

import cn.hutool.core.util.PageUtil;
import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Lists;
import com.sy.dao.WorkDao;
import com.sy.entity.Machine;
import com.sy.entity.Person;
import com.sy.entity.Task;
import com.sy.entity.Work;
import com.sy.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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


    @Override
    public Work startWork(Work work, Integer personId, Integer taskId, Integer machineId) {

        work.setPerson(new Person(personId));
        work.setTask(new Task(taskId));
        work.setMachine(new Machine(machineId));
        work.setCreateTime(new Timestamp(new Date().getTime()));
        work.setUpdateTime(new Timestamp(new Date().getTime()));

        return workDao.save(work);
    }

    @Override
    public Page<Work> getAllWork(Integer page, Integer pageSize,Integer personId, Date beginTime,Date endTime) {

        Pageable pageable = PageRequest.of(page,pageSize);

        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();
                if(personId!=null){
                    predicates.add(criteriaBuilder.equal(root.get("person").get("id"),personId));
                }
                if (beginTime!=null&&endTime!=null){
                    predicates.add(criteriaBuilder.between(root.get("createTime"),beginTime,endTime));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return workDao.findAll(querySpeci,pageable);
    }



}
