package org.unlimits.rest.crud.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unlimits.rest.crud.mapper.GenericMapper;

public interface CQRSService<DT, EN, ID> {

	/**
	 * 
	 * @return
	 */
	JpaRepository<EN, ID> getRepository();
	/***
	 * 
	 * @return
	 */
	GenericMapper<EN, DT>  getMapper();
}
