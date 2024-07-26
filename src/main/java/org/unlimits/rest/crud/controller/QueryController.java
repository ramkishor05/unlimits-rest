/**
 * 
 */
package org.unlimits.rest.crud.controller;

import java.util.List;
import java.util.Map;

import org.brijframework.util.text.StringUtil;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.unlimits.rest.crud.beans.PageDetail;
import org.unlimits.rest.crud.beans.Response;
import org.unlimits.rest.crud.service.QueryService;

/**
 *  @author ram kishor
 */
public interface QueryController<DT, EN, ID>  extends CQRSController<DT, EN, ID>{

	public abstract QueryService<DT, EN, ID> getService();
	
	@GetMapping("/findAllById/{ids}")
	default Response findAllById(@RequestHeader(required =false)  MultiValueMap<String,String> headers ,@PathVariable List<ID> ids, WebRequest webRequest){
		Map<String, Object> filters = getfilters(webRequest);
		Response response=new Response();
		try {
			response.setData(customizedResponse(getService().findAllById(ids, headers, filters)));
			response.setSuccess(SUCCESS);
			response.setMessage(SUCCESSFULLY_PROCCEED);
			return response;
		}catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
	}

	default Object customizedResponse(List<DT> values) {
		return values;
	}

	@GetMapping
	default Response findAll(@RequestHeader(required =false)  MultiValueMap<String,String> headers, 
			WebRequest webRequest){
		Map<String, Object> filters = getfilters(webRequest);
		Response response=new Response();
		try {
			response.setData(customizedResponse(getService().findAll(headers, filters)));
			response.setSuccess(SUCCESS);
			response.setMessage(SUCCESSFULLY_PROCCEED);
			return response;
		}catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
	}

	@GetMapping("/page/data/{pageNumber}/count/{count}")
	default Response fetchPageObject(
			@RequestHeader(required =false) MultiValueMap<String,String> headers,
			@PathVariable int pageNumber,
			@PathVariable int count,
			@RequestParam(required = false) String sort, 
			@RequestParam(required = false) String orderBy, 
			@RequestParam(required = false) String sortOrder, 
			WebRequest webRequest){
		Map<String, Object> filters = getfilters(webRequest);
	
		Response response=new Response();
		try {
			if(!ObjectUtils.isEmpty(orderBy)) {
				String findEntityKey = findSortingKey(orderBy);
				if(StringUtil.isNonEmpty(findEntityKey)) {
					Sort by= ObjectUtils.isEmpty(sortOrder) ? 
						Sort.by(new Order(Direction.ASC, findSortingKey(orderBy))):
						Sort.by(new Order(Direction.fromString(sortOrder.trim().toUpperCase()), findEntityKey));
					response.setData(customizedResponse(getService().fetchPageObject(headers, pageNumber, count, by, filters)));
				}else {
					response.setData(customizedResponse(getService().fetchPageObject(headers, pageNumber, count,  filters)));
				}
			} else if(!ObjectUtils.isEmpty(sort) ) {
				String[] sortArray = sort.split(":");
				String findEntityKey = findSortingKey(orderBy);
				if(StringUtil.isNonEmpty(findEntityKey)) {
					Sort by =sortArray.length==1 ? 
					Sort.by(new Order(Direction.ASC, findSortingKey(sortArray[0].trim()))):
					Sort.by(new Order(Direction.fromString(sortArray[1].trim().toUpperCase()), findSortingKey(sortArray[0].trim()))) ; 
					response.setData(customizedResponse(getService().fetchPageObject(headers,pageNumber, count, by, filters)));
				}else {
					response.setData(customizedResponse(getService().fetchPageObject(headers,pageNumber, count, filters)));
				}
			}else {
				response.setData(customizedResponse(getService().fetchPageObject(headers,pageNumber, count, filters)));
			}
			response.setSuccess(SUCCESS);
			response.setMessage(SUCCESSFULLY_PROCCEED);
			return response;
		}catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
	}
	
	public default String findSortingKey(String orderBy) {
		QueryService<DT, EN, ID> service = getService();
		String findEntityKey = service.findEntityKey(orderBy);
		if(StringUtil.isNonEmpty(findEntityKey)) {
			return findEntityKey;
		}
		findEntityKey=service.getCustomSortingMap().get(orderBy);
		if(StringUtil.isNonEmpty(findEntityKey)) {
			return findEntityKey;
		}
		return service.getPrimaryKey();
	}

	public default Object customizedResponse(PageDetail fetchPageObject) {
		return fetchPageObject;
	}

	@GetMapping("/page/list/{pageNumber}/count/{count}")
	default Response fetchPageList(
			@RequestHeader(required =false)  MultiValueMap<String,String> headers, 
			@PathVariable int pageNumber,@PathVariable int count, 
			@RequestParam(required = false) String sort, 
			@RequestParam(required = false) String orderBy, 
			@RequestParam(required = false) String sortOrder,
			WebRequest webRequest){
		Map<String, Object> filters = getfilters(webRequest);

		Response response=new Response();
		try {
			if(!ObjectUtils.isEmpty(orderBy)) {
				String findEntityKey = findSortingKey(orderBy);
				if(StringUtil.isNonEmpty(findEntityKey)) {
					Sort by= ObjectUtils.isEmpty(sortOrder) ? 
						Sort.by(new Order(Direction.ASC, findSortingKey(orderBy))):
						Sort.by(new Order(Direction.fromString(sortOrder.trim().toUpperCase()), findEntityKey));
					response.setData(customizedResponse(getService().fetchPageList(headers, pageNumber, count, by, filters)));
				}else {
					response.setData(customizedResponse(getService().fetchPageList(headers, pageNumber, count, filters)));
				}
			} else if(!ObjectUtils.isEmpty(sort) ) {
				String[] sortArray = sort.split(":");
				String findEntityKey = findSortingKey(orderBy);
				if(StringUtil.isNonEmpty(findEntityKey)) {
					Sort by =sortArray.length==1 ? 
					Sort.by(new Order(Direction.ASC, findSortingKey(sortArray[0].trim()))):
					Sort.by(new Order(Direction.fromString(sortArray[1].trim().toUpperCase()), findSortingKey(sortArray[0].trim()))) ; 
					response.setData(customizedResponse(getService().fetchPageList(headers, pageNumber, count, by, filters)));
				}else {
					response.setData(customizedResponse(getService().fetchPageList(headers, pageNumber, count, filters)));
				}
			}else {
				response.setData(customizedResponse(getService().fetchPageList(headers, pageNumber, count, filters)));
			}
			response.setSuccess(SUCCESS);
			response.setMessage(SUCCESSFULLY_PROCCEED);
			return response;
		}catch (Exception e) {
			e.printStackTrace();
			response.setSuccess(FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
	}
	
}
