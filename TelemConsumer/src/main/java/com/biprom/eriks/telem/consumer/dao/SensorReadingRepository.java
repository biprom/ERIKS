package com.biprom.eriks.telem.consumer.dao;

import com.biprom.eriks.telem.consumer.model.SensorReading;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Kristof
 *         Created on 14/10/16.
 */
@Repository("sensorReadingRepository")
public interface SensorReadingRepository extends PagingAndSortingRepository<SensorReading, String> {
}
