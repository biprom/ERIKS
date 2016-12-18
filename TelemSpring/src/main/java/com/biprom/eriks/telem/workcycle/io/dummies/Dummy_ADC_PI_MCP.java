package com.biprom.eriks.telem.workcycle.io.dummies;

import com.biprom.eriks.telem.workcycle.io.impl.ADC_PI_MCP;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kristof
 *         Created on 18/12/2016.
 */
public class Dummy_ADC_PI_MCP extends ADC_PI_MCP {


	@Override
	public byte set_config(int channel, int pga, int bitrate, int mode) {
		return 1;
	}

	@Override
	public long read_raw(int address, int channel, int pga, int bitrate, int mode) throws IOException, I2CFactory.UnsupportedBusNumberException {
		return ThreadLocalRandom.current().nextLong(20);
	}

	@Override
	public double read_voltage(int address, int channel, int pga, int bitrate, int mode) throws IOException, I2CFactory.UnsupportedBusNumberException {
		return ThreadLocalRandom.current().nextDouble(20);
	}
}
