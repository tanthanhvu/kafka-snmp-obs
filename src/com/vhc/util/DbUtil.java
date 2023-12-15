package com.vhc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.vhc.common.AppConfig;

import com.vhc.model.AlarmType;
import com.vhc.model.CellN;
import com.vhc.model.CellN3G;
import com.vhc.model.CellN4G;
import com.vhc.model.HCoreN;
import com.vhc.model.HProvinceCode;

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

public class DbUtil { 
	final static Logger logger = Logger.getLogger(DbUtil.class); 

	private static BoneCP dbConnectionPool = null;
	 
	private static int queryTimeout = 30; // seconds
	
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	public static List<CellN> BTS = new ArrayList<CellN>();
	public static List<CellN3G> BTS_3G = new ArrayList<CellN3G>();
	public static List<HCoreN> CORE_LIST = new ArrayList<HCoreN>();
	public static List<CellN4G> CELL_4G = new ArrayList<CellN4G>();
	
	public static ConcurrentMap<String, CellN> BSCID = new ConcurrentHashMap<String, CellN>();
	public static ConcurrentMap<String, CellN3G> RNCID = new ConcurrentHashMap<String, CellN3G>(); 
	public static ConcurrentMap<String, CellN4G> NODE4GID = new ConcurrentHashMap<String, CellN4G>(); 
	public static ConcurrentMap<String, HProvinceCode> 	PROVINCE_BY_NE = new ConcurrentHashMap<String, HProvinceCode>();
	public static ConcurrentMap<String, String>  H_MAP_IP_NODE_CISCO = new ConcurrentHashMap<String, String>();
	
	public static List<AlarmType> ALARM_CONFIG = new ArrayList<AlarmType>(); 
	public static List<HProvinceCode> PROVINCE_BY_CODE = new ArrayList<HProvinceCode>(); 
	
	//Connect DB
	static { 
		try {
			BoneCPConfig boneCPConfigDb = new BoneCPConfig(AppConfig.loadDBConnectInfo());
			dbConnectionPool = new BoneCP(boneCPConfigDb);
			System.out.println(dbConnectionPool);
			
			logger.debug("[INFO] Connection to the database successful");
		} catch (Exception e) {
			logger.error("[ERROR] Can not establish connection to the database.", e);
		}
		
		long period = 12*3600*1000;
		
		final Runnable reset = new Runnable() {
			public void run() {
				logger.debug("[INFO] " + new Date() + ": reload from database!");
				init();
			}
		};
		scheduler.scheduleAtFixedRate(reset, period, period, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Get connect DB
	 * @return
	 * @throws SQLException
	 */ 
	public static Connection getConnection() throws SQLException {
		System.out.println(dbConnectionPool);
		return dbConnectionPool.getConnection();
	}
	
	/**
	 * Monitoring Bone cp Connection pool
	 * @return
	 */
	public static boolean getDbStatus() {
		
		try {
			return dbConnectionPool.getTotalFree() > 0 ? true : false;
		} catch (Exception e) {}
		
		return false;
	}
	
	/**
	 * Initial
	 */
	public static void init() {
		
		// load BTS
		loadBTS();
		
		// load BTS
		loadBTS_3G();
		
		// load Cell 4G
		loadCELL_4G();
		
		// load BSC
		loadBSCID();
		
		// load RNC list
		loadRNCID();
		
		// load Node 4G
		loadNODE4GID();
		
		// load Core list
		loadCOREID();
		
		// load alarm config
		loadAlarmConfig(); 
		
		//load quan huyen theo ne
		loadProvinceByNe();
		
		//load quan huyen theo site
		loadProvinceByCode();  
	}
	
	/**
	 * Load BTS
	 */
	private static void loadBTS() {
		String mSQL = "select SUBSTR(objectname,0,INSTR(t.OBJECTNAME,'/BTS')-1) bts_objectname, T.objectname, T.bscname, T.btsname, nvl(cellname,segmentname) segmentname from DIMRAN2GNOKIA T order by DATEADDED desc";
		
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeout);
			
			rs = stmt.executeQuery(mSQL); 
			
			if(rs != null) {
				// clear 
				BTS.clear();
				
				CellN cellN = null;
				while(rs.next()) {
					cellN = new CellN(rs.getString("bts_objectname"), rs.getString("objectname"), null,
							rs.getString("bscname"), rs.getString("btsname"),
							rs.getString("segmentname"));
					BTS.add(cellN);
				}
				
				rs.close();
				rs = null;
				stmt.close();
				stmt = null;
				
				logger.info("[INFO] Load BTS succesful");
			}  
		} catch (Exception e) {
			logger.error("Can not load BTS", e);
		} finally {
            try {
            	conn.close();
    			conn = null;
            } catch (Exception e) {
            	logger.error("error", e);
            }
        }
	}
	
