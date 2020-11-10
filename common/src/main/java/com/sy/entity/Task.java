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
    private String number;
    private Integer pid;
    private String projectName;
    private Double count;
    private Integer deptId;
    @Transient
    private Dept dept;
    private Date beginTime;
    private Date endTime;
    private Date actualBeginTime;
    private Date actualEndTime;
    private String note;
    private Integer checker;
    @Transient
    private Person person;
    private String checkingStatus;
    private Integer personId;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;
    private String remark;
    @Transient
    private List<Task> sTasks;
    private String process;
    @Transient
    private Person workingPerson;//任务分解给哪个焊工了

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
    @Column(name = "number")
    public String getNumber() { return number; }

    public void setNumber(String number) { this.number = number; }

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

    @Basic
    @Column(name = "process")
    public String getProcess() { return process; }

    public void setProcess(String process) { this.process = process; }

    @Transient
    public List<Task> getsTasks() {
        return sTasks;
    }

    public void setsTasks(List<Task> sTasks) {
        this.sTasks = sTasks;
    }

    @Transient
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Transient
    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    @Transient
    public Person getWorkingPerson() {
        return workingPerson;
    }

    public void setWorkingPerson(Person workingPerson) {
        this.workingPerson = workingPerson;
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
                Objects.equals(remark, task.remark) &&
                Objects.equals(process, task.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workCode, pid, projectName, count, deptId, beginTime, endTime, actualBeginTime, actualEndTime, note, checker, checkingStatus, personId, status, createTime, updateTime, remark, process);
    }

	@Override
	public String toString() {
		return "Task [id=" + id + ", workCode=" + workCode + ", pid=" + pid + ", projectName=" + projectName
				+ ", count=" + count + ", deptId=" + deptId + ", beginTime=" + beginTime + ", endTime=" + endTime
				+ ", actualBeginTime=" + actualBeginTime + ", actualEndTime=" + actualEndTime + ", note=" + note
				+ ", checker=" + checker + ", checkingStatus=" + checkingStatus + ", personId=" + personId + ", status="
				+ status + ", createTime=" + createTime + ", updateTime=" + updateTime + ", remark=" + remark + ", " +
                "process=" + process + "]";
	}
}
