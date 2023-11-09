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

public class AppSetting {
	//Config connect snmp
	public static final String SNMP_SECURITY_NAME= "securityName";
	public static final String SNMP_PORT = "snmpPort";
	public static final String SNMP_AUTHENTICATION_PROTOCOL = "authenticationProtocol";
	public static final String SNMP_AUTHENTICATION_PASSPHRASE = "authenticationPassphrase";
	public static final String SNMP_PRIVACY_PROTOCOL = "privacyProtocol";
	public static final String SNMP_PRIVACY_PASSPHRASE = "privacyPassphrase";
	
	//Config OID Alarm
	public static final String NBI_EVENT_TIME= "nbiEventTime";
	public static final String NBI_OBJECT_INSTANCE = "nbiObjectInstance";
	public static final String NBI_ACK_STATE = "nbiAckState";
	public static final String NBI_PERCEIVED_SEVERITY = "nbiPerceivedSeverity";
	public static final String NBI_PROBABLE_CAUSE = "nbiProbableCause";
	public static final String NBI_SPECIFIC_PROBLEM = "nbiSpecificProblem";
	public static final String NBI_ACK_TIME = "nbiAckTime";
	public static final String NBI_ACK_USER = "nbiAckUser";
	public static final String NBI_ADDITIONAL_TEXT = "nbiAdditionalText";
	public static final String NBI_ALARM_ID = "nbiAlarmId";
	public static final String NBI_ALARM_TIME = "nbiAlarmTime";
	public static final String NBI_ALARM_TYPE = "nbiAlarmType";
	public static final String NBI_CLEAR_TIME = "nbiClearTime";
	public static final String NBI_CLEAR_USER = "nbiClearUser";
	
	//Config OID Cisco
	public static final String CEN_ALARM_TIMESTAMP ="cenAlarmTimestamp";
	public static final String CEN_ALARM_UPDATED_TIMESTAMP = "cenAlarmUpdatedTimestamp";
	public static final String CEN_ALARM_INSTANCE_ID = "cenAlarmInstanceID";
	public static final String CEN_ALARM_STATUS = "cenAlarmStatus";
	public static final String CEN_ALARM_STATUS_DEFINITION = "cenAlarmStatusDefinition";
	public static final String CEN_ALARM_CATEGORY_DEFINITION ="cenAlarmCategoryDefinition";
	public static final String CEN_ALARM_MANAGED_OBJECT_CLASS ="cenAlarmManagedObjectClass";
	public static final String CEN_ALARM_MANAGED_OBJECT_ADDRESS ="cenAlarmManagedObjectAddress";
	public static final String CEN_ALARM_DESCRIPTION ="cenAlarmDescription";
	public static final String CEN_ALARM_SEVERITY_DEFINITION ="cenAlarmSeverityDefinition";
	public static final String CEN_USER_MESSAGE1 ="cenUserMessage1";
	
	//Config OID 
	public static final String FTTX_NOTIFICATION_ID ="notificationID";
	public static final String FTTX_ALARM_CATEGORY_TYPE ="alarmCategoryType";
	public static final String FTTX_ALARM_SEVERITY ="alarmSeverity";
	public static final String FTTX_RAISED_TIMESTAMP ="raisedTimeStamp";
	public static final String FTTX_CLEARED_TIMESTAMP ="clearedTimeStamp";
	public static final String FTTX_ADDITIONAL_INFO ="additionalInfo";
	public static final String FTTX_ACKNOWLEDGED ="acknowledged";
	public static final String FTTX_PROBABLECAUSE ="probableCause";
	public static final String FTTX_NOTES ="notes";
	public static final String FTTX_SPECIFIC_PROBLEM ="specificProblem";
	public static final String FTTX_LAST_ACKNOWLEDGED_TIMESTAMP ="lastAcknowledgedTimeStamp";
	public static final String FTTX_LAST_ACKNOWLEDGED_USERID ="lastAcknowledgedUserId";
	public static final String FTTX_NE_ALARM_TYPE ="neAlarmType";
	public static final String FTTX_ALIAS_NAME ="aliasName";
	public static final String FTTX_NE_IP_ADDRESS ="neIpAddress";
	public static final String FTTX_REPAIR_ACTIONS ="repairActions";
	
	
	//Config list IP gui canh bao
	public static final String ALARM_SNMP_IP_LIST = "ipList";
	
	//Config list IP gui canh bao metro
	public static final String ALARM_SNMP_IP_METRO = "ipListMetro";
	
	//Config list IP gui canh bao FTTX
	public static final String ALARM_SNMP_IP_FTTX = "ipListFTTX";
}

