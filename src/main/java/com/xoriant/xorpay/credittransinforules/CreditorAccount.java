package com.xoriant.xorpay.credittransinforules;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.pojo001.AccountIdentification4Choice;
import com.xoriant.xorpay.pojo001.CashAccount16;
import com.xoriant.xorpay.pojo001.CashAccountType2;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;

@Service
public class CreditorAccount {
  
private static final Logger logger = LoggerFactory.getLogger(CreditorAccount.class);
  
  public void getCredtAcct(CreditTransferTransactionInformation10 CdtTrfTxInf,Map<String,String> pmtInfChilds) {
    
    logger.info("CreditorAccount getCredtAcct() Start..");
    
    CashAccount16 CrdtrAcct = CdtTrfTxInf.getCdtrAcct();
    
    if(CrdtrAcct != null && pmtInfChilds.containsKey(TagConstants.CDTR_ACCNT)) {
      
      AccountIdentification4Choice Id = CrdtrAcct.getId();
      if(Id != null) {
        if(pmtInfChilds.containsKey(TagConstants.CDTR_ACCNT_ID)) {
          /*if(!pmtInfChilds.containsKey(TagConstants.DBTR_ACCT_ID_IBAN)) {
            CdtTrfTxInf.getCdtrAcct().getId().setIBAN(null);
          }*/
          if(!pmtInfChilds.containsKey(TagConstants.CDTR_ACCNT_ID_OTHR)) {
            CdtTrfTxInf.getCdtrAcct().getId().setOthr(null);
          }
        }else if(!pmtInfChilds.containsKey(TagConstants.CDTR_ACCNT_ID)){
          CdtTrfTxInf.getCdtrAcct().setId(null);
        }
      }
      
      CashAccountType2 Tp = CrdtrAcct.getTp();
      if(Tp != null && pmtInfChilds.containsKey(TagConstants.CDTR_ACCNT_TP)) {
    	  
        if(!pmtInfChilds.containsKey(TagConstants.CDTR_ACCNT_TP_CD)) {
        	logger.info("Inside CrdtAccnt TP CD"+pmtInfChilds.containsKey(TagConstants.CDTR_ACCNT_TP_CD));
          CdtTrfTxInf.getCdtrAcct().getTp().setCd(null);
        }
        
        if(!pmtInfChilds.containsKey(TagConstants.CDTR_ACCNT_PRTRY)) {
        	logger.info("Inside CrdtAccnt TP PRTRY"+pmtInfChilds.containsKey(TagConstants.CDTR_ACCNT_PRTRY));
        	CdtTrfTxInf.getCdtrAcct().getTp().setPrtry(null);
        }
      }else if(!pmtInfChilds.containsKey(TagConstants.CDTR_ACCNT_TP)){
        CdtTrfTxInf.getCdtrAcct().setTp(null);
      }
      
      /*String Ccy = CrdtrAcct.getCcy();
      if(Ccy != null && !pmtInfChilds.containsKey(TagConstants.DBTR_ACCT_CCY)) {
        CdtTrfTxInf.getCdtrAcct().setCcy(null);
      }*/
    } else if(!pmtInfChilds.containsKey(TagConstants.CDTR_ACCNT)) {
      CdtTrfTxInf.setCdtrAcct(null);
    }
    logger.info("CreditorAccount getCredtAcct() End..");
  }

}
