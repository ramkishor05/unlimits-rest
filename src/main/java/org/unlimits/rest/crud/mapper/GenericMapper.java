package org.unlimits.rest.crud.mapper;

import java.util.List;

public interface GenericMapper<E, D> {

	D mapToDTO(E e);

	E mapToDAO(D d);
	
	List<E> mapToDAO(List<D> ds);
	
	List<D> mapToDTO(List<E> es);
}
