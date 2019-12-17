package com.sy.service.impl;

import com.google.common.collect.Lists;
import com.sy.dao.EngineeringDao;
import com.sy.entity.Engineering;
import com.sy.service.EngineeringService;
import com.sy.vo.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class EngineeringServiceImpl implements EngineeringService {

    @Autowired
    private EngineeringDao engineeringDao;

    @Override
    public List<Engineering> getAllData(Date beginTime, Date endTime) {
        return null;
    }

    @Override
    public List<Engineering> getInitData(Date beginTime, Date endTime) {

        List<Engineering> list = getData(0,beginTime,endTime);

        for (Engineering engineering : list) {
            int pid = engineering.getId();

            List<Engineering> sonList = getData(pid,beginTime,endTime);

            for (Engineering engineering1 : sonList) {

                int pid1 = engineering1.getId();

                engineering1.setSonLsit(getData(pid1,beginTime,endTime));
            }

            engineering.setSonLsit(sonList);
        }

        return list;
    }


    public List<Engineering> getData(int pid,Date beginTime, Date endTime) {

        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(criteriaBuilder.equal(root.get("pid"), pid));

                if (beginTime != null && endTime != null) {
                    predicates.add(criteriaBuilder.between(root.get("createTime"), beginTime, endTime));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return engineeringDao.findAll(querySpeci);
    }
}
