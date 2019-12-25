package com.sy.service.impl;

import com.google.common.collect.Lists;
import com.sy.dao.EfficiencyStatisticsDao;
import com.sy.dao.TaskDao;
import com.sy.entity.EfficiencyStatistics;
import com.sy.entity.Task;
import com.sy.service.EfficiencyStatisticsService;
import com.sy.vo.EfficiencyStatisticsVo;
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
    public List<EfficiencyStatisticsVo> getAllData(String taskName, Date beginTime, Date endTime) throws Exception {


        List<EfficiencyStatistics> list = getEfficiencyStatistics(taskName,beginTime, endTime);

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

        if(taskName!=""){
            if(!taskNames.contains(taskName)){
                throw new Exception("查询的项目不存在/当日该项目并未施工");
            }

            Unit unit = calculateData(taskName, map);

            List<EfficiencyStatisticsVo> vos = new ArrayList<>();

            handleVo(unit, vos);

            return vos;
        }else {
            List<EfficiencyStatisticsVo> vos = new ArrayList<>();
            for (String name : taskNames) {
                Unit unit = calculateData(name, map);
                handleVo(unit, vos);
            }

            if(!vos.isEmpty()){
                return vos;
            }
            return null;
        }

    }

    public List<EfficiencyStatistics> getEfficiencyStatistics(String taskName,Date beginTime, Date endTime) {
        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();

                if (beginTime!=null&&endTime!=null){
                    predicates.add(criteriaBuilder.between(root.get("date"),beginTime,endTime));
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return efficiencyStatisticsDao.findAll(querySpeci);
    }

    private void handleVo(Unit unit, List<EfficiencyStatisticsVo> vos) {
        Set<String> set = new HashSet<>();
        Map<String,List<Unit>> map = new HashMap<>();
        for (Unit son : unit.getSonList()) {
            String name = son.getName();
            set.add(name);
            if(map.get(name)==null){
                List<Unit> list = new ArrayList<>();
                list.add(son);
                map.put(name,list);
            }else {
                map.get(name).add(son);
            }
        }

        for (String s : set) {
            int time = 0;
            int workTime = 0;
            BigDecimal ePower = new BigDecimal("0");
            for (Unit u : map.get(s)) {
                time += u.getTime();
                workTime += u.getWorkTime();
                ePower = ePower.add(new BigDecimal(u.getPower()));
            }
            EfficiencyStatisticsVo vo = new EfficiencyStatisticsVo();
            vo.setTime(unit.getTime());
            vo.setWorkTime(unit.getWorkTime());
            vo.setName(unit.getName());
            vo.setPower(unit.getPower());
            vo.setSonName(s);
            vo.setSonTime(time);
            vo.setSonWorkTime(workTime);
            vo.setSonPower(ePower.doubleValue());
            vo.setWorkNo(taskDao.getWorkNoByName(unit.getName()));
            vos.add(vo);
        }
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
        unit.setPower(ePower.doubleValue());
        return unit;
    }

    public String getFirstTaskName(String taskName) {
        int pid = -1;
        String tempName = taskName;
        while (pid != 0){
            pid = taskDao.getPidByName(tempName);
            if(pid!=0){
                tempName = taskDao.getNameById(pid);
            }
        }
        return tempName;
    }
}
