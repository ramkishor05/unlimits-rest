package org.unlimits.rest.context;

import org.unlimits.rest.crud.beans.IUserAccount;

/**
 *  @author omnie
 */
public class ApiSecurityContext {
	
	private static ApiSecurityContext securityContext;
	
	private ThreadLocal<IUserAccount> userAccountRequest=new ThreadLocal<IUserAccount>();

	/**
	 * @return
	 */
	public static ApiSecurityContext getContext() {
		synchronized (ApiSecurityContext.class) {
			if(securityContext==null) {
				securityContext=new ApiSecurityContext();
			}
		}
		return securityContext;
	}

	/**
	 * @return
	 */
	public IUserAccount getCurrentAccount() {
		return userAccountRequest.get();
	}

	/**
	 * @param eoUserAccount
	 */
	public void setCurrentAccount(IUserAccount eoUserAccount) {
		this.userAccountRequest.set(eoUserAccount);
	}

}
