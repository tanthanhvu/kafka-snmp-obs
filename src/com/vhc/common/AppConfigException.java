package com.vhc.common;

/**
 * <p>
 * Title: 
 * </p>
 * <p>
 * Description: 
 * </p>
 * <p>
 * Copyright: Copyright (c) by VHCSoft 2016
 * </p>
 * <p>
 * Company: VietNam High Technology Software JSC.
 * </p>
 * <p>
 * Create Date:Aug 9, 2017
 * </p>
 * 
 * @author VHC - Software
 * @version 1.0
 */

public class AppConfigException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public AppConfigException() {
		super();
	}
	
	public AppConfigException(String message) {
		super(message);
	}
	
	public AppConfigException(Throwable throwable) {
		super(throwable);
	}
	
	public AppConfigException (String message, Throwable throwable) {
		super(message, throwable);
	}
}

