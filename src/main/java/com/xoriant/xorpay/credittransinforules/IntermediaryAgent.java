package com.xoriant.xorpay.credittransinforules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.pojo001.BranchAndFinancialInstitutionIdentification4;
import com.xoriant.xorpay.pojo001.CashAccount16;
import com.xoriant.xorpay.pojo001.ClearingSystemMemberIdentification2;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;
import com.xoriant.xorpay.pojo001.PostalAddress6;
import com.xoriant.xorpay.util.CustomTagPstlAdr;

@Service
public class IntermediaryAgent {

	public void getIntrmyAgt1(CreditTransferTransactionInformation10 CdtTrfTxInf, Map<String,String> pmtInfChilds) {
		
		BranchAndFinancialInstitutionIdentification4 IntrmyAgt = CdtTrfTxInf.getIntrmyAgt1();
		
		List<String> intrmAgntAddrList = new ArrayList<String>();
    List<String> intrmAgntUpdatedAddrList = new ArrayList<String>();
    CustomTagPstlAdr addrlCustom = new CustomTagPstlAdr();
		
		if(IntrmyAgt != null && pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1)) {
			
			if(IntrmyAgt.getFinInstnId() != null && pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_FINST)) {
				
				ClearingSystemMemberIdentification2 ClrSysMmbId = IntrmyAgt.getFinInstnId().getClrSysMmbId();
				if(ClrSysMmbId != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_FINST_CLRSYS_MMBID)) {
					CdtTrfTxInf.getIntrmyAgt1().getFinInstnId().setClrSysMmbId(null);
				}
				
