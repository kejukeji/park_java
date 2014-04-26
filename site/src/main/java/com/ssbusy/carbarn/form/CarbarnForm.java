package com.ssbusy.carbarn.form;

import java.io.Serializable;

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

	private String address;
	private String city;
	private String create_time;
	private String district;
	private String geotable_id;
	private String icon_style_id;
	private double location[];
	private String tags;
	private String distance;
	private String modify_time;
	private String province;
	private String title;
	private int total;
	private int emptyLocation;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getGeotable_id() {
		return geotable_id;
	}
	public void setGeotable_id(String geotable_id) {
		this.geotable_id = geotable_id;
	}
	public String getIcon_style_id() {
		return icon_style_id;
	}
	public void setIcon_style_id(String icon_style_id) {
		this.icon_style_id = icon_style_id;
	}
	public double[] getLocation() {
		return location;
	}
	public void setLocation(double[] location) {
		this.location = location;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public String getModify_time() {
		return modify_time;
	}
	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getEmptyLocation() {
		return emptyLocation;
	}
	public void setEmptyLocation(int emptyLocation) {
		this.emptyLocation = emptyLocation;
	}
}
