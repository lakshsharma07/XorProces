package com.xoriant.xorpay.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataProcessAuditPOJO {
	private String internalId;
	private String messageId;
	private String paymentId;
	private String status;
	private String comment;
	private String datetime;
}