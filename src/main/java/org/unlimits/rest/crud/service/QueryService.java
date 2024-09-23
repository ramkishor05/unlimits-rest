package org.unlimits.rest.crud.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.unlimits.rest.crud.beans.PageDetail;
import org.unlimits.rest.filters.FilterPredicate;
import org.unlimits.rest.repository.CustomRepository;
import org.unlimits.rest.spec.CurdSpecification;

public interface QueryService<DT, EN, ID> extends CQRSService<DT, EN, ID> {

	default Pageable getPageRequest(int pageNumber, int count) {
		return PageRequest.of(pageNumber, count);
	}

	default Pageable getPageRequest(int pageNumber, int count, Sort sort) {
		return PageRequest.of(pageNumber, count, sort);
	}

	default void preFetch(Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		preFetch(headers, filters);
	}
	
	default void preFetch(Map<String, List<String>> headers, Map<String, Object> filters) {
		preFetch(headers);
	}
	
	default void preFetch(Map<String, List<String>> headers) {

	}

	default List<DT> postFetch(List<EN> findObjects, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		List<DT> list = new ArrayList<DT>();
		for (EN findObject : findObjects) {
			DT dtoObject = getMapper().mapToDTO(findObject);
			list.add(dtoObject);
			postFetch(findObject, dtoObject, headers,  filters, actions);
		}
		return list;
	}
	
	default List<DT> postFetch(List<EN> findObjects, Map<String, List<String>> headers, Map<String, Object> filters) {
		List<DT> list = new ArrayList<DT>();
		for (EN findObject : findObjects) {
			DT dtoObject = getMapper().mapToDTO(findObject);
			list.add(dtoObject);
			postFetch(findObject, dtoObject, headers, filters);
		}
		return list;
	}
	
