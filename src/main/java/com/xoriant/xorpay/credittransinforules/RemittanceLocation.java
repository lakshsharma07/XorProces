package com.xoriant.xorpay.credittransinforules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;
import com.xoriant.xorpay.pojo001.RemittanceLocation2;

@Service
public class RemittanceLocation {
	
	public void getRltdRmtInf (CreditTransferTransactionInformation10 CdtTrfTxInf, Map<String,String> pmtInfChilds) {
		
		if(CdtTrfTxInf.getRltdRmtInf() != null && pmtInfChilds.containsKey(TagConstants.RLTD_RMTINF)) {
			List<RemittanceLocation2> rltdRmtInfList = CdtTrfTxInf.getRltdRmtInf();
			List<RemittanceLocation2> updatedRltdRmtInfList = new ArrayList<RemittanceLocation2>();
			
			for(RemittanceLocation2 rltdRmtInf : rltdRmtInfList) {
				if(rltdRmtInf.getRmtId() != null && !pmtInfChilds.containsKey(TagConstants.RLTD_RMTINF_RMTID)) {
					rltdRmtInf.setRmtId(null);				
				}
				
				if(rltdRmtInf.getRmtLctnElctrncAdr() != null && !pmtInfChilds.containsKey(TagConstants.RLTD_RMTINF_RMTLCT_ELECTADR)) {
					rltdRmtInf.setRmtLctnElctrncAdr(null);				
				}
				
				if(rltdRmtInf.getRmtLctnMtd() != null && !pmtInfChilds.containsKey(TagConstants.RLTD_RMTINF_RMTLCT_MTD)) {
					rltdRmtInf.setRmtLctnMtd(null);				
				}
				
				if(rltdRmtInf.getRmtLctnPstlAdr() != null && !pmtInfChilds.containsKey(TagConstants.RLTD_RMTINF_RMTLCT_PSTLADR)) {
					rltdRmtInf.setRmtLctnPstlAdr(null);				
				}
				
				updatedRltdRmtInfList.addAll(updatedRltdRmtInfList);
			}
			
			CdtTrfTxInf.getRltdRmtInf().clear();
			CdtTrfTxInf.getRltdRmtInf().addAll(updatedRltdRmtInfList);
		}else if(!pmtInfChilds.containsKey(TagConstants.RLTD_RMTINF)) {
			CdtTrfTxInf.getRltdRmtInf().clear();	
		}
	}

}
