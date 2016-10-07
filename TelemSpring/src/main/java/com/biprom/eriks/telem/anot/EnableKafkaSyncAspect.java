package com.biprom.eriks.telem.anot;

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
	KafkaTemplate<Integer, String> kafkaTemplate;


	@AfterReturning(
			pointcut = "@annotation(EnableKafkaSync) && execution(* *(..))",
			returning = "retVal")
	public void myFirstPointcut(Object retVal) {
		System.out.println("sending the measurement");
		final ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send("eriks", 0, "1234");
		future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
			@Override
			public void onFailure(Throwable throwable) {
				System.out.println("onFailure");
			}

			@Override
			public void onSuccess(SendResult<Integer, String> integerStringSendResult) {
				System.out.println("onSuccess");
			}
		});
	}

}
