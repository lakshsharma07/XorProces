package com.xoriant.xorpay.entity;

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
@Table(name = "XP_XML_TAGS_XPATH")
public class XMLTagXPathMasterEntity {

	@Id
	@Column(name = "TAG_SEQID")
	private Integer id;
	@Column(name = "MAX_LENGTH")
	private Integer maxLength;
	@Column(name = "CREATION_DATE")
	private LocalDateTime createdDate;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime updatedDate;
	@Column(name = "LAST_UPDATED_BY")
	private String updatedBy;
	@Column(name = "TAG_DATA_TYPE")
	private String tagDataType;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "AGG_REMITTANCE_COL")
	private String aggRemittanceCol;
	@Column(name = "TAG_NAME")
	private String tagName;
	@Column(name = "TAG_INFO")
	private String tagInfo;
	@Column(name = "XPATH")
	private String xpath;

}