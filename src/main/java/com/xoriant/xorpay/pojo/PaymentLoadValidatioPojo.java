package com.xoriant.xorpay.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentLoadValidatioPojo {

	private boolean isInvalidBatchNo;
	private boolean isInvalidEndToEndId;
	private boolean isInvalidNoOfTx;
	private boolean isInvalidInvoiceNo;	
	private boolean isInvalidCreationDt;
	private boolean isInvalidCurrency;
	private boolean isInvalidTransferDt;
	private boolean isInvalidInvoiceDt;
	private boolean isInvalidInstdAmt;
	private boolean isInvalidContrlSum;
	private boolean isInvalidInvoiceAmt;

}
