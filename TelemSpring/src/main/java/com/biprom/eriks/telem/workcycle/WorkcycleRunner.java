package com.biprom.eriks.telem.workcycle;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * @author Kristof
 *         Created on 18/12/2016.
 */
@Service
public class WorkcycleRunner {

	private Thread thread;

	WorkCycle wc;

	public WorkcycleRunner() {
		wc = new WorkCycle();
		thread = new Thread(wc);
	}
	
	public void setTemperatureMinMax(Double min, Double max){
		wc.setTempMinMax(min, max);
	}

	@PostConstruct
	public void init() {
		thread.start();
	}

	@PreDestroy
	public void destroy() {
		if (wc != null) {
			wc.stop();
		}
	}

	public Double getMaxTempValue() {
		return wc.getMaxTemp();
	}

	public Double getMinTempValue() {
		return wc.getMinTemp();
	}
	
	public boolean isRunning(){
		return wc.isRunning();
	}
}
