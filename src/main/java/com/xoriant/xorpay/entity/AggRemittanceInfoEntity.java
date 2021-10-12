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

@Table(name = "AGG_REMITTANCE_INFO")
public class AggRemittanceInfoEntity {

	@Id
	@Column(name = "slno")
	private Integer id;
	@Column(name = "msgid_4")
	private String msgId;
	@Column(name = "endtoendid_33")
	private String endToEndId;
	@Column(name = "ustrd_53")
	private String rmtUstrd;
	@Column(name = "strd_54")
	private String rmtstrd;
	@Column(name = "rfrddocinf_55")
	private String rfrdDocInf;
	@Column(name = "tp_56")
	private String strdtp;
	@Column(name = "cdorprtry_57")
	private String strdcdorprty;
	@Column(name = "cd_58")
	private String strdTpCd;
	@Column(name = "nb_59")
	private String strdNb;
	@Column(name = "rltddt_60")
	private LocalDateTime strdRtldDt;
	@Column(name = "rfrddocamt_61")
	private String rfrdDocAmt;
	@Column(name = "duepyblamt_62")
	private BigDecimal duePaybAmt;

	@Column(name = "dscntapldamt_63")
	private String strdDscntapldAmt;
	@Column(name = "cdtnoteamt_64")
	private String strdCdtnoteAmt;
	@Column(name = "taxamt_65")
	private BigDecimal strdtaxamt;
	@Column(name = "rmtdamt_66")
	private BigDecimal strdInstdAmt;
	@Column(name ="orgnl_invoice_amount")
	private BigDecimal rmtdAmt;

	@Column(name = "cdtrrefinf_67")
	private String strdCdtrrefInf;
	@Column(name = "tp_68")
	private String strdTp;
	@Column(name = "cdorprtry_69")
	private String strdCdorprtry;

	@Column(name = "cd_70")
	private String strdCd;
	@Column(name = "ref_71")
	private String strdRef;
	@Column(name = "addtlrmtinf_72")
	private String strdAddtlrmtinf;

	@Column(name = "erp_src_sys")
	private String sourceSystemId;
	

}