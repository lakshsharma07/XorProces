package com.xoriant.xorpay.paymentinforules;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.pojo001.AccountIdentification4Choice;
import com.xoriant.xorpay.pojo001.CashAccount16;
import com.xoriant.xorpay.pojo001.CashAccountType2;
import com.xoriant.xorpay.pojo001.PaymentInstructionInformation3;
import com.xoriant.xorpay.constants.TagConstants;


@Service
public class DebtorAccount {
	
  private static final Logger logger = LoggerFactory.getLogger(DebtorAccount.class);
  
	public void getDbtrAcct(PaymentInstructionInformation3 PmtInf,Map<String,String> pmtInfChilds) {
		
		CashAccount16 DbtrAcct = PmtInf.getDbtrAcct();
		
		if(DbtrAcct != null) {
			
			AccountIdentification4Choice Id = DbtrAcct.getId();
			if(Id != null) {
				if(pmtInfChilds.containsKey(TagConstants.DBTR_ACCT_ID)) {
					if(!pmtInfChilds.containsKey(TagConstants.DBTR_ACCT_ID_IBAN)) {
						PmtInf.getDbtrAcct().getId().setIBAN(null);
					}
					if(!pmtInfChilds.containsKey(TagConstants.DBTR_ACCT_ID_OTHR)) {
						PmtInf.getDbtrAcct().getId().setOthr(null);
					}
				}else if(!pmtInfChilds.containsKey(TagConstants.DBTR_ACCT_ID)){
					PmtInf.getDbtrAcct().setId(null);
				}
			}
			
			CashAccountType2 Tp = DbtrAcct.getTp();
			if(Tp != null && pmtInfChilds.containsKey(TagConstants.DBTR_ACCT_TP)) {
				if(!pmtInfChilds.containsKey(TagConstants.DBTR_ACCT_TP_CD)) {
					PmtInf.getDbtrAcct().getTp().setCd(null);
				}
				if(!pmtInfChilds.containsKey(TagConstants.DBTR_ACCT_TP_PRTRY)) {
					PmtInf.getDbtrAcct().getTp().setPrtry(null);
				}
			}else if(!pmtInfChilds.containsKey(TagConstants.DBTR_ACCT_TP)){
				PmtInf.getDbtrAcct().setTp(null);
			}
			
			String Ccy = DbtrAcct.getCcy();
			if(Ccy != null && !pmtInfChilds.containsKey(TagConstants.DBTR_ACCT_CCY)) {
				PmtInf.getDbtrAcct().setCcy(null);
			}
		}
	}
}
