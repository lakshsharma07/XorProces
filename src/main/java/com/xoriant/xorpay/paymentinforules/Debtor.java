package com.xoriant.xorpay.paymentinforules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.pojo001.ContactDetails2;
import com.xoriant.xorpay.pojo001.Party6Choice;
import com.xoriant.xorpay.pojo001.PartyIdentification32;
import com.xoriant.xorpay.pojo001.PaymentInstructionInformation3;
import com.xoriant.xorpay.pojo001.PostalAddress6;
import com.xoriant.xorpay.util.CustomTagPstlAdr;
import com.xoriant.xorpay.constants.TagConstants;

@Service
public class Debtor {
  
  private static final Logger logger = LoggerFactory.getLogger(Debtor.class);
	public void getDbtr(PaymentInstructionInformation3 PmtInf, Map<String,String> pmtInfChilds) {
		
	  logger.info("Debtor getDbtr() Start...");
	  
		PartyIdentification32 Dbtr = PmtInf.getDbtr();
		List<String> addrList = new ArrayList<String>();
	  List<String> updatedAddrList = new ArrayList<String>();
	  CustomTagPstlAdr addrlCustom = new CustomTagPstlAdr();
		
		if(Dbtr != null) {
			
			//This is mandatory, will always come
			//Dbtr.getNm();
			
			PostalAddress6 postalAddress = Dbtr.getPstlAdr();
			if(postalAddress != null && !pmtInfChilds.containsKey(TagConstants.DBTR_PSTLADR)) {
			  PmtInf.getDbtr().setPstlAdr(null);
			  
			} else if(pmtInfChilds.containsKey(TagConstants.DBTR_PSTLADR)) { 
      			 if(pmtInfChilds.containsKey(TagConstants.DBTR_PSTLADR_ADRLINE) && pmtInfChilds.containsValue(TagConstants.MAX_LENGTH)) {
      			  addrList = postalAddress.getAdrLine();
              addrlCustom.trimAddrLine35Char(addrList,postalAddress,updatedAddrList);  
      			} else if(!pmtInfChilds.containsKey(TagConstants.DBTR_PSTLADR_ADRLINE)) {
      			  postalAddress.getAdrLine().clear();
      			}
      			 if(!pmtInfChilds.containsKey(TagConstants.DBTR_PST_CODE)) {
      			   PmtInf.getDbtr().getPstlAdr().setPstCd(null);
      			 }
      			 if(!pmtInfChilds.containsKey(TagConstants.DBTR_TOWN_NAME)) {
               PmtInf.getDbtr().getPstlAdr().setTwnNm(null);
             }
      			 if(!pmtInfChilds.containsKey(TagConstants.DBTR_COUNTRY_SUBD)) {
               PmtInf.getDbtr().getPstlAdr().setCtrySubDvsn(null);
             }
      			 if(!pmtInfChilds.containsKey(TagConstants.DBTR_COUNTRY)) {
               PmtInf.getDbtr().getPstlAdr().setCtry(null);
             }
      			 
			}
			Party6Choice Id = Dbtr.getId();
			if(Id != null && pmtInfChilds.containsKey(TagConstants.DBTR_ID)) {
			  if(!pmtInfChilds.containsKey(TagConstants.DBTR_ORGID)) {
				 	PmtInf.getDbtr().getId().setOrgId(null);
				}
				
				if(!pmtInfChilds.containsKey(TagConstants.DBTR_PRVTID)) {
					PmtInf.getDbtr().getId().setPrvtId(null);
				}
			}else if(!pmtInfChilds.containsKey(TagConstants.DBTR_ID)) {
				PmtInf.getDbtr().setId(null);
			}
			
			String CtryOfRes = Dbtr.getCtryOfRes();
			if(CtryOfRes != null && !pmtInfChilds.containsKey(TagConstants.DBTR_CTGRYRES)) {
				PmtInf.getDbtr().setCtryOfRes(null);
			}
			
			ContactDetails2 CtctDtls = Dbtr.getCtctDtls();
			if(CtctDtls != null && !pmtInfChilds.containsKey(TagConstants.DBTR_CNTCT_DTLS)) {
				PmtInf.getDbtr().setCtctDtls(null);
			}
		}
		logger.info("Debtor getDbtr() End...");
	}
	
}
