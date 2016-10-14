package com.biprom.eriks.telem.consumer.util.serde;

import com.biprom.eriks.telem.consumer.model.SensorReading;

/**
 * @author Kristof
 *         Created on 14/10/16.
 */
public class MeasurementDeserializer extends JsonDeserializer<SensorReading> {

	public MeasurementDeserializer() {
		super(SensorReading.class);
	}
}
