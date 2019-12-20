package com.sy.vo;

public class EfficiencyStatisticsVo {

    private String sonName;

    private int sonTime;

    private int sonWorkTime;

    private double sonPower;

    private String name;

    private int time;

    private int workTime;

    private double power;


    public String getSonName() {
        return sonName;
    }

    public void setSonName(String sonName) {
        this.sonName = sonName;
    }

    public int getSonTime() {
        return sonTime;
    }

    public void setSonTime(int sonTime) {
        this.sonTime = sonTime;
    }

    public int getSonWorkTime() {
        return sonWorkTime;
    }

    public void setSonWorkTime(int sonWorkTime) {
        this.sonWorkTime = sonWorkTime;
    }

    public double getSonPower() {
        return sonPower;
    }

    public void setSonPower(double sonPower) {
        this.sonPower = sonPower;
    }

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

    @Override
    public String toString() {
        return "EfficiencyStatisticsVo{" +
                "sonName='" + sonName + '\'' +
                ", sonTime=" + sonTime +
                ", sonWorkTime=" + sonWorkTime +
                ", sonPower=" + sonPower +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", workTime=" + workTime +
                ", power=" + power +
                '}';
    }
}
