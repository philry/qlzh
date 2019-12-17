package com.sy.vo;

import java.util.List;

public class Unit {

    //名称
    private String name;

    //在岗时间
    private int time;

    //有效时间
    private int workTime;

    //能耗
    private double power;

    //工作用电量
    private double working_power;

    //空载用电量
    private double loading_power;

    //电流超限此处
    private int counts;

    //备注（补充字段）
    private Object remark;

    //子集
    private List<Unit> sonList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public List<Unit> getSonList() {
        return sonList;
    }

    public void setSonList(List<Unit> sonList) {
        this.sonList = sonList;
    }

    public double getWorking_power() {
        return working_power;
    }

    public void setWorking_power(double working_power) {
        this.working_power = working_power;
    }

    public double getLoading_power() {
        return loading_power;
    }

    public void setLoading_power(double loading_power) {
        this.loading_power = loading_power;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }
}
