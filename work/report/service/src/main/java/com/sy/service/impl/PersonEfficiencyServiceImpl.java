package com.sy.service.impl;

import com.sy.dao.DeptDao;
import com.sy.dao.PersonDao;
import com.sy.dao.PersonEfficiencyDao;
import com.sy.entity.DataManage;
import com.sy.entity.PersonEfficiency;
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
    @Transactional
    public void calculateData(String personName, String deptIds, Date beginTime, Date endTime) throws Exception {


        List<Integer> list = new ArrayList<>();

        String[] strs = deptIds.split(",");

        for (String str : strs) {
            for (Integer integer : personDao.getPersonIdByDeptId(Integer.parseInt(str))) {
                list.add(integer);
            }
        }

        if(personName!=null){
            Integer personId = personDao.getIdByName(personName);

            if(personId==null){
                throw new Exception("该员工不存在");
            }

            if(!list.contains(personId)){
                throw new Exception("当前部门不存在该员工");
            }

        }

        List<DataManage> dataManageList = new ArrayList<>();

        for (Integer integer : list) {

            for (DataManage data : manageDataService.getAllByData(integer, DateUtils.parseDate(beginTime), DateUtils.parseDate(endTime))) {
                dataManageList.add(data);
            }

        }

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
        }

        for (Integer set : sets) {
            PersonEfficiency personEfficiency = new PersonEfficiency();
            int time = 0 ;
            int work_time = 0 ;
            BigDecimal wPower = new BigDecimal("0");
            BigDecimal nPower = new BigDecimal("0");
            for (DataManage dataManage : map.get(set)) {
                time += dataManage.getNoloadingTime();
                time += dataManage.getWorkingTime();
                work_time += dataManage.getWorkingTime();
                wPower = wPower.add(new BigDecimal(dataManage.getWorkingPower()));
                nPower = nPower.add(new BigDecimal(dataManage.getNoloadingPower()));
            }
            personEfficiency.setName(map.get(set).get(0).getWork().getPerson().getName());
            personEfficiency.setWorkingPower(wPower.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            personEfficiency.setNoloadingPower(nPower.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            personEfficiency.setTime(time);
            personEfficiency.setWorkingTime(work_time);
            personEfficiency.setEfficiency(String.format("%.2f", (double)work_time/time*100));
            personEfficiencyDao.save(personEfficiency);
        }
    }


}
