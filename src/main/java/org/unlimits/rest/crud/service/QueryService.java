package org.unlimits.rest.crud.service;

import static org.unlimits.rest.constants.RestConstant.ORDER_BY;
import static org.unlimits.rest.constants.RestConstant.SORT;
import static org.unlimits.rest.constants.RestConstant.SORT_ORDER;

import java.lang.reflect.Field;
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
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.unlimits.rest.crud.beans.PageDetail;
import org.unlimits.rest.filters.FilterPredicate;
import org.unlimits.rest.repository.CustomPredicate;
import org.unlimits.rest.repository.CustomRepository;
import org.unlimits.rest.spec.CurdSpecification;
import org.unlimits.rest.util.ReflectionDBUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface QueryService<DT, EN, ID> extends CQRSService<DT, EN, ID> {

	public static final String EMPTY = "";
	public static final String NULL = "null";
	public static final String NOT = "!";
	public static final String PERCENTAGE = "%";

	default Pageable getPageRequest(int pageNumber, int count) {
		return PageRequest.of(pageNumber, count);
	}

	default Pageable getPageRequest(int pageNumber, int count, Sort sort) {
		return PageRequest.of(pageNumber, count, sort);
	}

	default void preFetch(Map<String, List<String>> headers, Map<String, Object> filters) {

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

	default List<DT> findAllById(List<ID> ids, Map<String, List<String>> headers, Map<String, Object> filters) {
		preFetch(headers, filters);
		return postFetch(getRepository().findAllById(ids));
	}

	default List<DT> findAllById(List<ID> ids, MultiValueMap<String, String> headers, Map<String, Object> sortOrders,
			Map<String, Object> filters) {
		List<org.springframework.data.domain.Sort.Order> buidOrders = buidOrders(sortOrders);
		if (CollectionUtils.isEmpty(buidOrders)) {
			return findAllById(ids, headers, filters);
		} else {
			return findAllById(ids, headers, Sort.by(buidOrders), filters);
		}
	}

	default List<DT> findAllById(List<ID> ids, MultiValueMap<String, String> headers, Sort sort,
			Map<String, Object> filters) {
		preFetch(headers, filters);
		List<EN> findAllById = getRepository().findAllById(ids);
		List<DT> postFetch = postFetch(findAllById);
		return postFetch;
	}

	default List<Sort.Order> buidOrders(Map<String, Object> sortOrders) {
		List<Sort.Order> orders=new ArrayList<Sort.Order>();
		Object sort = sortOrders.get(SORT);
		Object orderBy = sortOrders.get(ORDER_BY);
		Object sortOrder = sortOrders.get(SORT_ORDER);
		if (StringUtil.hasText(sort)) {
			if(sort instanceof List<?>) {
				@SuppressWarnings("unchecked")
				List<Object> sortList= (List<Object>) sort;
				for(Object sortObject : sortList) {
					Order order = getOrder(sortObject);
					if(order!=null) {
						orders.add(order);
					}
				}
			} else {
				Order order = getOrder(sort);
				if(order!=null) {
					orders.add(order);
				}
			}
		} else if (StringUtil.hasText(orderBy)) {
			Order order= getOrder(sortOrder, orderBy);
			if(order!=null) {
				orders.add(order);
			}
		}
		return orders;
	}

	default Order getOrder(Object sortOrder, Object orderBy) {
		String findSortingKey = findSortingKey(orderBy.toString());
		Order order=  null;
		if(StringUtil.isNonEmpty(findSortingKey)) {
			return StringUtil.hasText(sortOrder) ? 
				new Order(Direction.ASC, findSortingKey): 
				new Order(Direction.fromString(sortOrder.toString().toUpperCase()), findSortingKey);
		}
		return order;
	}

	default Order getOrder(Object sort) {
		String[] sortArray = sort.toString().split(":");
		Order order=  null;
		if(sortArray.length==1 ) {
			String findSortingKey = findEntityKey(sortArray[0].trim());
			if(StringUtil.isNonEmpty(findSortingKey)) {
			   order = new Order(Direction.ASC, findSortingKey);
			}
		} else {
			String findSortingKey = findEntityKey(sortArray[0].trim());
			if(StringUtil.isNonEmpty(findSortingKey)) {
			   order = new Order(Direction.fromString(sortArray[1].trim().toUpperCase()), findSortingKey);
			}
		}
		return order;
	}

	default List<DT> findAll(Map<String, List<String>> headers, Map<String, Object> filters) {
		preFetch(headers, filters);
		List<EN> findObjects = repositoryFindAll(headers, filters);
		return postFetch(findObjects);
	}

	default List<DT> findAll(Map<String, List<String>> headers, Map<String, Object> sortOrders,
			Map<String, Object> filters) {
		List<org.springframework.data.domain.Sort.Order> buidOrders = buidOrders(sortOrders);
		if (CollectionUtils.isEmpty(buidOrders)) {
			return findAll(headers, filters);
		} else {
			return findAll(headers, Sort.by(buidOrders), filters);
		}
	}

	default List<DT> findAll(Map<String, List<String>> headers, Sort sort, Map<String, Object> filters) {
		preFetch(headers, filters);
		List<EN> findObjects = repositoryFindAll(headers, sort, filters);
		return postFetch(findObjects);
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

	default PageDetail<DT> fetchPageObject(Map<String, List<String>> headers, int pageNumber, int count,
			Map<String, Object> filters) {
		preFetch(headers, filters);
		Pageable pageable = getPageRequest(pageNumber, count);
		Page<EN> page = repositoryFindAll(headers, pageable, filters);
		List<DT> reslist = postFetch(page.toList());
		PageDetail<DT> responseDto = new PageDetail<DT>();
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

	default PageDetail<DT> fetchPageObject(Map<String, List<String>> headers, int pageNumber, int count,
			Map<String, Object> sortOrders, Map<String, Object> filters) {
		List<org.springframework.data.domain.Sort.Order> buidOrders = buidOrders(sortOrders);
		if (CollectionUtils.isEmpty(buidOrders)) {
			return fetchPageObject(headers, pageNumber, count, filters);
		} else {
			return fetchPageObject(headers, pageNumber, count, Sort.by(buidOrders), filters);
		}
	}

	default PageDetail<DT> fetchPageObject(Map<String, List<String>> headers, int pageNumber, int count, Sort sort,
			Map<String, Object> filters) {
		preFetch(headers, filters);
		Pageable pageable = getPageRequest(pageNumber, count, sort);
		Page<EN> page = repositoryFindAll(headers, pageable, filters);
		List<DT> reslist = postFetch(page.toList());
		PageDetail<DT> responseDto = new PageDetail<DT>();
		responseDto.setPageCount(page.getNumber());
		responseDto.setTotalCount(page.getTotalElements());
		responseDto.setTotalPages(page.getTotalPages());
		responseDto.setElements(reslist);
		return responseDto;
	}

	default List<DT> fetchPageList(Map<String, List<String>> headers, int pageNumber, int count,
			Map<String, Object> filters) {
		preFetch(headers, filters);
		Pageable pageable = getPageRequest(pageNumber, count);
		Page<EN> page = repositoryFindAll(headers, pageable, filters);
		List<DT> dataList = postFetch(page.toList());
		return dataList;
	}

	default List<DT> fetchPageList(Map<String, List<String>> headers, int pageNumber, int count,
			Map<String, Object> sortOrders, Map<String, Object> filters) {
		List<org.springframework.data.domain.Sort.Order> buidOrders = buidOrders(sortOrders);
		if (CollectionUtils.isEmpty(buidOrders)) {
			return fetchPageList(headers, pageNumber, count, filters);
		} else {
			return fetchPageList(headers, pageNumber, count, Sort.by(buidOrders), filters);
		}
	}

	default List<DT> fetchPageList(Map<String, List<String>> headers, int pageNumber, int count, Sort sort,
			Map<String, Object> filters) {
		preFetch(headers, filters);
		Pageable pageable = getPageRequest(pageNumber, count, sort);
		Page<EN> page = repositoryFindAll(headers, pageable, filters);
		List<DT> dataList = postFetch(page.toList());
		return dataList;
	}

	/**
	 * @param root
	 * @param query
	 * @param criteriaBuilder
	 * @param filter
	 * @return
	 */
	default Predicate build(Type type, Root<EN> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder,
			FilterPredicate filter) {
		Map<String, CustomPredicate<EN>> customSortingMap = getCustomPredicateMap();
		if(!CollectionUtils.isEmpty(customSortingMap)) {
			CustomPredicate<EN> customQuery = customSortingMap.get(filter.getColumnName());
			if (customQuery != null) {
				return customQuery.build(type, root, query, criteriaBuilder, filter);
			}
		}
		String fieldName = ReflectionDBUtil.getFieldName(type, filter.getColumnName());
		Field field = ReflectionDBUtil.getField(type, filter.getColumnName());
		if (StringUtil.isNonEmpty(fieldName) && filter.getColumnValue() != null) {
			if (filter.getColumnValue() instanceof List<?>) {
				@SuppressWarnings("unchecked")
				List<Object> values = (List<Object>) filter.getColumnValue();
				Path<Object> path = root.get(fieldName);
				In<Object> fieldNameIn = criteriaBuilder.in(path);
				fieldNameIn.as(field.getType());
				fieldNameIn.value(values);
				List<Object> empltylist = values.stream().filter(value -> isNullOrEmpty(value)).toList();
				if (!empltylist.isEmpty()) {
					return criteriaBuilder.or(fieldNameIn, criteriaBuilder.isNull(path));
				}
				return fieldNameIn;
			}
			if (filter.getColumnValue().toString().startsWith(NOT)) {
				String value = filter.getColumnValue().toString().replace(NOT, EMPTY);
				Expression<String> path = root.get(fieldName);
				Predicate notLike = criteriaBuilder.notLike(path, PERCENTAGE + value + PERCENTAGE);
				if (isNullOrEmpty(value)) {
					return criteriaBuilder.or(notLike, criteriaBuilder.isNull(path));
				}
				return notLike;
			}
			Object value = ReflectionDBUtil.casting(type, fieldName, filter.getColumnValue().toString());
			Expression<String> path = root.get(fieldName);
			Predicate like = criteriaBuilder.like(path, PERCENTAGE + value + PERCENTAGE);
			if (isNullOrEmpty(value)) {
				return criteriaBuilder.or(like, criteriaBuilder.isNull(path));
			}
			return like;
		} else {
			System.out.println("Invalid config : " + filter.getColumnName());
			return null;
		}
	}

	default boolean isNullOrEmpty(Object value) {
		return value == null || EMPTY.equalsIgnoreCase(value.toString()) || NULL.equalsIgnoreCase(value.toString());
	}

	default void addCustomPredicate(String key, CustomPredicate<EN> customPredicate) {
		getCustomPredicateMap().put(key, customPredicate);
	}

	/**
	 * @return
	 */
	default Map<String, CustomPredicate<EN>> getCustomPredicateMap() {
		return new HashMap<String, CustomPredicate<EN>>();
	}

	/**
	 * @return
	 */
	default Map<String, String> getCustomSortingMap() {
		return new HashMap<String, String>();
	}

	public default String findSortingKey(String orderBy) {
		Map<String, String> customSortingMap = this.getCustomSortingMap();
		if(!CollectionUtils.isEmpty(customSortingMap)) {
			String findCustomProperty = customSortingMap.get(orderBy);
			if (StringUtil.isNonEmpty(findCustomProperty)) {
				return findCustomProperty;
			}
		}

		String findEntityProperty = this.findEntityKey(orderBy);
		if (StringUtil.isNonEmpty(findEntityProperty)) {
			return findEntityProperty;
		}
		
		return this.getPrimaryKey();
	}

}
