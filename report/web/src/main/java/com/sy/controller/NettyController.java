package com.sy.controller;


import com.sy.dao.MachineDao;
import com.sy.dao.XpgMapper;
import com.sy.entity.Netty;
import com.sy.service.NettyService;
import com.sy.utils.DateUtils;
import com.sy.vo.JsonResult;
import com.sy.vo.PageJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("netty")
public class NettyController {


    @Autowired
    private NettyService nettyService;

    @Autowired
    private XpgMapper xpgMapper;

    @Autowired
    private MachineDao machineDao;


    @RequestMapping("select")
    public JsonResult getSelect(String xpg,int page,int pageSize,String beginTime,String endTime){

        Page<Netty> pages =  nettyService.getAllByName(xpg,page,pageSize,beginTime =="" ?null: DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime));

        List<Netty> list = pages.getContent();

        for (Netty netty : list) {
            List<String> strings = Arrays.asList(netty.getCurrents().split(","));
            netty.setList(strings);


            String nettyName = netty.getXpg();
            String machineName = null;
            if(nettyName != null){
                Integer machineId = xpgMapper.selectXpgByName(nettyName).getMachineId();
                machineName = machineDao.getNameById(machineId);

            }
            netty.setMachineName(machineName);
        }

//        Collections.reverse(list);

        return PageJsonResult.buildSuccessPage(200,pages);
    }

}
