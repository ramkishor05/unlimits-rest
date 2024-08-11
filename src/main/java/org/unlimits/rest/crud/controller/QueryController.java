/**
 * 
 */
package org.unlimits.rest.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.WebRequest;
import org.unlimits.rest.crud.beans.PageDetail;
import org.unlimits.rest.crud.beans.QueryRequest;
import org.unlimits.rest.crud.beans.Response;
import org.unlimits.rest.crud.service.QueryService;


public interface QueryController<DT, EN, ID>  extends CQRSController<DT, EN, ID>{

	public static final String PARAM_PAGE_COUNT = "count";
	public static final String PARAM_PAGE_NUMBER = "pageNumber";
	public static final String PARAM_IDS = "ids";
	public static final String PAGE_LIST = "PAGE_LIST";
	public static final String PAGE_DATA = "PAGE_DATA";
	public static final String FIND_ALL = "FIND_ALL";
	public static final String FIND_ALL_BY_ID = "FIND_ALL_BY_ID";

	public abstract QueryService<DT, EN, ID> getService();
	
	@GetMapping("/findAllById/{ids}")
	default Response<Object> findAllById(@RequestHeader(required =false)  MultiValueMap<String,String> headers ,@PathVariable List<ID> ids, WebRequest webRequest){
		Map<String, Object> filters = CQRSController.getfilters(webRequest);
		Map<String, Object> sortOrders = CQRSController.getSortings(webRequest);
		Map<String, Object> params=new HashMap<String, Object>();
		params.put(PARAM_IDS, ids);
		QueryRequest queryRequest=new QueryRequest(params, headers, sortOrders, filters, FIND_ALL_BY_ID, "/findAllById/{ids}");
		Response<Object> response=new Response<Object>();
		try {
			response.setData(customizedResponse(getService().findAllById(ids, headers, sortOrders, filters), queryRequest));
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

	default Object customizedResponse(List<DT> values, QueryRequest queryRequest) {
		return values;
	}

	@GetMapping
	default Response<Object> findAll(@RequestHeader(required =false)  MultiValueMap<String,String> headers, 
			WebRequest webRequest){
		
		Map<String, Object> filters = CQRSController.getfilters(webRequest);
		
		Map<String, Object> sortOrders = CQRSController.getSortings(webRequest);
		Map<String, Object> params=new HashMap<String, Object>();
		QueryRequest queryRequest=new QueryRequest(params, headers, sortOrders, filters, FIND_ALL, "/");
		Response<Object> response=new Response<Object>();
		try {
			response.setData(customizedResponse(getService().findAll(headers, sortOrders, filters),queryRequest));
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
	default Response<Object> fetchPageObject(
			@RequestHeader(required =false) MultiValueMap<String,String> headers,
			@PathVariable int pageNumber,
			@PathVariable int count, 
			WebRequest webRequest){
		Map<String, Object> filters = CQRSController.getfilters(webRequest);
		Map<String, Object> sortOrders = CQRSController.getSortings(webRequest);
		Response<Object> response=new Response<Object>();
		Map<String, Object> params=new HashMap<String, Object>();
		params.put(PARAM_PAGE_NUMBER, pageNumber);
		params.put(PARAM_PAGE_COUNT, count);
		QueryRequest queryRequest=new QueryRequest(params, headers, sortOrders, filters, PAGE_DATA, "/page/data/{pageNumber}/count/{count}");
		try {
			response.setData(customizedResponse(getService().fetchPageObject(headers, pageNumber, count, sortOrders, filters), queryRequest));
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
	
	public default Object customizedResponse(PageDetail<DT> fetchPageObject, QueryRequest queryRequest) {
		return fetchPageObject;
	}

	@GetMapping("/page/list/{pageNumber}/count/{count}")
	default Response<Object> fetchPageList(
			@RequestHeader(required =false)  MultiValueMap<String,String> headers, 
			@PathVariable int pageNumber,@PathVariable int count,
			WebRequest webRequest){
		Map<String, Object> filters = CQRSController.getfilters(webRequest);
		
		Map<String, Object> sortOrders = CQRSController.getSortings(webRequest);
		Map<String, Object> params=new HashMap<String, Object>();
		params.put(PARAM_PAGE_NUMBER, pageNumber);
		params.put(PARAM_PAGE_COUNT, count);
		QueryRequest queryRequest=new QueryRequest(params, headers, sortOrders, filters, PAGE_LIST, "/page/list/{pageNumber}/count/{count}");
		Response<Object> response=new Response<Object>();
		try {
			response.setData(customizedResponse(getService().fetchPageList(headers, pageNumber, count, sortOrders, filters), queryRequest));
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
