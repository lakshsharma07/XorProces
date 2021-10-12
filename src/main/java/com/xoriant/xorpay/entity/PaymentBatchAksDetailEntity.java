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

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "XP_PAYBATCH_ACK_DETAILS")
public class PaymentBatchAksDetailEntity {

	@Id
	@Column(name = "slno")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(name = "internal_uuid")
	private String internalUuId;

	@Column(name = "PAYBATCH_ACK_ID")
	private String paymentBatchAckID;

	@Column(name = "RESPONSE_ACK_ID")
	private String responseAckID;

	@Column(name = "PAYMENT_INSTRUCTION_ID")
	private String paymentInstructionId;
	@Column(name = "ACK_STATUS")
	private String ackStatus;
	@Column(name = "FILE_NAME")
	private String fileName;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATION_DATE")
	private LocalDateTime createdDate;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime lastUpdateDate;
	@Column(name = "ORG_ID")
	private String orgId;
	@Column(name = "FILE_STATUS")
	private String fileStatus;
	@Column(name = "ADDITIONAL_INFO")
	private String additionalInfo;

}
