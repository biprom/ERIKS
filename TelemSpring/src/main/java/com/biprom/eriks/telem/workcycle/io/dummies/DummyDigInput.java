package com.biprom.eriks.telem.workcycle.io.dummies;

import com.biprom.eriks.telem.workcycle.io.DigInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

/**
 * @author Kristof
 *         Created on 18/12/2016.
 */
public class DummyDigInput extends DigInput {
	public DummyDigInput() {

	}

	@Override
	public PinState getState(Pin gpio02) {
		return PinState.LOW;
	}
}
