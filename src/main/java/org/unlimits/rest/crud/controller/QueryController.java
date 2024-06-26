/**
 * 
 */
package org.unlimits.rest.crud.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.unlimits.rest.crud.beans.Response;
import org.unlimits.rest.crud.service.QueryService;

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
	public Response fetchPageObject(@PathVariable int pageNumber,@PathVariable int count, @RequestHeader(required =false)  MultiValueMap<String,String> headers){
		Response response=new Response();
		try {
			response.setData(getService().fetchPageObject(headers,pageNumber, count));
			response.setSuccess(SUCCESS);
			response.setMessage(SUCCESSFULLY_PROCCEED);
			return response;
		}catch (Exception e) {
			response.setSuccess(FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
	}
	
	@GetMapping("/page/list/{pageNumber}/count/{count}")
	public Response fetchPageList(@PathVariable int pageNumber,@PathVariable int count, @RequestHeader(required =false)  MultiValueMap<String,String> headers){
		Response response=new Response();
		try {
			response.setData(getService().fetchPageList(headers,pageNumber, count));
			response.setSuccess(SUCCESS);
			response.setMessage(SUCCESSFULLY_PROCCEED);
			return response;
		}catch (Exception e) {
			response.setSuccess(FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
	}
	
	@GetMapping("/page/data/{pageNumber}/count/{count}/sort/{sort}")
	public Response fetchPageObject(@PathVariable int pageNumber,@PathVariable int count, @PathVariable String sort, @RequestHeader(required =false)  MultiValueMap<String,String> headers){
		Response response=new Response();
		try {
			response.setData(getService().fetchPageObject(headers,pageNumber, count, Sort.by(sort)));
			response.setSuccess(SUCCESS);
			response.setMessage(SUCCESSFULLY_PROCCEED);
			return response;
		}catch (Exception e) {
			response.setSuccess(FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
	}
	
	@GetMapping("/page/list/{pageNumber}/count/{count}/sort/{sort}")
	public Response fetchPageList(@PathVariable int pageNumber,@PathVariable int count, @PathVariable String sort, @RequestHeader(required =false)  MultiValueMap<String,String> headers){
		Response response=new Response();
		try {
			response.setData(getService().fetchPageList(headers,pageNumber, count, Sort.by(sort)));
			response.setSuccess(SUCCESS);
			response.setMessage(SUCCESSFULLY_PROCCEED);
			return response;
		}catch (Exception e) {
			response.setSuccess(FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
	}
}
