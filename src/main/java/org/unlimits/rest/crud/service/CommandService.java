package org.unlimits.rest.crud.service;

import java.util.List;
import java.util.Map;

public interface CommandService<DT, EN, ID>  extends CQRSService<DT, EN, ID>{

	/***
	 * 
	 * @param data
	 * @param headers 
	 * @return
	 */
	DT add(DT data, Map<String,  List<String>> headers);
	/***
	 * 
	 * @param data
	 * @return
	 */
	DT update(DT data, Map<String, List<String>> headers);
	
	DT update(ID id, DT dto, Map<String,  List<String>> headers);
	/***
	 * 
	 * @param uuid
	 * @return
	 */
	Boolean delete(ID uuid);
	
	DT find(ID uuid);
}
