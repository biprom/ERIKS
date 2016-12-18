package com.biprom.eriks.telem.workcycle.io.impl;


import com.biprom.eriks.telem.workcycle.io.DigOutput;
import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.gpio.extension.pcf.PCF8574Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import java.io.IOException;


public class DigOutput_PCF extends DigOutput {


	final GpioController gpio = GpioFactory.getInstance();


	// create custom MCP23017 GPIO provider
	private PCF8574GpioProvider gpioProvider;


	public DigOutput_PCF(int busnr, int adressnr) {

		int busnummer = busnr;

		int adressnummer = adressnr;


		try {
			gpioProvider = new PCF8574GpioProvider(busnummer, adressnummer);
		} catch (UnsupportedBusNumberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// provision gpio output pins and make sure they are all LOW at startup

		d1 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_00, PinState.HIGH);
		d2 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_01, PinState.HIGH);
		d3 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_02, PinState.HIGH);
		d4 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_03, PinState.HIGH);
		d5 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_04, PinState.HIGH);
		d6 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_05, PinState.HIGH);
		d7 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_06, PinState.HIGH);
		d8 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_07, PinState.HIGH);


		//set and reset outputs from this device


	}

	public void setOutputHigh(GpioPinDigitalOutput number) {

		gpio.setState(true, number);


	}

	public void setOutputLow(GpioPinDigitalOutput number) {

		gpio.setState(false, number);


	}


}
