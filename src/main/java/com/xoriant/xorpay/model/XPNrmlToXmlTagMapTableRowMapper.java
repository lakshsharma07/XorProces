package com.xoriant.xorpay.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

public class XPNrmlToXmlTagMapTableRowMapper implements RowMapper<List<ColumnDataType>>{

	@Override
    public List<ColumnDataType> mapRow(ResultSet rs, int rowNum) throws SQLException {

		//UserTabCols userTabCol = new UserTabCols();
		//userTabCol.setTableName("dummy");
		
		List<ColumnDataType> columnDataTypeList = new ArrayList<>();
        while(rs.next()) {
        	ColumnDataType colDataType = new ColumnDataType();
        	colDataType.setColumnName(rs.getString("NRMLZ_COL_NAME"));
        	colDataType.setDataType(rs.getString("NRMLZ_COL_DATA_TYPE"));
        	colDataType.setInitialVal(rs.getString("XPATH"));
        	
        	columnDataTypeList.add(colDataType);
        	
        }
       // userTabCol.setColumnDataType(columnDataTypeList);
        return columnDataTypeList;

    }
}

