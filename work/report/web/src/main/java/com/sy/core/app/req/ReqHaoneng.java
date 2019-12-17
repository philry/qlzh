package com.sy.core.app.req;

import java.io.Serializable;

public class ReqHaoneng implements Serializable {
	private static final long serialVersionUID = 1L;

	private String beginTime;
	private String endTime;
	private String queryType;// 1-所有工号，2-按工号明细
	private String gh;// 工号

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

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getGh() {
		return gh;
	}

	public void setGh(String gh) {
		this.gh = gh;
	}

	@Override
	public String toString() {
		return "ReqHaoneng [beginTime=" + beginTime + ", endTime=" + endTime + ", queryType=" + queryType + ", gh=" + gh
				+ "]";
	}
}
