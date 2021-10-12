package com.xoriant.xorpay.credittransinforules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;
import com.xoriant.xorpay.pojo001.InstructionForCreditorAgent1;

@Service
public class InstructionForCreditorAgent {
	
	public void getInstrForCdtrAgt(CreditTransferTransactionInformation10 CdtTrfTxInf, Map<String,String> pmtInfChilds) {
		List<InstructionForCreditorAgent1> InstrForCdtrAgtList = CdtTrfTxInf.getInstrForCdtrAgt();
		List<InstructionForCreditorAgent1> updatedInstrForCdtrAgtList = new ArrayList<InstructionForCreditorAgent1>();
		
		for(InstructionForCreditorAgent1 InstrForCdtrAgt : InstrForCdtrAgtList) {
			if(InstrForCdtrAgt.getCd() != null && !pmtInfChilds.containsKey(TagConstants.INSTR_CDTRAGNT_CD)) {
				InstrForCdtrAgt.setCd(null);
			}
			if(InstrForCdtrAgt.getInstrInf() != null && !pmtInfChilds.containsKey(TagConstants.INSTR_CDTRAGNT_INSTRINF)) {
				InstrForCdtrAgt.setInstrInf(null);
			}
			updatedInstrForCdtrAgtList.add(InstrForCdtrAgt);
		}
		CdtTrfTxInf.getInstrForCdtrAgt().clear();
		CdtTrfTxInf.getInstrForCdtrAgt().addAll(updatedInstrForCdtrAgtList);

	}
}
