package com.sy.vo;

import java.io.Serializable;
import java.util.List;

import com.sy.constant.HttpStatusConstant;

public class PageResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1896113606712284836L;

	private long total;
	
	private List<?> rows;
	
	private int code;
	
	private String msg;

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public static PageResult getPageResult(List<?> list,Long total) {
		PageResult pageResult = new PageResult();
		if(list!=null&&list.size()>0) {
			pageResult.setCode(HttpStatusConstant.SUCCESS);
			pageResult.setRows(list);
			if(total==null) {
				total=(long) list.size();
			}
			pageResult.setTotal(total);
		}else {
			pageResult.setMsg("暂无查询结果");
			pageResult.setCode(HttpStatusConstant.FAIL);
		}
		return pageResult;
	}
	
	public static PageResult getPageResult(List<?> list) {
		PageResult pageResult = new PageResult();
		if(list==null){
			pageResult.setMsg("暂无查询结果");
			pageResult.setCode(HttpStatusConstant.FAIL);
		}else {
			if(list.size()>0){
				pageResult.setCode(HttpStatusConstant.SUCCESS);
				pageResult.setRows(list);
				pageResult.setTotal(list.size());
			}
		}
		return pageResult;
	}
}
