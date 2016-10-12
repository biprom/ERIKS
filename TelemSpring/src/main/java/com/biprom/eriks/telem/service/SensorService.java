package com.biprom.eriks.telem.service;

import com.biprom.eriks.telem.model.SensorReading;

import java.util.List;

/**
 * @author Kristof
 *         Created on 27/09/16.
 */
public interface SensorService {

	SensorReading store(SensorReading m);

	SensorReading sync(SensorReading m);

	List<SensorReading> findUnsynchedMeasurements();

	void deleteOldMeasurements();
}
