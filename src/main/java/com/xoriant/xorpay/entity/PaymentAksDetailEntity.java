package com.xoriant.xorpay.entity;

import java.util.Date;

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

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "XP_PAYMENT_ACK_DETAILS")
public class PaymentAksDetailEntity {
	@Id
	@Column(name = "slno")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "internal_uuid")
	private String internalUuid;

	@Column(name = "ACK_ID")
	private String ackId;
	@Column(name = "RESPONSE_ACK_ID")
	private String responseAckId;
	@Column(name = "PAYMENT_INSTRUCTION_ID")
	private String paymentInstructionId;
	@Column(name = "PAYMENT_ID")
	private String paymentId;
	@Column(name = "ACK_STATUS")
	private String ackStatus;
	@Column(name = "FILE_NAME")
	private String fileName;
	@Column(name = "ORG_ID")
	private String orgId;
	@Column(name = "FILE_STATUS")
	private String fileStatus;
	@Column(name = "PAYMENT_AMOUNT")
	private Double paymentAmount;
	@Column(name = "PAYMENT_CURRENCY_CODE")
	private String paymentCurrencCode;
	@Column(name = "PAYMENT_DATE")
	private Date paymentDate;
	@Column(name = "PAYMENT_STATUS")
	private String paymentStatus;
	@Column(name = "ADDITIONAL_INFO")
	private String addInfo;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATION_DATE")
	private Date createdDate;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	@Column(name = "LAST_UPDATE_DATE")
	private Date lastUpdateDate;
	@Column(name = "PAYMENT_NUMBER")
	private String paymentNumber;
	@Column(name = "LOGICAL_GROUP_REFERENCE")
	private String logicalGrpRef;

}