package com.vhc.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.Properties;

import org.apache.log4j.Logger;

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

public class AppConfig {
	
	private static final String CONF_PATH = "conf";
	private static final String BONECP_CONF_FILE_DB = "db-config.properties";
	private static final String OID_MAPPING_INFO = "oid-mapping.properties";  
	 
	private static final Logger logger = Logger.getLogger(AppConfig.class); 
	
	/**
	 * Get path of config file
	 * @param location
	 * @return
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 */
	public static String getLocation(String location) throws MalformedURLException, UnsupportedEncodingException {
		String result = "";

		CodeSource src = AppConfig.class.getProtectionDomain().getCodeSource();

		URL url = new URL(src.getLocation(), location);
		result = URLDecoder.decode(url.getPath(), "utf-8");

		return result;
	}

	/**
	 * Get BoneCP configuration properties.
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Properties loadDBConnectInfo(){
		Properties prop = new Properties();
		String boneCPConfigFile = ""; 
		
		try {
			boneCPConfigFile = getLocation(CONF_PATH) + "/" + BONECP_CONF_FILE_DB;
			
			prop.load(new FileInputStream(boneCPConfigFile));
		} catch (FileNotFoundException e) {
			logger.error("File " + BONECP_CONF_FILE_DB + " not found");
		} catch (IOException e) {
			logger.error("Read file " + BONECP_CONF_FILE_DB + " error");
		}
		
		return prop;
	}
	
	public static void loadOIDMap() throws FileNotFoundException, IOException {
		String connectInfo = "";
		connectInfo = getLocation(CONF_PATH) + "/" + OID_MAPPING_INFO;
		InputStream propsStream = null;
		try {
			propsStream = new FileInputStream(connectInfo);
			Global.OID_CONFIG.load(propsStream);
			propsStream.close();
		} catch (FileNotFoundException e) {
			logger.error("File " + propsStream + " not found");
		} catch (IOException e) {
			logger.error("Read file " + propsStream + " error");
		}
	}  	
}

