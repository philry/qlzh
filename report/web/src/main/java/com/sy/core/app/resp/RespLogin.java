package com.sy.core.app.resp;

import java.io.Serializable;

public class RespLogin extends Resp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private SysUserVO userData;

	public SysUserVO getUserData() {
		return userData;
	}

	public void setUserData(SysUserVO userData) {
		this.userData = userData;
	}
}
