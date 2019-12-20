package com.sy.vo;

import java.io.Serializable;

public class MessageNumber implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4823951749023742527L;

	private Integer code;
	
	private String msg;
	
	// 审核数
	private Integer check;
	
	// 代办数
	private Integer todo;
	
	// 任务数
	private Integer mission;
	
	// 消息数
	private Integer warn;
	
	// 总数
	private Integer total;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getCheck() {
		return check;
	}

	public void setCheck(Integer check) {
		this.check = check;
	}

	public Integer getTodo() {
		return todo;
	}

	public void setTodo(Integer todo) {
		this.todo = todo;
	}

	public Integer getMission() {
		return mission;
	}

	public void setMission(Integer mission) {
		this.mission = mission;
	}

	public Integer getWarn() {
		return warn;
	}

	public void setWarn(Integer warn) {
		this.warn = warn;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}
	
	
}
