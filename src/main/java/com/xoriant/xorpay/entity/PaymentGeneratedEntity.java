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

@Table(name = "XP_PAYMENT_GENERATED")
public class PaymentGeneratedEntity {

	@Id
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator =
	// "SEQ_ADD_SOURCE") // , generator="SEQ")
	// @SequenceGenerator(name = "SEQ_ADD_SOURCE", sequenceName =
	// "SEQ_XP_ADD_NEW_SRC_COLUMN_SLNO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SLNO")
	private Integer slno;
	@Column(name = "MESSAGE_ID")
	private String messageId;
	@Column(name = "XML_PATH")
	private String xmlPath;

	@Column(name = "piuid_seqid")
	private Integer piuidSeqId;
	@Column(name = "profileName")
	private String profileName;
	@Column(name = "xml_data")
	private String xmlData;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATION_DATE")
	private LocalDateTime creationDate;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime lastUpdateDate;

}
