package com.biprom.eriks.telem.service;

import com.biprom.eriks.telem.config.MongoConfiguration;
import com.biprom.eriks.telem.config.TelemSpringApplication;
import com.biprom.eriks.telem.model.SensorReading;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author Kristof
 *         Created on 27/09/16.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TelemSpringApplication.class, MongoConfiguration.class})
public class MeasurementServiceTest {

	@Autowired
	SensorService measurementService;

	@Test
	public void pointcutTest() {
		final SensorReading m = new SensorReading();
		m.setTime(new Date());
		m.setValue(41d);
		m.setType("OIL_TEMP_IN");
		measurementService.store(m);
	}


}
