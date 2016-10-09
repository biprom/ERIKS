package com.biprom.eriks.telem.dao;

import com.biprom.eriks.telem.model.Measurement;
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
@Repository(value = "measurementRepository")
public interface MeasurementRepository extends PagingAndSortingRepository<Measurement, String>, MeasurementRepositoryCustom {

	List<Measurement> findByTypeAndTimeBetween(Measurement.MeasurementType type, Date from, Date to);

	List<Measurement> findByType(Measurement.MeasurementType type);

	@Query(value = "{ 's' : false }")
	List<Measurement> findUnsynched();

	List<Measurement> findByTimeBeforeAndSynched(LocalDateTime before, Boolean synched);
}
