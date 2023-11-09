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
 * Class: HProvinceCode.java 
 * </p>
 * <p>
 * Description: 
 * </p> 
 * <p>
 * Create Date: 2019-01-26 23:51
 * </p> 
 * @author Galaxy - VHC Dev Team
 */
public class HProvinceCode {
	private String code;
	private String region;
	private String province;
	private String district; 
	private String team;
	private String dept;
	
	public HProvinceCode(String code, String region, String province, String district, String dept, String team) {
		super();
		this.code = code;
		this.region = region;
		this.province = province;
		this.district = district;
		this.team = team;
		this.dept = dept;
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the region
	 */
	public String getRegion() {
		return region;
	}
	/**
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this.region = region;
	}
	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * @return the district
	 */
	public String getDistrict() {
		return district;
	}
	/**
	 * @param district the district to set
	 */
	public void setDistrict(String district) {
		this.district = district;
	}
	/**
	 * @return the team
	 */
	public String getTeam() {
		return team;
	}
	/**
	 * @param team the team to set
	 */
	public void setTeam(String team) {
		this.team = team;
	}
	/**
	 * @return the dept
	 */
	public String getDept() {
		return dept;
	}
	/**
	 * @param dept the dept to set
	 */
	public void setDept(String dept) {
		this.dept = dept;
	}

}
