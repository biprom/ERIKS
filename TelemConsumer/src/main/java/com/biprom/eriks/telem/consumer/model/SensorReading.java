package com.biprom.eriks.telem.consumer.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Kristof
 *         Created on 14/10/16.
 */
@Document(collection = "sensors")
public class SensorReading {

	@Id
	private String id;

	@Indexed
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Field("d")
	private Date time;

	@Indexed
	@Field("t")
	private String type;

	@Field("v")
	private Double value;

	@Field("s")
	private String source;

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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
