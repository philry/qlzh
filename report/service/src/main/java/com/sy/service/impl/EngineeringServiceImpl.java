package com.sy.service.impl;

import com.google.common.collect.Lists;
import com.sy.dao.DeptDao;
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
import java.math.BigDecimal;
import java.util.*;


@Service
public class EngineeringServiceImpl implements EngineeringService {

    @Autowired
    private EngineeringDao engineeringDao;

    @Autowired
    private DeptDao deptDao;

    @Override
    public List<Engineering> getAllData(Date beginTime, Date endTime) {
        return null;
    }

    @Override
    public List<EngineeringVo> getInitData(Date beginTime, Date endTime) {
        List<Engineering> list = getData(0,beginTime,endTime);//pid为0生产部级,list为engineering表中的生产部的数据
        for (Engineering engineering : list) { //engineering，list为生产部级数据
            int pid = engineering.getId();

            List<Engineering> sonList = getData(pid,beginTime,endTime);//sonList为车间级

            for (Engineering engineering1 : sonList) {

                int pid1 = engineering1.getId();
                List<Engineering> sonList1 = getData(pid1,beginTime,endTime);

                for(Engineering engineering2 : sonList1){//engineering2，sonList1为工程队级
                    int pid2 = engineering2.getId();
                    engineering2.setSonLsit(getData(pid2,beginTime,endTime));//getData(pid2,beginTime,endTime)为班组级
                }

                engineering1.setSonLsit(sonList1);

            }

            engineering.setSonLsit(sonList);
        }

        List<EngineeringVo> vos = new ArrayList<>();

        //定义总公司数据
        int time = 0;
        int workTime = 0;
        BigDecimal power = new BigDecimal("0");
        /*Set<String> set = new HashSet<>();
        Map<String,List<Engineering>> map = new HashMap<>();*/
        Set<Integer> set = new HashSet<>();
        Map<Integer,List<Engineering>> map = new HashMap<>();
        for (Engineering engineering : list) {
            //所有生产部级数据相加
            time += engineering.getTime();
            workTime += engineering.getWorkingTime();
            power = power.add(new BigDecimal(engineering.getPower()));
            for (Engineering engineering1 : engineering.getSonLsit()) {//车间级
                /*String name = engineering1.getName(); //name是部门名称，可能重复，例如：不同车间或工程队都有班组1
                set.add(name);  //set车间级*/

                Integer deptId = engineering1.getDeptId();
                set.add(deptId); //set车间级
                if(map.get(deptId)!=null){
                    map.get(deptId).add(engineering1);
                }else {
                    List<Engineering> list1 = new ArrayList<>();
                    list1.add(engineering1);
                    map.put(deptId,list1);
                }
            }
        }

   //   for (String s : set) { //车间级名称
        for (Integer s : set) { //车间级的id
            String name1 = deptDao.getById(s).getName();
            int time_1 = 0;
            int workTime_1 = 0;
            BigDecimal power_1 = new BigDecimal("0");
            for (Engineering engineering : map.get(s)) {
                time_1 += engineering.getTime();
                workTime_1 += engineering.getWorkingTime();
                power_1 = power_1.add(new BigDecimal(engineering.getPower()));
            }
            /*Set<String> set1 = new HashSet<>();
            Map<String,List<Engineering>> map1 = new HashMap<>();*/
            Set<Integer> set1 = new HashSet<>();
            Map<Integer,List<Engineering>> map1 = new HashMap<>();
            for (Engineering engineering : map.get(s)) {

                for (Engineering engineering1 : engineering.getSonLsit()) {

                    /*String name = engineering1.getName();
                    set1.add(name);   //set1工程队级*/
                    Integer deptId = engineering1.getDeptId();
                    set1.add(deptId); //set1工程队级
                    if(map1.get(deptId)!=null){
                        map1.get(deptId).add(engineering1);
                    }else {
                        List<Engineering> list1 = new ArrayList<>();
                        list1.add(engineering1);
                        map1.put(deptId,list1);
                    }

                }
            }

    //      for (String s1 : set1) { //工程队级的名称
            for (Integer s1 : set1) { //工程队级的id
                String name2 = deptDao.getById(s1).getName();
                int time_2 = 0 ;
                int workTime_2 = 0;
                BigDecimal power_2 = new BigDecimal("0");
                for (Engineering engineering : map1.get(s1)) {
                    time_2 += engineering.getTime();
                    workTime_2 += engineering.getWorkingTime();
                    power_2 = power_2.add(new BigDecimal(engineering.getPower()));
                }
                //新增
                /*Set<String> set2 = new HashSet<>();
                Map<String,List<Engineering>> map2 = new HashMap<>();*/
                Set<Integer> set2 = new HashSet<>();
                Map<Integer,List<Engineering>> map2 = new HashMap<>();
                for (Engineering engineering : map1.get(s1)) {

                    for (Engineering engineering2 : engineering.getSonLsit()) {

                        /*String name = engineering2.getName();
                        set2.add(name);   //set2 班组级*/
                        Integer deptId = engineering2.getDeptId();
                        set2.add(deptId); //set2 班组级
                        if(map2.get(deptId)!=null){
                            map2.get(deptId).add(engineering2);
                        }else {
                            List<Engineering> list2 = new ArrayList<>();
                            list2.add(engineering2);
                            map2.put(deptId,list2);
                        }
                    }
                }

                //新增一级
        //      for (String s2 : set2) { //班组级的名称
                for (Integer s2 : set2) { //班组级的id
                    String name3 = deptDao.getById(s2).getName();
                    int time_3 = 0;
                    int workTime_3 = 0;
                    BigDecimal power_3 = new BigDecimal("0");
                    for (Engineering engineering : map2.get(s2)) {
                        time_3 += engineering.getTime();
                        workTime_3 += engineering.getWorkingTime();
                        power_3 = power_3.add(new BigDecimal(engineering.getPower()));

                    }

                    EngineeringVo vo = new EngineeringVo();
                    vo.setTime(time);
                    vo.setWorkTime(workTime);
                    vo.setPower(String.valueOf(power.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                    vo.setTime_1(time_1);
                    vo.setTime_2(time_2);
                    vo.setPower_1(String.valueOf(power_1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                    vo.setWorkTime_1(workTime_1);
                    vo.setWorkTime_2(workTime_2);
                    vo.setPower_2(String.valueOf(power_2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                    vo.setName_1(name1);//vo.setName_1(s);
                    vo.setName_2(name2);//vo.setName_2(s1);
                    vo.setTime_3(time_3);
                    vo.setWorkTime_3(workTime_3);
                    vo.setPower_3(String.valueOf(power_3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
                    vo.setName_3(name3);//vo.setName_3(s2);
                    vos.add(vo);
                }
            }
        }

        return vos;

    }

    @Override
    public EngineeringVo getInitDataByDeptId(Integer deptId, Date beginTime, Date endTime) {
        List<Engineering> list = getDataByDeptId(deptId,beginTime,endTime);
        BigDecimal power = new BigDecimal("0");
        int time = 0;
        int workTime = 0;
        if(list != null && list.size() != 0){
            for(Engineering engineering : list){
                power = power.add(new BigDecimal(engineering.getPower()));
                time += engineering.getTime();
                workTime += engineering.getWorkingTime();
            }
        }

        EngineeringVo vo = new EngineeringVo();
        vo.setTime(time);
        vo.setWorkTime(workTime);
        vo.setPower(String.valueOf(power.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
        return vo;
    }


    public List<Engineering> getDataByDeptId(Integer deptId,Date beginTime, Date endTime) {

        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(criteriaBuilder.equal(root.get("deptId"), deptId));

                if (beginTime != null && endTime != null) {
                    predicates.add(criteriaBuilder.between(root.get("date"), beginTime, endTime));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return engineeringDao.findAll(querySpeci);
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

    @Override
    public List<Engineering> getDataByLevel(int level, Date beginTime, Date endTime) {
        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(criteriaBuilder.equal(root.get("level"), level));

                if (beginTime != null && endTime != null) {
                    predicates.add(criteriaBuilder.between(root.get("date"), beginTime, endTime));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return engineeringDao.findAll(querySpeci);
    }


    /*public Engineering getDataByLevelAndName(int level, String name) {
        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();

                predicates.add(criteriaBuilder.equal(root.get("level"), level));

                if (name != null) {
                    predicates.add(criteriaBuilder.equal(root.get("name"), name));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return engineeringDao.findAll(querySpeci);
    }*/


}
