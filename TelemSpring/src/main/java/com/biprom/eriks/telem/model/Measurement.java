package com.biprom.eriks.telem.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
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
	private String id;

	@Indexed
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private Date d;

	@Indexed
	private MeasurementType t;

	private Double m;

	public Date getD() {
		return d;
	}

	public void setD(Date d) {
		this.d = d;
	}

	public MeasurementType getT() {
		return t;
	}

	public void setT(MeasurementType t) {
		this.t = t;
	}

	public Double getM() {
		return m;
	}

	public void setM(Double m) {
		this.m = m;
	}
}
