package com.xoriant.xorpay.constants;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class XorConstant {

	public static final String PROCESS_ID = "ProcessId";
	public final static String INITIATED = "Initiated";
	public final static String IN_PROGRESS = "In-Progress";
	// public final static String TRANSMITTED = "Transmitted";
	public final static String TRANSLATED = "Translated";
	public final static String FAILED = "Failed";
	public final static String ALL = "All";

	public static final String ONS = "ONS";
	public static final String MBC = "MBC";
	public static final String EBS = "EBS";
	public static final String NET_SUITE = "NetSuite";
	public static final String MS_DYNAMICS = "MS-Dynamics";
	public static final String ORACLE_EBS = "Oracle EBS";
	public static final Character STATUS_Y = 'Y';
	public static final Character STATUS_N = 'N';
	public static final Character STATUS_E = 'E';
	public static final Character STATUS_V = 'V';
	public static final Character STATUS_BATCH_PROCESSED = 'B';
	public static final Character STATUS_PAYMENT_PROCESSED = 'P';
	public static final Character STATUS_NORMALIZED = 'N';
	public static final Character STATUS_READY = 'R';
	public static final Character STATUS_MOVE = 'M';
	public static final String SCHEDULED = "SCHEDULED";
	public static final String DISABLED = "DISABLED";
	public static final String nrmlProcName = "LOAD_NRMLZ_TABLES";
	public static final String aggrProcName = "LOAD_AGG_TABLES";
	public static final String NORM_AND_AGG_PROC = "Payment Generation";
	public static final String SUCCESS = "Success";
	public static final Character DISABLE = 'D';
	public static final Character ENABLE = 'E';
	public static final Character TERMINATE = 'T';
	public static final String PAYMENTS = "Payments";
	public static final String SEC = "Sec";
	public static final String MIN = "Min";
	public static final String HOUR = "Hour";
	public static final String DAY = "Day";
	public static final String NOW = "Now";
	public static final String COMPLETED = "COMPLETED";
	public static final Object NO_DATA = "No Data";
	public static final String PENDING = "Pending";
	public static final String NEW = "New";
	public static final String ACKNOWLEDGED = "Acknowledged";
	public static final String ACCEPTED = "Accepted";
	public static final String REJECTED = "Rejected";
	public static final String ACCP = "ACCP";
	public static final String ACSP = "ACSP";
	public static final String ACTC = "ACTC";
	public static final String ACSC = "ACSC";
	public static final String PDNG = "PDNG";

	public static final String MOCK_BATCH = "MOCK_BATCH";
	public static final String MOCK_PAYMENT = "MOCK_PAYMENT";
	public static final String MOCK_ACCP = "MOCK_ACCP";
	public static final String MOCK_RJCT = "MOCK_RJCT";
	public static final String FAIL = "FAIL";
	public static final String TARGET = "Target";
	public static final String CONTAINER = "container";
	public static final String NRMLCOLDBREGEX = "~";
	public static final String UNDERSCORE = "_";
	public static final String COMMA = ",";
	public static final String BOTH_INFO = "BOTH_INFO";
	public static final String TAG_INFO_REM = "REMITTANCE_INFO";
	public static final String TAG_INFO_AGG = "AGG_INFO";
	public static final String ACTIVE = "active";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static final String XML_ENC = "XML_ENC";
	public static final String BASE64 = "BASE64";
	public static final String PGP = "PGP";
	public static final String ADMIN_ROLE = "ADMIN";
	public static final String SYSTEM = "SYSTEM";
	public static final String CONFIG = "CONFIG";
	public static final String RELOAD = "RELOAD";
	public static final String MOCK_TYPE = "MOCK_TYPE";
	public static final String MOCK = "MOCK";
	public static final String CTRL_SUM_CHK = "CTRL_SUM_CHK";
	public static final String SOURCE = "Source";
	public static final String SOURCE_TABLE = "Source_Table";
	public static final String BOTH = "BOTH";
	public static final String AGGREGATE = "AGGREGATE";
	public static final String REMITTANCE = "REMITTANCE";
	public static final String SOURCE_IDENTITY = "Source_Identity";
	public static final String SOURCE_MESSAGE_ID = "SOURCE_MESSAGE_ID";
	public static final String SOURCE_PAYMENT_ID = "SOURCE_PAYMENT_ID";

	public static final String SAMPLE_PAN_IN = "SAMPLE_PAN_IN";
	public static final String PAN_IN = "PAN_IN";
	public static final String PAN_ARCHIVE = "PAN_ARCHIVE";
	public static final String PAN_OUT = "PAN_OUT";
	public static final String ENVIRONEMNT = "ENV";
	public static final String DATABASE = "Database";
	public static final String STORAGE = "Storage";
	public static final String SOURCE_PROFILENAME = "Source_ProfileColumnName";
	public static final String AGG = "Agg";
	public static final String REM = "Rem";
	public static final String NRMLZ = "Nrmlz";
	public static final String AGG_PRTY_PYMT_DTLS = "agg_prty_pymt_dtls";
	public static final String AGG_REMITTANCE_INFO = "agg_remittance_info";
	public static final String T = "T";
	public static final String SINGLE_QUOTE_T = "'T'";
	public static final String YYYY_MM_DDTHH_MM_SS_SSS = "YYYY-MM-DDThh:mm:ss.sss";
	public static final String YYYY_MM_DDTHH_MM = "yyyy-MM-dd'T'HH:mm";

	public static final String RUNNING = "RUNNING";
	public static final String COMPLETE = "COMPLETE";
	public static final String ERROR = "ERROR";
	public static final String LOAD_NRMLZ_PMT_DTLS = "LOAD_NRMLZ_PMT_DTLS";
	public static final String NRMLZ_PMT_DTLS = "nrmlz_pmt_dtls";
	public static final String RULE_AGG_LOOKUP = "xp_agg_lookup";
	public static final String AUDIT = "Audit";
	public static final Character PROCESS_INITIATED = 'I';
	public static final Character STATUS_LOADED = 'L';
	public static final Character MOVE_TO_HISTORY = 'H';
	public static final String REGEX_ALPHANUMERIC = "[^a-zA-Z0-9]";
	public static final String _00_INVENDID = "-00";
	public static final String _000_INVMSGID = "-000";
	public static final String NRM_MESSAGE_COLNAME = "message_id";
	public static final String AGG_MESSAGE_COLNAME = "MSGID_4";
	public static final String NULL = "null";
	public static final Character X_AND = '&';
	public static final Character X_OR = '~';
	public static final String MOCK_CLS_ACSC = "MOCK_CLS_ACSC";
	public static final String CLS = "CLS";
	public static boolean isCLS;
	public static final String XERO = "XERO";
	public static boolean isXERO;

	public static DateTimeFormatter YYYYMMDDHH24MMSS = DateTimeFormatter.ofPattern("YYYYMMDDHH24MMSS", Locale.ENGLISH);
	public static DateTimeFormatter formateMMDDYYYY = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
	public static DateTimeFormatter formateddMMMYYYY = DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH);
	public static DateTimeFormatter formateYYYYMMDD = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

	public static DateTimeFormatter formateYYYYMMDD_HHMMSSZ = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S",
			Locale.ENGLISH);
	public static DateTimeFormatter formateYYYYMMDD_HHMMSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss",
			Locale.ENGLISH);
	public static DateTimeFormatter formateYYYYMMDDTHHMMSS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss",
			Locale.ENGLISH);
	// public static DateTimeFormatter formateYYYYMMDDT_HHMMSS =
	// DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss",
	// Locale.ENGLISH);
	public static DateTimeFormatter formateFull = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS",
			Locale.ENGLISH);
	public static DateTimeFormatter formateFullWithZ = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
			Locale.ENGLISH);
	public static boolean activeFlag;
	public static long INTERVAL;

	public static Map<String, Integer> sourceMap = new HashMap<>();
	static {
		sourceMap.put(EBS, 1);
		sourceMap.put(ONS, 2);
		sourceMap.put(MBC, 3);
	}

}
