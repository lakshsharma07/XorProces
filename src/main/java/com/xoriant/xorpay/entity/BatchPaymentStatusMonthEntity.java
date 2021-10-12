package com.xoriant.xorpay.entity;

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

@Table(name = "XP_BATCH_PAYMENT_STATUS_VIEW_MONTH")
public class BatchPaymentStatusMonthEntity {

	@Id
	@Column(name = "slno")
	private Integer id;
	@Column(name = "BATCH")
	private Integer batch;
	@Column(name = "MONTH")
	private Integer month;
	@Column(name = "MONTH_YEAR")
	private String monthYear;
	@Column(name = "PAYMENT")
	private Integer payment;
	@Column(name = "YEAR")
	private Integer year;
	@Column(name = "YEAR_MNTH")
	private Integer yearMnth;
	
	@Column(name = "SRC_SYSTEM")
	private String sourceSystem;
	
	@Column(name = "SRC_SYS_ID")
	private Integer sourceSysId;
}
