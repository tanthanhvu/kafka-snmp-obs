/**
 * <p>
 * Copyright: Copyright (c) by VHCSoft 2019
 * </p>
 * <p>
 * Company: VietNam High Technology Software JSC.
 */
package com.vhc.util;

import org.apache.log4j.Logger;

import com.vhc.model.AlarmType;
import com.vhc.model.CellN;
import com.vhc.model.CellN3G;
import com.vhc.model.CellN4G;
import com.vhc.model.HCoreN;
import com.vhc.model.HProvinceCode;
import com.vhc.model.StructAlarm;

/**
 * <p>
 * Class: AlarmInfo.java 
 * </p>
 * <p>
 * Description: 
 * </p> 
 * <p>
 * Create Date: 2019-01-28 15:38
 * </p> 
 * @author Galaxy - VHC Dev Team
 */
public class AlarmInfo {
	final static Logger logger = Logger.getLogger(AlarmInfo.class);
	
	private static final String alarmRNC 	= "PLMN-PLMN/RNC";
	private static final String alarmBSC 	= "PLMN-PLMN/BSC"; 
	private static final String alarmRAN4G  = "PLMN-PLMN/MRBTS"; 
	private static final String alarmGGSN 	= "PLMN-PLMN/GPBB";
	private static final String alarmGGSN2 	= "PLMN-PLMN/FING";
	private static final String alarmSGSN 	= "PLMN-PLMN/SGSN";
	private static final String alarmSGSN2 	= "PLMN-PLMN/FLEXINS";
	private static final String alarmSGSN3 	= "PLMN-PLMN/SAM"; 
	
	//Phan loai tram
	public static StructAlarm classifyAlarm(StructAlarm structAlarm) {
		String objRef = structAlarm.nbiObjectInstance;
		
		//set network theo ip
		if(structAlarm.ipAddress.equals("10.51.130.13")) {
			structAlarm.network = "PS_CORE"; 
		}else {
			//set network theo nbiObjectInstance
			if(objRef.contains(alarmBSC)){
				structAlarm.network = "2G";  
			}else if(objRef.contains(alarmRNC)){
				structAlarm.network = "3G"; 
			}else if(objRef.contains(alarmRAN4G)){
				structAlarm.network = "RAN_4G";
			}
			
			if(structAlarm.network == null) {
				if(structAlarm.ipAddress.equals("10.19.236.13")
					||structAlarm.ipAddress.equals("10.19.237.13")
					||structAlarm.ipAddress.equals("10.19.239.13")
					||structAlarm.ipAddress.equals("10.30.111.13")
					||structAlarm.ipAddress.equals("10.30.113.13")
					||structAlarm.ipAddress.equals("10.52.100.13")
					) {
					structAlarm.network = "2G";
				}else if(structAlarm.ipAddress.equals("10.56.0.13")) {
					structAlarm.network = "RAN_4G";
				}else {
					structAlarm.network = "RAN_4G";
				}
			}
		}
		
		//set record type and format date
		if(structAlarm.nbiEventTime != null && !structAlarm.nbiEventTime.equals("")){ 
			structAlarm.nbiEventTime = structAlarm.nbiEventTime.substring(0,structAlarm.nbiEventTime.indexOf("."));
		}
		
		if(structAlarm.nbiAlarmTime != null && !structAlarm.nbiAlarmTime.equals("")){
			structAlarm.recordType = "START";
			structAlarm.nbiAlarmTime = structAlarm.nbiAlarmTime.substring(0,structAlarm.nbiAlarmTime.indexOf("."));
		}else if(structAlarm.nbiClearTime != null && !structAlarm.nbiClearTime.equals("")){
			structAlarm.recordType = "END";
			structAlarm.nbiClearTime = structAlarm.nbiClearTime.substring(0,structAlarm.nbiClearTime.indexOf("."));
		}else if(structAlarm.nbiAckTime != null && !structAlarm.nbiAckTime.equals("")){
			structAlarm.recordType = "ACK";
			structAlarm.nbiAckTime = structAlarm.nbiAckTime.substring(0,structAlarm.nbiAckTime.indexOf("."));
		} 
		
		//set security
		structAlarm.nbiPerceivedSeverity = AlarmInfo.getSecurity(structAlarm.nbiPerceivedSeverity);
		
		return structAlarm;
	}
	
