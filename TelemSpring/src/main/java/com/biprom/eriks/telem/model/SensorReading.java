package com.biprom.eriks.telem.model;

import com.google.gson.annotations.Expose;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Kristof
 *         Created on 06/10/16.
 */
@Document(collection = "sensors")
public class SensorReading {

	@Id
	@Expose
	private String id;

	@Indexed
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Field("d")
	@Expose
	private Date time;

	@Indexed
	@Field("t")
	@Expose
	private String type;

	@Field("v")
	@Expose
	private Double value;

	@Field("s")
	private Boolean synched;

	@Transient
	@Expose
	private String source;

	public SensorReading(Date d, String t, Double m) {
		this.time = d;
		this.type = t;
		this.value = m;
	}

	public SensorReading() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Boolean getSynched() {
		return synched;
	}

	public void setSynched(Boolean synched) {
		this.synched = synched;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
