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
 * Nokia Cell 2G
 * 
 * @author datnh
 *
 */
public class CellN {

	private String btsObjectname;
	private String objectName;
	private String bscId;
	private String bscName;
	private String btsName;
	private String segmentName;
	
	public CellN() {}
	
	public CellN(String btsObjectname, String objectName, String bscId, String bscName, String btsName, String segmentName) {
		this.btsObjectname = btsObjectname;
		this.objectName = objectName;
		this.bscId = bscId;
		this.bscName = bscName;
		this.btsName = btsName;
		this.segmentName = segmentName;
	}
	
	public String getBtsObjectname() {
		return btsObjectname;
	}
	public void setBtsObjectname(String btsObjectname) {
		this.btsObjectname = btsObjectname;
	}
	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
	public String getBscId() {
		return bscId;
	}
	public void setBscId(String bscId) {
		this.bscId = bscId;
	}

	public String getBscName() {
		return bscName;
	}
	public void setBscName(String bscName) {
		this.bscName = bscName;
	}
	public String getBtsName() {
		return btsName;
	}
	public void setBtsName(String btsName) {
		this.btsName = btsName;
	}
	public String getSegmentName() {
		return segmentName;
	}
	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
	}
	
}
