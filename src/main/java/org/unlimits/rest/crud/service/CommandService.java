package org.unlimits.rest.crud.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.support.ReflectionAccess;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.unlimits.rest.crud.mapper.GenericMapper;
import org.unlimits.rest.filters.FilterPredicate;
import org.unlimits.rest.repository.CustomRepository;
import org.unlimits.rest.spec.CurdSpecification;

public interface CommandService<DT, EN, ID>  extends CQRSService<DT, EN, ID>{
	
	// add actions
	
	default void preAdd(DT data, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		preAdd(data, headers, filters);
	}
	
	default void preAdd(DT data, Map<String, List<String>> headers, Map<String, Object> filters) {
		preAdd(data, headers);
	}
	
	default void preAdd(DT data, Map<String, List<String>> headers) {
		preAdd(data);
	}
	
	default void preAdd(DT data) {
		
	}

	default void preAdd(DT data, EN entity, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions ) {
		preAdd(data, entity, headers, filters);
	}

	default void preAdd(DT data, EN entity, Map<String, List<String>> headers, Map<String, Object> filters) {
		preAdd(data, entity, headers);
	}
	
	default void preAdd(DT data, EN entity, Map<String, List<String>> headers) {
		preAdd(data, entity);
	}
	
	default void preAdd(DT data, EN entity) {
		
	}
	
	default DT add(DT dtoObject, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		preAdd(dtoObject, headers, filters, actions);
		EN entityObject = getMapper().mapToDAO(dtoObject);
		preAdd(dtoObject, entityObject,  headers, filters, actions);
		EN addedEntityObject = getRepository().save(entityObject);
		postAdd(dtoObject, addedEntityObject,  headers, filters, actions);
		DT addedDtoObject = getMapper().mapToDTO(addedEntityObject);
		merge(dtoObject,entityObject,addedDtoObject, addedEntityObject,  headers, filters, actions);
		postFetch(addedEntityObject, addedDtoObject, headers, filters, actions);
		return addedDtoObject;
	}
	
	default DT add(DT dtoObject, Map<String, List<String>> headers, Map<String, Object> filters) {
		preAdd(dtoObject, headers, filters);
		EN entityObject = getMapper().mapToDAO(dtoObject);
		preAdd(dtoObject, entityObject,  headers, filters);
		EN addedEntityObject = getRepository().save(entityObject);
		postAdd(dtoObject, addedEntityObject,  headers, filters);
		DT addedDtoObject = getMapper().mapToDTO(addedEntityObject);
		merge(dtoObject,entityObject,addedDtoObject, addedEntityObject, headers, filters);
		postFetch(addedEntityObject, addedDtoObject, headers, filters);
		return addedDtoObject;
	}
	
	default DT add(DT dtoObject, Map<String, List<String>> headers) {
		preAdd(dtoObject, headers);
		EN entityObject = getMapper().mapToDAO(dtoObject);
		preAdd(dtoObject, entityObject,  headers);
		EN addedEntityObject = getRepository().save(entityObject);
		postAdd(dtoObject, addedEntityObject,  headers);
		DT addedDtoObject = getMapper().mapToDTO(addedEntityObject);
		merge(dtoObject,entityObject,addedDtoObject, addedEntityObject, headers);
		postFetch(addedEntityObject, addedDtoObject, headers);
		return addedDtoObject;
	}
	
	default DT add(DT dtoObject) {
		preAdd(dtoObject);
		EN entityObject = getMapper().mapToDAO(dtoObject);
		preAdd(dtoObject, entityObject);
		EN addedEntityObject = getRepository().save(entityObject);
		postAdd(dtoObject, addedEntityObject);
		DT addedDtoObject = getMapper().mapToDTO(addedEntityObject);
		merge(dtoObject,entityObject,addedDtoObject, addedEntityObject);
		postFetch(addedEntityObject, addedDtoObject);
		return addedDtoObject;
	}
	
	default void postAdd(DT data, EN entity, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		postAdd(data, entity, headers, filters);
	}
	
	default void postAdd(DT data, EN entity, Map<String, List<String>> headers, Map<String, Object> filters) {
		postAdd(data, entity, headers);
	}
	
	default void postAdd(DT data, EN entity, Map<String, List<String>> headers) {
		postAdd(data, entity);
	}
	
	default void postAdd(DT data, EN entity) {

	}
	
	default List<DT> addAll(List<DT> dtoObjectList, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions){
		List<DT> list=new ArrayList<DT>();
		for(DT dtoObject : dtoObjectList) {
			list.add(add(dtoObject, headers, filters, actions));
		}
		return list;
	}
	
	// update action

	default void preUpdate(DT data, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		preUpdate(data, headers, filters);
	}
	
