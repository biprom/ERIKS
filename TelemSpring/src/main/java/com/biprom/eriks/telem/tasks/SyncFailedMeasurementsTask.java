package com.biprom.eriks.telem.tasks;

import com.biprom.eriks.telem.model.SensorReading;
import com.biprom.eriks.telem.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Kristof
 *         Created on 09/10/16.
 */
@Component
public class SyncFailedMeasurementsTask {

	@Autowired
	SensorService sensorService;


	/**
	 * Once every our all non synched measurements are fetched from the database and resend through Kafka.
	 */
	@Scheduled(cron = "*/30 * * * * *")
	public void retrySync() {
		for (SensorReading m : sensorService.findUnsynchedMeasurements()) {
			sensorService.sync(m);
		}
	}
}
