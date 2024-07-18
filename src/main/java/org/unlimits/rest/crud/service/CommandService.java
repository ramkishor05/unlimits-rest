package org.unlimits.rest.crud.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

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
		preAdd(dtoObject, headers);
		EN entityObject = getMapper().mapToDAO(dtoObject);
		preUpdate(dtoObject, entityObject, headers );
		EN updateEntityObject = getRepository().save(entityObject);
		postUpdate(dtoObject, updateEntityObject, headers);
		DT updateDtoObject = getMapper().mapToDTO(updateEntityObject);
		merge(dtoObject,entityObject,updateDtoObject, updateEntityObject, headers);
		return updateDtoObject;
	}
	
	default void merge(DT dtoObject, EN entityObject, DT updateDtoObject, EN updateEntityObject,
			Map<String, List<String>> headers) {
	}

	default void postUpdate(DT updateDtoObject, Map<String, List<String>> headers) {
		
	}

	default void preUpdate(DT data, Map<String, List<String>> headers) {

	}

	default void preUpdate(DT data, EN entity, Map<String, List<String>> headers) {

	}
	
	default void postUpdate(DT data, EN entity, Map<String, List<String>> headers) {

	}

	default DT update(ID id, DT dtoObject, Map<String, List<String>> headers) {
		EN findObject = find(id);
		if(findObject==null) {
			return null;
		}
		preUpdate(dtoObject, findObject, headers);
		EN entityObject = getMapper().mapToDAO(dtoObject);
		BeanUtils.copyProperties(findObject, entityObject, ignoreProperties());
		EN updateEntityObject = getRepository().save(findObject);
		DT updateDtoObject = getMapper().mapToDTO(updateEntityObject);
		postUpdate(updateDtoObject, updateEntityObject, headers);
		return updateDtoObject;
	}

	default String[] ignoreProperties() {
		return new String[] {"id"};
	}

	default Boolean delete(ID uuid) {
		getRepository().deleteById(uuid);
		return true;
	}
}
