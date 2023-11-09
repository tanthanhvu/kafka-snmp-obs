package com.vhc.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.vhc.model.StructAlarm;

public class AlarmNokiaHandler implements  Runnable{

	private static final Logger logger = Logger.getLogger(AlarmNokiaHandler.class);

	private BlockingQueue<StructAlarm> queue;

	private Connection connection;

	private PreparedStatement stmt; 

	// dung de phan biet thread import ran2g, 3g, ...
	private String type;
	
	// dem so canh bao xu ly
	private int count = 0;
	
	// Batch size for severity update
	private final int _SEVERITY_BATCH_SIZE = 0x1f4/5;
	
	// Batch size counter
	private int _mBatchSizeCounter = 0;
	
	private long _lSeverityStart = 0L;
	
	private final int _DIFF_IN_SECS = 0x1;

	private long _lMillisToProcess = 0L;
	
	// Connection for severity update (additional & special implementation)
	private Connection _severityConnection = null;
	private PreparedStatement _severityStmt = null;
	
	// Count the alarms with sdate != null and alarms w/ edate != null
	private long _lAlarmsStartCounter = 0L;
	private long _lAlarmsECounterSuccess = 0L;
	private long _lAlarmsECounterWithInfo = 0L; 
	
	// Statement for batch processing
	// Added by: pt
	private String _severityProc = "UPDATE R_ALARM_LOG_ACTIVE SET EDATE= TO_DATE(?,'yyyy-MM-dd,hh24:mi:ss')" + 
			",CLEAR_TIME_IN_DB= SYSDATE" + ",END_RECEIVE= TO_DATE(?,'dd-MM-yyyy hh24:mi:ss') "+
			" WHERE EDATE IS NULL AND FM_ALARMID = ? AND NE= ?"; 

