package com.xoriant.xorpay.constants;

import java.io.File;

public class TagConstants {

	// public static final String INPUT_FILE =
	// Tomcat
	// public static final String INPUT_FILE =
	// "C:\\topoc\\xml2\\in\\SG421-Input.xml";
	// public static final String INPUT_FILE =
	// "D:\\XORPAY\\XORPAY_API\\topoc\\xml2\\in\\SG421-Input.xml";
	// for Weblogic Server configuration
	// u01/data/domains/citiconn_domain/files

	
	//public static String ENV = "LNX";
	// private static String ENV ="VM";
	// private static String ENV ="CLOUD";

	// public static String path = "D:\\XORPAY\\XORPAY_API\\XORCOMP\\";
	// public static final String path = "D:\\topoc\\xml2\\";
	// public static final String XOR_ENV="DEV";
	// public static final String XOR_ENV="VM";
	// public static final String path ="";
	// public static final String XOR_ENV="CLOUD";

	
	// for tomcat
	// public static final String OUTPUT_FILE =
	// "D:\\XORPAY\\XORPAY_API\\topoc\\xml2\\out\\";
	public static final String XSD_FILE = "C:\\topoc\\xorPay\\Latest\\dashboardWithXorapy\\dashboardWithXorapy\\files\\pain.001.001.03.xsd";

	public static final String PGP_PUB_KEY_FILE = "files" + File.separator + "pub.dat";
	public static final String PGP_PRIV_KEY_FILE = "files" + File.separator + "secret.dat";
	public static final String PGP_SIGN_FILE = "files" + File.separator + "signature.txt";

	public static final String CLIENT_ID = "Xoriant";
	public static final String SECRET_KEY = "xoriant123#";

	// Weblogic
	/*
	 * public static final String INPUT_FILE = "files/input/SG421-Input.xml"; public
	 * static final String OUTPUT_FILE = "files/output/PaylodOutFile.xml"; public
	 * static final String XSD_FILE = "files/pain.001.001.03.xsd";
	 */

	public static final String GRPHEADER = "GrpHdr";
	public static final String GRPHDR_NMBTR = "/Document/CstmrCdtTrfInitn/GrpHdr/NbOfTxs";
	public static final String GRPHDR_CRDTTIME = "/Document/CstmrCdtTrfInitn/GrpHdr/CreDtTm";

	public static final String PMT_INFO = "PmtInf";
	public static final String PMT_TP_INFO = "PmtTpInf";
	public static final String PMT_INFO_CHRGBR = "/Document/CstmrCdtTrfInitn/PmtInf/ChrgBr";
	public static final String PMT_INFO_BATCHBKNG = "/Document/CstmrCdtTrfInitn/PmtInf/BtchBookg";
	public static final String SRVC_LVL = "/Document/CstmrCdtTrfInitn/PmtInf/PmtTpInf/SvcLvl";
	public static final String SRVC_LVL_CD = "/Document/CstmrCdtTrfInitn/PmtInf/PmtTpInf/SvcLvl/Cd";
	public static final String SRVC_LVL_PRTRY = "/Document/CstmrCdtTrfInitn/PmtInf/PmtTpInf/SvcLvl/Prtry";
	public static final String LCL_INSTRM = "/Document/CstmrCdtTrfInitn/PmtInf/PmtTpInf/LclInstrm";
	public static final String LCL_INSTRM_CD = "/Document/CstmrCdtTrfInitn/PmtInf/PmtTpInf/LclInstrm/Cd";
	public static final String LCL_INSTRM_PRTRY = "/Document/CstmrCdtTrfInitn/PmtInf/PmtTpInf/LclInstrm/Prtry";
	public static final String CTGRY_PRP = "/Document/CstmrCdtTrfInitn/PmtInf/PmtTpInf/CtgyPurp";
	public static final String CTGRY_PRP_CD = "/Document/CstmrCdtTrfInitn/PmtInf/PmtTpInf/CtgyPurp/Cd";
	public static final String CTGRY_PRP_PRTRY = "/Document/CstmrCdtTrfInitn/PmtInf/PmtTpInf/CtgyPurp/Prtry";

