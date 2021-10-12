package com.xoriant.xorpay.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

public class TableRowMapper implements RowMapper<UserTabCols>{

	@Override
    public UserTabCols mapRow(ResultSet rs, int rowNum) throws SQLException {

		UserTabCols userTabCol = new UserTabCols();
		userTabCol.setTableName(rs.getString("table_name"));
		
		List<ColumnDataType> columnDataTypeList = new ArrayList<>();
        while(rs.next()) {
        	ColumnDataType colDataType = new ColumnDataType();
        	colDataType.setColumnName(rs.getString("column_name"));
        	colDataType.setDataType(rs.getString("data_type"));
        	colDataType.setInitialVal(null);
        	colDataType.setxPath(null);
        	
        	columnDataTypeList.add(colDataType);
        	
        }
        userTabCol.setColumnDataType(columnDataTypeList);
        return userTabCol;

    }
}
