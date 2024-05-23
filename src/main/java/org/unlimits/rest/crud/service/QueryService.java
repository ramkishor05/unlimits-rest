package org.unlimits.rest.crud.service;

import java.util.List;
import java.util.Map;

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
	 * @param ids
	 * @return
	 */
	List<DT> findAllById(List<ID> ids);
	/**
	 * 
	 * @return
	 */
	List<DT> findAll(Map<String, List<String>> headers);
	
	/**
	 * 
	 * @return
	 */
	List<DT> findAll(Map<String, List<String>> headers,Sort sort);
	
	/**
	 * @param pageNumber
	 * @param count
	 * @return
	 */
	PageDetail fetchPageObject(Map<String, List<String>> headers, int pageNumber, int count);

	/**
	 * @param pageNumber
	 * @param count
	 * @return
	 */
	List<DT> fetchPageList(Map<String, List<String>> headers, int pageNumber, int count);

	/**
	 * @param pageNumber
	 * @param count
	 * @param sort
	 * @return
	 */
	PageDetail fetchPageObject(Map<String, List<String>> headers, int pageNumber, int count, Sort sort);

	/**
	 * @param pageNumber
	 * @param count
	 * @param sort
	 * @return
	 */
	List<DT> fetchPageList(Map<String, List<String>> headers, int pageNumber, int count, Sort sort);
	
	
}
