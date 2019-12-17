package com.sy.service.impl;

import com.google.common.collect.Lists;
import com.sy.dao.EfficiencyStatisticsDao;
import com.sy.dao.TaskDao;
import com.sy.entity.EfficiencyStatistics;
import com.sy.entity.Task;
import com.sy.service.EfficiencyStatisticsService;
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
public class EfficiencyStatisticsServiceImpl implements EfficiencyStatisticsService {

    @Autowired
    private EfficiencyStatisticsDao efficiencyStatisticsDao;

    @Autowired
    private TaskDao taskDao;


    @Override
    public List<Unit> getAllData(String taskName, Date beginTime, Date endTime) throws Exception {


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

        List<EfficiencyStatistics> list = efficiencyStatisticsDao.findAll(querySpeci);

        //根据项目名称获取顶级上级项目
        Set<String> taskNames = new HashSet<>();
        Map<String,List<EfficiencyStatistics>> map = new HashMap<>();

        for (EfficiencyStatistics efficiencyStatistics : list) {
            String name = getFirstTaskName(efficiencyStatistics.getName());
            taskNames.add(name);
            if(map.get(name)==null){
                List<EfficiencyStatistics> tempList = new ArrayList<>();
                tempList.add(efficiencyStatistics);
                map.put(name,tempList);
            }else {
                map.get(name).add(efficiencyStatistics);
            }
        }

        if(taskName!=null){
            if(!taskNames.contains(taskName)){
                throw new Exception("查询的工程不存在");
            }

            Unit unit = calculateData(taskName, map);

            return Collections.singletonList(unit);
        }else {

            List<Unit> units = new ArrayList<>();
            for (String name : taskNames) {
                units.add(calculateData(name,map));
            }

            if(!units.isEmpty()){
                return units;
            }
        }
        return null;
    }

    private Unit calculateData(String taskName, Map<String, List<EfficiencyStatistics>> map) {
        Unit unit = new Unit();
        unit.setName(taskName);
        int time = 0;
        int work_time = 0 ;
        BigDecimal ePower = new BigDecimal("0");
        List<Unit> sonList = new ArrayList<>();
        for (EfficiencyStatistics efficiencyStatistics : map.get(taskName)) {
            Unit sonUnit = new Unit();
            sonUnit.setName(efficiencyStatistics.getName());
            sonUnit.setPower(efficiencyStatistics.getPower());
            sonUnit.setWorkTime(efficiencyStatistics.getWorkingTime());
            sonUnit.setTime(efficiencyStatistics.getTime());
            sonList.add(sonUnit);

            time += efficiencyStatistics.getTime();
            work_time += efficiencyStatistics.getWorkingTime();
            ePower = ePower.add(new BigDecimal(efficiencyStatistics.getPower()));
        }
        unit.setSonList(sonList);
        unit.setTime(time);
        unit.setWorkTime(work_time);
        return unit;
    }

    private String getFirstTaskName(String taskName) {
        int pid = -1;
        String tempName = taskName;
        while (pid != 0){

            pid = taskDao.getPidByName(taskName);

            tempName = taskDao.getNameById(pid);

        }
        return tempName;
    }
}
