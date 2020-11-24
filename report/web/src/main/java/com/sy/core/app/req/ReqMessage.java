package com.sy.core.app.req;

import java.io.Serializable;

public class ReqMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userId;

	private String xxid;

	private String xxzt;

	private String beginTime;
	private String endTime;

	public String getXxid() {
		return xxid;
	}

	public void setXxid(String xxid) {
		this.xxid = xxid;
	}

	public String getXxzt() {
		return xxzt;
	}

	public void setXxzt(String xxzt) {
		this.xxzt = xxzt;
	}

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

	@Override
	public String toString() {
		return "ReqMessage [userId=" + userId + ", xxid=" + xxid + ", xxzt=" + xxzt + ", beginTime=" + beginTime
				+ ", endTime=" + endTime + "]";
	}

	
}
