package com.sy.core.app.resp;

import java.io.Serializable;
import java.util.List;

public class RespMessage extends Resp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<MsgVO> msgs ;

	public List<MsgVO> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<MsgVO> msgs) {
		this.msgs = msgs;
	}

}
