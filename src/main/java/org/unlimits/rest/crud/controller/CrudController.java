/**
 * 
 */
package org.unlimits.rest.crud.controller;

import org.unlimits.rest.crud.service.CrudService;

/**
 *  @author ram kishor
 */
public interface CrudController<DT, EN, ID> extends CommandController<DT, EN, ID>, QueryController<DT, EN, ID>{
	
	
	public CrudService<DT, EN, ID> getService();


}
