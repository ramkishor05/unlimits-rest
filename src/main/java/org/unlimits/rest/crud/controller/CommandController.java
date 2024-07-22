/**
 * 
 */
package org.unlimits.rest.crud.controller;

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
	
	public abstract CommandService<DT, EN, ID> getService();

	@PostMapping
	default Response add(@RequestBody DT dto, @RequestHeader MultiValueMap<String,String> headers){
		Response response=new Response();
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
	
	@PutMapping
	default Response update(@RequestBody DT dto, @RequestHeader MultiValueMap<String,String> headers){
		Response response=new Response();
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
	default Response update(@PathVariable ID id,@RequestBody DT dto, @RequestHeader(required =false)  MultiValueMap<String,String> headers){
		Response response=new Response();
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
	default Response delete(@PathVariable ID id){
		Response response=new Response();
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
	default Response find(@PathVariable ID id){
		Response response=new Response();
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
