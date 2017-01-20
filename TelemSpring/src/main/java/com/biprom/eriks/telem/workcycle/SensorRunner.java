package com.biprom.eriks.telem.workcycle;

import com.biprom.eriks.telem.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Kristof
 *         Created on 18/12/2016.
 */
@Service
public class SensorRunner {

	
	private Thread thread;

	Sensor sensor;

	@Autowired
	public SensorRunner(SensorService sensorService, WorkcycleRunner wc) {
		sensor = new Sensor(sensorService, wc);
		thread = new Thread(sensor);
	}

	@PostConstruct
	public void init() {
		thread.start();
	}

	@PreDestroy
	public void destroy() {
		if (sensor != null) {
			sensor.stop();
		}
	}

}
