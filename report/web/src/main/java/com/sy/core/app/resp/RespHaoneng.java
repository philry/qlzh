package com.sy.core.app.resp;

import java.io.Serializable;
import java.util.List;

public class RespHaoneng extends Resp implements Serializable {

	private static final long serialVersionUID = 1L;

	private String zaigsj;
	private String gongzsj;
	private String gongxiao;
	private String huanbi;
	private String xmmc;
	private String haoneng;
	private String zgrs;//在岗人数
	private String gh;
	List<DataInfo> DataInfos ;

	public String getZaigsj() {
		return zaigsj;
	}

	public void setZaigsj(String zaigsj) {
		this.zaigsj = zaigsj;
	}

	public String getGongzsj() {
		return gongzsj;
	}

	public void setGongzsj(String gongzsj) {
		this.gongzsj = gongzsj;
	}

	public String getGongxiao() {
		return gongxiao;
	}

	public void setGongxiao(String gongxiao) {
		this.gongxiao = gongxiao;
	}

	public String getHuanbi() {
		return huanbi;
	}

	public void setHuanbi(String huanbi) {
		this.huanbi = huanbi;
	}

	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}

	public String getHaoneng() {
		return haoneng;
	}

	public void setHaoneng(String haoneng) {
		this.haoneng = haoneng;
	}

	public List<DataInfo> getDataInfos() {
		return DataInfos;
	}

	public void setDataInfos(List<DataInfo> dataInfos) {
		DataInfos = dataInfos;
	}
	
	public String getZgrs() {
		return zgrs;
	}

	public void setZgrs(String zgrs) {
		this.zgrs = zgrs;
	}

	public String getGh() {
		return gh;
	}

	public void setGh(String gh) {
		this.gh = gh;
	}
}
