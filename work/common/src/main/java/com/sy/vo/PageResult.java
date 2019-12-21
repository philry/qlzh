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
	
	private int msg;

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

	public int getMsg() {
		return msg;
	}

	public void setMsg(int msg) {
		this.msg = msg;
	}
	
	public static PageResult getPageResult(List<?> list,Long total) {
		PageResult pageResult = new PageResult();
		pageResult.setCode(HttpStatusConstant.SUCCESS);
		pageResult.setRows(list);
		if(total==null) {
			total=(long) list.size();
		}
		pageResult.setTotal(total);
		return pageResult;
	}
	
	public static PageResult getPageResult(List<?> list) {
		PageResult pageResult = new PageResult();
		if(list!=null&list.size()>0) {
			pageResult.setCode(HttpStatusConstant.SUCCESS);
			pageResult.setRows(list);
			pageResult.setTotal(list.size());
			return pageResult;
			
		}else {
			pageResult.setCode(HttpStatusConstant.FAIL);
			return pageResult;
		}
	}
}
