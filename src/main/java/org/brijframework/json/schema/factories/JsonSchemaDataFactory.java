package org.brijframework.json.schema.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class JsonSchemaDataFactory {

	final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<String, Object>();

	final String beans = "beans";

	// singleton pattern
	private static JsonSchemaDataFactory instance = null;

	public ConcurrentHashMap<String, Object> getCache() {
		return cache;
	}

	public static JsonSchemaDataFactory getInstance() {
		synchronized (JsonSchemaDataFactory.class) {
			if (instance == null) {
				instance = new JsonSchemaDataFactory();
			}
			return instance;
		}
	}

	private JsonSchemaDataFactory() {
		this.init();
	}

	private void init() {
		JsonSchemaMetaFactory.getInstance().getCache().forEach((key,val)-> {
			register(key,val);
		});
	}

	private void register(String key, JsonSchemaObject val) {
		buildObject(val);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getAll(Class<T> type){
		List<T> typeObjectList =new ArrayList<>();
		for(Object object :  this.getCache().values()) {
			if(object.getClass().equals(type)){
				typeObjectList.add((T)object);
			}
		}
		return typeObjectList;
	}

	private Object buildObject(JsonSchemaObject segmentMetaData) {
		Object object = getCache().get(segmentMetaData.getId());
		if(object!=null) {
			return object;
		}
		Object instance = ReflectionUtils.getInstance(segmentMetaData.getType());
		segmentMetaData.getProperties().forEach((key, val)->{
			if(val instanceof JsonSchemaObject) {
				JsonSchemaObject schemaObject= (JsonSchemaObject)val;
				ReflectionUtils.setField(instance, buildObject(schemaObject), key);
			} else {
				ReflectionUtils.setField(instance,  val, key);
			}
		});
		segmentMetaData.getRelationship().forEach((key, val)->{
			if(val instanceof JsonSchemaObject) {
				ReflectionUtils.setField(instance,  buildObject((JsonSchemaObject)val), key);
			} else {
				ReflectionUtils.setField(instance,  val, key);
			}
		});
		getCache().put(segmentMetaData.getId(), instance);
		return instance;
	}
}