	// Set thong tin tram
	public static StructAlarm setAlarmInfo(StructAlarm structAlarm){ 
		//set ne, site, cell
		AlarmInfo.getBSCId(structAlarm); 
		 
		//mapping loai canh bao
		if(structAlarm.ipAddress.equals("10.53.141.13") && structAlarm.nbiSpecificProblem.contains("NE3SWS AGENT NOT RESPONDING TO REQUESTS")) {
			structAlarm.isMll = "Y";
			structAlarm.nbiAlarmType = "TRANSMISSION";
		}else {
			AlarmType alarmConfig = AlarmInfo.getAlarmConfig(structAlarm.network, structAlarm.nbiAdditionalText , structAlarm.nbiSpecificProblem); 
			if (alarmConfig != null) { 
				structAlarm.alarmMappingName = alarmConfig.getAlarmMappingName();
				structAlarm.alarmMappingId = alarmConfig.getAlarmMappingId();
				structAlarm.isMonitor = alarmConfig.getIsMonitor();
				structAlarm.isSendSms = alarmConfig.getIsSendSms();
				structAlarm.isMll = alarmConfig.getIsMll();
				structAlarm.nbiAlarmType = alarmConfig.getAlarmType();
			}
		} 
		
		try {			
			//Thay doi ten tram neu la canh bao Mat Dien CRAN
			if(structAlarm.nbiAlarmType.equals("POWER")
					//&& (structAlarm.neType.equals("NODEB") || structAlarm.neType.equals("ENODEB"))
				) {
				// Site name theo alarm info
				if(structAlarm.nbiAdditionalText.contains("<") && structAlarm.nbiAdditionalText.contains(">")) {
					structAlarm.site = structAlarm.nbiAdditionalText.substring(structAlarm.nbiAdditionalText.lastIndexOf("<") + 1, structAlarm.nbiAdditionalText.lastIndexOf(">"));
				}
				// Site name theo alarm name
				if(structAlarm.nbiSpecificProblem.contains("<") && structAlarm.nbiSpecificProblem.contains(">")) {
					structAlarm.site = structAlarm.nbiSpecificProblem.substring(structAlarm.nbiSpecificProblem.lastIndexOf("<") + 1, structAlarm.nbiSpecificProblem.lastIndexOf(">"));
				}
			}
			
			//Thay doi ten tram neu canh bao MFD
			if(structAlarm.nbiSpecificProblem.toUpperCase().contains("GENERATOR") || structAlarm.nbiAdditionalText.toUpperCase().contains("GENERATOR")) {
				//if(structAlarm.neType.equals("NODEB") || structAlarm.neType.equals("ENODEB")) {
					// Site name theo alarm info
					if(structAlarm.nbiAdditionalText.contains("<") && structAlarm.nbiAdditionalText.contains(">")) {
						structAlarm.site = structAlarm.nbiAdditionalText.substring(structAlarm.nbiAdditionalText.lastIndexOf("<") + 1, structAlarm.nbiAdditionalText.lastIndexOf(">"));
					}
					// Site name theo alarm name
					if(structAlarm.nbiSpecificProblem.contains("<") && structAlarm.nbiSpecificProblem.contains(">")) {
						structAlarm.site = structAlarm.nbiSpecificProblem.substring(structAlarm.nbiSpecificProblem.lastIndexOf("<") + 1, structAlarm.nbiSpecificProblem.lastIndexOf(">"));
					}
				//}
			}
			
		} catch (Exception e) {} 
		
		//set NE = SITE
		if(structAlarm.network.equals("RAN_4G")) {
			if(structAlarm.site != null && !structAlarm.site.equals("")) {
				structAlarm.ne = structAlarm.site;
			}
		} 
		
		//set thong tin ne type
		structAlarm.neType = AlarmInfo.getNeType(structAlarm.ne, structAlarm.site, structAlarm.cellid, structAlarm.network); 
		
		// mapping thong tin quan huyen 
		getProvince(structAlarm);
		
		//set region default
		if(structAlarm.region == null) {
			structAlarm.region = "UNKNOWN";
		}   
		
		return structAlarm;
	}
	
