package com.vhc.model;

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
 * Create Date:Sep 6, 2017
 * </p>
 * 
 * @author VHC - Software
 * @version 1.0
 */

public class HCoreN {
	private String region;
	private String neType;
	private String ne;
	private String nodeId;
	private String dept;
	private String team;
	private String vendor;

	public HCoreN(String region, String neType, String ne, String nodeId, String dept, String team, String vendor) {
		super();
		this.region = region;
		this.neType = neType;
		this.ne = ne;
		this.nodeId = nodeId;
		this.dept = dept;
		this.team = team;
		this.vendor = vendor;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getNeType() {
		return neType;
	}

	public void setNeType(String neType) {
		this.neType = neType;
	}

	public String getNe() {
		return ne;
	}

	public void setNe(String ne) {
		this.ne = ne;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
}

