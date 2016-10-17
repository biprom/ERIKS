package com.biprom.workcycle;

/**
 * Hello world!
 */
public class App {
	public static void main(String[] args) {


		/**
		 * Start workcycle THREAD
		 */
		new WorkCycle().start();

		/**
		 * Start communication to TelemSpring
		 */
		String sensorFile = "/dev/shm/sensor";
		if (args.length == 1) {
			final String arg = args[0];
			if (arg.startsWith("--sensorfile=")) {
				sensorFile = arg.substring(13);
				System.out.println("Using sensorfile: " + sensorFile);
			}
			new Sensor(sensorFile).start();
		}
	}

}
