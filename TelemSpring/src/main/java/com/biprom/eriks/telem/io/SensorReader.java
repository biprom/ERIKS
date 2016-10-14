package com.biprom.eriks.telem.io;

import com.biprom.eriks.telem.model.SensorReading;
import com.biprom.eriks.telem.service.SensorService;
import io.mappedbus.MappedBusReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Kristof
 *         Created on 12/10/16.
 */
@Component
public class SensorReader {

	@Value("${sensorfile}")
	private String sensorFile;

	@Autowired
	SensorService sensorService;


	@Scheduled(cron = "*/10 * * * * *")
	public void readSensorData() {
		// Change this to a memory file
		MappedBusReader reader = new MappedBusReader(sensorFile, 500000L, 64);
		try {
			reader.open();
			while (reader.next()) {
				SensorReading reading = new SensorReading();
				reader.readMessage(reading);
				sensorService.store(reading);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