	/**
	 * Get bscid, site, cellid
	 * 
	 * @param structAlarm
	 */
	private static void getBSCId(StructAlarm structAlarm) {

		String[] arrAll = structAlarm.nbiObjectInstance.split("/");

		// 2G
		if (structAlarm.network.equals("2G")) {

			// BTS - Alarm CELL
			if (structAlarm.nbiObjectInstance.contains("BTS")) { 
				CellN cellN = AlarmInfo.getCellN(1, arrAll[0] + "/" + arrAll[1] + "/" + arrAll[2] + "/" + arrAll[3]);

				if (cellN != null) {
					structAlarm.ne = cellN.getBscName();
					structAlarm.site = cellN.getBtsName();
					structAlarm.cellid = cellN.getSegmentName();
				}else {
					structAlarm.ne = arrAll[1];
					structAlarm.site = arrAll[2];
					structAlarm.cellid = arrAll[3];
				}
			}
			// BCF - Alarm SITE
			else if (structAlarm.nbiObjectInstance.contains("BCF")) { 
				CellN cellN = AlarmInfo.getCellN(2, arrAll[0] + "/" + arrAll[1] + "/" + arrAll[2]);
				if (cellN != null) {
					structAlarm.ne = cellN.getBscName();
					structAlarm.site = cellN.getBtsName();
				}else {
					structAlarm.ne = arrAll[1];
					structAlarm.site = arrAll[2];
				}
			}
			// RNC - ALARM MUC BSC
			else { 
				CellN cellN = DbUtil.BSCID.get(arrAll[0] + "/" + arrAll[1]);
				if (cellN != null) {
					structAlarm.ne = cellN.getBscName();
				}else {
					structAlarm.ne = arrAll[1];
				}
			}
		}

		// 3G
		else if (structAlarm.network.equals("3G")) {

			// PLMN-PLMN/RNC-61/WBTS-377/WCEL-1
			if (structAlarm.nbiObjectInstance.contains("WCEL")) {
				CellN3G cellN3G = AlarmInfo.getCellN3G(1, arrAll[0] + "/" + arrAll[1] + "/" + arrAll[2] + "/" + arrAll[3]);

				if (cellN3G != null) {
					structAlarm.ne = cellN3G.getRncname();
					structAlarm.site = cellN3G.getWbtsname();
					structAlarm.cellid = cellN3G.getCellname();
				}else {
					structAlarm.ne = arrAll[1];
					structAlarm.site = arrAll[2];
					structAlarm.cellid = arrAll[3];
				}
			}

			else if (structAlarm.nbiObjectInstance.contains("WBTS")) { 
				CellN3G cellN3G = AlarmInfo.getCellN3G(2, arrAll[0] + "/" + arrAll[1] + "/" + arrAll[2]);

				if (cellN3G != null) {
					structAlarm.ne = cellN3G.getRncname();
					structAlarm.site = cellN3G.getWbtsname();
				}else {
					structAlarm.ne = arrAll[1];
					structAlarm.site = arrAll[2];
				}
			}

			else { 
				CellN3G cellN3G = DbUtil.RNCID.get(arrAll[0] + "/" + arrAll[1]);

				if (cellN3G != null) {
					structAlarm.ne = cellN3G.getRncname();
				}else {
					structAlarm.ne = arrAll[1];
				}
			}
		}
		
		// 4G
		else if(structAlarm.network.equals("RAN_4G")) {
			
			// PLMN-PLMN/MRBTS-574102/LNBTS-574102/LNCEL-13
			if(structAlarm.nbiObjectInstance.contains("LNCEL")) {
				CellN4G cellN4G = AlarmInfo.getCellN4G(arrAll[1].substring(arrAll[1].indexOf("-")+1, arrAll[1].length()), arrAll[3].substring(arrAll[3].indexOf("-")+1, arrAll[3].length()));
				
				if(cellN4G != null) {
					structAlarm.ne = cellN4G.getNodeName();
					structAlarm.site = cellN4G.getNodeName();
					structAlarm.cellid = cellN4G.getCellName();
				}else {
					structAlarm.ne = arrAll[1];
					structAlarm.site = arrAll[1];
					structAlarm.cellid = arrAll[3];
				}
			}else if(structAlarm.nbiObjectInstance.contains("LNBTS")) {
				CellN4G cellN4G = DbUtil.NODE4GID.get(arrAll[1].substring(arrAll[1].indexOf("-")+1, arrAll[1].length()));
				if (cellN4G != null) {
					structAlarm.ne = cellN4G.getNodeName();
					structAlarm.site = cellN4G.getNodeName();
				}else {
					structAlarm.ne = arrAll[1];
					structAlarm.site = arrAll[1];
				}
			}else {
				CellN4G cellN4G = DbUtil.NODE4GID.get(arrAll[1].substring(arrAll[1].indexOf("-")+1, arrAll[1].length()));
				if (cellN4G != null) {
					structAlarm.ne = cellN4G.getNodeName(); 
				}else {
					structAlarm.ne = arrAll[1]; 
				}
			}
		}
		
		// CORE PS
		else {
			if(structAlarm.nbiObjectInstance.contains(alarmSGSN)
					|| structAlarm.nbiObjectInstance.contains(alarmSGSN2)
						||structAlarm.nbiObjectInstance.contains(alarmGGSN2)) { 
				
				HCoreN hCoreN = getCoreN(arrAll[1].substring(arrAll[1].indexOf("-")+1, arrAll[1].length()));
				
				if(hCoreN != null) {
					structAlarm.ne = hCoreN.getNe();
					structAlarm.neType = hCoreN.getNeType();
					structAlarm.region = hCoreN.getRegion();
					structAlarm.dept = hCoreN.getDept();
					structAlarm.team = hCoreN.getTeam();
				}else {
					structAlarm.ne = arrAll[1]; 
				}
				 
			}else if (structAlarm.nbiObjectInstance.contains(alarmGGSN)) {
				for(String item: arrAll) {
					if(item.contains("GGSN-")) {
						
						HCoreN hCoreN = getCoreN(item.substring(item.indexOf("-")+1, item.length()));
						
						if(hCoreN != null) {
							structAlarm.ne = hCoreN.getNe();
							structAlarm.neType = hCoreN.getNeType();
							structAlarm.region = hCoreN.getRegion();
							structAlarm.dept = hCoreN.getDept();
							structAlarm.team = hCoreN.getTeam();
						}else {
							structAlarm.ne = item; 
						}
					}
				}
			}else if(structAlarm.nbiObjectInstance.contains(alarmSGSN3)){
				HCoreN hCoreN = getCoreN(arrAll[2].substring(arrAll[2].indexOf("@")+1, arrAll[2].length()));
				
				if(hCoreN != null) {
					structAlarm.ne = hCoreN.getNe();
					structAlarm.neType = hCoreN.getNeType();
					structAlarm.region = hCoreN.getRegion();
					structAlarm.dept = hCoreN.getDept();
					structAlarm.team = hCoreN.getTeam();
				}else {
					structAlarm.ne = arrAll[1]; 
				}
			}else {
				structAlarm.ne = arrAll[1]; 
			} 
		}
	}
	
