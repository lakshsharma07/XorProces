package com.xoriant.xorpay.credittransinforules;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;

@Service
public class InstructionForDebtorAgent {
	
	public void getInstrForDbtrAgt(CreditTransferTransactionInformation10 CdtTrfTxInf, Map<String,String> pmtInfChilds) {
		String InstrForDbtrAgt = CdtTrfTxInf.getInstrForDbtrAgt();
		if(InstrForDbtrAgt != null && !pmtInfChilds.containsKey(TagConstants.INSTR_DBTRAGNT)) {
			CdtTrfTxInf.setInstrForDbtrAgt(null);
		}
	}

}
