package com.sy.vo;

import java.util.Objects;

public class AppVo {

    private String workNo;

    private int time;

    private int workTime;

    private String efficiency;

    private String power;


    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
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

    public String getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppVo appVo = (AppVo) o;
        return time == appVo.time &&
                workTime == appVo.workTime &&
                workNo.equals(appVo.workNo) &&
                efficiency.equals(appVo.efficiency) &&
                power.equals(appVo.power);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workNo, time, workTime, efficiency, power);
    }
}
