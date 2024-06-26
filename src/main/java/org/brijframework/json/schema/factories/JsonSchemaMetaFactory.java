package org.brijframework.json.schema.factories;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSchemaMetaFactory implements SingletonFactory{

	private static final String LK = "LK@";

	final ConcurrentHashMap<String, JsonSchemaObject> cache = new ConcurrentHashMap<String, JsonSchemaObject>();

	final String beans = "beans";

	// singleton pattern
	private static JsonSchemaMetaFactory instance = null;

	public ConcurrentHashMap<String, JsonSchemaObject> getCache() {
		return cache;
	}

	public static JsonSchemaMetaFactory getInstance() {
		synchronized (JsonSchemaMetaFactory.class) {
			if (instance == null) {
				instance = new JsonSchemaMetaFactory();
			}
		}
		return instance;
	}

	private JsonSchemaMetaFactory() {
		this.init(beans);
		for (JsonSchemaObject segmentMetaData : this.getCache().values()) {
			buildRelationship(segmentMetaData);
		}
	}

	
	@Override
	public void load(Path file, InputStream inputStream) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonSchemaFile jsonSchemaFile = objectMapper.readValue(inputStream, JsonSchemaFile.class);
			List<JsonSchemaObject> schemaObjects = jsonSchemaFile.getObjects();
			for (JsonSchemaObject segmentMetaData : schemaObjects) {
				getCache().put(segmentMetaData.getId(), segmentMetaData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void buildRelationship(JsonSchemaObject segmentMetaData) {
		Map<String, Object> properties = segmentMetaData.getProperties();
		properties.entrySet().forEach(entry -> {
			Object value = entry.getValue();
			if (value instanceof String) {
				String ref = value.toString();
				if (ref.startsWith(LK)) {
					String[] refInfos = ref.split(LK);
					JsonSchemaObject refInfo = this.getCache().get(refInfos[1].trim());
					entry.setValue(refInfo);
				}
			}
		});
		Map<String, Object> relationship = segmentMetaData.getRelationship();
		relationship.entrySet().forEach(entry -> {
			Object value = entry.getValue();
			if (value instanceof String) {
				String ref = value.toString();
				if (ref.startsWith(LK)) {
					String[] refInfos = ref.split(LK);
					JsonSchemaObject refInfo = this.getCache().get(refInfos[1].trim());
					entry.setValue(refInfo);
				}
			}
		});
	}

	public List<JsonSchemaObject> getAll(Class<? extends Object> type) {
		List<JsonSchemaObject> typeObjectList = new ArrayList<>();
		for (JsonSchemaObject segmentMetaData : this.getCache().values()) {
			if (segmentMetaData.getType().equals(type.getName())) {
				typeObjectList.add(segmentMetaData);
			}
		}
		return typeObjectList;
	}

}
