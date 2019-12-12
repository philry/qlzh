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


    @Scheduled(cron = "0 0/3 * * * ? ") // 每隔5分钟进行统计，将当天的数据进行处理核算
    @Transactional
    public void handleData(){

        //获取指定日期（前一天）
        Date now = new Date();
        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
        String day = DateUtils.getPrevDay(today);
        insertData(day);


    }

    private void insertData(String day) {
        //获取指定日期区间内的同步数据
        List<Netty> nettyList =  nettyService.getAllByDate(DateUtils.parseDate(day),DateUtils.getNextDay(day));

        for (Netty netty : nettyList) {

            //存储处理数据对象
            DataManage data = new DataManage();
            data.setCreateTime(new Timestamp(DateUtils.parseDate(day).getTime()));


            //将数据库存储的60s电流取出
            String currentStr = netty.getCurrents();
            List<String> currents = Arrays.asList(currentStr.split(","));

            //根据2G码获取最新的扫码工作信息信息
            Xpg xpg = xpgDao.getByName(netty.getXpg());

            Work work = workDao.getLastWorkByTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,netty.getCreateTime()),xpg.getMachineId());

            //获取焊机的电流临界值
            Machine machine = machineDao.getById(xpg.getMachineId());
            Double maxA = machine.getMaxA();
            Double minA = machine.getMinA();

            int noloadingTime=0;
            int workingTime=0;
            BigDecimal iWorking = new BigDecimal(0);
            BigDecimal iNoloading = new BigDecimal(0);

            //定义境界次数，若60s内全部高于最大工作电流，则判定为超载，需要自动关闭焊机
            int warningCounts = 0;

            //处理电流数据，得出具体的工作时间（电量），空载时间（电量）
            for (String current : currents) {
                BigDecimal ib = new BigDecimal(current);
                double i = ib.doubleValue();
                if(i>maxA){
                    warningCounts++;
                }else if(i>=minA){
                    workingTime++;
                    iWorking.add(ib);
                }else if(i>0){
                    noloadingTime++;
                    iNoloading.add(ib);
                }
            }

            //TODO  关闭焊机操作
            if(warningCounts==60){

            }

            BigDecimal power = new BigDecimal(netty.getPower());

            BigDecimal iTotal = iWorking.add(iNoloading);

            BigDecimal workingPower = power.multiply(iWorking.divide(iTotal));

            BigDecimal noloadingPower = power.subtract(workingPower);

            data.setWork(work);
            data.setNoloadingPower(noloadingPower.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            data.setWorkingPower(workingPower.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            data.setWorkingTime(workingTime);
            data.setNoloadingTime(noloadingTime);

            dataManageDao.save(data);

            //对报表数据进行存储
            List<DataManage> dataList = manageDataService.getAllByData(DateUtils.parseDate(day),DateUtils.getNextDay(day));

            Map<Integer,List<DataManage>> map = new HashMap<>();

            //1.处理数据,将数据与人员进行绑定
            Set<Integer> personIds = new HashSet<>();

            for (DataManage dataManage : dataList) {
                Integer personId = dataManage.getWork().getPerson().getId();
                personIds.add(personId);
                if(map.get(personId)==null){
                    List<DataManage> list = new ArrayList<>();
                    list.add(dataManage);
                    map.put(personId,list);
                }else {
                    map.get(personId).add(dataManage);
                }
            }

            Map<Integer, Unit> units = new HashMap<>();

            //2.根据人员处理数据，将数据整合
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

                units.put(personId,unit);

            }

            //3.根据人员，获取部门




        }
    }

}
