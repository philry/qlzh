package com.sy.service.impl;

import com.google.common.collect.Lists;
import com.sy.dao.DeptDao;
import com.sy.dao.EfficiencyStatisticsDao;
import com.sy.dao.EfficiencyStatisticsNewDao;
import com.sy.dao.TaskDao;
import com.sy.entity.EfficiencyStatistics;
import com.sy.entity.EfficiencyStatisticsNew;
import com.sy.entity.Engineering;
import com.sy.entity.Task;
import com.sy.service.EfficiencyStatisticsService;
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
public class EfficiencyStatisticsServiceImpl implements EfficiencyStatisticsService {

    @Autowired
    private EfficiencyStatisticsDao efficiencyStatisticsDao;

    @Autowired
    private EfficiencyStatisticsNewDao efficiencyStatisticsNewDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private DeptDao deptDao;

    @Override
    public List<EfficiencyStatisticsVo> getAllData(String taskName, Date beginTime, Date endTime) throws Exception {


        List<EfficiencyStatistics> list = getEfficiencyStatistics(taskName,beginTime, endTime);//所有的工程报表数据(所有数据都是焊工级的)

        //根据项目名称获取顶级上级项目
        Set<String> taskNames = new HashSet<>();
        Map<String,List<EfficiencyStatistics>> map = new HashMap<>();

        for (EfficiencyStatistics efficiencyStatistics : list) {
      //    String name = getFirstTaskName(efficiencyStatistics.getName());
            String name = getFirstTaskName1(efficiencyStatistics.getTaskId());//name为生产部级的projectName

            taskNames.add(name); //taskNames是所有生产部级的projectName
            if(map.get(name)==null){
                List<EfficiencyStatistics> tempList = new ArrayList<>();
                tempList.add(efficiencyStatistics);
                map.put(name,tempList);
            }else {
                map.get(name).add(efficiencyStatistics);
            }
        }

        if(!"".equals(taskName)&&taskName!=null){
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

            /*if(!vos.isEmpty()){
                return vos;
            }*/
            return vos;
        }

    }

