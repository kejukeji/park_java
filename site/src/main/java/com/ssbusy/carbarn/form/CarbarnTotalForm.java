package com.ssbusy.carbarn.form;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author song
 * 
 */
public class CarbarnTotalForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String status;
	private int size;
	private int total;
	private List<CarbarnForm> contents;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<CarbarnForm> getContents() {
		return contents;
	}

	public void setContents(List<CarbarnForm> contents) {
		this.contents = contents;
	}
}
