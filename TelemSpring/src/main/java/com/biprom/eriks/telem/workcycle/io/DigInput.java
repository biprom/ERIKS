package com.biprom.eriks.telem.workcycle.io;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;

/**
 * @author Kristof
 *         Created on 18/12/2016.
 */
public abstract class DigInput {
	public abstract PinState getState(Pin gpio02);
}
