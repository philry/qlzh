package com.sy.controller;


import com.sy.dao.*;
import com.sy.entity.*;
import com.sy.service.EfficiencyStatisticsService;
import com.sy.service.EngineeringService;
import com.sy.service.MachineNowService;
import com.sy.service.NettyService;
import com.sy.utils.DateUtils;
import com.sy.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("pcIndex")
public class PcIndexController {

    @Autowired
    private MachineNowDao machineNowDao;

    @Autowired
    private WorkDao workDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private EngineeringService engineeringService;

    @Autowired
    private EfficiencyStatisticsService statisticsService;

    @Autowired
    private MachineDao machineDao;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private DeptDao deptDao;

    @Autowired
    private NettyDao nettyDao;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private NettyService nettyService;

    @Autowired
    private XpgDao xpgDao;

    @RequestMapping(value = "todayWorkCount",method = RequestMethod.GET)
    public AjaxResult getTodayWorkCount(Integer deptId){ //今日在岗人数(显示本级部门级及以下部门的扫码人员人数)

        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        List<Integer> personids = new ArrayList<>();

        //最高到生产部级
        List<Integer> personids0 = personDao.getPersonIdByDeptId(deptId);
        for(Integer personId : personids0){
            personids.add(personId);
        }

        //下级部门(最高到车间级)
        List<Integer> deptIds1 = deptDao.getIdByPid(deptId);
        for(Integer deptId1 : deptIds1){
            List<Integer> personids1 = personDao.getPersonIdByDeptId(deptId1);
            for(Integer personId : personids1){
                personids.add(personId);//最高到车间级的部门所有人员放入personids
            }

            //每个部门的下级部门(最高到工程队级)
            List<Integer> deptIds2 = deptDao.getIdByPid(deptId1);
            for(Integer deptId2 : deptIds2){
                List<Integer> personids2 = personDao.getPersonIdByDeptId(deptId2);
                for(Integer personId : personids2){
                    personids.add(personId);//最高到工程队级的部门所有人员放入personids
                }

                //每个部门的下级部门(最高到班组级)
                List<Integer> deptIds3 = deptDao.getIdByPid(deptId2);
                for(Integer deptId3 : deptIds3){
                    List<Integer> personids3 = personDao.getPersonIdByDeptId(deptId3);
                    for(Integer personId : personids3){
                        personids.add(personId); //最高到班组级的部门所有人员放入personids
                    }
                }
            }
        }

        //今日在岗人数(显示本级部门级及以下部门的扫码人员人数)
        List<Integer> work_day = workDao.getPersonIdsByDateAndPersonids(today,personids);
        int work_day_counts = work_day.size();

        //环比(查询work表,查询昨日扫码的人员的id的个数)
        int pre_work_day_counts = workDao.getPersonIdsByDate(day).size();
        //总人数(班长+焊工，所属部门等级为4的人员)
        int person_counts = 0;
        Person person = new Person();
        List<Person> list = personMapper.selectPersonList(person);
        for(Person person2 : list){
            Dept dept  = deptMapper.selectDeptById(person2.getDeptId());
            if(dept.getLevel() == 4){
                person_counts++;
            }
        }

        String workerProportion = null;
//        if(person_counts==0){
            workerProportion = (String.format("%.2f", (double)(work_day_counts-pre_work_day_counts)));//应产品要求,环比是今日减昨日差值
        /*}else {
            workerProportion = (String.format("%.2f", (double)(work_day_counts-pre_work_day_counts)/pre_work_day_counts*100));
        }*/

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayWorkCount", work_day_counts);//今日在岗人数(显示本级部门级及以下部门的扫码人员人数)
        result.put("yesterdayWorkCount", pre_work_day_counts);//昨日在岗人数
        result.put("totalWorkCount", person_counts);  //总人数
        result.put("countCompare", workerProportion);  //人数环比
        return result;
    }


    @RequestMapping(value = "todayRate",method = RequestMethod.GET)
    public AjaxResult getTodayRate() { //今日工人工作效率
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        //两周图表
        List<String> dayStrings = new ArrayList<>();
        int length = 14;
        String tempDay = today;
        dayStrings.add(tempDay);
        for (int i = 0; i <length-1 ; i++) {
            tempDay = DateUtils.getPrevDay(tempDay);
            dayStrings.add(tempDay);
        }

        List<ChartVo> chartResult = new ArrayList<>();
        for (String dayString : dayStrings) {
            ChartVo vo = new ChartVo();
            vo.setDate(dayString);
            List<EngineeringVo> engineeringVos = engineeringService.getInitData(DateUtils.parseDate(dayString),DateUtils.parseDate(dayString));
            if(!engineeringVos.isEmpty()){
                EngineeringVo engineeringVo = engineeringVos.get(0);
                vo.setRateValue(String.format("%.2f", (double)(engineeringVo.getWorkTime())/engineeringVo.getTime()*100));
            }
            chartResult.add(vo);
        }

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayRate", chartResult.get(0).getRateValue());
        return result;
    }

