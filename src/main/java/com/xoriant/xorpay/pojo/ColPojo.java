package com.xoriant.xorpay.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ColPojo {

	private String colPaymentIdSource;
	private String colBatch;
	private String colNoOftxn;
	private String colPartyName;
	private String colPaymentDate;
	private String colProfileName;
	private String colCurrency;
	private String colTransferDt;
	private String colInvoiceDt;
	private String colInstdAmt;
	private String colContrlSum;
	private String colInvoiceNo;
	private String colInvoiceAmt;
	private String colCompanyName;
	private String colPaymentMethod;

	@Override
	public String toString() {
		return "ColPojo [colPaymentIdSource=" + colPaymentIdSource + ", colBatch=" + colBatch + ", colNoOftxn="
				+ colNoOftxn + ", colPartyName=" + colPartyName + ", colPaymentDate=" + colPaymentDate
				+ ", colProfileName=" + colProfileName + ", colCurrency=" + colCurrency + ", colTransferDt="
				+ colTransferDt + ", colInvoiceDt=" + colInvoiceDt + ", colInstdAmt=" + colInstdAmt + ", colContrlSum="
				+ colContrlSum + ", colInvoiceNo=" + colInvoiceNo + ", colInvoiceAmt=" + colInvoiceAmt
				+ ", colCompanyName=" + colCompanyName + ", colPaymentMethod=" + colPaymentMethod + "]";
	}

}
