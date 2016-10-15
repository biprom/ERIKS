package com.biprom.workcycle;

import io.mappedbus.MappedBusWriter;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Kristof
 *         Created on 12/10/16.
 */
public class Sensor extends Thread {


	private String sensorFile;

	/**
	 * Read sensor data every 10 seconds.
	 */
	private static final int SENSOR_FREQUENCY = 10 * 1000;

	private static final long BUFFER_SIZE = 500000L;

	public enum SensorReadingType {
		OIL_TEMP_IN,                    // temperatuur olie ingang
		OIL_PRESSURE_IN,                // druk olie ingang
		OIL_TEMP_BEFORE_FILTERS,        // temperatuur olie voor filters
		FLOW_BEFORE_FILTERS,            // flowmeter voor filters
		OIL_PRESSURE_BEFORE_FILTERS,    // druk olie voor filters
		OIL_PRESSURE_AFTER_FILTERS,     // druk olie na filters
		FLOW_DRAIN,                     // flowmeter leegloop
		PARTICLE_COUNT,                 // deeltjesteller
		RELATIVE_HUMIDITY               // relatieve vochtigheid
	}

	/**
	 * Reading out sensor data from this card
	 * TODO Probably not correct
	 */
//	ADC_PI_MCP analog_input_card_1 = new ADC_PI_MCP();

	/**
	 * Een memory mapped file voor communicatie tussen processen.
	 * Native linux feature, dus eigenlijk nog stabieler dan Sockets!!
	 */
	MappedBusWriter writer;

	public Sensor(String sensorFile) {
		this.sensorFile = sensorFile;
		writer = new MappedBusWriter(sensorFile, BUFFER_SIZE, 64, true);
		try {
			writer.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		long test = 0;
		while (!Thread.currentThread().isInterrupted()) {
			try {

				// TODO: BRAM, Check of de KLOK altijd JUIST staat op de raspberry. Anders zijn die metingen waardeloos!
				Date now = new Date();


				//TODO: BRAM, Zou verwachten hier doubles te krijgen, waarschijnlijk lees ik hier helemaal geen sensor data uit.
//				final long s0 = analog_input_card_1.read_raw(0X6C, 0, 0, 0, 0);
//				final long s1 = analog_input_card_1.read_raw(0X6C, 1, 0, 0, 0);
//				final long s2 = analog_input_card_1.read_raw(0X6C, 2, 0, 0, 0);
//				final long s3 = analog_input_card_1.read_raw(0X6C, 3, 0, 0, 0);
//				final long s4 = analog_input_card_1.read_raw(0X6D, 0, 0, 0, 0);
//				final long s5 = analog_input_card_1.read_raw(0X6D, 1, 0, 0, 0);
//				final long s6 = analog_input_card_1.read_raw(0X6D, 2, 0, 0, 0);
//				final long s7 = analog_input_card_1.read_raw(0X6D, 3, 0, 0, 0);


				// FF wat cijfertjes om te kunnen testen.
				final long s0 = test++;
				final long s1 = test++;
				final long s2 = test++;
				final long s3 = test++;
				final long s4 = test++;
				final long s5 = test++;
				final long s6 = test++;
				final long s7 = test++;


				// TODO: BRAM, ook dit is helemaal NIET juist hé. Hier koppel ik de uitgelezen waarde aan een effectief TYPE reading.
				List<SensorReading> readings =
						Arrays.asList(
								new SensorReading(now, SensorReadingType.OIL_TEMP_IN.name(), (double) s0),
								new SensorReading(now, SensorReadingType.OIL_PRESSURE_IN.name(), (double) s1),
								new SensorReading(now, SensorReadingType.OIL_TEMP_BEFORE_FILTERS.name(), (double) s2),
								new SensorReading(now, SensorReadingType.FLOW_BEFORE_FILTERS.name(), (double) s3),
								new SensorReading(now, SensorReadingType.OIL_PRESSURE_BEFORE_FILTERS.name(), (double) s4),
								new SensorReading(now, SensorReadingType.OIL_PRESSURE_AFTER_FILTERS.name(), (double) s5),
								new SensorReading(now, SensorReadingType.FLOW_DRAIN.name(), (double) s6),
								new SensorReading(now, SensorReadingType.PARTICLE_COUNT.name(), (double) s7)
						);


				for (SensorReading reading : readings) {
					try {
						writer.write(reading);
					} catch (EOFException e) {
						try {
							writer.close();
							writer = new MappedBusWriter(sensorFile, BUFFER_SIZE, 64, false);
							writer.open();
							writer.write(reading);
						} catch (IOException e1) {
							e1.printStackTrace();
						}

					}
				}

				Thread.sleep(SENSOR_FREQUENCY);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}

	}


}
