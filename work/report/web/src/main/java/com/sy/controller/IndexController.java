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
import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("index")
public class IndexController {


    @Autowired
    private MachineNowService machineNowService;

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
    private TaskDao taskDao;

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

    @RequestMapping(value = "data",method = RequestMethod.GET)
//    public AjaxResult getData(){ //原来的
    public AjaxResult getData(Integer deptId){

        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        //我新写的start
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
        //我新写的end

        /*原来的
        //在岗人数(查询work表,查询当日扫码的人员的id的个数)
        List<Integer> work_day = workDao.getPersonIdsByDate(today);*/

        int work_day_counts = work_day.size();
        //环比(查询work表,查询昨日扫码的人员的id的个数)
        int pre_work_day_counts = workDao.getPersonIdsByDate(day).size();
       /* //总人数(根据人员表获取总人数)旧的
        int person_counts = personDao.findAll().size();*/

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
        if(person_counts==0){
            workerProportion = (String.format("%.2f", (double)(work_day_counts-pre_work_day_counts)*100));
        }else {
            workerProportion = (String.format("%.2f", (double)(work_day_counts-pre_work_day_counts)/pre_work_day_counts*100));
        }
        //今日工人工效(调用工效查询接口，直接获取最上级数据)
        double todayEfficiency = Double.parseDouble(getEfficiency(today));
        double dayEfficiency = Double.parseDouble(getEfficiency(day));
        String efficiencyProportion = null;
        if(person_counts==0){
            efficiencyProportion = (String.format("%.2f", (todayEfficiency-dayEfficiency)*100));
        }else {
            efficiencyProportion = (String.format("%.2f", (todayEfficiency-dayEfficiency)/dayEfficiency*100));
        }
        //焊机总数
        int machineCounts = machineDao.findAll().size();
        //实时焊机数(查询machineNow表,获取个数)
        List<MachineNow> machineNowList = machineNowDao.findAll();
        int machineNowCounts = machineNowDao.findAll().size();
        //今日工作焊机台数
    //  int machineUseCounts = workDao.getMachineId(today).size();//这是扫码开机的焊机台数，开机后不一定工作

        //今日工作焊机台数(根据今日netty底表数据,根据所有xpg对应数据包的电流判定是否在工作)
        int machineUseCounts = 0;//
        Date date = DateUtils.parseDate(today);

        List<String> xpgs = nettyDao.findAllXpgs(DateUtils.parseDate(today), DateUtils.getNextDay(today));
        for(String xpgId : xpgs) {
            List<Netty> nettyList1 = nettyService.getAllByDateAndXpgId(xpgId, DateUtils.parseDate(day), DateUtils.getNextDay(day));

            /*for (int a = 1; a < nettyList1.size(); a++) {
                Netty netty = nettyList1.get(a);*/
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


        //用电量(调用工程查询接口)
        List<EfficiencyStatisticsVo> efficiencyStatisticsVos = new ArrayList<>();
        try {
       //     efficiencyStatisticsVos = statisticsService.getAllData("",DateUtils.parseDate(today),DateUtils.parseDate(today));
            efficiencyStatisticsVos = statisticsService.getInitData("",DateUtils.parseDate(today),DateUtils.parseDate(today));
        } catch (Exception e) {
            e.printStackTrace();

        }
        //今日工程耗能(调用工程查询接口)
        Set<IndexVo> indexVosProject = new HashSet<>();
        double todayPower = 0.0;
        if(efficiencyStatisticsVos!=null&&!efficiencyStatisticsVos.isEmpty()){
            for (EfficiencyStatisticsVo vo : efficiencyStatisticsVos) {
           //   todayPower += vo.getPower();
                todayPower += vo.getPower3();//今日所有班组级用电量总和就是今日用电量
                IndexVo indexVo = new IndexVo();
                indexVo.setName(vo.getName());
                indexVo.setPower(String.valueOf(vo.getPower()));
                indexVo.setWorkNo(taskDao.getWorkNoByName(vo.getName()));
                indexVosProject.add(indexVo);
            }
        }
        //本月总用电量(调用工程查询接口)
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
    //          totalPower = vo.getPower(); //totalPower += vo.getPower();原来错误的
                totalPower += vo.getSonPower();//方法二
            }
        }
        String currentMonthUsedPower = String.format("%.2f", totalPower);
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
            try {
                List<EfficiencyStatisticsVo> vos = statisticsService.getAllData("",DateUtils.parseDate(dayString),DateUtils.parseDate(dayString));
                double power = 0.0;
                if(!vos.isEmpty()){
                    for (EfficiencyStatisticsVo efficiencyStatisticsVo : vos) {
            //        power = efficiencyStatisticsVo.getPower(); //power += efficiencyStatisticsVo.getPower();原来错误的
                      power += efficiencyStatisticsVo.getSonPower();//方法二
                    }
                }
            //  vo.setPowerValue(String.valueOf(power));
                vo.setPowerValue(String.format("%.2f", power));//保留两位小数
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<EngineeringVo> engineeringVos = engineeringService.getInitData(DateUtils.parseDate(dayString),DateUtils.parseDate(dayString));
            if(!engineeringVos.isEmpty()){
                EngineeringVo engineeringVo = engineeringVos.get(0);
                vo.setRateValue(String.format("%.2f", (double)(engineeringVo.getWorkTime())/engineeringVo.getTime()*100));
            }
            chartResult.add(vo);
        }
        //工效表(调用工效查询接口)
        List<EngineeringVo> vos = new ArrayList<>();
        try {
             vos = engineeringService.getInitData(DateUtils.parseDate(today),DateUtils.parseDate(today));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //班组工效(调用工效查询接口)
        int time = 0;
        int workTime = 0;
        //今日工效
        Set<IndexVo> indexVoEfficiency = new HashSet<>();
        if(vos!=null&&!vos.isEmpty()){
            for (EngineeringVo vo : vos) {
                time += vo.getTime_2();
                workTime += vo.getWorkTime_2();
                IndexVo indexVo = new IndexVo();
                indexVo.setDate(today);
                indexVo.setName(vo.getName_2());
                indexVo.setEfficiency(String.format("%.2f", (double)(vo.getWorkTime_2())/vo.getTime_2()*100));
                indexVoEfficiency.add(indexVo);
            }
        }
        //昨日工效
        try {
            vos = engineeringService.getInitData(DateUtils.parseDate(day),DateUtils.parseDate(day));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<IndexVo> indexVoPrevEfficiency = new HashSet<>();
        if(vos!=null&&!vos.isEmpty()){
            for (EngineeringVo vo : vos) {
                IndexVo indexVo = new IndexVo();
                indexVo.setDate(today);
                indexVo.setName(vo.getName_2());
                indexVo.setEfficiency(String.format("%.2f", (double)(vo.getWorkTime_2())/vo.getTime_2()*100));
                indexVoPrevEfficiency.add(indexVo);
            }
        }

        if(!indexVoEfficiency.isEmpty()&&!indexVoPrevEfficiency.isEmpty()){
            for (IndexVo indexVo : indexVoEfficiency) {
                for (IndexVo vo : indexVoPrevEfficiency) {
                    if(vo.getName().equals(indexVo.getName())){
                        if(vo.getEfficiency() == null){
                            indexVo.setPervEfficiency("0");
                        }
                        indexVo.setPervEfficiency(vo.getEfficiency());
                    }
                }
            }
        }

        //总工效
        AppScan appScan = new AppScan();
        appScan.setEfficiency(chartResult.get(0).getRateValue());
        try {
            double dayEff = Double.parseDouble(chartResult.get(0).getRateValue());
            double prevDayEff = Double.parseDouble(chartResult.get(1).getRateValue());
      //    appScan.setProportion(String.format("%.2f", (dayEff-prevDayEff)*100/prevDayEff)); //环比
            appScan.setProportion(String.format("%.2f", (new BigDecimal(dayEff).subtract(new BigDecimal(prevDayEff))).doubleValue()*100));//应要求环比改成直接相减
        }catch (Exception e){
            e.printStackTrace();
        }
        appScan.setTime(time);
        appScan.setWork_time(workTime);

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayWorkCount", work_day_counts);//今日在岗人数
        result.put("yesterdayWorkCount", pre_work_day_counts);//昨日在岗人数
        result.put("openCount", machineNowCounts);    //实时焊机开机台数
        result.put("workCount", machineUseCounts);    //今天工作焊机台数(扫码开机后电流大于最小工作电流)
        result.put("totalCount", machineCounts);      //焊机总数
        result.put("todayUsedPower", todayPower);     //今日用电量
        result.put("totalWorkCount", person_counts);  //总人数
        result.put("currentMonthUsedPower", currentMonthUsedPower);  //本月总用电量
        result.put("chartResult", chartResult);          //两周图表
        result.put("projectResult", indexVosProject);     //今日工程耗能
        result.put("groupResult", indexVoEfficiency);     //今日工效
        result.put("appScan",appScan);                 //app总工效等
        return result;
    }

    @RequestMapping(value = "/appdata",method = RequestMethod.GET)
//    public AjaxResult getAppData(){
    public AjaxResult getAppData(Integer deptId){

        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        //我新写的start
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
        //我新写的end

        /*原来的
        //在岗人数(查询work表,查询当日扫码的人员的id的个数)
        List<Integer> work_day = workDao.getPersonIdsByDate(today); //work_day是今日所有扫码开关机的人员id*/

        int work_day_counts = work_day.size();
        //环比(查询work表,查询昨日扫码的人员的id的个数)
        int pre_work_day_counts = workDao.getPersonIdsByDate(day).size();
        //总人数(根据人员表获取总人数)
        int person_counts = personDao.findAll().size();
        String workerProportion = null;
        if(person_counts==0){
            workerProportion = (String.format("%.2f", (double)(work_day_counts-pre_work_day_counts)*100));
        }else {
            workerProportion = (String.format("%.2f", (double)(work_day_counts-pre_work_day_counts)/pre_work_day_counts*100));
        }
        //今日工人工效(调用工效查询接口，直接获取最上级数据)
        double todayEfficiency = Double.parseDouble(getEfficiency(today));
        double dayEfficiency = Double.parseDouble(getEfficiency(day));
        String efficiencyProportion = null;
        if(person_counts==0){
            efficiencyProportion = (String.format("%.2f", (todayEfficiency-dayEfficiency)*100));
        }else {
            efficiencyProportion = (String.format("%.2f", (todayEfficiency-dayEfficiency)/dayEfficiency*100));
        }
        //焊机总数
        int machineCounts = machineDao.findAll().size();
        //实时焊机数(查询machineNow表,获取个数)
        List<MachineNow> machineNowList = machineNowDao.findAll();
        int machineNowCounts = machineNowDao.findAll().size();
        /*//今日焊机工作数
        int machineUseCounts = workDao.getMachineId(today).size();*/

        //今日工作焊机台数(根据今日netty底表数据,根据所有xpg对应数据包的电流判定是否在工作)
        int machineUseCounts = 0;//
        List<String> xpgs = nettyDao.findAllXpgs(DateUtils.parseDate(today), DateUtils.getNextDay(today));
        for(String xpgId : xpgs) {
            List<Netty> nettyList1 = nettyService.getAllByDateAndXpgId(xpgId, DateUtils.parseDate(day), DateUtils.getNextDay(day));

            /*for (int a = 1; a < nettyList1.size(); a++) {
                Netty netty = nettyList1.get(a);*/
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

        //用电量(调用工程查询接口)
        List<EfficiencyStatisticsVo> efficiencyStatisticsVos = new ArrayList<>();
        try {
    //        efficiencyStatisticsVos = statisticsService.getAllData("",DateUtils.parseDate(today),DateUtils.parseDate(today));
            efficiencyStatisticsVos = statisticsService.getInitData("",DateUtils.parseDate(today),DateUtils.parseDate(today));
        } catch (Exception e) {
            e.printStackTrace();

        }
        //今日工程耗能(调用工程查询接口)
        Set<IndexVo> indexVosProject = new HashSet<>();
        double todayPower = 0.0;
        if(efficiencyStatisticsVos!=null&&!efficiencyStatisticsVos.isEmpty()){
            for (EfficiencyStatisticsVo vo : efficiencyStatisticsVos) {
        //        todayPower += vo.getPower();
                todayPower += vo.getPower3();//今日所有班组级用电量总和就是今日用电量
                IndexVo indexVo = new IndexVo();
                indexVo.setName(vo.getName());
                indexVo.setPower(String.valueOf(vo.getPower()));
                indexVo.setWorkNo(taskDao.getWorkNoByName(vo.getName()));
                indexVosProject.add(indexVo);
            }
        }
        //本月总用电量(调用工程查询接口)
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
        //      totalPower += vo.getPower();
                totalPower += vo.getSonPower();
            }
        }
        String currentMonthUsedPower = String.format("%.2f", totalPower);
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
            try {
                List<EfficiencyStatisticsVo> vos = statisticsService.getAllData("",DateUtils.parseDate(dayString),DateUtils.parseDate(dayString));
                double power = 0.0;
                if(!vos.isEmpty()){
                    for (EfficiencyStatisticsVo efficiencyStatisticsVo : vos) {
                        power += efficiencyStatisticsVo.getPower();
                    }
                }
                vo.setPowerValue(String.format("%.2f", power));//保留两位小数
            //  vo.setPowerValue(String.valueOf(power));
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<EngineeringVo> engineeringVos = engineeringService.getInitData(DateUtils.parseDate(dayString),DateUtils.parseDate(dayString));
            if(!engineeringVos.isEmpty()){
                EngineeringVo engineeringVo = engineeringVos.get(0);
                vo.setRateValue(String.format("%.2f", (double)(engineeringVo.getWorkTime())/engineeringVo.getTime()*100));
            }
            chartResult.add(vo);
        }
        //工效表(调用工效查询接口)
        List<EngineeringVo> vos = new ArrayList<>();
        try {
            vos = engineeringService.getInitData(DateUtils.parseDate(today),DateUtils.parseDate(today));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //班组工效(调用工效查询接口)
        int time = 0;
        int workTime = 0;
        //今日工效
        Set<IndexVo> indexVoEfficiency = new HashSet<>();
        if(vos!=null&&!vos.isEmpty()){
            for (EngineeringVo vo : vos) {
                time += vo.getTime_2();
                workTime += vo.getWorkTime_2();
                IndexVo indexVo = new IndexVo();
                indexVo.setDate(today);
                indexVo.setName(vo.getName_2());
                indexVo.setEfficiency(String.format("%.2f", (double)(vo.getWorkTime_2())/vo.getTime_2()*100));
                indexVoEfficiency.add(indexVo);
            }
        }
        //昨日工效
        try {
            vos = engineeringService.getInitData(DateUtils.parseDate(day),DateUtils.parseDate(day));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Set<IndexVo> indexVoPrevEfficiency = new HashSet<>();
        if(vos!=null&&!vos.isEmpty()){
            for (EngineeringVo vo : vos) {
                IndexVo indexVo = new IndexVo();
                indexVo.setDate(today);
                indexVo.setName(vo.getName_2());
                indexVo.setEfficiency(String.format("%.2f", (double)(vo.getWorkTime_2())/vo.getTime_2()*100));
                indexVoPrevEfficiency.add(indexVo);
            }
        }

        if(!indexVoEfficiency.isEmpty()&&!indexVoPrevEfficiency.isEmpty()){
            for (IndexVo indexVo : indexVoEfficiency) {
                for (IndexVo vo : indexVoPrevEfficiency) {
                    if(vo.getName().equals(indexVo.getName())){
                        if(vo.getEfficiency() == null){
                            indexVo.setPervEfficiency("0");
                        }
                        indexVo.setPervEfficiency(vo.getEfficiency());
                    }
                }
            }
        }

        //总工效
        AppScan appScan = new AppScan();
        appScan.setEfficiency(chartResult.get(0).getRateValue());
        try {
            double dayEff = Double.parseDouble(chartResult.get(0).getRateValue());
            double prevDayEff = Double.parseDouble(chartResult.get(1).getRateValue());
       //   appScan.setProportion(String.format("%.2f", (dayEff-prevDayEff)*100/prevDayEff));//环比
            appScan.setProportion(String.format("%.2f", (new BigDecimal(dayEff).subtract(new BigDecimal(prevDayEff))).doubleValue()*100));//应要求环比改成直接相减
        }catch (Exception e){
            e.printStackTrace();
        }
        appScan.setTime(time);
        appScan.setWork_time(workTime);

        AjaxResult result = new AjaxResult();
        result.setMsg("操作成功");
        result.setCode(200);
        result.put("todayWorkCount", work_day_counts);//今日在岗人数
        result.put("yesterdayWorkCount", pre_work_day_counts);//昨日在岗人数
        result.put("openCount", machineNowCounts);   //实时焊机开机台数
        result.put("workCount", machineUseCounts);   //今天工作焊机台数
        result.put("totalCount", machineCounts);      //焊机总数
        result.put("todayUsedPower", todayPower);      //今日工程耗能
        result.put("totalWorkCount", person_counts);    //总人数
        result.put("currentMonthUsedPower", currentMonthUsedPower);  //本月总用电量
//        result.put("chartResult", chartResult);          //两周图表
//        result.put("projectResult", indexVosProject);     //今日工程耗能
        result.put("groupResult", indexVoEfficiency);     //今日工效
        result.put("appScan",appScan);                 //app对象()
        return result;
    }


    private String getEfficiency(String today) {
        List<EngineeringVo> engineeringVos = engineeringService.getInitData(DateUtils.parseDate(today),DateUtils.parseDate(today));
        if (engineeringVos.isEmpty()){
            return "0";
        }
        EngineeringVo vo = engineeringVos.get(0);
        return String.format("%.2f", (double)vo.getWorkTime()/vo.getTime()*100);
    }

}
