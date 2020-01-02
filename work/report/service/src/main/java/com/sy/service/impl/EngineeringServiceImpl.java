package com.sy.service.impl;

import com.google.common.collect.Lists;
import com.sy.dao.EngineeringDao;
import com.sy.entity.Engineering;
import com.sy.service.EngineeringService;
import com.sy.vo.EfficiencyStatisticsVo;
import com.sy.vo.EngineeringVo;
import com.sy.vo.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;


@Service
public class EngineeringServiceImpl implements EngineeringService {

    @Autowired
    private EngineeringDao engineeringDao;

    @Override
    public List<Engineering> getAllData(Date beginTime, Date endTime) {
        return null;
    }

    @Override
    public List<EngineeringVo> getInitData(Date beginTime, Date endTime) {
        System.out.println(beginTime);
        System.out.println(endTime);
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

        List<EngineeringVo> vos = new ArrayList<>();

        //定义总公司数据
        int time = 0;
        int workTime = 0;
        Set<String> set = new HashSet<>();
        Map<String,List<Engineering>> map = new HashMap<>();
        for (Engineering engineering : list) {
            time += engineering.getTime();
            workTime += engineering.getWorkingTime();
            for (Engineering engineering1 : engineering.getSonLsit()) {
                String name = engineering1.getName();
                set.add(name);
                if(map.get(name)!=null){
                    map.get(name).add(engineering1);
                }else {
                    List<Engineering> list1 = new ArrayList<>();
                    list1.add(engineering1);
                    map.put(name,list1);
                }
            }
        }

        for (String s : set) {
            int time_1 = 0;
            int workTime_1 = 0;
            for (Engineering engineering : map.get(s)) {
                time_1 += engineering.getTime();
                workTime_1 += engineering.getWorkingTime();
            }
            Set<String> set1 = new HashSet<>();
            Map<String,List<Engineering>> map1 = new HashMap<>();
            for (Engineering engineering : map.get(s)) {

                for (Engineering engineering1 : engineering.getSonLsit()) {

                    String name = engineering1.getName();
                    set1.add(name);
                    if(map1.get(name)!=null){
                        map1.get(name).add(engineering1);
                    }else {
                        List<Engineering> list1 = new ArrayList<>();
                        list1.add(engineering1);
                        map1.put(name,list1);
                    }

                }
            }

            for (String s1 : set1) {
                int time_2 = 0 ;
                int workTime_2 = 0;
                for (Engineering engineering : map1.get(s1)) {
                    time_2 += engineering.getTime();
                    workTime_2 += engineering.getWorkingTime();
                }
                EngineeringVo vo = new EngineeringVo();
                vo.setTime(time);
                vo.setWorkTime(workTime);
                vo.setTime_1(time_1);
                vo.setTime_2(time_2);
                vo.setWorkTime_1(workTime_1);
                vo.setWorkTime_2(workTime_2);
                vo.setName_1(s);
                vo.setName_2(s1);
                System.out.println(vo);
                vos.add(vo);
            }
        }

        return vos;

    }


    public List<Engineering> getData(int pid,Date beginTime, Date endTime) {

        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(criteriaBuilder.equal(root.get("pid"), pid));

                if (beginTime != null && endTime != null) {
                    predicates.add(criteriaBuilder.between(root.get("date"), beginTime, endTime));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return engineeringDao.findAll(querySpeci);
    }

	
}
