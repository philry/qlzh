package com.sy.task;

import com.sy.dao.*;
import com.sy.entity.*;
import com.sy.service.ManageDataService;
import com.sy.service.NettyService;
import com.sy.utils.DateUtils;
import com.sy.vo.Unit;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class NewNettyDataHandler {

    @Autowired
    private NettyDao nettyDao;

    @Autowired
    private NettyService nettyService;

    @Autowired
    private DataManageDao dataManageDao;

    @Autowired
    private DataManageMapper dataManageMapper;

    @Autowired
    private ManageDataService manageDataService;

    @Autowired
    private WorkDao workDao;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private XpgDao xpgDao;

    @Autowired
    private MachineDao machineDao;

    @Autowired
    private EngineeringDao engineeringDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private DeptDao deptDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private PersonEfficiencyDao personEfficiencyDao;

    @Autowired
    private StatisticsDao statisticsDao;

    @Autowired
    private EfficiencyStatisticsNewDao efficiencyStatisticsNewDao;

    @Autowired
    private MachineUseDao machineUseDao;

    Logger logger = Logger.getLogger(NewNettyDataHandler.class);

    @Scheduled(cron = "0 30 0 * * ?")
    @Transactional
    public void handleData() {


        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);

        deleteData2(day);
        deleteEightDaysAgoNettyData();

        insertData2(day);
    }



    @Scheduled(cron = "0 0/5 * * * ?")
    @Transactional
    public void handleTodayDataManageData() {


        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);

        dataManageDao.deleteByCreateTime(DateUtils.parseDate(today));

        insertDataManageData(today);

    }


    @Scheduled(cron = "0 2/5 * * * ?")
    @Transactional
    public void handleTodayEfficiencyStatisticsNewData() {



        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);

        efficiencyStatisticsNewDao.deleteByDate(DateUtils.parseDate(today));

        insertEfficiencyStatisticsNewData(today);

    }



    @Scheduled(cron = "0 2/5 * * * ?")
    @Transactional
    public void handleTodayEngineeringData() {

        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);

        engineeringDao.deleteByDate(DateUtils.parseDate(today));

        insertEngineeringData(today);
;
    }


    @Scheduled(cron = "0 2/5 * * * ?")
    @Transactional
    public void handleTodayPersonEfficiencyData() {

        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);

        personEfficiencyDao.deleteByDate(DateUtils.parseDate(today));

        insertPersonEfficiencyData(today);

    }


    @Scheduled(cron = "0 2/5 * * * ?")
    @Transactional
    public void handleTodayMachineUseData() {

        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);

        machineUseDao.deleteByRemark(today);

        insertMachineUseData(today);

    }


    @Scheduled(cron = "0 2/5 * * * ?")
