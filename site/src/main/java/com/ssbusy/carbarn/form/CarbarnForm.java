package com.ssbusy.carbarn.form;

import java.io.Serializable;
import java.util.List;

import com.ssbusy.core.carbarn.domain.Carbarn;

/**
 * 
 * @author song
 *
 */
public class CarbarnForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int status;
	private String errorMessage;
	private List<Carbarn> data;
	public int getStatus() {
		return status;
	}
	public CarbarnForm(int status, String errorMessage, List<Carbarn> data) {
		super();
		this.status = status;
		this.errorMessage = errorMessage;
		this.data = data;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public List<Carbarn> getData() {
		return data;
	}
	public void setData(List<Carbarn> data) {
		this.data = data;
	}
}
