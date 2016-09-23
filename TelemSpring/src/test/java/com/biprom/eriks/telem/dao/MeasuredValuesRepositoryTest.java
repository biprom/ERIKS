package com.biprom.eriks.telem.dao;

import com.biprom.eriks.telem.model.MeasuredValues;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Kristof
 *         Created on 27/08/16.
 */
public class MeasuredValuesRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	MeasuredValuesRepository measuredValuesRepository;


	Date now = new Date();

	@Test
	public void saveMeasureValuesRepository_ShouldReturn_SavedObject() {
		MeasuredValues mv = new MeasuredValues();
		mv.setAnalogValuesRead(new Double[]{1d, 2d, 3d, 4d});
		mv.setBinValuesRead(new Boolean[]{true, true, false, true});
		mv.setD(now);

		final MeasuredValues save = measuredValuesRepository.save(mv);
		Assert.assertNotNull(save.getId());
	}

	@Test
	public void findByD_ShouldReturn_ObjectList() {
		// Be sure there is an object in the database first!
		saveMeasureValuesRepository_ShouldReturn_SavedObject();
		// Search the object
		final List<MeasuredValues> byD = measuredValuesRepository.findByDBetween(getStartOfDay(now), getEndOfDay(now));
		Assert.assertNotNull(byD);
		Assert.assertTrue(byD.size() > 0);
	}

	private Date getEndOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

	private Date getStartOfDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}


}