	default void preUpdate(DT data, Map<String, List<String>> headers, Map<String, Object> filters) {
		preUpdate(data, headers);
	}
	
	default void preUpdate(DT data, Map<String, List<String>> headers) {
		preUpdate(data);
	}
	
	default void preUpdate(DT data) {

	}

	default void preUpdate(DT data, EN entity, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		preAdd(data, entity, headers, filters);
	}
	
	default void preUpdate(DT data, EN entity, Map<String, List<String>> headers, Map<String, Object> filters) {
		preAdd(data, entity, headers);
	}
	
	default void preUpdate(DT data, EN entity, Map<String, List<String>> headers) {
		preAdd(data, entity);
	}
	
	default void preUpdate(DT data, EN entity) {

	}
	
	default DT update(ID id, DT dtoObject, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		if(id==null) {
			return add(dtoObject, headers, filters, actions);
		}
		preUpdate(dtoObject,  headers, filters, actions);
		EN findObject = find(id);
		if(findObject==null) {
			return null;
		}
		preUpdate(dtoObject, findObject,  headers, filters, actions);
		GenericMapper<EN, DT> mapper = getMapper();
		EN entityObject=updateProperties(mapper.mapToDAO(dtoObject), findObject);
		EN updateEntityObject = getRepository().save(entityObject);
		DT updateDtoObject = mapper.mapToDTO(updateEntityObject);
		postUpdate(updateDtoObject, updateEntityObject, headers, filters, actions);
		merge(dtoObject,entityObject,updateDtoObject, updateEntityObject , headers, filters, actions);
		postFetch(updateEntityObject, updateDtoObject, headers, filters, actions);
		return updateDtoObject;
	}
	
	default DT update(ID id, DT dtoObject, Map<String, List<String>> headers, Map<String, Object> filters) {
		if(id==null) {
			return add(dtoObject, headers, filters);
		}
		preUpdate(dtoObject,  headers, filters);
		EN findObject = find(id);
		if(findObject==null) {
			return null;
		}
		preUpdate(dtoObject, findObject,  headers, filters);
		GenericMapper<EN, DT> mapper = getMapper();
		EN entityObject=updateProperties(mapper.mapToDAO(dtoObject), findObject);
		EN updateEntityObject = getRepository().save(entityObject);
		DT updateDtoObject = mapper.mapToDTO(updateEntityObject);
		postUpdate(updateDtoObject, updateEntityObject, headers, filters);
		merge(dtoObject,entityObject,updateDtoObject, updateEntityObject, headers, filters);
		postFetch(updateEntityObject, updateDtoObject, headers, filters);
		return updateDtoObject;
	}
	
	
	default DT update(ID id, DT dtoObject, Map<String, List<String>> headers) {
		if(id==null) {
			return add(dtoObject, headers);
		}
		preUpdate(dtoObject,  headers);
		EN findObject = find(id);
		if(findObject==null) {
			return null;
		}
		preUpdate(dtoObject, findObject,  headers);
		GenericMapper<EN, DT> mapper = getMapper();
		EN entityObject=updateProperties(mapper.mapToDAO(dtoObject), findObject);
		EN updateEntityObject = getRepository().save(entityObject);
		DT updateDtoObject = mapper.mapToDTO(updateEntityObject);
		postUpdate(updateDtoObject, updateEntityObject, headers);
		merge(dtoObject,entityObject,updateDtoObject, updateEntityObject, headers);
		postFetch(updateEntityObject, updateDtoObject, headers);
		return updateDtoObject;
	}
	
	
	default DT update(ID id, DT dtoObject) {
		if(id==null) {
			return add(dtoObject);
		}
		preUpdate(dtoObject);
		EN findObject = find(id);
		if(findObject==null) {
			return null;
		}
		preUpdate(dtoObject, findObject);
		GenericMapper<EN, DT> mapper = getMapper();
		EN entityObject=updateProperties(mapper.mapToDAO(dtoObject), findObject);
		EN updateEntityObject = getRepository().save(entityObject);
		DT updateDtoObject = mapper.mapToDTO(updateEntityObject);
		postUpdate(updateDtoObject, updateEntityObject);
		merge(dtoObject,entityObject,updateDtoObject, updateEntityObject);
		postFetch(updateEntityObject, updateDtoObject);
		return updateDtoObject;
	}
	
	default DT update(DT dtoObject, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		return update(PropertyAccessorUtil.getProperty(dtoObject, getPrimaryKey(), ReflectionAccess.PRIVATE), dtoObject , headers, filters, actions);
	}
	
	default DT update(DT dtoObject, Map<String, List<String>> headers, Map<String, Object> filters) {
		return update(PropertyAccessorUtil.getProperty(dtoObject, getPrimaryKey(), ReflectionAccess.PRIVATE), dtoObject , headers, filters);
	}
	
