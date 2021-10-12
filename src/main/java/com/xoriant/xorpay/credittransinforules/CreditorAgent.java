package com.xoriant.xorpay.credittransinforules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.pojo001.BranchAndFinancialInstitutionIdentification4;
import com.xoriant.xorpay.pojo001.CashAccount16;
import com.xoriant.xorpay.pojo001.ClearingSystemMemberIdentification2;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;
import com.xoriant.xorpay.pojo001.PostalAddress6;
import com.xoriant.xorpay.util.CustomTagPstlAdr;

@Service
public class CreditorAgent {
	
	private static final Logger logger = LoggerFactory.getLogger(CreditorAgent.class);

	public void getCdtrAgt(CreditTransferTransactionInformation10 CdtTrfTxInf, Map<String, String> pmtInfChilds) {
		
		logger.info("CreditorAgent getCdtrAgt Start...");
		
		BranchAndFinancialInstitutionIdentification4 CdtrAgt = CdtTrfTxInf.getCdtrAgt();

		List<String> cdtrAddrList = new ArrayList<String>();
		List<String> cdtrUpdatedAddrList = new ArrayList<String>();
		CustomTagPstlAdr addrlCustom = new CustomTagPstlAdr();

		if (CdtrAgt != null && pmtInfChilds.containsKey(TagConstants.CDTR_AGNT)) {

			if (CdtrAgt.getFinInstnId() != null && pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_FINST_ID)) {

				ClearingSystemMemberIdentification2 ClrSysMmbId = CdtrAgt.getFinInstnId().getClrSysMmbId();
				if (ClrSysMmbId != null && !pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_FINST_CLRSYS_MMBID)) {
					CdtTrfTxInf.getCdtrAgt().getFinInstnId().setClrSysMmbId(null);
				}

				String BIC = CdtrAgt.getFinInstnId().getBIC();
				if (BIC != null && !pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_FINST_ID_BIC)) {
					CdtTrfTxInf.getCdtrAgt().getFinInstnId().setBIC(null);
				}

				String Name = CdtrAgt.getFinInstnId().getNm();
				if (Name != null && !pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_FINST_ID_NM)) {
					CdtTrfTxInf.getCdtrAgt().getFinInstnId().setNm(null);
				}

				PostalAddress6 finstpostalAddress = CdtrAgt.getFinInstnId().getPstlAdr();

				if (finstpostalAddress != null && !pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_FINST_ID_PSTL_ADR)) {
					CdtTrfTxInf.getCdtrAgt().getFinInstnId().setPstlAdr(null);
				} else if (pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_FINST_ID_PSTL_ADR)) {
					if (pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_FINST_ID_PSTL_ADRLINE)
							&& pmtInfChilds.containsValue(TagConstants.MAX_LENGTH)) {
						cdtrAddrList = finstpostalAddress.getAdrLine();
						addrlCustom.trimAddrLine35Char(cdtrAddrList, finstpostalAddress, cdtrUpdatedAddrList);
					} else if (!pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_FINST_ID_PSTL_ADRLINE)) {
						finstpostalAddress.getAdrLine().clear();
					}
					if (CdtrAgt.getFinInstnId().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_FINST_ID_PSTL_CODE)) {
						CdtTrfTxInf.getCdtrAgt().getFinInstnId().getPstlAdr().setPstCd(null);
					}
					if (CdtrAgt.getFinInstnId().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_FINST_ID_PSTL_TOWN_NAME)) {
						CdtTrfTxInf.getCdtrAgt().getFinInstnId().getPstlAdr().setTwnNm(null);
					}
					if (CdtrAgt.getFinInstnId().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_FINST_ID_PSTL_COUNTRY_SUBD)) {
						CdtTrfTxInf.getCdtrAgt().getFinInstnId().getPstlAdr().setCtrySubDvsn(null);
					}
					if (CdtrAgt.getFinInstnId().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_FINST_ID_PSTL_COUNTRY)) {
					  CdtTrfTxInf.getCdtrAgt().getFinInstnId().getPstlAdr().setCtry(null);
					}
				}

			} else if (!pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_FINST_ID)) {
				CdtTrfTxInf.getCdtrAgt().setFinInstnId(null);
			}

			if (CdtrAgt.getBrnchId() != null && !pmtInfChilds.containsKey(TagConstants.CDTR_AGNT_BRANCHID)) {
				CdtTrfTxInf.getCdtrAgt().setBrnchId(null);
			}
		} else if (!pmtInfChilds.containsKey(TagConstants.CDTR_AGNT)) {
			CdtTrfTxInf.setCdtrAgt(null);
		}
		
		logger.info("CreditorAgent getCdtrAgt End...");

	}

	public void getCdtrAgtAcct(CreditTransferTransactionInformation10 CdtTrfTxInf, Map pmtInfChilds) {

		CashAccount16 CdtrAgtAcct = CdtTrfTxInf.getCdtrAgtAcct();

		if (CdtrAgtAcct != null && pmtInfChilds.containsKey(TagConstants.CDTRAGNT_ACCNT)) {

			if (CdtrAgtAcct.getTp() != null && pmtInfChilds.containsKey(TagConstants.CDTRAGNT_ACCNT_TP)) {

				if (CdtrAgtAcct.getTp().getCd() != null
						&& !pmtInfChilds.containsKey(TagConstants.CDTRAGNT_ACCNT_TP_CD)) {
					CdtTrfTxInf.getCdtrAgtAcct().getTp().setCd(null);
				}
				if (CdtrAgtAcct.getTp().getPrtry() != null
						&& !pmtInfChilds.containsKey(TagConstants.CDTRAGNT_ACCNT_PRTRY)) {
					CdtTrfTxInf.getCdtrAgtAcct().getTp().setPrtry(null);
				}
			} else if (!pmtInfChilds.containsKey(TagConstants.CDTRAGNT_ACCNT_TP)) {
				CdtTrfTxInf.getCdtrAgtAcct().setTp(null);
			}

			if (CdtrAgtAcct.getId() != null && pmtInfChilds.containsKey(TagConstants.CDTRAGNT_ACCNT_ID)) {

				if (CdtrAgtAcct.getId().getIBAN() != null
						&& !pmtInfChilds.containsKey(TagConstants.CDTRAGNT_ACCNT_ID_IBAN)) {
					CdtTrfTxInf.getCdtrAgtAcct().getId().setIBAN(null);
				}
				if (CdtrAgtAcct.getId().getOthr() != null
						&& !pmtInfChilds.containsKey(TagConstants.CDTRAGNT_ACCNT_ID_OTHR)) {
					CdtTrfTxInf.getCdtrAgtAcct().getId().setOthr(null);
				}
			}
		} else if (!pmtInfChilds.containsKey(TagConstants.CDTRAGNT_ACCNT)) {
			CdtTrfTxInf.setCdtrAgtAcct(null);
		}
	}
}
