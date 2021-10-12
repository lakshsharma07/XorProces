package com.xoriant.xorpay.credittransinforules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;
import com.xoriant.xorpay.pojo001.RegulatoryReporting3;
import com.xoriant.xorpay.pojo001.StructuredRegulatoryReporting3;

@Service
public class RegulatoryReporting {
	
	public void getRegulatoryReporting(CreditTransferTransactionInformation10 CdtTrfTxInf, Map<String,String> pmtInfChilds) {
		List<RegulatoryReporting3> regulatoryReportingList = CdtTrfTxInf.getRgltryRptg();
		List<RegulatoryReporting3> updatedRegulatoryReportingList = new ArrayList();
		for(RegulatoryReporting3 regulatoryReporting : regulatoryReportingList) {
			if(regulatoryReporting.getDbtCdtRptgInd() != null && !pmtInfChilds.containsKey(TagConstants.RGLT_DBTCDT_RPT_IND)){
				regulatoryReporting.setDbtCdtRptgInd(null);
			}
			
			if(regulatoryReporting.getAuthrty() != null && !pmtInfChilds.containsKey(TagConstants.RGLT_AUTHRTY)){
				regulatoryReporting.setAuthrty(null);
			}
			
			if(regulatoryReporting.getDtls() != null && !pmtInfChilds.containsKey(TagConstants.RGLT_DTLS)){
				List<StructuredRegulatoryReporting3> dtlsList = regulatoryReporting.getDtls();
				List<StructuredRegulatoryReporting3> updatedDtlsList = new ArrayList();
				
				for(StructuredRegulatoryReporting3 dtls : dtlsList) {
					if(dtls.getTp() != null && !pmtInfChilds.containsKey(TagConstants.RGLT_DTLS_TP)) {
						dtls.setTp(null);
					}
					
					if(dtls.getDt() != null && !pmtInfChilds.containsKey(TagConstants.RGLT_DTLS_DT)) {
						dtls.setDt(null);
					}

					if(dtls.getCtry() != null && !pmtInfChilds.containsKey(TagConstants.RGLT_DTLS_CTRY)) {
						dtls.setCtry(null);
					}
					
					if(dtls.getCd() != null && !pmtInfChilds.containsKey(TagConstants.RGLT_DTLS_CD)) {
						dtls.setCd(null);
					}
					
					if(dtls.getAmt() != null && !pmtInfChilds.containsKey(TagConstants.RGLT_DTLS_AMNT)) {
						dtls.setAmt(null);
					}
					
					if(dtls.getInf() != null && !pmtInfChilds.containsKey(TagConstants.RGLT_DTLS_INF)) {
						dtls.getInf().clear();
					}
					regulatoryReporting.getDtls().clear();
					regulatoryReporting.getDtls().addAll(updatedDtlsList);
				}
			}
						
			updatedRegulatoryReportingList.add(regulatoryReporting);
		}
		regulatoryReportingList.clear();
		regulatoryReportingList.addAll(updatedRegulatoryReportingList);
	}

}
