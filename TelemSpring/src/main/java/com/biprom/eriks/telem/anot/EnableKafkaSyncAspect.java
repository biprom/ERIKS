package com.biprom.eriks.telem.anot;

import com.biprom.eriks.telem.dao.MeasurementRepository;
import com.biprom.eriks.telem.model.Measurement;
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
	KafkaTemplate<String, Measurement> kafkaTemplate;

	@Autowired
	MeasurementRepository measurementRepository;


	@AfterReturning(
			pointcut = "@annotation(EnableKafkaSync)",
			returning = "retVal")
	public void myFirstPointcut(Object retVal) {

		if (retVal instanceof Measurement) {
			final Measurement m = (Measurement) retVal;
			final ListenableFuture<SendResult<String, Measurement>> future = kafkaTemplate.send("eriks", m);

			future.addCallback(new ListenableFutureCallback<SendResult<String, Measurement>>() {
				@Override
				public void onSuccess(SendResult<String, Measurement> stringMeasurementSendResult) {
					measurementRepository.updateSynchronised(m.getId(), Boolean.TRUE);
				}

				@Override
				public void onFailure(Throwable throwable) {
					measurementRepository.updateSynchronised(m.getId(), Boolean.FALSE);
				}
			});


		}


	}

}
