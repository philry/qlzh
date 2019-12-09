package com.sy.core.app.req;

import java.io.Serializable;

/**
 * 开关机操作
 * @author administrator
 *
 */
public class ReqOper implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String taskId;
	
	private String userId;
	
	private String hanjibianhao;
	
	private String zhuangtoubianhao;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getHanjibianhao() {
		return hanjibianhao;
	}

	public void setHanjibianhao(String hanjibianhao) {
		this.hanjibianhao = hanjibianhao;
	}

	public String getZhuangtoubianhao() {
		return zhuangtoubianhao;
	}

	public void setZhuangtoubianhao(String zhuangtoubianhao) {
		this.zhuangtoubianhao = zhuangtoubianhao;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public String toString() {
		return "ReqOper [taskId=" + taskId + ", userId=" + userId + ", hanjibianhao=" + hanjibianhao
				+ ", zhuangtoubianhao=" + zhuangtoubianhao + "]";
	}
	
	
}