	/**
	 * Load BTS 3G
	 */
	private static void loadBTS_3G() {
		String mSQL = "select SUBSTR(objectname,0,INSTR(t.OBJECTNAME,'/WCEL')-1) bts_objectname, T.objectname, t.rncname, t.wbtsname, t.cellname from DIMRAN3GNOKIA T order by DATEADDED desc";
		
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeout);
			
			rs = stmt.executeQuery(mSQL);
			
			if(rs != null) {
				// clear
				BTS_3G.clear();
				
				CellN3G cellN3G = null;
				while(rs.next()) {
					cellN3G = new CellN3G(rs.getString("bts_objectname"), rs.getString("objectname"), null,
							rs.getString("rncname"), rs.getString("wbtsname"),
							rs.getString("cellname"));
					BTS_3G.add(cellN3G);
				}
				
				rs.close();
				rs = null;
				stmt.close();
				stmt = null; 
				
				logger.info("[INFO] Load BTS 3G succesful");
			}
		} catch (Exception e) {
			logger.error("Can not load BTS 3G", e);
		} finally {
            try {
            	conn.close();
    			conn = null;
            } catch (Exception e) {
            	logger.error("error", e);
            }
        }
	}
	
	/**
	 * Load BSC
	 */
	private static void loadBSCID() {
		String mSQL = "select bscid, bscname from (\r\n"
				+ "    select SUBSTR(objectname,0,INSTR(OBJECTNAME,'/BCF')-1) bscid, bscname, max(DATEADDED) DATEADDED \r\n"
				+ "    from DIMRAN2GNOKIA \r\n"
				+ "    group by SUBSTR(objectname,0,INSTR(OBJECTNAME,'/BCF')-1), bscname\r\n"
				+ ") order by DATEADDED desc";
		
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeout);
			
			rs = stmt.executeQuery(mSQL);
			
			if(rs != null) {
				// clear
				BSCID.clear();
				
				CellN cellN = null;
				while(rs.next()) {
					cellN = new CellN(null, null, rs.getString("bscid"),
							rs.getString("bscname"), null,
							null);
					BSCID.putIfAbsent(cellN.getBscId(), cellN);
				}
				
				rs.close();
				rs = null;
				stmt.close();
				stmt = null;
				
				logger.info("[INFO] Load BSCID succesful");
			} 
		} catch (Exception e) {
			logger.error("Can not load BSCID", e);
		} finally {
            try {
            	conn.close();
    			conn = null;
            } catch (Exception e) {
            	logger.error("error", e);
            }
        }
	}
	
	/**
	 * Load RNC
	 */
	private static void loadRNCID() {
		String mSQL = "select rncid, rncname from (\r\n"
				+ "    select SUBSTR(objectname, 0, INSTR(OBJECTNAME,'/WBTS')-1) rncid, rncname, max(DATEADDED) DATEADDED \r\n"
				+ "    from DIMRAN3GNOKIA \r\n"
				+ "    group by SUBSTR(objectname, 0, INSTR(OBJECTNAME,'/WBTS')-1), rncname\r\n"
				+ ") order by DATEADDED desc";
		
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeout);
			
			rs = stmt.executeQuery(mSQL);
			
			if(rs != null) {
				// clear
				RNCID.clear();
				
				CellN3G cellN3G = null;
				while(rs.next()) {
					cellN3G = new CellN3G(null, null, rs.getString("rncid"),
							rs.getString("rncname"), null, null);
					RNCID.putIfAbsent(cellN3G.getRncid(), cellN3G);
				}
				
				rs.close();
				rs = null;
				stmt.close();
				stmt = null;
				
				logger.info("[INFO] Load RNCID succesful");
			} 
		} catch (Exception e) {
			logger.error("Can not load RNCID", e);
		} finally {
            try {
            	conn.close();
    			conn = null;
            } catch (Exception e) {
            	logger.error("error", e);
            }
        }
	}
	
	/**
	 * Load NODE 4G
	 */
	private static void loadNODE4GID() {
		String mSQL = "select NODEID, NODENAME from (\r\n"
				+ "    select NODEID, NODENAME, max(DATEADDED) DATEADDED \r\n"
				+ "    from DIMRAN4GCELL where vendor='NOKIA SIEMENS'\r\n"
				+ "    group by NODEID, NODENAME\r\n"
				+ ") order by DATEADDED desc";
		
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeout);
			
			rs = stmt.executeQuery(mSQL);
			
			if(rs != null) {
				// clear
				NODE4GID.clear();
				
				CellN4G cellN4G = null;
				while(rs.next()) {
					cellN4G = new CellN4G(rs.getString("NODEID"), null, rs.getString("NODENAME"), null);
					NODE4GID.putIfAbsent(cellN4G.getNodeId(), cellN4G);
				}
				
				rs.close();
				rs = null;
				stmt.close();
				stmt = null;
				
				logger.info("[INFO] Load NODE4GID succesful");
			} 
		} catch (Exception e) {
			logger.error("Can not load NODE4GID", e);
		} finally {
            try {
            	conn.close();
    			conn = null;
            } catch (Exception e) {
            	logger.error("error", e);
            }
        }
	}
	
	/**
	 * Load Cell 4G
	 */
	private static void loadCELL_4G() {
		String mSQL = "select NODEID, CELLID, NODENAME, CELLNAME from DIMRAN4GCELL  where vendor='NOKIA SIEMENS' order by DATEADDED desc";
		
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeout);
			
			rs = stmt.executeQuery(mSQL);
			
			if(rs != null) {
				// clear
				CELL_4G.clear();
				
				CellN4G cell4G = null;
				while(rs.next()) {
					cell4G = new  CellN4G(rs.getString("NODEID"), rs.getString("CELLID"), rs.getString("NODENAME"), rs.getString("CELLNAME"));
					
					CELL_4G.add(cell4G);
				}
				
				rs.close();
				rs = null;
				stmt.close();
				stmt = null; 
				
				logger.info("[INFO] Load CELL_4G succesful");
			} 
		} catch (Exception e) {
			logger.error("Can not load CELL_4G", e);
		} finally {
            try {
            	conn.close();
    			conn = null;
            } catch (Exception e) {
            	logger.error("error", e);
            }
        }
	}
	
	/**
	 * Load CORE
	 */
	private static void loadCOREID() {
		String mSQL = "select REGION,NE_TYPE,NE,NODE_ID,DEPT,TEAM,VENDOR from H_CORE_N ";
		
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeout);
			
			rs = stmt.executeQuery(mSQL);
			
			if(rs != null) {
				// clear
				CORE_LIST.clear();
				
				HCoreN hCoreN = null;
				while(rs.next()) {
					hCoreN = new HCoreN(rs.getString("REGION"), rs.getString("NE_TYPE"), rs.getString("NE"), rs.getString("NODE_ID"), 
							rs.getString("DEPT"), rs.getString("TEAM"), rs.getString("VENDOR"));
					CORE_LIST.add(hCoreN);
				}
				
				rs.close();
				rs = null;
				stmt.close();
				stmt = null;
				
				logger.info("[INFO] Load COREID succesful");
			} 
		} catch (Exception e) {
			logger.error("Can not load COREID", e);
		} finally {
            try {
            	conn.close();
    			conn = null;
            } catch (Exception e) {
            	logger.error("error", e);
            }
        }
	}
	
	/**
	 * Load alarm config
	 */
	private static void loadAlarmConfig() {
		String mSQL = "select node, block_value, alarm_info_value, alarm_type, alarm_mapping_name, alarm_mapping_id, is_monitor, is_send_sms, is_mll, search "
				+ " from c_config_alarm_type "
				+ " where vendor='NOKIA SIEMENS' "
				+ " and is_enable='Y' ";
		
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeout);
			
			rs = stmt.executeQuery(mSQL);
			
			if(rs != null) {
				// clear
				ALARM_CONFIG.clear();
				
				AlarmType alarmType = null;
				while(rs.next()) {
					alarmType = new AlarmType
							(
								rs.getString("node"), rs.getString("block_value"), rs.getString("alarm_info_value"), 
								rs.getString("alarm_type"), rs.getString("alarm_mapping_name"), 
								rs.getString("alarm_mapping_id"), rs.getString("is_monitor"), 
								rs.getString("is_send_sms"), rs.getString("is_mll"), rs.getString("search")
							);
					
					ALARM_CONFIG.add(alarmType);
				}
				
				rs.close();
				rs = null;
				stmt.close();
				stmt = null;
				
				logger.info("[INFO] Load AlarmConfig succesful");
			} 
		} catch (Exception e) {
			logger.error("Can not load AlarmConfig", e);
		} finally {
            try {
            	conn.close();
    			conn = null;
            } catch (Exception e) {
            	logger.error("error", e);
            }
        }
	}
	
	/**
	 * Load thong tin dia ly theo ten NE
	 */
	private static void loadProvinceByNe() {
		String mSQL = "select code, region, province, district, dept, team from (" + 
				"select bscid code, region, province, location_name district, dept, team, vendor FROM H_BSC " + 
				"UNION ALL " + 
				"select bscid code, region, province, location_name district, dept, team, vendor FROM H_BSC_3G " + 
				") " + 
				"where vendor='NOKIA SIEMENS'";
		
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeout);
			
			rs = stmt.executeQuery(mSQL);
			
			if(rs != null) {
				// clear
				PROVINCE_BY_NE.clear();
				 
				HProvinceCode hProvinceCode = null;
				while(rs.next()) {
					hProvinceCode = new HProvinceCode(rs.getString("code"), rs.getString("region"), rs.getString("province"), 
							rs.getString("district"), rs.getString("dept"), rs.getString("team"));
					
					PROVINCE_BY_NE.putIfAbsent(hProvinceCode.getCode(), hProvinceCode);
				}
				
				rs.close();
				rs = null;
				stmt.close();
				stmt = null; 
				
				logger.info("[INFO] Load PROVINCE_BY_NE succesful");
			} 
		} catch (Exception e) {
			logger.error("Can not load PROVINCE_BY_NE", e);
		} finally {
            try {
            	conn.close();
    			conn = null;
            } catch (Exception e) {
            	logger.error("error", e);
            }
        }
	}
	
	/**
	 * Load thong tin dia ly theo site
	 */
	private static void loadProvinceByCode() {
		String mSQL = "select code, region, province, district, dept, team from H_PROVINCES_CODE ";
		
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.setQueryTimeout(queryTimeout);
			
			rs = stmt.executeQuery(mSQL);
			
			if(rs != null) {
				// clear
				PROVINCE_BY_CODE.clear();
				
				HProvinceCode hProvinceCode = null;
				while(rs.next()) {
					hProvinceCode = new HProvinceCode(rs.getString("code"), rs.getString("region"), rs.getString("province"), 
							rs.getString("district"), rs.getString("dept"), rs.getString("team"));
					
					PROVINCE_BY_CODE.add(hProvinceCode);
				}
				
				rs.close();
				rs = null;
				stmt.close();
				stmt = null; 
				
				logger.info("[INFO] Load PROVINCE_BY_CODE succesful");
			} 
		} catch (Exception e) {
			logger.error("Can not load PROVINCE_BY_CODE", e);
		} finally {
            try {
            	conn.close();
    			conn = null;
            } catch (Exception e) {
            	logger.error("error", e);
            }
        }
	}
}

