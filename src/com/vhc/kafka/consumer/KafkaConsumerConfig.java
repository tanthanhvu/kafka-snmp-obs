package com.vhc.kafka.consumer;

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

import com.vhc.common.AppConfig;
import com.vhc.common.Global;

public class KafkaConsumerConfig {
	private static final String CONF_PATH = "conf";
	
	private static final String KAFKA_CONF_FILE = "kafka-consumer.properties";
	
	public static final Properties KAFKA_CONFIG = new Properties();
	
	private static final Logger logger = Logger.getLogger(KafkaConsumerConfig.class); 

	
	public static String getLocation(String location) throws MalformedURLException, UnsupportedEncodingException {
		String result = "";

		CodeSource src = AppConfig.class.getProtectionDomain().getCodeSource();

		URL url = new URL(src.getLocation(), location);
		result = URLDecoder.decode(url.getPath(), "utf-8");

		return result;
	}
	
	public static void loadKafkaConsumerConfig() throws FileNotFoundException, IOException {
		String connectInfo = "";
		connectInfo = getLocation(CONF_PATH) + "/" + KAFKA_CONF_FILE;
		InputStream propsStream = null;
		try {
			propsStream = new FileInputStream(connectInfo);
			KAFKA_CONFIG.load(propsStream);
			propsStream.close();
		} catch (FileNotFoundException e) {
			logger.error("File " + propsStream + " not found");
		} catch (IOException e) {
			logger.error("Read file " + propsStream + " error");
		}
	}  
}
