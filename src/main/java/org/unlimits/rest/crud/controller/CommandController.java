/**
 * 
 */
package org.unlimits.rest.crud.controller;

import java.util.HashMap;
import java.util.Map;

import org.brijframework.util.text.StringUtil;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.unlimits.rest.crud.beans.Response;
import org.unlimits.rest.crud.service.CommandService;

/**
 *  @author ram kishor
 */
public interface CommandController<DT, EN, ID> extends CQRSController<DT, EN, ID>{
	
	public CommandService<DT, EN, ID> getService();
	
	public static Map<String, String> getEndpointMapping(){
		return new HashMap<String, String>();
	}
	
	public static String findEndpoint(String method) {
		String endpointKey=method;
		String endpointVal=getEndpointMapping().get(endpointKey);
		System.out.println("endpointKey: "+endpointKey);
		System.out.println("endpointVal: "+endpointVal);
		return StringUtil.isEmpty(endpointVal)? "": endpointVal;
	}

	@PostMapping("#{T(org.unlimits.rest.crud.controller.CommandController).findEndpoint('add')}")
	default Response<Object> add(@RequestBody DT dto, @RequestHeader MultiValueMap<String,String> headers){
		Response<Object> response=new Response<Object>();
		try {
			response.setData(customizedResponse(getService().add(dto,headers)));
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
	
	@PutMapping("#{T(org.unlimits.rest.crud.controller.CommandController).findEndpoint('update')}")
	default Response<Object> update(@RequestBody DT dto, @RequestHeader MultiValueMap<String,String> headers){
		Response<Object> response=new Response<Object>();
		try {
			response.setData(customizedResponse(getService().update(dto,headers)));
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
	
	@PutMapping("/{id}")
	default Response<Object> update(@PathVariable ID id,@RequestBody DT dto, @RequestHeader(required =false)  MultiValueMap<String,String> headers){
		Response<Object> response=new Response<Object>();
		try {
			response.setData(customizedResponse(getService().update(id, dto,headers)));
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
	
	@DeleteMapping("/{id}")
	default Response<Object> delete(@PathVariable ID id){
		Response<Object> response=new Response<Object>();
		try {
			response.setData(customizedResponse(getService().delete(id)));
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
	
	public default Object customizedResponse(Boolean delete) {
		return delete;
	}

	@GetMapping("/{id}")
	default Response<Object> find(@PathVariable ID id){
		Response<Object> response=new Response<Object>();
		try {
			response.setData(customizedResponse(getService().findById(id)));
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
