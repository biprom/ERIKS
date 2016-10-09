package com.biprom.eriks.telem.config;

import com.biprom.eriks.telem.model.Measurement;
import com.biprom.eriks.telem.util.serde.MeasurementSerializer;
import com.vaadin.spring.annotation.EnableVaadin;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableVaadin
@ComponentScan("com.biprom.eriks.telem")
@EnableAspectJAutoProxy
@EnableKafka
@EnableScheduling
public class TelemSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelemSpringApplication.class, args);
	}


	@Bean
	public ProducerFactory<String, Measurement> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "eriks.liquinno.com:9092");
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "client-1");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MeasurementSerializer.class);
		return props;
	}

	@Bean
	public KafkaTemplate<String, Measurement> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());

	}


}