	public static final int INT_MAX_LENGTH = 35;
	public static final String MAX_LENGTH = "35";
	public static final int INT_MAX_LENGTH_10 = 10;

	public static final String PIUID = "780";
	public static final String COUNTRY = "CA";

	public static final String DBTR = "Dbtr";
	public static final String DBTR_PSTLADR = "/Document/CstmrCdtTrfInitn/PmtInf/Dbtr/PstlAdr";
	public static final String DBTR_PSTLADR_ADRLINE = "/Document/CstmrCdtTrfInitn/PmtInf/Dbtr/PstlAdr/AdrLine";
	public static final String DBTR_PST_CODE = "/Document/CstmrCdtTrfInitn/PmtInf/Dbtr/PstlAdr/PstCd";
	public static final String DBTR_TOWN_NAME = "/Document/CstmrCdtTrfInitn/PmtInf/Dbtr/PstlAdr/TwnNm";
	public static final String DBTR_COUNTRY_SUBD = "/Document/CstmrCdtTrfInitn/PmtInf/Dbtr/PstlAdr/CtrySubDvsn";
	public static final String DBTR_COUNTRY = "/Document/CstmrCdtTrfInitn/PmtInf/Dbtr/PstlAdr/Ctry";

	public static final String DBTR_ID = "/Document/CstmrCdtTrfInitn/PmtInf/Dbtr/Id";
	public static final String DBTR_ORGID = "/Document/CstmrCdtTrfInitn/PmtInf/Dbtr/Id/OrgId";
	public static final String DBTR_PRVTID = "/Document/CstmrCdtTrfInitn/PmtInf/Dbtr/Id/PrvtId";
	public static final String DBTR_CTGRYRES = "/Document/CstmrCdtTrfInitn/PmtInf/Dbtr/CtryOfRes";
	public static final String DBTR_CNTCT_DTLS = "/Document/CstmrCdtTrfInitn/PmtInf/Dbtr/CtctDtls";

	public static final String DBTR_ACCT = "DbtrAcct";
	public static final String DBTR_ACCT_ID = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAcct/Id";
	public static final String DBTR_ACCT_ID_IBAN = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAcct/Id/IBAN";
	public static final String DBTR_ACCT_ID_OTHR = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAcct/Id/Othr";
	public static final String DBTR_ACCT_TP = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAcct/Tp";
	public static final String DBTR_ACCT_CCY = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAcct/Ccy";
	public static final String DBTR_ACCT_TP_CD = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAcct/Tp/Cd";
	public static final String DBTR_ACCT_TP_PRTRY = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAcct/Tp/Prtry";

	public static final String DBTR_AGNT = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt";
	public static final String DBTR_AGNT_FINST_ID = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId";
	public static final String DBTR_AGNT_FINST_ID_BIC = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId/BIC";
	public static final String DBTR_AGNT_FINST_ID_CLRSYS_MMBID = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId/ClrSysMmbId";
	public static final String DBTR_AGNT_FINST_ID_NAME = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId/Nm";
	public static final String DBTR_AGNT_FINST_ID_OTHR = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId/Othr";
	public static final String DBTR_AGNT_FINST_ID_PSTL_ADR = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId/PstlAdr";
	public static final String DBTR_AGNT_FINST_ID_PSTL_ADRLINE = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId/PstlAdr/AdrLine";
	public static final String DBTR_AGNT_FINST_ID_PSTL_CODE = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId/PstlAdr/PstCd";
	public static final String DBTR_AGNT_FINST_ID_PSTL_TOWN_NAME = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId/PstlAdr/TwnNm";
	public static final String DBTR_AGNT_FINST_ID_PSTL_COUNTRY_SUBD = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId/PstlAdr/CtrySubDvsn";
	public static final String DBTR_AGNT_FINST_ID_PSTL_COUNTRY = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId/PstlAdr/Ctry";

