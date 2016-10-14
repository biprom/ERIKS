package com.biprom.eriks.telem.consumer.config;

import com.biprom.eriks.telem.consumer.model.SensorReading;
import com.biprom.eriks.telem.consumer.util.serde.MeasurementDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kristof
 *         Created on 14/10/16.
 */
@SpringBootApplication
@ComponentScan("com.biprom.eriks.telem.consumer")
@EnableKafka
public class TelemConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelemConsumerApplication.class, args);
	}

	@Bean
	public ConsumerFactory<String, SensorReading> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfig());
	}

	@Bean
	public Map<String, Object> consumerConfig() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "eriks.liquinno.com:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "eriks-group");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MeasurementDeserializer.class);
		return props;
	}

	@Bean
	KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, SensorReading>>
	kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, SensorReading> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setConcurrency(3);
		factory.getContainerProperties().setPollTimeout(3000);
		return factory;
	}


}
