package com.xoriant.xorpay.model;

import java.io.Serializable;

public class UserTable implements Serializable {
	private static final long serialVersionUID = 10L;
	private String tableName;
	
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
