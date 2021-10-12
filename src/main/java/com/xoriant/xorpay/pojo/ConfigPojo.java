package com.xoriant.xorpay.pojo;

import java.util.Map;

import com.xoriant.xorpay.entity.SourceSysEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigPojo {

	private Integer SOURCE_SYS_ID;
	private String CONFIG_ENV;
	private String SAMPLE_PAN_IN;
	private String PAN_IN;
	private String PAN_ARCHIVE;
	private String PAN_OUT;
	private String BASE64;
	private String PGP;
	private String SOURCE_PROFILE_COLUMN_NAME;
	private String DATABASE_SYSTEM;
	private String SOURCE_TABLE_IDENTITY_COLUMN_NAME;
	private String SOURCE_TABLE_NAME;
	private String SOURCE_MESSAGE_ID;
	private String SOURCE_PAYMENT_ID;

	public Map<Integer, String> dbDataTypeIdMap;
	public Map<String, Integer> dbDataTypeStrMap;
	private SourceSysEntity srcSys;

	@Override
	public String toString() {
		return "ConfigPojo [SOURCE_SYS_ID=" + SOURCE_SYS_ID + ", CONFIG_ENV=" + CONFIG_ENV + ", SAMPLE_PAN_IN=" + SAMPLE_PAN_IN
				+ ", PAN_IN=" + PAN_IN + ", PAN_ARCHIVE=" + PAN_ARCHIVE + ", PAN_OUT=" + PAN_OUT + ", BASE64="
				+ BASE64 + ", PGP=" + PGP + ", SOURCE_PROFILE_COLUMN_NAME=" + SOURCE_PROFILE_COLUMN_NAME
				+ ", DATABASE_SYSTEM=" + DATABASE_SYSTEM + ", SOURCE_TABLE_IDENTITY_COLUMN_NAME="
				+ SOURCE_TABLE_IDENTITY_COLUMN_NAME + ", SOURCE_TABLE_NAME=" + SOURCE_TABLE_NAME
				+ ", SOURCE_MESSAGE_ID=" + SOURCE_MESSAGE_ID + ", SOURCE_PAYMENT_ID=" + SOURCE_PAYMENT_ID
				+ ", dbDataTypeIdMap=" + dbDataTypeIdMap + ", dbDataTypeStrMap=" + dbDataTypeStrMap + ", srcSys="
				+ srcSys + "]";
	}

}
