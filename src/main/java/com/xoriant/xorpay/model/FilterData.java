package com.xoriant.xorpay.model;

public class FilterData {
	private String[] ouName;
	private String respId;
	private String 	userName;
	private String userId;
	private String orgId;
	
	public FilterData() {
		//default constructor
	}
	public FilterData(String[] ouName, String respId, String userName, String userId,String orgId) {
		super();
		this.ouName = ouName;
		this.respId = respId;
		this.userName = userName;
		this.userId = userId;
		this.orgId=orgId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String[] getOuName() {
		return ouName;
	}
	public void setOuName(String[] ouName2) {
		this.ouName = ouName2;
	}
	public String getRespId() {
		return respId;
	}
	public void setRespId(String respId) {
		this.respId = respId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
