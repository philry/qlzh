package com.sy.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Netty {
    private int id;
    private String xpg;
    private Integer noloadingTime;
    private Integer workingTime;
    private String power;
    private String status;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String remark;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "xpg")
    public String getXpg() {
        return xpg;
    }

    public void setXpg(String xpg) {
        this.xpg = xpg;
    }

    @Basic
    @Column(name = "noloading_time")
    public Integer getNoloadingTime() {
        return noloadingTime;
    }

    public void setNoloadingTime(Integer noloadingTime) {
        this.noloadingTime = noloadingTime;
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
    @Column(name = "power")
    public String getPower() {
        return power;
    }

    public void setPower(String power) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Netty netty = (Netty) o;
        return id == netty.id &&
                Objects.equals(xpg, netty.xpg) &&
                Objects.equals(noloadingTime, netty.noloadingTime) &&
                Objects.equals(workingTime, netty.workingTime) &&
                Objects.equals(power, netty.power) &&
                Objects.equals(status, netty.status) &&
                Objects.equals(createTime, netty.createTime) &&
                Objects.equals(updateTime, netty.updateTime) &&
                Objects.equals(remark, netty.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, xpg, noloadingTime, workingTime, power, status, createTime, updateTime, remark);
    }
}