	public static final String DBTRAGNT_ACCNT = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgtAcct";
	public static final String DBTRAGNT_ACCNT_ID = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgtAcct/Id";
	public static final String DBTRAGNT_ACCNT_ID_IBAN = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgtAcct/Id/IBAN";
	public static final String DBTRAGNT_ACCNT_ID_OTHR = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgtAcct/Id/Othr";
	public static final String DBTRAGNT_ACCNT_TP = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgtAcct/Tp";
	public static final String DBTRAGNT_ACCNT_CCY = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgtAcct/Ccy";
	public static final String DBTRAGNT_ACCNT_TP_CD = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgtAcct/Tp/Cd";
	public static final String DBTRAGNT_ACCNT_TP_PRTRY = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgtAcct/Tp/Prtry";
	public static final String DBTR_AGNT_BRANCH_ID = "/Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/BrnchId";

	public static final String XCHANGERATEINFO = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/XchgRateInf";
	public static final String XCHANGERATEINFORATETP = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/XchgRateInf/RateTp";
	public static final String XCHANGERATE = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/XchgRateInf/XchgRate";
	public static final String CHARGEBR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/ChrgBr";

	public static final String PURPOSE = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Purp";
	public static final String PURPOSE_CODE = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Purp/Cd";
	public static final String PURPOSE_PRTRY = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Purp/Prtry";

	public static final String CDTR_PSTLADR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Cdtr/PstlAdr";
	public static final String CDTR_PSTLADR_ADRLINE = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Cdtr/PstlAdr/AdrLine";

	public static final String CDTR_PSTL_CODE = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Cdtr/PstlAdr/PstCd";
	public static final String CDTR_TOWN_NAME = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Cdtr/PstlAdr/TwnNm";
	public static final String CDTR_COUNTRY_SUBD = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Cdtr/PstlAdr/CtrySubDvsn";
	public static final String CDTR_COUNTRY = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Cdtr/PstlAdr/Ctry";

	public static final String CDTR_ID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Cdtr/Id";
	public static final String CDTR_ORGID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Cdtr/Id/OrgId";
	public static final String CDTR_PRVTID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Cdtr/Id/PrvtId";
	public static final String CDTR_CTGRYRES = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Cdtr/CtryOfRes";
	public static final String CDTR_CNTCT_DTLS = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Cdtr/CtctDtls";

	public static final String ULTM_CDTR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/UltmtCdtr";
	public static final String ULTM_CDTR_PSTLADR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/UltmtCdtr/PstlAdr";
	public static final String ULTM_CDTR_ID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/UltmtCdtr/Id";
	public static final String ULTM_CDTR_ORGID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/UltmtCdtr/Id/OrgId";
	public static final String ULTM_CDTR_PRVTID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/UltmtCdtr/Id/PrvtId";
	public static final String ULTM_CDTR_CTGRYRES = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/UltmtCdtr/CtryOfRes";
	public static final String ULTM_CDTR_CNTCT_DTLS = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/UltmtCdtr/CtctDtls";

	public static final String CDTR_AGNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgt";
	public static final String CDTR_AGNT_FINST_ID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgt/FinInstnId";
	public static final String CDTR_AGNT_FINST_ID_NM = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgt/FinInstnId/Nm";
	public static final String CDTR_AGNT_FINST_ID_PSTL_ADR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgt/FinInstnId/PstlAdr";
	public static final String CDTR_AGNT_FINST_ID_PSTL_ADRLINE = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgt/FinInstnId/PstlAdr/AdrLine";
	public static final String CDTR_AGNT_FINST_ID_PSTL_CODE = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgt/FinInstnId/PstlAdr/PstCd";
	public static final String CDTR_AGNT_FINST_ID_PSTL_TOWN_NAME = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgt/FinInstnId/PstlAdr/TwnNm";
	public static final String CDTR_AGNT_FINST_ID_PSTL_COUNTRY_SUBD = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgt/FinInstnId/PstlAdr/CtrySubDvsn";
	public static final String CDTR_AGNT_FINST_ID_PSTL_COUNTRY = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgt/FinInstnId/PstlAdr/Ctry";

