package com.vhc.snmp;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.vhc.model.StructAlarm;
import com.vhc.util.AlarmNokiaHandler;
import com.vhc.test.DbConnection;

public class ProcessAlarm extends Thread{
	private final Logger logger = Logger.getLogger(ProcessAlarm.class.getName()); 
	
	private static final int QUEUE_SIZE = 10000;
	
	// Queue data from Queue socket
	BlockingQueue<StructAlarm> _mAlarmQueue;
	
	// Quece data classifyAlarm
	private BlockingQueue<StructAlarm> queue2G;
	private BlockingQueue<StructAlarm> queue3G;
	private BlockingQueue<StructAlarm> queue4G;
	private BlockingQueue<StructAlarm> queueCore;  
	
	// Thread handling alarm
	protected AlarmNokiaHandler _2GThread;
	protected AlarmNokiaHandler _3GThread;
	protected AlarmNokiaHandler _4GThread;
	protected AlarmNokiaHandler _coreThread;  
	
	public ProcessAlarm(BlockingQueue<StructAlarm> _alarmQueue) {
		// TODO Auto-generated constructor stub
		_mAlarmQueue = _alarmQueue;
		
//		queue2G = new ArrayBlockingQueue<StructAlarm>(QUEUE_SIZE);
//		queue3G = new ArrayBlockingQueue<StructAlarm>(QUEUE_SIZE);
//		queue4G = new ArrayBlockingQueue<StructAlarm>(QUEUE_SIZE);
//		queueCore = new ArrayBlockingQueue<StructAlarm>(QUEUE_SIZE); 
//		
//		_2GThread = new AlarmNokiaHandler("RAN_2G", queue2G);
//		_3GThread = new AlarmNokiaHandler("RAN_3G", queue3G);
//		_4GThread = new AlarmNokiaHandler("RAN_4G", queue4G);
//		_coreThread = new AlarmNokiaHandler("CORE", queueCore);   
	}
	
	@Override
    public void run() 
    {  
		try
		{
			while(true)
			{
	
				//process alarm, remore after things done
				StructAlarm structAlarm = _mAlarmQueue.take(); 
				
				DbConnection dbConnection = new DbConnection();
				dbConnection.insertData(structAlarm);
				
//				System.out.println("aa12");
//				 
//				if(structAlarm.network.equals("2G") && queue2G.size() < QUEUE_SIZE) {
//					System.out.println("b12");
//					queue2G.add(structAlarm);
//					System.out.println("b13");
//					//System.out.println("Add 2G succ alarmId = "+structAlarm.nbiAlarmId);
//				} else if(structAlarm.network.equals("3G") && queue3G.size() < QUEUE_SIZE) {
//					System.out.println("b12");
//					queue3G.add(structAlarm);
//					System.out.println("b12");
//					//System.out.println("Add 3g succ alarmId = "+structAlarm.nbiAlarmId);
//				} else if(structAlarm.network.equals("RAN_4G") && queue4G.size() < QUEUE_SIZE) {
//					System.out.println("b12");
//					queue4G.add(structAlarm);
//					System.out.println("b12");
//					//System.out.println("Add 4g succ alarmId = "+structAlarm.nbiAlarmId);
//				} else if (queueCore.size() < QUEUE_SIZE){
//					System.out.println("b12");
//					queueCore.add(structAlarm);
//					System.out.println("b12");
//					//System.out.println("Add core succ alarmId = "+structAlarm.nbiAlarmId);
//				}
			}
		}
		catch(Exception e)
		{  
			logger.error("---------------_QUEUE_SIZE = "+QUEUE_SIZE);
			logger.error("---------------_mAlarmQueue.size = "+_mAlarmQueue.size());
			logger.error("---------------_queue2G.size = "+queue2G.size());
			logger.error("---------------_queue3G.size = "+queue3G.size());
			logger.error("---------------_queue4G.size = "+queue4G.size());
			logger.error("---------------_queueCore.size = "+queueCore.size()); 
			logger.error("---------------_Loi ProcessAlarm run(): "+e);
		}
    }
}
