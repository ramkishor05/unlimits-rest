package org.unlimits.rest.crud.beans;

import java.util.List;

public class PageDetail {
	
	private long totalCount;
	
	private long pageCount;
	
	private List<?> elements;

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public long getPageCount() {
		return pageCount;
	}

	public void setPageCount(long pageCount) {
		this.pageCount = pageCount;
	}

	public List<?> getElements() {
		return elements;
	}

	public void setElements(List<?> elements) {
		this.elements = elements;
	}

	
}
