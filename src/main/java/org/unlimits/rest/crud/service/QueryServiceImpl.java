package org.unlimits.rest.crud.service;

import java.util.HashMap;
import java.util.Map;

import org.unlimits.rest.repository.CustomPredicate;

public abstract class QueryServiceImpl<DT, EN, ID>  implements QueryService<DT, EN, ID> {
	
	private Map<String, CustomPredicate<EN>> customPredicateMap =new  HashMap<String, CustomPredicate<EN>>();
	
	private Map<String, String> customSortingMap =new  HashMap<String, String>();
	
	@Override
	public Map<String, CustomPredicate<EN>> getCustomPredicateMap() {
		return customPredicateMap;
	}
	
	@Override
	public Map<String, String> getCustomSortingMap() {
		return customSortingMap;
	}

}
