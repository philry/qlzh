package com.sy.service.impl;

import com.google.common.collect.Lists;
import com.sy.dao.NettyDao;
import com.sy.entity.Netty;
import com.sy.service.NettyService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class NettyServiceImpl implements NettyService {

    @Autowired
    private NettyDao nettyDao;


    @Override
    public Netty insertData(Netty netty) {

        netty.setCreateTime(new Timestamp(new Date().getTime()));
        netty.setUpdateTime(new Timestamp(new Date().getTime()));

        return nettyDao.save(netty);
    }

    @Override
    public List<Netty> getAllByDate(Date beginTime,Date endTime) {

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

        return nettyDao.findAll(querySpeci);
    }
}
