package com.xoriant.xorpay.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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

@Table(name = "XP_NRMLZ_TO_XML_TAGS_MAP")
public class NormalizeAggXmlTagsMapEntity {

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NAXTM")
	//@SequenceGenerator(name = "SEQ_NAXTM", sequenceName = "SEQ_XP_NRMLZ_TO_XML_TAGS_MAP_SLNO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SLNO")
	private Integer id;
	@Column(name = "ERP_SRC_SYS")
	private Integer erpSrcSys;
	@Column(name = "NRMLZ_COL_NAME")
	private String nrmlzColName;
	@Column(name = "NRMLZ_COL_DATA_TYPE")
	private Integer nrmlzColDataType;
	@Column(name = "TAG_SEQID")
	private Integer tagSeqId;
	@Column(name = "AGG_COL_NAME")
	private String aggColName;
	@Column(name = "AGG_COL_DATA_TYPE")
	private Integer aggColDataType;
	@Column(name = "TAG_NAME")
	private String tagName;
	@Column(name = "XPATH")
	private String xpath;
	@Column(name = "TAG_INFO_TYPE")
	private String tagInfoType;
	@Column(name = "CREATION_DATE")
	private LocalDateTime creationDate;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime updateDate;
	@Column(name = "LAST_UPDATED_BY")
	private String updatedBy;
	@Column(name = "ACTIVE_IND")
	private Character activeInd;
	@Column(name = "EFFECTIVE_START_DATE")
	private LocalDateTime startDate;
	@Column(name = "EFFECTIVE_END_DATE")
	private LocalDateTime endDate;
	@Column(name = "PIUID_SEQID")
	private Integer piuidSeqId;

}
