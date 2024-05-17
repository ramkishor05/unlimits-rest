/**
 * 
 */
package org.unlimits.rest.crud.service;

import java.util.ArrayList;
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
		preAdd(data);
		EN mappedToDT = getMapper().mapToDAO(data);
		EN save = getRepository().save(mappedToDT);
		return getMapper().mapToDTO(save);
	}

	protected void preAdd(DT data) {
		
	}

	@Override
	public DT update(DT data) {
		preUpdate(data);
		EN mappedToDT = getMapper().mapToDAO(data);
		EN save = getRepository().save(mappedToDT);
		return getMapper().mapToDTO(save);
	}
	
	protected void preUpdate(DT data) {
		
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
		EN findObject = getRepository().getReferenceById(uuid);
		DT dtoObject = getMapper().mapToDTO(findObject);
		postFind(findObject, dtoObject);
		return dtoObject;
	}

	protected void postFind(EN findObject, DT dtoObject) {
		
	}

	@Override
	public DT findById(ID uuid) {
		EN findObject = getRepository().getReferenceById(uuid);
		DT dtoObject = getMapper().mapToDTO(findObject);
		postFind(findObject, dtoObject);
		return dtoObject;
	}
	
	@Override
	public List<DT> findAllById(List<ID> ids) {
		
		List<EN> findObjects = getRepository().findAllById(ids);
		return postCall(findObjects);
	}

	private List<DT> postCall(List<EN> findObjects) {
		List<DT> list=new ArrayList<DT>();
		for(EN findObject : findObjects) {
			DT dtoObject = getMapper().mapToDTO(findObject);
			list.add(dtoObject);
			postFind(findObject, dtoObject);
		}
		return list;
	}

	@Override
	public List<DT> findAll() {
		List<EN> findObjects = getRepository().findAll();
		return postCall(findObjects);
	}
	
	@Override
	public List<DT> findAll(Sort sort) {
		List<EN> findObjects = getRepository().findAll(sort);
		return postCall(findObjects);
	}

	@Override
	public PageDetail fetchPageObject(int pageNumber, int count) {
		Pageable pageable =PageRequest.of(pageNumber, count);
		Page<EN> page = getRepository().findAll(pageable);
		List<DT> reslist = postCall(page.toList());
		PageDetail responseDto=new PageDetail();
		responseDto.setCount(reslist.size());
		responseDto.setElements(reslist);
		return responseDto;
	}
	
	@Override
	public PageDetail fetchPageObject(int pageNumber, int count, Sort sort) {
		Pageable pageable =PageRequest.of(pageNumber, count , sort);
		Page<EN> page = getRepository().findAll(pageable);
		List<DT> reslist = postCall(page.toList());
		PageDetail responseDto=new PageDetail();
		responseDto.setCount(reslist.size());
		responseDto.setElements(reslist);
		return responseDto;
	}

	@Override
	public List<DT> fetchPageList(int pageNumber, int count) {
		Pageable pageable =PageRequest.of(pageNumber, count);
		Page<EN> page = getRepository().findAll(pageable);
		return postCall(page.toList());
	}
	
	@Override
	public List<DT> fetchPageList(int pageNumber, int count , Sort sort) {
		Pageable pageable =PageRequest.of(pageNumber, count, sort);
		Page<EN> page = getRepository().findAll(pageable);
		return postCall(page.toList());
	}
}
