package com.sy.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "scan_work", schema = "qlzh", catalog = "")
public class ScanWork {
    private int id;
    private Integer personId;
    private Integer machineId;
    private Integer orderId;
    private String operation;
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
    @Column(name = "person_id")
    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    @Basic
    @Column(name = "machine_id")
    public Integer getMachineId() {
        return machineId;
    }

    public void setMachineId(Integer machineId) {
        this.machineId = machineId;
    }

    @Basic
    @Column(name = "order_id")
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "operation")
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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
        ScanWork scanWork = (ScanWork) o;
        return id == scanWork.id &&
                Objects.equals(personId, scanWork.personId) &&
                Objects.equals(machineId, scanWork.machineId) &&
                Objects.equals(orderId, scanWork.orderId) &&
                Objects.equals(operation, scanWork.operation) &&
                Objects.equals(status, scanWork.status) &&
                Objects.equals(createTime, scanWork.createTime) &&
                Objects.equals(updateTime, scanWork.updateTime) &&
                Objects.equals(remark, scanWork.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personId, machineId, orderId, operation, status, createTime, updateTime, remark);
    }
}
