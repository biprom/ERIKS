package com.biprom.eriks.telem.consumer.service;

import com.biprom.eriks.telem.consumer.model.SensorReading;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Kristof
 *         Created on 14/10/16.
 */
@Component
public class SensorReadingsListener {

	@KafkaListener(topics = "eriks")
	public void listener(SensorReading reading) {
		System.out.println(reading.getValue());
	}
}
