package com.biprom.eriks.telem.dao;

import com.biprom.eriks.telem.model.SensorReading;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Kristof
 *         Created on 06/10/16.
 */
@Repository(value = "sensorReadingRepository")
public interface SensorReadingRepository extends PagingAndSortingRepository<SensorReading, String>, SensorReadingRepositoryCustom {

	List<SensorReading> findByTypeAndTimeBetween(String type, Date from, Date to);

	List<SensorReading> findByType(String type);

	@Query(value = "{ 's' : false }")
	List<SensorReading> findUnsynched();

	List<SensorReading> findByTimeBeforeAndSynched(LocalDateTime before, Boolean synched);
}
