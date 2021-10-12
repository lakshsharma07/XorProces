package com.xoriant.xorpay.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataProcessAudit {
	private Integer totalRecordToProcess;
	private DataProcessStageAuditPOJO stagingToNrmalizeAudit;
	private DataProcessStageAuditPOJO nrmalizeToAggregatedAudit;
	private DataProcessStageAuditPOJO loadPaymentDetailsAudit;
	private DataProcessStageAuditPOJO ruleEngineAudit;
	private DataProcessStageAuditPOJO paymentGeratedAudit;
}