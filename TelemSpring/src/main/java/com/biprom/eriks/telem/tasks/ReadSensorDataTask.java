package com.biprom.eriks.telem.tasks;

import com.biprom.eriks.telem.model.SensorReading;
import com.biprom.eriks.telem.service.SensorService;
import io.mappedbus.MappedBusReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.EOFException;
import java.io.IOException;

/**
 * @author Kristof
 *         Created on 12/10/16.
 */
@Component
public class ReadSensorDataTask {


	private String sensorFile;

	private static final long BUFFER_SIZE = 500000L;

	@Autowired
	SensorService sensorService;

	MappedBusReader reader;

	public ReadSensorDataTask(@Value("${sensorfile}") String sensorFile) {
		// Change this to a memory file
		this.sensorFile = sensorFile;
		reader = new MappedBusReader(sensorFile, BUFFER_SIZE, 64);
		try {
			reader.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Scheduled(cron = "*/10 * * * * *")
	public void readSensorData() {
		try {
			while (reader.next()) {
				SensorReading reading = new SensorReading();
				reader.readMessage(reading);
				sensorService.store(reading);
			}
		} catch (EOFException e) {
			try {
				reader.close();
				reader = new MappedBusReader(sensorFile, 500000L, 64);
				reader.open();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		}


	}

}
