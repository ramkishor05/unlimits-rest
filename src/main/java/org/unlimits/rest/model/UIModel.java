package org.unlimits.rest.model;

public class UIModel {
	
	private Long id;

	private Double orderSequence;
	
	private String recordState;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getOrderSequence() {
		if(orderSequence==null) {
			orderSequence=0.0d;
		}
		return orderSequence;
	}

	public void setOrderSequence(Double orderSequence) {
		this.orderSequence = orderSequence;
	}

	public String getRecordState() {
		return recordState;
	}

	public void setRecordState(String recordState) {
		this.recordState = recordState;
	}
	
}
