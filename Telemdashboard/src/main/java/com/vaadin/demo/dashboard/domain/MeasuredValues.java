package com.vaadin.demo.dashboard.domain;

import java.util.Date;

public class MeasuredValues {
	
	private String title;
	private Date timestamp;
	private Double value;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double val) {
		this.value = val;
	}

}
