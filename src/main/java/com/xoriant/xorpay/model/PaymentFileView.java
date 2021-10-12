package com.xoriant.xorpay.model;

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
public class PaymentFileView {

	private String createdDate;
	private String paymentDate;
	private Long id;
	private Long orgMsgId;
	private String orgCreDt;
	private String orgGroup;
	private String proposalRun;
	private String status;
	private String duplicate;
	private String incomingMsgType;
	private Long noOfTrasn;
	private String orgCntrlSum;
	private Long psrMsgId;
	private String psrAckCreDt;
	private String pmtGrpStatus;
	private String addInfo;
	private String createdBy;
	private String modifiedBy;
	private String modifiedDt;
	private String comments;
	//org_id
	private Long orgId;
	//payment_instruction_id
	private Long paymentInstructionId;
	private String ackStatus;
	private String fileStatus;
	//file_name
	private String fileName;
	private int countRows;
	private Integer pageNo;
	private Integer pageSize;
	private Integer totalRecords;

}
