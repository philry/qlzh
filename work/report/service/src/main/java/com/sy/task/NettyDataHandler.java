package com.sy.task;

import com.sy.dao.*;
import com.sy.entity.*;
import com.sy.service.ManageDataService;
import com.sy.service.NettyService;
import com.sy.utils.DateUtils;
import com.sy.vo.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;

@Component
public class NettyDataHandler {

    @Autowired
    private NettyDao nettyDao;

    @Autowired
    private NettyService nettyService;

    @Autowired
    private DataManageDao dataManageDao;

    @Autowired
    private ManageDataService manageDataService;

    @Autowired
    private WorkDao workDao;

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
    private EfficiencyStatisticsDao efficiencyStatisticsDao;


    @Scheduled(cron = "0 30 0 * * ?") // 每天十二点半处理前天的数据
//    @Scheduled(fixedRate = 30 * 60 * 1000)
    @Transactional
    public void handleData(){


        //获取指定日期（前一天）
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);
        //删除指定日期的输出
        deleteDate(day);
        //插入数据
        insertData(day);
    }

    @Scheduled(cron = "0 */5 * * * ?") // 5分钟
    @Transactional
    public void handleTodayData(){


        //获取指定日期
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        //删除指定日期的输出
        deleteDate(today);
        //插入数据
        insertData(today);
    }

    private void insertData(String day) {
        //获取指定日期区间内的同步数据
        List<Netty> nettyList =  nettyService.getAllByDate(DateUtils.parseDate(day),DateUtils.getNextDay(day));

        if(nettyList.size()<=0){
            System.out.println("无可同步数据");
            return;
        }

        for (int a = 1; a < nettyList.size(); a++) {
            Netty netty = nettyList.get(a);
            //存储处理数据对象
            DataManage data = new DataManage();
            data.setCreateTime(new Timestamp(DateUtils.parseDate(day).getTime()));


            //将数据库存储的60s电流取出
            String currentStr = netty.getCurrents();
            List<String> currents = Arrays.asList(currentStr.split(","));
            //根据2G码获取最新的扫码工作信息信息
            Xpg xpg = xpgDao.getByName(netty.getXpg());
            Work work = workDao.getLastWorkByTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,netty.getCreateTime()),xpg.getMachineId());
            //获取焊机的电流临界值
            Machine machine = machineDao.getById(xpg.getMachineId());
            Double maxA = machine.getMaxA();
            Double minA = machine.getMinA();

            int noloadingTime=0;
            int workingTime=0;
            BigDecimal iWorking = new BigDecimal(0);
            BigDecimal iNoloading = new BigDecimal(0);

            //定义境界次数，若60s内全部高于最大工作电流，则判定为超载
            int warningCounts = 0;

            //处理电流数据，得出具体的工作时间（电量），空载时间（电量）
            for (String current : currents) {
                double i = Double.parseDouble(current);
                if(i>maxA){
                    warningCounts++;
                }else if(i>=minA){
                    workingTime++;
                    iWorking = iWorking.add(new BigDecimal(i));
                }else if(i>0){
                    noloadingTime++;
                    iNoloading = iNoloading.add(new BigDecimal(i));
                }
            }


            BigDecimal power = new BigDecimal(netty.getPower()).subtract(new BigDecimal(nettyList.get(a-1).getPower()));

            BigDecimal iTotal = iWorking.add(iNoloading);

            BigDecimal workingPower = power.multiply(iWorking.divide(iTotal));

            BigDecimal noloadingPower = power.subtract(workingPower);

            data.setWork(work);
            data.setNoloadingPower(noloadingPower.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            data.setWorkingPower(workingPower.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            data.setWorkingTime(workingTime);
            data.setNoloadingTime(noloadingTime);

            if(warningCounts==60){
                data.setRemark("1");
            }else {
                data.setRemark("0");
            }

            dataManageDao.save(data);
        }


        //对报表数据进行存储
        List<DataManage> dataList = manageDataService.getAllByData(0,DateUtils.parseDate(day),DateUtils.getNextDay(day));

        //开始计算部门报表（以部门作为判定依据）

        handleDeptReport(day, dataList);

        //开始计算工程报表（以派工单作为判定依据）

        Set<Integer> taskIds = new HashSet<>();

        Map<Integer,List<DataManage>> map = new HashMap<>();

        handleDataManage(dataList, taskIds, map,"部门");


        for (Integer taskId : taskIds) {

            EfficiencyStatistics efficiencyStatistics = new EfficiencyStatistics();
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
            efficiencyStatistics.setTime(time);
            efficiencyStatistics.setWorkingTime(working_time);
            efficiencyStatistics.setCreateTime(new Timestamp(new Date().getTime()));
            efficiencyStatistics.setDate(DateUtils.parseDate(day));
            efficiencyStatistics.setName(name);
            efficiencyStatistics.setEfficiency(String.format("%.2f", (double)working_time/time*100));
            efficiencyStatistics.setPower(ePower.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            efficiencyStatisticsDao.save(efficiencyStatistics);

        }


    }

    private void deleteDate(String day){

        dataManageDao.deleteByCreateTime(DateUtils.parseDate(day));
        engineeringDao.deleteByDate(DateUtils.parseDate(day));
        efficiencyStatisticsDao.deleteByDate(DateUtils.parseDate(day));

    }


    //处理数据，将数据根据personId或者taskId进行存储
    private void handleDataManage(List<DataManage> dataList, Set<Integer> taskIds, Map<Integer, List<DataManage>> map,String type) {
        for (DataManage dataManage : dataList) {
            Integer taskId = dataManage.getWork().getTask().getId();
            if("人员".equals(type)){
                taskId = dataManage.getWork().getPerson().getId();
            }
            taskIds.add(taskId);
            if(map.get(taskId)==null){
                List<DataManage> list = new ArrayList<>();
                list.add(dataManage);
                map.put(taskId,list);
            }else {
                map.get(taskId).add(dataManage);
            }
        }
    }

    private void handleDeptReport(String day, List<DataManage> dataList) {
        Map<Integer,List<DataManage>> map = new HashMap<>();

        //1.处理数据,将数据与人员进行绑定
        Set<Integer> personIds = new HashSet<>();

        handleDataManage(dataList, personIds, map,"人员");

        //2.根据人员处理数据,将数据整合(人员id，处理好的数据)
        Map<Integer, Unit> unitPerson = new HashMap<>();
        for (Integer personId : personIds) {
            Unit unit = new Unit();
            int time = 0 ;
            int working_time = 0 ;
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
            unit.setPower(ePower.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());

            unitPerson.put(personId,unit);

        }

        //3.根据人员,获取部门（部门id，处理好的数据）
        Map<Integer,List<Unit>> unitDept_3 = new HashMap<>();
        Set<Integer> dept_3 = new HashSet<>();
        for (Integer personId : personIds) {
            Person person = personDao.getById(personId);
            Integer deptId = person.getDept().getId();

            if(unitDept_3.get(deptId)==null){
                List<Unit> list = new ArrayList<>();
                list.add(unitPerson.get(personId));
                unitDept_3.put(deptId,list);
            }else {
                unitDept_3.get(deptId).add(unitPerson.get(personId));
            }

            dept_3.add(deptId);
        }

        //3.根据部门id,处理数据
        Map<Integer, Engineering> engineeringMap_3 = new HashMap<>();
        for (Integer deptId : dept_3) {
            int time = 0 ;
            int working_time = 0 ;
            BigDecimal ePower = new BigDecimal("0");
            for (Unit unit : unitDept_3.get(deptId)) {
                time += unit.getTime();
                working_time += unit.getWorkTime();
                ePower = ePower.add(new BigDecimal(unit.getPower()));
            }

            //将部门工效属性赋值（pid需要等上级部门完成后才能插入）
            Dept dept = deptDao.getById(deptId);
            Engineering engineering = getEngineering(day, time, working_time, ePower, dept,3);
            //将处理后的数据存储，进行循环
            engineeringMap_3.put(deptId,engineering);

        }

        //4.根据部门id获取部门,再获取上级pid
        Map<Integer,List<Engineering>> engineeringData_2 = new HashMap<>();
        Set<Integer> dept_2 = new HashSet<>();
        for (Integer integer : dept_3) {
            Dept dept = deptDao.getById(integer);
            if(engineeringData_2.get(dept.getPid())==null){
                List<Engineering> list = new ArrayList<>();
                list.add(engineeringMap_3.get(integer));
                engineeringData_2.put(dept.getPid(),list);
            }else {
                engineeringData_2.get(dept.getPid()).add(engineeringMap_3.get(integer));
            }

            dept_2.add(dept.getPid());
        }

        //处理数据,计算二级数据
        Map<Integer,Engineering> engineeringMap_2 = new HashMap<>();
        for (Integer integer : dept_2) {
            int time = 0 ;
            int working_time = 0 ;
            BigDecimal ePower = new BigDecimal("0");
            for (Engineering engineering : engineeringData_2.get(integer)) {
                time += engineering.getTime();
                working_time += engineering.getWorkingTime();
                ePower = ePower.add(new BigDecimal(engineering.getPower()));
            }
            //将部门工效属性赋值（pid需要等上级部门完成后才能插入）
            Dept dept = deptDao.getById(integer);
            Engineering engineering = getEngineering(day, time, working_time, ePower, dept,2);
            //将处理后的数据存储，进行循环
            engineeringMap_2.put(integer,engineering);

        }

        //5.根据部门id获取部门,再获取顶级pid
        Map<Integer,List<Engineering>> engineeringData_1 = new HashMap<>();
        Set<Integer> dept_1 = new HashSet<>();
        for (Integer integer : dept_2) {
            Dept dept = deptDao.getById(integer);
            if(engineeringData_1.get(dept.getPid())==null){
                List<Engineering> list = new ArrayList<>();
                list.add(engineeringMap_2.get(integer));
                engineeringData_1.put(dept.getPid(),list);
            }else {
                engineeringData_1.get(dept.getPid()).add(engineeringMap_2.get(integer));
            }

            dept_1.add(dept.getPid());
        }

        //处理数据,计算一级数据
        Map<Integer,Engineering> engineeringMap_1 = new HashMap<>();
        for (Integer integer : dept_1) {
            int time = 0 ;
            int working_time = 0 ;
            BigDecimal ePower = new BigDecimal("0");
            for (Engineering engineering : engineeringData_1.get(integer)) {
                time += engineering.getTime();
                working_time += engineering.getWorkingTime();
                ePower = ePower.add(new BigDecimal(engineering.getPower()));
            }
            //将部门工效属性赋值（pid需要等上级部门完成后才能插入）
            Dept dept = deptDao.getById(integer);
            Engineering engineering = getEngineering(day, time, working_time, ePower, dept,1);
            //将处理后的数据存储，进行循环
            engineeringMap_1.put(integer,engineering);
        }

        //从上往下开始插入数据，并设置pid
        for (Integer integer_1 : dept_1) {
            Engineering engineering_1 = engineeringMap_1.get(integer_1);
            engineering_1.setPid(0);
            int pid_2 = engineeringDao.save(engineering_1).getId();
            System.out.println(pid_2);
            List<Integer> list_2 = deptDao.getIdByPid(integer_1);
            System.out.println(list_2);
            System.out.println(111);
            for (Integer integer_2 : list_2) {
                Engineering engineering_2 = engineeringMap_2.get(integer_2);
                if (engineering_2!=null){
                    engineering_2.setPid(pid_2);
                    int pid_3 = engineeringDao.save(engineering_2).getId();
                    List<Integer> list_3 = deptDao.getIdByPid(integer_2);
                    for (Integer integer_3 : list_3) {
                        Engineering engineering_3 = engineeringMap_3.get(integer_3);
                        if(engineering_3!=null){
                            engineering_3.setPid(pid_3);
                            engineeringDao.save(engineering_3);
                        }
                    }
                }
            }
        }
    }

    private Engineering getEngineering(String day, int time, int working_time, BigDecimal ePower, Dept dept,int level) {
        Engineering engineering = new Engineering();
        engineering.setLevel(level);
        engineering.setDate(DateUtils.parseDate(day));
        engineering.setCreateTime(new Timestamp(new Date().getTime()));
        engineering.setName(dept.getName());
        engineering.setTime(time);
        engineering.setWorkingTime(working_time);
        engineering.setPower(ePower.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
        engineering.setEfficency(String.format("%.2f", (double)working_time/time*100));
        return engineering;
    }

}
