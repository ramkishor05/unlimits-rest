/**
 * 
 */
package org.unlimits.rest.crud.controller;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
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
import org.unlimits.rest.crud.beans.Response;
import org.unlimits.rest.crud.service.QueryService;
import org.unlimits.rest.util.ReflectionDBUtil;

/**
 *  @author ram kishor
 */
public abstract class QueryController<DT, EN, ID> {
	

	/**
	 * 
	 */
	public static final String SUCCESSFULLY_PROCCEED = "Successfully procceed";
	/**
	 * 
	 */
	public static final String FAILED = "0";
	/**
	 * 
	 */
	public static final String SUCCESS = "1";
	
	public abstract QueryService<DT, EN, ID> getService();
	
	@GetMapping("/{id}")
	public Response find(@PathVariable ID id){
		Response response=new Response();
		try {
			response.setData(getService().findById(id));
			response.setSuccess(SUCCESS);
			response.setMessage(SUCCESSFULLY_PROCCEED);
			return response;
		}catch (Exception e) {
			response.setSuccess(FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
	}
	
	@GetMapping("/findAllById/{ids}")
	public Response findAllById(@PathVariable List<ID> ids){
		Response response=new Response();
		try {
			response.setData(getService().findAllById(ids));
			response.setSuccess(SUCCESS);
			response.setMessage(SUCCESSFULLY_PROCCEED);
			return response;
		}catch (Exception e) {
			response.setSuccess(FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
	}
	
	@GetMapping
	public Response findAll(@RequestHeader(required =false)  MultiValueMap<String,String> headers){
		Response response=new Response();
		try {
			response.setData(getService().findAll(headers));
			response.setSuccess(SUCCESS);
			response.setMessage(SUCCESSFULLY_PROCCEED);
			return response;
		}catch (Exception e) {
			response.setSuccess(FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
	}

	@GetMapping("/page/data/{pageNumber}/count/{count}")
	public Response fetchPageObject(@PathVariable int pageNumber,@PathVariable int count,@RequestParam(required = false) String sort, 
			@RequestParam(required = false) String orderBy, 
			@RequestParam(required = false) String sortOrder, WebRequest webRequest){
		Map<String, String> filters=new HashMap<String, String>();
		webRequest.getParameterMap().forEach((key,values)->{
			filters.put(key, values[0]);
		});
	
		Response response=new Response();
		try {
			if(!ObjectUtils.isEmpty(orderBy)) {
				String findEntityKey = findEntityKey(orderBy);
				if(StringUtil.isNonEmpty(findEntityKey)) {
					Sort by= ObjectUtils.isEmpty(sortOrder) ? 
						Sort.by(new Order(Direction.ASC, findEntityKey(orderBy))):
						Sort.by(new Order(Direction.fromString(sortOrder.trim().toUpperCase()), findEntityKey));
					response.setData(getService().fetchPageObject(pageNumber, count, by, filters));
				}else {
					response.setData(getService().fetchPageObject(pageNumber, count,  filters));
				}
			} else if(!ObjectUtils.isEmpty(sort) ) {
				String[] sortArray = sort.split(":");
				String findEntityKey = findEntityKey(orderBy);
				if(StringUtil.isNonEmpty(findEntityKey)) {
					Sort by =sortArray.length==1 ? 
					Sort.by(new Order(Direction.ASC, findEntityKey(sortArray[0].trim()))):
					Sort.by(new Order(Direction.fromString(sortArray[1].trim().toUpperCase()), findEntityKey(sortArray[0].trim()))) ; 
					response.setData(getService().fetchPageObject(pageNumber, count, by, filters));
				}else {
					response.setData(getService().fetchPageObject(pageNumber, count, filters));
				}
			}else {
				response.setData(getService().fetchPageObject(pageNumber, count, filters));
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
	
	@GetMapping("/page/list/{pageNumber}/count/{count}")
	public Response fetchPageList(@PathVariable int pageNumber,@PathVariable int count, 
			@RequestParam(required = false) String sort, 
			@RequestParam(required = false) String orderBy, 
			@RequestParam(required = false) String sortOrder, WebRequest webRequest){
		Map<String, String> filters=new HashMap<String, String>();
		webRequest.getParameterMap().forEach((key,values)->{
			filters.put(key, values[0]);
		});

		Response response=new Response();
		try {
			if(!ObjectUtils.isEmpty(orderBy)) {
				String findEntityKey = findEntityKey(orderBy);
				if(StringUtil.isNonEmpty(findEntityKey)) {
					Sort by= ObjectUtils.isEmpty(sortOrder) ? 
						Sort.by(new Order(Direction.ASC, findEntityKey(orderBy))):
						Sort.by(new Order(Direction.fromString(sortOrder.trim().toUpperCase()), findEntityKey));
					response.setData(getService().fetchPageList(pageNumber, count, by, filters));
				}else {
					response.setData(getService().fetchPageList(pageNumber, count,  filters));
				}
			} else if(!ObjectUtils.isEmpty(sort) ) {
				String[] sortArray = sort.split(":");
				String findEntityKey = findEntityKey(orderBy);
				if(StringUtil.isNonEmpty(findEntityKey)) {
					Sort by =sortArray.length==1 ? 
					Sort.by(new Order(Direction.ASC, findEntityKey(sortArray[0].trim()))):
					Sort.by(new Order(Direction.fromString(sortArray[1].trim().toUpperCase()), findEntityKey(sortArray[0].trim()))) ; 
					response.setData(getService().fetchPageList(pageNumber, count, by, filters));
				}else {
					response.setData(getService().fetchPageList(pageNumber, count, filters));
				}
			}else {
				response.setData(getService().fetchPageList(pageNumber, count, filters));
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


	/**
	 * @param trim
	 * @return
	 */
	protected String findEntityKey(String key) {
		return ReflectionDBUtil.getFieldName(type(), key);
	}
	
	protected Type type() {
		return ((ParameterizedType)getClass().getGenericSuperclass())
	      .getActualTypeArguments()[1];
	}
	
}
