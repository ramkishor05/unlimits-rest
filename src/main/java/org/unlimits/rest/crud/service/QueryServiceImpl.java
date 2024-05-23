package org.unlimits.rest.crud.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.unlimits.rest.crud.beans.PageDetail;

public abstract class QueryServiceImpl<DT, EN, ID>  implements QueryService<DT, EN, ID> {
	
	protected void postFind(EN findObject, DT dtoObject) {

	}
	
	private List<DT> postCall(List<EN> findObjects) {
		List<DT> list = new ArrayList<DT>();
		for (EN findObject : findObjects) {
			DT dtoObject = getMapper().mapToDTO(findObject);
			list.add(dtoObject);
			postFind(findObject, dtoObject);
		}
		return list;
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
	public List<DT> findAll(Map<String, List<String>> headers) {
		List<EN> findObjects = repositoryFindAll(headers);
		return postCall(findObjects);
	}

	/**
	 * @return
	 */
	protected List<EN> repositoryFindAll(Map<String, List<String>> headers) {
		return getRepository().findAll();
	}

	@Override
	public List<DT> findAll(Map<String, List<String>> headers, Sort sort) {
		List<EN> findObjects = repositoryFindAll(headers, sort);
		return postCall(findObjects);
	}
	
	/**
	 * @return
	 */
	protected List<EN> repositoryFindAll(Map<String, List<String>> headers, Sort sort) {
		return getRepository().findAll(sort);
	}

	@Override
	public PageDetail fetchPageObject(Map<String, List<String>> headers, int pageNumber, int count) {
		Pageable pageable = PageRequest.of(pageNumber, count);
		Page<EN> page = repositoryFindAll(headers, pageable);
		List<DT> reslist = postCall(page.toList());
		PageDetail responseDto = new PageDetail();
		responseDto.setPageCount(page.getNumber());
		responseDto.setTotalCount(page.getTotalElements());
		responseDto.setTotalPages(page.getTotalPages());
		responseDto.setElements(reslist);
		return responseDto;
	}
	
	/**
	 * @return
	 */
	protected Page<EN> repositoryFindAll(Map<String, List<String>> headers,Pageable pageable) {
		return getRepository().findAll(pageable);
	}


	@Override
	public PageDetail fetchPageObject(Map<String, List<String>> headers, int pageNumber, int count, Sort sort) {
		Pageable pageable = PageRequest.of(pageNumber, count, sort);
		Page<EN> page = repositoryFindAll(headers, pageable);
		List<DT> reslist = postCall(page.toList());
		PageDetail responseDto = new PageDetail();
		responseDto.setPageCount(page.getNumber());
		responseDto.setTotalCount(page.getTotalElements());
		responseDto.setTotalPages(page.getTotalPages());
		responseDto.setElements(reslist);
		return responseDto;
	}

	@Override
	public List<DT> fetchPageList(Map<String, List<String>> headers, int pageNumber, int count) {
		Pageable pageable = PageRequest.of(pageNumber, count);
		Page<EN> page =repositoryFindAll(headers, pageable);
		return postCall(page.toList());
	}

	@Override
	public List<DT> fetchPageList(Map<String, List<String>> headers, int pageNumber, int count, Sort sort) {
		Pageable pageable = PageRequest.of(pageNumber, count, sort);
		Page<EN> page = repositoryFindAll(headers,pageable);
		return postCall(page.toList());
	}
}
