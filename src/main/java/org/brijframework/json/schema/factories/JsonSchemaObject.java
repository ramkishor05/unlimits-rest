package org.brijframework.json.schema.factories;

import java.util.HashMap;
import java.util.Map;

public class JsonSchemaObject {
	public String id;
	public String type;
	public String name;
	public Map<String, Object> properties;
	public Map<String, Object> relationship;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public Map<String, Object> getRelationship() {
		if(relationship==null) {
			relationship=new HashMap<String, Object>();
		}
		return relationship;
	}

	public void setRelationship(Map<String, Object> relationship) {
		this.relationship = relationship;
	}

	
}