	public static final String CDTR_AGNT_FINST_CLRSYS_MMBID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgt/FinInstnId/ClrSysMmbId";
	public static final String CDTR_AGNT_FINST_ID_BIC = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgt/FinInstnId/BIC";
	public static final String CDTR_AGNT_BRANCHID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgt/BrnchId";

	public static final String CDTRAGNT_ACCNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgtAcct";
	public static final String CDTRAGNT_ACCNT_TP = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgtAcct/Tp";
	public static final String CDTRAGNT_ACCNT_TP_CD = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgtAcct/Tp/Cd";
	public static final String CDTRAGNT_ACCNT_PRTRY = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgtAcct/Prtry";
	public static final String CDTRAGNT_ACCNT_ID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgtAcct/Id";
	public static final String CDTRAGNT_ACCNT_ID_IBAN = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgtAcct/Id/IBAN";
	public static final String CDTRAGNT_ACCNT_ID_OTHR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAgtAcct/Id/Othr";

	public static final String CDTR_ACCNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAcct";
	public static final String CDTR_ACCNT_ID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAcct/Id";
	public static final String CDTR_ACCNT_ID_OTHR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAcct/Id/Othr";
	public static final String CDTR_ACCNT_TP = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAcct/Tp";
	public static final String CDTR_ACCNT_TP_CD = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAcct/Tp/Cd";
	public static final String CDTR_ACCNT_PRTRY = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/CdtrAcct/Tp/Prtry";

	public static final String INSTR_CDTRAGNT_CD = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/InstrForCdtrAgt/Cd";
	public static final String INSTR_CDTRAGNT_INSTRINF = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/InstrForCdtrAgt/InstrInf";

	public static final String INSTR_DBTRAGNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/InstrForDbtrAgt";

	public static final String INTRM_AGNT1 = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1";
	public static final String INTRM_AGNT1_FINST = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1/FinInstnId";
	public static final String INTRM_AGNT1_FINST_CLRSYS_MMBID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1/FinInstnId/ClrSysMmbId";
	public static final String INTRM_AGNT1_FINST_BIC = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1/FinInstnId/BIC";
	public static final String INTRM_AGNT1_BRNCHID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1/BrnchId";

	public static final String INTRM_AGNT1_ACCNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1Acct";
	public static final String INTRM_AGNT1_ACCNT_TP = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1Acct/Tp";
	public static final String INTRM_AGNT1_ACCNT_TP_CD = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1Acct/Tp/Cd";
	public static final String INTRM_AGNT1_ACCNT_PRTRY = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1Acct/Prtry";
	public static final String INTRM_AGNT1_ACCNT_ID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1Acct/Id";
	public static final String INTRM_AGNT1_ACCNT_ID_IBAN = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1Acct/Id/IBAN";
	public static final String INTRM_AGNT1_ACCNT_ID_OTHR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1Acct/Id/Othr";
	public static final String INTRM_AGNT1_ACCNT_NM = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1Acct/Nm";

	public static final String INTRM_AGNT1_FINST_ID_PSTL_ADR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1/FinInstnId/PstlAdr";
	public static final String INTRM_AGNT1_FINST_ID_PSTL_ADRLINE = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1/FinInstnId/PstlAdr/AdrLine";
	public static final String INTRM_AGNT1_FINST_ID_PSTL_CODE = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1/FinInstnId/PstlAdr/PstCd";
	public static final String INTRM_AGNT1_FINST_ID_PSTL_TOWN_NAME = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1/FinInstnId/PstlAdr/TwnNm";
	public static final String INTRM_AGNT1_FINST_ID_PSTL_COUNTRY_SUBD = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1/FinInstnId/PstlAdr/CtrySubDvsn";
	public static final String INTRM_AGNT1_FINST_ID_PSTL_COUNTRY = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1/FinInstnId/PstlAdr/Ctry";
	// public static final String INTRM_AGNT1_FINST_ID_PSTL_CITY =
	// "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt1/FinInstnId/PstlAdr/City";

