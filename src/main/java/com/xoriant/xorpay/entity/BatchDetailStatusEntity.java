package com.xoriant.xorpay.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "XP_BATCH_DETAILS_STATUS")
public class BatchDetailStatusEntity {
	@Id
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BDSL")
	// @SequenceGenerator(name = "SEQ_BDSL", sequenceName =
	// "SEQ_XP_BATCH_DETAILS_STATUS_SLNO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SLNO")
	private Integer id;

	@Column(name = "BATCH")
	private String batch;
	@Column(name = "NOOFPAYMENT")
	private Integer noOfPayment;
	@Column(name = "PARTY_NAME")
	private String partyName;

	@Column(name = "PAYMENT_DATE")
	// @Temporal(TemporalType.TIMESTAMP)
	// @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
	private LocalDateTime paymentDate;

	@Column(name = "RECORDS_PROCESSED")
	private Integer recordProcessed;
	@Column(name = "ERP_SRC_SYS")
	private Integer srcSystem;
	@Column(name = "START_TIME")
	private LocalDateTime startTime;
	@Column(name = "END_TIME")
	private LocalDateTime endTime;
	@Column(name = "ETL_STATUS")
	private String status;

	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATION_DATE")
	private LocalDateTime createdDate;

	@Column(name = "LAST_UPDATED_BY")
	private String updatedBy;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime updatedDate;

	public BatchDetailStatusEntity(Integer srcSystemId, Integer noOfTxn, String batch, String partyName,
			LocalDateTime paymentDate, int recordsProcessed, LocalDateTime creatioDate, String createdBy,
			LocalDateTime updatedDate, String updatedBy) {
		this.srcSystem = srcSystemId;
		this.noOfPayment = noOfTxn;
		this.batch = batch;
		this.paymentDate = paymentDate;
		this.recordProcessed = recordsProcessed;
		this.createdBy = createdBy;
		this.createdDate = creatioDate;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;

	}
}