	default DT update(DT dtoObject, Map<String, List<String>> headers) {
		return update(PropertyAccessorUtil.getProperty(dtoObject, getPrimaryKey(), ReflectionAccess.PRIVATE), dtoObject , headers);
	}
	
	default DT update(DT dtoObject) {
		return update(PropertyAccessorUtil.getProperty(dtoObject, getPrimaryKey(), ReflectionAccess.PRIVATE), dtoObject);
	}

	default List<DT> updateAll(List<DT> dtoObjectList, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions){
		List<DT> list=new ArrayList<DT>();
		for(DT dtoObject : dtoObjectList) {
			list.add(update(dtoObject, headers, filters, actions));
		}
		return list;
	}
	
	default List<DT> updateAll(List<DT> dtoObjectList, Map<String, List<String>> headers, Map<String, Object> filters){
		List<DT> list=new ArrayList<DT>();
		for(DT dtoObject : dtoObjectList) {
			list.add(update(dtoObject, headers, filters));
		}
		return list;
	}
	
	default List<DT> updateAll(List<DT> dtoObjectList, Map<String, List<String>> headers){
		List<DT> list=new ArrayList<DT>();
		for(DT dtoObject : dtoObjectList) {
			list.add(update(dtoObject, headers));
		}
		return list;
	}
	
	default List<DT> updateAll(List<DT> dtoObjectList){
		List<DT> list=new ArrayList<DT>();
		for(DT dtoObject : dtoObjectList) {
			list.add(update(dtoObject));
		}
		return list;
	}
	
	default List<DT> saveAll(List<DT> dtoObjectList, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions){
		List<DT> list=new ArrayList<DT>();
		for(DT dtoObject : dtoObjectList) {
			list.add(update(dtoObject, headers, filters, actions));
		}
		return list;
	}
	
	default List<DT> saveAll(List<DT> dtoObjectList, Map<String, List<String>> headers, Map<String, Object> filters){
		List<DT> list=new ArrayList<DT>();
		for(DT dtoObject : dtoObjectList) {
			list.add(update(dtoObject, headers, filters));
		}
		return list;
	}
	
	default List<DT> saveAll(List<DT> dtoObjectList, Map<String, List<String>> headers){
		List<DT> list=new ArrayList<DT>();
		for(DT dtoObject : dtoObjectList) {
			list.add(update(dtoObject, headers));
		}
		return list;
	}
	
	default List<DT> saveAll(List<DT> dtoObjectList){
		List<DT> list=new ArrayList<DT>();
		for(DT dtoObject : dtoObjectList) {
			list.add(update(dtoObject));
		}
		return list;
	}
	
	default void postUpdate(DT data, EN entity, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		postUpdate(data, entity, headers, filters);
	}
	
	default void postUpdate(DT data, EN entity, Map<String, List<String>> headers, Map<String, Object> filters) {
		postUpdate(data, entity, headers);
	}
	
	default void postUpdate(DT data, EN entity, Map<String, List<String>> headers) {
		postUpdate(data, entity);
	}
	
	default void postUpdate(DT data, EN entity) {

	}

	default void postUpdate(DT updateDtoObject, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		postUpdate(updateDtoObject, headers, filters);
	}
	
	default void postUpdate(DT updateDtoObject, Map<String, List<String>> headers, Map<String, Object> filters) {
		postUpdate(updateDtoObject, headers);
	}

	default void postUpdate(DT updateDtoObject, Map<String, List<String>> headers) {
		postUpdate(updateDtoObject);
	}
	
	default void postUpdate(DT updateDtoObject) {
		
	}
	
	default void merge(DT dtoObject, EN entityObject, DT updateDtoObject, EN updateEntityObject,
			Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		merge(dtoObject, entityObject, updateDtoObject, updateEntityObject, headers, filters);
	}
	
	default void merge(DT dtoObject, EN entityObject, DT updateDtoObject, EN updateEntityObject,
			Map<String, List<String>> headers, Map<String, Object> filters) {
		merge(dtoObject, entityObject, updateDtoObject, updateEntityObject, headers);
	}
	
	default void merge(DT dtoObject, EN entityObject, DT updateDtoObject, EN updateEntityObject,
			Map<String, List<String>> headers) {
		merge(dtoObject, entityObject, updateDtoObject, updateEntityObject);
	}

	default void merge(DT dtoObject, EN entityObject, DT updateDtoObject, EN updateEntityObject) {
	}

    List<String> ignoreProperties();

	default EN updateProperties(EN source, EN target) {
		if(CollectionUtils.isEmpty(ignoreProperties())) {
			BeanUtils.copyProperties(source, target);
		} else {
			String[] ignoreProperties=new String[ignoreProperties().size()];
			BeanUtils.copyProperties(source, target, ignoreProperties().toArray(ignoreProperties));
		}
		return target;
	}
		
