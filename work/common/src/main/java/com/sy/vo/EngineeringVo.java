package com.sy.vo;

public class EngineeringVo {

    private int workTime;

    private int time;

    private String name_1;

    private int workTime_1;

    private int time_1;

    private String name_2;

    private int workTime_2;

    private int time_2;

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getName_1() {
        return name_1;
    }

    public void setName_1(String name_1) {
        this.name_1 = name_1;
    }

    public int getWorkTime_1() {
        return workTime_1;
    }

    public void setWorkTime_1(int workTime_1) {
        this.workTime_1 = workTime_1;
    }

    public int getTime_1() {
        return time_1;
    }

    public void setTime_1(int time_1) {
        this.time_1 = time_1;
    }

    public String getName_2() {
        return name_2;
    }

    public void setName_2(String name_2) {
        this.name_2 = name_2;
    }

    public int getWorkTime_2() {
        return workTime_2;
    }

    public void setWorkTime_2(int workTime_2) {
        this.workTime_2 = workTime_2;
    }

    public int getTime_2() {
        return time_2;
    }

    public void setTime_2(int time_2) {
        this.time_2 = time_2;
    }

    @Override
    public String toString() {
        return "EngineeringVo{" +
                "workTime=" + workTime +
                ", time=" + time +
                ", name_1='" + name_1 + '\'' +
                ", workTime_1=" + workTime_1 +
                ", time_1=" + time_1 +
                ", name_2='" + name_2 + '\'' +
                ", workTime_2=" + workTime_2 +
                ", time_2=" + time_2 +
                '}';
    }
}
