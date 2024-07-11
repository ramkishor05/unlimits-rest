package org.unlimits.rest.crud.service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.brijframework.util.text.StringUtil;
import org.springframework.data.domain.Sort;
import org.unlimits.rest.crud.beans.PageDetail;
import org.unlimits.rest.filters.FilterPredicate;
import org.unlimits.rest.repository.CustomPredicate;
import org.unlimits.rest.util.ReflectionDBUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

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
	
	/**
	 
	 * @param pageNumber
	 * @param count
	 * @return
	 */
	PageDetail fetchPageObject(int pageNumber, int count, Map<String, String> filters);

	/**
	 * @param pageNumber
	 * @param count
	 * @return
	 */
	List<DT> fetchPageList(int pageNumber, int count, Map<String, String> filters);

	/**
	 * @param pageNumber
	 * @param count
	 * @param sort
	 * @return
	 */
	PageDetail fetchPageObject(int pageNumber, int count, Sort sort, Map<String, String> filters);
	
	/**
	 * @param pageNumber
	 * @param count
	 * @param sort
	 * @return
	 */
	List<DT> fetchPageList(int pageNumber, int count, Sort sort, Map<String, String> filters);
	
	/**
	 * @param root
	 * @param query
	 * @param criteriaBuilder
	 * @param filter
	 * @return
	 */
	default Predicate build(Type type,  Root<EN> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder, FilterPredicate filter){
		CustomPredicate<EN> customQuery = getCustomPredicateMap().get(filter.getColumnName());
		if(customQuery!=null) {
			return customQuery.build(type,root, query, criteriaBuilder, filter);
		}
		String field = ReflectionDBUtil.getFieldName(type, filter.getColumnName());
		if(StringUtil.isNonEmpty(field)) {
			if(filter.getColumnValue().startsWith("!")) {
				String value =filter.getColumnValue().replace("!", "");
				Expression<String> path = root.get(field).as(String.class);
				return criteriaBuilder.notLike(path,"%"+value+"%");
			}
			Object value = ReflectionDBUtil.casting(type, field, filter.getColumnValue());
			Expression<String> path = root.get(field).as(String.class);
			return criteriaBuilder.like(path,"%"+value+"%");
		} else {
			System.out.println("Invalid config : "+filter.getColumnName());
			return null;
		}
	}
	
	/**
	 * @return
	 */
	default Map<String, CustomPredicate<EN>> getCustomPredicateMap(){
		return new HashMap<String, CustomPredicate<EN>>();
	}

}
