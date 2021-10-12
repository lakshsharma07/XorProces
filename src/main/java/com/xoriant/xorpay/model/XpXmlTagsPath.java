package com.xoriant.xorpay.model;

import java.util.Date;

public class XpXmlTagsPath {
	private int tagSeqId;
	private  String tagName;
	private  String tagInfo;
	private  String xPath;
	private  int maxLength;
	private  Date createdDate;
	private  String createdBy;
	private  Date updatedDate;
	private  String updatedBy;
	private String tagDataType;
	
	public int getTagSeqId() {
		return tagSeqId;
	}
	public void setTagSeqId(int tagSeqId) {
		this.tagSeqId = tagSeqId;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagInfo() {
		return tagInfo;
	}
	public void setTagInfo(String tagInfo) {
		this.tagInfo = tagInfo;
	}
	public String getxPath() {
		return xPath;
	}
	public void setxPath(String xPath) {
		this.xPath = xPath;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getTagDataType() {
		return tagDataType;
	}
	public void setTagDataType(String tagDataType) {
		this.tagDataType = tagDataType;
	}

}
