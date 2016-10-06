package com.biprom.eriks.telem.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.biprom.eriks.telem.model.CpuConfigBean;
import com.biprom.eriks.telem.model.Person;

@Repository("cpuConfigRepository")
public interface CpuConfigRepository extends PagingAndSortingRepository<CpuConfigBean, String> {
	
	CpuConfigRepository findByIoName(String loginName);

}
