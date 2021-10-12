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

@Table(name = "XP_PIUID_XML_XSD")
public class XMLPIUIDXSDEntity {

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_XML_XSD") 
	//@SequenceGenerator(name = "SEQ_XML_XSD", sequenceName = "SEQ_XP_XML_PIUID_XSD_SLNO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SLNO")
	private Integer id;

	@Column(name = "piuid_seqid")
	private Integer piuidSeqId;

	@Column(name = "XML_XSD", columnDefinition = "TEXT")
	private String xmlXsd;

	@Column(name = "ACTIVE_IND")
	private Character activeInd;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATION_DATE")
	private LocalDateTime creationDate;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime lastUpdateDate;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;

}
