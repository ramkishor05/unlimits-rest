package org.unlimits.rest.crud.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.unlimits.rest.crud.beans.Response;
import org.unlimits.rest.crud.service.CQRSService;

public interface CQRSController<DT, EN, ID> {
	

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
	
	public abstract CQRSService<DT, EN, ID> getService();
	
	@GetMapping("/{id}")
	default Response find(@PathVariable ID id){
		Response response=new Response();
		try {
			response.setData(customizedResponse(getService().findById(id)));
			response.setSuccess(SUCCESS);
			response.setMessage(SUCCESSFULLY_PROCCEED);
			return response;
		}catch (Exception e) {
			response.setSuccess(FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
	}

	public default Object customizedResponse(DT dtoObject) {
		return dtoObject;
		
	}
	
}
