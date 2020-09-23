package com.sy.controller;

import com.sy.constant.HttpStatusConstant;
import com.sy.core.netty.tcp.NettyServerHandler;
import com.sy.dao.*;
import com.sy.entity.*;
import com.sy.service.WorkService;
import com.sy.utils.DateUtils;
import com.sy.vo.JsonResult;
import com.sy.vo.PageJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("work")
public class WorkController {

    @Autowired
    private WorkService workService;

    @Autowired
    private MachineDao machineDao;

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private MachineNowDao machineNowDao;

    @Autowired
    private NettyMapper nettyMapper;

    @Autowired
    private XpgMapper xpgMapper;

    @RequestMapping(value = "all",method = RequestMethod.GET)
    public PageJsonResult getAllWorks(Integer pageNum, Integer pageSize, String personName, String beginTime, String endTime){


        Page<Work> workPages = null;
        try {
            workPages = workService.getAllWork(pageNum,pageSize,personName, beginTime ==null ?null: DateUtils.parseDate(beginTime),endTime ==null ?null:DateUtils.getNextDay(endTime));
        } catch (Exception e) {
            return PageJsonResult.buildFailurePage(404,e.getMessage());
        }

        return PageJsonResult.buildSuccessPage(HttpStatusConstant.SUCCESS,workPages);
    }

    @RequestMapping(value = "startWork",method = RequestMethod.POST)
    public JsonResult startWork(Integer personId, Integer taskId, String machineId){
        try {
            int machineIndex = Integer.parseInt(machineId);
            Machine machine = machineDao.getById(machineIndex);
            Integer pileCounts = personDao.getPileCounts(personId);

            //判定用户有否有开焊机的权利
            Person person = personDao.getById(personId);
            int counts = person.getPileCounts();

            //获取当前角色已开启的焊机数量
            List<MachineNow> list = machineNowDao.getByPersonId(personId);
            int openCounts = list.size();

            if(pileCounts == 0 || pileCounts == null || "".equals(pileCounts)){ //人员的焊机开机数量为‘0’或空白或没填时不能打开焊机
                throw new RuntimeException("该人员不能打开焊机！");
            }else if(openCounts>=counts){
                throw new Exception("可开焊机达到最大数量,请关闭其他焊机后在尝试");
            }else{
                nettyServerHandler.controlMachine(machine.getXpg().getName(),true);
                workService.startWork(personId,taskId,machineIndex);//原来的

                /*//新增的start
                Thread.sleep(1*60*1000);
                String xpgName = xpgMapper.selectXpgByMachineId(machineIndex).getName();
                Netty netty = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
                if(netty == null){ //没包说明开机失败
                    System.out.println(xpgName+"---------------第一次开机失败，尝试第二次开机-----------------");
                    nettyServerHandler.controlMachine(machine.getXpg().getName(),true);
                    Thread.sleep(1*60*1000);
                    Netty netty2 = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
                    if(netty2 == null){ //还有包说明第二次关机失败
                        System.out.println(xpgName+"---------------第二次开机失败，尝试第三次开机----------------");
                        nettyServerHandler.controlMachine(machine.getXpg().getName(),true);
                        Thread.sleep(1*60*1000);
                        Netty netty3 = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
                        if(netty3 == null){ //还有包说明第三次关机失败
                            System.out.println(xpgName+"---------------第三次开机失败----------------");
                        }
                    }
                }else { //有包说明开机成功
                    workService.startWork(personId, taskId, machineIndex);
                }
                //新增的end*/
            }
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }

        return JsonResult.buildSuccess(200,"操作成功");
    }

    @RequestMapping(value = "endWork",method = RequestMethod.POST)
    public JsonResult endWork(Integer personId, Integer taskId, String machineId){
        try {
            int machineIndex = Integer.parseInt(machineId);
            Machine machine = machineDao.getById(machineIndex);
            nettyServerHandler.controlMachine(machine.getXpg().getName(),false);
            workService.endWork(personId,taskId,machineIndex);//原来的

            /*//新增的start
            Thread.sleep(1*60*1000);
            String xpgName = xpgMapper.selectXpgByMachineId(machineIndex).getName();
            Netty netty = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
            if(netty != null){ //还有包说明第一次关机失败
                System.out.println(xpgName+"---------------第一次关机失败,尝试第二次关机-----------------");
                nettyServerHandler.controlMachine(machine.getXpg().getName(),false);
                Thread.sleep(1*60*1000);
                Netty netty2 = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
                if(netty2 != null){ //还有包说明第二次关机失败
                    System.out.println(xpgName+"---------------第二次关机失败，尝试第三次关机----------------");
                    nettyServerHandler.controlMachine(machine.getXpg().getName(),false);
                    Thread.sleep(1*60*1000);
                    Netty netty3 = nettyMapper.getLastNettyByXpgAndOpenTime(xpgName,new Date(new Date().getTime() - 1*60*1000));//往前1分钟之后有没包
                    if(netty3 != null){ //还有包说明第三次关机失败
                        System.out.println(xpgName+"---------------第三次关机失败----------------");
                    }
                }
            }else { //没包说明关机成功
                workService.endWork(personId,taskId,machineIndex);
            }
            //新增的end*/
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.buildFailure(404,e.getMessage());
        }

        return JsonResult.buildSuccess(200,"操作成功");
    }

}
