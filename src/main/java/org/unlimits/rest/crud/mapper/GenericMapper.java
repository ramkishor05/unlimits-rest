package org.unlimits.rest.crud.mapper;

import java.util.List;

public interface GenericMapper<E, D> {

	D mapToDTO(E entityObject);

	E mapToDAO(D dtoObject);
	
	List<E> mapToDAO(List<D> dtoObjectList);
	
	List<D> mapToDTO(List<E> entityObjectList);
}