	/*
	 * AlarmHuaweiHandler()
	 * Init
	 */
	public AlarmNokiaHandler(String type, BlockingQueue<StructAlarm> queue) 
	{
		try 
		{
			this.type = type;
			this.queue = queue;
			
			// Added by pt, separate batch processing session
			// Get connection for update
			_severityConnection = DbUtil.getConnection();

			// Prepare statement
			_severityStmt = _severityConnection.prepareStatement(_severityProc);
			
			// Disable auto-commit
			_severityConnection.setAutoCommit(false);
			
			 
			// Get connection for insert
			connection = DbUtil.getConnection();
			
			stmt = connection.prepareStatement(
									"{call PK_AL_CORE_ANHNT.pr_insert_alarm_NSN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"
								); 
		} 
		catch (SQLException e) 
		{
			logger.error(e, e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() 
	{

		long lExecOneStmt = 0L;
		long lStart = 0L;
		
		while (true) 
		{
			
			StructAlarm alarm = new StructAlarm();

			try 
			{
				
				alarm = queue.take();
				
				//logger.error(type + " remain queue: " + queue.size());
				
				System.out.println("vao day");
				
				// Convert data
				alarm = AlarmInfo.setAlarmInfo(alarm); 
				
				System.out.println("vao day");
				
				
				logger.info(alarm.toString());
				//System.out.println(alarm.toString());
				// Batch processing, modified by pt
				if(alarm.recordType.equals("END") 
						&& !alarm.nbiSpecificProblem.startsWith("8502")
							&& !alarm.nbiSpecificProblem.startsWith("9047")) 
				{
					severityBatchUpdate(alarm);
				}
				else 
				{
				
					lStart = System.currentTimeMillis();
					
					insertAlarm(alarm);

					lExecOneStmt += (System.currentTimeMillis() - lStart);

					if (count++ >= 50) 
					{
					
						// Print after every 50 alarms received
						logger.error("INSERT PROCESSED ==> " + type + ": " + count + " ALARMS/ TOTAL => " + (_lAlarmsStartCounter += count) + 
								" , TOOK: " + lExecOneStmt/1000 + "." + lExecOneStmt%1000 + " SECS" +
								"\n REMAINED DATA QUEUE => " + queue.size() + "\n");
					
						// Reset counter
						count = 0;
						lExecOneStmt = 0;
					}
				}
			} 
			catch (Exception e) 
			{
				logger.error("Loi class run(): "+e); 
			}
		}

	}
	

	/*
	 * 	handleAlarm(StructAlarm alarm)
	 * 
	 * 	Inserts one alarm to DB table
	 */
	private void insertAlarm(StructAlarm structAlarm)
	{
		
		// Prepare statement
		try {
			stmt.setString(1, structAlarm.cellid);
			stmt.setString(2, structAlarm.nbiAlarmType);
			stmt.setString(3, structAlarm.nbiPerceivedSeverity);
			stmt.setString(4, structAlarm.nbiSpecificProblem);
			stmt.setString(5, structAlarm.site);
			stmt.setString(6, structAlarm.ne);
			stmt.setString(7, structAlarm.nbiAdditionalText);
			stmt.setString(8, structAlarm.recordType);
			stmt.setString(9, structAlarm.nbiAlarmId);
			stmt.setString(10, structAlarm.neType);
			stmt.setString(11, structAlarm.nbiClearTime);
			stmt.setString(12, structAlarm.nbiAlarmTime);
			stmt.setString(13, structAlarm.nbiObjectInstance);
			stmt.setString(14, structAlarm.isSendSms);
			stmt.setString(15, structAlarm.isMonitor); 
			stmt.setString(16, structAlarm.network); 
			stmt.setString(17, structAlarm.region);
			stmt.setString(18, structAlarm.province);
			stmt.setString(19, structAlarm.district);
			stmt.setString(20, structAlarm.dept);
			stmt.setString(21, structAlarm.team);
			stmt.setString(22, structAlarm.ipAddress);
			stmt.setString(23, structAlarm.isMll);
			stmt.setString(24, structAlarm.tgNhan); 

			// Execute to insert
			stmt.executeUpdate();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Loi class insertAlarm(): " +e); 
		} 
	} 

	/*
	 * 	severityBatchUpdate()
	 * 	The extension implementation for severity update only.
	 * 
	 * 	Author: pt
	 */
	private void severityBatchUpdate(StructAlarm alarm)
	{
		try 
		{
			
			// Prepare update statement
			_severityStmt.setString(1, alarm.nbiClearTime); 
			_severityStmt.setString(2, alarm.tgNhan); 
			_severityStmt.setString(3, alarm.nbiAlarmId);
			_severityStmt.setString(4, alarm.ne);
			
			// Not reaching to limit, add more
			if(_mBatchSizeCounter < _SEVERITY_BATCH_SIZE) 
			{
				_severityStmt.addBatch(); 
			}
			
			// Increase batch
			_mBatchSizeCounter++;
			
			// Save the point where 1st batch's stmt added
			if(_lSeverityStart == 0L) 
			{
				_lSeverityStart = System.currentTimeMillis();
			}
			// Either reaching to limit or over waiting time
			if(_mBatchSizeCounter == _SEVERITY_BATCH_SIZE ||
					((System.currentTimeMillis() - _lSeverityStart)/1000/60 >= _DIFF_IN_SECS &&
					_mBatchSizeCounter >= 1)) 
			{
				
				// To be shown in display process info
				_lMillisToProcess = System.currentTimeMillis();

				int[] _severityAffected = _severityStmt.executeBatch();
				
				/*
				int _severitySuccess = 0;
				int _severityInfo = 0;
				int _severityEx = 0;
				int _affected = -1;
				
				// Verify batch update status
				for(int i = 0; i < _severityAffected.length; _affected = _severityAffected[i]) 
				{
					
					// Batch stmt gets success
					if(_affected == 1) 
					{
						_severitySuccess++;
					}
					// Batch stmt gets info 
					else if (_affected == 2) 
					{
						_severityInfo++ ;
					}
					else 
					{	// Exception raised within batch
						_severityEx++;
					}
					i++;
				}
				*/
				
				// Show things
				displayProcessInfo(_severityAffected.length, 0, 0);
				
				// Reset counters
				_mBatchSizeCounter = 0;
					
				_lMillisToProcess = 0;
				
				_lSeverityStart = System.currentTimeMillis();

				// Commit things
				_severityConnection.commit();
				
				// Refresh to get new one
				_severityStmt.clearBatch();
			}
		}
		catch(Exception e) 
		{
			logger.error("Loi class severityBatchUpdate(): "+e);
		}
	}
	/*
	 * 	displayProcessInfo()
	 */
	private void displayProcessInfo(int _success, int _info, int _exception)
	{
		long _lMillisDiff = 0L;

		String _showInfo = "";
		
		_showInfo = "\n [UPDATE PROCESSED " + type + " SUCCESS (" + _success + ")";
		
		_showInfo = _showInfo + " * INFO (" + _info + ") * EXCEPTION (" + _exception + ")";
		
		_showInfo = _showInfo + " TOOK " + (_lMillisDiff = (System.currentTimeMillis() - _lMillisToProcess))/1000;

		_showInfo = _showInfo + "." + _lMillisDiff%1000 + " SECS TO COMPLETE \n";
		
		_showInfo = _showInfo + " ALARMS UPDATED SUCCESS/INFO (" + (_lAlarmsECounterSuccess += _success); // Show while increasing numbers of success
		
		_showInfo = _showInfo + "/ " + (_lAlarmsECounterWithInfo += _exception) + ")";	// Show and count-up exceptions
		
		_showInfo = _showInfo + "\nALARMS REMAINED IN QUEUE => " + queue.size() + "\n";
		
		logger.error(_showInfo);
	}
	
}
