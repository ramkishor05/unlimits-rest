/**
 * 
 */
package org.unlimits.rest.crud.controller;

import java.util.List;
import java.util.Map;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
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
		Map<String, Object> sortOrders = getSortings(webRequest);
		Response response=new Response();
		try {
			response.setData(customizedResponse(getService().findAllById(ids, headers, sortOrders, filters)));
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
		
		Map<String, Object> sortOrders = getSortings(webRequest);
		
		Response response=new Response();
		try {
			response.setData(customizedResponse(getService().findAll(headers, sortOrders, filters)));
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
			WebRequest webRequest){
		Map<String, Object> filters = getfilters(webRequest);
		Map<String, Object> sortOrders = getSortings(webRequest);
		Response response=new Response();
		try {
			response.setData(customizedResponse(getService().fetchPageObject(headers, pageNumber, count, sortOrders, filters)));
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
	
	public default Object customizedResponse(PageDetail fetchPageObject) {
		return fetchPageObject;
	}

	@GetMapping("/page/list/{pageNumber}/count/{count}")
	default Response fetchPageList(
			@RequestHeader(required =false)  MultiValueMap<String,String> headers, 
			@PathVariable int pageNumber,@PathVariable int count,
			WebRequest webRequest){
		Map<String, Object> filters = getfilters(webRequest);
		
		Map<String, Object> sortOrders = getSortings(webRequest);

		Response response=new Response();
		try {
			response.setData(customizedResponse(getService().fetchPageList(headers, pageNumber, count, sortOrders, filters)));
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
