package com.sy.core.app.req;

import java.io.Serializable;

public class ReqLogin implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String companyName;
	
	private String userName;
	
	private String password;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
