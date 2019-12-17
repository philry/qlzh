package com.sy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
public class Task {
    private int id;
    private String workCode;
    private Integer pid;
    private String projectName;
    private Double count;
    private Integer deptId;
    private Date beginTime;
    private Date endTime;
    private Date actualBeginTime;
    private Date actualEndTime;
    private String note;
    private Integer checker;
    private String checkingStatus;
    private Integer personId;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;
    private String remark;

    private List<Task> tasks;

    public Task() {
    }

    public Task(int id) {
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
    @Column(name = "work_code")
    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
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
    @Column(name = "project_name")
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Basic
    @Column(name = "count")
    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
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
    @Column(name = "begin_time")
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    @Basic
    @Column(name = "end_time")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "actual_begin_time")
    public Date getActualBeginTime() {
        return actualBeginTime;
    }

    public void setActualBeginTime(Date actualBeginTime) {
        this.actualBeginTime = actualBeginTime;
    }

    @Basic
    @Column(name = "actual_end_time")
    public Date getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(Date actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    @Basic
    @Column(name = "note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Basic
    @Column(name = "checker")
    public Integer getChecker() {
        return checker;
    }

    public void setChecker(Integer checker) {
        this.checker = checker;
    }

    @Basic
    @Column(name = "checking_status")
    public String getCheckingStatus() {
        return checkingStatus;
    }

    public void setCheckingStatus(String checkingStatus) {
        this.checkingStatus = checkingStatus;
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
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                Objects.equals(workCode, task.workCode) &&
                Objects.equals(pid, task.pid) &&
                Objects.equals(projectName, task.projectName) &&
                Objects.equals(count, task.count) &&
                Objects.equals(deptId, task.deptId) &&
                Objects.equals(beginTime, task.beginTime) &&
                Objects.equals(endTime, task.endTime) &&
                Objects.equals(actualBeginTime, task.actualBeginTime) &&
                Objects.equals(actualEndTime, task.actualEndTime) &&
                Objects.equals(note, task.note) &&
                Objects.equals(checker, task.checker) &&
                Objects.equals(checkingStatus, task.checkingStatus) &&
                Objects.equals(personId, task.personId) &&
                Objects.equals(status, task.status) &&
                Objects.equals(createTime, task.createTime) &&
                Objects.equals(updateTime, task.updateTime) &&
                Objects.equals(remark, task.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workCode, pid, projectName, count, deptId, beginTime, endTime, actualBeginTime, actualEndTime, note, checker, checkingStatus, personId, status, createTime, updateTime, remark);
    }
}