	public static final String INTRM_AGNT2 = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt2";
	public static final String INTRM_AGNT2_FINST = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt2/FinInstnId";
	public static final String INTRM_AGNT2_FINST_CLRSYS_MMBID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt2/FinInstnId/ClrSysMmbId";
	public static final String INTRM_AGNT2_FINST_BIC = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt2/FinInstnId/BIC";
	public static final String INTRM_AGNT2_BRNCHID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt2/BrnchId";

	public static final String INTRM_AGNT2_ACCNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt2Acct";
	public static final String INTRM_AGNT2_ACCNT_TP = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt2Acct/Tp";
	public static final String INTRM_AGNT2_ACCNT_TP_CD = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt2Acct/Tp/Cd";
	public static final String INTRM_AGNT2_ACCNT_PRTRY = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt2Acct/Prtry";
	public static final String INTRM_AGNT2_ACCNT_ID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt2Acct/Id";
	public static final String INTRM_AGNT2_ACCNT_ID_IBAN = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt2Acct/Id/IBAN";
	public static final String INTRM_AGNT2_ACCNT_ID_OTHR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/IntrmyAgt2Acct/Id/Othr";

	public static final String RGLT_DBTCDT_RPT_IND = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RgltryRptg/DbtCdtRptgInd";
	public static final String RGLT_AUTHRTY = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RgltryRptg/Authrty";
	public static final String RGLT_DTLS = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RgltryRptg/Dtls";
	public static final String RGLT_DTLS_TP = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RgltryRptg/Dtls/Tp";
	public static final String RGLT_DTLS_DT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RgltryRptg/Dtls/Dt";
	public static final String RGLT_DTLS_CTRY = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RgltryRptg/Dtls/Ctry";
	public static final String RGLT_DTLS_CD = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RgltryRptg/Dtls/Cd";
	public static final String RGLT_DTLS_AMNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RgltryRptg/Dtls/Amt";
	public static final String RGLT_DTLS_INF = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RgltryRptg/Dtls/Inf";

	public static final String RMT_INF = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf";
	public static final String RMT_INF_STRD = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd";
	public static final String RMT_INF_STRD_ADDL_RMTINF = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/AddtlRmtInf";
	public static final String RMT_INF_STRD_CDTR_REFINF = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/CdtrRefInf";
	public static final String RMT_INF_STRD_CDTR_REFINF_REF = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/CdtrRefInf/Ref";

	public static final String RMT_INF_STRD_CDTR_REFINF_TP = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/CdtrRefInf/Tp";
	public static final String RMT_INF_STRD_CDTR_REFINF_TP_CD_PRTRY = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/CdtrRefInf/Tp/CdOrPrtry";
	public static final String RMT_INF_STRD_CDTR_REFINF_TP_ISSR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/CdtrRefInf/Tp/Issr";

	public static final String RMT_INF_STRD_INVCE = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/Invcee";
	public static final String RMT_INF_STRD_INVCE_CNTCT_DTLS = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/Invcee/CtctDtls";
	public static final String RMT_INF_STRD_INVCE_CNTRYRES = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/Invcee/CtryOfRes";
	public static final String RMT_INF_STRD_INVCE_ID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/Invcee/Id";
	public static final String RMT_INF_STRD_INVCE_NM = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/Invcee/Nm";
	public static final String RMT_INF_STRD_INVCE_PSTLADR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/Invcee/PstlAdr";

	public static final String RMT_INF_STRD_INVCR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/Invcr";
	public static final String RMT_INF_STRD_INVCR_CNTCT_DTLS = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/Invcr/CtctDtls";
	public static final String RMT_INF_STRD_INVCR_CNTCT_CNTRYRES = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/Invcr/CtryOfRes";
	public static final String RMT_INF_STRD_INVCR_ID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/Invcr/Id";
	public static final String RMT_INF_STRD_INVCR_NM = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/Invcr/Nm";
	public static final String RMT_INF_STRD_INVCR_PSTLADR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/Invcr/PstlAdr";

