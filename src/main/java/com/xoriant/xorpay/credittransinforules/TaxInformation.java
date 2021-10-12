package com.xoriant.xorpay.credittransinforules;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;
import com.xoriant.xorpay.pojo001.TaxInformation3;

@Service
public class TaxInformation {
  private static final Logger logger = LoggerFactory.getLogger(TaxInformation.class);
  
	public void getTax(CreditTransferTransactionInformation10 CdtTrfTxInf, Map<String,String> pmtInfChilds) {
	  logger.info("TaxInformation getTax() Started...");
	  TaxInformation3 tax = CdtTrfTxInf.getTax();
		if(tax != null && pmtInfChilds.containsKey(TagConstants.TAX)){
		  if(tax.getCdtr() != null && !pmtInfChilds.containsKey(TagConstants.TAX_CDTR)){
				CdtTrfTxInf.getTax().setCdtr(null);
			}
			
			if(tax.getDbtr() != null && !pmtInfChilds.containsKey(TagConstants.TAX_DBTR)){
				CdtTrfTxInf.getTax().setDbtr(null);
			}
			
			if(tax.getAdmstnZn() != null && !pmtInfChilds.containsKey(TagConstants.TAX_ADMST_ZN)){
				CdtTrfTxInf.getTax().setAdmstnZn(null);
			}
			
			if(tax.getRefNb() != null && !pmtInfChilds.containsKey(TagConstants.TAX_REFNB)){
				CdtTrfTxInf.getTax().setRefNb(null);
			}
			
			if(tax.getMtd() != null && !pmtInfChilds.containsKey(TagConstants.TAX_MTD)){
				CdtTrfTxInf.getTax().setMtd(null);
			}

			if(tax.getTtlTaxAmt() != null && !pmtInfChilds.containsKey(TagConstants.TAX_TTL_TAXAMNT)){
				CdtTrfTxInf.getTax().setTtlTaxAmt(null);
			}

			if(tax.getTtlTaxblBaseAmt() != null && !pmtInfChilds.containsKey(TagConstants.TAX_TTL_TAXBLEAMNT)){
				CdtTrfTxInf.getTax().setTtlTaxblBaseAmt(null);
			}

			if(tax.getDt() != null && !pmtInfChilds.containsKey(TagConstants.TAX_DT)){
				CdtTrfTxInf.getTax().setDt(null);
			}

			if(tax.getSeqNb() != null && !pmtInfChilds.containsKey(TagConstants.TAX_SEQNB)){
				CdtTrfTxInf.getTax().setSeqNb(null);
			}
		}else if (!pmtInfChilds.containsKey(TagConstants.TAX)) {
		  CdtTrfTxInf.setTax(null);
    }
		logger.info("TaxInformation getTax() Ended...");
	}
}
