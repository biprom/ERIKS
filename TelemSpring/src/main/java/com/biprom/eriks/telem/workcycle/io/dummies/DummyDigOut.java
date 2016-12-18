package com.biprom.eriks.telem.workcycle.io.dummies;

import com.biprom.eriks.telem.workcycle.io.DigOutput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;

/**
 * @author Kristof
 *         Created on 18/12/2016.
 */
public class DummyDigOut extends DigOutput {
	@Override
	public void setOutputHigh(GpioPinDigitalOutput number) {

	}

	@Override
	public void setOutputLow(GpioPinDigitalOutput number) {

	}
}
