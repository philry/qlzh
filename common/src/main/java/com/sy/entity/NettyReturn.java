package com.sy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class NettyReturn {
    private int id;
    private String xpg;
    private String machineName;
    private String power;
    private String currents;
    private Double voltage;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;
    private String remark;

    @Transient
    private List<String> list;

    @Transient
    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
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
    @Column(name = "xpg")
    public String getXpg() {
        return xpg;
    }

    public void setXpg(String xpg) {
        this.xpg = xpg;
    }

    /*@Basic
    @Column(name = "machine_name")*/
    @Transient
    public String getMachineName() { return machineName; }

    public void setMachineName(String machineName) { this.machineName = machineName; }

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
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
        NettyReturn netty = (NettyReturn) o;
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
        return "NettyReturn{" +
                "id=" + id +
                '}';
    }
}
