package com.xoriant.xorpay.credittransinforules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;
import com.xoriant.xorpay.pojo001.StructuredRemittanceInformation7;

@Service
public class RemittanceInformation {
	
	public void getRmtInf(CreditTransferTransactionInformation10 CdtTrfTxInf, Map<String,String> pmtInfChilds) {
		
		if(CdtTrfTxInf.getRmtInf() != null && pmtInfChilds.containsKey(TagConstants.RMT_INF)) {
			if(CdtTrfTxInf.getRmtInf().getStrd() != null && pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD)) {
				List<StructuredRemittanceInformation7> strdList = CdtTrfTxInf.getRmtInf().getStrd(); 
				List<StructuredRemittanceInformation7> updatedStrdList = new ArrayList<StructuredRemittanceInformation7>();
				for(StructuredRemittanceInformation7 strd : strdList) {
					if(strd.getAddtlRmtInf() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_ADDL_RMTINF)) {
						strd.getAddtlRmtInf().clear();
					}
					
					if(strd.getCdtrRefInf() != null && pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_CDTR_REFINF)) {
						if(strd.getCdtrRefInf().getRef() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_CDTR_REFINF_REF)){
							strd.getCdtrRefInf().setRef(null);
						}
						
						if(strd.getCdtrRefInf().getTp() != null && pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_CDTR_REFINF_TP)){
							if(strd.getCdtrRefInf().getTp().getCdOrPrtry() != null && pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_CDTR_REFINF_TP_CD_PRTRY)) {
								strd.getCdtrRefInf().getTp().setCdOrPrtry(null);
							}
							
							if(strd.getCdtrRefInf().getTp().getIssr() != null && pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_CDTR_REFINF_TP_ISSR)) {
								strd.getCdtrRefInf().getTp().setIssr(null);
							}
							
						}else if(!pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_CDTR_REFINF_TP)) {
							strd.getCdtrRefInf().setTp(null);
						}
					} else if (!pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_CDTR_REFINF)) {
						strd.setCdtrRefInf(null);
					}
					
					if(strd.getInvcee() != null && pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_INVCE)) {
						if(strd.getInvcee().getCtctDtls() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_INVCE_CNTCT_DTLS)) {
							strd.getInvcee().setCtctDtls(null);
						}
						
						if(strd.getInvcee().getCtryOfRes() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_INVCE_CNTRYRES)) {
							strd.getInvcee().setCtryOfRes(null);
						}
						
						if(strd.getInvcee().getId() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_INVCE_ID)) {
							strd.getInvcee().setId(null);
						}
						
						if(strd.getInvcee().getNm() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_INVCE_NM)) {
							strd.getInvcee().setNm(null);
						}
	
						if(strd.getInvcee().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_INVCE_PSTLADR)) {
							strd.getInvcee().setPstlAdr(null);
						}
					}
					
					if(strd.getInvcr() != null && pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_INVCR)) {
						if(strd.getInvcr().getCtctDtls() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_INVCR_CNTCT_DTLS)) {
							strd.getInvcr().setCtctDtls(null);
						}
						
						if(strd.getInvcr().getCtryOfRes() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_INVCR_CNTCT_CNTRYRES)) {
							strd.getInvcr().setCtryOfRes(null);
						}
						
						if(strd.getInvcr().getId() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_INVCR_ID)) {
							strd.getInvcr().setId(null);
						}
						
						if(strd.getInvcr().getNm() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_INVCR_NM)) {
							strd.getInvcr().setNm(null);
						}
	
						if(strd.getInvcr().getPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_INVCR_PSTLADR)) {
							strd.getInvcr().setPstlAdr(null);
						}
						
					}else if(!pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_INVCR)) {
						strd.setInvcr(null);
					}
					
					if(strd.getRfrdDocAmt() != null && pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_RFRDOCAMT)) {
						if(strd.getRfrdDocAmt().getAdjstmntAmtAndRsn() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_RFRDOCAMT_ADJST_AMNT_RSN)) {
							strd.getRfrdDocAmt().getAdjstmntAmtAndRsn().clear();
						}
						
						if(strd.getRfrdDocAmt().getCdtNoteAmt() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_RFRDOCAMT_CDTNOTEAMNT)) {
							strd.getRfrdDocAmt().setCdtNoteAmt(null);
						}
						
						if(strd.getRfrdDocAmt().getDscntApldAmt() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_RFRDOCAMT_DSCNTAMNT)) {
							strd.getRfrdDocAmt().setDscntApldAmt(null);
						}
						
						if(strd.getRfrdDocAmt().getDuePyblAmt() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_RFRDOCAMT_DUEPAYBAMNT)) {
							strd.getRfrdDocAmt().setDuePyblAmt(null);
						}
						
						if(strd.getRfrdDocAmt().getRmtdAmt() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_RFRDOCAMT_RMTDAMNT)) {
							strd.getRfrdDocAmt().setRmtdAmt(null);
						}
						
						if(strd.getRfrdDocAmt().getTaxAmt() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_RFRDOCAMT_TAXAMNT)) {
							strd.getRfrdDocAmt().setTaxAmt(null);
						}
						
					}else if (!pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_RFRDOCAMT)) {
						strd.setRfrdDocAmt(null);
					}
	
					if(strd.getRfrdDocInf() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD_RFRDOCINF)) {
						strd.getRfrdDocInf().clear();
					}
					updatedStrdList.add(strd);
				}
				
				CdtTrfTxInf.getRmtInf().getStrd().clear();
				CdtTrfTxInf.getRmtInf().getStrd().addAll(updatedStrdList);
				
			}else if (!pmtInfChilds.containsKey(TagConstants.RMT_INF_STRD)) {
				CdtTrfTxInf.getRmtInf().getStrd().clear();
			}
			
			if(CdtTrfTxInf.getRmtInf().getUstrd() != null && !pmtInfChilds.containsKey(TagConstants.RMT_INF_USTRD)) {
				CdtTrfTxInf.getRmtInf().getUstrd().clear();
			}
		}else if(!pmtInfChilds.containsKey(TagConstants.RMT_INF
		    )) {
			CdtTrfTxInf.setRmtInf(null);
		}
	} 
	
}
