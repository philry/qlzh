package com.sy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
public class Engineering {
    private int id;
    private String name;
    private Integer level;
    private Integer pid;
    private Integer time;
    private Integer workingTime;
    private String efficency;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;
    private Double power;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;
    private String remark;

    private List<Engineering> sonLsit;

    public Engineering() {
    }

    public Engineering(int id) {
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
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "level")
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Basic
    @Column(name = "pid")
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
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
    @Column(name = "efficency")
    public String getEfficency() {
        return efficency;
    }

    public void setEfficency(String efficency) {
        this.efficency = efficency;
    }

    @Basic
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Basic
    @Column(name = "power")
    public Double getPower() {
        return power;
    }

    public void setPower(Double power) {
        this.power = power;
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

    @Transient
    public List<Engineering> getSonLsit() {
        return sonLsit;
    }

    public void setSonLsit(List<Engineering> sonLsit) {
        this.sonLsit = sonLsit;
    }

    @Override
    public String toString() {
        return "Engineering{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", pid=" + pid +
                ", time=" + time +
                ", workingTime=" + workingTime +
                ", efficency='" + efficency + '\'' +
                ", date=" + date +
                ", power=" + power +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", remark='" + remark + '\'' +
                ", sonLsit=" + sonLsit +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Engineering that = (Engineering) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(level, that.level) &&
                Objects.equals(pid, that.pid) &&
                Objects.equals(time, that.time) &&
                Objects.equals(workingTime, that.workingTime) &&
                Objects.equals(efficency, that.efficency) &&
                Objects.equals(date, that.date) &&
                Objects.equals(power, that.power) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, level, pid, time, workingTime, efficency, date, power, status, createTime, updateTime, remark);
    }
}
