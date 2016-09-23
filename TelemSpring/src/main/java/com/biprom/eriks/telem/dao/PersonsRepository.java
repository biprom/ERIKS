package com.biprom.eriks.telem.dao;

import com.biprom.eriks.telem.model.Person;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository(value = "personsRepository")
public interface PersonsRepository extends PagingAndSortingRepository<Person, String> {

	Person findByLoginName(String loginName);


}
