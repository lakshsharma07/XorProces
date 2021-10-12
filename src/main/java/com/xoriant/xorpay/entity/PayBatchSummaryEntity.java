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

@Table(name = "XP_PAY_BATCH_SUMMARY")
public class PayBatchSummaryEntity {

	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BDSL")
	// @SequenceGenerator(name = "SEQ_BDSL", sequenceName =
	// "SEQ_XP_BATCH_DETAILS_STATUS_SLNO", allocationSize = 1)
	// @GeneratedValue(strategy = GenerationType.AUTO)

	@Id
	@Column(name = "slno")
	private String slno;

	@Column(name = "internal_uuid")
	private String internalUuid;

	@Column(name = "PAYMENT_INSTRUCTION_ID")
	private String paymentInstructionId;
	@Column(name = "PAYMENT_INSTRUCTION_STATUS")
	private String paymentInstructionStatus;
	@Column(name = "PAYMENT_COUNT")
	private Integer paymentCount;
	@Column(name = "PAYMENT_AMOUNT")
	private Double paymentAmount;
	@Column(name = "ORG_ID")
	private Integer orgId;
	@Column(name = "ORG_NAME")
	private String orgName;
	@Column(name = "PAYMENT_DATE")
	private LocalDateTime paymentDate;
	@Column(name = "PAYMENT_CURRENCY_CODE")
	private String paymentCurrencyCode;
	@Column(name = "ADDITIONAL_INFO")
	private String additionalInfo;
	@Column(name = "PROFILENAME")
	private String profilename;
	@Column(name = "FILE_NAME")
	private String fileName;
	@Column(name = "FILE_STATUS")
	private String fileStatus;
	@Column(name = "CREATION_DATE")
	private LocalDateTime creationDate;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime lastUpdateDate;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	@Column(name = "PROCESS_RUN_DATE")
	private LocalDateTime processRunDate;
	@Column(name = "REQUEST_ID")
	private Integer requestId;
	@Column(name = "SRC_SYSTEM")
	private String srcSystem;
	@Column(name = "ERP_SRC_SYS")
	private Integer erpSrcSys;

	public PayBatchSummaryEntity(String paymentInstructionId, Integer paymentCount, Double paymentAmount,
			String orgName, LocalDateTime paymentDate, String profilename, String paymentCurrencyCode) {
		this.paymentInstructionId = paymentInstructionId;
		this.paymentCount = paymentCount;
		this.paymentAmount = paymentAmount;
		this.orgName = orgName;
		this.paymentDate = paymentDate;
		this.profilename = profilename;
		this.paymentCurrencyCode = paymentCurrencyCode;
	}
}
