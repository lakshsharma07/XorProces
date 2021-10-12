package com.xoriant.xorpay.entity;

import java.time.LocalDate;

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

@Table(name = "XP_BATCH_PAYMENT_STATUS_VIEW_DAY")
public class BatchPaymentStatusDayEntity {

	@Id
	@Column(name = "slno")
	private Integer id;
	@Column(name = "MONTH")
	private Integer month;
	@Column(name = "YEAR")
	private Integer year;
	@Column(name = "MONTH_YEAR")
	private String monthYear;
	@Column(name = "PAYMENT")
	private Integer payment;
	@Column(name = "PAYMENT_DATE")
	private LocalDate paymentDate;
	@Column(name = "PAYMENT_DAY")
	private String paymentDay;
	
	@Column(name = "YEAR_MONTH_DAY")
	private String yearMonthDay;
	
	@Column(name = "SRC_SYSTEM")
	private String sourceSystem;
	
	@Column(name = "SRC_SYS_ID")
	private Integer sourceSysId;
	
}