    @RequestMapping(value = "nowMachineCount",method = RequestMethod.GET)
    public AjaxResult getNowMachineCount() {
        //实时焊机数(查询machineNow表,获取个数)
        int machineNowCounts = machineNowDao.findAll().size();
        //焊机总数
        int machineCounts = machineDao.findAll().size();

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("nowMachineCount", machineNowCounts);   //实时焊机开机台数
        result.put("totalCount", machineCounts);      //焊机总数
        return result;
    }

    @RequestMapping(value = "todayMachineUseCount",method = RequestMethod.GET)
    public AjaxResult getTodayMachineUseCount() {
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        //今日工作焊机台数(根据今日netty底表数据,根据所有xpg对应数据包的电流判定是否在工作)
        int machineUseCounts = 0;//
        Date date = DateUtils.parseDate(today);

        List<String> xpgs = nettyDao.findAllXpgs(DateUtils.parseDate(today), DateUtils.getNextDay(today));
        for(String xpgId : xpgs) {
            List<Netty> nettyList1 = nettyService.getAllByDateAndXpgId(xpgId, DateUtils.parseDate(day), DateUtils.getNextDay(day));

            a:  for (Netty netty : nettyList1) {
                //将数据库存储的60s电流取出
                String currentStr = netty.getCurrents();
                List<String> currents = Arrays.asList(currentStr.split(","));
                //根据2G码获取最新的扫码工作信息
                Xpg xpg = xpgDao.getByName(netty.getXpg());
                //获取焊机的电流临界值
                Machine machine = machineDao.getById(xpg.getMachineId());
        //      if(machine != null) {
                    Double minA = machine.getMinA();

                    //处理电流数据，超过最小工作电流machineUseCounts就自增并跳出当前xpgId的循环，进入下一个xpgId的判断
                    for (String current : currents) {
                        double i = Double.parseDouble(current);
                        if (i >= minA) {
                            machineUseCounts++;
                            break a;
                        }
                    }
    //          }
            }
        }

        //焊机总数
        int machineCounts = machineDao.findAll().size();

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayMachineUseCount", machineUseCounts); //今日有效工作台数(扫码开机并且电流大于工作电流的焊机台数)
        result.put("totalCount", machineCounts);      //焊机总数
        return result;
    }

    @RequestMapping(value = "todayPower",method = RequestMethod.GET)
    public AjaxResult getTodayPower() {
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        //用电量(调用工程查询接口)
        List<EfficiencyStatisticsVo> efficiencyStatisticsVos = new ArrayList<>();
        try {
            efficiencyStatisticsVos = statisticsService.getInitData("",DateUtils.parseDate(today),DateUtils.parseDate(today));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //今日工程耗能(调用工程查询接口)
        double todayPower = 0.0;
        if(efficiencyStatisticsVos != null && !efficiencyStatisticsVos.isEmpty()){
            for (EfficiencyStatisticsVo vo : efficiencyStatisticsVos) {
                todayPower += vo.getPower3(); //今日所有班组级用电量总和就是今日用电量
            }
        }

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayPower", String.format("%.2f",todayPower));   //今日用电量
        return result;
    }

    @RequestMapping(value = "currentMonthPower",method = RequestMethod.GET)
    public AjaxResult getCurrentMonthPower() {
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        //本月总用电量(调用工程查询接口)
        List<EfficiencyStatisticsVo> efficiencyStatisticsVos = new ArrayList<>();
        String monthFirstDay = DateUtils.getMonthFirstDay();
        try {
            efficiencyStatisticsVos = statisticsService.getAllData("",DateUtils.parseDate(monthFirstDay),DateUtils.parseDate(today));
        } catch (Exception e) {
            e.printStackTrace();
        }

        double totalPower = 0.0;
        System.out.println(efficiencyStatisticsVos);
        if(efficiencyStatisticsVos!=null&&!efficiencyStatisticsVos.isEmpty()){
            for (EfficiencyStatisticsVo vo : efficiencyStatisticsVos) {
                totalPower += vo.getSonPower();
            }
        }
        String currentMonthUsedPower = String.format("%.2f", totalPower);


        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("currentMonthPower", currentMonthUsedPower);   //本月用电量
        return result;
    }
}
