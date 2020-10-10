package com.sy.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "machine_use", schema = "qlzh2", catalog = "")
public class MachineUse {
    private int id;
    private Integer machineId;
    @Transient
    private String machineName;
    private String depeName;
    private Integer noloadingTime;
    private Integer workTime;
    private Integer time;
    private String efficiency;
    private String noloadingPower;
    private String workingPower;
    private String power;
    private String overcounts;
    private Date createTime;
    private Date updateTime;
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
    @Column(name = "machine_id")
    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    @Transient
    public String getMachineName() { return machineName; }

    public void setMachineName(String machineName) { this.machineName = machineName; }

    @Basic
    @Column(name = "depe_name")
    public String getDepeName() {
        return depeName;
    }

    public void setDepeName(String depeName) {
        this.depeName = depeName;
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
    @Column(name = "work_time")
    public Integer getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Integer workTime) {
        this.workTime = workTime;
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
    @Column(name = "efficiency")
    public String getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(String efficiency) {
        this.efficiency = efficiency;
    }

    @Basic
    @Column(name = "noloading_power")
    public String getNoloadingPower() {
        return noloadingPower;
    }

    public void setNoloadingPower(String noloadingPower) {
        this.noloadingPower = noloadingPower;
    }

    @Basic
    @Column(name = "working_power")
    public String getWorkingPower() {
        return workingPower;
    }

    public void setWorkingPower(String workingPower) {
        this.workingPower = workingPower;
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
    @Column(name = "overcounts")
    public String getOvercounts() {
        return overcounts;
    }

    public void setOvercounts(String overcounts) {
        this.overcounts = overcounts;
    }

    @Basic
    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
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
        MachineUse that = (MachineUse) o;
        return id == that.id &&
                Objects.equals(machineId, that.machineId) &&
                Objects.equals(depeName, that.depeName) &&
                Objects.equals(noloadingTime, that.noloadingTime) &&
                Objects.equals(workTime, that.workTime) &&
                Objects.equals(time, that.time) &&
                Objects.equals(efficiency, that.efficiency) &&
                Objects.equals(noloadingPower, that.noloadingPower) &&
                Objects.equals(workingPower, that.workingPower) &&
                Objects.equals(power, that.power) &&
                Objects.equals(overcounts, that.overcounts) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, machineId, depeName, noloadingTime, workTime, time, efficiency, noloadingPower, workingPower, power, overcounts, createTime, updateTime, remark);
    }
}
