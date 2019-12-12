package com.sy.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Netty {
    private int id;
    private String xpg;
    private String power;
    private String currents;
    private Double voltage;
    private String status;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String remark;

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
    @Column(name = "xpg")
    public String getXpg() {
        return xpg;
    }

    public void setXpg(String xpg) {
        this.xpg = xpg;
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
    @Column(name = "currents")
    public String getCurrents() {
        return currents;
    }

    public void setCurrents(String currents) {
        this.currents = currents;
    }

    @Basic
    @Column(name = "voltage")
    public Double getVoltage() {
        return voltage;
    }

    public void setVoltage(Double voltage) {
        this.voltage = voltage;
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
                Objects.equals(power, netty.power) &&
                Objects.equals(currents, netty.currents) &&
                Objects.equals(voltage, netty.voltage) &&
                Objects.equals(status, netty.status) &&
                Objects.equals(createTime, netty.createTime) &&
                Objects.equals(updateTime, netty.updateTime) &&
                Objects.equals(remark, netty.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, xpg, power, currents, voltage, status, createTime, updateTime, remark);
    }

    @Override
    public String toString() {
        return "Netty{" +
                "id=" + id +
                '}';
    }
}
