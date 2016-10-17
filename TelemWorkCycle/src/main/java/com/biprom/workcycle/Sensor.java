package com.biprom.workcycle;

import io.mappedbus.MappedBusWriter;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

/**
 * @author Kristof Created on 12/10/16.
 */
public class Sensor extends Thread {

	private String sensorFile;

	/**
	 * Read sensor data every 10 seconds.
	 */
	private static final int SENSOR_FREQUENCY = 15 * 1000;

	private static final long BUFFER_SIZE = 500000L;

	public enum SensorReadingType {
		FLOW_FILTERUNIT, FLOW_FILTER_DISCHARGE, PRESSURE_FILTERUNIT, TEMPERATURE_AFTER_PUMP, OIL_TEMPEARTURE_INPUT, OIL_PRESSURE_INPUT, PRESSURE_BEFORE_FILTERS, PRESSURE_AFTER_FILTERS, NAS1, NAS2, NAS3, RH_OIL//
	}

	/**
	 * Reading out sensor data from this card TODO Probably not correct
	 */
	ADC_PI_MCP analog_input_card_1_1 = new ADC_PI_MCP();
	ADC_PI_MCP analog_input_card_2_1 = new ADC_PI_MCP();
	ADC_PI_MCP analog_input_card_3_1 = new ADC_PI_MCP();

	/**
	 * Een memory mapped file voor communicatie tussen processen. Native linux
	 * feature, dus eigenlijk nog stabieler dan Sockets!!
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

				// TODO: BRAM, Check of de KLOK altijd JUIST staat op de
				// raspberry. Anders zijn die metingen waardeloos!
				Date now = new Date();

				final double s0 = analog_input_card_1_1.read_raw(0X6C, 0, 1, 0, 0); // flow
																					// 18
				final double s1 = analog_input_card_1_1.read_raw(0X6C, 1, 1, 0, 0);// flow
																					// 29
				final double s2 = (analog_input_card_1_1.read_raw(0X6C, 2, 1, 0, 0) / 48.375); // eds13
				final double s3 = (analog_input_card_1_1.read_raw(0X6C, 3, 1, 0, 0) / 35.30); // temp
																								// olie
																								// na
																								// pomp
				final double s4 = (analog_input_card_2_1.read_raw(0X6D, 0, 1, 0, 0) / 35.30); // temp
																								// 03
				final double s5 = ((0.62 / 186) * analog_input_card_2_1.read_raw(0X6D, 1, 0, 1, 0) - 2.4333); // sensor
																												// 4
				final double s6 = ((0.62 / 186) * analog_input_card_2_1.read_raw(0X6D, 2, 0, 1, 0) - 2.4333); // sensor
																												// 13
				final double s7 = ((0.62 / 186) * analog_input_card_2_1.read_raw(0X6D, 3, 0, 1, 0) - 2.4333); // sensor
																												// 20
				final double s8 = (analog_input_card_3_1.read_raw(0X6E, 0, 1, 0, 0) / 146.214); // stauf
																								// nas1
				final double s9 = (analog_input_card_3_1.read_raw(0X6E, 1, 1, 0, 0) / 155.636); // stauf
																								// nas
																								// 2
				final double s10 = (analog_input_card_3_1.read_raw(0X6E, 2, 1, 0, 0) / 158.9); // stauf
																								// nas
																								// 3
				final double s11 = (analog_input_card_3_1.read_raw(0X6E, 3, 1, 0, 0) / 28.431); // RHOlie

				List<SensorReading> readings = Arrays.asList(
						new SensorReading(now, SensorReadingType.FLOW_FILTERUNIT.name(), s0),
						new SensorReading(now, SensorReadingType.FLOW_FILTER_DISCHARGE.name(), s1),
						new SensorReading(now, SensorReadingType.PRESSURE_FILTERUNIT.name(), s2),
						new SensorReading(now, SensorReadingType.TEMPERATURE_AFTER_PUMP.name(), s3),
						new SensorReading(now, SensorReadingType.OIL_TEMPEARTURE_INPUT.name(), s4),
						new SensorReading(now, SensorReadingType.OIL_PRESSURE_INPUT.name(), s5),
						new SensorReading(now, SensorReadingType.PRESSURE_BEFORE_FILTERS.name(), s6),
						new SensorReading(now, SensorReadingType.PRESSURE_AFTER_FILTERS.name(), s7),
						new SensorReading(now, SensorReadingType.NAS1.name(), s8),
						new SensorReading(now, SensorReadingType.NAS2.name(), s9),
						new SensorReading(now, SensorReadingType.NAS3.name(), s10),
						new SensorReading(now, SensorReadingType.RH_OIL.name(), s11));

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
			} catch (IOException e2) {
				e2.printStackTrace();
			} catch (UnsupportedBusNumberException e2) {
				e2.printStackTrace();
			}
		}

	}

}