//    @Scheduled(fixedRate = 5 * 60 * 1000)
    @Transactional
    public void handleTodayData() {

        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);

        statisticsDao.deleteByDate(DateUtils.parseDate(today));

        insertStatisticsData(today);

    }

    private void insertDataManageData(String day) {

        List<Netty> nettyList = nettyService.getAllByDate(DateUtils.parseDate(day), DateUtils.getNextDay(day));

        if (nettyList.size() <= 0) {
            System.out.println("无可同步数据");
            return;
        }


       /* Map<String, List<Netty>> result = new HashMap<String, List<Netty>>();
        for (Netty netty : nettyList) {
            String xpg = netty.getXpg();
            if (xpg == null) {
                continue;
            }
            List<Netty> nettys = result.get(xpg);
            if (nettys == null) {
                nettys = new ArrayList<Netty>();
                result.put(xpg, nettys);
            }
            nettys.add(netty);
        }*/


        Map<String, List<Netty>> nettyListMap =
                nettyList.stream().collect(Collectors.groupingBy(Netty::getXpg));

        List<Machine> machineLists = machineDao.findAll();
        Map<Integer, Machine> machineMap = new HashMap<Integer, Machine>();
        for (Machine machine : machineLists) {
            Integer machineId = machine.getId();
            if (machineId == null) {
                continue;
            }
            machineMap.put(machineId, machine);
        }

        List<Xpg> xpgLists = xpgDao.findAll();
        Map<String, Xpg> xpgMap = new HashMap<String, Xpg>();
        for (Xpg xpg : xpgLists) {
            String xpgName = xpg.getName();
            if (xpgName == null) {
                continue;
            }
            xpgMap.put(xpgName, xpg);
        }

        /*Map<Integer,Work> lastOnWorkMap = new HashMap<>();
        for (Machine machine : machineLists) {
            Integer machineId = machine.getId();
            Work lastOnWork = workDao.getLastOnWork();
            if(lastOnWork == null){
                continue;
            }
            lastOnWorkMap.put(machineId,lastOnWork);
        }

        Map<Integer,Work> lastOffWorkMap = new HashMap<>();
        for (Machine machine : machineLists) {
            Integer machineId = machine.getId();
            Work lastOffWork = workDao.getLastOffWork();
            if(lastOffWork == null){
                continue;
            }
            lastOffWorkMap.put(machineId,lastOffWork);
        }*/


        for (String xpgId : nettyListMap.keySet()) {
            long groupBeginTime = System.currentTimeMillis();
            if (nettyListMap.get(xpgId).size() >= 2) {
                /*Netty netty0 = nettyListMap.get(xpgId).get(0);
                Xpg xpg0 = xpgMap.get(netty0.getXpg());
                String dataStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, netty0.getCreateTime());
                Work work = workDao.getLastWorkByTime(dataStr, xpg0.getMachineId());*/

                for (int a = 1; a < nettyListMap.get(xpgId).size(); a++) {
                    /*
                    long beginTime = System.currentTimeMillis();*/

                    Netty netty = nettyListMap.get(xpgId).get(a);
                    if (netty == null) {
                        continue;
                    }



                    DataManage data = new DataManage();
                    data.setCreateTime(new Timestamp(DateUtils.parseDate(day).getTime()));


                    String currentStr = netty.getCurrents();
                    List<String> currents = Arrays.asList(currentStr.split(","));


//                    Xpg xpg = xpgDao.getByName(netty.getXpg());
                    Xpg xpg = xpgMap.get(netty.getXpg());
                    String dataStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, netty.getCreateTime());
//                    Work work = workDao.getLastWorkByTime(dataStr, xpg.getMachineId());
                    Work work = workMapper.getLastWorkByTime(dataStr, xpg.getMachineId());

                    if (work != null && "1".equals(work.getOperate())) {
                        continue;
                    }

                    /*Work onWork = lastOnWorkMap.get(xpg.getMachineId());
                    Work offWork = lastOffWorkMap.get(xpg.getMachineId());
                    if( onWork != null && offWork != null
                            && offWork.getCreateTime().after(onWork.getCreateTime())
                            && netty.getCreateTime().after(offWork.getCreateTime()) ){
                        continue;
                    }*/


//                    Machine machine = machineDao.getById(xpg.getMachineId());
                    Machine machine = machineMap.get(xpg.getMachineId());

                    if (machine != null) {
                        Double maxA = machine.getMaxA();
                        Double minA = machine.getMinA();

                        int noloadingTime = 0;
                        int workingTime = 0;
                        BigDecimal iWorking = new BigDecimal(0);
                        BigDecimal iNoloading = new BigDecimal(0);


                        int warningCounts = 0;


                        for (String current : currents) {
                            double i = Double.parseDouble(current);
                            if (i > maxA) {
                                warningCounts++;
                            } else if (i >= minA) {
                                workingTime++;
                                iWorking = iWorking.add(new BigDecimal(i));
                            } else if (i >= 0) {
                                noloadingTime++;
                                iNoloading = iNoloading.add(new BigDecimal(i));
                            }
                        }

                        //BigDecimal（str1）.subtract（str2）
                        BigDecimal power = new BigDecimal(netty.getPower()).subtract(new BigDecimal(nettyListMap.get(xpgId).get(a - 1).getPower()));

                        if (power.doubleValue() < 0) {
                            int id = netty.getId();
                            nettyDao.deleteById(id);
                            continue;
                        }

                        BigDecimal iTotal = iWorking.add(iNoloading);
                        BigDecimal workingPower = null;
                        BigDecimal noloadingPower = new BigDecimal("0");

                        try {
                            if (0 == iTotal.doubleValue()) {
                                workingPower = new BigDecimal("0");
                            } else {
                                workingPower = power.multiply(iWorking.divide(iTotal, 2, BigDecimal.ROUND_HALF_UP));
                            }
                            noloadingPower = power.subtract(workingPower); //noloadingPower= power-workingPower
                        } catch (Exception e) {
                            e.printStackTrace();
                            workingPower = power;
                        }

                        data.setDate(DateUtils.parseDate(day));
                        data.setWork(work);
                        data.setNoloadingPower(noloadingPower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//BigDecimal小数点后四舍五入保留2位小数转为Double类型
                        data.setWorkingPower(workingPower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        data.setWorkingTime(workingTime);
                        data.setNoloadingTime(noloadingTime);

                        if (warningCounts == 3) {
                            data.setRemark("1");
                        } else {
                            data.setRemark("0");
                        }
                        dataManageDao.save(data);

                    }
                }
            }
            long groupEndTime = System.currentTimeMillis();
        }
    }

    private void insertEfficiencyStatisticsNewData(String day) {

        List<DataManage> dataList = manageDataService.getAllByData(0, DateUtils.parseDate(day), DateUtils.getNextDay(day));
        handleEfficiencyStatisticsReport(day, dataList);
    }

    private void insertStatisticsData(String day) {


        List<DataManage> dataList = manageDataService.getAllByData(0, DateUtils.parseDate(day), DateUtils.getNextDay(day));

        Set<Integer> taskIds = new HashSet<>();

        Map<Integer, List<DataManage>> map = new HashMap<>();

        handleDataManage(dataList, taskIds, map, "部门");

        for (Integer taskId : taskIds) {

            Statistics statistics = new Statistics();
            String name = taskDao.getById(taskId).getProjectName();
            int time = 0;
            int working_time = 0;
            BigDecimal ePower = new BigDecimal("0");
            for (DataManage dataManage : map.get(taskId)) {
                time += dataManage.getWorkingTime();
                time += dataManage.getNoloadingTime();
                working_time += dataManage.getWorkingTime();
                ePower = ePower.add(new BigDecimal(dataManage.getNoloadingPower()));
                ePower = ePower.add(new BigDecimal(dataManage.getWorkingPower()));
            }
            statistics.setTime(time);
            statistics.setWorkingTime(working_time);
            statistics.setCreateTime(new Timestamp(new Date().getTime()));
            statistics.setDate(DateUtils.parseDate(day));
            statistics.setName(name);
            statistics.setTaskId(taskId);
            statistics.setEfficiency(String.format("%.2f", (double) working_time / time * 100));
            statistics.setPower(ePower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            statisticsDao.save(statistics);
        }
    }

    private void insertEngineeringData(String day) {

        List<DataManage> dataList = manageDataService.getAllByData(0, DateUtils.parseDate(day), DateUtils.getNextDay(day));
        handleDeptReport(day, dataList);
    }

    private void insertPersonEfficiencyData(String day) {


        List<DataManage> dataList = manageDataService.getAllByData(0, DateUtils.parseDate(day), DateUtils.getNextDay(day));


        Map<Integer,List<DataManage>> map = new HashMap<>();
        Set<Integer> sets = new HashSet<>();

        for (DataManage dataManage : dataList) {
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
            personEfficiency.setPersonId(set);
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
            personEfficiency.setDate(DateUtils.parseDate(day));
            personEfficiency.setCreateTime(new Timestamp(new Date().getTime()));
            personEfficiencyDao.save(personEfficiency);
        }
    }

    private void insertMachineUseData(String day) {

        List<Integer> ids = workDao.getMachineId(day);

        for (Integer id : ids) {

            List<Work> works = workDao.getWorkByMachineId(id);
            int noloadingTime = 0;
            int workingTime = 0;
            int time = 0;
            double noloadingPower = 0;
            double workingPower = 0;
            double power = 0;
            int counts = 0;
            for (Work work : works) {
                List<DataManage> dataManages = manageDataService.getDataByWork(work.getId(), DateUtils.parseDate(day), DateUtils.getNextDay(day));
                if (!dataManages.isEmpty()) {
                    for (DataManage dataManage : dataManages) {
                        noloadingTime += dataManage.getNoloadingTime();
                        workingTime += dataManage.getWorkingTime();
                        time += dataManage.getWorkingTime();
                        time += dataManage.getNoloadingTime();
                        noloadingPower += dataManage.getNoloadingPower();
                        workingPower += dataManage.getWorkingPower();
                        power += dataManage.getNoloadingPower();
                        power += dataManage.getWorkingPower();
                        counts += Integer.parseInt(dataManage.getRemark());
                    }
                }
            }
            MachineUse machineUse = new MachineUse();
            machineUse.setDepeName(works.get(0).getMachine().getDept().getName());
            machineUse.setMachineId(works.get(0).getMachine().getId());
            machineUse.setNoloadingTime(noloadingTime);
            machineUse.setNoloadingPower(String.format("%.2f", noloadingPower));
            machineUse.setWorkTime(workingTime);
            machineUse.setWorkingPower(String.format("%.2f", workingPower));
            machineUse.setTime(time);
            machineUse.setPower(String.format("%.2f", power));
            machineUse.setOvercounts(String.valueOf(counts));
            machineUse.setRemark(day);
            machineUse.setCreateTime(DateUtils.parseDate(day));
            machineUseDao.save(machineUse);
        }
    }




    private void handleDataManage(List<DataManage> dataList, Set<Integer> taskIds, Map<Integer, List<DataManage>> map, String type) {
        for (DataManage dataManage : dataList) {
            Integer taskId = dataManage.getWork().getTask().getId();
            if ("人员".equals(type)) {
                taskId = dataManage.getWork().getPerson().getId();
            }
            taskIds.add(taskId);
            if (map.get(taskId) == null) {
                List<DataManage> list = new ArrayList<>();
                list.add(dataManage);
                map.put(taskId, list);
            } else {
                map.get(taskId).add(dataManage);
            }
        }
    }


    private void handleDeptReport(String day, List<DataManage> dataList) {
        Map<Integer, List<DataManage>> map = new HashMap<>();


        Set<Integer> personIds = new HashSet<>();

        handleDataManage(dataList, personIds, map, "人员");


        Map<Integer, Unit> unitPerson = new HashMap<>();
        for (Integer personId : personIds) {
            Unit unit = new Unit();
            int time = 0;
            int working_time = 0;
            BigDecimal ePower = new BigDecimal("0");
            for (DataManage dataManage : map.get(personId)) {
                time += dataManage.getNoloadingTime();
                time += dataManage.getWorkingTime();
                working_time += dataManage.getWorkingTime();
                ePower = ePower.add(new BigDecimal(dataManage.getNoloadingPower()));
                ePower = ePower.add(new BigDecimal(dataManage.getWorkingPower()));
            }

            unit.setTime(time);
            unit.setWorkTime(working_time);
            unit.setPower(ePower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            unitPerson.put(personId, unit);

        }


        Map<Integer, List<Unit>> unitDept_3 = new HashMap<>();
        Set<Integer> dept_3 = new HashSet<>();
        for (Integer personId : personIds) {
            Person person = personDao.getById(personId);
            Integer deptId = person.getDept().getId();

            if (unitDept_3.get(deptId) == null) {
                List<Unit> list = new ArrayList<>();
                list.add(unitPerson.get(personId));
                unitDept_3.put(deptId, list);
            } else {
                unitDept_3.get(deptId).add(unitPerson.get(personId));
            }

            dept_3.add(deptId);
        }


        Map<Integer, Engineering> engineeringMap_3 = new HashMap<>();
        for (Integer deptId : dept_3) {
            int time = 0;
            int working_time = 0;
            BigDecimal ePower = new BigDecimal("0");
            for (Unit unit : unitDept_3.get(deptId)) {
                time += unit.getTime();
                working_time += unit.getWorkTime();
                ePower = ePower.add(new BigDecimal(unit.getPower()));
            }


            Dept dept = deptDao.getById(deptId);
            Engineering engineering = getEngineering(day, time, working_time, ePower, dept, 4);

            engineeringMap_3.put(deptId, engineering);

        }


        Map<Integer, List<Engineering>> engineeringData_2 = new HashMap<>();
        Set<Integer> dept_2 = new HashSet<>();
        for (Integer integer : dept_3) {
            Dept dept = deptDao.getById(integer);
            if (engineeringData_2.get(dept.getPid()) == null) {
                List<Engineering> list = new ArrayList<>();
                list.add(engineeringMap_3.get(integer));
                engineeringData_2.put(dept.getPid(), list);
            } else {
                engineeringData_2.get(dept.getPid()).add(engineeringMap_3.get(integer));
            }

            dept_2.add(dept.getPid());
        }


        Map<Integer, Engineering> engineeringMap_2 = new HashMap<>();
        for (Integer integer : dept_2) {
            int time = 0;
            int working_time = 0;
            BigDecimal ePower = new BigDecimal("0");
            for (Engineering engineering : engineeringData_2.get(integer)) {
                time += engineering.getTime();
                working_time += engineering.getWorkingTime();
                ePower = ePower.add(new BigDecimal(engineering.getPower()));
            }

            Dept dept = deptDao.getById(integer);
            Engineering engineering = getEngineering(day, time, working_time, ePower, dept, 3);

            engineeringMap_2.put(integer, engineering);

        }


        Map<Integer, List<Engineering>> engineeringData_1 = new HashMap<>();
        Set<Integer> dept_1 = new HashSet<>();
        for (Integer integer : dept_2) {
            Dept dept = deptDao.getById(integer);
            if (engineeringData_1.get(dept.getPid()) == null) {
                List<Engineering> list = new ArrayList<>();
                list.add(engineeringMap_2.get(integer));
                engineeringData_1.put(dept.getPid(), list);
            } else {
                engineeringData_1.get(dept.getPid()).add(engineeringMap_2.get(integer));
            }

            dept_1.add(dept.getPid());
        }


        Map<Integer, Engineering> engineeringMap_1 = new HashMap<>();
        for (Integer integer : dept_1) {
            int time = 0;
            int working_time = 0;
            BigDecimal ePower = new BigDecimal("0");
            for (Engineering engineering : engineeringData_1.get(integer)) {
                time += engineering.getTime();
                working_time += engineering.getWorkingTime();
                ePower = ePower.add(new BigDecimal(engineering.getPower()));
            }

            Dept dept = deptDao.getById(integer);
            Engineering engineering = getEngineering(day, time, working_time, ePower, dept, 2);

            engineeringMap_1.put(integer, engineering);
        }


        Map<Integer, List<Engineering>> engineeringData_0 = new HashMap<>();
        Set<Integer> dept_0 = new HashSet<>();
        for (Integer integer : dept_1) {
            Dept dept = deptDao.getById(integer);
            if (engineeringData_0.get(dept.getPid()) == null) {
                List<Engineering> list = new ArrayList<>();
                list.add(engineeringMap_1.get(integer));
                engineeringData_0.put(dept.getPid(), list);
            } else {
                engineeringData_0.get(dept.getPid()).add(engineeringMap_1.get(integer));
            }

            dept_0.add(dept.getPid());
        }


        Map<Integer, Engineering> engineeringMap_0 = new HashMap<>();
        for (Integer integer : dept_0) {
            int time = 0;
            int working_time = 0;
            BigDecimal ePower = new BigDecimal("0");
            for (Engineering engineering : engineeringData_0.get(integer)) {
                time += engineering.getTime();
                working_time += engineering.getWorkingTime();
                ePower = ePower.add(new BigDecimal(engineering.getPower()));
            }

            Dept dept = deptDao.getById(integer);
            Engineering engineering = getEngineering(day, time, working_time, ePower, dept, 1);

            engineeringMap_0.put(integer, engineering);
        }


        for (Integer integer_0 : dept_0) {
            Engineering engineering_0 = engineeringMap_0.get(integer_0);
            engineering_0.setPid(0);
            int pid_1 = engineeringDao.save(engineering_0).getId();
            List<Integer> list_1 = deptDao.getIdByPid(integer_0);
            for (Integer integer_1 : dept_1) {
                Engineering engineering_1 = engineeringMap_1.get(integer_1);
                if (engineering_1 != null) {
                    engineering_1.setPid(pid_1);
                    int pid_2 = engineeringDao.save(engineering_1).getId();
                    List<Integer> list_2 = deptDao.getIdByPid(integer_1);
                    for (Integer integer_2 : list_2) {
                        Engineering engineering_2 = engineeringMap_2.get(integer_2);
                        if (engineering_2 != null) {
                            engineering_2.setPid(pid_2);
                            int pid_3 = engineeringDao.save(engineering_2).getId();
                            List<Integer> list_3 = deptDao.getIdByPid(integer_2);
                            for (Integer integer_3 : list_3) {
                                Engineering engineering_3 = engineeringMap_3.get(integer_3);
                                if (engineering_3 != null) {
                                    engineering_3.setPid(pid_3);
                                    engineeringDao.save(engineering_3);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private void handleEfficiencyStatisticsReport(String day, List<DataManage> dataList) {
        Map<Integer, List<DataManage>> map = new HashMap<>();


        Set<Integer> taskIds = new HashSet<>();

        handleDataManage(dataList, taskIds, map, "部门");


        Map<Integer, Unit> unitTask = new HashMap<>();
        for (Integer taskId : taskIds) {
            Unit unit = new Unit();
            int time = 0;
            int working_time = 0;
            BigDecimal ePower = new BigDecimal("0");
            for (DataManage dataManage : map.get(taskId)) {
                time += dataManage.getNoloadingTime();
                time += dataManage.getWorkingTime();
                working_time += dataManage.getWorkingTime();
                ePower = ePower.add(new BigDecimal(dataManage.getNoloadingPower()));
                ePower = ePower.add(new BigDecimal(dataManage.getWorkingPower()));
            }

            unit.setTime(time);
            unit.setWorkTime(working_time);
            unit.setPower(ePower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

            unitTask.put(taskId, unit);
        }


        Map<Integer, List<Unit>> unitTask_3 = new HashMap<>();
        Set<Integer> task_3 = new HashSet<>();
        for (Integer taskId : taskIds) {

            Integer banzuTaskId = taskDao.getPidById(taskId);

            if (unitTask_3.get(banzuTaskId) == null) {
                List<Unit> list = new ArrayList<>();
                list.add(unitTask.get(taskId));
                unitTask_3.put(banzuTaskId, list);
            } else {
                unitTask_3.get(banzuTaskId).add(unitTask.get(taskId));
            }

            task_3.add(banzuTaskId);
        }


        Map<Integer, EfficiencyStatisticsNew> efficiencyStatisticsMap_3 = new HashMap<>();
        for (Integer taskId : task_3) {
            int time = 0;
            int working_time = 0;
            BigDecimal ePower = new BigDecimal("0");
            for (Unit unit : unitTask_3.get(taskId)) {
                time += unit.getTime();
                working_time += unit.getWorkTime();
                ePower = ePower.add(new BigDecimal(unit.getPower()));
            }


            Task task = taskDao.getById(taskId);
            EfficiencyStatisticsNew efficiencyStatistics = getEfficiencyStatisticsNew(day, time, working_time, ePower, task, 4);

            efficiencyStatisticsMap_3.put(taskId, efficiencyStatistics);
        }


        Map<Integer, List<EfficiencyStatisticsNew>> efficiencyStatisticsData_2 = new HashMap<>();
        Set<Integer> task_2 = new HashSet<>();
        for (Integer integer : task_3) {
            Task task = taskDao.getById(integer);
            if (efficiencyStatisticsData_2.get(task.getPid()) == null) {
                List<EfficiencyStatisticsNew> list = new ArrayList<>();
                list.add(efficiencyStatisticsMap_3.get(integer));
                efficiencyStatisticsData_2.put(task.getPid(), list);
            } else {
                efficiencyStatisticsData_2.get(task.getPid()).add(efficiencyStatisticsMap_3.get(integer));
            }

            task_2.add(task.getPid());
        }


        Map<Integer, EfficiencyStatisticsNew> efficiencyStatisticsMap_2 = new HashMap<>();
        for (Integer integer : task_2) {
            int time = 0;
            int working_time = 0;
            BigDecimal ePower = new BigDecimal("0");
            for (EfficiencyStatisticsNew efficiencyStatistics : efficiencyStatisticsData_2.get(integer)) {
                time += efficiencyStatistics.getTime();
                working_time += efficiencyStatistics.getWorkingTime();
                ePower = ePower.add(new BigDecimal(efficiencyStatistics.getPower()));
            }


            Task task = taskDao.getById(integer);
            EfficiencyStatisticsNew efficiencyStatistics = getEfficiencyStatisticsNew(day, time, working_time, ePower, task, 3);


            efficiencyStatisticsMap_2.put(integer, efficiencyStatistics);
        }


        Map<Integer, List<EfficiencyStatisticsNew>> efficiencyStatisticsData_1 = new HashMap<>();
        Set<Integer> task_1 = new HashSet<>();
        for (Integer integer : task_2) {
            Task task = taskDao.getById(integer);
            if (efficiencyStatisticsData_1.get(task.getPid()) == null) {
                List<EfficiencyStatisticsNew> list = new ArrayList<>();
                list.add(efficiencyStatisticsMap_2.get(integer));
                efficiencyStatisticsData_1.put(task.getPid(), list);
            } else {
                efficiencyStatisticsData_1.get(task.getPid()).add(efficiencyStatisticsMap_2.get(integer));
            }

            task_1.add(task.getPid());
        }


        Map<Integer, EfficiencyStatisticsNew> efficiencyStatisticsMap_1 = new HashMap<>();
        for (Integer integer : task_1) {
            int time = 0;
            int working_time = 0;
            BigDecimal ePower = new BigDecimal("0");
            for (EfficiencyStatisticsNew efficiencyStatistics : efficiencyStatisticsData_1.get(integer)) {
                time += efficiencyStatistics.getTime();
                working_time += efficiencyStatistics.getWorkingTime();
                ePower = ePower.add(new BigDecimal(efficiencyStatistics.getPower()));
            }


            Task task = taskDao.getById(integer);
            EfficiencyStatisticsNew efficiencyStatistics = getEfficiencyStatisticsNew(day, time, working_time, ePower, task, 2);


            efficiencyStatisticsMap_1.put(integer, efficiencyStatistics);
        }



        Map<Integer, List<EfficiencyStatisticsNew>> efficiencyStatisticsData_0 = new HashMap<>();
        Set<Integer> task_0 = new HashSet<>();
        for (Integer integer : task_1) {
            Task task = taskDao.getById(integer);
            if (efficiencyStatisticsData_0.get(task.getPid()) == null) {
                List<EfficiencyStatisticsNew> list = new ArrayList<>();
                list.add(efficiencyStatisticsMap_1.get(integer));
                efficiencyStatisticsData_0.put(task.getPid(), list);
            } else {
                efficiencyStatisticsData_0.get(task.getPid()).add(efficiencyStatisticsMap_1.get(integer));
            }

            task_0.add(task.getPid());
        }


        Map<Integer, EfficiencyStatisticsNew> efficiencyStatisticsMap_0 = new HashMap<>();
        for (Integer integer : task_0) {
            int time = 0;
            int working_time = 0;
            BigDecimal ePower = new BigDecimal("0");
            for (EfficiencyStatisticsNew efficiencyStatistics : efficiencyStatisticsData_0.get(integer)) {
                time += efficiencyStatistics.getTime();
                working_time += efficiencyStatistics.getWorkingTime();
                ePower = ePower.add(new BigDecimal(efficiencyStatistics.getPower()));
            }


            Task task = taskDao.getById(integer);
            EfficiencyStatisticsNew efficiencyStatistics = getEfficiencyStatisticsNew(day, time, working_time, ePower, task, 1);


            efficiencyStatisticsMap_0.put(integer, efficiencyStatistics);
        }


        for (Integer integer_0 : task_0) {
            EfficiencyStatisticsNew efficiencyStatistics_0 = efficiencyStatisticsMap_0.get(integer_0);
            efficiencyStatistics_0.setPid(0);
            int pid_1 = efficiencyStatisticsNewDao.save(efficiencyStatistics_0).getId();
            List<Integer> list_1 = taskDao.getIdsByPid(integer_0);
            for (Integer integer_1 : list_1) {
                EfficiencyStatisticsNew efficiencyStatistics_1 = efficiencyStatisticsMap_1.get(integer_1);
                if (efficiencyStatistics_1 != null) {
                    efficiencyStatistics_1.setPid(pid_1);
                    int pid_2 = efficiencyStatisticsNewDao.save(efficiencyStatistics_1).getId();
                    List<Integer> list_2 = taskDao.getIdsByPid(integer_1);
                    for (Integer integer_2 : list_2) {
                        EfficiencyStatisticsNew efficiencyStatistics_2 = efficiencyStatisticsMap_2.get(integer_2);
                        if (efficiencyStatistics_2 != null) {
                            efficiencyStatistics_2.setPid(pid_2);
                            int pid_3 = efficiencyStatisticsNewDao.save(efficiencyStatistics_2).getId();
                            List<Integer> list_3 = taskDao.getIdsByPid(integer_2);
                            for (Integer integer_3 : list_3) {
                                EfficiencyStatisticsNew efficiencyStatistics_3 = efficiencyStatisticsMap_3.get(integer_3);
                                if (efficiencyStatistics_3 != null) {
                                    efficiencyStatistics_3.setPid(pid_3);
                                    efficiencyStatisticsNewDao.save(efficiencyStatistics_3);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private Engineering getEngineering(String day, int time, int working_time, BigDecimal ePower, Dept dept, int level) {
        Engineering engineering = new Engineering();
        engineering.setLevel(level);
        engineering.setDate(DateUtils.parseDate(day));
        engineering.setCreateTime(new Timestamp(new Date().getTime()));
        engineering.setName(dept.getName());
        engineering.setDeptId(dept.getId());
        engineering.setTime(time);
        engineering.setWorkingTime(working_time);
        engineering.setPower(ePower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        engineering.setEfficency(String.format("%.2f", (double) working_time / time * 100));
        return engineering;
    }

    private EfficiencyStatisticsNew getEfficiencyStatisticsNew(String day, int time, int working_time, BigDecimal ePower, Task task, int level) {
        EfficiencyStatisticsNew efficiencyStatistics = new EfficiencyStatisticsNew();
        efficiencyStatistics.setLevel(level);
        efficiencyStatistics.setDate(DateUtils.parseDate(day));
        efficiencyStatistics.setCreateTime(new Timestamp(new Date().getTime()));
        efficiencyStatistics.setName(task.getProjectName());
        efficiencyStatistics.setTaskId(task.getId());
        efficiencyStatistics.setTime(time);
        efficiencyStatistics.setWorkingTime(working_time);
        efficiencyStatistics.setPower(ePower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        efficiencyStatistics.setEfficiency(String.format("%.2f", (double) working_time / time * 100));
        return efficiencyStatistics;
    }

    public void deleteEightDaysAgoNettyData() {

        LocalDateTime now = LocalDateTime.now();
        String nowTime = now.format(DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime minusTime = now.minusDays(8);
        String eightDaysAgoTime = minusTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
        nettyDao.deleteByDate(DateUtils.parseDate(eightDaysAgoTime));
    }

    private void deleteData2(String day) {

        dataManageDao.deleteByCreateTime(DateUtils.parseDate(day));
        engineeringDao.deleteByDate(DateUtils.parseDate(day));
        statisticsDao.deleteByDate(DateUtils.parseDate(day));
        efficiencyStatisticsNewDao.deleteByDate(DateUtils.parseDate(day));
        machineUseDao.deleteByRemark(day);
        personEfficiencyDao.deleteByDate(DateUtils.parseDate(day));
    }

    private void insertData2(String day) {

        List<Netty> nettyList = nettyService.getAllByDate(DateUtils.parseDate(day), DateUtils.getNextDay(day));

        if (nettyList.size() <= 0) {
            System.out.println("无可同步数据");
            return;
        }

        //start2
/*        Map<String, List<Netty>> result = new HashMap<String, List<Netty>>();
        for (Netty netty : nettyList) {
            String xpg = netty.getXpg();
            if (xpg == null) {
                continue;
            }
            List<Netty> nettys = result.get(xpg);
            if (nettys == null) {
                nettys = new ArrayList<Netty>();
                result.put(xpg, nettys);
            }
            nettys.add(netty);
        }*/


        Map<String, List<Netty>> result =
                nettyList.stream().collect(Collectors.groupingBy(Netty::getXpg));

        List<Machine> machineLists = machineDao.findAll();
        Map<Integer, Machine> machineMap = new HashMap<Integer, Machine>();
        for (Machine machine : machineLists) {
            Integer machineId = machine.getId();
            if (machineId == null) {
                continue;
            }
            machineMap.put(machineId, machine);
        }

        List<Xpg> xpgLists = xpgDao.findAll();
        Map<String, Xpg> xpgMap = new HashMap<String, Xpg>();
        for (Xpg xpg : xpgLists) {
            String xpgName = xpg.getName();
            if (xpgName == null) {
                continue;
            }
            xpgMap.put(xpgName, xpg);
        }

        for (String xpgId : result.keySet()) {
            if (result.get(xpgId).size() >= 2) {
                for (int a = 1; a < result.get(xpgId).size(); a++) {
                    Netty netty = result.get(xpgId).get(a);
                    if (netty == null) {
                        continue;
                    }
                    //end2

                    DataManage data = new DataManage();
                    data.setCreateTime(new Timestamp(DateUtils.parseDate(day).getTime()));


                    String currentStr = netty.getCurrents();
                    List<String> currents = Arrays.asList(currentStr.split(","));

//                    Xpg xpg = xpgDao.getByName(netty.getXpg());
                    Xpg xpg = xpgMap.get(netty.getXpg());
                    String dataStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, netty.getCreateTime());
                    Work work = workDao.getLastWorkByTime(dataStr, xpg.getMachineId());
                    if ("1".equals(work.getOperate())) {
                        continue;
                    }

//                   Machine machine = machineDao.getById(xpg.getMachineId());
                    Machine machine = machineMap.get(xpg.getMachineId());
                    if (machine != null) {
                        Double maxA = machine.getMaxA();
                        Double minA = machine.getMinA();

                        int noloadingTime = 0;
                        int workingTime = 0;
                        BigDecimal iWorking = new BigDecimal(0);
                        BigDecimal iNoloading = new BigDecimal(0);


                        int warningCounts = 0;


                        for (String current : currents) {
                            double i = Double.parseDouble(current);
                            if (i > maxA) {
                                warningCounts++;
                            } else if (i >= minA) {
                                workingTime++;
                                iWorking = iWorking.add(new BigDecimal(i));
                            } else if (i >= 0) {
                                noloadingTime++;
                                iNoloading = iNoloading.add(new BigDecimal(i));
                            }
                        }


//                        BigDecimal power = new BigDecimal(netty.getPower()).subtract(new BigDecimal(nettyList1.get(a - 1).getPower()));/
                        BigDecimal power = new BigDecimal(netty.getPower()).subtract(new BigDecimal(result.get(xpgId).get(a - 1).getPower()));
                        if (power.doubleValue() < 0) {
                            int id = netty.getId();
                            nettyDao.deleteById(id);
                            continue;
                        }


                        BigDecimal iTotal = iWorking.add(iNoloading);
                        BigDecimal workingPower = null;

                        BigDecimal noloadingPower = new BigDecimal("0");

                        try {
                            if (0 == iTotal.doubleValue()) {
                                workingPower = new BigDecimal("0");
                            } else {
                                workingPower = power.multiply(iWorking.divide(iTotal, 2, BigDecimal.ROUND_HALF_UP));
                            }
                            noloadingPower = power.subtract(workingPower); //noloadingPower= power-workingPower
                        } catch (Exception e) {
                            e.printStackTrace();
                            workingPower = power;
                        }

                        data.setDate(DateUtils.parseDate(day));
                        data.setWork(work);
                        data.setNoloadingPower(noloadingPower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        data.setWorkingPower(workingPower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        data.setWorkingTime(workingTime);
                        data.setNoloadingTime(noloadingTime);

                        if (warningCounts == 3) {
                            data.setRemark("1");
                        } else {
                            data.setRemark("0");
                        }

                        dataManageDao.save(data);
                    }
                }
            }
        }
        //


        List<DataManage> dataList = manageDataService.getAllByData(0, DateUtils.parseDate(day), DateUtils.getNextDay(day));



        handleDeptReport(day, dataList);


        handleEfficiencyStatisticsReport(day, dataList);


        Set<Integer> taskIds = new HashSet<>();

        Map<Integer, List<DataManage>> map = new HashMap<>();

        handleDataManage(dataList, taskIds, map, "部门");

        for (Integer taskId : taskIds) {

            Statistics statistics = new Statistics();
            String name = taskDao.getById(taskId).getProjectName();
            int time = 0;
            int working_time = 0;
            BigDecimal ePower = new BigDecimal("0");
            for (DataManage dataManage : map.get(taskId)) {
                time += dataManage.getWorkingTime();
                time += dataManage.getNoloadingTime();
                working_time += dataManage.getWorkingTime();
                ePower = ePower.add(new BigDecimal(dataManage.getNoloadingPower()));
                ePower = ePower.add(new BigDecimal(dataManage.getWorkingPower()));
            }
            statistics.setTime(time);
            statistics.setWorkingTime(working_time);
            statistics.setCreateTime(new Timestamp(new Date().getTime()));
            statistics.setDate(DateUtils.parseDate(day));
            statistics.setName(name);
            statistics.setTaskId(taskId);
            statistics.setEfficiency(String.format("%.2f", (double) working_time / time * 100));
            statistics.setPower(ePower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            statisticsDao.save(statistics);

        }



        List<Integer> ids = workDao.getMachineId(day);

        for (Integer id : ids) {

            List<Work> works = workDao.getWorkByMachineId(id);
            int noloadingTime = 0;
            int workingTime = 0;
            int time = 0;
            double noloadingPower = 0;
            double workingPower = 0;
            double power = 0;
            int counts = 0;
            for (Work work : works) {
                List<DataManage> dataManages = manageDataService.getDataByWork(work.getId(), DateUtils.parseDate(day), DateUtils.getNextDay(day));
                if (!dataManages.isEmpty()) {
                    for (DataManage dataManage : dataManages) {
                        noloadingTime += dataManage.getNoloadingTime();
                        workingTime += dataManage.getWorkingTime();
                        time += dataManage.getWorkingTime();
                        time += dataManage.getNoloadingTime();
                        noloadingPower += dataManage.getNoloadingPower();
                        workingPower += dataManage.getWorkingPower();
                        power += dataManage.getNoloadingPower();
                        power += dataManage.getWorkingPower();
                        counts += Integer.parseInt(dataManage.getRemark());
                    }
                }
            }
            MachineUse machineUse = new MachineUse();
            machineUse.setDepeName(works.get(0).getMachine().getDept().getName());
            machineUse.setMachineId(works.get(0).getMachine().getId());
            machineUse.setNoloadingTime(noloadingTime);
            machineUse.setNoloadingPower(String.format("%.2f", noloadingPower));
            machineUse.setWorkTime(workingTime);
            machineUse.setWorkingPower(String.format("%.2f", workingPower));
            machineUse.setTime(time);
            machineUse.setPower(String.format("%.2f", power));
            machineUse.setOvercounts(String.valueOf(counts));
            machineUse.setRemark(day);
            machineUse.setCreateTime(DateUtils.parseDate(day));
            machineUseDao.save(machineUse);
        }


        List<DataManage> dataList2 = manageDataService.getAllByData(0, DateUtils.parseDate(day), DateUtils.getNextDay(day));


        Map<Integer, List<DataManage>> map2 = new HashMap<>();
        Set<Integer> sets = new HashSet<>();

        for (DataManage dataManage : dataList2) {
            Integer id = dataManage.getWork().getPerson().getId();
            if (map2.get(id) == null) {
                List<DataManage> temp = new ArrayList<>();
                temp.add(dataManage);
                map2.put(id, temp);
            } else {
                map2.get(id).add(dataManage);
            }
            sets.add(id);
        }

        for (Integer set : sets) {
            PersonEfficiency personEfficiency = new PersonEfficiency();

            int time = 0;
            int work_time = 0;
            int noloading_time = 0;
            int overCounts = 0;
            BigDecimal wPower = new BigDecimal("0");
            BigDecimal nPower = new BigDecimal("0");
            for (DataManage dataManage : map2.get(set)) {
                time += dataManage.getNoloadingTime();
                time += dataManage.getWorkingTime();
                work_time += dataManage.getWorkingTime();
                noloading_time = time - work_time;
                wPower = wPower.add(new BigDecimal(dataManage.getWorkingPower()));
                nPower = nPower.add(new BigDecimal(dataManage.getNoloadingPower()));
                overCounts += Integer.parseInt(dataManage.getRemark());

            }
            personEfficiency.setPersonId(set);
            personEfficiency.setName(map2.get(set).get(0).getWork().getPerson().getName());
            personEfficiency.setDeptOne(map2.get(set).get(0).getWork().getPerson().getDept().getName());
            personEfficiency.setWorkingPower(wPower.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            personEfficiency.setNoloadingPower(nPower.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            personEfficiency.setTime(time);
            personEfficiency.setNoloadingTime(noloading_time);
            personEfficiency.setWorkingTime(work_time);
            personEfficiency.setEfficiency(String.format("%.2f", (double) work_time / time * 100));
            personEfficiency.setRemark(String.valueOf(set));
            personEfficiency.setCounts(overCounts);
            personEfficiency.setDate(DateUtils.parseDate(day));
            personEfficiency.setCreateTime(new Timestamp(new Date().getTime()));
            personEfficiencyDao.save(personEfficiency);
        }
    }
}