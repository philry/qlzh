package com.sy.quartz;

import com.sy.core.netty.tcp.NettyServerHandler;
import com.sy.dao.*;
import com.sy.entity.*;
import com.sy.service.MessageDataService;
import com.sy.service.WorkService;
import com.sy.starter.Starter;
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
 * 超限警告及定时关机
 */
@Component
public class NettyNewQuartz extends QuartzJobBean {

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
    private WorkTypeMapper workTypeMapper;

    @Autowired
    private WorkTypeDao workTypeDao;

    Logger logger = Logger.getLogger(NettyNewQuartz.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		/*//测试
		try {
			Thread.sleep(2*60*1000);//
			System.out.println("-----------------------");
			System.out.println("----------nettyQuartz运行了-----------");
			System.out.println("-----------------------");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/

//        System.out.println("--------------开始运行nettyNewQuartz-----------------");
        logger.info("--------------"+new Date()+"--------------");
        logger.info("--------------"+new Timestamp(new Date().getTime())+"开始运行nettyNewQuartz-----------------");
        // 查询出当前正在工作的所有焊机
        List<MachineNow> list = machineNowDao.findAll();
        if (list != null && list.size() > 0) {
            Xpg xpg = null;
            Netty last = null;
            Netty newLast = null;
            double maxA = 0;
            double minA = 0;
            for (MachineNow machineNow : list) {
                // 获取焊机的工作最大电流和非工作最大电流
                maxA = machineNow.getMachine().getMaxA();
                minA = machineNow.getMachine().getMinA();
                Integer machineId = machineNow.getMachine().getId();
                Machine machine =  machineDao.getById(machineId);
                // 获取xpg信息
                xpg = xpgMapper.selectXpgByMachineId(machineNow.getMachine().getId());
                Integer personId = machineNow.getPerson().getId();
                Date openTime = workService.getLastOpenTimeByMachine(machineId);//该焊机的最近开机时间
                Date thisTimeRollOneMinute = new Date(new Date().getTime() - 1 * 60 * 1000);
//                System.out.println("-------------前一分钟时间是:"+thisTimeRollOneMinute+"------------------");
                logger.info("-------------前一分钟时间是:"+thisTimeRollOneMinute+"------------------");

                // 获取最新的netty数据
                //		last = nettyMapper.getLastNettyByXpg(xpg.getName());//原来的
                //		Date openTimerollOneMinute= new Date(openTime.getTime() - 1 * 60 * 1000); //将OpenTime往前减1分钟
                last = nettyMapper.getLastNettyByXpgAndOpenTime(xpg.getName(),openTime);//该焊机最近一次开机后的最新数据包
                newLast = nettyMapper.getLastNettyByXpgAndOpenTime(xpg.getName(),thisTimeRollOneMinute);//之前一分钟后该焊机的最新数据包

                // 判断是否超限
				/*//原来的start
				boolean flag = true;//初始判断为超限
				if(last == null){
					try {
						Thread.sleep(60*1000);//没包就等一分钟等包过来
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				String[] currents = last.getCurrents().split(",");
				for (String s : currents) {
					if (Double.valueOf(s) < maxA) { //原来的对比方式,底表该焊机对应的最新包有一秒的电流小于最大电流判断为不超限，
				//	if(new BigDecimal(s).compareTo(new BigDecimal(maxA)) < 0){ //即60s内全部高于最大工作电流，则判定为超载
						flag = false;
						break;
					}
				}
				//原来的end*/

                //我写的
                boolean flag = false;//初始判断为不超限
                Date now = new Date();
                long jgtime = now.getTime()-openTime.getTime();//现在距这台焊机开机时间的间隔
                if(jgtime <= 1*60*1000){ //距这台焊机开机时间间隔小于1分钟先跳过这台焊机的判断
                    //	if(last == null){
                    //	while(last == null){
						/*try {
							//TODO
							Thread.sleep(60*1000);//现在时间距开机时间小于1分钟时没包就等一分钟等包过来
							break; //需要跳出循环，不然下面沿用的还是为null的last值
						} catch (InterruptedException e) {
							e.printStackTrace();
						}*/
                    continue;//跳出这台焊机的自动关机,先判断其他焊机，不然下面沿用的还是为null的last值
                    //	}
                }else{ //距这台焊机开机时间间隔大于1分钟

                    if(newLast == null){ //距这台焊机开机时间间隔大于1分钟后没包过来，那就是断开连接了\
//                        System.out.println(("---------"+machineNow.getMachine().getName()+"的4G模块已断开连接导致自动关机和电流超限关机失败"+"---------"));
                        logger.info(("---------"+machineNow.getMachine().getName()+"的4G模块已断开连接导致自动关机和电流超限关机失败"+"---------"));
					/*try {
						nettyServerHandler.controlMachine(xpg.getName(), false);
						Integer taskId = workService.selectTaskIdByPersonAndMachine(personId, machineId);
						workService.endWork(personId, taskId, machineId);
						machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println(machineNow.getMachine().getName() + "已自动关机(1)");*/
                        continue;//跳出这台焊机的自动关机，继续下一台的判断
                    }else{ //距这台焊机开机时间间隔大于1分钟并且有包
                        String[] currents = last.getCurrents().split(",");
						/*for (String s : currents) {
							if (Double.valueOf(s) > maxA) { //底表该焊机对应的最新包有一秒的电流大于最大电流判断为超限
								//	if(new BigDecimal(s).compareTo(new BigDecimal(maxA)) < 0){ //我改的对比方式
								flag = true;
								break;
							}
						}*/
						/*for(int j = 0; j < currents.length; j++) { //这个判断方式有问题，经常不起作用
							if(j < (currents.length-2)){
								double i = Double.parseDouble(currents[j]);
								if (i > maxA) {
									double i2 = Double.parseDouble(currents[j+1]);
									if(i2 > maxA) {
										double i3 = Double.parseDouble(currents[j+2]);
										if (i3 > maxA) {  //底表该焊机对应的最新包有连续3秒的电流大于最大电流判断为超限
											flag = true;
											break;
										}
									}
								}
							}
						}*/

                        //底表该焊机对应的最新包有连续3秒的电流大于最大电流判断为超限
                        for (int j = 0; j < (currents.length - 2); j++) {
                            double i = Double.parseDouble(currents[j]);
                            if (i > maxA) {
                                double i2 = Double.parseDouble(currents[j + 1]);
                                double i3 = Double.parseDouble(currents[j + 2]);
                                if (i2 > maxA && i3 > maxA) {
                                    flag = true;
                                    break;
                                }
                            }
                        }

                        if (flag) {
						// 如果超限,发送超限警告,并关闭焊机,删除machine_now中该焊机的数据
						//发给焊工
						MessageData messageData = new MessageData();
						messageData.setSendId(0);
						/*Integer leader = deptMapper.selectDeptById(machineNow.getMachine().getDept().getId()).getLeader();
						messageData.setAccpetId(leader);//原来的*/
						messageData.setAccpetId(personId);//我改的
						messageData.setContext(String.valueOf(machineNow.getMachine().getId()));
						try {
							// 发送超限警告信息
							messageDataService.sendMessage(messageData, 2);
						} catch (Exception e) {
							e.printStackTrace();
						}
						/*//发给焊工所属班组部门领导
                    	Person person = personMapper.selectPersonById(personId);
						Dept dept = deptMapper.selectDeptById(person.getDeptId());
						Integer leader = dept.getLeader();*/
						//发给焊机所属部门领导
						MessageData messageData2 = new MessageData();
						messageData2.setSendId(0);
						Integer leader = deptMapper.selectDeptById(machineNow.getMachine().getDept().getId()).getLeader();
						messageData2.setAccpetId(leader);
						messageData2.setContext(String.valueOf(machineNow.getMachine().getId()));
						try {
							// 发送超限警告信息
							messageDataService.sendMessage(messageData2, 2);
						} catch (Exception e) {
							e.printStackTrace();
						}
						//	machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
						try {
							nettyServerHandler.controlMachine(xpg.getName(), false);
							Integer taskId = workService.selectTaskIdByPersonAndMachine(personId, machineId);
							workService.endWork(personId, taskId, machineId);//原来的

                            /*//新增的start
                            String xpgName = xpgMapper.selectXpgByMachineId(machineId).getName();
                            Netty netty = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
                            if(netty != null){ //还有包说明第一次关机失败
//                                System.out.println(xpgName+"---------------第一次关机失败,尝试第二次关机-----------------");
                                logger.info(xpgName+"---------------第一次关机失败,尝试第二次关机----------------");
                                nettyServerHandler.controlMachine(machine.getXpg().getName(),false);
                                Thread.sleep(1*60*1000);
                                Netty netty2 = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
                                if(netty2 != null){ //还有包说明第二次关机失败
//                                    System.out.println(xpgName+"---------------第二次关机失败，尝试第三次关机----------------");
                                    logger.info(xpgName+"---------------第二次关机失败，尝试第三次关机----------------");
                                    nettyServerHandler.controlMachine(machine.getXpg().getName(),false);
                                    Thread.sleep(1*60*1000);
                                    Netty netty3 = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
                                    if(netty3 != null){ //还有包说明第三次关机失败
//                                        System.out.println(xpgName+"---------------第三次关机失败----------------");
                                        logger.info(xpgName+"---------------第三次关机失败----------------");
                                    }
                                }
                            }else { //没包说明关机成功
                                workService.endWork(personId,taskId,machineId);
                            }
                            //新增的end*/
//							machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
						} catch (Exception e) {
							e.printStackTrace();
						}
//						System.out.println(machineNow.getMachine().getName() + "已超限");
                            logger.info("----------"+machineNow.getMachine().getName() + "已超限------------");
						// 如果没有超限,则判断是否在工作,如果处于非工作状态,判断其未工作时间是否达到设定的定时关机时间
                        }  else {

                            boolean flag2 = true;//flag2为true表示处于非工作状态
                            for (String s : currents) {
                                if (Double.valueOf(s) > minA) { //有一个电流大于最小电流就表示在工作，处于工作状态flag2就为false
                                    //	if(new BigDecimal(s).compareTo(new BigDecimal(minA))>0){ //我改的对比方式
                                    flag2 = false;
                                    break;
                                }
                            }
                            if (flag2) {//非工作状态下定时关机
                                // 获取定时关机设定时间
                               /* List<Energy> energyList = energyMapper.selectEnergyList();
                                Integer time = energyList.get(0).getTime(); //time单位为min,原来的*/

                               //自动关机时间按工种取值
                                Integer personId1 = machineNowDao.getDataByMachineId(machineId).get(0).getPerson().getId();
                                String workTypeName = personDao.getById(personId1).getWorkType();
                                WorkType workType = workTypeDao.getByName(workTypeName);
                                Integer time = workType.getTime();

                                Netty pre = nettyMapper.selectNettyByXpgAndTime(xpg.getName(), time); //当前正在工作的焊机当前时间往前{time}个的底表数据
                                //距该xpg对应最新包的时间未超过2分钟内
                                if ((new Date().getTime() / 1000L / 60 - last.getCreateTime().getTime() / 1000L / 60) <= 2) { //Todo
                                    String[] currents2 = pre.getCurrents().split(",");
                                    boolean flag3 = true;
                                    for (String s : currents2) {
                                        if (Double.valueOf(s) > minA) { //有一个电流大于最小电流就表示在工作，处于非工作状态就为false
                                            //		if(new BigDecimal(s).compareTo(new BigDecimal(minA))>0){ //我改的对比方式
                                            flag3 = false;
                                            break;
                                        }
                                    }
                                    if (flag3) {
                                        // 判断是否持续未工作(原来的方式)
                                        List<Netty> lists = nettyMapper.selectAllNettyByXpgAndTime(xpg.getName(), time);
                                        if (lists.size() >= (time - 1)) { //该xpg对应的netty表中的包有了time-1个了，表示到了设定的关机时间了
                                            boolean flag4 = true; // flag4为true表示已经到了设定关机的时间
                                            //原来的方式
                                            long preTime = (lists.get(0).getCreateTime().getTime()) / 1000L / 60;
                                            outer:
                                            for (Netty n : lists) {
                                                String[] currents3 = n.getCurrents().split(",");
                                                if (preTime - n.getCreateTime().getTime() / 1000L / 60 <= 1) {
                                                    preTime = n.getCreateTime().getTime() / 1000L / 60;
                                                    for (String s : currents3) {
                                                        if (Double.valueOf(s) > minA) {//原来的对比方式
                                                            //		if(new BigDecimal(s).compareTo(new BigDecimal(minA))>0){//我改的对比方式
                                                            flag4 = false;
                                                            break outer;
                                                        }
                                                    }
                                                } else {
                                                    flag4 = false;
                                                    break;
                                                }
                                            }
                                            if (flag4) {
                                                //		machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
                                                try {
                                                    nettyServerHandler.controlMachine(xpg.getName(), false);
                                                    Integer taskId = workService.selectTaskIdByPersonAndMachine(personId, machineId);
                                                    workService.endWork(personId, taskId, machineId);//原来的

                                                   /* //新增的start
                                                    String xpgName = xpgMapper.selectXpgByMachineId(machineId).getName();
                                                    Netty netty = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
                                                    if(netty != null){ //还有包说明第一次关机失败
//                                                        System.out.println(xpgName+"---------------第一次关机失败,尝试第二次关机-----------------");
                                                        logger.info(xpgName + "---------------第一次关机失败,尝试第二次关机-----------------");
                                                        nettyServerHandler.controlMachine(machine.getXpg().getName(),false);
                                                        Thread.sleep(1*60*1000);
                                                        Netty netty2 = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
                                                        if(netty2 != null){ //还有包说明第二次关机失败
//                                                            System.out.println(xpgName+"---------------第二次关机失败，尝试第三次关机----------------");
                                                            logger.info(xpgName+"---------------第二次关机失败，尝试第三次关机----------------");
                                                            nettyServerHandler.controlMachine(machine.getXpg().getName(),false);
                                                            Thread.sleep(1*60*1000);
                                                            Netty netty3 = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
                                                            if(netty3 != null){ //还有包说明第三次关机失败
//                                                                System.out.println(xpgName+"---------------第三次关机失败----------------");
                                                                logger.info(xpgName+"---------------第三次关机失败----------------");
                                                            }
                                                        }
                                                    }else { //没包说明关机成功
                                                        workService.endWork(personId,taskId,machineId);
                                                    }
                                                    //新增的end*/

//                                                    machineNowMapper.deleteMachineNowByMachineId(machineNow.getMachine().getId());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
//                                                System.out.println(machineNow.getMachine().getName() + "已自动关机(2)");
                                                logger.info(machineNow.getMachine().getName() + "已自动关机(2)");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
