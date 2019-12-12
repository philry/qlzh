package com.sy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

@Entity
public class Machine {
    private int id;
    private String name;
    private Integer typeId;
    private Date payTime;
    private Double maxA;
    private Double minA;
//    private Integer xpgId;
    private Xpg xpg;
    private Integer deptId;
    private byte[] code;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;
    private String remark;

    public Machine() {
    }

    public Machine(int id) {
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
    @Column(name = "type_id")
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    @Basic
    @Column(name = "pay_time")
    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    @Basic
    @Column(name = "max_a")
    public Double getMaxA() {
        return maxA;
    }

    public void setMaxA(Double maxA) {
        this.maxA = maxA;
    }

    @Basic
    @Column(name = "min_a")
    public Double getMinA() {
        return minA;
    }

    public void setMinA(Double minA) {
        this.minA = minA;
    }


    @ManyToOne(targetEntity = Xpg.class)
    @JoinColumn(name = "xpg_id")
    @Fetch(FetchMode.SELECT)
    public Xpg getXpg() {
        return xpg;
    }

    public void setXpg(Xpg xpg) {
        this.xpg = xpg;
    }

    @Basic
    @Column(name = "dept_id")
    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    @Basic
    @Column(name = "code")
    public byte[] getCode() {
        return code;
    }

    public void setCode(byte[] code) {
        this.code = code;
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
        Machine machine = (Machine) o;
        return id == machine.id &&
                Objects.equals(name, machine.name) &&
                Objects.equals(typeId, machine.typeId) &&
                Objects.equals(payTime, machine.payTime) &&
                Objects.equals(maxA, machine.maxA) &&
                Objects.equals(minA, machine.minA) &&
                Objects.equals(deptId, machine.deptId) &&
                Arrays.equals(code, machine.code) &&
                Objects.equals(status, machine.status) &&
                Objects.equals(createTime, machine.createTime) &&
                Objects.equals(updateTime, machine.updateTime) &&
                Objects.equals(remark, machine.remark);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name, typeId, payTime, maxA, minA, deptId, status, createTime, updateTime, remark);
        result = 31 * result + Arrays.hashCode(code);
        return result;
    }
}
