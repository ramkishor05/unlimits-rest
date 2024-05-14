package org.brijframework.json.schema.factories;

import java.util.List;

public class JsonSchemaFile {
	public String id;
	public Double orderSequence;
	public List<JsonSchemaObject> objects;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getOrderSequence() {
		return orderSequence;
	}

	public void setOrderSequence(Double orderSequence) {
		this.orderSequence = orderSequence;
	}

	public List<JsonSchemaObject> getObjects() {
		return objects;
	}

	public void setObjects(List<JsonSchemaObject> objects) {
		this.objects = objects;
	}

}
