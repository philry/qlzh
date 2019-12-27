package com.sy.service.impl;

import com.google.common.collect.Lists;
import com.sy.dao.DataManageDao;
import com.sy.entity.DataManage;
import com.sy.entity.Netty;
import com.sy.service.ManageDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;


@Service
public class ManageDataServiceImpl implements ManageDataService {

    @Autowired
    private DataManageDao dataManageDao;

    @Override
    public List<DataManage> getAllByData(int personId,Date beginTime, Date endTime) {

        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();

                if(personId!=0){
                    predicates.add(criteriaBuilder.equal(root.get("work").get("person").get("id"),personId));
                }

                if (beginTime!=null&&endTime!=null){
                    predicates.add(criteriaBuilder.between(root.get("createTime"),beginTime,endTime));
                }


                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return dataManageDao.findAll(querySpeci);
    }

    @Override
    public List<DataManage> getDataByWork(int workId, Date beginTime, Date endTime) {

        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();

                if(workId!=0){
                    predicates.add(criteriaBuilder.equal(root.get("work").get("id"),workId));
                }

                if (beginTime!=null&&endTime!=null){
                    predicates.add(criteriaBuilder.between(root.get("createTime"),beginTime,endTime));
                }


                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return dataManageDao.findAll(querySpeci);
    }


}
