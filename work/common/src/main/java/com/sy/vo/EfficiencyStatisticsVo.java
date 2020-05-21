package com.sy.vo;

import java.util.Objects;

public class EfficiencyStatisticsVo {

    private String sonName;

    private String name_1;

    private String name_2;

    private String name_3;

    private int sonTime;

    private int sonWorkTime;

    private double sonPower;

    private String name;

    private int time;

    private int workTime;

    private double power;

    private String workNo;


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

    public String getName_1() { return name_1; }

    public void setName_1(String name_1) { this.name_1 = name_1; }

    public String getName_2() { return name_2; }

    public void setName_2(String name_2) { this.name_2 = name_2; }

    public String getName_3() { return name_3; }

    public void setName_3(String name_3) { this.name_3 = name_3; }

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

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EfficiencyStatisticsVo that = (EfficiencyStatisticsVo) o;
        return sonTime == that.sonTime &&
                sonWorkTime == that.sonWorkTime &&
                Double.compare(that.sonPower, sonPower) == 0 &&
                time == that.time &&
                workTime == that.workTime &&
                Double.compare(that.power, power) == 0 &&
                sonName.equals(that.sonName) &&
                name.equals(that.name) &&
                workNo.equals(that.workNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sonName, sonTime, sonWorkTime, sonPower, name, time, workTime, power, workNo);
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
                ", workNo='" + workNo + '\'' +
                '}';
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EfficiencyStatisticsVo that = (EfficiencyStatisticsVo) o;
        return sonTime == that.sonTime &&
                sonWorkTime == that.sonWorkTime &&
                Double.compare(that.sonPower, sonPower) == 0 &&
                time == that.time &&
                workTime == that.workTime &&
                Double.compare(that.power, power) == 0 &&
                Objects.equals(sonName, that.sonName) &&
                Objects.equals(name_1, that.name_1) &&
                Objects.equals(name_2, that.name_2) &&
                Objects.equals(name_3, that.name_3) &&
                Objects.equals(name, that.name) &&
                Objects.equals(workNo, that.workNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sonName, name_1, name_2, name_3, sonTime, sonWorkTime, sonPower, name, time, workTime, power, workNo);
    }

    @Override
    public String toString() {
        return "EfficiencyStatisticsVo{" +
                "sonName='" + sonName + '\'' +
                ", name_1='" + name_1 + '\'' +
                ", name_2='" + name_2 + '\'' +
                ", name_3='" + name_3 + '\'' +
                ", sonTime=" + sonTime +
                ", sonWorkTime=" + sonWorkTime +
                ", sonPower=" + sonPower +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", workTime=" + workTime +
                ", power=" + power +
                ", workNo='" + workNo + '\'' +
                '}';
    }
}
