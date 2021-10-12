package com.xoriant.xorpay.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentErrorPojo {

	private String msgId;
	private String endToEndId;
	private String invoiceNo;
	private String instdAmt;
	private String ccy;
	// reqdExctnDt
	private String transferDt;
	private String comment;
	private Integer sourceSystemId;
	private String noOfPayment;
	private String internalId;
	private String profileName;
	private String paymentMethod;
	private String companyName;
	private String creationDate;
	private String invoiceDt;
	private String controlSum;
	private String invoiceAmt;

	@Override
	public String toString() {
		return "PaymentErrorPojo [msgId=" + msgId + ", endToEndId=" + endToEndId + ", invoiceNo=" + invoiceNo
				+ ", instdAmt=" + instdAmt + ", ccy=" + ccy + ", transferDt=" + transferDt + ", comment=" + comment
				+ ", sourceSystemId=" + sourceSystemId + ", noOfPayment=" + noOfPayment + ", internalId=" + internalId
				+ ", profileName=" + profileName + ", paymentMethod=" + paymentMethod + ", companyName=" + companyName
				+ ", creationDate=" + creationDate + ", invoiceDt=" + invoiceDt + ", controlSum=" + controlSum
				+ ", invoiceAmt=" + invoiceAmt + "]";
	}

}
