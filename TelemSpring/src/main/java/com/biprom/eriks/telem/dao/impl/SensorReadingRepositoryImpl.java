package com.biprom.eriks.telem.dao.impl;

import com.biprom.eriks.telem.dao.SensorReadingRepositoryCustom;
import com.biprom.eriks.telem.model.SensorReading;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * @author Kristof
 *         Created on 09/10/16.
 */
public class SensorReadingRepositoryImpl implements SensorReadingRepositoryCustom {

	@Autowired
	MongoOperations mongoOperations;

	@Override
	public void updateSynchronised(String id, Boolean s) {
		Query query = new Query(
				Criteria.where("_id").is(new ObjectId(id)));
		Update update = new Update();
		update.set("s", s);
		mongoOperations.updateFirst(query, update, SensorReading.class);
	}

}
