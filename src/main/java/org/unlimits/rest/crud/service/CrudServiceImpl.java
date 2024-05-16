/**
 * 
 */
package org.unlimits.rest.crud.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.unlimits.rest.crud.beans.PageDetail;

/**
 *  @author ram kishor
 */
public abstract class CrudServiceImpl<DT, EN, ID> implements CrudService<DT,EN, ID> {
	

	@Override
	public DT add(DT data) {
		EN mappedToDT = getMapper().mapToDAO(data);
		EN save = getRepository().save(mappedToDT);
		return getMapper().mapToDTO(save);
	}

	@Override
	public DT update(DT data) {
		EN mappedToDT = getMapper().mapToDAO(data);
		EN save = getRepository().save(mappedToDT);
		return getMapper().mapToDTO(save);
	}
	
	@Override
	public DT update(ID id, DT dto) {
		DT findById = findById(id);
		if(findById==null) {
			return null;
		}
		return null;
	}

	@Override
	public Boolean delete(ID uuid) {
		getRepository().deleteById(uuid);
		return true;
	}
	
	@Override
	public DT find(ID uuid) {
		return getMapper().mapToDTO(getRepository().getReferenceById(uuid));
	}

	@Override
	public DT findById(ID uuid) {
		return getMapper().mapToDTO(getRepository().getReferenceById(uuid));
	}
	
	@Override
	public List<DT> findAllById(List<ID> ids) {
		return getMapper().mapToDTO(getRepository().findAllById(ids));
	}

	@Override
	public List<DT> findAll() {
		return getMapper().mapToDTO(getRepository().findAll()) ;
	}
	
	@Override
	public List<DT> findAll(Sort sort) {
		return getMapper().mapToDTO(getRepository().findAll(sort)) ;
	}

	@Override
	public PageDetail fetchPageObject(int pageNumber, int count) {
		Pageable pageable =PageRequest.of(pageNumber, count);
		Page<EN> page = getRepository().findAll(pageable);
		List<DT> reslist = getMapper().mapToDTO(page.toList());
		PageDetail responseDto=new PageDetail();
		responseDto.setCount(reslist.size());
		responseDto.setElements(reslist);
		return responseDto;
	}
	
	@Override
	public PageDetail fetchPageObject(int pageNumber, int count, Sort sort) {
		Pageable pageable =PageRequest.of(pageNumber, count , sort);
		Page<EN> page = getRepository().findAll(pageable);
		List<DT> reslist = getMapper().mapToDTO(page.toList());
		PageDetail responseDto=new PageDetail();
		responseDto.setCount(reslist.size());
		responseDto.setElements(reslist);
		return responseDto;
	}

	@Override
	public List<DT> fetchPageList(int pageNumber, int count) {
		Pageable pageable =PageRequest.of(pageNumber, count);
		Page<EN> page = getRepository().findAll(pageable);
		return getMapper().mapToDTO(page.toList());
	}
	
	@Override
	public List<DT> fetchPageList(int pageNumber, int count , Sort sort) {
		Pageable pageable =PageRequest.of(pageNumber, count, sort);
		Page<EN> page = getRepository().findAll(pageable);
		return getMapper().mapToDTO(page.toList());
	}
}
