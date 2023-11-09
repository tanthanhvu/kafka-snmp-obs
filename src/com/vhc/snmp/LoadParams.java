
package com.vhc.snmp;

import java.util.ResourceBundle;

/**
 * Load parameters
 * 
 * @author thanhdh
 * 
 */
public class LoadParams {
	private static final ResourceBundle Bundle = ResourceBundle
			.getBundle("config");

	public static String get(String key) {
		try {
			return Bundle.getString(key);
		} catch (Exception e) {
			return null;
		}
	}
}
