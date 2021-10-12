package com.xoriant.xorpay.model;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


public class XpXmlTagsXpathMapper implements RowMapper<XpXmlTagsPath>{

	@Override
    public XpXmlTagsPath mapRow(ResultSet rs, int rowNum) throws SQLException {

		XpXmlTagsPath table = new XpXmlTagsPath();
		table.setTagSeqId(rs.getInt("TAG_SEQID"));
		table.setTagName(rs.getString("TAG_NAME"));
		table.setTagInfo(rs.getString("TAG_INFO"));
		table.setxPath(rs.getString("XPATH"));
		table.setMaxLength(rs.getInt("MAX_LENGTH"));
		table.setCreatedDate(rs.getDate("CREATED_DATE"));
		table.setCreatedBy(rs.getString("CREATED_BY"));
		table.setUpdatedDate(rs.getDate("UPDATED_DATE"));
		table.setUpdatedBy(rs.getString("UPDATED_BY"));
		table.setTagDataType(rs.getString("TAG_DATA_TYPE"));
		
		return table;
	}

}
