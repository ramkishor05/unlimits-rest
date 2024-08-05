package org.unlimits.rest.crud.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.unlimits.rest.constants.RestConstant.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.WebRequest;
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
	

	static Map<String, Object> getfilters(WebRequest webRequest) {
		Map<String, Object> filters=new HashMap<String, Object>();
		webRequest.getParameterMap().forEach((key,values)->{
			if(!sortingKeys(key)) {
				if(values!=null ) {
					if(values.length==1) {
						filters.put(key, values[0]);
					} else {
						filters.put(key, Arrays.asList(values));
					}
				}else {
					filters.put(key, values);
				}
			}
		});
		return filters;
	}
	
	static Map<String, Object> getSortings(WebRequest webRequest) {
		Map<String, Object> filters=new HashMap<String, Object>();
		webRequest.getParameterMap().forEach((key,values)->{
			if(sortingKeys(key)) {
				if(values!=null ) {
					if(values.length==1) {
						filters.put(key, values[0]);
					} else {
						filters.put(key, Arrays.asList(values));
					}
				}else {
					filters.put(key, values);
				}
			}
		});
		return filters;
	}


	static boolean sortingKeys(String key) {
		return SORT_ORDER.equalsIgnoreCase(key) || ORDER_BY.equalsIgnoreCase(key) ||SORT.equalsIgnoreCase(key);
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
			response.setSuccess(FAILED);
			response.setMessage(e.getMessage());
			return response;
		}
	}

	public default Object customizedResponse(DT dtoObject) {
		return dtoObject;
		
	}
	
}
