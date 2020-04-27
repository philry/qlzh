package com.sy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "person_efficiency", schema = "qlzh2", catalog = "")
public class PersonEfficiency {
    private int id;
    private String task;
    private String deptOne;
    private String deptTwo;
    private String deptThree;
    private String name;
    private Integer time;
    private Integer workingTime;
    private Integer noloadingTime;
    private String efficiency;
    private Double workingPower;
    private Double noloadingPower;
    private Integer counts;
    private Integer overTime;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;
    private String remark;

    public PersonEfficiency() {
    }

    public PersonEfficiency(int id) {
        this.id = id;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "task")
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Basic
    @Column(name = "dept_one")
    public String getDeptOne() {
        return deptOne;
    }

    public void setDeptOne(String deptOne) {
        this.deptOne = deptOne;
    }

    @Basic
    @Column(name = "dept_two")
    public String getDeptTwo() {
        return deptTwo;
    }

    public void setDeptTwo(String deptTwo) {
        this.deptTwo = deptTwo;
    }

    @Basic
    @Column(name = "dept_three")
    public String getDeptThree() {
        return deptThree;
    }

    public void setDeptThree(String deptThree) {
        this.deptThree = deptThree;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "time")
    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    @Basic
    @Column(name = "working_time")
    public Integer getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(Integer workingTime) {
        this.workingTime = workingTime;
    }

    @Basic
    @Column(name = "noloading_time")
    public Integer getNoloadingTime() { return noloadingTime; }

    public void setNoloadingTime(Integer noloadingTime) { this.noloadingTime = noloadingTime; }

    @Basic
    @Column(name = "efficiency")
    public String getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }

    @Basic
    @Column(name = "working_power")
    public Double getWorkingPower() {
        return workingPower;
    }

    public void setWorkingPower(Double workingPower) {
        this.workingPower = workingPower;
    }

    @Basic
    @Column(name = "noloading_power")
    public Double getNoloadingPower() {
        return noloadingPower;
    }

    public void setNoloadingPower(Double noloadingPower) {
        this.noloadingPower = noloadingPower;
    }

    @Basic
    @Column(name = "counts")
    public Integer getCounts() {
        return counts;
    }

    public void setCounts(Integer counts) {
        this.counts = counts;
    }

    @Basic
    @Column(name = "over_time")
    public Integer getOverTime() {
        return overTime;
    }

    public void setOverTime(Integer overTime) {
        this.overTime = overTime;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonEfficiency that = (PersonEfficiency) o;
        return id == that.id &&
                Objects.equals(task, that.task) &&
                Objects.equals(deptOne, that.deptOne) &&
                Objects.equals(deptTwo, that.deptTwo) &&
                Objects.equals(deptThree, that.deptThree) &&
                Objects.equals(name, that.name) &&
                Objects.equals(time, that.time) &&
                Objects.equals(workingTime, that.workingTime) &&
                Objects.equals(efficiency, that.efficiency) &&
                Objects.equals(workingPower, that.workingPower) &&
                Objects.equals(noloadingPower, that.noloadingPower) &&
                Objects.equals(counts, that.counts) &&
                Objects.equals(overTime, that.overTime) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, task, deptOne, deptTwo, deptThree, name, time, workingTime, efficiency, workingPower, noloadingPower, counts, overTime, status, createTime, updateTime, remark);
    }
}
