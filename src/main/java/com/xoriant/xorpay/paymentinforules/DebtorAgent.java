package com.xoriant.xorpay.paymentinforules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.pojo001.BranchAndFinancialInstitutionIdentification4;
import com.xoriant.xorpay.pojo001.PaymentInstructionInformation3;
import com.xoriant.xorpay.pojo001.PostalAddress6;
import com.xoriant.xorpay.util.CustomTagPstlAdr;
import com.xoriant.xorpay.constants.TagConstants;

@Service
public class DebtorAgent {
  
  private static final Logger logger = LoggerFactory.getLogger(DebtorAgent.class);
	
	public void getDbtrAgt(PaymentInstructionInformation3 PmtInf,Map<String,String> pmtInfChilds) {
		BranchAndFinancialInstitutionIdentification4 DbtrAgt = PmtInf.getDbtrAgt();
		
		logger.info("DebtorAgent getDbtrAgt() Started...");
		List<String> dbtrAgtAddrList = new ArrayList<String>();
		List<String> dbtrAgtUpdatedAddrList = new ArrayList<String>();
		CustomTagPstlAdr addrlCustom = new CustomTagPstlAdr();
		
		if(DbtrAgt != null && pmtInfChilds.containsKey(TagConstants.DBTR_AGNT)) {
			
				if(DbtrAgt.getFinInstnId() != null && pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID)) {
				  
				  if(DbtrAgt.getFinInstnId().getBIC() != null && !pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID_BIC)) {
				    PmtInf.getDbtrAgt().getFinInstnId().setBIC(null);
	        }
	    	  
	        if(DbtrAgt.getFinInstnId().getClrSysMmbId() != null && !pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID_CLRSYS_MMBID)) {
	          PmtInf.getDbtrAgt().getFinInstnId().setClrSysMmbId(null);
	        }

				  
				  if(DbtrAgt.getFinInstnId().getNm() != null && !pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID_NAME)) {
						PmtInf.getDbtrAgt().getFinInstnId().setNm(null);
					}
					
					/*if(DbtrAgt.getFinInstnId().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID_NAME)) {
						PmtInf.getDbtrAgt().getFinInstnId().setPstlAdr(null);
					}*/
					
					if(DbtrAgt.getFinInstnId().getOthr() != null && !pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID_OTHR)) {
						PmtInf.getDbtrAgt().getFinInstnId().setOthr(null);
					}
					
					PostalAddress6 dbtrAgntPstlAddr = DbtrAgt.getFinInstnId().getPstlAdr();
					
					if (dbtrAgntPstlAddr != null && !pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID_PSTL_ADR)) {
						PmtInf.getDbtrAgt().getFinInstnId().setPstlAdr(null);
					} else if (pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID_PSTL_ADR)) {
						if (pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID_PSTL_ADRLINE)
								&& pmtInfChilds.containsValue(TagConstants.MAX_LENGTH)) {
							dbtrAgtAddrList = dbtrAgntPstlAddr.getAdrLine();
							addrlCustom.trimAddrLine35Char(dbtrAgtAddrList, dbtrAgntPstlAddr, dbtrAgtUpdatedAddrList);
						} else if (dbtrAgntPstlAddr != null && !pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID_PSTL_ADRLINE)) {
							dbtrAgntPstlAddr.getAdrLine().clear();
						}
						if (DbtrAgt.getFinInstnId().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID_PSTL_CODE)) {
							PmtInf.getDbtrAgt().getFinInstnId().getPstlAdr().setPstCd(null);
						}
						if (DbtrAgt.getFinInstnId().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID_PSTL_TOWN_NAME)) {
							PmtInf.getDbtrAgt().getFinInstnId().getPstlAdr().setTwnNm(null);
						}
						if (DbtrAgt.getFinInstnId().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID_PSTL_COUNTRY_SUBD)) {
							PmtInf.getDbtrAgt().getFinInstnId().getPstlAdr().setCtrySubDvsn(null);
						}
						if (DbtrAgt.getFinInstnId().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID_PSTL_COUNTRY)) {
							PmtInf.getDbtrAgt().getFinInstnId().getPstlAdr().setCtry(null);
						}
					}
					
					
				}else if (!pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_FINST_ID)) {
					PmtInf.getDbtrAgt().setFinInstnId(null);
				}
			
			
			if(DbtrAgt.getBrnchId() != null && !pmtInfChilds.containsKey(TagConstants.DBTR_AGNT_BRANCH_ID)) {
				PmtInf.getDbtrAgt().setBrnchId(null);
			}
		} else if(!pmtInfChilds.containsKey(TagConstants.DBTR_AGNT)) {
		  PmtInf.setDbtrAgt(null);
		}
		logger.info("DebtorAgent getDbtrAgt() Ended...");
	}
	
}
