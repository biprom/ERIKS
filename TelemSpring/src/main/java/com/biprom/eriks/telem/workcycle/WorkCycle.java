package com.biprom.eriks.telem.workcycle;

import com.biprom.eriks.telem.workcycle.io.DigInput;
import com.biprom.eriks.telem.workcycle.io.DigOutput;
import com.biprom.eriks.telem.workcycle.io.dummies.DummyDigInput;
import com.biprom.eriks.telem.workcycle.io.dummies.DummyDigOut;
import com.biprom.eriks.telem.workcycle.io.dummies.Dummy_ADC_PI_MCP;
import com.biprom.eriks.telem.workcycle.io.impl.ADC_PI_MCP;
import com.biprom.eriks.telem.workcycle.io.impl.DigInput_PCF;
import com.biprom.eriks.telem.workcycle.io.impl.DigOutput_PCF;
import com.pi4j.gpio.extension.pcf.PCF8574Pin;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

import java.io.IOException;

import static com.biprom.eriks.telem.workcycle.DummyMode.DUMMY_MODE;

public class WorkCycle implements Runnable {

	private static boolean run = true;

	// Initialisatie digitale outputs
	DigOutput digital_output_card_1 = DUMMY_MODE ? new DummyDigOut() : new DigOutput_PCF(1, 0X20);

	DigOutput digital_output_card_2 = DUMMY_MODE ? new DummyDigOut() : new DigOutput_PCF(1, 0X21);

	// initialisatie digitale inputs
	DigInput digital_input_card_1 = DUMMY_MODE ? new DummyDigInput() : new DigInput_PCF(1, 0X22, "cardnr1");
	// DigInput_PCF digital_input_card_2 = new DigInput_PCF(1, 0X23, "cardnr2");

	// initialisatie analoge inputs
	private ADC_PI_MCP analog_input_card_1 = DUMMY_MODE ? new Dummy_ADC_PI_MCP() : new ADC_PI_MCP();

	private ADC_PI_MCP analog_input_card_2 = DUMMY_MODE ? new Dummy_ADC_PI_MCP() : new ADC_PI_MCP();

	private ADC_PI_MCP analog_input_card_3 = DUMMY_MODE ? new Dummy_ADC_PI_MCP() : new ADC_PI_MCP();

	public void stop() {
		run = false;
	}

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

	boolean reset_drukknop;

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
	double temp_olie; // gemeten temperatuur olie

	double temp_olie_min = 20; // minimum waarde olie

	double temp_olie_max = 40; // maximum waarde olie

	boolean verwarming_olie = false; // tweefasig olieverwarmer

	// leeglopen draintank
	boolean niv_hoog_peilglas; // maximaal niveau peilglas

	boolean schakelklep_draintank_open; // actie ifv peilglas

	boolean schakelklep_draintank_sluiten;

	boolean niv_laag_peilglas;

	boolean leegpompmotor_26; // actie ifv peilglas en bij filterwissel

	boolean niv_hoog_peilstok;

	// filterwissel
	// opstart parameters zonder spanning zetten
	boolean klep_23; // geschakelde klep bij filterwissel

	// leegloopmotor_26 schakekelen
	// controle sensor 29 flowmeter
	boolean sensor_29_flowmeter; // na aantal seconden motor 26 afslaan spoel 19

	// en 23 onbekrachtigd worden
	boolean klep_19; // bekrachtigen tijdens filterwissel

	boolean schakelklep_drainfilter_open;

	boolean schakelklep_drainfilter_sluiten;

	// alarmen + uitschakelen als
	double sensor4; // <0.2 bar op ingang of > 1.9 bar

	double sensor20; // differentiaaldruk samen met sensor 13

	double sensor13; // >=4bar (fout 5) druk op filterelement te hoog / >= 3bar
	// druk op filterelement waarschuwing
	// -0.1 bar onderdruk op filterelement drainventing open
	// (fout 8)
	double edsTotaalDrukSysteem;

