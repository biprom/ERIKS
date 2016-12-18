package com.biprom.eriks.telem.workcycle.io;

import com.pi4j.io.gpio.GpioPinDigitalOutput;

/**
 * @author Kristof
 *         Created on 18/12/2016.
 */
public abstract class DigOutput {

	public GpioPinDigitalOutput d1, d2, d3, d4, d5, d6, d7, d8;

	public abstract void setOutputHigh(GpioPinDigitalOutput number);

	public abstract void setOutputLow(GpioPinDigitalOutput number);

}
