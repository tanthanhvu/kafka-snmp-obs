/**
 * <p>
 * Copyright: Copyright (c) by VHCSoft 2019
 * </p>
 * <p>
 * Company: VietNam High Technology Software JSC.
 */
package com.vhc.model;

/**
 * <p>
 * Class: CellN4G.java 
 * </p>
 * <p>
 * Description: 
 * </p> 
 * <p>
 * Create Date: 2019-01-26 21:32
 * </p> 
 * @author Galaxy - VHC Dev Team
 */
public class CellN4G { 
	private String nodeId;
	private String cellId;
	private String nodeName;
	private String cellName;
	
	public CellN4G(String nodeId, String cellId, String nodeName, String cellName) {
		super();
		this.nodeId = nodeId;
		this.cellId = cellId;
		this.nodeName = nodeName;
		this.cellName = cellName;
	}

	/**
	 * @return the nodeId
	 */
	public String getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId the nodeId to set
	 */
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * @return the cellId
	 */
	public String getCellId() {
		return cellId;
	}

	/**
	 * @param cellId the cellId to set
	 */
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	/**
	 * @return the nodeName
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * @param nodeName the nodeName to set
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * @return the cellName
	 */
	public String getCellName() {
		return cellName;
	}

	/**
	 * @param cellName the cellName to set
	 */
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
}