	public static final String RMT_INF_STRD_RFRDOCAMT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/RfrdDocAmt";
	public static final String RMT_INF_STRD_RFRDOCAMT_ADJST_AMNT_RSN = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/RfrdDocAmt/AdjstmntAmtAndRsn";
	public static final String RMT_INF_STRD_RFRDOCAMT_CDTNOTEAMNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/RfrdDocAmt/CdtNoteAmt";
	public static final String RMT_INF_STRD_RFRDOCAMT_DSCNTAMNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/RfrdDocAmt/DscntApldAmt";
	public static final String RMT_INF_STRD_RFRDOCAMT_DUEPAYBAMNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/RfrdDocAmt/DuePyblAmt";
	public static final String RMT_INF_STRD_RFRDOCAMT_RMTDAMNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/RfrdDocAmt/RmtdAmt";
	public static final String RMT_INF_STRD_RFRDOCAMT_TAXAMNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/RfrdDocAmt/TaxAmt";
	public static final String RMT_INF_STRD_RFRDOCINF = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/RfrdDocInf";

	public static final String RMT_INF_USTRD = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Ustrd";

	public static final String RLTD_RMTINF = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RltdRmtInf";
	public static final String RLTD_RMTINF_RMTID = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RltdRmtInf/RmtId";
	public static final String RLTD_RMTINF_RMTLCT_ELECTADR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RltdRmtInf/RmtLctnElctrncAdr";
	public static final String RLTD_RMTINF_RMTLCT_MTD = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RltdRmtInf/RmtLctnMtd";
	public static final String RLTD_RMTINF_RMTLCT_PSTLADR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RltdRmtInf/RmtLctnPstlAdr";

	public static final String TAX = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Tax";
	public static final String TAX_CDTR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Tax/Cdtr";
	public static final String TAX_DBTR = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Tax/Dbtr";
	public static final String TAX_ADMST_ZN = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Tax/AdmstnZn";
	public static final String TAX_REFNB = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Tax/RefNb";
	public static final String TAX_MTD = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Tax/Mtd";
	public static final String TAX_TTL_TAXAMNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Tax/TtlTaxAmt";
	public static final String TAX_TTL_TAXBLEAMNT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Tax/TtlTaxblBaseAmt";
	public static final String TAX_DT = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Tax/Dt";
	public static final String TAX_SEQNB = "/Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Tax/SeqNb";

	public static final String CHK_INSTR = "/PmtInf/CdtTrfTxInf/ChqInstr";
	public static final String CHK_TP = "/PmtInf/CdtTrfTxInf/ChqInstr/ChqTp";
	public static final String CHK_NB = "/PmtInf/CdtTrfTxInf/ChqInstr/ChqNb";
	public static final String CHK_FREQ = "/PmtInf/CdtTrfTxInf/ChqInstr/ChqFr";
	public static final String CHK_DLVR_MTD = "/PmtInf/CdtTrfTxInf/ChqInstr/DlvryMtd";
	public static final String CHK_DLVR_TO = "/PmtInf/CdtTrfTxInf/ChqInstr/DlvrTo";
	public static final String CHK_INSTR_PRTY = "/PmtInf/CdtTrfTxInf/ChqInstr/InstrPrty";
	public static final String CHK_MTRTY_DATE = "/PmtInf/CdtTrfTxInf/ChqInstr/ChqMtrtyDt";
	public static final String CHK_FRMS_CD = "/PmtInf/CdtTrfTxInf/ChqInstr/FrmsCd";
	public static final String CHK_MEMO_FLD = "/PmtInf/CdtTrfTxInf/ChqInstr/MemoFld";
	public static final String CHK_RGNL_CLR_ZNE = "/PmtInf/CdtTrfTxInf/ChqInstr/RgnlClrZone";
	public static final String CHK_PRNT_LCTN = "/PmtInf/CdtTrfTxInf/ChqInstr/PrtLctn";

}
