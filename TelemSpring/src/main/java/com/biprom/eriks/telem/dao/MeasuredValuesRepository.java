package com.biprom.eriks.telem.dao;

import com.biprom.eriks.telem.model.MeasuredValues;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository(value = "measuredValuesRepository")
public interface MeasuredValuesRepository extends PagingAndSortingRepository<MeasuredValues, String> {

	List<MeasuredValues> findByDBetween(Date from, Date to);

}
