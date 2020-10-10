package com.sy.core.app.resp;

import java.io.Serializable;
import java.util.List;

public class RespGongxiao extends Resp implements Serializable {

	private static final long serialVersionUID = 1L;

	private String zaigsj;
	private String gongzsj;
	private String type;//0-总功效，1-车间，2-工程队，3-班组，4-人员姓名，5-人员工号
	private String gongxiao;
	private String huanbi;
	private String mingc;
	private String ryxm;
	private String haoneng;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getMingc() {
		return mingc;
	}
	public void setMingc(String mingc) {
		this.mingc = mingc;
	}
	public String getRyxm() {
		return ryxm;
	}
	public void setRyxm(String ryxm) {
		this.ryxm = ryxm;
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
	@Override
	public String toString() {
		return "RespGongxiao [zaigsj=" + zaigsj + ", gongzsj=" + gongzsj + ", type=" + type + ", gongxiao=" + gongxiao
				+ ", huanbi=" + huanbi + ", mingc=" + mingc + ", ryxm=" + ryxm + ", haoneng=" + haoneng + ", DataInfos="
				+ DataInfos + "]";
	}

}
