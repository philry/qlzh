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
    @Transient
    private Integer typeId;
    private MachineType machineType;
    private Date payTime;
    private Date lastMaintenanceTime;
    private Double maxA;
    private Double minA;
    @Transient
    private Integer xpgId;
    private Xpg xpg;
    @Transient
    private Integer deptId;
    private Dept dept;
    @Transient
    private Object code;
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

    @ManyToOne(targetEntity = MachineType.class)
    @JoinColumn(name = "type_id")
    @Fetch(FetchMode.SELECT)
    public MachineType getMachineType() {
		return machineType;
	}

	public void setMachineType(MachineType machineType) {
		this.machineType = machineType;
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

	@ManyToOne(targetEntity = Dept.class)
    @JoinColumn(name = "dept_id")
    @Fetch(FetchMode.SELECT)
	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	@Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
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
    @Column(name = "last_maintenance_time")
    public Date getLastMaintenanceTime() {
        return lastMaintenanceTime;
    }

    public void setLastMaintenanceTime(Date lastMaintenanceTime) {
        this.lastMaintenanceTime = lastMaintenanceTime;
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

    @Transient
    public Integer getXpgId() {
        return xpgId;
    }

    public void setXpgId(Integer xpgId) {
        this.xpgId = xpgId;
    }

    @Transient
    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    @Transient
    public Object getCode() {
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

}
