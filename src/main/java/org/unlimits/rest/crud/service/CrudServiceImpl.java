/**
 * 
 */
package org.unlimits.rest.crud.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.brijframework.util.text.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.unlimits.rest.crud.beans.PageDetail;
import org.unlimits.rest.filters.FilterPredicate;
import org.unlimits.rest.repository.CustomPredicate;
import org.unlimits.rest.repository.CustomRepository;
import org.unlimits.rest.spec.CurdSpecification;

/**
 * @author ram kishor
 */
public class CrudServiceImpl<DT, EN, ID> implements CrudService<DT, EN, ID> {
	
	private Map<String, CustomPredicate<EN>> customPredicateMap =new  HashMap<String, CustomPredicate<EN>>();
	
	@Override
	public Map<String, CustomPredicate<EN>> getCustomPredicateMap() {
		return customPredicateMap;
	}

	@Override
	public DT add(DT data, Map<String, List<String>> headers) {
		EN mappedToDT = getMapper().mapToDAO(data);
		preAdd(data, mappedToDT,  headers);
		EN save = getRepository().save(mappedToDT);
		DT mapToDTO = getMapper().mapToDTO(save);
		postAdd(mapToDTO, save);
		return mapToDTO;
	}

	protected void preAdd(DT data, EN entity, Map<String, List<String>> headers) {

	}

	protected void postAdd(DT data, EN entity) {

	}

	@Override
	public DT update(DT data, Map<String, List<String>> headers) {
		EN mappedToDT = getMapper().mapToDAO(data);
		preUpdate(data, mappedToDT, headers );
		EN save = getRepository().save(mappedToDT);
		DT mapToDTO = getMapper().mapToDTO(save);
		postUpdate(mapToDTO, save);
		return mapToDTO;
	}

	protected void preUpdate(DT data, EN entity, Map<String, List<String>> headers) {

	}
	
	protected void postUpdate(DT data, EN entity) {

	}

	@Override
	public DT update(ID id, DT data, Map<String, List<String>> headers) {
		DT findObject = findById(id);
		if(findObject==null) {
			return null;
		}
		EN dtoObject = getMapper().mapToDAO(data);
		BeanUtils.copyProperties(findObject, dtoObject, "id");
		EN save = getRepository().save(dtoObject);
		DT mapToDTO = getMapper().mapToDTO(save);
		postUpdate(mapToDTO, save);
		return mapToDTO;
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
		postFetch(findObject, dtoObject);
		return dtoObject;
	}

	protected void postFetch(EN findObject, DT dtoObject) {

	}

	@Override
	public DT findById(ID uuid) {
		EN findObject = getRepository().getReferenceById(uuid);
		DT dtoObject = getMapper().mapToDTO(findObject);
		postFetch(findObject, dtoObject);
		return dtoObject;
	}

	@Override
	public List<DT> findAllById(List<ID> ids) {
		List<EN> findObjects = getRepository().findAllById(ids);
		return postFetch(findObjects);
	}

	protected List<DT> postFetch(List<EN> findObjects) {
		List<DT> list = new ArrayList<DT>();
		for (EN findObject : findObjects) {
			DT dtoObject = getMapper().mapToDTO(findObject);
			list.add(dtoObject);
			postFetch(findObject, dtoObject);
		}
		return list;
	}

	@Override
	public List<DT> findAll(Map<String, List<String>> headers) {
		List<EN> findObjects = repositoryFindAll(headers);
		return postFetch(findObjects);
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
		return postFetch(findObjects);
	}
	
	/**
	 * @return
	 */
	protected List<EN> repositoryFindAll(Map<String, List<String>> headers, Sort sort) {
		return getRepository().findAll(sort);
	}

	@Override
	public PageDetail fetchPageObject(Map<String, List<String>> headers, int pageNumber, int count) {
		Pageable pageable = getPageRequest(pageNumber, count);
		Page<EN> page = repositoryFindAll(headers, pageable);
		List<DT> reslist = postFetch(page.toList());
		PageDetail responseDto = new PageDetail();
		responseDto.setPageCount(page.getNumber());
		responseDto.setTotalCount(page.getTotalElements());
		responseDto.setTotalPages(page.getTotalPages());
		responseDto.setElements(reslist);
		return responseDto;
	}
	
	public Pageable getPageRequest(int pageNumber, int count){
		return PageRequest.of(pageNumber, count);
	}
	
	public Pageable getPageRequest(int pageNumber, int count, Sort sort){
		return PageRequest.of(pageNumber, count , sort);
	}
	
	
	/**
	 * @return
	 */
	protected Page<EN> repositoryFindAll(Map<String, List<String>> headers,Pageable pageable) {
		return getRepository().findAll(pageable);
	}

	@Override
	public List<DT> fetchPageList(int pageNumber, int count, Map<String, String> filters) {
		String idField = getIdField(type());
		return fetchPageList(pageNumber, count, StringUtil.isNonEmpty(idField) ? Sort.by(idField) : null,
				filters);
	}

	@Override
	public List<DT> fetchPageList(int pageNumber, int count, Sort sort, Map<String, String> filters) {
		Pageable pageable = PageRequest.of(pageNumber, count, sort);
		Page<EN> page = getPage(pageable, filters);
		return postCall(page.toList());
	}

	private Page<EN> getPage(Pageable pageable, Map<String, String> filters) {
		if (getRepository() instanceof CustomRepository<EN, ID>) {
			CustomRepository<EN, ID> customRepository= ((CustomRepository<EN, ID>) getRepository());
			if (!CollectionUtils.isEmpty(filters)) {
				List<FilterPredicate> filterList=new ArrayList<FilterPredicate>();
				filters.forEach((key, value)->{
					filterList.add(new FilterPredicate(key, value));
				});
				if(filterList.isEmpty()) {
					return customRepository.findAll(pageable);
				} else {
					CurdSpecification<DT, EN, ID> specification=new CurdSpecification<DT, EN, ID>( this,type(),filterList);
					return customRepository.findAll(specification,pageable);
				}
			}
		}
		return getRepository().findAll(pageable);
	}

}
