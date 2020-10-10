package com.sy.quartz;

import com.sy.core.netty.tcp.NettyServerHandler;
import com.sy.dao.*;
import com.sy.entity.*;
import com.sy.service.MessageDataService;
import com.sy.service.WorkService;
import com.sy.starter.Starter;
import com.sy.utils.DateUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 * 每分钟检测开关机情况并自动纠错
 */
@Component
public class OnAndOffCheckQuartz extends QuartzJobBean {

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Autowired
    private NettyMapper nettyMapper;

    @Autowired
    private EnergyMapper energyMapper;

    @Autowired
    private XpgMapper xpgMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private MessageDataService messageDataService;

    @Autowired
    private MachineNowMapper machineNowMapper;

    @Autowired
    private MachineNowDao machineNowDao;

    @Autowired
    private  MachineDao machineDao;

    @Autowired
    private WorkService workService;

    @Autowired
    private WorkDao workDao;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private WorkTypeMapper workTypeMapper;

    Logger logger = Logger.getLogger(OnAndOffCheckQuartz.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info(new Date()+"---------开始运行OnAndOffCheckQuartz的开机失败检测了-------");
        //开机失败检测,machinenow表中有,netty表没有包
        List<MachineNow> list = machineNowDao.findAll();
        if (list != null && list.size() > 0) {//开机失败检测
            Xpg xpg = null;
            Netty last = null;
            Netty newLast = null;
            for (MachineNow machineNow : list) {
                Integer machineId = machineNow.getMachine().getId();
                Machine machine = machineDao.getById(machineId);
                // 获取xpg信息
                xpg = xpgMapper.selectXpgByMachineId(machineNow.getMachine().getId());
//                Integer personId = machineNow.getPerson().getId();
                Date openTime = workService.getLastOpenTimeByMachine(machineId);//该焊机的最近开机时间
//                Integer taskId = workService.selectTaskIdByPersonAndMachine(personId,machineId);
                Date thisTimeRollTwoMinute = new Date(new Date().getTime() - 2 * 60 * 1000);//前两分钟
                last = nettyMapper.getLastNettyByXpgAndOpenTime(xpg.getName(),openTime);//该焊机最近一次开机后的最新数据包
//                newLast = nettyMapper.getLastNettyByXpgAndOpenTime(xpg.getName(),thisTimeRollTwoMinute);//之前两分钟后该焊机的最新数据包
                Date now = new Date();
                long jgtime = now.getTime()-openTime.getTime();//现在距这台焊机开机时间的间隔
                if(jgtime <= 1*60*1000) { //距这台焊机开机时间间隔小于1分钟先跳过这台焊机的判断
                    continue;
                }else { //距这台焊机开机时间间隔大于1分钟
                    if(last == null) { //距这台焊机开机时间间隔大于1分钟后没包过来，那就是开机失败了
                        try {
                            nettyServerHandler.controlMachine(machine.getXpg().getName(),true);//再开一次机
                            logger.info("[********]---------xpg:"+xpg.getName()+",-------焊机:"+machine.getName()+"再次开机------------");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else{
                        logger.info("[********]---------xpg:"+xpg.getName()+",-------焊机:"+machine.getName()+"再次开机成功------------");
                    }
                }
            }
        }

        logger.info(new Date()+"---------开始运行OnAndOffCheckQuartz的关机失败检测了-------");
        //关机失败检测,work表状态是1(关机),netty表还有包
//        String today = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, now);
//        Timestamp timestamp = new Timestamp(DateUtils.parseDate(today).getTime());
        Date thisTimeRollTwoMinute = new Date(new Date().getTime() - 120 * 60 * 1000);//2分钟之前的时间
        Timestamp thisTimeRollTwoMinuteStamp = new Timestamp(thisTimeRollTwoMinute.getTime());
        String time = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, thisTimeRollTwoMinuteStamp);
//        List<Work> works = workDao.getCloseWorkByCreateTime(thisTimeRollTwoMinute);//2分钟之内所有的关机记录
        List<Work> works = workDao.getCloseWorkByCreateTime1(time);//2分钟之内所有的关机记录
//        List<Work> works = workMapper.getCloseWorkByCreateTime1(time);
        if(works != null && works.size() > 0){
            logger.info(new Date()+"---------运行到 works != null && works.size() > 0 了-------");
            for(Work work : works){
                logger.info(new Date()+"---------运行到works遍历了-------");
                int machineId = work.getMachine().getId();
                Date closeTime = workDao.getLastCloseTimeByMachine(machineId);//该焊机的最近关机时间
                // 获取xpg信息
                Xpg xpg = xpgMapper.selectXpgByMachineId(machineId);
                String xpgName = xpg.getName();
                Date thisTimeRollOneMinute = new Date(new Date().getTime() - 1 * 60 * 1000);//前一分钟
                Netty last = nettyMapper.getLastNettyByXpgAndOpenTime(xpg.getName(),closeTime);//该焊机最近一次关机后的最新数据包
                /*Netty newLast = nettyMapper.getLastNettyByXpgAndOpenTime(xpg.getName(),thisTimeRollOneMinute);//之前两分钟后该焊机的最新数据包
                logger.info("[************]------------------newLast:"+newLast+"--------------");*/
                Date now = new Date();
                long jgtime = now.getTime()-closeTime.getTime();//现在距这台焊机关机时间的间隔
                if(jgtime <= 1*60*1000) { //距这台焊机关机时间间隔小于1分钟先跳过这台焊机的判断
                    logger.info(new Date()+"---------运行到jgtime<=1*60*1000了-------");
                    continue;
                }else { //距这台焊机关机时间间隔大于1分钟
                    logger.info(new Date()+"---------运行到else1了-------");
                    if(last != null) { //距这台焊机关机时间间隔大于1分钟后还有包过来，那就是关机失败了
                        try {
                            nettyServerHandler.controlMachine(xpgName,false);//再关一次机
                            logger.info("[**********]---------xpg:"+xpg.getName()+",-------焊机:"+xpgName+"再次关机------------");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }else{
                        logger.info("[**********]---------xpg:"+xpg.getName()+",-------焊机:"+xpgName+"再次关机成功------------");
                    }
                }
            }
        }

    }
}
