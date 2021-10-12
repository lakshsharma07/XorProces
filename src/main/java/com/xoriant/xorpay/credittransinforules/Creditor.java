package com.xoriant.xorpay.credittransinforules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.pojo001.ContactDetails2;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;
import com.xoriant.xorpay.pojo001.Party6Choice;
import com.xoriant.xorpay.pojo001.PartyIdentification32;
import com.xoriant.xorpay.pojo001.PostalAddress6;
import com.xoriant.xorpay.util.CustomTagPstlAdr;

@Service
public class Creditor {

	private static final Logger logger = LoggerFactory.getLogger(Creditor.class);
	CustomTagPstlAdr addrlCustom = new CustomTagPstlAdr();

	public void getCdtr(CreditTransferTransactionInformation10 CdtTrfTxInf, Map<String, String> pmtInfChilds) {

		PartyIdentification32 Cdtr = CdtTrfTxInf.getCdtr();
		logger.info("Creditor getCdtr() Start...");

		List<String> crdtrAddrList = new ArrayList<String>();
		List<String> crdtrUpdatedAddrList = new ArrayList<String>();
		CustomTagPstlAdr addrlCustom = new CustomTagPstlAdr();

		if (Cdtr != null) {

			// This is mandatory, will always come
			// Cdtr.getNm();

			PostalAddress6 postalAddress = Cdtr.getPstlAdr();
			if (postalAddress != null && !pmtInfChilds.containsKey(TagConstants.CDTR_PSTLADR)) {
				CdtTrfTxInf.getCdtr().setPstlAdr(null);
			} else if(pmtInfChilds.containsKey(TagConstants.CDTR_PSTLADR)) { 
	    if(pmtInfChilds.containsKey(TagConstants.CDTR_PSTLADR_ADRLINE) && pmtInfChilds.containsValue(TagConstants.MAX_LENGTH)) {
        	crdtrAddrList = postalAddress.getAdrLine();
          addrlCustom.trimAddrLine35Char(crdtrAddrList,postalAddress,crdtrUpdatedAddrList);  
        } else if(!pmtInfChilds.containsKey(TagConstants.CDTR_PSTLADR_ADRLINE)) {
          postalAddress.getAdrLine().clear();
        }
         if(!pmtInfChilds.containsKey(TagConstants.CDTR_PSTL_CODE)) {
           CdtTrfTxInf.getCdtr().getPstlAdr().setPstCd(null);
         }
         if(!pmtInfChilds.containsKey(TagConstants.CDTR_TOWN_NAME)) {
           CdtTrfTxInf.getCdtr().getPstlAdr().setTwnNm(null);
         }
         if(!pmtInfChilds.containsKey(TagConstants.CDTR_COUNTRY_SUBD)) {
           CdtTrfTxInf.getCdtr().getPstlAdr().setCtrySubDvsn(null);
         }
         if(!pmtInfChilds.containsKey(TagConstants.CDTR_COUNTRY)) {
           CdtTrfTxInf.getCdtr().getPstlAdr().setCtry(null);
         }
         
  } 
			
			/*
				 * else if(pmtInfChilds.containsKey(TagConstants.CDTR_PSTLADR_ADRLINE) &&
				 * pmtInfChilds.containsValue(TagConstants.MAX_LENGTH)) { addrList =
				 * postalAddress.getAdrLine();
				 * addrlCustom.trimAddrLine(addrList,postalAddress,updatedAddrList); }
				 */

			Party6Choice Id = Cdtr.getId();
			if (Id != null && pmtInfChilds.containsKey(TagConstants.CDTR_ID)) {
				if (!pmtInfChilds.containsKey(TagConstants.CDTR_ORGID)) {
					CdtTrfTxInf.getCdtr().getId().setOrgId(null);
				}

				if (!pmtInfChilds.containsKey(TagConstants.CDTR_PRVTID)) {
					CdtTrfTxInf.getCdtr().getId().setPrvtId(null);
				}
			} else if (!pmtInfChilds.containsKey(TagConstants.CDTR_ID)) {
				CdtTrfTxInf.getCdtr().setId(null);
			}

			String CtryOfRes = Cdtr.getCtryOfRes();
			if (CtryOfRes != null && !pmtInfChilds.containsKey(TagConstants.CDTR_CTGRYRES)) {
				CdtTrfTxInf.getCdtr().setCtryOfRes(null);
			}

			ContactDetails2 CtctDtls = Cdtr.getCtctDtls();
			if (CtctDtls != null && !pmtInfChilds.containsKey(TagConstants.CDTR_CNTCT_DTLS)) {
				CdtTrfTxInf.getCdtr().setCtctDtls(null);
			}
		}
	}

	public void getUltmtCdtr(CreditTransferTransactionInformation10 CdtTrfTxInf,
			Map<String, String> pmtInfChilds) {

		PartyIdentification32 UltmtCdtr = CdtTrfTxInf.getUltmtCdtr();

		if (UltmtCdtr != null && pmtInfChilds.containsKey(TagConstants.ULTM_CDTR)) {

			// This is mandatory, will always come
			// Cdtr.getNm();

			if (UltmtCdtr.getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.ULTM_CDTR_PSTLADR)) {
				CdtTrfTxInf.getUltmtCdtr().setPstlAdr(null);
			}

			if (UltmtCdtr.getId() != null && pmtInfChilds.containsKey(TagConstants.ULTM_CDTR_ID)) {
				if (!pmtInfChilds.containsKey(TagConstants.ULTM_CDTR_ORGID)) {
					CdtTrfTxInf.getUltmtCdtr().getId().setOrgId(null);
				}

				if (!pmtInfChilds.containsKey(TagConstants.ULTM_CDTR_PRVTID)) {
					CdtTrfTxInf.getUltmtCdtr().getId().setPrvtId(null);
				}
			} else if (!pmtInfChilds.containsKey(TagConstants.ULTM_CDTR_ID)) {
				CdtTrfTxInf.getUltmtCdtr().setId(null);
			}

			if (UltmtCdtr.getCtryOfRes() != null && !pmtInfChilds.containsKey(TagConstants.ULTM_CDTR_CTGRYRES)) {
				CdtTrfTxInf.getUltmtCdtr().setCtryOfRes(null);
			}

			if (UltmtCdtr.getCtctDtls() != null && !pmtInfChilds.containsKey(TagConstants.ULTM_CDTR_CNTCT_DTLS)) {
				CdtTrfTxInf.getUltmtCdtr().setCtctDtls(null);
			}
		} else if (!pmtInfChilds.containsKey(TagConstants.ULTM_CDTR)) {
			CdtTrfTxInf.setUltmtCdtr(null);
		}
	}
}
