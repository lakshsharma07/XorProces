package com.xoriant.xorpay.model;

import java.util.List;

public class UserTabCols {
	private String tableName;
	private List<ColumnDataType> columnDataType = null;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<ColumnDataType> getColumnDataType() {
		return columnDataType;
	}

	public void setColumnDataType(List<ColumnDataType> columnDataType) {
		this.columnDataType = columnDataType;
	}

}
