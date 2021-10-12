package com.xoriant.xorpay.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentView {

	// comments
	private Long paymentId;
	private Long paymentNumber;
	private String comments;
	private Long paymentInstructionId;

	// payment details
	private Long supplierId;
	private String supplierName;
	private Long supplierSiteId;
	private String supplierSiteName;
	private String paymentMethodCode;
	private String paymentStatus;
	private String alreadyExists;
	private String paymentAmount;
	private String paymentCurrencyCode;
	private Long orgId;
	private Long bankId;
	private String bankAccountNumber;
	private String bankAccountName;
	private String bankBranchName;
	private Long requestId;
	private String paymentDate;
	private String creationDate;
	private String createdBy;
	private String lastUpdateDate;
	private String lastUpdatedBy;
	private String fileStatus;
	private String addInfo;
	

	// Acknowledgement
	private Long paymentAckId;
	private String ackStatus;
	private String statusDesc;
	private String fileName;
	private Integer countRows;
	
	//Aded for pmtDelComments
	private String status;
	
/*
	// comments
	private Long payment_id;
	private Long payment_number;
	private String comments;
	private Long payment_instruction_id;

	// payment details
	private Long supplier_id;
	private String supplier_name;
	private Long supplier_site_id;
	private String supplier_site_name;
	private String payment_method_code;
	private String payment_status;
	private String already_exists;
	private String payment_amount;
	private String payment_currency_code;
	private Long org_id;
	private Long bank_id;
	private String bank_account_number;
	private String bank_account_name;
	private String bank_branch_name;
	private Long request_id;
	private String payment_date;
	private String creation_date;
	private String created_by;
	private String last_update_date;
	private String last_updated_by;
	private String file_status;
	private String addInfo;
	

	// Acknowledgement
	private Long payment_ack_id;
	private String ack_status;
	private String statusDesc;
	private String file_name;
	private Integer countRows;
*/
}