	default List<DT> postFetch(List<EN> findObjects, Map<String, List<String>> headers) {
		List<DT> list = new ArrayList<DT>();
		for (EN findObject : findObjects) {
			DT dtoObject = getMapper().mapToDTO(findObject);
			list.add(dtoObject);
			postFetch(findObject, dtoObject, headers);
		}
		return list;
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

	default List<DT> findAllById(List<ID> ids, Map<String, List<String>> headers, Map<String, Object> sortOrders,
			Map<String, Object> filters, Map<String, Object> actions) {
		List<org.springframework.data.domain.Sort.Order> buidOrders = buidOrders(sortOrders);
		if (CollectionUtils.isEmpty(buidOrders)) {
			return findAllById(ids, headers, filters, actions);
		} else {
			return findAllById(ids, headers, filters, actions , Sort.by(buidOrders));
		}
	}

	default List<DT> findAllById(List<ID> ids, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		preFetch(headers, filters, actions);
		return postFetch(getRepository().findAllById(ids), headers, filters, actions);
	}

	default List<DT> findAllById(List<ID> ids, Map<String, List<String>> headers,
			Map<String, Object> filters, Map<String, Object> actions, Sort sort) {
		preFetch(headers, filters, actions);
		List<EN> findAllById = getRepository().findAllById(ids);
		List<DT> postFetch = postFetch(findAllById, headers, filters, actions);
		return postFetch;
	}


	default List<DT> findAll(Map<String, List<String>> headers,
			Map<String, Object> filters, Map<String, Object> actions , Map<String, Object> sortOrders) {
		List<org.springframework.data.domain.Sort.Order> buidOrders = buidOrders(sortOrders);
		if (CollectionUtils.isEmpty(buidOrders)) {
			return findAll(headers, filters, actions);
		} else {
			return findAll(headers,  filters, actions, Sort.by(buidOrders));
		}
	}

	default List<DT> findAll(Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		preFetch(headers, filters, actions);
		List<EN> findObjects = repositoryFindAll(headers, filters);
		return postFetch(findObjects, headers, filters, actions);
	}
	
	default List<DT> findAll(Map<String, List<String>> headers, Map<String, Object> filters) {
		preFetch(headers, filters);
		List<EN> findObjects = repositoryFindAll(headers, filters);
		return postFetch(findObjects, headers, filters);
	}

	default List<DT> findAll(Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions, Sort sort) {
		preFetch(headers, filters, actions);
		List<EN> findObjects = repositoryFindAll(headers, sort, filters);
		return postFetch(findObjects, headers, filters, actions);
	}

	default List<EN> repositoryFindAll(Map<String, List<String>> headers, Map<String, Object> filters) {
		if (getRepository() instanceof CustomRepository<EN, ID>) {
			CustomRepository<EN, ID> customRepository = ((CustomRepository<EN, ID>) getRepository());
			if (!CollectionUtils.isEmpty(filters)) {
				List<FilterPredicate> filterList = new ArrayList<FilterPredicate>();
				filters.forEach((key, value) -> {
					filterList.add(new FilterPredicate(key, value));
				});
				if (filterList.isEmpty()) {
					return customRepository.findAll();
				} else {
					CurdSpecification<DT, EN, ID> specification = new CurdSpecification<DT, EN, ID>(this,
							getEntityType(), filterList);
					return customRepository.findAll(specification);
				}
			}
		}
		return getRepository().findAll();
	}

	/**
	 * @return
	 */
	default List<EN> repositoryFindAll(Map<String, List<String>> headers, Sort sort, Map<String, Object> filters) {
		if (getRepository() instanceof CustomRepository<EN, ID>) {
			CustomRepository<EN, ID> customRepository = ((CustomRepository<EN, ID>) getRepository());
			if (!CollectionUtils.isEmpty(filters)) {
				List<FilterPredicate> filterList = new ArrayList<FilterPredicate>();
				filters.forEach((key, value) -> {
					filterList.add(new FilterPredicate(key, value));
				});
				if (filterList.isEmpty()) {
					return customRepository.findAll(sort);
				} else {
					CurdSpecification<DT, EN, ID> specification = new CurdSpecification<DT, EN, ID>(this,
							getEntityType(), filterList);
					return customRepository.findAll(specification, sort);
				}
			}
		}
		return getRepository().findAll(sort);
	}

	/**
	 * @param filters
	 * @return
	 */
	default Page<EN> repositoryFindAll(Map<String, List<String>> headers, Pageable pageable,
			Map<String, Object> filters) {
		if (getRepository() instanceof CustomRepository<EN, ID>) {
			CustomRepository<EN, ID> customRepository = ((CustomRepository<EN, ID>) getRepository());
			if (!CollectionUtils.isEmpty(filters)) {
				List<FilterPredicate> filterList = new ArrayList<FilterPredicate>();
				filters.forEach((key, value) -> {
					filterList.add(new FilterPredicate(key, value));
				});
				if (filterList.isEmpty()) {
					return customRepository.findAll(pageable);
				} else {
					CurdSpecification<DT, EN, ID> specification = new CurdSpecification<DT, EN, ID>(this,
							getEntityType(), filterList);
					return customRepository.findAll(specification, pageable);
				}
			}
		}
		return getRepository().findAll(pageable);
	}

	default PageDetail<DT> fetchPageObject(int pageNumber, int count,
			Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions, Map<String, Object> sortOrders) {
		List<org.springframework.data.domain.Sort.Order> buidOrders = buidOrders(sortOrders);
		if (CollectionUtils.isEmpty(buidOrders)) {
			return fetchPageObject( pageNumber, count, headers,filters, actions);
		} else {
			return fetchPageObject( pageNumber, count,headers, filters, actions, Sort.by(buidOrders));
		}
	}
	

	default PageDetail<DT> fetchPageObject( 
			int pageNumber, 
			int count,
			Map<String, List<String>> headers,
			Map<String, Object> filters, 
			Map<String, Object> actions) {
		preFetch(headers, filters, actions);
		Pageable pageable = getPageRequest(pageNumber, count);
		Page<EN> page = repositoryFindAll(headers, pageable, filters);
		List<DT> reslist = postFetch(page.toList(), headers, filters, actions);
		PageDetail<DT> responseDto = new PageDetail<DT>();
		responseDto.setPageCount(page.getNumber());
		responseDto.setTotalCount(page.getTotalElements());
		responseDto.setTotalPages(page.getTotalPages());
		responseDto.setElements(reslist);
		return responseDto;
	}


	default PageDetail<DT> fetchPageObject(
			int pageNumber, 
			int count,
			Map<String, List<String>> headers, 
			Map<String, Object> filters, 
			Map<String, Object> actions, 
			Sort sort) {
		preFetch(headers, filters, actions);
		Pageable pageable = getPageRequest(pageNumber, count, sort);
		Page<EN> page = repositoryFindAll(headers, pageable, filters);
		List<DT> reslist = postFetch(page.toList(),headers, filters, actions);
		PageDetail<DT> responseDto = new PageDetail<DT>();
		responseDto.setPageCount(page.getNumber());
		responseDto.setTotalCount(page.getTotalElements());
		responseDto.setTotalPages(page.getTotalPages());
		responseDto.setElements(reslist);
		return responseDto;
	}

	default List<DT> fetchPageList(
			int pageNumber, 
			int count,
			Map<String, List<String>> headers, 
			Map<String, Object> filters, 
			Map<String, Object> actions, 
			Map<String, Object> sortOrders) {
		List<org.springframework.data.domain.Sort.Order> buidOrders = buidOrders(sortOrders);
		if (CollectionUtils.isEmpty(buidOrders)) {
			return fetchPageList(pageNumber, count, headers, filters, actions);
		} else {
			return fetchPageList(pageNumber, count, headers, filters, actions, Sort.by(buidOrders));
		}
	}

	default List<DT> fetchPageList(
			int pageNumber, 
			int count,
			Map<String, List<String>> headers, 
			Map<String, Object> filters, 
			Map<String, Object> actions) {
		preFetch(headers, filters, actions);
		Pageable pageable = getPageRequest(pageNumber, count);
		Page<EN> page = repositoryFindAll(headers, pageable, filters);
		List<DT> dataList = postFetch(page.toList(),headers, filters, actions);
		return dataList;
	}


	default List<DT> fetchPageList(
			int pageNumber, 
			int count, 
			Map<String, List<String>> headers, 
			Map<String, Object> filters, 
			Map<String, Object> actions,
			Sort sort) {
		preFetch(headers, filters, actions);
		Pageable pageable = getPageRequest(pageNumber, count, sort);
		Page<EN> page = repositoryFindAll(headers, pageable, filters);
		List<DT> dataList = postFetch(page.toList(), headers, filters, actions);
		return dataList;
	}


}
