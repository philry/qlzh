package com.sy.entity;



import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class ProcessCondition
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String tableType;

    private String conditionField;

    private String moreValue;

    private Long moreOperationId;

    private String lessValue;

    private Long lessOperationId;

    private String equalValue;

    private Long equalOperationId;

    private Date createTime;

    private Date updateTime;

    private String status;

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
    public void setTableType(String tableType)
    {
        this.tableType = tableType;
    }

    public String getTableType()
    {
        return tableType;
    }
    public void setConditionField(String conditionField)
    {
        this.conditionField = conditionField;
    }

    public String getConditionField()
    {
        return conditionField;
    }
    public void setMoreValue(String moreValue)
    {
        this.moreValue = moreValue;
    }

    public String getMoreValue()
    {
        return moreValue;
    }
    public void setMoreOperationId(Long moreOperationId)
    {
        this.moreOperationId = moreOperationId;
    }

    public Long getMoreOperationId()
    {
        return moreOperationId;
    }
    public void setLessValue(String lessValue)
    {
        this.lessValue = lessValue;
    }

    public String getLessValue()
    {
        return lessValue;
    }
    public void setLessOperationId(Long lessOperationId)
    {
        this.lessOperationId = lessOperationId;
    }

    public Long getLessOperationId()
    {
        return lessOperationId;
    }
    public void setEqualValue(String equalValue)
    {
        this.equalValue = equalValue;
    }

    public String getEqualValue()
    {
        return equalValue;
    }
    public void setEqualOperationId(Long equalOperationId)
    {
        this.equalOperationId = equalOperationId;
    }

    public Long getEqualOperationId()
    {
        return equalOperationId;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
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
            .append("tableType", getTableType())
            .append("conditionField", getConditionField())
            .append("moreValue", getMoreValue())
            .append("moreOperationId", getMoreOperationId())
            .append("lessValue", getLessValue())
            .append("lessOperationId", getLessOperationId())
            .append("equalValue", getEqualValue())
            .append("equalOperationId", getEqualOperationId())
            .append("status", getStatus())
            .toString();
    }
}
