package com.sy.core.app.resp;

import java.io.Serializable;
import java.util.List;

public class RespDay7 extends Resp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<DataInfo> dataInfos ;

	public List<DataInfo> getDataInfos() {
		return dataInfos;
	}

	public void setDataInfos(List<DataInfo> dataInfos) {
		this.dataInfos = dataInfos;
	}
	
	

}
