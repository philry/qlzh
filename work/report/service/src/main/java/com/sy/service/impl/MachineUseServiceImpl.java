package com.sy.service.impl;

import com.google.common.collect.Lists;
import com.sy.dao.MachineUseDao;
import com.sy.entity.MachineUse;
import com.sy.service.MachineUseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;


@Service
public class MachineUseServiceImpl implements MachineUseService {

    @Autowired
    private MachineUseDao machineUseDao;


    @Override
    public List<MachineUse> getDataByIdAndTime(int machineId,Date beginTime, Date endTime) {

        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();

                if(machineId!=0){
                    predicates.add(criteriaBuilder.equal(root.get("machineId"),machineId));
                }

                if (beginTime!=null&&endTime!=null){
                    predicates.add(criteriaBuilder.between(root.get("createTime"),beginTime,endTime));
                }


                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return machineUseDao.findAll(querySpeci);
    }
}
