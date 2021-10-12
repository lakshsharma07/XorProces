package com.xoriant.xorpay.paymentinforules;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.pojo001.CashAccount16;
import com.xoriant.xorpay.pojo001.CashAccountType2;
import com.xoriant.xorpay.pojo001.PaymentInstructionInformation3;
import com.xoriant.xorpay.constants.TagConstants;

@Service
public class DebtorAgentAccount {
  
  private static final Logger logger = LoggerFactory.getLogger(DebtorAgentAccount.class);

	public void getDbtrAgtAcct(PaymentInstructionInformation3 PmtInf, Map<String,String> pmtInfChilds) {
		
	  logger.info("DebtorAgentAccount getDbtrAgtAcct() Start...");
	  
		if(PmtInf.getDbtrAgtAcct() != null && pmtInfChilds.containsKey(TagConstants.DBTRAGNT_ACCNT)) {
			CashAccount16 DbtrAgtAcct = PmtInf.getDbtrAgtAcct();
			
			if(DbtrAgtAcct.getId() != null && pmtInfChilds.containsKey(TagConstants.DBTRAGNT_ACCNT_ID)) {
				if(!pmtInfChilds.containsKey(TagConstants.DBTRAGNT_ACCNT_ID_IBAN)) {
					PmtInf.getDbtrAgtAcct().getId().setIBAN(null);
				}
				if(!pmtInfChilds.containsKey(TagConstants.DBTRAGNT_ACCNT_ID_OTHR)) {
					PmtInf.getDbtrAgtAcct().getId().setOthr(null);
				}
			}else if(!pmtInfChilds.containsKey(TagConstants.DBTRAGNT_ACCNT_ID)){
				PmtInf.getDbtrAgtAcct().setId(null);
			}
		
	
			if(DbtrAgtAcct.getTp() != null && pmtInfChilds.containsKey(TagConstants.DBTRAGNT_ACCNT_TP)) {
				CashAccountType2 Tp = DbtrAgtAcct.getTp();
				if(Tp != null && pmtInfChilds.containsKey(TagConstants.DBTRAGNT_ACCNT_TP)) {
					if(!pmtInfChilds.containsKey(TagConstants.DBTRAGNT_ACCNT_TP_CD)) {
						PmtInf.getDbtrAgtAcct().getTp().setCd(null);
					}
					if(!pmtInfChilds.containsKey(TagConstants.DBTRAGNT_ACCNT_TP_PRTRY)) {
						PmtInf.getDbtrAgtAcct().getTp().setPrtry(null);
					}
				}else if(!pmtInfChilds.containsKey(TagConstants.DBTRAGNT_ACCNT_TP)){
					PmtInf.getDbtrAgtAcct().setTp(null);
				}
			}else if(!pmtInfChilds.containsKey(TagConstants.DBTRAGNT_ACCNT_TP)) {
				DbtrAgtAcct.setTp(null);
			}
			
			
			if(DbtrAgtAcct.getCcy() != null && !pmtInfChilds.containsKey(TagConstants.DBTRAGNT_ACCNT_CCY)) {
				PmtInf.getDbtrAgtAcct().setCcy(null);
			}
		}
		logger.info("DebtorAgentAccount getDbtrAgtAcct() Ends...");
	}
}
