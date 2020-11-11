package com.sy.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class TaskData {
    private int id;
    private Integer taskId;
    private String workCode;
    private String number;
    private Integer pid;
    private String projectName;
    private Double count;
    private Integer time;
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
    private Date date;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Timestamp updateTime;
    private String remark;
    @Transient
    private List<TaskData> sTasks;
    private String process;
    @Transient
    private Person workingPerson;//任务分解给哪个焊工了

    public TaskData() {
    }

    public TaskData(int id) {
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
    @Column(name = "task_id")
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
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
    @Column(name = "time")
    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
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

    @Basic
    @Column(name = "process")
    public String getProcess() { return process; }

    public void setProcess(String process) { this.process = process; }

    @Transient
    public List<TaskData> getsTasks() {
        return sTasks;
    }

    public void setsTasks(List<TaskData> sTasks) {
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

	/*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskData taskData = (TaskData) o;
        return id == taskData.id &&
                Objects.equals(workCode, taskData.workCode) &&
                Objects.equals(pid, taskData.pid) &&
                Objects.equals(projectName, taskData.projectName) &&
                Objects.equals(count, taskData.count) &&
                Objects.equals(deptId, taskData.deptId) &&
                Objects.equals(beginTime, taskData.beginTime) &&
                Objects.equals(endTime, taskData.endTime) &&
                Objects.equals(actualBeginTime, taskData.actualBeginTime) &&
                Objects.equals(actualEndTime, taskData.actualEndTime) &&
                Objects.equals(note, taskData.note) &&
                Objects.equals(checker, taskData.checker) &&
                Objects.equals(checkingStatus, taskData.checkingStatus) &&
                Objects.equals(personId, taskData.personId) &&
                Objects.equals(status, taskData.status) &&
                Objects.equals(createTime, taskData.createTime) &&
                Objects.equals(updateTime, taskData.updateTime) &&
                Objects.equals(remark, taskData.remark) &&
                Objects.equals(process, taskData.process);
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
	}*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskData taskData = (TaskData) o;
        return id == taskData.id &&
                Objects.equals(taskId, taskData.taskId) &&
                Objects.equals(workCode, taskData.workCode) &&
                Objects.equals(number, taskData.number) &&
                Objects.equals(pid, taskData.pid) &&
                Objects.equals(projectName, taskData.projectName) &&
                Objects.equals(count, taskData.count) &&
                Objects.equals(time, taskData.time) &&
                Objects.equals(deptId, taskData.deptId) &&
                Objects.equals(beginTime, taskData.beginTime) &&
                Objects.equals(endTime, taskData.endTime) &&
                Objects.equals(actualBeginTime, taskData.actualBeginTime) &&
                Objects.equals(actualEndTime, taskData.actualEndTime) &&
                Objects.equals(note, taskData.note) &&
                Objects.equals(checker, taskData.checker) &&
                Objects.equals(checkingStatus, taskData.checkingStatus) &&
                Objects.equals(personId, taskData.personId) &&
                Objects.equals(status, taskData.status) &&
                Objects.equals(date, taskData.date) &&
                Objects.equals(createTime, taskData.createTime) &&
                Objects.equals(updateTime, taskData.updateTime) &&
                Objects.equals(remark, taskData.remark) &&
                Objects.equals(process, taskData.process);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskId, workCode, number, pid, projectName, count, time, deptId, beginTime, endTime, actualBeginTime, actualEndTime, note, checker, checkingStatus, personId, status, date, createTime, updateTime, remark, process);
    }

    @Override
    public String toString() {
        return "TaskData{" +
                "id=" + id +
                ", taskId=" + taskId +
                ", workCode='" + workCode + '\'' +
                ", number='" + number + '\'' +
                ", pid=" + pid +
                ", projectName='" + projectName + '\'' +
                ", count=" + count +
                ", time=" + time +
                ", deptId=" + deptId +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", actualBeginTime=" + actualBeginTime +
                ", actualEndTime=" + actualEndTime +
                ", note='" + note + '\'' +
                ", checker=" + checker +
                ", checkingStatus='" + checkingStatus + '\'' +
                ", personId=" + personId +
                ", status='" + status + '\'' +
                ", date=" + date +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", remark='" + remark + '\'' +
                ", process='" + process + '\'' +
                '}';
    }

}
