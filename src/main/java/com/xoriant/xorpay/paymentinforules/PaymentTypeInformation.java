package com.xoriant.xorpay.paymentinforules;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.pojo001.CategoryPurpose1Choice;
import com.xoriant.xorpay.pojo001.LocalInstrument2Choice;
import com.xoriant.xorpay.pojo001.PaymentInstructionInformation3;
import com.xoriant.xorpay.pojo001.PaymentTypeInformation19;
import com.xoriant.xorpay.pojo001.ServiceLevel8Choice;
import com.xoriant.xorpay.constants.TagConstants;

@Service
public class PaymentTypeInformation {
	
  private static final Logger logger = LoggerFactory.getLogger(PaymentTypeInformation.class);
	public void getPaymentTypeInformation(PaymentInstructionInformation3 PmtInf, Map<String,String> pmtInfChilds) {
		PaymentTypeInformation19 PmtTpInf = PmtInf.getPmtTpInf();
		
		logger.info("PaymentTypeInformation getPaymentTypeInformation Starts... ");
		if(PmtTpInf != null) {
			
			ServiceLevel8Choice serviceLevel = PmtTpInf.getSvcLvl();
			
			if (serviceLevel != null && pmtInfChilds.containsKey(TagConstants.SRVC_LVL)) {
				if(serviceLevel.getCd() != null) {
					if(!pmtInfChilds.containsKey(TagConstants.SRVC_LVL_CD)) {
						PmtTpInf.getSvcLvl().setCd(null);
					}
				}
				if(serviceLevel.getPrtry() != null) {
					if(!pmtInfChilds.containsKey(TagConstants.SRVC_LVL_PRTRY)) {
						PmtTpInf.getSvcLvl().setPrtry(null);
					}	
				}
			}else{
				PmtTpInf.setSvcLvl(null);
			}
			
			LocalInstrument2Choice localInstrument = PmtTpInf.getLclInstrm();
			
			if (localInstrument != null && pmtInfChilds.containsKey(TagConstants.LCL_INSTRM)) {
				if(localInstrument.getCd() != null) {
					if(!pmtInfChilds.containsKey(TagConstants.LCL_INSTRM_CD)) {
						PmtTpInf.getLclInstrm().setCd(null);
					}
				}
				if(localInstrument.getPrtry() != null) {
					if(!pmtInfChilds.containsKey(TagConstants.LCL_INSTRM_PRTRY)) {
						PmtTpInf.getLclInstrm().setPrtry(null);
					}	
				}
			}else{
				PmtTpInf.setLclInstrm(null);
			}
			
			CategoryPurpose1Choice categoryPurpose = PmtTpInf.getCtgyPurp();
			
			
			if (categoryPurpose != null && pmtInfChilds.containsKey(TagConstants.CTGRY_PRP)) {
				if(categoryPurpose.getCd() != null) {
					if(!pmtInfChilds.containsKey(TagConstants.CTGRY_PRP_CD)) {
						PmtTpInf.getCtgyPurp().setCd(null);
					}
				}
				if(categoryPurpose.getPrtry() != null) {
					if(!pmtInfChilds.containsKey(TagConstants.CTGRY_PRP_PRTRY)) {
						PmtTpInf.getCtgyPurp().setPrtry(null);
					}	
				}
			}else{
				PmtTpInf.setCtgyPurp(null);
			}
			logger.info("PaymentTypeInformation getPaymentTypeInformation Ends... ");	
		}
	}
}
