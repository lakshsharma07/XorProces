package com.xoriant.xorpay.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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

@Table(name = "XP_XML_STRUCTURE_MASTER")
public class XMLStructureMasterEntity {

	@Id
	@Column(name = "SLNO")
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_XML_MASTER_STR") // , generator="SEQ")
	//@SequenceGenerator(name = "SEQ_XML_MASTER_STR", sequenceName = "SEQ_XP_XML_STRUCTURE_MASTER_SLNO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(name = "TAG_NAME")
	private String tagName;
	@Column(name = "XPATH")
	private String xpath;
	@Column(name = "TAG_PARENT_ID")
	private Integer tagParentId;
	@Column(name = "TAG_SEQ")
	private Integer tagSeq;
	@Column(name = "TAG_LEVEL")
	private Integer tagLevel;
	@Column(name = "ACTIVE_IND")
	private Character activeInd;
	@Column(name = "TAG_INFO_TYPE")
	private String tagInfoType;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATION_DATE")
	private LocalDateTime creationDate;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime lastUpdateDate;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

}
