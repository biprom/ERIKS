package com.biprom.eriks.telem.service.impl;

import com.biprom.eriks.telem.anot.EnableKafkaSync;
import com.biprom.eriks.telem.dao.SensorReadingRepository;
import com.biprom.eriks.telem.model.SensorReading;
import com.biprom.eriks.telem.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Kristof
 *         Created on 27/09/16.
 */
@Service("sensorService")
public class SensorServiceImpl implements SensorService {

	private static final int DAYS_TO_KEEP = 4;

	@Autowired
	SensorReadingRepository sensorReadingRepository;


	@Override
	@EnableKafkaSync
	public SensorReading store(SensorReading m) {
		sensorReadingRepository.save(m);
		System.out.println("Saving sensor: " + m.getType() + " " + m.getValue());
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
	public SensorReading sync(SensorReading m) {
		return m;
	}


	@Override
	public List<SensorReading> findUnsynchedMeasurements() {
		return sensorReadingRepository.findUnsynched();
	}

	@Override
	public void deleteOldMeasurements() {
		final LocalDate before = LocalDate.now().minusDays(DAYS_TO_KEEP);
		final List<SensorReading> ms = sensorReadingRepository.findByTimeBeforeAndSynched(before.atStartOfDay(), Boolean.TRUE);
		for (SensorReading m : ms) {
			sensorReadingRepository.delete(m);
		}
	}


}
