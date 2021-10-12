package com.xoriant.xorpay.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "AGG_PRTY_PYMT_DTLS_H")
public class AggregatedPaymentHistoryEntity {

	@Id
	@Column(name = "SLNO")
	private Integer id;
	@Column(name = "MSGID_4")
	private String msgid4;
	@Column(name = "CREDTTM_5")
	private LocalDateTime credttm5;
	@Column(name = "NBOFTXS_6")
	private Integer nboftxs6;
	@Column(name = "NM_8")
	private String nm8;
	@Column(name = "PMTINFID_10")
	private String pmtinfid10;
	@Column(name = "PMTMTD_11")
	private String pmtmtd11;
	@Column(name = "CD_14")
	private String cd14;
	@Column(name = "CD_16")
	private String cd16;
	@Column(name = "CD_18")
	private String cd18;
	@Column(name = "PRTRY_19")
	private String prtry19;
	@Column(name = "REQDEXCTNDT_20")
	private LocalDateTime reqdexctndt20;
	@Column(name = "NM_22")
	private String dbtracct23;
	@Column(name = "ID_26")
	private String id26;
	@Column(name = "MMBID_30")
	private String mmbid30;
	@Column(name = "ENDTOENDID_33")
	private String endtoendid33;
	@Column(name = "INSTDAMT_35")
	private String instdamt35;
	@Column(name = "MMBID_39")
	private String mmbid39;
	@Column(name = "NM_41")
	private String nm41;
	@Column(name = "ID_45")
	private String id45;
	@Column(name = "NM_46")
	private String nm46;
	@Column(name = "CTRY_49")
	private String ctry49;
	@Column(name = "CTRY_51")
	private String ctry51;
	@Column(name = "ID_76")
	private String id76;
	@Column(name = "CD_78")
	private String cd78;
	@Column(name = "ADRLINE_79")
	private String adrline79;
	@Column(name = "BIC_80")
	private String bic80;
	@Column(name = "CTRY_82")
	private String ctry82;
	@Column(name = "CHRGBR_83")
	private String chrgbr83;
	@Column(name = "BIC_86")
	private String bic86;
	@Column(name = "MMBID_88")
	private String mmbid88;
	@Column(name = "NM_89")
	private String nm89;
	@Column(name = "ADRLINE_91")
	private String adrline91;
	@Column(name = "ID_95")
	private String id95;
	@Column(name = "BIC_98")
	private String bic98;
	@Column(name = "MMBID_100")
	private String mmbid100;
	@Column(name = "NM_101")
	private String nm101;
	@Column(name = "ADRLINE_103")
	private String adrline103;
	@Column(name = "ID_107")
	private String id107;
	@Column(name = "BIC_108")
	private String bic108;
	@Column(name = "NM_109")
	private String nm109;
	@Column(name = "ADRLINE_110")
	private String adrline110;
	@Column(name = "ADRLINE_111")
	private String adrline111;
	@Column(name = "INSTRINF_113")
	private String instrinf113;
	@Column(name = "INSTRFORDBTRAGT_114")
	private String instrfordbtragt114;
	@Column(name = "CTRY_115")
	private String ctry115;
	@Column(name = "CTRY_116")
	private String ctry116;
	@Column(name = "PRTRY_117")
	private String prtry117;
	@Column(name = "CD_118")
	private String cd118;
	@Column(name = "TAXTP_122")
	private String taxtp122;
	@Column(name = "TAXID_124")
	private String taxid124;
	@Column(name = "CTGYDTLS_126")
	private String ctgydtls126;
	@Column(name = "DT_127")
	private String dt127;
	@Column(name = "AMT_130")
	private String amt130;
	@Column(name = "CD_131")
	private String cd131;
	@Column(name = "TWNNM_132")
	private String twnnm132;
	@Column(name = "XML_STATUS")
	private Character xmlStatus;
	@Column(name = "ERP_SRC_SYS")
	private Integer srcSystem;
	@Column(name = "CTRLSUM_133")
	private Integer ctrlsum133;
	@Column(name = "NM_136")
	private String nm136;
	@Column(name = "ID_138")
	private String id138;
	@Column(name = "ID_140")
	private String id140;
	@Column(name = "PRTRY_142")
	private String prtry142;
	@Column(name = "RMTLCTNMTD_144")
	private String rmtlctnmtd144;
	@Column(name = "RMTLCTNELCTRNCADR_145")
	private String rmtlctnelctrncadr145;
	@Column(name = "DBTRAGT_FININSTNID_146")
	private String dbtragtFininstnid146;
	@Column(name = "DBTRAGT_FIN_BIC_147")
	private String dbtragtFinBic147;
	@Column(name = "DBTRAGT_PSTL_ADDR_148")
	private String dbtragtPstlAddr148;
	@Column(name = "DBTRAGT_PSTL_CNTRY_149")
	private String dbtragtPstlCntry149;
	@Column(name = "DBTRAGT_BRNCHID_150")
	private String dbtragtBrnchid150;
	@Column(name = "DBTRAGT_BRNCHID_ID_151")
	private String dbtragtBrnchidId151;
	@Column(name = "CDTRAGT_FININSTNID_152")
	private String cdtragtFininstnid152;
	@Column(name = "CDTRAGT_FIN_BIC_153")
	private String cdtragtFinBic153;
	@Column(name = "CDTRAGT_FIN_NM_154")
	private String cdtragtFinNm154;
	@Column(name = "CDTRAGT_PSTL_ADDR_155")
	private String cdtragtPstlAddr155;
	@Column(name = "CDTRAGT_PSTL_CNTRY_156")
	private String cdtragtPstlCntry156;
	@Column(name = "CDTRAGT_BRNCHID_157")
	private String cdtragtBrnchid157;
	@Column(name = "CDTRAGT_BRNCHID_ID_158")
	private String cdtragtBrnchidId158;
	@Column(name = "PROFILENAME")
	private String profilename;
	@Column(name = "CCY_CD")
	private String ccyCd;
	@Column(name = "XML_COMMENT")
	private String xmlComment;
	@Column(name = "ETL_PROCESS_COMMENT")
	private String etlProcessComment;

}