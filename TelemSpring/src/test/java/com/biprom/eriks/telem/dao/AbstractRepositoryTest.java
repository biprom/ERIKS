package com.biprom.eriks.telem.dao;

import com.biprom.eriks.telem.config.MongoConfiguration;
import com.biprom.eriks.telem.config.TelemSpringApplication;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Kristof
 *         Created on 27/08/16.
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TelemSpringApplication.class, MongoConfiguration.class})
public abstract class AbstractRepositoryTest {


	@Autowired
	@Qualifier("mongoTemplate")
	MongoTemplate mongoTemplate;

	@Before
	public void setUp() throws Exception {
		mongoTemplate.getDb().dropDatabase();
	}

}
