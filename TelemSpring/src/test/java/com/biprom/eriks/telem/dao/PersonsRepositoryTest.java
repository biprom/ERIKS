package com.biprom.eriks.telem.dao;

import com.biprom.eriks.telem.model.Person;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author Kristof
 *         Created on 27/08/16.
 */
public class PersonsRepositoryTest extends AbstractRepositoryTest {

	@Autowired
	PersonsRepository personsRepository;

	@Test
	public void savePerson_ShouldReturn_SavedPerson() {
		Person p = new Person();
		p.setFirstName("First Name");
		p.setLastName("Last Name");
		p.setLoginName("loginName");
		p.setPassWord("password");
		p.setPin(1234);
		p.setUserDate(new Date());
		final Person save = personsRepository.save(p);
		Assert.assertNotNull(save.getId());
	}

	@Test
	public void findByLoginName_ShouldReturn_Object() {
		Person p = new Person();
		p.setFirstName("FN");
		p.setLastName("LN");
		p.setLoginName("loginName2");
		p.setPassWord("password");
		p.setPin(2345);
		p.setUserDate(new Date());
		personsRepository.save(p);
		final Person byLoginName = personsRepository.findByLoginName("loginName2");
		Assert.assertEquals(2345, byLoginName.getPin());
	}


}
