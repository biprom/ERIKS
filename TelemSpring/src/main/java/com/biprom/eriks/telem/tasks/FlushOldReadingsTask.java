package com.biprom.eriks.telem.tasks;

import com.biprom.eriks.telem.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Kristof
 *         Created on 09/10/16.
 */
@Component
public class FlushOldReadingsTask {

	@Autowired
	SensorService measurementService;

	@Scheduled(cron = "0 * 5 * * *")
	public void flushOldReadings() {
		measurementService.deleteOldMeasurements();
	}

}