    //我新写的
    @Override
    public List<EfficiencyStatisticsVo> getInitData(String taskName,Date beginTime, Date endTime) {
        List<EfficiencyStatisticsNew> list = getData(0,beginTime,endTime);//pid为0生产部级,list为efficiencyStatistics表中的生产部的数据

        for (EfficiencyStatisticsNew efficiencyStatistics : list) { //efficiencyStatistics，list为生产部级数据

            int pid = efficiencyStatistics.getId();

            List<EfficiencyStatisticsNew> sonList = getData(pid,beginTime,endTime);//sonList为车间级

            for (EfficiencyStatisticsNew efficiencyStatistics1 : sonList) {

                int pid1 = efficiencyStatistics1.getId();
                List<EfficiencyStatisticsNew> sonList1 = getData(pid1,beginTime,endTime);

                for(EfficiencyStatisticsNew efficiencyStatistics2 : sonList1){//engineering2，sonList1为工程队级
                    int pid2 = efficiencyStatistics2.getId();
                    efficiencyStatistics2.setSonLsit(getData(pid2,beginTime,endTime));//getData(pid2,beginTime,endTime)为班组级
                }

                efficiencyStatistics1.setSonLsit(sonList1);

            }

            efficiencyStatistics.setSonLsit(sonList);
        }

        List<EfficiencyStatisticsVo> vos = new ArrayList<>();

    //  Set<String> set0 = new HashSet<>();
    //  Map<String,List<EfficiencyStatistics>> map0 = new HashMap<>();
        Set<Integer> set0 = new HashSet<>();
        Map<Integer,List<EfficiencyStatisticsNew>> map0 = new HashMap<>();
        for (EfficiencyStatisticsNew efficiencyStatistics : list) {//整条船级

        //  String name = efficiencyStatistics.getName();
        //  set0.add(name);  //set整条船级
            Integer taskId = efficiencyStatistics.getTaskId();
            set0.add(taskId);
            if(map0.get(taskId)!=null){
                map0.get(taskId).add(efficiencyStatistics);
            }else {
                List<EfficiencyStatisticsNew> list0 = new ArrayList<>();
                list0.add(efficiencyStatistics);
           //   map0.put(name,list0);
                map0.put(taskId,list0);//map0是不同船的生产部级的任务集合
            }
        }
        for(Integer s0 : set0) { //生产部级所有船任务id集合
            //定义总公司数据
            String name = efficiencyStatisticsNewDao.getNameByTaskId(s0);
            Integer deptId = taskDao.getDeptIdById(s0);
            String deptName = deptDao.getNameById(deptId);
            int time = 0;
            int workTime = 0;
            BigDecimal power = new BigDecimal("0");
        //  Set<String> set = new HashSet<>();
        //  Map<String, List<EfficiencyStatistics>> map = new HashMap<>();
            Set<Integer> set = new HashSet<>();
            Map<Integer, List<EfficiencyStatisticsNew>> map = new HashMap<>();

            for (EfficiencyStatisticsNew efficiencyStatistics : map0.get(s0)) {
                //所有生产部级数据相加
                time += efficiencyStatistics.getTime();
                workTime += efficiencyStatistics.getWorkingTime();
                power = power.add(new BigDecimal(efficiencyStatistics.getPower()));
                for (EfficiencyStatisticsNew efficiencyStatistics1 : efficiencyStatistics.getSonLsit()) {//车间级
                //  String name = efficiencyStatistics1.getName();
                //  set.add(name);  //set车间级
                    Integer taskId = efficiencyStatistics1.getTaskId();
                    set.add(taskId);
                    if (map.get(taskId) != null) {
                        map.get(taskId).add(efficiencyStatistics1);
                    } else {
                        List<EfficiencyStatisticsNew> list1 = new ArrayList<>();
                        list1.add(efficiencyStatistics1);
                //      map.put(name,list1);
                        map.put(taskId, list1);//map是相同生产部级任务下派的不同的车间级的任务集合
                    }
                }
            }
            for (Integer s : set) { //派到车间级的任务id集合
                String name1 = efficiencyStatisticsNewDao.getNameByTaskId(s);
                Integer deptId1 = taskDao.getDeptIdById(s);
                String deptName1 = deptDao.getNameById(deptId1);
                int time_1 = 0;
                int workTime_1 = 0;
                BigDecimal power_1 = new BigDecimal("0");
                for (EfficiencyStatisticsNew efficiencyStatistics : map.get(s)) {
                    time_1 += efficiencyStatistics.getTime();
                    workTime_1 += efficiencyStatistics.getWorkingTime();
                    power_1 = power_1.add(new BigDecimal(efficiencyStatistics.getPower()));
                }
           //   Set<String> set1 = new HashSet<>();
           //   Map<String, List<EfficiencyStatistics>> map1 = new HashMap<>();
                Set<Integer> set1 = new HashSet<>();
                Map<Integer, List<EfficiencyStatisticsNew>> map1 = new HashMap<>();
                for (EfficiencyStatisticsNew efficiencyStatistics : map.get(s)) {

                    for (EfficiencyStatisticsNew efficiencyStatistics2 : efficiencyStatistics.getSonLsit()) {

                        /*String name = efficiencyStatistics1.getName();
                        set1.add(name);   //set1工程队级*/
                        Integer taskId = efficiencyStatistics2.getTaskId();
                        set1.add(taskId);
                        if (map1.get(taskId) != null) {
                            map1.get(taskId).add(efficiencyStatistics2);
                        } else {
                            List<EfficiencyStatisticsNew> list2 = new ArrayList<>();
                            list2.add(efficiencyStatistics2);
                    //      map1.put(name, list2);
                            map1.put(taskId, list2);
                        }

                    }
                }

                for (Integer s1 : set1) { //派到工程队级任务id集合
                    String name2 = efficiencyStatisticsNewDao.getNameByTaskId(s1);
                    Integer deptId2 = taskDao.getDeptIdById(s1);
                    String deptName2 = deptDao.getNameById(deptId2);
                    int time_2 = 0;
                    int workTime_2 = 0;
                    BigDecimal power_2 = new BigDecimal("0");
                    for (EfficiencyStatisticsNew efficiencyStatistics : map1.get(s1)) {
                        time_2 += efficiencyStatistics.getTime();
                        workTime_2 += efficiencyStatistics.getWorkingTime();
                        power_2 = power_2.add(new BigDecimal(efficiencyStatistics.getPower()));
                    }
                    //新增
            //      Set<String> set2 = new HashSet<>();
            //      Map<String, List<EfficiencyStatistics>> map2 = new HashMap<>();
                    Set<Integer> set2 = new HashSet<>();
                    Map<Integer, List<EfficiencyStatisticsNew>> map2 = new HashMap<>();
                    for (EfficiencyStatisticsNew efficiencyStatistics : map1.get(s1)) {

                        for (EfficiencyStatisticsNew efficiencyStatistics3 : efficiencyStatistics.getSonLsit()) {

                            /*String name = efficiencyStatistics3.getName();
                            set2.add(name);   //set2 班组级*/
                            Integer taskId = efficiencyStatistics3.getTaskId();
                            set2.add(taskId);
                            if (map2.get(taskId) != null) {
                                map2.get(taskId).add(efficiencyStatistics3);
                            } else {
                                List<EfficiencyStatisticsNew> list3 = new ArrayList<>();
                                list3.add(efficiencyStatistics3);
                            //  map2.put(name, list3);
                                map2.put(taskId, list3);
                            }
                        }
                    }

                    //新增一级
                    for (Integer s2 : set2) { //派到班组级任务id集合
                        String name3 = efficiencyStatisticsNewDao.getNameByTaskId(s2);
                        Integer deptId3 = taskDao.getDeptIdById(s2);
                        String deptName3 = deptDao.getNameById(deptId3);
                        int time_3 = 0;
                        int workTime_3 = 0;
                        BigDecimal power_3 = new BigDecimal("0");
                        String workNo = null;
                        for (EfficiencyStatisticsNew efficiencyStatistics : map2.get(s2)) {
                            time_3 += efficiencyStatistics.getTime();
                            workTime_3 += efficiencyStatistics.getWorkingTime();
                            power_3 = power_3.add(new BigDecimal(efficiencyStatistics.getPower()));
                            workNo = getWorkNo(efficiencyStatistics.getTaskId());
                        }

                        EfficiencyStatisticsVo vo = new EfficiencyStatisticsVo();
                        vo.setWorkNo(workNo);
                        vo.setName(name);//vo.setName(s0);
                        vo.setName_1(name1);//vo.setName(s);
                        vo.setName_2(name2);//vo.setName(s1);
                        vo.setName_3(name3);//vo.setName(s2);
                        vo.setTaskId1(s);
                        vo.setTaskId2(s1);
                        vo.setTaskId3(s2);
                        vo.setDeptName(deptName);
                        vo.setDeptName1(deptName1);
                        vo.setDeptName2(deptName2);
                        vo.setDeptName3(deptName3);
                        vo.setTime(time);
                        vo.setTime1(time_1);
                        vo.setTime2(time_2);
                        vo.setTime3(time_3);
                        vo.setWorkTime(workTime);
                        vo.setWorkTime1(workTime_1);
                        vo.setWorkTime2(workTime_2);
                        vo.setWorkTime3(workTime_3);
                        vo.setPower(power.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        vo.setPower1(power_1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        vo.setPower2(power_2.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        vo.setPower3(power_3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        vos.add(vo);
                    }
                }
            }
        }

        return vos;

    }

    //我新写的
    public List<EfficiencyStatisticsNew> getData(int pid, Date beginTime, Date endTime) {

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
        return efficiencyStatisticsNewDao.findAll(querySpeci);
    }

    public List<EfficiencyStatistics> getEfficiencyStatistics(String taskName,Date beginTime, Date endTime) {
        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();
                /*if(taskName!=null&&!("").equals(taskName)){
                    predicates.add(criteriaBuilder.equal(root.get("name"),taskName));
                }*/
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
            set.add(name); //焊工级别的project_name
            if(map.get(name)==null){
                List<Unit> list = new ArrayList<>();
                list.add(son);
                map.put(name,list);
            }else {
                map.get(name).add(son);
            }
        }

        for (String s : set) { //set是所有焊工级别的project_name
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
//          vo.setWorkNo(taskDao.getWorkNoByName(unit.getName()));
            vo.setWorkNo(taskDao.getWorkNoById(unit.getId()));
            vos.add(vo);
        }
    }



    private void handleVo01(Unit unit, List<EfficiencyStatisticsVo> vos) {
        Set<String> set = new HashSet<>();
        Map<String,List<Unit>> map = new HashMap<>();
        for (Unit son : unit.getSonList()) {
            String name = son.getName();
            set.add(name); //焊工级别的project_name
            if(map.get(name)==null){
                List<Unit> list = new ArrayList<>();
                list.add(son);
                map.put(name,list);
            }else {
                map.get(name).add(son);
            }
        }

        for (String s : set) { //set是所有焊工级别的project_name
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

            /*//车间
            vo.setTime1();
            vo.setWorkTime1();
            vo.setName_1();
            vo.setPower1();

            //工程队
            vo.setTime2();
            vo.setWorkTime2();
            vo.setName_2();
            vo.setPower2();

            //班组
            vo.setTime3();
            vo.setWorkTime3();
            vo.setName_3();
            vo.setPower3();*/

            vo.setSonName(s);
            vo.setSonTime(time);
            vo.setSonWorkTime(workTime);
            vo.setSonPower(ePower.doubleValue());
//          vo.setWorkNo(taskDao.getWorkNoByName(unit.getName()));
            vo.setWorkNo(taskDao.getWorkNoById(unit.getId()));
            vos.add(vo);
        }
    }


    private void handleVo2(Unit unit, List<EfficiencyStatisticsVo> vos) {
        Set<String> set = new HashSet<>();
        Map<String,List<Unit>> map = new HashMap<>();
        Map<Integer,List<EfficiencyStatistics>> map2 = new HashMap<>();
        List<Integer> banzupids = new ArrayList<>();

        List<EfficiencyStatistics> lists =  efficiencyStatisticsDao.findAll();
        for (Unit son : unit.getSonList()) {
            String name = son.getName();
            set.add(name); //焊工级别的project_name
            if(map.get(name)==null){
                List<Unit> list = new ArrayList<>();
                list.add(son);
                map.put(name,list);
            }else {
                map.get(name).add(son);
            }
        }

        for(EfficiencyStatistics efficiencyStatistics : lists){
            Integer taskId = efficiencyStatistics.getTaskId();
            if(map2.get(taskId)==null){
                List<EfficiencyStatistics> list = new ArrayList<>();
                list.add(efficiencyStatistics);
                map2.put(taskId,list);
            }else {
                map2.get(taskId).add(efficiencyStatistics);
            }
        }

        for(EfficiencyStatistics efficiencyStatistics : lists){
            Integer taskId = efficiencyStatistics.getTaskId();
            Integer pid = taskDao.getPidById(taskId);
            banzupids.add(pid);
        }


      for (String s : set) { //set是所有焊工级别的project_name
//        for(EfficiencyStatistics efficiencyStatistics : lists){//lists是所有焊工级别的数据
//            String s = efficiencyStatistics.getName();
            int time = 0;
            int workTime = 0;
            BigDecimal ePower = new BigDecimal("0");

            String name3 = null;
            int time3 = 0;//班组级
            int workTime3 = 0;
            BigDecimal Power3 = new BigDecimal("0");
            for (Unit u : map.get(s)) {
                time += u.getTime();
                workTime += u.getWorkTime();
                ePower = ePower.add(new BigDecimal(u.getPower()));
//            }


                Integer taskId = u.getId();
//          for(EfficiencyStatistics efficiencyStatistics1 : map2.get(taskId)) {
                Integer id = taskId;
                Integer pid = taskDao.getPidById(id);
                if(pid !=null){
                    name3 = taskDao.getNameById(pid);//班组级
                }
                if (banzupids.contains(pid)) {
                    time3 += time;
                    workTime3 += workTime;
                    Power3 = Power3.add(ePower);
                }
            }


//          }



            //新增的start
            /*List<EfficiencyStatistics> lists =  efficiencyStatisticsDao.findAll();
            List<Integer> banzupids = new ArrayList<>();
            Map<Integer,EfficiencyStatistics> map2 = new HashMap<>();
            int time3=0; //班组级别的
            int workingtime3=0;
            for(EfficiencyStatistics efficiencyStatistics : lists){
                Integer taskId = efficiencyStatistics.getTaskId();
                Integer pid = taskDao.getPidById(taskId);
                banzupids.add(pid);
            }

            for(Integer pid : banzupids){
                if(!banzupids.contains(pid)){ //不包含
                    Integer id_first = pid;
                    time3 = unit.getTime();
                    workingtime3 = unit.getWorkTime();
                }else{ //包含
                    if(pid.equals(banzupids.get(0))){
                        time3 += time3;
                        workingtime3 += workingtime3;
                    }else{
                        Integer id_first = pid;
                        time3 = unit.getTime();
                        workingtime3 = unit.getWorkTime();
                    }
                }
            }*/
            //新增的end

            EfficiencyStatisticsVo vo = new EfficiencyStatisticsVo();
            vo.setTime(unit.getTime());
            vo.setWorkTime(unit.getWorkTime());
            vo.setName(unit.getName());
            vo.setPower(unit.getPower());

            vo.setName_3(name3);
            vo.setTime3(time3);
            vo.setWorkTime3(workTime3);
            vo.setPower3(Power3.doubleValue());

            vo.setSonName(s);
            vo.setSonTime(time);
            vo.setSonWorkTime(workTime);
            vo.setSonPower(ePower.doubleValue());
            vo.setWorkNo(taskDao.getWorkNoById(unit.getId()));
            vos.add(vo);
        }
    }

    private Unit calculateData(String taskName, Map<String, List<EfficiencyStatistics>> map) {
        Integer id = taskDao.getIdByName(taskName);//生产部级别任务的id
        Unit unit = new Unit();
        unit.setId(id);
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


    private Unit calculateData01(String taskName, Map<String, List<EfficiencyStatistics>> map) {
        Integer id = taskDao.getIdByName(taskName);//生产部级别任务的id
        Unit unit = new Unit();
        unit.setId(id);
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


    private Unit calculateData2(String taskName, Map<String, List<EfficiencyStatistics>> map) {
        Unit unit = new Unit();
        unit.setName(taskName);
        int time = 0;
        int work_time = 0 ;
        BigDecimal ePower = new BigDecimal("0");
        List<Unit> sonList = new ArrayList<>();

        int time3 = 0;
        int work_time3 = 0 ;
        BigDecimal ePower3 = new BigDecimal("0");
        List<Unit> sonList3 = new ArrayList<>();
        List<Integer> banzuids = new ArrayList<>();
        Unit unit3 = null;
        Integer pid = 0;
        /*int taskId = 0;
        List<Integer> ids = new ArrayList<>();
        int time3=0; //班组级别的
        int workingtime3=0;
        Integer id = null;
        Map<Integer,EfficiencyStatistics> map2 = new HashMap<>();*/
        for (EfficiencyStatistics efficiencyStatistics : map.get(taskName)) {
            /*if(ids.size() == 0){ //
                taskId = efficiencyStatistics.getTaskId();
                Integer pid = taskDao.getPidById(taskId);
                id = pid;
                ids.add(id);
                map2.put(id,efficiencyStatistics);
                time3 = efficiencyStatistics.getTime();
                workingtime3 = efficiencyStatistics.getWorkingTime();

            }else{
                taskId = efficiencyStatistics.getTaskId();
                Integer pid = taskDao.getPidById(taskId);
                id = pid;
                if(ids.contains(id)){
                    time3 += time3;
                    workingtime3 += workingtime3;
                }else{
                    time3 = unit.getTime();
                    workingtime3 = unit.getWorkTime();
                }
            }*/



            Unit sonUnit = new Unit();
            sonUnit.setId(efficiencyStatistics.getTaskId());//焊工级别的任务id
            sonUnit.setName(efficiencyStatistics.getName());
            sonUnit.setPower(efficiencyStatistics.getPower());
            sonUnit.setWorkTime(efficiencyStatistics.getWorkingTime());
            sonUnit.setTime(efficiencyStatistics.getTime());
            sonList.add(sonUnit);

            pid = taskDao.getPidById(efficiencyStatistics.getTaskId());

            if(banzuids.contains(pid)){
                time3 += time;
                work_time3 += work_time;
                ePower3 = ePower3.add(ePower);
                unit3.setName(taskDao.getNameById(pid));
                unit3.setTime(time3);
                unit3.setWorkTime(work_time3);
                unit3.setPower(ePower3.doubleValue());
                sonList3.add(unit3);
            }else{
                unit3 = new Unit();//班组级别,不包含,新建unit3
                time3 = time;
                work_time3 = work_time;
                ePower3 = ePower;
            }
            banzuids.add(pid);


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

    public String getFirstTaskName(String taskName) { //获取该任务最顶级的项目名称
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

    //获取该任务最顶级的项目名称(新方法,解决项目名称不能重复问题)
    public String getFirstTaskName1(Integer taskId){
        Integer pid = -1;
        Integer id = taskId;
        String tempName = null;
        while (pid != 0){
            pid = taskDao.getPidById(id);
            if(pid!=0){
                id = pid;
            }
            tempName = taskDao.getNameById(id);
        }
        return tempName;
    }

    public String getWorkNo(Integer taskId){
        Integer pid = -1;
        Integer id = taskId;
        String tempName = null;
        while (pid != 0){
            pid = taskDao.getPidById(id);
            if(pid!=0){
                id = pid;
            }
            tempName = taskDao.getWorkNoById(id);
        }
        return tempName;
    }

}
