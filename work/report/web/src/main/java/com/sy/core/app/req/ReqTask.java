package com.sy.core.app.req;

import java.io.Serializable;

public class ReqTask implements Serializable {
	private static final long serialVersionUID = 1L;
	private String beginTime;
	private String endTime;
	private String id;
	private String userId;
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ReqTask [beginTime=" + beginTime + ", endTime=" + endTime + ", id=" + id + ", userId=" + userId + "]";
	}


}
