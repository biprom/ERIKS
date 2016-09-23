package com.biprom.eriks.telem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Document
public class MeasuredValues {


	@Id
	private String id;

	@Indexed
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date d;

	private Double[] analogValuesRead;

	private Boolean[] binValuesRead;

	public MeasuredValues() {
	}

	public MeasuredValues(Date date, Double[] anaVal, Boolean[] binVal) {
		this.d = date;
		this.analogValuesRead = anaVal;
		this.binValuesRead = binVal;

	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getD() {
		return d;
	}


	public void setD(Date d) {
		this.d = d;
	}


	public Double[] getAnalogValuesRead() {
		return analogValuesRead;
	}


	public void setAnalogValuesRead(Double[] analogValuesRead) {
		this.analogValuesRead = analogValuesRead;
	}


	public Boolean[] getBinValuesRead() {
		return binValuesRead;
	}


	public void setBinValuesRead(Boolean[] binValuesRead) {
		this.binValuesRead = binValuesRead;
	}


}
