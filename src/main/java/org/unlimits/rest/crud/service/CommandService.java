package org.unlimits.rest.crud.service;

public interface CommandService<DT, EN, ID>  extends CQRSService<DT, EN, ID>{

	/***
	 * 
	 * @param data
	 * @return
	 */
	DT add(DT data);
	/***
	 * 
	 * @param data
	 * @return
	 */
	DT update(DT data);
	
	DT update(ID id, DT dto);
	/***
	 * 
	 * @param uuid
	 * @return
	 */
	Boolean delete(ID uuid);
	
	DT find(ID uuid);
}
