package com.biprom.eriks.telem.tasks;

import com.biprom.eriks.telem.model.Measurement;
import com.biprom.eriks.telem.service.MeasurementService;
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
	MeasurementService measurementService;


	/**
	 * Once every our all non synched measurements are fetched from the database and resend through Kafka.
	 */
	@Scheduled(cron = "*/30 * * * * *")
	public void retrySync() {
		for (Measurement m : measurementService.findUnsynchedMeasurements()) {
			measurementService.sync(m);
		}
	}
}
