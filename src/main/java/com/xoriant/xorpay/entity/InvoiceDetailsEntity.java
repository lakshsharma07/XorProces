package com.xoriant.xorpay.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
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

@Table(name = "xp_pay_invoices")
public class InvoiceDetailsEntity {

	@Id
	@Column(name = "INVOICE_ID")
	private Integer invoiceId;

	@Column(name = "PAYMENT_ID")
	private Long paymentId;
	
	@Column(name = "INVOICE_NUM")
	private String invoiceNum;
	
	@Column(name = "INVOICE_AMOUNT")
	private String invoiceAmount;
	
	@Column(name = "INVOICE_CURRENCY_CODE")
	private String invoiceCurrencyCode;
	
	@Column(name = "INVOICE_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy")
	private LocalDateTime invoiceDate;
	
	@Column(name = "PAYMENT_METHOD_CODE")
	private String paymentMethodCode;
	
	@Column(name = "REQUEST_ID")
	private Long requestId;
	
	@Column(name = "SUPPLIER_NAME")
	private String supplierName;
	
	@Column(name = "SUPPLIER_SITE_NAME")
	private String supplierSiteName;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATION_DATE")
	private LocalDateTime creationDate;
	
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime lastUpdateDate;
	
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdatedBy;
	
	@Column(name = "ORG_ID")
	private Long orgId;
	
}
