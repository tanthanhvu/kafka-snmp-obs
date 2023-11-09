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
 * Nokia Cell 3G
 * 
 * @author datnh
 *
 */
public class CellN3G {

	private String btsObjectname;
	private String objectname;
	private String rncid;
	private String rncname;
	private String wbtsname;
	private String cellname;
	
	public CellN3G(String btsObjectname, String objectname, String rncid, String rncname, String wbtsname, String cellname) {
		this.btsObjectname = btsObjectname;
		this.objectname = objectname;
		this.rncid = rncid;
		this.rncname = rncname;
		this.wbtsname = wbtsname;
		this.cellname = cellname;
	}
	
	public String getBtsObjectname() {
		return btsObjectname;
	}
	public void setBtsObjectname(String btsObjectname) {
		this.btsObjectname = btsObjectname;
	}
	public String getObjectname() {
		return objectname;
	}
	public void setObjectname(String objectname) {
		this.objectname = objectname;
	}
	public String getRncid() {
		return rncid;
	}
	public void setRncid(String rncid) {
		this.rncid = rncid;
	}
	public String getRncname() {
		return rncname;
	}
	public void setRncname(String rncname) {
		this.rncname = rncname;
	}
	public String getWbtsname() {
		return wbtsname;
	}
	public void setWbtsname(String wbtsname) {
		this.wbtsname = wbtsname;
	}
	public String getCellname() {
		return cellname;
	}
	public void setCellname(String cellname) {
		this.cellname = cellname;
	}
	
}
