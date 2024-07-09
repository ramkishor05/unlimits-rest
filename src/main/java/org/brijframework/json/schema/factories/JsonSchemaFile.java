package org.brijframework.json.schema.factories;

import java.util.ArrayList;
import java.util.List;

public class JsonSchemaFile {
	public String id;
	public String type;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<JsonSchemaObject> getObjects() {
		if(objects==null) {
			objects=new ArrayList<JsonSchemaObject>();
		}
		return objects;
	}

	public void setObjects(List<JsonSchemaObject> objects) {
		this.objects = objects;
	}

}
