package com.xoriant.xorpay.data.sync.repo;

import java.util.HashSet;
import java.util.Set;

public class CommonJPAConstant {

	public static final String NRMLZ = "NRMLZ";
	public static final String MYSQL_DB_DATA_TYPE = "mysql_db_data_type";
	public static final String POSTGRES_DB_DATA_TYPE = "postgres_db_data_type";
	public static final String COL33_ENDTOENDID = "ENDTOENDID_33";
	public static final String COL5_CREDTTM = "CREDTTM_5";
	public static final String COL41_NM = "NM_41";
	public static final String COL6_NBOFTXS = "NBOFTXS_6";
	public static final String COL4_MSGID = "MSGID_4";
	public static final String CCY_CD = "CCY_CD";
	public static final String COL20_REQDEXCTNDT = "REQDEXCTNDT_20";
	public static final String COL60_RLTDDT = "RLTDDT_60";
	public static final String COL35_INSTDAMT = "INSTDAMT_35";
	public static final String COL133_CTRLSUM = "CTRLSUM_133";
	public static final String COL62_DUEPYBLAM = "DUEPYBLAMT_62";
	public static final String COL59_NB = "NB_59";
	public static final String COL8_NM = "NM_8";
	public static final String COL11_PMTMTD = "PMTMTD_11";
	public static final String colInternalUuid = "INTERNAL_UUID";

	public static String[] colList = { COL4_MSGID, COL6_NBOFTXS, COL41_NM, COL5_CREDTTM, COL33_ENDTOENDID, CCY_CD,
			COL20_REQDEXCTNDT, COL60_RLTDDT, COL35_INSTDAMT, COL133_CTRLSUM, COL62_DUEPYBLAM, COL59_NB, COL8_NM,
			COL11_PMTMTD };

	//public static Set<String> messageIdSet = new HashSet<>();
	public static Set<String> endToEndIdSet = new HashSet<>();

	public static Set<String> loadmessageIdSet = new HashSet<>();
	public static Set<String> loadendToEndIdSet = new HashSet<>();
	public static Set<String> loadInvoiceSet = new HashSet<>();

	public static void clearObject() {
		//messageIdSet.clear();
		endToEndIdSet.clear();
		loadmessageIdSet.clear();
		loadendToEndIdSet.clear();
		loadInvoiceSet.clear();
	}
}
