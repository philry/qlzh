package com.sy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "data_manage", schema = "qlzh2", catalog = "")
public class DataManage {
    private int id;
//    private Integer workId;
    private Work work;
    private Integer taskId;
    private Integer personId;
    private Integer noloadingTime;
    private Integer workingTime;
    private Double noloadingPower;
    private Double workingPower;
    private String status;
    private Date date;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;
    private String remark;

    public DataManage() {
    }

    public DataManage(int id) {
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


    @ManyToOne(targetEntity = Work.class)
    @JoinColumn(name = "work_id")
    @Fetch(FetchMode.SELECT)
    public Work getWork() {
        return work;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    @Basic
    @Column(name = "task_id")
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
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
    @Column(name = "noloading_power")
    public Double getNoloadingPower() {
        return noloadingPower;
    }

    public void setNoloadingPower(Double noloadingPower) {
        this.noloadingPower = noloadingPower;
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

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataManage that = (DataManage) o;
        return id == that.id &&
                Objects.equals(work, that.work) &&
                Objects.equals(noloadingTime, that.noloadingTime) &&
                Objects.equals(workingTime, that.workingTime) &&
                Objects.equals(noloadingPower, that.noloadingPower) &&
                Objects.equals(workingPower, that.workingPower) &&
                Objects.equals(status, that.status) &&
                Objects.equals(date, that.date) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, work, noloadingTime, workingTime, noloadingPower, workingPower, status, date, createTime, updateTime, remark);
    }

    @Override
    public String toString() {
        return "DataManage{" +
                "id=" + id +
                ", work=" + work +
                ", noloadingTime=" + noloadingTime +
                ", workingTime=" + workingTime +
                ", noloadingPower=" + noloadingPower +
                ", workingPower=" + workingPower +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", remark='" + remark + '\'' +
                '}';
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataManage that = (DataManage) o;
        return id == that.id &&
                Objects.equals(work, that.work) &&
                Objects.equals(taskId, that.taskId) &&
                Objects.equals(personId, that.personId) &&
                Objects.equals(noloadingTime, that.noloadingTime) &&
                Objects.equals(workingTime, that.workingTime) &&
                Objects.equals(noloadingPower, that.noloadingPower) &&
                Objects.equals(workingPower, that.workingPower) &&
                Objects.equals(status, that.status) &&
                Objects.equals(date, that.date) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, work, taskId, personId, noloadingTime, workingTime, noloadingPower, workingPower, status, date, createTime, updateTime, remark);
    }

    @Override
    public String toString() {
        return "DataManage{" +
                "id=" + id +
                ", work=" + work +
                ", taskId=" + taskId +
                ", personId=" + personId +
                ", noloadingTime=" + noloadingTime +
                ", workingTime=" + workingTime +
                ", noloadingPower=" + noloadingPower +
                ", workingPower=" + workingPower +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", remark='" + remark + '\'' +
                '}';
    }
}
