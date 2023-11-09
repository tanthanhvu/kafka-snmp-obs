// ----------------------------------------------------------------------------
// Copyright 2013, VHCSoft JSC.
// All rights reserved
// ----------------------------------------------------------------------------
// Change History:
//  2013.12.11  datnh
//     - Initial release
// ----------------------------------------------------------------------------

package com.vhc.model;

/**
 * Alarm config type
 * 
 * @author datnh
 *
 */
public class AlarmType {

	private String node;
	private String blockValue;
	private String alarmInfoValue;
	private String alarmType;
	private String alarmMappingName;
	private String alarmMappingId;
	private String isMonitor;
	private String isSendSms;
	private String isMll; 
	private String search;
	
	public AlarmType(String node, String blockValue, String alarmInfoValue, String alarmType, String alarmMappingName,
			String alarmMappingId, String isMonitor, String isSendSms, String isMll, String search) {
		super();
		this.node = node;
		this.blockValue = blockValue;
		this.alarmInfoValue = alarmInfoValue;
		this.alarmType = alarmType;
		this.alarmMappingName = alarmMappingName;
		this.alarmMappingId = alarmMappingId;
		this.isMonitor = isMonitor;
		this.isSendSms = isSendSms;
		this.isMll = isMll;
		this.search = search;
	}
	
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public String getBlockValue() {
		return blockValue;
	}
	public void setBlockValue(String blockValue) {
		this.blockValue = blockValue;
	}
	public String getAlarmInfoValue() {
		return alarmInfoValue;
	}
	public void setAlarmInfoValue(String alarmInfoValue) {
		this.alarmInfoValue = alarmInfoValue;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public String getAlarmMappingName() {
		return alarmMappingName;
	}
	public void setAlarmMappingName(String alarmMappingName) {
		this.alarmMappingName = alarmMappingName;
	}
	public String getAlarmMappingId() {
		return alarmMappingId;
	}
	public void setAlarmMappingId(String alarmMappingId) {
		this.alarmMappingId = alarmMappingId;
	}
	public String getIsMonitor() {
		return isMonitor;
	}
	public void setIsMonitor(String isMonitor) {
		this.isMonitor = isMonitor;
	}
	public String getIsSendSms() {
		return isSendSms;
	}
	public void setIsSendSms(String isSendSms) {
		this.isSendSms = isSendSms;
	}
	public String getIsMll() {
		return isMll;
	}
	public void setIsMll(String isMll) {
		this.isMll = isMll;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	} 
}
