package com.sy.entity;



import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class ProcessRun
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long tableId;

    private String role;

    private String people;

    private String operation;

    private String content;

    private String stepLevel;
 
    private String status;

    private Date createTime;

    private Date updateTime;
 
    private String result;

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setTableId(Long tableId)
    {
        this.tableId = tableId;
    }

    public Long getTableId()
    {
        return tableId;
    }
    public void setRole(String role)
    {
        this.role = role;
    }

    public String getRole()
    {
        return role;
    }
    public void setPeople(String people)
    {
        this.people = people;
    }

    public String getPeople()
    {
        return people;
    }
    public void setOperation(String operation)
    {
        this.operation = operation;
    }

    public String getOperation()
    {
        return operation;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setStepLevel(String stepLevel)
    {
        this.stepLevel = stepLevel;
    }

    public String getStepLevel()
    {
        return stepLevel;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setResult(String result)
    {
        this.result = result;
    }

    public String getResult()
    {
        return result;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tableId", getTableId())
            .append("role", getRole())
            .append("people", getPeople())
            .append("operation", getOperation())
            .append("content", getContent())
            .append("stepLevel", getStepLevel())
            .append("status", getStatus())
            .append("result", getResult())
            .toString();
    }
}
