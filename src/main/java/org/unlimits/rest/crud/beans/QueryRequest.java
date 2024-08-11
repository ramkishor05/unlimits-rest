package org.unlimits.rest.crud.beans;

import java.util.List;
import java.util.Map;

/**
 * @author ram kishor
 */
public class QueryRequest {

	private Map<String, Object> params;
	private Map<String, List<String>> headers;
	private Map<String, Object> sortOrders;
	private Map<String, Object> filters;
	private String type;
	private String endPoint;

	public QueryRequest(Map<String, Object> params, Map<String, List<String>> headers, Map<String, Object> sortOrders,
			Map<String, Object> filters, String type, String endPoint) {
		super();
		this.params = params;
		this.headers = headers;
		this.sortOrders = sortOrders;
		this.filters = filters;
		this.type = type;
		this.endPoint=endPoint;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public Map<String, Object> getSortOrders() {
		return sortOrders;
	}

	public void setSortOrders(Map<String, Object> sortOrders) {
		this.sortOrders = sortOrders;
	}

	public Map<String, Object> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}
}