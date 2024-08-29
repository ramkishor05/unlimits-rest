package org.unlimits.rest.crud.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.support.ReflectionAccess;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

public interface CommandService<DT, EN, ID>  extends CQRSService<DT, EN, ID>{
	
	default DT add(DT dtoObject, Map<String, List<String>> headers) {
		preAdd(dtoObject, headers);
		EN entityObject = getMapper().mapToDAO(dtoObject);
		preAdd(dtoObject, entityObject,  headers);
		EN addedEntityObject = getRepository().save(entityObject);
		postAdd(dtoObject, addedEntityObject);
		DT addedDtoObject = getMapper().mapToDTO(addedEntityObject);
		merge(dtoObject,entityObject,addedDtoObject, addedEntityObject, headers);
		return addedDtoObject;
	}
	
	default void preAdd(DT data, Map<String, List<String>> headers) {
		
	}

	default void preAdd(DT data, EN entity, Map<String, List<String>> headers) {
	}


	default void postAdd(DT data, EN entity) {

	}

	default DT update(DT dtoObject, Map<String, List<String>> headers) {
		return update(PropertyAccessorUtil.getProperty(dtoObject, getPrimaryKey(), ReflectionAccess.PRIVATE), dtoObject, headers);
	}

	default DT update(ID id, DT dtoObject, Map<String, List<String>> headers) {
		preUpdate(dtoObject, headers);
		EN findObject = find(id);
		if(findObject==null) {
			return null;
		}
		preUpdate(dtoObject, findObject, headers);
		EN entityObject=updateProperties(getMapper().mapToDAO(dtoObject), findObject);
		EN updateEntityObject = getRepository().save(findObject);
		DT updateDtoObject = getMapper().mapToDTO(updateEntityObject);
		postUpdate(updateDtoObject, updateEntityObject, headers);
		merge(dtoObject,entityObject,updateDtoObject, updateEntityObject, headers);
		return updateDtoObject;
	}

	default void preUpdate(DT data, Map<String, List<String>> headers) {

	}

	default void preUpdate(DT data, EN entity, Map<String, List<String>> headers) {

	}

	default void postUpdate(DT updateDtoObject, Map<String, List<String>> headers) {
		
	}

	default void postUpdate(DT data, EN entity, Map<String, List<String>> headers) {

	}
	
	default void merge(DT dtoObject, EN entityObject, DT updateDtoObject, EN updateEntityObject,
			Map<String, List<String>> headers) {
	}


	default List<String> ignoreProperties() {
		return Arrays.asList(getPrimaryKey());
	}


	default EN updateProperties(EN source, EN target) {
		if(CollectionUtils.isEmpty(ignoreProperties())) {
			BeanUtils.copyProperties(source, target);
		} else {
			String[] ignoreProperties=new String[ignoreProperties().size()];
			BeanUtils.copyProperties(source, target, ignoreProperties().toArray(ignoreProperties));
		}
		return target;
	}
		
	default Boolean delete(ID uuid) {
		getRepository().deleteById(uuid);
		return true;
	}
}
