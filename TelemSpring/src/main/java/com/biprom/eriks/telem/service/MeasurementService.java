package com.biprom.eriks.telem.service;

import com.biprom.eriks.telem.model.Measurement;

import java.util.List;

/**
 * @author Kristof
 *         Created on 27/09/16.
 */
public interface MeasurementService {

	Measurement store(Measurement m);

	Measurement sync(Measurement m);

	List<Measurement> findUnsynchedMeasurements();

	void deleteOldMeasurements();
}
