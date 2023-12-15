package com.vhc.snmp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.vhc.common.AppConfig;
import com.vhc.kafka.consumer.ConsumerService;
import com.vhc.kafka.consumer.KafkaConsumerConfig;
import com.vhc.model.StructAlarm;
import com.vhc.util.DbUtil;



public class SnmpObserver {
	final static Logger logger = Logger.getLogger(SnmpObserver.class); 
	
	public static BlockingQueue<StructAlarm> _mAlarmQueueFromSocket = null;
	
	static ProcessAlarm mProcessAlarm = null; 
	
	private static Boolean _getDataFromKafka = true;
	
		
	public SnmpObserver() throws FileNotFoundException, IOException {
		_mAlarmQueueFromSocket = new ArrayBlockingQueue<>(5000);
		mProcessAlarm = new ProcessAlarm(_mAlarmQueueFromSocket); 
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
		AppConfig.loadOIDMap();  
		DbUtil.init();
		KafkaConsumerConfig.loadKafkaConsumerConfig();
		
		new SnmpObserver().run();
		 
	}
	
	private void run() {
		try {
			if(_getDataFromKafka)
			{
				Thread _ps = (Thread) new ConsumerService();
			
				_ps.start();
				
				mProcessAlarm.start();  
				
				new Thread(mProcessAlarm._2GThread).start();
				new Thread(mProcessAlarm._3GThread).start();
				new Thread(mProcessAlarm._4GThread).start();
				new Thread(mProcessAlarm._coreThread).start(); 
			}
		
		while(true)
		{
			System.out.println("\n[INFO] => Checking Kafka server's status ... ");
			
			try
			{
				if(
						!ConsumerService.isConsumerConnected()) 

//					!ConsumerService.isKafkaServerRunning())
				{
					System.out.println("\n\n[INFO] => Trying to reconnect to Kafka server ... \n\n");
//					ConsumerService.restart();
					
					TimeUnit.SECONDS.sleep(0xa);
				}
				else
				{
					TimeUnit.SECONDS.sleep(0xa * 0x6);
				}
			}
			catch(Exception e)
			{
				System.out.println("[INFO] => Obs exception... " + e);
			}
		}
	} 
	catch (Exception e) 
	{
		System.out.println("[INFO] => Obs running: " + e);
		
		logger.error(e, e);			
	}
	}
	
}
