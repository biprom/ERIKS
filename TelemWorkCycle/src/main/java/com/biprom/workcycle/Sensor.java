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
		FLOW_FILTERUNIT, FLOW_FILTER_DRAIN, PRESSURE_FILTERUNIT, TEMPERATURE_AFTER_PUMP, OIL_TEMPEARTURE_INLET, OIL_PRESSURE_INLET, PRESSURE_BEFORE_FILTERS, PRESSURE_AFTER_FILTERS, IS02micro, ISO5micro, ISO15micro, RH//
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
		writer = new MappedBusWriter(sensorFile, BUFFER_SIZE, 64, false);
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

				final double s0 = analog_input_card_1_1.read_raw(0X6C, 0, 1, 0, 0); // flow 18
																	
				final double s1 = analog_input_card_1_1.read_raw(0X6C, 1, 1, 0, 0);// flow 29
																	
				final double s2 = ((0.19*(analog_input_card_1_1.read_raw(0X6C, 2, 0, 1, 0)-736))); // eds13
				
				final double s3 = (((analog_input_card_2_1.read_raw(0X6D, 0, 1, 0, 0))-365.04)/19.16); // temp olie na pomp
																								
				final double s4 = (((analog_input_card_1_1.read_raw(0X6C, 3, 1, 0, 0))-365.04)/19.16); // temp olie input
																								// 03
				final double s5 = ((0.62/186)*analog_input_card_2_1.read_raw(0X6D, 1, 0, 1, 0)-2.4333); // sensor
																												// 4
				final double s6 = ((0.62/186)*analog_input_card_2_1.read_raw(0X6D, 2, 0, 1, 0)-2.4333); // sensor
																												// 13
				final double s7 = ((0.62/186)*analog_input_card_2_1.read_raw(0X6D, 3, 0, 1, 0)-2.4333); // sensor
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

				
				
//				final double s0 = 1; // flow					// 18
//				final double s1 = 2;// flow
//				final double s2 = 3; // eds13
//				final double s3 = 4; // temp
//				final double s4 = 5; // temp
//				final double s5 = 6; // sensor
//				final double s6 = 7; // sensor
//				final double s7 = 8; // sensor
//				final double s8 = 9; // stauf
//				final double s9 = 10; // stauf
//				final double s10 = 11; // stauf
//				final double s11 =  12;
//				
				
				
				List<SensorReading> readings = Arrays.asList(
						new SensorReading(now, SensorReadingType.FLOW_FILTERUNIT.name(), s0),
						new SensorReading(now, SensorReadingType.FLOW_FILTER_DRAIN.name(), s1),
						new SensorReading(now, SensorReadingType.PRESSURE_FILTERUNIT.name(), s2),
						new SensorReading(now, SensorReadingType.TEMPERATURE_AFTER_PUMP.name(), s3),
						new SensorReading(now, SensorReadingType.OIL_TEMPEARTURE_INLET.name(), s4),
						new SensorReading(now, SensorReadingType.OIL_PRESSURE_INLET.name(), s5),
						new SensorReading(now, SensorReadingType.PRESSURE_BEFORE_FILTERS.name(), s6),
						new SensorReading(now, SensorReadingType.PRESSURE_AFTER_FILTERS.name(), s7),
						new SensorReading(now, SensorReadingType.IS02micro.name(), s8),
						new SensorReading(now, SensorReadingType.ISO5micro.name(), s9),
						new SensorReading(now, SensorReadingType.ISO15micro.name(), s10),
						new SensorReading(now, SensorReadingType.RH.name(), s11));

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
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (UnsupportedBusNumberException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}

	}

}
