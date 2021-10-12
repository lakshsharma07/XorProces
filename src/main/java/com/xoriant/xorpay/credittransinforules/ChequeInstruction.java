package com.xoriant.xorpay.credittransinforules;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.pojo001.Cheque6;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;



@Service
public class ChequeInstruction {
	
	public void getChqInstr(CreditTransferTransactionInformation10 CdtTrfTxInf, Map<String,String> pmtInfChilds) {
		Cheque6 ChqInstr = CdtTrfTxInf.getChqInstr();
		if(ChqInstr != null && pmtInfChilds.containsKey(TagConstants.CHK_INSTR)) {
			
		  if(ChqInstr.getChqTp() != null && !pmtInfChilds.containsKey(TagConstants.CHK_TP)) {
				CdtTrfTxInf.getChqInstr().setChqTp(null);
			}
			
			
			if(ChqInstr.getChqNb() != null && !pmtInfChilds.containsKey(TagConstants.CHK_NB)) {
				CdtTrfTxInf.getChqInstr().setChqNb(null);
			}
			
			if(ChqInstr.getChqFr() != null && !pmtInfChilds.containsKey(TagConstants.CHK_FREQ)) {
				CdtTrfTxInf.getChqInstr().setChqFr(null);
			}
			
			if(ChqInstr.getDlvryMtd() != null && !pmtInfChilds.containsKey(TagConstants.CHK_DLVR_MTD)) {
				CdtTrfTxInf.getChqInstr().setDlvryMtd(null);
			}
			
			if(ChqInstr.getDlvrTo() != null && !pmtInfChilds.containsKey(TagConstants.CHK_DLVR_TO)) {
				CdtTrfTxInf.getChqInstr().setDlvrTo(null);
			}
			
			if(ChqInstr.getInstrPrty() != null && !pmtInfChilds.containsKey(TagConstants.CHK_INSTR_PRTY)) {
				CdtTrfTxInf.getChqInstr().setInstrPrty(null);
			}
			
			if(ChqInstr.getChqMtrtyDt() != null && !pmtInfChilds.containsKey(TagConstants.CHK_MTRTY_DATE)) {
				CdtTrfTxInf.getChqInstr().setChqMtrtyDt(null);
			}
			
			if(ChqInstr.getFrmsCd() != null && !pmtInfChilds.containsKey(TagConstants.CHK_FRMS_CD)) {
				CdtTrfTxInf.getChqInstr().setFrmsCd(null);
			}
			
			if(ChqInstr.getMemoFld() != null && !pmtInfChilds.containsKey(TagConstants.CHK_MEMO_FLD)) {
				CdtTrfTxInf.getChqInstr().getMemoFld().clear();
			}
			
			if(ChqInstr.getRgnlClrZone() != null && !pmtInfChilds.containsKey(TagConstants.CHK_RGNL_CLR_ZNE)) {
				CdtTrfTxInf.getChqInstr().setRgnlClrZone(null);
			}
			
			if(ChqInstr.getPrtLctn() != null && !pmtInfChilds.containsKey(TagConstants.CHK_PRNT_LCTN)) {
				CdtTrfTxInf.getChqInstr().setPrtLctn(null);
			}
			
		}else if(!pmtInfChilds.containsKey(TagConstants.CHK_INSTR)) {
			CdtTrfTxInf.setChqInstr(null);
		}
	}
}
