package com.biprom.workcycle;

import java.io.IOException;

import com.pi4j.gpio.extension.pcf.PCF8574Pin;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class WorkCycle extends Thread {
	
	//Initialisatie digitale outputs
	DigOutput_PCF digital_output_card_1 = new DigOutput_PCF(1, 0X20);
	DigOutput_PCF digital_output_card_2 = new DigOutput_PCF(1, 0X21);
	
	//initialisatie digitale inputs
	DigInput_PCF digital_input_card_1 = new DigInput_PCF(1, 0X22, "cardnr1");
	DigInput_PCF digital_input_card_2 = new DigInput_PCF(1, 0X23, "cardnr2");
	
	//initialisatie analoge inputs
	ADC_PI_MCP analog_input_card_1 = new ADC_PI_MCP();
	ADC_PI_MCP analog_input_card_2 = new ADC_PI_MCP();

	// status werking filterunit
	public enum Status {
		INIT, STANDBY, OPSTART, BEDRIJF, FILTERWISSEL, FILTERWISSEL_OK, DRAINAGE, FILTER_VERVUILD
	}

	public enum Error {
		OK, SENSOR4_LAAG, SENSOR4_HOOG, SENSOR3_LAAG, SENSOR3_HOOG, DIFF13_20_HOOG, DIFF13_20_LAAG, CONTACT1_HOOG
	}

	// declaratie status en error Filterunit
	Status status = Status.STANDBY;
	Error error = Error.OK;

	// AAN/UIT- knop filterunit
	boolean aan_uit_knop;
	
	// indicatielichten frontpanel elektrische kast
	boolean groenBedrijf;
	boolean oranjeFilterwisselOK;
	boolean oranjeFilterVervuild;
	boolean roodAlarm;
	boolean roodDrainnage;

	// parameters opstart (openen hydraulische kring + circuleren olie
	boolean klepNC17 = false; // wordt geschakeld tijdens opstart
	boolean klepNC28 = false; // wordt geschakeld tijdens opstart
	boolean koeling_motor_9 = false; // wordt geschakeld tijdens opstart

	// opstart op temperatuur brengen olie
	int temp_olie; // gemeten temperatuur olie
	int temp_olie_min; // minimum waarde olie
	int temp_olie_max; // maximum waarde olie
	boolean verwarming_olie = false; // tweefasig olieverwarmer

	// leeglopen draintank
	boolean niv_hoog_peilglas; // maximaal niveau peilglas
	boolean niv_laag_peilglas; // minimum niveau peilglas
	boolean schakelklep_draintank_30; // actie ifv peilglas
	boolean leegpompmotor_26; // actie ifv peilglas en bij filterwissel

	// filterwissel
	// opstart parameters zonder spanning zetten
	boolean klep_23; // geschakelde klep bij filterwissel
	// leegloopmotor_26 schakekelen
	// controle sensor 29 flowmeter
	boolean sensor_29_flowmeter; // na aantal seconden motor 26 afslaan spoel 19
									// en 23 onbekrachtigd worden
	boolean klep_19; // bekrachtigen tijdens filterwissel

	// alarmen + uitschakelen als
	int sensor4; // <0.2 bar op ingang of > 1.9 bar

	int sensor20; // differentiaaldruk samen met sensor 13
	int sensor13; // >=4bar (fout 5) druk op filterelement te hoog / >= 3bar
					// druk op filterelement waarschuwing
					// -0.1 bar onderdruk op filterelement drainventing open
					// (fout 8)
	boolean schakelcontact_tehoog_1; // direct uitschakelen installatie

	public void run() {

		while (true) {
			
			
			
			try {
				System.out.println(""+analog_input_card_1.read_raw(0X6C, 0, 0, 0, 0)); 
				System.out.println(""+analog_input_card_1.read_raw(0X6C, 1, 0, 0, 0));
				System.out.println(""+analog_input_card_1.read_raw(0X6C, 2, 0, 0, 0));
				System.out.println(""+analog_input_card_1.read_raw(0X6C, 3, 0, 0, 0));
				
				System.out.println(""+analog_input_card_1.read_raw(0X6D, 0, 0, 0, 0)); 
				System.out.println(""+analog_input_card_1.read_raw(0X6D, 1, 0, 0, 0));
				System.out.println(""+analog_input_card_1.read_raw(0X6D, 2, 0, 0, 0));
				System.out.println(""+analog_input_card_1.read_raw(0X6D, 3, 0, 0, 0));
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedBusNumberException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			setInputsToVariables();

			if (status == Status.STANDBY) {
				System.out.println("system is in STANDBY MODE");
				standby();
				System.out.println("Alle uitgangen zijn NIET geschakeld");

				// luisteren naar AAN/UIT knop
				if (aan_uit_knop == true) {
					System.out.println("KNOP POSITIE AAN");
					status = Status.BEDRIJF;

				}

				
			}

			if (status == Status.BEDRIJF) {
				System.out.println("system is in BEDRIJFS MODE");
				bedrijf();
				
								

				if (niv_hoog_peilglas == true) {
					System.out.println("LEEGPOMPEN START");
					leegpompen_Start();
				} else{
					System.out.println("LEEGPOMPEN STOP");
					leegpompen_Stop();
				}
					
				//kan terug keren naar standby via drukknop
				if (aan_uit_knop == false) {
					System.out.println("KNOP POSITIE UIT");
					status = Status.STANDBY;

					}
				
			}
			
			
			if (sensor13 - sensor20 >= 3 && sensor13 - sensor20 < 4) {
				System.out.println("system is FILTERWISSEL MODE gestart");
				status = Status.FILTERWISSEL;
				filterwissel_Start();
			}
			if (leegpompmotor_26 == true && sensor_29_flowmeter == false) {

				filterwissel_Stop();
				System.out.println("system is FILTERWISSEL MODE gestopt");

			}
			
			

			/*if (sensor4 < -0.2) {
				System.out.println("ERROR : sensor 4 < 0.2 bar");
				error = Error.SENSOR4_LAAG;
				standby();

			}

			if (sensor4 > 1.9) {
				System.out.println("ERROR : sensor 4 > 1.9 bar");
				error = Error.SENSOR4_HOOG;
				standby();
			}

			if (temp_olie < 15) {
				System.out.println("ERROR : temp olie < 15°C");
				error = Error.SENSOR3_LAAG;
				standby();
			}

			if (temp_olie > 80) {
				System.out.println("ERROR : temp olie > 80°C");
				error = Error.SENSOR3_HOOG;
				standby();
			}

			if (sensor13 - sensor20 >= 4) {
				System.out.println("ERROR : sensor 13 - 20 >= 4 bar");
				error = Error.SENSOR3_HOOG;
				standby();
			}

			if (schakelcontact_tehoog_1 == true) {
				System.out.println("ERROR : schakelcontact 1 te hoog");
				error = Error.CONTACT1_HOOG;
				standby();
			}
			
			if(error != Error.OK){
				roodAlarm = true;
			}
			else {
				roodAlarm = false;
			}
			
			*/
			
			setOutputsToVariables();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	

	

	private void filterwissel_Start() {
		// Motor 9 uitschakelen, verwarmingselement 10 uit, na 3 sec klep 17 en
		// 28 zonder spanning zetten
		// klep 23 schakelen
		// Motor 26 aanschakekelen
		
		oranjeFilterVervuild = true;
		
		koeling_motor_9 = false;
		verwarming_olie = false;
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		klepNC17 = false;
		klepNC28 = false;
		klep_23 = true;
		klep_19 = true;

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		leegpompmotor_26 = true;

	}

	private void filterwissel_Stop() {
			oranjeFilterwisselOK = true;
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//leegpompmotor_26 = false;
		klep_23 = false;
		klep_19 = false;
		
		oranjeFilterwisselOK = false;
		oranjeFilterVervuild = false;
	}

	private void leegpompen_Start() {
		// schakelklep 30 en motor 26
		// regelen tussen deze waarden
		roodDrainnage = true;
		schakelklep_draintank_30 = true;
		leegpompmotor_26 = true;

	}

	private void leegpompen_Stop() {
		// schakelklep 30 en motor 26
		// regelen tussen deze waarden
		schakelklep_draintank_30 = false;
		leegpompmotor_26 = false;
		roodDrainnage = false;

	}

	private void bedrijf() {
		// opstart sequentie filterunit
		// opstart unit 17 28 + 09
		//plaats groen lampje aan
		groenBedrijf = true;

		klepNC17 = true;

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		klepNC28 = true;

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		koeling_motor_9 = true;

		// opstart verwarmen olie

		if (temp_olie <= temp_olie_min) {
			verwarming_olie = true;
		}
		if (temp_olie >= temp_olie_max) {
			verwarming_olie = false;
		}

	}

	private void standby() {
		// afsluit sequentie filterunit
		//groen lampje bedrijf afleggen
		
		groenBedrijf = false;
		 klepNC17 = false; // wordt geschakeld tijdens opstart
		 klepNC28 = false; // wordt geschakeld tijdens opstart
		 koeling_motor_9 = false; // wordt geschakeld tijdens opstart
		 verwarming_olie = false; // tweefasig olieverwarmer
		 schakelklep_draintank_30 = false; // actie ifv peilglas
		 leegpompmotor_26 = false; // actie ifv peilglas en bij filterwissel
		 klep_23 = false; 
		 sensor_29_flowmeter = false; // na aantal seconden motor 26 afslaan spoel 19
		 klep_19 = false; // bekrachtigen tijdens filterwissel
		 
		 
	}

	
	//funtie setten van inputs tijdens start van loop
	
	private void setInputsToVariables() {
		
//		digital_input_card_1.providerReader.getState(PCF8574Pin.GPIO_00);
//		digital_input_card_1.providerReader.getState(PCF8574Pin.GPIO_01);
//		digital_input_card_1.providerReader.getState(PCF8574Pin.GPIO_02);
//		digital_input_card_1.providerReader.getState(PCF8574Pin.GPIO_03);
		niv_hoog_peilglas = !(digital_input_card_1.providerReader.getState(PCF8574Pin.GPIO_03).getValue() != 0);
		niv_laag_peilglas = !(digital_input_card_1.providerReader.getState(PCF8574Pin.GPIO_04).getValue() != 0);
		schakelcontact_tehoog_1 = !(digital_input_card_1.providerReader.getState(PCF8574Pin.GPIO_05).getValue() != 0);
		aan_uit_knop = !(digital_input_card_1.providerReader.getState(PCF8574Pin.GPIO_07).getValue() != 0);
	}

	private void setOutputsToVariables() {
	digital_output_card_1.d1.setState(!koeling_motor_9);
	digital_output_card_1.d2.setState(!koeling_motor_9);
	//digital_output_card_1.d3.setState(!leegpompmotor_26);
	digital_output_card_1.d4.setState(!schakelklep_draintank_30);
	digital_output_card_1.d5.setState(!klepNC17);
	digital_output_card_1.d6.setState(!klepNC17);
	digital_output_card_1.d7.setState(!klep_23);
	digital_output_card_1.d8.setState(!klepNC28);
	
	digital_output_card_2.d1.setState(!klep_19);
	digital_output_card_2.d2.setState(!klep_19);
	digital_output_card_2.d3.setState(!groenBedrijf);
	digital_output_card_2.d4.setState(!oranjeFilterwisselOK);
	digital_output_card_2.d5.setState(!oranjeFilterVervuild);
	digital_output_card_2.d6.setState(!roodAlarm);
	digital_output_card_2.d7.setState(!roodDrainnage);
	digital_output_card_2.d8.setState(!leegpompmotor_26);
	
	
}


}
