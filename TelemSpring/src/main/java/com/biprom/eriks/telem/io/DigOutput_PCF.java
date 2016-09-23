package com.biprom.eriks.telem.io;


import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.gpio.extension.pcf.PCF8574Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;


public class DigOutput_PCF {


	final GpioController gpio = GpioFactory.getInstance();


	// create custom MCP23017 GPIO provider
	PCF8574GpioProvider gpioProvider;

	GpioPinDigitalOutput d1, d2, d3, d4, d5, d6, d7, d8;

	public DigOutput_PCF(int busnr, int adressnr) {

		int busnummer = busnr;

		int adressnummer = adressnr;


		try {
			gpioProvider = new PCF8574GpioProvider(busnummer, adressnummer);

			// provision gpio output pins and make sure they are all LOW at startup

			d1 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_00, PinState.LOW);
			d2 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_01, PinState.LOW);
			d3 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_02, PinState.LOW);
			d4 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_03, PinState.LOW);
			d5 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_04, PinState.LOW);
			d6 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_05, PinState.LOW);
			d7 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_06, PinState.LOW);
			d8 = gpio.provisionDigitalOutputPin(gpioProvider, PCF8574Pin.GPIO_07, PinState.LOW);

		} catch (IOException | I2CFactory.UnsupportedBusNumberException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//set and reset outputs from this device


	}

	public void setOutputHigh(GpioPinDigitalOutput number) {

		gpio.setState(true, number);


	}

	public void setOutputLow(GpioPinDigitalOutput number) {

		gpio.setState(false, number);


	}


}
