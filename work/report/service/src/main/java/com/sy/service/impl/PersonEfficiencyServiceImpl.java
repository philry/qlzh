package com.sy.service.impl;

import com.sy.dao.DeptDao;
import com.sy.dao.PersonDao;
import com.sy.dao.PersonEfficiencyDao;
import com.sy.entity.DataManage;
import com.sy.entity.Dept;
import com.sy.entity.PersonEfficiency;
import com.sy.service.DeptService;
import com.sy.service.ManageDataService;
import com.sy.service.PersonEfficiencyService;
import com.sy.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private DeptService deptService;

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
    public List<PersonEfficiency> initDeptData(Integer deptId) {

        return personEfficiencyDao.selectDeptData(deptId);
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
            addDeptId(deptId, deptList);
        }

        //根据部门编号，获取当前部门下的所有员工id
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
            personEfficiency.setWorkingPower(wPower.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            personEfficiency.setNoloadingPower(nPower.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
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

        for (Dept dept1 : dept_1) {

            for (Dept getsDept : dept1.getsDepts()) {
                for(Dept dept3:getsDept.getsDepts()){
                    deptList.add(dept3.getId());
                }
                deptList.add(getsDept.getId());
            }
            deptList.add(dept1.getId());
        }
        deptList.add(dept.getId());
    }


}