	/**
	 * Get thong tin core
	 * */
	public static HCoreN getCoreN(String nodeId) {
		for(HCoreN item : DbUtil.CORE_LIST) {
			if(item.getNodeId().equals(nodeId)) {
				return item;
			}
		}
		
		return null;
	}
	
	/**
	 * Get cell by object name
	 * @param objectName
	 * @return
	 */
	public static CellN getCellN(int type, String name) {
		if (type == 1) {
			for (CellN item: DbUtil.BTS) {
				if (item.getObjectName().equals(name))
					return item;
			}
		}
		else {
			for (CellN item: DbUtil.BTS) {
				if (item.getBtsObjectname().equals(name))
					return item;
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public static CellN3G getCellN3G(int type, String name) {
		
		// objectname
		if (type == 1) {
			for (CellN3G item: DbUtil.BTS_3G) {
				if (item.getObjectname().equals(name)) {
					return item;
				}
			}
		} 
		// bts_objectname
		else {
			for (CellN3G item: DbUtil.BTS_3G) {
				if (item.getBtsObjectname().equals(name)) {
					return item;
				}
			}
		}
		
		return null;
	} 
	
	/**
	 * 
	 * @param type
	 * @param name
	 * @return
	 */
	public static CellN4G getCellN4G(String nodeId, String cellId) {
		for (CellN4G item: DbUtil.CELL_4G) {
			if (item.getNodeId().equals(nodeId)
					&& item.getCellId().equals(cellId)) {
				return item;
			}
		}
		
		return null;
	} 
	
	/**
	 * 
	 * @param type
	 * @param name
	 * @return
	 * su dung cho 2G,3G
	 */
	public static void getProvince(StructAlarm structAlarm) { 
		HProvinceCode hProvinceCode = null;
		// 4G
		if(structAlarm.network.equals("RAN_4G")) {
			for (HProvinceCode item: DbUtil.PROVINCE_BY_CODE) {
				if (structAlarm.ne.startsWith(item.getCode())) {
					structAlarm.region = item.getRegion();
					structAlarm.province = item.getProvince();
					structAlarm.district = item.getDistrict();
					structAlarm.dept = item.getDept();
					structAlarm.team = item.getTeam();
				}
			}
		}
		// 2G, 3G
		else if(structAlarm.network.equals("2G") || structAlarm.network.equals("3G")) {
			// site
			if(structAlarm.site != null) {
				for (HProvinceCode item: DbUtil.PROVINCE_BY_CODE) {
					if (structAlarm.site.startsWith(item.getCode())) {
						structAlarm.region = item.getRegion();
						structAlarm.province = item.getProvince();
						structAlarm.district = item.getDistrict();
						structAlarm.dept = item.getDept();
						structAlarm.team = item.getTeam();
						
						break;
					}
				}
			}
			//ne
			else {
				if(structAlarm.ne != null) {
					hProvinceCode = DbUtil.PROVINCE_BY_NE.get(structAlarm.ne);
					if(hProvinceCode != null) {
						structAlarm.region = hProvinceCode.getRegion();
						structAlarm.province = hProvinceCode.getProvince();
						structAlarm.district = hProvinceCode.getDistrict();
						structAlarm.dept = hProvinceCode.getDept();
						structAlarm.team = hProvinceCode.getTeam();
					}
				}
			}  
		} 
	}  
	
	//get security
	public static String getSecurity(String code) {
		if (code.equals("1")) return "A1";
		else if(code.equals("2")) return "A2";
		else if(code.equals("3")) return "A3";
		else return "A4";
	} 
	
	// Get Ne Type
	public static String getNeType(String ne, String site, String cell, String network) {
		String neType = "";
		
		try {
			if(network.equals("2G")) {
				if(cell != null && !cell.equals("")) neType = "CELL";
				else {
					if(site != null && !site.equals("")) neType = "BTS";
					else neType = "BSC";
				}
			}else if(network.equals("3G")){
				if(cell != null && !cell.equals("")) neType = "CELLB";
				else {
					if(site != null && !site.equals("")) neType = "NODEB";
					else neType = "RNC";
				}
			}else if(network.equals("RAN_4G")){
				if(cell != null && !cell.equals("")) neType = "EUTRANCELL";
				else neType = "ENODEB"; 
			}
		} catch (Exception e) {
			// TODO: handle exception
		} 
		 
		return neType;
	}
	

	
	/**
	 * Get alarm config
	 * 
	 * @param network 2G/3G/RAN_4G
	 * @param alarmInfoValue
	 * @param alarmNameValue
	 * @return
	 */
	public static AlarmType getAlarmConfig(String network, String alarmInfoValue, String alarmNameValue) { 
		System.out.println("==> GetAlarmType--"+network+"--"+alarmInfoValue+"--"+alarmNameValue);
		try {
			for (AlarmType item: DbUtil.ALARM_CONFIG) {
				if(item.getSearch().equals("SINGLE")
						&& (item.getBlockValue() != null || item.getAlarmInfoValue() != null)) 
				{
					if(item.getBlockValue() != null 
							&& item.getNode().equals(network)
							&& alarmNameValue.toUpperCase().contains(item.getBlockValue().toUpperCase())) {
						return item;
					}
					
					if(item.getAlarmInfoValue() != null 
							&& item.getNode().equals(network)
							&& alarmInfoValue.toUpperCase().contains(item.getAlarmInfoValue().toUpperCase())) {
						return item;
					}
				}
				
				if(item.getSearch().equals("MULTI") && item.getBlockValue() != null && item.getAlarmInfoValue() != null) {
					if(item.getNode().equals(network)
							&& alarmNameValue.toUpperCase().contains(item.getBlockValue().toUpperCase())
							&& alarmInfoValue.toUpperCase().contains(item.getAlarmInfoValue().toUpperCase())) {
						return item;
					}
				} 
			}
		} catch (Exception e) {
			logger.error("Exception getAlarmConfig() : ", e);
			return null;
		}
		
		return null;
	} 
}