	default Boolean deleteById(ID uuid) {
		getRepository().deleteById(uuid);
		return true;
	}
	
	default void deleteAllById(List<ID> ids, Map<String, List<String>> headers, Map<String, Object> sortOrders,
			Map<String, Object> filters, Map<String, Object> actions) {
		List<org.springframework.data.domain.Sort.Order> buidOrders = buidOrders(sortOrders);
		if (CollectionUtils.isEmpty(buidOrders)) {
			 deleteAllById(ids, headers, filters, actions);
		} else {
			 deleteAllById(ids, headers, filters, actions , Sort.by(buidOrders));
		}
	}
	
	default void deleteAllById(List<ID> ids, Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		preDelete(ids, headers, filters, actions);
		getRepository().deleteAllById(ids);
		postDelete(ids, headers, filters, actions);
	}

	default void preDelete(List<ID> ids, Map<String, List<String>> headers, Map<String, Object> filters,
			Map<String, Object> actions) {
		
	}

	default void postDelete(List<ID> ids, Map<String, List<String>> headers, Map<String, Object> filters,
			Map<String, Object> actions) {
		
	}

	default void postDelete(List<ID> ids,List<EN> findAllById, Map<String, List<String>> headers, Map<String, Object> filters,
			Map<String, Object> actions){
		
	}

	default void preDelete(Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
	}

	default void deleteAllById(List<ID> ids, Map<String, List<String>> headers,
			Map<String, Object> filters, Map<String, Object> actions, Sort sort) {
		preDelete(ids, headers, filters, actions);
		getRepository().deleteAllById(ids);
		postDelete(ids, headers, filters, actions);
	}
	
	default void deleteAll(Map<String, List<String>> headers,
			Map<String, Object> filters, Map<String, Object> actions , Map<String, Object> sortOrders) {
		List<org.springframework.data.domain.Sort.Order> buidOrders = buidOrders(sortOrders);
		if (CollectionUtils.isEmpty(buidOrders)) {
			deleteAll(headers, filters, actions);
		} else {
			deleteAll(headers,  filters, actions, Sort.by(buidOrders));
		}
	}

	default void deleteAll(Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		preDelete(headers, filters, actions);
		repositoryDeleteAll(headers, filters);
		postDelete(headers, filters, actions);
	}
	
	default void deleteAll(Map<String, List<String>> headers, Map<String, Object> filters) {
		preDelete(headers, filters);
		repositoryDeleteAll(headers, filters);
		postDelete(headers, filters);
	}

	default void preDelete(Map<String, List<String>> headers, Map<String, Object> filters) {
	}
	
	default void postDelete(Map<String, List<String>> headers, Map<String, Object> filters) {
	}

	default void deleteAll(Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions, Sort sort) {
		preDelete(headers, filters, actions);
		repositoryDeleteAll(headers, sort, filters);
		postDelete(headers, filters, actions);
	}

	default void postDelete(Map<String, List<String>> headers, Map<String, Object> filters, Map<String, Object> actions) {
		
	}

	default void repositoryDeleteAll(Map<String, List<String>> headers, Sort sort, Map<String, Object> filters) {
		if (getRepository() instanceof CustomRepository<EN, ID>) {
			CustomRepository<EN, ID> customRepository = ((CustomRepository<EN, ID>) getRepository());
			if (!CollectionUtils.isEmpty(filters)) {
				List<FilterPredicate> filterList = new ArrayList<FilterPredicate>();
				filters.forEach((key, value) -> {
					filterList.add(new FilterPredicate(key, value));
				});
				if (filterList.isEmpty()) {
					customRepository.deleteAll();
					return;
				} else {
					CurdSpecification<DT, EN, ID> specification = new CurdSpecification<DT, EN, ID>(this,
							getEntityType(), filterList);
					customRepository.delete(specification);
					return;
				}
			}
		}
		 getRepository().deleteAll();
	}
	
	default void repositoryDeleteAll(Map<String, List<String>> headers, Map<String, Object> filters) {
		if (getRepository() instanceof CustomRepository<EN, ID>) {
			CustomRepository<EN, ID> customRepository = ((CustomRepository<EN, ID>) getRepository());
			if (!CollectionUtils.isEmpty(filters)) {
				List<FilterPredicate> filterList = new ArrayList<FilterPredicate>();
				filters.forEach((key, value) -> {
					filterList.add(new FilterPredicate(key, value));
				});
				if (filterList.isEmpty()) {
					customRepository.deleteAll();
					return;
				} else {
					CurdSpecification<DT, EN, ID> specification = new CurdSpecification<DT, EN, ID>(this,
							getEntityType(), filterList);
					customRepository.delete(specification);
					return;
				}
			}
		}
		getRepository().deleteAll();
	}

}
