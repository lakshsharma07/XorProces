package com.xoriant.xorpay.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AGG_PRTY_PYMT_DTLS_E")
public class XMLTagErrorEntity {

	@Id
	@Column(name = "slno")
	private Integer id;

	@Column(name = "msgid_4")
	private String msgId;
	@Column(name = "credttm_5")
	private LocalDateTime cretionDt;
	@Column(name = "nboftxs_6")
	private Integer nbOfTxs;
	@Column(name = "ctrlsum_133")
	private BigDecimal CntrlSum;
	@Column(name = "initgpty_7")
	private String InitgPty;
	@Column(name = "nm_8")
	private String InitgNm;

	@Column(name = "nm_136")
	private String fwdAgntNm;
	@Column(name = "pmtinfid_10")
	private String pmtInfId;
	@Column(name = "pmtmtd_11")
	private String pmtMtd;

	@Column(name = "cd_14")
	private String svcLvlCd;
	@Column(name = "cd_16")
	private String lclInstmCd;
	@Column(name = "cd_18")
	private String CtgyPurpCd;
	@Column(name = "reqdexctndt_20")
	private LocalDateTime reqstExDt;
	@Column(name = "nm_22")
	private String dbtrNm;
	@Column(name = "id_26")
	private String dbtrAccntOthrId;

	@Column(name = "bic_80")
	private String dbtrAgntBic;
	@Column(name = "ctry_82")
	private String dbtrAgntPstlCtry;
	@Column(name = "id_138")
	private String dbtrAgntBrnId;

	@Column(name = "endtoendid_33")
	private String endToEndId;
	@Column(name = "instdamt_35")
	private BigDecimal instdAmnt;
	@Column(name = "bic_108")
	private String cdtrAgntBic;
	@Column(name = "nm_109")
	private String cdtrAgntNm;

	@Column(name = "id_140")
	private String cdtrAgntBrnId;

	@Column(name = "nm_41")
	private String cdtrNm;
	@Column(name = "dbtragt_pstl_cntry_149")
	private String cdtrPstlCtry;
	@Column(name = "id_45")
	private String cdtrAccntOthrId;

	@Column(name = "nm_46")
	private String cdtrAccntNm;
	@Column(name = "prtry_142")
	private String purpPrtry;
	@Column(name = "rmtlctnmtd_144")
	private String rmtLctnMthd;
	@Column(name = "rmtlctnelctrncadr_145")
	private String rmtLctnElctAddr;
	@Column(name = "ustrd_53")
	private String rmtUstrd;

	@Column(name = "cd_58")
	private String strdTpCd;
	@Column(name = "nb_59")
	private String strdNb;
	@Column(name = "rltddt_60")
	private LocalDateTime strdRtldDt;
	@Column(name = "duepyblamt_62")
	private BigDecimal duePaybAmt;
	@Column(name = "rmtdamt_66")
	private BigDecimal rmtdAmt;

	@Column(name = "ccy_cd")
	private String ccyCode;

	@Column(name = "erp_src_sys")
	private Integer sourceSystem;

	@Column(name = "xml_status")
	private Character xmlStatus;

	@Column(name = "PROFILENAME")
	private String profileName;

	@Column(name = "ctry_51")
	private String CntrCode2;
	@Column(name = "ctry_115")
	private String CntrCode4;
	@Column(name = "ctry_116")
	private String CntrCode5;

	@Column(name = "XML_COMMENT")
	private String comment;
}