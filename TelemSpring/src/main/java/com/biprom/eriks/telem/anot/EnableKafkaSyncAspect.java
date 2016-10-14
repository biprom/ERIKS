package com.biprom.eriks.telem.anot;

import com.biprom.eriks.telem.dao.SensorReadingRepository;
import com.biprom.eriks.telem.model.SensorReading;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @author Kristof
 *         Created on 27/09/16.
 */
@Aspect
@Component
public class EnableKafkaSyncAspect {

	@Autowired
	KafkaTemplate<String, SensorReading> kafkaTemplate;

	@Autowired
	SensorReadingRepository sensorReadingRepository;


	@AfterReturning(
			pointcut = "@annotation(EnableKafkaSync)",
			returning = "retVal")
	public void myFirstPointcut(Object retVal) {

		if (retVal instanceof SensorReading) {
			final SensorReading m = (SensorReading) retVal;

			final ListenableFuture<SendResult<String, SensorReading>> future = kafkaTemplate.send("eriks", m);

			future.addCallback(new ListenableFutureCallback<SendResult<String, SensorReading>>() {
				@Override
				public void onSuccess(SendResult<String, SensorReading> stringMeasurementSendResult) {
					sensorReadingRepository.updateSynchronised(m.getId(), Boolean.TRUE);
				}

				@Override
				public void onFailure(Throwable throwable) {
					sensorReadingRepository.updateSynchronised(m.getId(), Boolean.FALSE);
				}
			});


		}


	}

}
