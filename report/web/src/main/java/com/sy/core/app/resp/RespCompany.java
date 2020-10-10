package com.sy.core.app.resp;

import java.io.Serializable;

public class RespCompany extends Resp implements Serializable {
	private static final long serialVersionUID = 1L;
	private String companyName;
	private String serverIp;
	private String serverPort;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	@Override
	public String toString() {
		return "RespCompany [companyName=" + companyName + ", serverIp=" + serverIp + ", serverPort=" + serverPort
				+ "]";
	}

}
