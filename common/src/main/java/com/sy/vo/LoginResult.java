package com.sy.vo;

import java.io.Serializable;

import com.sy.entity.Person;

import javax.servlet.http.HttpServletResponse;

public class LoginResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4610800903096815690L;

	private int code;
	
	private String message;
	
	private Person person;
	
	private Object obj;

	private HttpServletResponse response;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public HttpServletResponse getResponse() {return response;}

	public void setResponse(HttpServletResponse response) {this.response = response;}
}
