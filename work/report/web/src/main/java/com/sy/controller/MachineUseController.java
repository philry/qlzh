package com.sy.controller;

import com.sy.entity.Machine;
import com.sy.entity.MachineUse;
import com.sy.service.MachineUseService;
import com.sy.utils.DateUtils;
import com.sy.vo.JsonResult;
import com.sy.vo.PageJsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("machineUse")
public class MachineUseController {

    @Autowired
    private MachineUseService machineUseService;

    @RequestMapping("init")
    public JsonResult getInitData(int machineId,String beginTime,String endTime){

        List<MachineUse> list = machineUseService.getDataByIdAndTime(machineId,beginTime =="" ?null: DateUtils.parseDate(beginTime),endTime =="" ?null:DateUtils.parseDate(endTime));
        Set<Integer> sets = new HashSet<>();
        Map<Integer,List<MachineUse>> map = new HashMap<>();
        for (MachineUse machineUse : list) {
            Integer id = machineUse.getMachineId();
            if(map.get(id)==null){
                List<MachineUse> tempList = new ArrayList<>();
                tempList.add(machineUse);
                map.put(id,tempList);
            }else {
                map.get(id).add(machineUse);
            }
            sets.add(id);
        }

        
        List<MachineUse> result = new ArrayList<>();
        for (Integer set : sets) {
            int noloadingTime = 0;
            int workingTime = 0;
            int time = 0;
            double noloadingPower = 0;
            double workingPower = 0;
            double power = 0;
            int counts =0;
            for (MachineUse machineUse : map.get(set)) {
                noloadingTime += machineUse.getNoloadingTime();
                workingTime += machineUse.getWorkTime();
                time += machineUse.getWorkTime();
                time += machineUse.getNoloadingTime();
                noloadingPower += Double.parseDouble(machineUse.getNoloadingPower());
                workingPower += Double.parseDouble(machineUse.getWorkingPower());
                power += Double.parseDouble(machineUse.getNoloadingPower());
                power += Double.parseDouble(machineUse.getWorkingPower());
                counts += Integer.parseInt(machineUse.getOvercounts());
            }
            MachineUse machineUse = new MachineUse();
            machineUse.setDepeName(map.get(set).get(0).getDepeName());
            machineUse.setMachineId(map.get(set).get(0).getMachineId());
            machineUse.setNoloadingTime(noloadingTime);
            machineUse.setNoloadingPower(String.format("%.2f",noloadingPower));
            machineUse.setWorkTime(workingTime);
            machineUse.setWorkingPower(String.format("%.2f",workingPower));
            machineUse.setTime(time);
            machineUse.setPower(String.format("%.2f",power));
            machineUse.setOvercounts(String.valueOf(counts));
            result.add(machineUse);
        }

        if(result.isEmpty()){
            return JsonResult.buildFailure(404,"没有相关数据");
        }


        return JsonResult.buildSuccess(200,result);
    }

}