				String BIC = IntrmyAgt.getFinInstnId().getBIC();
				if(BIC != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_FINST_BIC)) {
					CdtTrfTxInf.getIntrmyAgt1().getFinInstnId().setBIC(null);
				}
				
				PostalAddress6 finstpostalAddress = IntrmyAgt.getFinInstnId().getPstlAdr();
				
				if (finstpostalAddress != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_FINST_ID_PSTL_ADR)) {
          CdtTrfTxInf.getIntrmyAgt1().getFinInstnId().setPstlAdr(null);
        } else if (pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_FINST_ID_PSTL_ADR)) {
          if (pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_FINST_ID_PSTL_ADRLINE)
              && pmtInfChilds.containsValue(TagConstants.MAX_LENGTH)) {
            intrmAgntAddrList = finstpostalAddress.getAdrLine();
            addrlCustom.trimAddrLine35Char(intrmAgntAddrList, finstpostalAddress, intrmAgntUpdatedAddrList);
          } else if (!pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_FINST_ID_PSTL_ADRLINE)) {
            finstpostalAddress.getAdrLine().clear();
          }
          if (IntrmyAgt.getFinInstnId().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_FINST_ID_PSTL_CODE)) {
            CdtTrfTxInf.getIntrmyAgt1().getFinInstnId().getPstlAdr().setPstCd(null);
          }
          if (IntrmyAgt.getFinInstnId().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_FINST_ID_PSTL_TOWN_NAME)) {
            CdtTrfTxInf.getIntrmyAgt1().getFinInstnId().getPstlAdr().setTwnNm(null);
          }
          if (IntrmyAgt.getFinInstnId().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_FINST_ID_PSTL_COUNTRY_SUBD)) {
            CdtTrfTxInf.getIntrmyAgt1().getFinInstnId().getPstlAdr().setCtrySubDvsn(null);
          }
          if (IntrmyAgt.getFinInstnId().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_FINST_ID_PSTL_COUNTRY)) {
            CdtTrfTxInf.getIntrmyAgt1().getFinInstnId().getPstlAdr().setCtry(null);
          }
        }
				
				
			}else if(!pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_FINST)) {
				CdtTrfTxInf.getIntrmyAgt1().setFinInstnId(null);
			}
			
			if(IntrmyAgt.getBrnchId() != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_BRNCHID)) {
				CdtTrfTxInf.getIntrmyAgt1().setBrnchId(null);
			}
		}else if(!pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1)) {
			CdtTrfTxInf.setIntrmyAgt1(null);
		}
		
	}
	
	public void getIntrmyAgt2(CreditTransferTransactionInformation10 CdtTrfTxInf, Map pmtInfChilds) {
		
		BranchAndFinancialInstitutionIdentification4 IntrmyAgt = CdtTrfTxInf.getIntrmyAgt2();
		
		if(IntrmyAgt != null && pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2)) {
			
			if(IntrmyAgt.getFinInstnId() != null && pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_FINST)) {
				
				ClearingSystemMemberIdentification2 ClrSysMmbId = IntrmyAgt.getFinInstnId().getClrSysMmbId();
				if(ClrSysMmbId != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_FINST_CLRSYS_MMBID)) {
					CdtTrfTxInf.getIntrmyAgt2().getFinInstnId().setClrSysMmbId(null);
				}
				
				String BIC = IntrmyAgt.getFinInstnId().getBIC();
				if(BIC != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_FINST_BIC)) {
					CdtTrfTxInf.getIntrmyAgt2().getFinInstnId().setBIC(null);
				}
				
			}else if(!pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_FINST)) {
				CdtTrfTxInf.getIntrmyAgt2().setFinInstnId(null);
			}
			
			if(IntrmyAgt.getBrnchId() != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_BRNCHID)) {
				CdtTrfTxInf.getIntrmyAgt2().setBrnchId(null);
			}
		}else if(!pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2)) {
			CdtTrfTxInf.setIntrmyAgt2(null);
		}	
	}
	
	public void getIntrmyAgtAcct2(CreditTransferTransactionInformation10 CdtTrfTxInf, Map pmtInfChilds) {
		
		CashAccount16 IntrmyAgt2Acct = CdtTrfTxInf.getIntrmyAgt2Acct();
		
		if(IntrmyAgt2Acct != null && pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_ACCNT)) {
			
			if(IntrmyAgt2Acct.getTp() != null && pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_ACCNT_TP)) {
				
				if(IntrmyAgt2Acct.getTp().getCd() != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_ACCNT_TP_CD)) {
					CdtTrfTxInf.getIntrmyAgt2Acct().getTp().setCd(null);
				}
				if(IntrmyAgt2Acct.getTp().getPrtry() != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_ACCNT_PRTRY)) {
					CdtTrfTxInf.getIntrmyAgt2Acct().getTp().setPrtry(null);
				}
			}else if(!pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_ACCNT_TP)){
				CdtTrfTxInf.getIntrmyAgt2Acct().setTp(null);
			}
			
			if(IntrmyAgt2Acct.getId() != null && pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_ACCNT_ID)) {
				
				if(IntrmyAgt2Acct.getId().getIBAN() != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_ACCNT_ID_IBAN)) {
					CdtTrfTxInf.getIntrmyAgt2Acct().getId().setIBAN(null);
				}
				if(IntrmyAgt2Acct.getId().getOthr()!= null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_ACCNT_ID_OTHR)) {
					CdtTrfTxInf.getIntrmyAgt2Acct().getId().setOthr(null);
				}
			}
		}else if(!pmtInfChilds.containsKey(TagConstants.INTRM_AGNT2_ACCNT)){
			CdtTrfTxInf.setIntrmyAgt2Acct(null);
		}
	}
	
	public void getIntrmyAgtAcct1(CreditTransferTransactionInformation10 CdtTrfTxInf, Map pmtInfChilds) {
		
		CashAccount16 IntrmyAgt1Acct = CdtTrfTxInf.getIntrmyAgt1Acct();
		
		if(IntrmyAgt1Acct != null && pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_ACCNT)) {
			
			if(IntrmyAgt1Acct.getTp() != null && pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_ACCNT_TP)) {
				
				if(IntrmyAgt1Acct.getTp().getCd() != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_ACCNT_TP_CD)) {
					CdtTrfTxInf.getIntrmyAgt1Acct().getTp().setCd(null);
				}
				if(IntrmyAgt1Acct.getTp().getPrtry() != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_ACCNT_PRTRY)) {
					CdtTrfTxInf.getIntrmyAgt1Acct().getTp().setPrtry(null);
				}
			}else if(!pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_ACCNT_TP)){
				CdtTrfTxInf.getIntrmyAgt1Acct().setTp(null);
			}
			
			if(IntrmyAgt1Acct.getId() != null && pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_ACCNT_ID)) {

				if(IntrmyAgt1Acct.getId().getIBAN() != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_ACCNT_ID_IBAN)) {
					CdtTrfTxInf.getIntrmyAgt1Acct().getId().setIBAN(null);
				}
				if(IntrmyAgt1Acct.getId().getOthr()!= null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_ACCNT_ID_OTHR)) {
					CdtTrfTxInf.getIntrmyAgt1Acct().getId().setOthr(null);
				}
			}
			
			if(IntrmyAgt1Acct.getNm() != null && !pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_ACCNT_NM)) {
					CdtTrfTxInf.getIntrmyAgt1Acct().setNm(null);
			}
			
		}else if(!pmtInfChilds.containsKey(TagConstants.INTRM_AGNT1_ACCNT)){
			CdtTrfTxInf.setIntrmyAgt1Acct(null);
		}
	}
}
