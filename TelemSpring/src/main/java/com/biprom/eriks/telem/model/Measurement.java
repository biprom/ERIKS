package com.biprom.eriks.telem.model;

import com.google.gson.annotations.Expose;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Kristof
 *         Created on 06/10/16.
 */
@Document(collection = "measurement")
public class Measurement {

	public enum MeasurementType {
		OIL_TEMP_IN,                    // temperatuur olie ingang
		OIL_PRESSURE_IN,                // druk olie ingang
		OIL_TEMP_BEFORE_FILTERS,        // temperatuur olie voor filters
		FLOW_BEFORE_FILTERS,            // flowmeter voor filters
		OIL_PRESSURE_BEFORE_FILTERS,    // druk olie voor filters
		OIL_PRESSURE_AFTER_FILTERS,     // druk olie na filters
		FLOW_DRAIN,                     // flowmeter leegloop
		PARTICLE_COUNT,                 // deeltjesteller
		RELATIVE_HUMIDITY               // relatieve vochtigheid
	}

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
	private MeasurementType type;

	@Field("v")
	@Expose
	private Double value;

	@Field("s")
	private Boolean synched;

	public Measurement(Date d, MeasurementType t, Double m) {
		this.time = d;
		this.type = t;
		this.value = m;
	}

	public Measurement() {
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

	public MeasurementType getType() {
		return type;
	}

	public void setType(MeasurementType type) {
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
}
