/**
 * 
 */
package org.unlimits.rest.crud.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.unlimits.rest.repository.CustomPredicate;

/**
 * @author ram kishor
 */
public abstract class CrudServiceImpl<DT, EN, ID> implements CrudService<DT, EN, ID> {
	
	private Map<String, CustomPredicate<EN>> customPredicateMap =new  HashMap<String, CustomPredicate<EN>>();
	
	private List<String>  ignoreProperties=new ArrayList<String>();
	
	@Override
	public Map<String, CustomPredicate<EN>> getCustomPredicateMap() {
		return customPredicateMap;
	}
	
	@Override
	public List<String> ignoreProperties() {
		return ignoreProperties;
	}
}
