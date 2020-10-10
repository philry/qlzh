package com.sy.vo;

import java.util.Objects;

public class IndexVo {

    private String workNo;

    private String name;

    private String power;

    private String efficiency;

    private String date;

    private String pervEfficiency;

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPervEfficiency() {
        return pervEfficiency;
    }

    public void setPervEfficiency(String pervEfficiency) {
        this.pervEfficiency = pervEfficiency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexVo indexVo = (IndexVo) o;
        return Objects.equals(workNo, indexVo.workNo) &&
                Objects.equals(name, indexVo.name) &&
                Objects.equals(power, indexVo.power) &&
                Objects.equals(efficiency, indexVo.efficiency) &&
                Objects.equals(date, indexVo.date) &&
                Objects.equals(pervEfficiency, indexVo.pervEfficiency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workNo, name, power, efficiency, date, pervEfficiency);
    }
}
