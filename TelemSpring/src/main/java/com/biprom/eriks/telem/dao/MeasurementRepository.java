package com.biprom.eriks.telem.dao;

import com.biprom.eriks.telem.model.Measurement;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Kristof
 *         Created on 06/10/16.
 */
@Repository(value = "measurementRepository")
public interface MeasurementRepository extends PagingAndSortingRepository<Measurement, String> {

	List<Measurement> findByTAndDBetween(Measurement.MeasurementType type, Date from, Date to);

	List<Measurement> findByT(Measurement.MeasurementType type);

}
