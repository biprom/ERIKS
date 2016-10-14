package com.biprom.eriks.telem.consumer.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author Kristof
 *         Created on 06/01/16.
 */
@Configuration
@EnableMongoRepositories(value = "com.biprom.eriks.telem.consumer.dao")
public class MongoConfiguration extends AbstractMongoConfiguration {

	@Value("${database.name}")
	String databaseName;

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient();
	}

	@Bean
	public GridFsTemplate gridFsTemplate() throws Exception {
		return new GridFsTemplate(mongoDbFactory(), mappingMongoConverter());
	}


}
