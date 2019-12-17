package com.sy.core.app.resp;

import java.io.Serializable;

public class OnLineUsersVO extends Resp implements Serializable {

	private static final long serialVersionUID = 1L;

	private String count;

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "OnLineUsersVO [count=" + count + "]";
	}

}
