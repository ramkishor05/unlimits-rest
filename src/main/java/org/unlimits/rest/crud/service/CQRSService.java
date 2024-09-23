package org.unlimits.rest.crud.service;

import static org.unlimits.rest.constants.RestConstant.ORDER_BY;
import static org.unlimits.rest.constants.RestConstant.SORT;
import static org.unlimits.rest.constants.RestConstant.SORT_ORDER;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.brijframework.util.reflect.AnnotationUtil;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.support.ReflectionAccess;
import org.brijframework.util.text.StringUtil;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.CollectionUtils;
import org.unlimits.rest.crud.mapper.GenericMapper;
import org.unlimits.rest.filters.FilterPredicate;
import org.unlimits.rest.repository.CustomPredicate;
import org.unlimits.rest.util.ReflectionDBUtil;

import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.CriteriaBuilder.In;

public interface CQRSService<DT, EN, ID> {

	public static final String EMPTY = "";
	public static final String NULL = "null";
	public static final String NOT = "!";
	public static final String PERCENTAGE = "%";

	/**
	 * 
	 * @return
	 */
	JpaRepository<EN, ID> getRepository();

	/***
	 * 
	 * @return
	 */
	GenericMapper<EN, DT> getMapper();

	default Type getEntityType() {
		return getEntityType(getClass());
	}

	default Type getEntityType(Class<?> type) {
		Type[] genericInterfaces = type.getGenericInterfaces();
		if (type.getGenericSuperclass() instanceof ParameterizedType) {
			Type entry = ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments()[1];
			return entry;
		}
		for (Type genericInterface : genericInterfaces) {
			if (genericInterface instanceof ParameterizedType) {
				Type entry = ((ParameterizedType) genericInterface).getActualTypeArguments()[1];
				return entry;
			} else if (genericInterface instanceof Class<?>) {
				return getEntityType((Class<?>) genericInterface);
			}
		}
		return type;
	}

	default String getPrimaryKey() {
		List<Field> allField = FieldUtil.getAllField((Class<?>) getEntityType(), ReflectionAccess.PRIVATE);
		for (Field field : allField) {
			if (AnnotationUtil.isExistAnnotation(field, Id.class)) {
				return field.getName();
			}
		}
		return null;
	}

	/**
	 * @param trim
	 * @return
	 */
	default String findEntityKey(String key) {
		return ReflectionDBUtil.getFieldName(getEntityType(), key);
	}

	default List<Sort.Order> buidOrders(Map<String, Object> sortOrders) {
		List<Sort.Order> orders = new ArrayList<Sort.Order>();
		Object sort = sortOrders.get(SORT);
		Object orderBy = sortOrders.get(ORDER_BY);
		Object sortOrder = sortOrders.get(SORT_ORDER);
		if (!StringUtil.isEmpty(sort)) {
			if (sort instanceof List<?>) {
				@SuppressWarnings("unchecked")
				List<Object> sortList = (List<Object>) sort;
				for (Object sortObject : sortList) {
					Order order = getOrder(sortObject);
					if (order != null) {
						orders.add(order);
					}
				}
			} else {
				Order order = getOrder(sort);
				if (order != null) {
					orders.add(order);
				}
			}
		} else if (!StringUtil.isEmpty(orderBy)) {
			Order order = getOrder(sortOrder, orderBy);
			if (order != null) {
				orders.add(order);
			}
		}
		return orders;
	}

	public default String findSortingKey(String orderBy) {
		Map<String, String> customSortingMap = this.getCustomSortingMap();
		if (!CollectionUtils.isEmpty(customSortingMap)) {
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

	default Order getOrder(Object sortOrder, Object orderBy) {
		String findSortingKey = findSortingKey(orderBy.toString());
		Order order = null;
		if (StringUtil.isNonEmpty(findSortingKey)) {
			return !StringUtil.isEmpty(sortOrder) ? new Order(Direction.ASC, findSortingKey)
					: new Order(Direction.fromString(sortOrder.toString().toUpperCase()), findSortingKey);
		}
		return order;
	}

	default Order getOrder(Object sort) {
		String[] sortArray = sort.toString().split(":");
		Order order = null;
		if (sortArray.length == 1) {
			String findSortingKey = findEntityKey(sortArray[0].trim());
			if (StringUtil.isNonEmpty(findSortingKey)) {
				order = new Order(Direction.ASC, findSortingKey);
			}
		} else {
			String findSortingKey = findEntityKey(sortArray[0].trim());
			if (StringUtil.isNonEmpty(findSortingKey)) {
				order = new Order(Direction.fromString(sortArray[1].trim().toUpperCase()), findSortingKey);
			}
		}
		return order;
	}

	default EN find(ID id) {
		return getRepository().findById(id).orElse(null);
	}

	default boolean isNullOrEmpty(Object value) {
		return value == null || EMPTY.equalsIgnoreCase(value.toString()) || NULL.equalsIgnoreCase(value.toString());
	}

	default void addCustomPredicate(String key, CustomPredicate<EN> customPredicate) {
		getCustomPredicateMap().put(key, customPredicate);
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
		if (!CollectionUtils.isEmpty(customSortingMap)) {
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
				Expression<Object> path = root.get(fieldName);
				Predicate notLike = criteriaBuilder.notLike(path.as(String.class), PERCENTAGE + value + PERCENTAGE);
				if (isNullOrEmpty(value)) {
					return criteriaBuilder.or(notLike, criteriaBuilder.isNull(path));
				}
				return notLike;
			}
			if(fieldName.equals(getPrimaryKey())) {
				Object value = ReflectionDBUtil.casting(type, fieldName, filter.getColumnValue().toString());
				Expression<Object> path = root.get(fieldName);
				Predicate eq = criteriaBuilder.equal(path, value);
				return eq;
			}
			Object value = ReflectionDBUtil.casting(type, fieldName, filter.getColumnValue().toString());
			Expression<Object> path = root.get(fieldName);
			Predicate like = criteriaBuilder.like(path.as(String.class), PERCENTAGE + value + PERCENTAGE);
			if (isNullOrEmpty(value)) {
				return criteriaBuilder.or(like, criteriaBuilder.isNull(path));
			}
			return like;
		} else {
			System.out.println("Invalid config : " + filter.getColumnName());
			return null;
		}
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

	default DT findById(ID uuid, Map<String, List<String>> headers, Map<String, Object> filters,
			Map<String, Object> actions) {
		preFetch(uuid, filters, actions);
		EN findObject = getRepository().findById(uuid).orElse(null);
		DT dtoObject = getMapper().mapToDTO(findObject);
		postFetch(findObject, dtoObject, headers, filters, actions);
		return dtoObject;
	}

	default void preFetch(ID uuid, Map<String, Object> filters, Map<String, Object> actions) {
		preFetch(uuid, filters);
	}
	
	default void preFetch(ID uuid, Map<String, Object> filters) {
		preFetch(uuid);
	}
	
	default void preFetch(ID uuid) {

	}

	default void postFetch(EN findObject, DT dtoObject, Map<String, List<String>> headers, Map<String, Object> filters,
			Map<String, Object> actions) {
		postFetch(findObject, dtoObject, headers, filters);
	}
	
	default void postFetch(EN findObject, DT dtoObject, Map<String, List<String>> headers, Map<String, Object> filters) {
		postFetch(findObject, dtoObject, headers);
	}

	default void postFetch(EN findObject, DT dtoObject, Map<String, List<String>> headers) {
		postFetch(findObject, dtoObject);
	}

	default void postFetch(EN findObject, DT dtoObject) {

	}
}
