package com.sy.service.impl;

import com.google.common.collect.Lists;
import com.sy.dao.DeptDao;
import com.sy.dao.PersonDao;
import com.sy.dao.PersonEfficiencyDao;
import com.sy.dao.PersonEfficiencyMapper;
import com.sy.entity.*;
import com.sy.service.DeptService;
import com.sy.service.ManageDataService;
import com.sy.service.PersonEfficiencyService;
import com.sy.service.PersonService;
import com.sy.utils.DateUtils;
import com.sy.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.*;

@Service
public class PersonEfficiencyServiceImpl implements PersonEfficiencyService {

    @Autowired
    private PersonEfficiencyDao personEfficiencyDao;

    @Autowired
    private ManageDataService manageDataService;

    @Autowired
    private DeptDao deptDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private PersonService personService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private PersonEfficiencyMapper personEfficiencyMapper;

    @Override
    @Transactional
    public PersonEfficiency saveReport(PersonEfficiency personEfficiency) {

        return personEfficiencyDao.save(personEfficiency);
    }

    @Override
    public Page<PersonEfficiency> initAllData(Integer page, Integer pageSize) {

        Pageable pageable = PageRequest.of(page,pageSize);
        return personEfficiencyDao.findAll(pageable);
    }

    @Override
    public List<PersonEfficiency> initDeptData(String personName,Integer deptId,Date beginTime,Date endTime) throws Exception{

        List<Integer> deptList = new ArrayList<>();

        if(deptId==0){
            List<Integer> depts = deptDao.getIdByPid(0);
            for (Integer dept : depts) {//level为1的部门列表(pId为0)
                addDeptId(dept, deptList);
            }
        }else {
            addDeptId(deptId, deptList);//查出该deptId层级下所有部门Id放入deptList
        }

        //根据部门编号，获取当前部门下（多层部门）的所有员工id
        List<Integer> list = new ArrayList<>();
        for (Integer integer : deptList) {
            for (Integer integer1 : personDao.getPersonIdByDeptId(integer)) {
                list.add(integer1);
            }
        }
        //判定当前员工是否是该部门的负责人,负责人可以查看所有员工信息



        //判断查询人员和部门是否对应
        if(!"".equals(personName)){
            Integer personId = personDao.getIdByName(personName);
            if(personId==null){
                throw new Exception("该员工不存在");
            }

            if(!list.contains(personId)){
                throw new Exception("当前部门不存在该员工");
            }

            list = Collections.singletonList(personId);
        }


        List<PersonEfficiency> list2 = new ArrayList<>();
        for(Integer id: list){
            List<PersonEfficiency> list1 = getDataByIdAndTime(id,beginTime ,endTime);
            for(PersonEfficiency personEfficiency : list1){
                list2.add(personEfficiency);
            }
        }

        Set<Integer> sets = new HashSet<>();
        Map<Integer,List<PersonEfficiency>> map = new HashMap<>();
        for (PersonEfficiency personEfficiency : list2) {
            Integer id = personEfficiency.getPersonId();
            if(map.get(id)==null){
                List<PersonEfficiency> tempList = new ArrayList<>();
                tempList.add(personEfficiency);
                map.put(id,tempList);
            }else {
                map.get(id).add(personEfficiency);
            }
            sets.add(id);
        }


        List<PersonEfficiency> result = new ArrayList<>();
        for (Integer set : sets) {
            int noloadingTime = 0;
            int workingTime = 0;
            int time = 0;
            double noloadingPower = 0;
            double workingPower = 0;
            double power = 0;
            int counts =0;
            for (PersonEfficiency personEfficiency : map.get(set)) {
                noloadingTime += personEfficiency.getNoloadingTime();
                workingTime += personEfficiency.getWorkingTime();
                time += personEfficiency.getWorkingTime();
                time += personEfficiency.getNoloadingTime();
                noloadingPower += Double.parseDouble(personEfficiency.getNoloadingPower());
                workingPower += Double.parseDouble(personEfficiency.getWorkingPower());
                power += Double.parseDouble(personEfficiency.getNoloadingPower());
                power += Double.parseDouble(personEfficiency.getWorkingPower());
                counts += personEfficiency.getCounts();
            }
            Person person = personService.selectPersonById(map.get(set).get(0).getPersonId());
            PersonEfficiency personEfficiency = new PersonEfficiency();
            personEfficiency.setName(person.getName());
            personEfficiency.setDeptId(map.get(set).get(0).getDeptId());
            personEfficiency.setPersonId(map.get(set).get(0).getPersonId());
            personEfficiency.setNoloadingTime(noloadingTime);
            personEfficiency.setNoloadingPower(String.format("%.2f",noloadingPower));
            personEfficiency.setWorkingTime(workingTime);
            personEfficiency.setWorkingPower(String.format("%.2f",workingPower));
            personEfficiency.setTime(time);
            personEfficiency.setEfficiency(String.format("%.2f", (double)workingTime/time*100));
            personEfficiency.setCounts(counts);
            personEfficiency.setRemark(String.valueOf(set));
            result.add(personEfficiency);
        }

        if(result.isEmpty()){
            throw new Exception("没有相关数据");
        }

        return result;
    }



