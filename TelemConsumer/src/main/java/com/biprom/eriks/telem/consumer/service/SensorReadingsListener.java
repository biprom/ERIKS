package com.biprom.eriks.telem.consumer.service;

import com.biprom.eriks.telem.consumer.dao.SensorReadingRepository;
import com.biprom.eriks.telem.consumer.model.SensorReading;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Kristof
 *         Created on 14/10/16.
 */
@Component
public class SensorReadingsListener {

	@Autowired
	SensorReadingRepository sensorReadingRepository;

	@KafkaListener(topics = "eriks")
	public void listener(SensorReading reading) {
		sensorReadingRepository.save(reading);
	}
}
