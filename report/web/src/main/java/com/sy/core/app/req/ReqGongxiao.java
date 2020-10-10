package com.sy.core.app.req;

import java.io.Serializable;

public class ReqGongxiao implements Serializable {
	private static final long serialVersionUID = 1L;
	private String beginTime;
	private String endTime;
	private String type;// 0-总功效，1-车间，2-工程队，3-班组，4-人员姓名，5-人员工号
	private String cjmc;
	private String cjbh;// 车间编号
	private String gcdmc;
	private String gcdbh;// 大班组编号
	private String bzmc;
	private String ryxm;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCjmc() {
		return cjmc;
	}

	public void setCjmc(String cjmc) {
		this.cjmc = cjmc;
	}

	public String getGcdmc() {
		return gcdmc;
	}

	public void setGcdmc(String gcdmc) {
		this.gcdmc = gcdmc;
	}

	public String getBzmc() {
		return bzmc;
	}

	public void setBzmc(String bzmc) {
		this.bzmc = bzmc;
	}

	public String getRyxm() {
		return ryxm;
	}

	public void setRyxm(String ryxm) {
		this.ryxm = ryxm;
	}

	public String getGcdbh() {
		return gcdbh;
	}

	public void setGcdbh(String gcdbh) {
		this.gcdbh = gcdbh;
	}

	public String getCjbh() {
		return cjbh;
	}

	public void setCjbh(String cjbh) {
		this.cjbh = cjbh;
	}

	@Override
	public String toString() {
		return "ReqGongxiao [beginTime=" + beginTime + ", endTime=" + endTime + ", type=" + type + ", cjmc=" + cjmc
				+ ", cjbh=" + cjbh + ", gcdmc=" + gcdmc + ", gcdbh=" + gcdbh + ", bzmc=" + bzmc + ", ryxm=" + ryxm
				+ "]";
	}
	
}
