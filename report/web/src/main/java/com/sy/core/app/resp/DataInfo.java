package com.sy.core.app.resp;

import java.io.Serializable;

public class DataInfo extends Resp implements Serializable {

	private static final long serialVersionUID = 1L;

	private String zaigsj;
	private String gongzsj;
	private String gongxiao;
	private String huanbi;
	private String mingc;
	private String ryxm;
	private String haoneng;
	private String xmmc;
	private String bianhao;
	private String rybh;
	private String riqi;
	private String gh ;
	
	public String getGh() {
		return gh;
	}

	public void setGh(String gh) {
		this.gh = gh;
	}

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getXmmc() {
		return xmmc;
	}

	public void setXmmc(String xmmc) {
		this.xmmc = xmmc;
	}
	
	
	public String getRiqi() {
		return riqi;
	}

	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	public String getBianhao() {
		return bianhao;
	}

	public void setBianhao(String bianhao) {
		this.bianhao = bianhao;
	}

	public String getRybh() {
		return rybh;
	}

	public void setRybh(String rybh) {
		this.rybh = rybh;
	}

	@Override
	public String toString() {
		return "DataInfo [zaigsj=" + zaigsj + ", gongzsj=" + gongzsj + ", gongxiao=" + gongxiao + ", huanbi=" + huanbi
				+ ", mingc=" + mingc + ", ryxm=" + ryxm + ", haoneng=" + haoneng + ", xmmc=" + xmmc + "]";
	}

}
