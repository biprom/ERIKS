package com.biprom.eriks.telem.service.impl;

import com.biprom.eriks.telem.anot.EnableKafkaSync;
import com.biprom.eriks.telem.dao.MeasurementRepository;
import com.biprom.eriks.telem.model.Measurement;
import com.biprom.eriks.telem.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Kristof
 *         Created on 27/09/16.
 */
@Service("measurementService")
public class MeasurementServiceImpl implements MeasurementService {

	private static final int DAYS_TO_KEEP = 4;

	@Autowired
	MeasurementRepository measurementRepository;


	@Override
	@EnableKafkaSync
	public Measurement store(Measurement m) {
		measurementRepository.save(m);
		return m;
	}


	/**
	 * This implementation provides the measurement to push to Kafka
	 *
	 * @param m
	 *
	 * @return the Measurement pushed to Kafka.
	 */
	@EnableKafkaSync
	public Measurement sync(Measurement m) {
		return m;
	}


	@Override
	public List<Measurement> findUnsynchedMeasurements() {
		return measurementRepository.findUnsynched();
	}

	@Override
	public void deleteOldMeasurements() {
		final LocalDate before = LocalDate.now().minusDays(DAYS_TO_KEEP);
		final List<Measurement> ms = measurementRepository.findByTimeBeforeAndSynched(before.atStartOfDay(), Boolean.TRUE);
		for (Measurement m : ms) {
			measurementRepository.delete(m);
		}
	}


}
