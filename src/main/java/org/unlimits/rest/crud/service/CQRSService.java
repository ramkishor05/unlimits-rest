package org.unlimits.rest.crud.service;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.brijframework.util.reflect.AnnotationUtil;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.support.ReflectionAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.unlimits.rest.crud.mapper.GenericMapper;
import org.unlimits.rest.util.ReflectionDBUtil;

import jakarta.persistence.Id;

public interface CQRSService<DT, EN, ID> {

	/**
	 * 
	 * @return
	 */
	JpaRepository<EN, ID> getRepository();
	/***
	 * 
	 * @return
	 */
	GenericMapper<EN, DT>  getMapper();
	
    default Type getEntityType() {
		return ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
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
    
    default EN find(ID id) {
    	return getRepository().getReferenceById(id);
    }
	
	default DT findById(ID uuid) {
		EN findObject = getRepository().getReferenceById(uuid);
		DT dtoObject = getMapper().mapToDTO(findObject);
		postFetch(findObject, dtoObject);
		return dtoObject;
	}

	default void postFetch(EN findObject, DT dtoObject) {
	}
	
}
