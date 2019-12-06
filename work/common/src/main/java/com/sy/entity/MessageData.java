package com.sy.entity;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "message_data", schema = "qlzh", catalog = "")
public class MessageData {
    private int id;
    private Integer sendId;
    private Integer accpetId;
    private String context;
    private Integer messgaeType;
    private String status;
    private Timestamp createTime;
    private Date updateTime;
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
    @Column(name = "send_id")
    public Integer getSendId() {
        return sendId;
    }

    public void setSendId(Integer sendId) {
        this.sendId = sendId;
    }

    @Basic
    @Column(name = "accpet_id")
    public Integer getAccpetId() {
        return accpetId;
    }

    public void setAccpetId(Integer accpetId) {
        this.accpetId = accpetId;
    }

    @Basic
    @Column(name = "context")
    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Basic
    @Column(name = "messgae_type")
    public Integer getMessgaeType() {
        return messgaeType;
    }

    public void setMessgaeType(Integer messgaeType) {
        this.messgaeType = messgaeType;
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
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
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
        MessageData that = (MessageData) o;
        return id == that.id &&
                Objects.equals(sendId, that.sendId) &&
                Objects.equals(accpetId, that.accpetId) &&
                Objects.equals(context, that.context) &&
                Objects.equals(messgaeType, that.messgaeType) &&
                Objects.equals(status, that.status) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime) &&
                Objects.equals(remark, that.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sendId, accpetId, context, messgaeType, status, createTime, updateTime, remark);
    }
}
