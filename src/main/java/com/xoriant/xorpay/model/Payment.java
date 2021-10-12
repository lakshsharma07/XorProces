package com.xoriant.xorpay.model;


import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	
  private Date fromDate;
	private Date toDate;
	private Date pmtfromDate;
	private Date pmttoDate;
	private String identification;
	private String selectedPaymentId;
	private String pmtGpSts;
	private String houseBk;
	private String accId;
	private String payingCmpCode;
	private String sendingCmpCode;
	private String busiPartRef;
	private String pmtMethod;
	private String pmtMtSupp;
	private StatusCombo orgName;
	private StatusCombo pmtselectedStatus;
	private StatusCombo selectedStatus;
	private StatusCombo selectedOrgname;
	private StatusCombo pmtselectedOrgname;
	private Integer pageNo;
  private Integer pageSize;
  private Integer totalRecords;
  private boolean flag;

}
