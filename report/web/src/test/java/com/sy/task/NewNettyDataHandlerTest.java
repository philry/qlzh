//package com.sy.task;
//
//import com.sy.dao.*;
//import com.sy.entity.*;
//import com.sy.service.ManageDataService;
//import com.sy.service.NettyService;
//import com.sy.starter.Starter;
//import com.sy.utils.DateUtils;
//import com.sy.vo.Unit;
//import org.apache.log4j.Logger;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//
//@SpringBootTest(classes = Starter.class)
//@RunWith(SpringRunner.class)
//@Component
//public class NewNettyDataHandlerTest {
//
//    @Autowired
//    private NettyDao nettyDao;
//
//    @Autowired
//    private NettyService nettyService;
//
//    @Autowired
//    private DataManageDao dataManageDao;
//
//    @Autowired
//    private DataManageMapper dataManageMapper;
//
//    @Autowired
//    private ManageDataService manageDataService;
//
//    @Autowired
//    private WorkDao workDao;
//
//    @Autowired
//    private XpgDao xpgDao;
//
//    @Autowired
//    private MachineDao machineDao;
//
//    @Autowired
//    private EngineeringDao engineeringDao;
//
//    @Autowired
//    private PersonDao personDao;
//
//    @Autowired
//    private DeptDao deptDao;
//
//    @Autowired
//    private TaskDao taskDao;
//
//    @Autowired
//    private PersonEfficiencyDao personEfficiencyDao;
//
//    @Autowired
//    private EfficiencyStatisticsDao efficiencyStatisticsDao;
//
//    @Autowired
//    private EfficiencyStatisticsNewDao efficiencyStatisticsNewDao;
//
//    @Autowired
//    private MachineUseDao machineUseDao;
//
//    Logger logger = Logger.getLogger(NewNettyDataHandlerTest.class);
//
//    @Scheduled(cron = "0 30 0 * * ?") // 每天夜晚十二点半处理前天的数据
//    @Transactional
//    public void handleData() {
//
//        logger.info(">>>>>>>>>>>进入0点30定时任务方法");
//        //获取指定日期（前一天）
//        Date now = new Date();
//        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
//        String day = DateUtils.getPrevDay(today);
//        //删除指定日期的输出
//        deleteData(day);
//        deleteEightDaysAgoNettyData();//删除8天前底表数据
//        //插入数据
//        insertData(day);
//    }
//
//
//    //######### 5分钟定时任务拆分 start
//
//    //---->>>>>>>> 中间表dataManage 5分钟定时任务
//    @Scheduled(cron = "0 */5 * * * ?") // 5分钟
//    @Transactional
//    public void handleTodayDataManageData() {
//
//        logger.info(">>>>>>>>>>>进入中间表dataManage 5分钟定时任务方法-开始");
//        long beginTime = System.currentTimeMillis();
//
//        //获取指定日期
//        Date now = new Date();
//        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
//        //删除指定日期的输出
//        dataManageDao.deleteByCreateTime(DateUtils.parseDate(today));
//        //插入数据
////        insertDataManageData(today);
//
//        long endTime = System.currentTimeMillis();
//        logger.info(">>>>>>>>>>>进入中间表dataManage 5分钟定时任务方法-结束。耗时：" + (endTime - beginTime) +"ms");
//    }
//
//    //---->>>>>>>> 工效报表engineering 5分钟定时任务
//    @Scheduled(cron = "0 */5 * * * ?") // 5分钟
//    @Transactional
//    public void handleTodayEngineeringData() {
//
//        logger.info(">>>>>>>>>>>进入工效报表engineering 5分钟定时任务方法-开始");
//        long beginTime = System.currentTimeMillis();
//
//        //获取指定日期
//        Date now = new Date();
//        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
//        //删除指定日期的输出
//        engineeringDao.deleteByDate(DateUtils.parseDate(today));
//        //插入数据
//        insertEngineeringData(today);
//
//        long endTime = System.currentTimeMillis();
//        logger.info(">>>>>>>>>>>进入工效报表engineering 5分钟定时任务方法-结束。耗时：" + (endTime - beginTime) +"ms");
//    }
//
//    //---->>>>>>>> 工程报表efficiency_statistics_new 5分钟定时任务
//    @Scheduled(cron = "0 */5 * * * ?") // 5分钟
//    @Transactional
//    public void handleTodayEfficiencyStatisticsNewData() {
//
//        logger.info(">>>>>>>>>>>进入工程报表efficiency_statistics_new 5分钟定时任务方法-开始");
//        long beginTime = System.currentTimeMillis();
//
//        //获取指定日期
//        Date now = new Date();
//        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
//        //删除指定日期的输出
//        efficiencyStatisticsNewDao.deleteByDate(DateUtils.parseDate(today));
//        //插入数据
//        insertEfficiencyStatisticsNewData(today);
//
//        long endTime = System.currentTimeMillis();
//        logger.info(">>>>>>>>>>>进入工程报表efficiency_statistics_new 5分钟定时任务方法-结束。耗时：" + (endTime - beginTime) +"ms");
//    }
//
//    //---->>>>>>>> 焊机使用报表machineUse 5分钟定时任务
//    @Scheduled(cron = "0 */5 * * * ?") // 5分钟
//    @Transactional
//    public void handleTodayMachineUseData() {
//
//        logger.info(">>>>>>>>>>>进入焊机使用报表machineUse 5分钟定时任务方法-开始");
//        long beginTime = System.currentTimeMillis();
//
//        //获取指定日期
//        Date now = new Date();
//        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
//        //删除指定日期的输出
//        machineUseDao.deleteByRemark(today);
//        //插入数据
//        insertMachineUseData(today);
//
//        long endTime = System.currentTimeMillis();
//        logger.info(">>>>>>>>>>>进入焊机使用报表machineUse 5分钟定时任务方法-结束。耗时：" + (endTime - beginTime) +"ms");
//    }
//
//    //---->>>>>>>> 个人工效报表personEfficiency 5分钟定时任务
//    @Scheduled(cron = "0 */5 * * * ?") // 5分钟
//    @Transactional
//    public void handleTodayPersonEfficiencyData() {
//
//        logger.info(">>>>>>>>>>>进入个人工效报表personEfficiency 5分钟定时任务方法-开始");
//        long beginTime = System.currentTimeMillis();
//
//        //获取指定日期
//        Date now = new Date();
//        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
//        //删除指定日期的输出
//        personEfficiencyDao.deleteByDate(DateUtils.parseDate(today));
//        //插入数据
//        insertPersonEfficiencyData(today);
//
//        long endTime = System.currentTimeMillis();
//        logger.info(">>>>>>>>>>>进入个人工效报表personEfficiency 5分钟定时任务方法-结束。耗时：" + (endTime - beginTime) +"ms");
//    }
//
//    @Test
//    public void insertDataManageData() {
////    private void insertDataManageData(String day) {
//        //获取指定日期区间内netty表中的所有netty数据列表
//        String day = "2020-10-27";
//        List<Netty> nettyList = nettyService.getAllByDate(DateUtils.parseDate(day), DateUtils.getNextDay(day)); //所有底表数据
//
//        if (nettyList.size() <= 0) {
//            System.out.println("无可同步数据");
//            return;
//        }
//
//
//        //新的start2
//        Map<String, List<Netty>> result = new HashMap<String, List<Netty>>();
//        for (Netty netty : nettyList) {
//            String xpg = netty.getXpg();
//            if (xpg == null) {
//                continue;
//            }
//            List<Netty> nettys = result.get(xpg);
//            if (nettys == null) {
//                nettys = new ArrayList<Netty>();
//                result.put(xpg, nettys);
//            }
//            nettys.add(netty);
//        }
//
//        /*//java8流式操作
//        Map<String, List<Netty>> result =
//                nettyList.stream().collect(Collectors.groupingBy(Netty::getXpg));*/
//
//        List<Machine> machineLists = machineDao.findAll();
//        Map<Integer, Machine> machineMap = new HashMap<Integer, Machine>();
//        for (Machine machine : machineLists) {
//            Integer machineId = machine.getId();
//            if (machineId == null) {
//                continue;
//            }
//            machineMap.put(machineId, machine);
//        }
//
//        for (String xpgId : result.keySet()) {
//            if (result.get(xpgId).size() >= 2) {
//                for (int a = 1; a < result.get(xpgId).size(); a++) { //按2G码分组
//                    logger.info(">>>>>>>>>>>进入中间表dataManage 5分钟定时任务方法【单条数据处理】-开始");
//                    long beginTime = System.currentTimeMillis();
//
//                    Netty netty = result.get(xpgId).get(a);
//                    if (netty == null) {
//                        continue;
//                    }
//                    //新的end2
//
//                    //存储处理数据对象
//                    DataManage data = new DataManage();
//                    data.setCreateTime(new Timestamp(DateUtils.parseDate(day).getTime()));//时分秒为00:00:00
//
//                    //将数据库存储的60s电流取出
//                    String currentStr = netty.getCurrents();
//                    List<String> currents = Arrays.asList(currentStr.split(","));
//                    //根据2G码获取最新的扫码工作信息信息
//                    Xpg xpg = xpgDao.getByName(netty.getXpg());
//                    String dataStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, netty.getCreateTime());
//                    Work work = workDao.getLastWorkByTime(dataStr, xpg.getMachineId());
//                    long endTime0 = System.currentTimeMillis();
//                    logger.info(">>>>>>>>>>>进入中间表dataManage 5分钟定时任务方法【单条数据处理】-【到1处】。【单条数据处理到1处】耗时：" + (endTime0 - beginTime) + "ms");
//                    long beginTime0 = System.currentTimeMillis();
//                    if ("1".equals(work.getOperate())) { //底表包对应采集器的最近一次上工记录是关机那就是关机之后其他原因接收到的包,跳过不统计
//                        continue;
//                    }
//                    //获取焊机的电流临界值
//                    //                   Machine machine = machineDao.getById(xpg.getMachineId());
//                    Machine machine = machineMap.get(xpg.getMachineId());
//                    if (machine != null) {
//                        Double maxA = machine.getMaxA();
//                        Double minA = machine.getMinA();
//
//                        int noloadingTime = 0;
//                        int workingTime = 0;
//                        BigDecimal iWorking = new BigDecimal(0);
//                        BigDecimal iNoloading = new BigDecimal(0);
//
//                        //定义警戒次数，若60s内全部高于最大工作电流，则判定为超载（原来的）
//                        int warningCounts = 0;
//
//                        //处理电流数据，得出具体的工作时间（电量），空载时间（电量）
//                        for (String current : currents) {
//                            double i = Double.parseDouble(current);
//                            if (i > maxA) {
//                                warningCounts++; //电流大于最大工作电流警戒次数加1
//                            } else if (i >= minA) {
//                                workingTime++;
//                                iWorking = iWorking.add(new BigDecimal(i));
//                            } else if (i >= 0) {
//                                noloadingTime++;
//                                iNoloading = iNoloading.add(new BigDecimal(i));
//                            }
//                        }
//
//                        //BigDecimal（str1）.subtract（str2）当条netty记录的电量1减去上条netty记录的电量2得到使用的电量
//                        BigDecimal power = new BigDecimal(netty.getPower()).subtract(new BigDecimal(result.get(xpgId).get(a - 1).getPower()));//按2G码分组后减的就是相同2G码的前一条数据
//                        if (power.doubleValue() < 0) { //最新的包电量减去前一个包电量小于0，就是出现了包电量为0的情况，把这个异常的底表数据删除
//                            int id = netty.getId();
//                            nettyDao.deleteById(id);
//                            continue;
//                        }
//
//
//                        BigDecimal iTotal = iWorking.add(iNoloading);
//                        BigDecimal workingPower = null;
//
//                        BigDecimal noloadingPower = new BigDecimal("0");
//
//                        try {
//                            if (0 == iTotal.doubleValue()) {
//                                workingPower = new BigDecimal("0");
//                                ;
//                            } else {
//                                workingPower = power.multiply(iWorking.divide(iTotal, 2, BigDecimal.ROUND_HALF_UP));
//                            }
//                            noloadingPower = power.subtract(workingPower); //noloadingPower= power-workingPower
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            workingPower = power;
//                        }
//
//                        data.setDate(DateUtils.parseDate(day));
//                        data.setWork(work);
//                        data.setNoloadingPower(noloadingPower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//BigDecimal小数点后四舍五入保留2位小数转为Double类型
//                        data.setWorkingPower(workingPower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                        data.setWorkingTime(workingTime);
//                        data.setNoloadingTime(noloadingTime);
//
//                        if (warningCounts == 3) {//定义警戒次数，若60s内有连续3秒大于最大工作电流，则判定为超载
//                            data.setRemark("1");
//                        } else {
//                            data.setRemark("0");
//                        }
//                        dataManageDao.save(data);
//
//                        long endTime = System.currentTimeMillis();
//                        logger.info(">>>>>>>>>>>进入中间表dataManage 5分钟定时任务方法【单条数据处理】-结束。【单条数据处理】耗时：" + (endTime - beginTime) + "ms");
//                    }
//                }
//            }
//        }
//    }
//
//    private void insertEngineeringData(String day) {
//
//        List<DataManage> dataList = manageDataService.getAllByData(0, DateUtils.parseDate(day), DateUtils.getNextDay(day));
//        handleDeptReport(day, dataList);
//    }
//
//    private void insertEfficiencyStatisticsNewData(String day) {
//
//        List<DataManage> dataList = manageDataService.getAllByData(0, DateUtils.parseDate(day), DateUtils.getNextDay(day));
//        handleEfficiencyStatisticsReport(day, dataList);
//    }
//
//    private void insertMachineUseData(String day) {
//
//        List<Integer> ids = workDao.getMachineId(day);
//
//        for (Integer id : ids) {
//
//            List<Work> works = workDao.getWorkByMachineId(id);
//            int noloadingTime = 0;
//            int workingTime = 0;
//            int time = 0;
//            double noloadingPower = 0;
//            double workingPower = 0;
//            double power = 0;
//            int counts = 0;
//            for (Work work : works) {
//                List<DataManage> dataManages = manageDataService.getDataByWork(work.getId(), DateUtils.parseDate(day), DateUtils.getNextDay(day));
//                if (!dataManages.isEmpty()) {
//                    for (DataManage dataManage : dataManages) {
//                        noloadingTime += dataManage.getNoloadingTime();
//                        workingTime += dataManage.getWorkingTime();
//                        time += dataManage.getWorkingTime();
//                        time += dataManage.getNoloadingTime();
//                        noloadingPower += dataManage.getNoloadingPower();
//                        workingPower += dataManage.getWorkingPower();
//                        power += dataManage.getNoloadingPower();
//                        power += dataManage.getWorkingPower();
//                        counts += Integer.parseInt(dataManage.getRemark());
//                    }
//                }
//            }
//            MachineUse machineUse = new MachineUse();
//            machineUse.setDepeName(works.get(0).getMachine().getDept().getName());
//            machineUse.setMachineId(works.get(0).getMachine().getId());
//            machineUse.setNoloadingTime(noloadingTime);
//            machineUse.setNoloadingPower(String.format("%.2f", noloadingPower));
//            machineUse.setWorkTime(workingTime);
//            machineUse.setWorkingPower(String.format("%.2f", workingPower));
//            machineUse.setTime(time);
//            machineUse.setPower(String.format("%.2f", power));
//            machineUse.setOvercounts(String.valueOf(counts));
//            machineUse.setRemark(day);
//            machineUse.setCreateTime(DateUtils.parseDate(day));
//            machineUseDao.save(machineUse);
//        }
//    }
//
//    private void insertPersonEfficiencyData(String day) {
//
//        //个人工效存储
//        //将所有的数据全部存储
//        List<DataManage> dataList2 = manageDataService.getAllByData(0, DateUtils.parseDate(day), DateUtils.getNextDay(day));
//
//        System.out.println("当天日期的数据"+dataList2);
//
//        //分类数据,将数据按照人分类好
//        Map<Integer,List<DataManage>> map2 = new HashMap<>();
//        Set<Integer> sets = new HashSet<>();
//
//        for (DataManage dataManage : dataList2) {
//            Integer id = dataManage.getWork().getPerson().getId();
//            if(map2.get(id)==null){
//                List<DataManage> temp = new ArrayList<>();
//                temp.add(dataManage);
//                map2.put(id,temp);
//            }else {
//                map2.get(id).add(dataManage);
//            }
//            sets.add(id);
//        }
//        //计算处理数据,并入表
//        for (Integer set : sets) { //当天
//            PersonEfficiency personEfficiency = new PersonEfficiency();
//
//            int time = 0 ;
//            int work_time = 0 ;
//            int noloading_time = 0;
//            int overCounts = 0;
//            BigDecimal wPower = new BigDecimal("0");
//            BigDecimal nPower = new BigDecimal("0");
//            for (DataManage dataManage : map2.get(set)) {
//                time += dataManage.getNoloadingTime();
//                time += dataManage.getWorkingTime();
//                work_time += dataManage.getWorkingTime();
//                noloading_time = time - work_time;
//                wPower = wPower.add(new BigDecimal(dataManage.getWorkingPower()));
//                nPower = nPower.add(new BigDecimal(dataManage.getNoloadingPower()));
//                overCounts += Integer.parseInt(dataManage.getRemark());
//
//            }
//            personEfficiency.setPersonId(set);
//            personEfficiency.setName(map2.get(set).get(0).getWork().getPerson().getName());
//            personEfficiency.setDeptOne(map2.get(set).get(0).getWork().getPerson().getDept().getName());
//            personEfficiency.setWorkingPower(wPower.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//            personEfficiency.setNoloadingPower(nPower.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//            personEfficiency.setTime(time);
//            personEfficiency.setNoloadingTime(noloading_time);
//            personEfficiency.setWorkingTime(work_time);
//            personEfficiency.setEfficiency(String.format("%.2f", (double)work_time/time*100));
//            personEfficiency.setRemark(String.valueOf(set));
//            personEfficiency.setCounts(overCounts);
//            personEfficiency.setDate(DateUtils.parseDate(day));
//            personEfficiency.setCreateTime(new Timestamp(new Date().getTime()));
//            personEfficiencyDao.save(personEfficiency);
//        }
//    }
//
//    //#########  5分钟定时任务拆分 end
//
//
//    private void insertData(String day) {
//        //获取指定日期区间内netty表中的所有netty数据列表
//        List<Netty> nettyList = nettyService.getAllByDate(DateUtils.parseDate(day), DateUtils.getNextDay(day)); //所有底表数据
//
//        if (nettyList.size() <= 0) {
//            System.out.println("无可同步数据");
//            return;
//        }
//
//        /*//新增的start
//        //获取指定日期区间内netty表中的所有不重复的xpg列表
//        List<String> xpgLists = nettyService.getAllXpgsByDate(DateUtils.parseDate(day), DateUtils.getNextDay(day));
//        if (xpgLists.size() <= 0) {
//            System.out.println("无可同步数据");
//            return;
//        }
//
//        for (String xpgId : xpgLists) { //按2G码分组
//            //获取指定日期区间内netty表中xpg值=xpgId的所有netty数据列表
//            List<Netty> nettyList1 = nettyService.getAllByDateAndXpgId(xpgId, DateUtils.parseDate(day), DateUtils.getNextDay(day));
//
//            for (int a = 1; a < nettyList1.size(); a++) {
//                Netty netty = nettyList1.get(a);*/
//
//        //新的start2
//        Map<String, List<Netty>> result = new HashMap<String, List<Netty>>();
//        for (Netty netty : nettyList) {
//            String xpg = netty.getXpg();
//            if (xpg == null) {
//                continue;
//            }
//            List<Netty> nettys = result.get(xpg);
//            if (nettys == null) {
//                nettys = new ArrayList<Netty>();
//                result.put(xpg, nettys);
//            }
//            nettys.add(netty);
//        }
//
//        /*//java8流式操作
//            Map<String, List<Netty>> result =
//                    nettyList.stream().collect(Collectors.groupingBy(Netty::getXpg));*/
//
//        List<Machine> machineLists = machineDao.findAll();
//        Map<Integer, Machine> machineMap = new HashMap<Integer, Machine>();
//        for (Machine machine : machineLists) {
//            Integer machineId = machine.getId();
//            if (machineId == null) {
//                continue;
//            }
//            machineMap.put(machineId, machine);
//        }
//
//        for (String xpgId : result.keySet()) {
//            if (result.get(xpgId).size() >= 2) {
//                for (int a = 1; a < result.get(xpgId).size(); a++) { //按2G码分组
//                    Netty netty = result.get(xpgId).get(a);
//                    if (netty == null) {
//                        continue;
//                    }
//                    //新的end2
//
//                    //存储处理数据对象
//                    DataManage data = new DataManage();
//                    data.setCreateTime(new Timestamp(DateUtils.parseDate(day).getTime()));//时分秒为00:00:00
//
//                    //将数据库存储的60s电流取出
//                    String currentStr = netty.getCurrents();
//                    List<String> currents = Arrays.asList(currentStr.split(","));
//                    //根据2G码获取最新的扫码工作信息信息
//                    Xpg xpg = xpgDao.getByName(netty.getXpg());
//                    String dataStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, netty.getCreateTime());
//                    Work work = workDao.getLastWorkByTime(dataStr, xpg.getMachineId());
//                    if ("1".equals(work.getOperate())) { //底表包对应采集器的最近一次上工记录是关机那就是关机之后其他原因接收到的包,跳过不统计
//                        continue;
//                    }
//                    //获取焊机的电流临界值
////                   Machine machine = machineDao.getById(xpg.getMachineId());
//                    Machine machine = machineMap.get(xpg.getMachineId());
//                    if (machine != null) {
//                        Double maxA = machine.getMaxA();
//                        Double minA = machine.getMinA();
//
//                        int noloadingTime = 0;
//                        int workingTime = 0;
//                        BigDecimal iWorking = new BigDecimal(0);
//                        BigDecimal iNoloading = new BigDecimal(0);
//
//                        //定义警戒次数，若60s内全部高于最大工作电流，则判定为超载（原来的）
//                        int warningCounts = 0;
//
//                        //处理电流数据，得出具体的工作时间（电量），空载时间（电量）
//                        for (String current : currents) {
//                            double i = Double.parseDouble(current);
//                            if (i > maxA) {
//                                warningCounts++; //电流大于最大工作电流警戒次数加1
//                            } else if (i >= minA) {
//                                workingTime++;
//                                iWorking = iWorking.add(new BigDecimal(i));
//                            } else if (i >= 0) {
//                                noloadingTime++;
//                                iNoloading = iNoloading.add(new BigDecimal(i));
//                            }
//                        }
//
//                        //BigDecimal（str1）.subtract（str2）当条netty记录的电量1减去上条netty记录的电量2得到使用的电量
////                        BigDecimal power = new BigDecimal(netty.getPower()).subtract(new BigDecimal(nettyList1.get(a - 1).getPower()));//按2G码分组后减的就是相同2G码的前一条数据
//                        BigDecimal power = new BigDecimal(netty.getPower()).subtract(new BigDecimal(result.get(xpgId).get(a - 1).getPower()));//按2G码分组后减的就是相同2G码的前一条数据
//                        if (power.doubleValue() < 0) { //最新的包电量减去前一个包电量小于0，就是出现了包电量为0的情况，把这个异常的底表数据删除
//                            int id = netty.getId();
//                            nettyDao.deleteById(id);
//                            continue;
//                        }
//
//
//                        BigDecimal iTotal = iWorking.add(iNoloading);
//                        BigDecimal workingPower = null;
//
//                        BigDecimal noloadingPower = new BigDecimal("0");
//
//                        try {
//                            if (0 == iTotal.doubleValue()) {
//                                workingPower = new BigDecimal("0");
//                            } else {
//                                workingPower = power.multiply(iWorking.divide(iTotal, 2, BigDecimal.ROUND_HALF_UP));
//                            }
//                            noloadingPower = power.subtract(workingPower); //noloadingPower= power-workingPower
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            workingPower = power;
//                        }
//
//                        data.setDate(DateUtils.parseDate(day));
//                        data.setWork(work);
//                        data.setNoloadingPower(noloadingPower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());//BigDecimal小数点后四舍五入保留2位小数转为Double类型
//                        data.setWorkingPower(workingPower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//                        data.setWorkingTime(workingTime);
//                        data.setNoloadingTime(noloadingTime);
//
//                        if (warningCounts == 3) {//定义警戒次数，若60s内有连续3秒大于最大工作电流，则判定为超载
//                            data.setRemark("1");
//                        } else {
//                            data.setRemark("0");
//                        }
//
//                        dataManageDao.save(data);
//                    }
//                }
//            }
//        }
//        //新增的end
//
//        //对报表数据进行存储
//        //获取指定日期区间内data_manage表中的所有数据列表
//        List<DataManage> dataList = manageDataService.getAllByData(0, DateUtils.parseDate(day), DateUtils.getNextDay(day));
//
//        //开始计算部门报表（以部门作为判定依据）
//
//        handleDeptReport(day, dataList);
//
//        //开始计算工程报表（以派工单作为判定依据，数据放入新工程报表）
//        handleEfficiencyStatisticsReport(day, dataList);//新增的
//
//        //原来的工程报表存储start
//        Set<Integer> taskIds = new HashSet<>();
//
//        Map<Integer, List<DataManage>> map = new HashMap<>();
//
//        handleDataManage(dataList, taskIds, map, "部门");
//
//        for (Integer taskId : taskIds) { //taskIds是所有焊工开机扫码选择的任务id
//
//            EfficiencyStatistics efficiencyStatistics = new EfficiencyStatistics();
//            String name = taskDao.getById(taskId).getProjectName();
//            int time = 0;
//            int working_time = 0;
//            BigDecimal ePower = new BigDecimal("0");
//            for (DataManage dataManage : map.get(taskId)) {
//                time += dataManage.getWorkingTime();
//                time += dataManage.getNoloadingTime();
//                working_time += dataManage.getWorkingTime();
//                ePower = ePower.add(new BigDecimal(dataManage.getNoloadingPower()));
//                ePower = ePower.add(new BigDecimal(dataManage.getWorkingPower()));
//            }
//            efficiencyStatistics.setTime(time);
//            efficiencyStatistics.setWorkingTime(working_time);
//            efficiencyStatistics.setCreateTime(new Timestamp(new Date().getTime()));
//            efficiencyStatistics.setDate(DateUtils.parseDate(day));
//            efficiencyStatistics.setName(name);
//            efficiencyStatistics.setTaskId(taskId); //新加的字段,任务id
//            efficiencyStatistics.setEfficiency(String.format("%.2f", (double) working_time / time * 100));
//            efficiencyStatistics.setPower(ePower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//            efficiencyStatisticsDao.save(efficiencyStatistics); //存储的是焊工一级的数据
//
//        }
//        //原来的工程报表存储end
//
//        //开始计算设备使用报表
//        List<Integer> ids = workDao.getMachineId(day);
//
//        for (Integer id : ids) {
//
//            List<Work> works = workDao.getWorkByMachineId(id);
//            int noloadingTime = 0;
//            int workingTime = 0;
//            int time = 0;
//            double noloadingPower = 0;
//            double workingPower = 0;
//            double power = 0;
//            int counts = 0;
//            for (Work work : works) {
//                List<DataManage> dataManages = manageDataService.getDataByWork(work.getId(), DateUtils.parseDate(day), DateUtils.getNextDay(day));
//                if (!dataManages.isEmpty()) {
//                    for (DataManage dataManage : dataManages) {
//                        noloadingTime += dataManage.getNoloadingTime();
//                        workingTime += dataManage.getWorkingTime();
//                        time += dataManage.getWorkingTime();
//                        time += dataManage.getNoloadingTime();
//                        noloadingPower += dataManage.getNoloadingPower();
//                        workingPower += dataManage.getWorkingPower();
//                        power += dataManage.getNoloadingPower();
//                        power += dataManage.getWorkingPower();
//                        counts += Integer.parseInt(dataManage.getRemark());
//                    }
//                }
//            }
//            MachineUse machineUse = new MachineUse();
//            machineUse.setDepeName(works.get(0).getMachine().getDept().getName());
//            machineUse.setMachineId(works.get(0).getMachine().getId());
//            machineUse.setNoloadingTime(noloadingTime);
//            machineUse.setNoloadingPower(String.format("%.2f", noloadingPower));
//            machineUse.setWorkTime(workingTime);
//            machineUse.setWorkingPower(String.format("%.2f", workingPower));
//            machineUse.setTime(time);
//            machineUse.setPower(String.format("%.2f", power));
//            machineUse.setOvercounts(String.valueOf(counts));
//            machineUse.setRemark(day);
//            machineUse.setCreateTime(DateUtils.parseDate(day));
//            machineUseDao.save(machineUse);
//        }
//
//        //个人工效存储
//        //将所有的数据全部存储
//        List<DataManage> dataList2 = manageDataService.getAllByData(0, DateUtils.parseDate(day), DateUtils.getNextDay(day));
//
//        System.out.println("当天日期的数据"+dataList2);
//
//        //分类数据,将数据按照人分类好
//        Map<Integer,List<DataManage>> map2 = new HashMap<>();
//        Set<Integer> sets = new HashSet<>();
//
//        for (DataManage dataManage : dataList2) {
//            Integer id = dataManage.getWork().getPerson().getId();
//            if(map2.get(id)==null){
//                List<DataManage> temp = new ArrayList<>();
//                temp.add(dataManage);
//                map2.put(id,temp);
//            }else {
//                map2.get(id).add(dataManage);
//            }
//            sets.add(id);
//        }
//        //计算处理数据,并入表
//        for (Integer set : sets) { //当天
//            PersonEfficiency personEfficiency = new PersonEfficiency();
//
//            int time = 0 ;
//            int work_time = 0 ;
//            int noloading_time = 0;
//            int overCounts = 0;
//            BigDecimal wPower = new BigDecimal("0");
//            BigDecimal nPower = new BigDecimal("0");
//            for (DataManage dataManage : map2.get(set)) {
//                time += dataManage.getNoloadingTime();
//                time += dataManage.getWorkingTime();
//                work_time += dataManage.getWorkingTime();
//                noloading_time = time - work_time;
//                wPower = wPower.add(new BigDecimal(dataManage.getWorkingPower()));
//                nPower = nPower.add(new BigDecimal(dataManage.getNoloadingPower()));
//                overCounts += Integer.parseInt(dataManage.getRemark());
//
//            }
//            personEfficiency.setPersonId(set);
//            personEfficiency.setName(map2.get(set).get(0).getWork().getPerson().getName());
//            personEfficiency.setDeptOne(map2.get(set).get(0).getWork().getPerson().getDept().getName());
//            personEfficiency.setWorkingPower(wPower.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//            personEfficiency.setNoloadingPower(nPower.setScale(2,BigDecimal.ROUND_HALF_UP).toString());
//            personEfficiency.setTime(time);
//            personEfficiency.setNoloadingTime(noloading_time);
//            personEfficiency.setWorkingTime(work_time);
//            personEfficiency.setEfficiency(String.format("%.2f", (double)work_time/time*100));
//            personEfficiency.setRemark(String.valueOf(set));
//            personEfficiency.setCounts(overCounts);
//            personEfficiency.setDate(DateUtils.parseDate(day));
//            personEfficiency.setCreateTime(new Timestamp(new Date().getTime()));
//            personEfficiencyDao.save(personEfficiency);
//        }
//
//    }
//
//    private void deleteData(String day) {
//
//        dataManageDao.deleteByCreateTime(DateUtils.parseDate(day));
//        engineeringDao.deleteByDate(DateUtils.parseDate(day));
//        efficiencyStatisticsDao.deleteByDate(DateUtils.parseDate(day));
//        efficiencyStatisticsNewDao.deleteByDate(DateUtils.parseDate(day));
//        machineUseDao.deleteByRemark(day);
//        personEfficiencyDao.deleteByDate(DateUtils.parseDate(day));
//    }
//
//
//    public void deleteEightDaysAgoNettyData(){
//        //删除8天前底表数据
//        LocalDateTime now = LocalDateTime.now();
//        logger.info("------now:"+now+"-------");
//        logger.info("现在时间格式化后：" + now.format(DateTimeFormatter.ofPattern(DateUtils.YYYY_MM_DD_HH_MM_SS)));
//        String nowTime = now.format(DateTimeFormatter.ISO_LOCAL_DATE);
//        LocalDateTime minusTime = now.minusDays(8);//8天前时间
//        String eightDaysAgoTime = minusTime.format(DateTimeFormatter.ISO_LOCAL_DATE);
//        logger.info("现在时间是:"+nowTime);
//        logger.info("8天前时间是:"+eightDaysAgoTime);
//        nettyDao.deleteByDate(DateUtils.parseDate(eightDaysAgoTime));
//    }
//
//    //处理数据，将数据根据personId或者taskId进行存储
//    private void handleDataManage(List<DataManage> dataList, Set<Integer> taskIds, Map<Integer, List<DataManage>> map, String type) {
//        for (DataManage dataManage : dataList) {
//            Integer taskId = dataManage.getWork().getTask().getId();//焊工开机扫码选择的任务id
//            if ("人员".equals(type)) {
//                taskId = dataManage.getWork().getPerson().getId();
//            }
//            taskIds.add(taskId);
//            if (map.get(taskId) == null) {
//                List<DataManage> list = new ArrayList<>();
//                list.add(dataManage);
//                map.put(taskId, list);
//            } else {
//                map.get(taskId).add(dataManage);
//            }
//        }
//    }
//
//    //工效报表 engineering
//    private void handleDeptReport(String day, List<DataManage> dataList) {
//        Map<Integer, List<DataManage>> map = new HashMap<>();
//
//        //1.处理数据,将数据与人员进行绑定
//        Set<Integer> personIds = new HashSet<>();
//
//        handleDataManage(dataList, personIds, map, "人员");
//
//        //2.根据人员处理数据,将数据整合(人员id，处理好的数据)
//        Map<Integer, Unit> unitPerson = new HashMap<>();
//        for (Integer personId : personIds) {
//            Unit unit = new Unit();
//            int time = 0;
//            int working_time = 0;
//            BigDecimal ePower = new BigDecimal("0");
//            for (DataManage dataManage : map.get(personId)) {
//                time += dataManage.getNoloadingTime();
//                time += dataManage.getWorkingTime();
//                working_time += dataManage.getWorkingTime();
//                ePower = ePower.add(new BigDecimal(dataManage.getNoloadingPower()));
//                ePower = ePower.add(new BigDecimal(dataManage.getWorkingPower()));
//            }
//
//            unit.setTime(time);
//            unit.setWorkTime(working_time);
//            unit.setPower(ePower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//
//            unitPerson.put(personId, unit);
//
//        }
//
//        //3.根据人员,获取部门（部门id，处理好的数据）
//        Map<Integer, List<Unit>> unitDept_3 = new HashMap<>();
//        Set<Integer> dept_3 = new HashSet<>();
//        for (Integer personId : personIds) {
//            Person person = personDao.getById(personId);
//            Integer deptId = person.getDept().getId();
//
//            if (unitDept_3.get(deptId) == null) {
//                List<Unit> list = new ArrayList<>();
//                list.add(unitPerson.get(personId));
//                unitDept_3.put(deptId, list);
//            } else {
//                unitDept_3.get(deptId).add(unitPerson.get(personId));
//            }
//
//            dept_3.add(deptId);
//        }
//
//        //3.根据部门id,处理数据
//        Map<Integer, Engineering> engineeringMap_3 = new HashMap<>();
//        for (Integer deptId : dept_3) {
//            int time = 0;
//            int working_time = 0;
//            BigDecimal ePower = new BigDecimal("0");
//            for (Unit unit : unitDept_3.get(deptId)) {
//                time += unit.getTime();
//                working_time += unit.getWorkTime();
//                ePower = ePower.add(new BigDecimal(unit.getPower()));
//            }
//
//            //将部门工效属性赋值（pid需要等上级部门完成后才能插入）
//            Dept dept = deptDao.getById(deptId);
//            Engineering engineering = getEngineering(day, time, working_time, ePower, dept, 4);//班组级别
//            //将处理后的数据存储，进行循环
//            engineeringMap_3.put(deptId, engineering);
//
//        }
//
//        //4.根据部门id获取部门,再获取上级pid
//        Map<Integer, List<Engineering>> engineeringData_2 = new HashMap<>();
//        Set<Integer> dept_2 = new HashSet<>();
//        for (Integer integer : dept_3) {
//            Dept dept = deptDao.getById(integer);
//            if (engineeringData_2.get(dept.getPid()) == null) {
//                List<Engineering> list = new ArrayList<>();
//                list.add(engineeringMap_3.get(integer));
//                engineeringData_2.put(dept.getPid(), list);
//            } else {
//                engineeringData_2.get(dept.getPid()).add(engineeringMap_3.get(integer));
//            }
//
//            dept_2.add(dept.getPid());
//        }
//
//        //处理数据,计算二级数据
//        Map<Integer, Engineering> engineeringMap_2 = new HashMap<>();
//        for (Integer integer : dept_2) {
//            int time = 0;
//            int working_time = 0;
//            BigDecimal ePower = new BigDecimal("0");
//            for (Engineering engineering : engineeringData_2.get(integer)) {
//                time += engineering.getTime();
//                working_time += engineering.getWorkingTime();
//                ePower = ePower.add(new BigDecimal(engineering.getPower()));
//            }
//            //将部门工效属性赋值（pid需要等上级部门完成后才能插入）
//            Dept dept = deptDao.getById(integer);
//            Engineering engineering = getEngineering(day, time, working_time, ePower, dept, 3);//工程队级别
//            //将处理后的数据存储，进行循环
//            engineeringMap_2.put(integer, engineering);
//
//        }
//
//        //5.根据部门id获取部门,再获取上级pid
//        Map<Integer, List<Engineering>> engineeringData_1 = new HashMap<>();
//        Set<Integer> dept_1 = new HashSet<>();
//        for (Integer integer : dept_2) {
//            Dept dept = deptDao.getById(integer);
//            if (engineeringData_1.get(dept.getPid()) == null) {
//                List<Engineering> list = new ArrayList<>();
//                list.add(engineeringMap_2.get(integer));
//                engineeringData_1.put(dept.getPid(), list);
//            } else {
//                engineeringData_1.get(dept.getPid()).add(engineeringMap_2.get(integer));
//            }
//
//            dept_1.add(dept.getPid());
//        }
//
//        //处理数据,计算一级数据
//        Map<Integer, Engineering> engineeringMap_1 = new HashMap<>();
//        for (Integer integer : dept_1) {
//            int time = 0;
//            int working_time = 0;
//            BigDecimal ePower = new BigDecimal("0");
//            for (Engineering engineering : engineeringData_1.get(integer)) {
//                time += engineering.getTime();
//                working_time += engineering.getWorkingTime();
//                ePower = ePower.add(new BigDecimal(engineering.getPower()));
//            }
//            //将部门工效属性赋值（pid需要等上级部门完成后才能插入）
//            Dept dept = deptDao.getById(integer);
//            Engineering engineering = getEngineering(day, time, working_time, ePower, dept, 2);//车间级别
//            //将处理后的数据存储，进行循环
//            engineeringMap_1.put(integer, engineering);
//        }
//
//        //-------------------------------------新增的一级
//        //6.根据部门id获取部门,再获取顶级pid
//        Map<Integer, List<Engineering>> engineeringData_0 = new HashMap<>();
//        Set<Integer> dept_0 = new HashSet<>();
//        for (Integer integer : dept_1) {
//            Dept dept = deptDao.getById(integer);
//            if (engineeringData_0.get(dept.getPid()) == null) {
//                List<Engineering> list = new ArrayList<>();
//                list.add(engineeringMap_1.get(integer));
//                engineeringData_0.put(dept.getPid(), list);
//            } else {
//                engineeringData_0.get(dept.getPid()).add(engineeringMap_1.get(integer));
//            }
//
//            dept_0.add(dept.getPid());
//        }
//
//        //处理数据,计算一级数据
//        Map<Integer, Engineering> engineeringMap_0 = new HashMap<>();
//        for (Integer integer : dept_0) {
//            int time = 0;
//            int working_time = 0;
//            BigDecimal ePower = new BigDecimal("0");
//            for (Engineering engineering : engineeringData_0.get(integer)) {
//                time += engineering.getTime();
//                working_time += engineering.getWorkingTime();
//                ePower = ePower.add(new BigDecimal(engineering.getPower()));
//            }
//            //将部门工效属性赋值（pid需要等上级部门完成后才能插入）
//            Dept dept = deptDao.getById(integer);
//            Engineering engineering = getEngineering(day, time, working_time, ePower, dept, 1);//生产部级别
//            //将处理后的数据存储，进行循环
//            engineeringMap_0.put(integer, engineering);
//        }
//
//        //从上往下开始插入数据，并设置pid
//        for (Integer integer_0 : dept_0) {
//            Engineering engineering_0 = engineeringMap_0.get(integer_0);
//            engineering_0.setPid(0);
//            int pid_1 = engineeringDao.save(engineering_0).getId();//生产部级
//            List<Integer> list_1 = deptDao.getIdByPid(integer_0);
//            for (Integer integer_1 : dept_1) {
//                Engineering engineering_1 = engineeringMap_1.get(integer_1);
//                if (engineering_1 != null) {
//                    engineering_1.setPid(pid_1);
//                    int pid_2 = engineeringDao.save(engineering_1).getId();//车间级
//                    List<Integer> list_2 = deptDao.getIdByPid(integer_1);
//                    for (Integer integer_2 : list_2) {
//                        Engineering engineering_2 = engineeringMap_2.get(integer_2);
//                        if (engineering_2 != null) {
//                            engineering_2.setPid(pid_2);
//                            int pid_3 = engineeringDao.save(engineering_2).getId();//工程队级
//                            List<Integer> list_3 = deptDao.getIdByPid(integer_2);
//                            for (Integer integer_3 : list_3) {
//                                Engineering engineering_3 = engineeringMap_3.get(integer_3);
//                                if (engineering_3 != null) {
//                                    engineering_3.setPid(pid_3);
//                                    engineeringDao.save(engineering_3);//班组级
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    //工程报表efficiency_statistics_new
//    private void handleEfficiencyStatisticsReport(String day, List<DataManage> dataList) {
//        Map<Integer, List<DataManage>> map = new HashMap<>();
//
//        //1.处理数据,将数据与任务进行绑定
//        Set<Integer> taskIds = new HashSet<>();
//
//        handleDataManage(dataList, taskIds, map, "部门");
//
//        //2.根据任务处理数据,将数据整合(任务id，处理好的数据)
//        Map<Integer, Unit> unitTask = new HashMap<>();
//        for (Integer taskId : taskIds) {//taskIds是所有焊工开机扫码选择的任务id
//            Unit unit = new Unit();
//            int time = 0;
//            int working_time = 0;
//            BigDecimal ePower = new BigDecimal("0");
//            for (DataManage dataManage : map.get(taskId)) {
//                time += dataManage.getNoloadingTime();
//                time += dataManage.getWorkingTime();
//                working_time += dataManage.getWorkingTime();
//                ePower = ePower.add(new BigDecimal(dataManage.getNoloadingPower()));
//                ePower = ePower.add(new BigDecimal(dataManage.getWorkingPower()));
//            }
//
//            unit.setTime(time);
//            unit.setWorkTime(working_time);
//            unit.setPower(ePower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//
//            unitTask.put(taskId, unit);
//        }
//
//        //3.根据任务,获取分派到班组级级的任务（任务id，处理好的数据）
//        Map<Integer, List<Unit>> unitTask_3 = new HashMap<>();
//        Set<Integer> task_3 = new HashSet<>();
//        for (Integer taskId : taskIds) { //taskIds是所有上过工的焊工级别的任务
//
//            Integer banzuTaskId = taskDao.getPidById(taskId);//班组级别任务id
//
//            if (unitTask_3.get(banzuTaskId) == null) {
//                List<Unit> list = new ArrayList<>();
//                list.add(unitTask.get(taskId));
//                unitTask_3.put(banzuTaskId, list);
//            } else {
//                unitTask_3.get(banzuTaskId).add(unitTask.get(taskId));
//            }
//
//            task_3.add(banzuTaskId);//班组级别任务id集合
//        }
//
//        //3.根据任务id,处理数据
//        Map<Integer, EfficiencyStatisticsNew> efficiencyStatisticsMap_3 = new HashMap<>();
//        for (Integer taskId : task_3) {
//            int time = 0;
//            int working_time = 0;
//            BigDecimal ePower = new BigDecimal("0");
//            for (Unit unit : unitTask_3.get(taskId)) {
//                time += unit.getTime();
//                working_time += unit.getWorkTime();
//                ePower = ePower.add(new BigDecimal(unit.getPower()));
//            }
//
//            //将分派到班组级别任务的工程报表属性赋值（pid需要等上级任务完成后才能插入）
//            Task task = taskDao.getById(taskId);
//            EfficiencyStatisticsNew efficiencyStatistics = getEfficiencyStatisticsNew(day, time, working_time, ePower, task, 4);//分派到班组级别的任务
//            //将处理后的数据存储，进行循环
//            efficiencyStatisticsMap_3.put(taskId, efficiencyStatistics);
//        }
//
//        //4.根据任务id获取任务,再获取上级pid
//        Map<Integer, List<EfficiencyStatisticsNew>> efficiencyStatisticsData_2 = new HashMap<>();
//        Set<Integer> task_2 = new HashSet<>();
//        for (Integer integer : task_3) {
//            Task task = taskDao.getById(integer);
//            if (efficiencyStatisticsData_2.get(task.getPid()) == null) {
//                List<EfficiencyStatisticsNew> list = new ArrayList<>();
//                list.add(efficiencyStatisticsMap_3.get(integer));
//                efficiencyStatisticsData_2.put(task.getPid(), list);
//            } else {
//                efficiencyStatisticsData_2.get(task.getPid()).add(efficiencyStatisticsMap_3.get(integer));
//            }
//
//            task_2.add(task.getPid());
//        }
//
//        //处理数据,计算二级数据
//        Map<Integer, EfficiencyStatisticsNew> efficiencyStatisticsMap_2 = new HashMap<>();
//        for (Integer integer : task_2) {
//            int time = 0;
//            int working_time = 0;
//            BigDecimal ePower = new BigDecimal("0");
//            for (EfficiencyStatisticsNew efficiencyStatistics : efficiencyStatisticsData_2.get(integer)) {
//                time += efficiencyStatistics.getTime();
//                working_time += efficiencyStatistics.getWorkingTime();
//                ePower = ePower.add(new BigDecimal(efficiencyStatistics.getPower()));
//            }
//
//            //将分派到工程队级别任务的工程报表属性赋值（pid需要等上级部门完成后才能插入）
//            Task task = taskDao.getById(integer);
//            EfficiencyStatisticsNew efficiencyStatistics = getEfficiencyStatisticsNew(day, time, working_time, ePower, task, 3);//分派到工程队级别的任务
//
//            //将处理后的数据存储，进行循环
//            efficiencyStatisticsMap_2.put(integer, efficiencyStatistics);
//        }
//
//        //5.根据任务id获取任务,再获取上级pid
//        Map<Integer, List<EfficiencyStatisticsNew>> efficiencyStatisticsData_1 = new HashMap<>();
//        Set<Integer> task_1 = new HashSet<>();
//        for (Integer integer : task_2) {
//            Task task = taskDao.getById(integer);
//            if (efficiencyStatisticsData_1.get(task.getPid()) == null) {
//                List<EfficiencyStatisticsNew> list = new ArrayList<>();
//                list.add(efficiencyStatisticsMap_2.get(integer));
//                efficiencyStatisticsData_1.put(task.getPid(), list);
//            } else {
//                efficiencyStatisticsData_1.get(task.getPid()).add(efficiencyStatisticsMap_2.get(integer));
//            }
//
//            task_1.add(task.getPid());
//        }
//
//        //处理数据,计算一级数据
//        Map<Integer, EfficiencyStatisticsNew> efficiencyStatisticsMap_1 = new HashMap<>();
//        for (Integer integer : task_1) {
//            int time = 0;
//            int working_time = 0;
//            BigDecimal ePower = new BigDecimal("0");
//            for (EfficiencyStatisticsNew efficiencyStatistics : efficiencyStatisticsData_1.get(integer)) {
//                time += efficiencyStatistics.getTime();
//                working_time += efficiencyStatistics.getWorkingTime();
//                ePower = ePower.add(new BigDecimal(efficiencyStatistics.getPower()));
//            }
//
//            //将分派到车间级别任务的工程报表属性赋值（pid需要等上级部门完成后才能插入）
//            Task task = taskDao.getById(integer);
//            EfficiencyStatisticsNew efficiencyStatistics = getEfficiencyStatisticsNew(day, time, working_time, ePower, task, 2);//分派到车间级别的任务
//
//            //将处理后的数据存储，进行循环
//            efficiencyStatisticsMap_1.put(integer, efficiencyStatistics);
//        }
//
//
//        //6.根据部门id获取部门,再获取顶级pid
//        Map<Integer, List<EfficiencyStatisticsNew>> efficiencyStatisticsData_0 = new HashMap<>();
//        Set<Integer> task_0 = new HashSet<>();
//        for (Integer integer : task_1) {
//            Task task = taskDao.getById(integer);
//            if (efficiencyStatisticsData_0.get(task.getPid()) == null) {
//                List<EfficiencyStatisticsNew> list = new ArrayList<>();
//                list.add(efficiencyStatisticsMap_1.get(integer));
//                efficiencyStatisticsData_0.put(task.getPid(), list);
//            } else {
//                efficiencyStatisticsData_0.get(task.getPid()).add(efficiencyStatisticsMap_1.get(integer));
//            }
//
//            task_0.add(task.getPid());
//        }
//
//        //处理数据,计算一级数据
//        Map<Integer, EfficiencyStatisticsNew> efficiencyStatisticsMap_0 = new HashMap<>();
//        for (Integer integer : task_0) {
//            int time = 0;
//            int working_time = 0;
//            BigDecimal ePower = new BigDecimal("0");
//            for (EfficiencyStatisticsNew efficiencyStatistics : efficiencyStatisticsData_0.get(integer)) {
//                time += efficiencyStatistics.getTime();
//                working_time += efficiencyStatistics.getWorkingTime();
//                ePower = ePower.add(new BigDecimal(efficiencyStatistics.getPower()));
//            }
//
//            //将分派到生产部级别任务的工程报表属性赋值（pid需要等上级部门完成后才能插入）
//            Task task = taskDao.getById(integer);
//            EfficiencyStatisticsNew efficiencyStatistics = getEfficiencyStatisticsNew(day, time, working_time, ePower, task, 1);//生产部级别的任务
//
//            //将处理后的数据存储，进行循环
//            efficiencyStatisticsMap_0.put(integer, efficiencyStatistics);
//        }
//
//        //从上往下开始插入数据，并设置pid
//        for (Integer integer_0 : task_0) {
//            EfficiencyStatisticsNew efficiencyStatistics_0 = efficiencyStatisticsMap_0.get(integer_0);
//            efficiencyStatistics_0.setPid(0);
//            int pid_1 = efficiencyStatisticsNewDao.save(efficiencyStatistics_0).getId();//生产部级任务
//            List<Integer> list_1 = taskDao.getIdsByPid(integer_0);
//            for (Integer integer_1 : list_1) {
//                EfficiencyStatisticsNew efficiencyStatistics_1 = efficiencyStatisticsMap_1.get(integer_1);
//                if (efficiencyStatistics_1 != null) {
//                    efficiencyStatistics_1.setPid(pid_1);
//                    int pid_2 = efficiencyStatisticsNewDao.save(efficiencyStatistics_1).getId();//车间级任务
//                    List<Integer> list_2 = taskDao.getIdsByPid(integer_1);
//                    for (Integer integer_2 : list_2) {
//                        EfficiencyStatisticsNew efficiencyStatistics_2 = efficiencyStatisticsMap_2.get(integer_2);
//                        if (efficiencyStatistics_2 != null) {
//                            efficiencyStatistics_2.setPid(pid_2);
//                            int pid_3 = efficiencyStatisticsNewDao.save(efficiencyStatistics_2).getId();//工程队级任务
//                            List<Integer> list_3 = taskDao.getIdsByPid(integer_2);
//                            for (Integer integer_3 : list_3) {
//                                EfficiencyStatisticsNew efficiencyStatistics_3 = efficiencyStatisticsMap_3.get(integer_3);
//                                if (efficiencyStatistics_3 != null) {
//                                    efficiencyStatistics_3.setPid(pid_3);
//                                    efficiencyStatisticsNewDao.save(efficiencyStatistics_3);//班组级任务
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private Engineering getEngineering(String day, int time, int working_time, BigDecimal ePower, Dept dept, int level) {
//        Engineering engineering = new Engineering();
//        engineering.setLevel(level);
//        engineering.setDate(DateUtils.parseDate(day));
//        engineering.setCreateTime(new Timestamp(new Date().getTime()));
//        engineering.setName(dept.getName());
//        engineering.setDeptId(dept.getId());
//        engineering.setTime(time);
//        engineering.setWorkingTime(working_time);
//        engineering.setPower(ePower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//        engineering.setEfficency(String.format("%.2f", (double) working_time / time * 100));
//        return engineering;
//    }
//
//    private EfficiencyStatisticsNew getEfficiencyStatisticsNew(String day, int time, int working_time, BigDecimal ePower, Task task, int level) {
//        EfficiencyStatisticsNew efficiencyStatistics = new EfficiencyStatisticsNew();
//        efficiencyStatistics.setLevel(level);
//        efficiencyStatistics.setDate(DateUtils.parseDate(day));
//        efficiencyStatistics.setCreateTime(new Timestamp(new Date().getTime()));
//        efficiencyStatistics.setName(task.getProjectName());
//        efficiencyStatistics.setTaskId(task.getId());
//        efficiencyStatistics.setTime(time);
//        efficiencyStatistics.setWorkingTime(working_time);
//        efficiencyStatistics.setPower(ePower.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//        efficiencyStatistics.setEfficiency(String.format("%.2f", (double) working_time / time * 100));
//        return efficiencyStatistics;
//    }
//}
