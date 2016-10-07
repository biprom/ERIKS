package com.biprom.eriks.telem.service.impl;

import com.biprom.eriks.telem.anot.EnableKafkaSync;
import com.biprom.eriks.telem.model.Measurement;
import com.biprom.eriks.telem.service.MeasurementService;
import org.springframework.stereotype.Service;

/**
 * @author Kristof
 *         Created on 27/09/16.
 */
@Service("measurementService")
public class MeasurementServiceImpl implements MeasurementService {


	@Override
	@EnableKafkaSync
	public Measurement store(Measurement m) {
		return m;
	}

}
