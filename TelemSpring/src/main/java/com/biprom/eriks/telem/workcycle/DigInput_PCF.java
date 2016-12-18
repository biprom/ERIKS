package com.biprom.eriks.telem.workcycle;

import com.pi4j.gpio.extension.pcf.PCF8574GpioProvider;
import com.pi4j.gpio.extension.pcf.PCF8574Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import java.io.IOException;

public class DigInput_PCF {

	PCF8574GpioProvider providerReader = null;


	public DigInput_PCF(int busnr, int adressnr, String cardnr) {


		int busnummer = busnr;

		int adressnummer = adressnr;

		final String cardnumber = cardnr;
				
		// create gpio controller
		final GpioController gpio = GpioFactory.getInstance();

		
			// create custom MCP23017 GPIO provider
			try {
				providerReader = new PCF8574GpioProvider(busnummer, adressnummer);
			} catch (UnsupportedBusNumberException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		


		// provision gpio input pins from MCP23017
		GpioPinDigitalInput myInputs[] = {
				gpio.provisionDigitalInputPin(providerReader, PCF8574Pin.GPIO_00),
				gpio.provisionDigitalInputPin(providerReader, PCF8574Pin.GPIO_01),
				gpio.provisionDigitalInputPin(providerReader, PCF8574Pin.GPIO_02),
				gpio.provisionDigitalInputPin(providerReader, PCF8574Pin.GPIO_03),
				gpio.provisionDigitalInputPin(providerReader, PCF8574Pin.GPIO_04),
				gpio.provisionDigitalInputPin(providerReader, PCF8574Pin.GPIO_05),
				gpio.provisionDigitalInputPin(providerReader, PCF8574Pin.GPIO_06),
				gpio.provisionDigitalInputPin(providerReader, PCF8574Pin.GPIO_07)
		};


		// create and register gpio pin listener
		gpio.addListener(new GpioPinListenerDigital() {
			public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
				// display pin state on console
				
				System.out.println(" --> : "+ cardnumber + "   " + event.getPin() + " = "
						+ event.getState());
				
			}
		}, myInputs);

	}


}



