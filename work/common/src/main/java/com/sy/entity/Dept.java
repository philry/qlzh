package com.sy.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
public class Dept {
    private int id;
    private String name;
    private Integer level;
    private Integer pid;
    private Dept pDept;
    private Integer leader;
    private Person person;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;
    private String remark;
    @Transient
    private List<Dept> sDepts;

    public Dept() {
    }

    public Dept(int id) {
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

    @Transient
    public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
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

    @Transient
    public Dept getpDept() {
		return pDept;
	}

	public void setpDept(Dept pDept) {
		this.pDept = pDept;
	}

	@Transient
    public Integer getLeader() {
        return leader;
    }

    public void setLeader(Integer leader) {
        this.leader = leader;
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
    public List<Dept> getsDepts() {
		return sDepts;
	}

	public void setsDepts(List<Dept> sDepts) {
		this.sDepts = sDepts;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dept dept = (Dept) o;
        return id == dept.id &&
                Objects.equals(name, dept.name) &&
                Objects.equals(level, dept.level) &&
                Objects.equals(pid, dept.pid) &&
                Objects.equals(leader, dept.leader) &&
                Objects.equals(status, dept.status) &&
                Objects.equals(createTime, dept.createTime) &&
                Objects.equals(updateTime, dept.updateTime) &&
                Objects.equals(remark, dept.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, level, pid, leader, status, createTime, updateTime, remark);
    }

	@Override
	public String toString() {
		return "Dept [id=" + id + ", name=" + name + ", level=" + level + ", pid=" + pid + ", leader=" + leader
				+ ", status=" + status + ", createTime=" + createTime + ", updateTime=" + updateTime + ", remark="
				+ remark + "]";
	}


}
