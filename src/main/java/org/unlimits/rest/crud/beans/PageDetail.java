package org.unlimits.rest.crud.beans;

import java.util.List;

public class PageDetail {
	
	private long count;
	
	private List<?> elements;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<?> getElements() {
		return elements;
	}

	public void setElements(List<?> elements) {
		this.elements = elements;
	}

	
}
