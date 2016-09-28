package workcycle;

public class WorkCycle extends Thread {
	
			//status werking filterunit
			public enum Status{
				INIT, STANDBY, OPSTART, BEDRIJF, FILTERWISSEL, FILTERWISSEL_OK, FOUT, DRAINAGE, FILTER_VERVUILD
			}
			
			public enum Error{
				OK, SENSOR4_LAAG, SENSOR4_HOOG, SENSOR3_LAAG, SENSOR3_HOOG, DIFF13_20_HOOG, DIFF13_20_LAAG, CONTACT1_HOOG
			}
			
			//declaratie status en error Filterunit
			Status status = Status.STANDBY;
			Error error = Error.OK;
			
			
			//AAN/UIT- knop filterunit
			boolean aan_uit_knop;
	
			//parameters opstart (openen hydraulische kring + circuleren olie
			boolean klepNC17 = false;	// wordt geschakeld tijdens opstart
			boolean klepNC28 = false;	//wordt geschakeld tijdens opstart
			boolean koeling_motor_9 = false;	//wordt geschakeld tijdens opstart
			
			//opstart op temperatuur brengen olie
			int temp_olie;					//gemeten temperatuur olie
			int temp_olie_min;				//minimum waarde olie
			int temp_olie_max;				//maximum waarde olie
			boolean verwarming_olie;		//tweefasig olieverwarmer
			
			//leeglopen draintank
			boolean niv_hoog_peilglas;		//maximaal niveau peilglas
			boolean niv_laag_peilglas;		//minimum niveau peilglas
			boolean schakelklep_draintank_30;	//actie ifv peilglas
			boolean leegpompmotor_26;				//actie ifv peilglas en bij filterwissel
			
			
			//filterwissel
				//opstart parameters zonder spanning zetten
			boolean klep_23;		//geschakelde klep bij filterwissel
				//leegloopmotor_26 schakekelen
				//controle sensor 29 flowmeter
			boolean sensor_29_flowmeter; 	//na aantal seconden motor 26 afslaan spoel 19 en 23 onbekrachtigd worden
			boolean klep_19;				//bekrachtigen tijdens filterwissel
			
			//alarmen + uitschakelen als 
			int sensor4; 	//<0.2 bar op ingang of > 1.9 bar
			
			int sensor20;	//differentiaaldruk samen met sensor 13
			int sensor13;	//>=4bar (fout 5) druk op filterelement te hoog / >= 3bar druk op filterelement waarschuwing
							//-0.1 bar onderdruk op filterelement drainventing open (fout 8)
			boolean schakelcontact_tehoog_1;			//direct uitschakelen installatie
			
			
	
	public WorkCycle(){
		
		//INITIALISATIE filterunit
		//status = Status.INIT;
			//proberen opstarten visualisatie Kristof vragen
		
		
		
			while( status == Status.STANDBY){
				standby();
				
				//luisteren naar AAN/UIT knop
				if (aan_uit_knop == true){
					status = Status.BEDRIJF;
					
				}
				
				else status = Status.STANDBY;
			}
			 
			while (status == Status.BEDRIJF){
				bedrijf();
				
				if (niv_hoog_peilglas == true){
					status = Status.DRAINAGE;
					leegpompen_Start();
					}
				else leegpompen_Stop();
				status = Status.BEDRIJF;
				}
				
				if (sensor13 - sensor20 >= 3 && sensor13 - sensor20 < 4){
					status = Status.FILTERWISSEL;
					filterwissel_Start();
				}
				if (leegpompmotor_26 == true && sensor_29_flowmeter == false){
					
					filterwissel_Stop();
					status = Status.FILTERWISSEL_OK;
				
					
				}
					
					
				
				if (sensor4 < -0.2 ){
					status = Status.FOUT;
					error = Error.SENSOR4_LAAG;
					standby();
					
				}
				
				if (sensor4 > 1.9 ){
					status = Status.FOUT;
					error = Error.SENSOR4_HOOG;
					standby();
				}
				
				if (temp_olie < 15){
					status = Status.FOUT;
					error = Error.SENSOR3_LAAG;
					standby();
				}
				
				if (temp_olie > 80){
					status = Status.FOUT;
					error = Error.SENSOR3_HOOG;
					standby();
				}
				
				if (sensor13 - sensor20 >= 4){
					status = Status.FOUT;
					error = Error.SENSOR3_HOOG;
					standby();
				}
				
				if (schakelcontact_tehoog_1 == true){
					status = Status.FOUT;
					error = Error.CONTACT1_HOOG;
					standby();
				}
				
				
				
				
			}
			
		
		
	



	
	





	private void filterwissel_Start() {
		// Motor 9 uitschakelen, verwarmingselement 10 uit, na 3 sec klep 17 en 28 zonder spanning zetten
		//klep 23 schakelen
		//Motor 26 aanschakekelen
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
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		leegpompmotor_26 = false;
		klep_23 = false;
		klep_19 = false;
		
		
	}






	private void leegpompen_Start() {
		// schakelklep 30 en motor 26
		// regelen tussen deze waarden
		schakelklep_draintank_30 = true;
		leegpompmotor_26 = true;
		
		
	}
	
	private void leegpompen_Stop() {
		// schakelklep 30 en motor 26
		// regelen tussen deze waarden
		schakelklep_draintank_30 = false;
		leegpompmotor_26 = false;
		
	}



	



	private void bedrijf() {
		// opstart sequentie filterunit
		// opstart unit 17 28 + 09
		
		
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
		
		if(temp_olie <= temp_olie_min){
			verwarming_olie = true;
		}
		if(temp_olie >= temp_olie_max){
			verwarming_olie = false;
		}
		
	}



	private void standby() {
		// afsluit sequentie filterunit
		
		
	}

}
