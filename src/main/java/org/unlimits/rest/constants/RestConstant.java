package org.unlimits.rest.constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class RestConstant {

	public static final String SORT_ORDER="sortOrder";
	
	public static final String ORDER_BY="orderBy";
	
	public static final String SORT="sort";
	
	public static final String INCLUDE_KEYS="includeKeys";
	
	public static final String EXCLUDE_KEYS="excludeKeys";
	
	public static List<String> getIncludeKeys(Map<String, Object> actions){
		Object actionInclude = actions.get(INCLUDE_KEYS);
		if(actionInclude!=null) {
			String[] actionIncludeKeys = actionInclude.toString().split(",");
			return Stream.of(actionIncludeKeys).map(key->key.trim()).toList();
		}else {
			return new ArrayList<String>();
		}
	}
	
	public static boolean isIncludeKey(Map<String, Object> actions, String findKey){
		Object actionInclude = actions.get(INCLUDE_KEYS);
		if(actionInclude!=null) {
			String[] actionIncludeKeys = actionInclude.toString().split(",");
			return Stream.of(actionIncludeKeys).map(key->key.trim()).anyMatch(key->key.equalsIgnoreCase(findKey));
		}else {
			return false;
		}
	}
	
	public static List<String> getExcludeKeys(Map<String, Object> actions){
		Object actionInclude = actions.get(EXCLUDE_KEYS);
		if(actionInclude!=null) {
			String[] actionIncludeKeys = actionInclude.toString().split(",");
			return Stream.of(actionIncludeKeys).map(key->key.trim()).toList();
		}else {
			return new ArrayList<String>();
		}
	}
	
	public static boolean isExcludeKey(Map<String, Object> actions, String findKey){
		Object actionInclude = actions.get(EXCLUDE_KEYS);
		if(actionInclude!=null) {
			String[] actionIncludeKeys = actionInclude.toString().split(",");
			return Stream.of(actionIncludeKeys).map(key->key.trim()).anyMatch(key->key.equalsIgnoreCase(findKey));
		}else {
			return false;
		}
	}
	
}
