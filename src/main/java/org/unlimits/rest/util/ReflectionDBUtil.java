/**
 * 
 */
package org.unlimits.rest.util;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import org.brijframework.util.casting.CastingUtil;
import org.brijframework.util.reflect.AnnotationUtil;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.support.ReflectionAccess;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;

/**
 *  @author omnie
 */
public class ReflectionDBUtil {
	

	/**
	 * @param columnName
	 * @param columnValue
	 * @return
	 */
	public static Object casting(Type entity, String columnName, String columnValue) {
		Field entityField = getField(entity, columnName);
		if(entityField==null) {
			return columnValue;
		}
		if(entityField.getType().isAssignableFrom(UUID.class)) {
			return UUID.fromString(columnValue);
		}
		return CastingUtil.castObject(columnValue, entityField.getType());
	}

	public static Field getField(Type entity, String key) {
		List<Field> allField = FieldUtil.getAllField((Class<?>)entity, ReflectionAccess.PRIVATE);
		for(Field field:  allField) {
			String name=field.getName();
			if(name.equals(key)) {
				return field;
			} else if(AnnotationUtil.isExistAnnotation(field, Column.class)) {
				Column column=field.getAnnotation(Column.class);
				if(column.name().equals(key)) {
					return field;
				}
			}else if(AnnotationUtil.isExistAnnotation(field, JoinColumn.class)) {
				JoinColumn column=field.getAnnotation(JoinColumn.class);
				if(column.name().equals(key)) {
					return field;
				}
			}
		}
		return null;
	}
	
	public static String getFieldName(Type entity, String key) {
		List<Field> allField = FieldUtil.getAllField((Class<?>)entity, ReflectionAccess.PRIVATE);
		for(Field field:  allField) {
			String name=field.getName();
			if(name.equals(key)) {
				return field.getName();
			} else if(AnnotationUtil.isExistAnnotation(field, Column.class)) {
				Column column=field.getAnnotation(Column.class);
				if(column.name().equals(key)) {
					return field.getName();
				}
			}else if(AnnotationUtil.isExistAnnotation(field, JoinColumn.class)) {
				JoinColumn column=field.getAnnotation(JoinColumn.class);
				if(column.name().equals(key)) {
					return field.getName();
				}
			}
		}
		return null;
	}
}
