package org.unlimits.rest.crud.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.unlimits.rest.crud.beans.PageDetail;

public interface QueryService<DT, EN, ID>  extends CQRSService<DT, EN, ID>{
	/**
	 * 
	 * @param uuid
	 * @return
	 */
	DT findById(ID uuid);
	/**
	 * 
	 * @return
	 */
	List<DT> findAll();
	
	/**
	 * 
	 * @return
	 */
	List<DT> findAll(Sort sort);
	
	/**
	 * @param pageNumber
	 * @param count
	 * @return
	 */
	PageDetail fetchPageObject(int pageNumber, int count);

	/**
	 * @param pageNumber
	 * @param count
	 * @return
	 */
	List<DT> fetchPageList(int pageNumber, int count);

	/**
	 * @param pageNumber
	 * @param count
	 * @param sort
	 * @return
	 */
	PageDetail fetchPageObject(int pageNumber, int count, Sort sort);

	/**
	 * @param pageNumber
	 * @param count
	 * @param sort
	 * @return
	 */
	List<DT> fetchPageList(int pageNumber, int count, Sort sort);
	/**
	 * @param ids
	 * @return
	 */
	List<DT> findAllById(List<ID> ids);
	
}
