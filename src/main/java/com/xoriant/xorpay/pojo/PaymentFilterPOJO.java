package com.xoriant.xorpay.pojo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentFilterPOJO {

  private Integer id;
  private Integer paymentInstructionId;
  private Integer paymentId;
  private Integer paymentNumber;
  private String logicalGroupReference;
  private String alreadyExists;
  private Integer supplierId;
  private String supplierName;
  private Integer supplierSiteId;
  private String supplierSiteName;
  private String paymentMethodCode;
  private String paymentStatus;
  private String paymentAmount;
  private String paymentCurrencyCode;
  private Integer orgId;
  private String orgName;
  private Integer requestId;
  private LocalDateTime paymentDate;
  private LocalDateTime creationDate;
  private String createdBy;
  private String lastUpdatedBy;
  private String fileStatus;
  private String additionalInfo;
  private String piuid;
  private String profileName;
  private Integer checkId;
  private String fileName;
  private String lastUpdateDate;
  private String comments;
  private String ackStatus;
  private String statusDesc;

  //Below item not reuired  now
  private Long bankId;
  private String bankAccountNumber;
  private String bankAccountName;
  private String bankBranchName;
  private Long paymentAckId;
  

  public PaymentFilterPOJO(String ackStatus, String orgName,String supplierName,String paymentMethodCode) {
    this.ackStatus = ackStatus;
    this.orgName = orgName;
    this.paymentMethodCode = paymentMethodCode;
    this.supplierName = supplierName;
  }
}