	public void run() {
		System.out.println("Starting WORKCYCLE");
		while (run) {
			try {

				// inlezen analoge ingangen

				System.out.println("flow 18 : " + (analog_input_card_1.read_raw(0X6C, 0, 1, 0, 0) * 0.05456766));
				System.out.println("flow 29 : " + analog_input_card_1.read_raw(0X6C, 1, 1, 0, 0));
				edsTotaalDrukSysteem = ((0.19 * (analog_input_card_1.read_raw(0X6C, 2, 0, 1, 0) - 736)));

				System.out.println("eds 13 : " + edsTotaalDrukSysteem);

				temp_olie = (((analog_input_card_1.read_raw(0X6C, 3, 1, 0, 0)) - 365.04) / 19.16);
				System.out.println("temp olie aanzuig: " + temp_olie);

				System.out
						.println("temp 03 : " + (((analog_input_card_2.read_raw(0X6D, 0, 1, 0, 0)) - 365.04) / 19.16));

				sensor4 = ((0.62 / 186) * analog_input_card_2.read_raw(0X6D, 1, 0, 1, 0) - 2.4333);
				System.out.println("aanzuigdruk- sensor 04 : " + sensor4);

				sensor13 = ((0.62 / 186) * analog_input_card_2.read_raw(0X6D, 2, 0, 1, 0) - 2.4333);
				System.out.println("P 13 : " + sensor13);

				sensor20 = ((0.62 / 186) * analog_input_card_2.read_raw(0X6D, 3, 0, 1, 0) - 2.4333);
				System.out.println("P 20 : " + sensor20);

				System.out.println("stauf : NAS 1 : "
						+ (((((11.179 / 2047) * analog_input_card_3.read_raw(0X6E, 0, 0, 0, 0))) * 3) - 3));
				System.out.println("stauf : NAS 2 : "
						+ (((((11.179 / 2047) * analog_input_card_3.read_raw(0X6E, 1, 0, 0, 0))) * 3) - 3));
				System.out.println("stauf : NAS 3 : "
						+ (((((11.179 / 2047) * analog_input_card_3.read_raw(0X6E, 2, 0, 0, 0))) * 3) - 3));
				System.out.println("RH Olie : "
						+ ((((11.179 / 2047) * analog_input_card_3.read_raw(0X6E, 3, 0, 0, 0)) - 1.0) / 0.08));

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

				// luisteren naar AAN/UIT knop
				if (aan_uit_knop == true) {
					System.out.println("KNOP POSITIE AAN");
					status = Status.BEDRIJF;

				}

			}

			if (status == Status.BEDRIJF && error == Error.OK) {
				System.out.println("system is in BEDRIJFS MODE");

				// schakel installatie in
				bedrijf();

				// voorwaarden zonder in ERROR te gaan

				// leegpompen hoog pijlglas

				if (niv_hoog_peilstok == true) {
					System.out.println("LEEGPOMPEN START PARALLELWERKING");
					leegpompen_Start();

				}

				// leeglopen hoog pijlstok, zal leegpompten met machine uit te
				// schakelen (auto opstart !!!)

				// if (niv_hoog_peilstok == true) {
				// System.out.println(" pijlstok 1 te hoog");
				// standby();
				//
				// try {
				// Thread.sleep(1000);
				// } catch (InterruptedException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				//
				// leegpompen_Start();
				//
				// }

				if (niv_laag_peilglas == false) {
					System.out.println("LEEGPOMPEN STOP");
					leegpompen_Stop();

				}

				// kan terug keren naar standby via drukknop
				if (aan_uit_knop == false) {
					System.out.println("KNOP POSITIE UIT");
					status = Status.STANDBY;

				}

			}

			// elke scan controle van filters en errors

			if (sensor13 - sensor20 >= 3 && sensor13 - sensor20 < 4) {
				System.out.println("Aanraden filters te verwisselen");
				oranjeFilterVervuild = true;

			}

			if (sensor13 - sensor20 > 4) {
				System.out.println("Verplicht filters te verwisselen");
				status = Status.FILTERWISSEL;
				oranjeFilterVervuild = true;
				filterwissel_Start();

			}

			if (status == Status.FILTERWISSEL && leegpompmotor_26 == true && sensor_29_flowmeter == false) {

				filterwissel_Stop();
				System.out.println("system is FILTERWISSEL MODE gestopt");

				if (reset_drukknop == true) {
					status = Status.STANDBY;
				}

			}

			if (sensor4 < -0.2) {
				System.out.println("ERROR : sensor 4 < 0.2 bar");
				error = Error.SENSOR4_LAAG;

				standby();
				status = Status.STANDBY;

			}

			if (sensor4 > 1.9) {
				System.out.println("ERROR : sensor 4 > 1.9 bar");
				error = Error.SENSOR4_HOOG;

				standby();
				status = Status.STANDBY;
			}

			if (edsTotaalDrukSysteem > 5) {
				System.out.println("ERROR : Totaaldruk > 5bar");
				error = Error.SENSOR3_HOOG;

				standby();
				status = Status.STANDBY;
			}

			if (temp_olie > 80) {
				System.out.println("ERROR : temp olie > 80°C");
				error = Error.SENSOR3_HOOG;

				standby();
				status = Status.STANDBY;
			}

			if (sensor13 - sensor20 >= 4) {
				System.out.println("ERROR : sensor 13 - 20 >= 4 bar");
				error = Error.SENSOR3_HOOG;

				standby();
				status = Status.STANDBY;
			}

			if (error != Error.OK) {
				roodAlarm = true;
			} else {
				roodAlarm = false;
				error = Error.OK;
			}

			if (reset_drukknop == true) {
				// probeer error te resetten
				error = Error.OK;
			}

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

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void filterwissel_Stop() {
		oranjeFilterwisselOK = true;
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		leegpompmotor_26 = false;
		klep_23 = false;
		klep_19 = false;

		// oranjeFilterwisselOK = false;
		// oranjeFilterVervuild = false;
	}

	private void leegpompen_Start() {
		// schakelklep 30 en motor 26
		// regelen tussen deze waarden
		roodDrainnage = true;
		schakelklep_draintank_open = true;
		schakelklep_draintank_sluiten = false;
		/*
		 * try { Thread.sleep(3000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		leegpompmotor_26 = true;

		System.out.println("functie leegpompen gestart");

	}

	private void leegpompen_Stop() {
		// schakelklep 30 en motor 26
		// regelen tussen deze waarden
		schakelklep_draintank_open = false;
		schakelklep_draintank_sluiten = true;
		leegpompmotor_26 = false;
		roodDrainnage = false;
		System.out.println("functie leegpompen gestopt");
	}

	private void bedrijf() {
		// opstart sequentie filterunit
		// opstart unit 17 28 + 09
		// plaats groen lampje aan
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

		System.out.println("temp: " + temp_olie_min + ", " + temp_olie_max);
		if (temp_olie <= temp_olie_min) {
			verwarming_olie = true;
		}
		if (temp_olie >= temp_olie_max) {
			verwarming_olie = false;
		}

	}

	public void setTempMinMax(double min, double max) {
		this.temp_olie_min = min;
		this.temp_olie_max = max;
	}

	private void standby() {
		// afsluit sequentie filterunit
		// groen lampje bedrijf afleggen

		groenBedrijf = false;
		klepNC17 = false; // wordt geschakeld tijdens opstart
		klepNC28 = false; // wordt geschakeld tijdens opstart
		koeling_motor_9 = false; // wordt geschakeld tijdens opstart
		verwarming_olie = false; // tweefasig olieverwarmer
		schakelklep_draintank_open = false;
		schakelklep_draintank_sluiten = true;
		leegpompmotor_26 = false; // actie ifv peilglas en bij filterwissel
		klep_23 = false;
		sensor_29_flowmeter = false; // na aantal seconden motor 26 afslaan
										// spoel 19
		klep_19 = false; // bekrachtigen tijdens filterwissel
		verwarming_olie = false; // olie mag niet worden verwarmd in standby

	}

	// funtie setten van inputs tijdens start van loop

	private void setInputsToVariables() {

		// digital_input_card_1.providerReader.getState(PCF8574Pin.GPIO_00);
		// digital_input_card_1.providerReader.getState(PCF8574Pin.GPIO_01);
		reset_drukknop = !(digital_input_card_1.getState(PCF8574Pin.GPIO_02).getValue() != 0);
		niv_hoog_peilstok = !(digital_input_card_1.getState(PCF8574Pin.GPIO_03).getValue() != 0);
		System.out.println("niveau pijlstok hoog : " + niv_hoog_peilstok);
		niv_hoog_peilglas = !(digital_input_card_1.getState(PCF8574Pin.GPIO_04).getValue() != 0);
		niv_laag_peilglas = !(digital_input_card_1.getState(PCF8574Pin.GPIO_05).getValue() != 0);
		System.out.println("niveau pijglas laag : " + niv_laag_peilglas);
		// aan_uit_knop =
		// !(digital_input_card_1.providerReader.getState(PCF8574Pin.GPIO_06).getValue()
		// != 0);
		aan_uit_knop = !(digital_input_card_1.getState(PCF8574Pin.GPIO_07).getValue() != 0);
	}

	private void setOutputsToVariables() {
		try {
			digital_output_card_1.d1.setState(!koeling_motor_9);
			digital_output_card_1.d2.setState(!koeling_motor_9);
			digital_output_card_1.d3.setState(!leegpompmotor_26);
			digital_output_card_1.d4.setState(!schakelklep_draintank_sluiten);
			digital_output_card_1.d5.setState(!schakelklep_draintank_open);
			digital_output_card_1.d6.setState(!schakelklep_drainfilter_open);
			digital_output_card_1.d7.setState(!schakelklep_drainfilter_sluiten);
			digital_output_card_1.d8.setState(!klepNC28);

			digital_output_card_2.d1.setState(!klep_19);
			digital_output_card_2.d2.setState(!klep_19);
			digital_output_card_2.d3.setState(!groenBedrijf);
			digital_output_card_2.d4.setState(!oranjeFilterwisselOK);
			digital_output_card_2.d5.setState(!oranjeFilterVervuild);
			digital_output_card_2.d6.setState(!roodAlarm);
			digital_output_card_2.d7.setState(!roodDrainnage);
			digital_output_card_2.d8.setState(!verwarming_olie);
		} catch (Exception e) {
		}

	}

	public Double getMaxTemp() {
		return temp_olie_max;
	}

	public Double getMinTemp() {
		return temp_olie_min;
	}

	public boolean isRunning(){
		return aan_uit_knop;
	}
}