    @Override
    public List<PersonEfficiency> getDataByIdAndTime(Integer personId,Date beginTime, Date endTime) {

        Specification querySpeci = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = Lists.newArrayList();

                if(personId != null){
                    predicates.add(criteriaBuilder.equal(root.get("personId"),personId));
                }

                if (beginTime!=null&&endTime!=null){
                    predicates.add(criteriaBuilder.between(root.get("date"),beginTime,endTime));
                }


                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        return personEfficiencyDao.findAll(querySpeci);
    }


    @Override
    @Transactional
    public void calculateData(String personName, int deptId, Date beginTime, Date endTime) throws Exception {

        personEfficiencyDao.deleteAllData();

        List<Integer> deptList = new ArrayList<>();

        if(deptId==0){
            List<Integer> depts = deptDao.getIdByPid(0);
            for (Integer dept : depts) {
                addDeptId(dept, deptList);
            }
        }else {
            addDeptId(deptId, deptList);//查出该deptId层级下所有部门Id(包含该层级)
        }

        //根据部门编号，获取当前部门下（多层部门）的所有员工id
        List<Integer> list = new ArrayList<>();
        for (Integer integer : deptList) {
            for (Integer integer1 : personDao.getPersonIdByDeptId(integer)) {
                list.add(integer1);
            }
        }
        //判定当前员工是否是该部门的负责人,负责人可以查看所有员工信息



        //判断查询人员和部门是否对应
        if(!"".equals(personName)){
            Integer personId = personDao.getIdByName(personName);
            if(personId==null){
                throw new Exception("该员工不存在");
            }

            if(!list.contains(personId)){
                throw new Exception("当前部门不存在该员工");
            }

            list = Collections.singletonList(personId);
        }
        //原来的 start
        //将所有的数据全部存储
        List<DataManage> dataManageList = new ArrayList<>();
        for (Integer integer : list) {
            for (DataManage data : manageDataService.getAllByData(integer, beginTime, endTime)) {
                dataManageList.add(data);
            }
        }

        System.out.println("当天日期的数据"+dataManageList);

        //分类数据,将数据按照人分类好
        Map<Integer,List<DataManage>> map = new HashMap<>();
        Set<Integer> sets = new HashSet<>();

        for (DataManage dataManage : dataManageList) {
            Integer id = dataManage.getWork().getPerson().getId();
            if(map.get(id)==null){
                List<DataManage> temp = new ArrayList<>();
                temp.add(dataManage);
                map.put(id,temp);
            }else {
                map.get(id).add(dataManage);
            }
            sets.add(id);
        }
        //计算处理数据,并入表
        for (Integer set : sets) {
            PersonEfficiency personEfficiency = new PersonEfficiency();
            int time = 0 ;
            int work_time = 0 ;
            int noloading_time = 0;
            int overCounts = 0;
            BigDecimal wPower = new BigDecimal("0");
            BigDecimal nPower = new BigDecimal("0");
            for (DataManage dataManage : map.get(set)) {
                time += dataManage.getNoloadingTime();
                time += dataManage.getWorkingTime();
                work_time += dataManage.getWorkingTime();
                noloading_time = time - work_time;
                wPower = wPower.add(new BigDecimal(dataManage.getWorkingPower()));
                nPower = nPower.add(new BigDecimal(dataManage.getNoloadingPower()));
                overCounts += Integer.parseInt(dataManage.getRemark());

            }
            personEfficiency.setName(map.get(set).get(0).getWork().getPerson().getName());
            personEfficiency.setDeptOne(map.get(set).get(0).getWork().getPerson().getDept().getName());
            personEfficiency.setWorkingPower(wPower.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            personEfficiency.setNoloadingPower(nPower.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            personEfficiency.setTime(time);
            personEfficiency.setNoloadingTime(noloading_time);
            personEfficiency.setWorkingTime(work_time);
            personEfficiency.setEfficiency(String.format("%.2f", (double)work_time/time*100));
            personEfficiency.setRemark(String.valueOf(set));
            personEfficiency.setCounts(overCounts);
            personEfficiencyDao.save(personEfficiency);
        }
    }

    private void addDeptId(int deptId, List<Integer> deptList) {
        Dept dept = deptService.selectDeptById(deptId);

        List<Dept> dept_1 = dept.getsDepts();

        for (Dept dept1 : dept_1) { //dept1是dept列表下所有车间id（最高级到车间）

            for (Dept getsDept : dept1.getsDepts()) { //getsDept是dept1所有车间列表下的所有工程队id（最高级到工程队）
                for(Dept dept3:getsDept.getsDepts()){ //dept3是getsDept所有工程队列表下的所有班组id（最高级到班组）
                    deptList.add(dept3.getId());
                }
                deptList.add(getsDept.getId());
            }
            deptList.add(dept1.getId());
        }
        deptList.add(dept.getId());
    }


}
