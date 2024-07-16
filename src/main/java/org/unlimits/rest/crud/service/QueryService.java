package org.unlimits.rest.crud.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.brijframework.util.text.StringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.unlimits.rest.crud.beans.PageDetail;
import org.unlimits.rest.filters.FilterPredicate;
import org.unlimits.rest.repository.CustomPredicate;
import org.unlimits.rest.repository.CustomRepository;
import org.unlimits.rest.spec.CurdSpecification;
import org.unlimits.rest.util.ReflectionDBUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface QueryService<DT, EN, ID>  extends CQRSService<DT, EN, ID>{
	
    default Pageable getPageRequest(int pageNumber, int count){
		return PageRequest.of(pageNumber, count);
	}
	
    default Pageable getPageRequest(int pageNumber, int count, Sort sort){
		return PageRequest.of(pageNumber, count , sort);
	}
    
	default List<DT> postFetch(List<EN> findObjects) {
		List<DT> list = new ArrayList<DT>();
		for (EN findObject : findObjects) {
			DT dtoObject = getMapper().mapToDTO(findObject);
			list.add(dtoObject);
			postFetch(findObject, dtoObject);
		}
		return list;
	}
	default List<DT> findAllById(List<ID> ids) {
		return postFetch(getRepository().findAllById(ids));
	}

	default List<DT> findAll(Map<String, List<String>> headers, Map<String, String> filters) {
		List<EN> findObjects = repositoryFindAll(headers, filters);
		return postFetch(findObjects);
	}

	default List<EN> repositoryFindAll(Map<String, List<String>> headers, Map<String, String> filters) {
		if (getRepository() instanceof CustomRepository<EN, ID>) {
			CustomRepository<EN, ID> customRepository= ((CustomRepository<EN, ID>) getRepository());
			if (!CollectionUtils.isEmpty(filters)) {
				List<FilterPredicate> filterList=new ArrayList<FilterPredicate>();
				filters.forEach((key, value)->{
					filterList.add(new FilterPredicate(key, value));
				});
				if(filterList.isEmpty()) {
					return customRepository.findAll();
				} else {
					CurdSpecification<DT, EN, ID> specification=new CurdSpecification<DT, EN, ID>( this,getEntityType(),filterList);
					return customRepository.findAll(specification);
				}
			}
		}
		return getRepository().findAll();
	}

	default List<DT> findAll(Map<String, List<String>> headers, Sort sort,Map<String, String> filters) {
		List<EN> findObjects = repositoryFindAll(headers, sort, filters);
		return postFetch(findObjects);
	}
	
	/**
	 * @return
	 */
	default List<EN> repositoryFindAll(Map<String, List<String>> headers, Sort sort, Map<String, String> filters) {
		if (getRepository() instanceof CustomRepository<EN, ID>) {
			CustomRepository<EN, ID> customRepository= ((CustomRepository<EN, ID>) getRepository());
			if (!CollectionUtils.isEmpty(filters)) {
				List<FilterPredicate> filterList=new ArrayList<FilterPredicate>();
				filters.forEach((key, value)->{
					filterList.add(new FilterPredicate(key, value));
				});
				if(filterList.isEmpty()) {
					return customRepository.findAll(sort);
				} else {
					CurdSpecification<DT, EN, ID> specification=new CurdSpecification<DT, EN, ID>( this,getEntityType(),filterList);
					return customRepository.findAll(specification, sort);
				}
			}
		}
		return getRepository().findAll(sort);
	}

	
	default PageDetail fetchPageObject(Map<String, List<String>> headers, int pageNumber, int count, Map<String, String> filters) {
		Pageable pageable = getPageRequest(pageNumber, count);
		Page<EN> page = repositoryFindAll(headers, pageable, filters);
		List<DT> reslist = postFetch(page.toList());
		PageDetail responseDto = new PageDetail();
		responseDto.setPageCount(page.getNumber());
		responseDto.setTotalCount(page.getTotalElements());
		responseDto.setTotalPages(page.getTotalPages());
		responseDto.setElements(reslist);
		return responseDto;
	}
	
	/**
	 * @param filters 
	 * @return
	 */
	default Page<EN> repositoryFindAll(Map<String, List<String>> headers,Pageable pageable, Map<String, String> filters) {
		if (getRepository() instanceof CustomRepository<EN, ID>) {
			CustomRepository<EN, ID> customRepository= ((CustomRepository<EN, ID>) getRepository());
			if (!CollectionUtils.isEmpty(filters)) {
				List<FilterPredicate> filterList=new ArrayList<FilterPredicate>();
				filters.forEach((key, value)->{
					filterList.add(new FilterPredicate(key, value));
				});
				if(filterList.isEmpty()) {
					return customRepository.findAll(pageable);
				} else {
					CurdSpecification<DT, EN, ID> specification=new CurdSpecification<DT, EN, ID>( this,getEntityType(),filterList);
					return customRepository.findAll(specification,pageable);
				}
			}
		}
		return getRepository().findAll(pageable);
	}

	default PageDetail fetchPageObject(Map<String, List<String>> headers, int pageNumber, int count, Sort sort, Map<String, String> filters) {
		Pageable pageable = getPageRequest(pageNumber, count, sort);
		Page<EN> page = repositoryFindAll(headers, pageable, filters);
		List<DT> reslist = postFetch(page.toList());
		PageDetail responseDto = new PageDetail();
		responseDto.setPageCount(page.getNumber());
		responseDto.setTotalCount(page.getTotalElements());
		responseDto.setTotalPages(page.getTotalPages());
		responseDto.setElements(reslist);
		return responseDto;
	}

	default List<DT> fetchPageList(Map<String, List<String>> headers, int pageNumber, int count, Map<String, String> filters) {
		Pageable pageable = getPageRequest(pageNumber, count);
		Page<EN> page =repositoryFindAll(headers, pageable, filters);
		return postFetch(page.toList());
	}

	default List<DT> fetchPageList(Map<String, List<String>> headers, int pageNumber, int count, Sort sort, Map<String, String> filters) {
		Pageable pageable = getPageRequest(pageNumber, count, sort);
		Page<EN> page = repositoryFindAll(headers,pageable, filters);
		return postFetch(page.toList());
	}
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
	
	default void addCustomPredicate(String key , CustomPredicate<EN> customPredicate) {
		getCustomPredicateMap().put(key, customPredicate);
	}
	
	/**
	 * @return
	 */
	default Map<String, CustomPredicate<EN>> getCustomPredicateMap(){
		return new HashMap<String, CustomPredicate<EN>>();
	}

}
