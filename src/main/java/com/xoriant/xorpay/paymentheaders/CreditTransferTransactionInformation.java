package com.xoriant.xorpay.paymentheaders;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.entity.AggRemittanceInfoEntity;
import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.model.Tags;
import com.xoriant.xorpay.pojo001.AccountIdentification4Choice;
import com.xoriant.xorpay.pojo001.ActiveOrHistoricCurrencyAndAmount;
import com.xoriant.xorpay.pojo001.AmountType3Choice;
import com.xoriant.xorpay.pojo001.BranchAndFinancialInstitutionIdentification4;
import com.xoriant.xorpay.pojo001.BranchData2;
import com.xoriant.xorpay.pojo001.CashAccount16;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;
import com.xoriant.xorpay.pojo001.Document;
import com.xoriant.xorpay.pojo001.DocumentType5Code;
import com.xoriant.xorpay.pojo001.ExchangeRateInformation1;
import com.xoriant.xorpay.pojo001.FinancialInstitutionIdentification7;
import com.xoriant.xorpay.pojo001.GenericAccountIdentification1;
import com.xoriant.xorpay.pojo001.PartyIdentification32;
import com.xoriant.xorpay.pojo001.PaymentIdentification1;
import com.xoriant.xorpay.pojo001.PaymentInstructionInformation3;
import com.xoriant.xorpay.pojo001.PostalAddress6;
import com.xoriant.xorpay.pojo001.Purpose2Choice;
import com.xoriant.xorpay.pojo001.ReferredDocumentInformation3;
import com.xoriant.xorpay.pojo001.ReferredDocumentType1Choice;
import com.xoriant.xorpay.pojo001.ReferredDocumentType2;
import com.xoriant.xorpay.pojo001.RemittanceAmount1;
import com.xoriant.xorpay.pojo001.RemittanceInformation5;
import com.xoriant.xorpay.pojo001.StructuredRemittanceInformation7;
import com.xoriant.xorpay.util.TagCustomUtil;
import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.credittransinforules.ChequeInstruction;
import com.xoriant.xorpay.credittransinforules.Creditor;
import com.xoriant.xorpay.credittransinforules.CreditorAccount;
import com.xoriant.xorpay.credittransinforules.CreditorAgent;
import com.xoriant.xorpay.credittransinforules.InstructionForCreditorAgent;
import com.xoriant.xorpay.credittransinforules.InstructionForDebtorAgent;
import com.xoriant.xorpay.credittransinforules.IntermediaryAgent;
import com.xoriant.xorpay.credittransinforules.RegulatoryReporting;
import com.xoriant.xorpay.credittransinforules.RemittanceInformation;
import com.xoriant.xorpay.credittransinforules.RemittanceLocation;
import com.xoriant.xorpay.credittransinforules.TaxInformation;

@Service
public class CreditTransferTransactionInformation {

	@Autowired
	ChequeInstruction chequeInstruction;

	@Autowired
	IntermediaryAgent intermediaryAgent;

	@Autowired
	CreditorAgent creditorAgent;

	@Autowired
	Creditor creditor;

	@Autowired
	CreditorAccount creditorAccount;

	@Autowired
	InstructionForCreditorAgent instructionForCreditorAgent;

	@Autowired
	InstructionForDebtorAgent instructionForDebtorAgent;

	@Autowired
	RegulatoryReporting regulatoryReporting;

	@Autowired
	TaxInformation tax;

	@Autowired
	RemittanceLocation remittanceLocation;

	@Autowired
	RemittanceInformation remittanceInformation;

	/*
	 * @Autowired XmlRuleUtility xmlRuleUtility;
	 */

	TagCustomUtil tagCustomUtil = new TagCustomUtil();

	private static final Logger logger = LoggerFactory.getLogger(CreditTransferTransactionInformation.class);

	public void getCdtTrfTxInf(PaymentInstructionInformation3 PmtInf, Map<String, String> pmtInfChilds) {

		logger.info(" CreditTransferTransactionInformation getCdtTrfTxInf() execution started");

		boolean isCustomization = false;
		List<String> tagListToCheck = new ArrayList<String>();
		tagListToCheck.add(TagConstants.MAX_LENGTH);
		tagListToCheck.add(TagConstants.PIUID);
		tagListToCheck.add(TagConstants.COUNTRY);
		List<CreditTransferTransactionInformation10> CdtTrfTxInfList = PmtInf.getCdtTrfTxInf();
		List<CreditTransferTransactionInformation10> updatedCdtTrfTxInfList = new ArrayList<CreditTransferTransactionInformation10>();
		for (CreditTransferTransactionInformation10 CdtTrfTxInf : CdtTrfTxInfList) {

			// This is mandatory
			// CdtTrfTxInf.getPmtId();

			// This is mandatory
			// CdtTrfTxInf.getPmtTpInf();

			// This is mandatory
			// CdtTrfTxInf.getAmt();

			ExchangeRateInformation1 XchgRateInf = CdtTrfTxInf.getXchgRateInf();
			if (XchgRateInf != null && pmtInfChilds.containsKey(TagConstants.XCHANGERATEINFO)) {
				if (XchgRateInf.getRateTp() != null && !pmtInfChilds.containsKey(TagConstants.XCHANGERATEINFORATETP)) {
					CdtTrfTxInf.getXchgRateInf().setRateTp(null);
				}
				if (XchgRateInf.getXchgRate() != null && !pmtInfChilds.containsKey(TagConstants.XCHANGERATE)) {
					CdtTrfTxInf.getXchgRateInf().setXchgRate(null);
				}
			} else if (!pmtInfChilds.containsKey(TagConstants.XCHANGERATEINFO)) {
				CdtTrfTxInf.setXchgRateInf(null);
			}

			if (CdtTrfTxInf.getChrgBr() != null && !pmtInfChilds.containsKey(TagConstants.CHARGEBR)) {
				CdtTrfTxInf.setChrgBr(null);
			}

			chequeInstruction.getChqInstr(CdtTrfTxInf, pmtInfChilds);

			/* TODO CdtTrfTxInf.getUltmtDbtr(); */
			intermediaryAgent.getIntrmyAgt1(CdtTrfTxInf, pmtInfChilds);
			intermediaryAgent.getIntrmyAgtAcct1(CdtTrfTxInf, pmtInfChilds);
			intermediaryAgent.getIntrmyAgt2(CdtTrfTxInf, pmtInfChilds);
			intermediaryAgent.getIntrmyAgtAcct2(CdtTrfTxInf, pmtInfChilds);

			creditor.getCdtr(CdtTrfTxInf, pmtInfChilds);

			creditorAgent.getCdtrAgt(CdtTrfTxInf, pmtInfChilds);
			creditorAgent.getCdtrAgtAcct(CdtTrfTxInf, pmtInfChilds);

			creditorAccount.getCredtAcct(CdtTrfTxInf, pmtInfChilds);

			creditor.getUltmtCdtr(CdtTrfTxInf, pmtInfChilds);

			instructionForCreditorAgent.getInstrForCdtrAgt(CdtTrfTxInf, pmtInfChilds);
			instructionForDebtorAgent.getInstrForDbtrAgt(CdtTrfTxInf, pmtInfChilds);

			/*
			 * if (tagCustomUtil.checkCustomization(pmtInfChilds, tagListToCheck)) {
			 * isCustomization = true; }
			 * 
			 * logger.debug("isCustomization " + isCustomization);
			 * 
			 * if (isCustomization) { xmlRuleUtility.creditTransInfoRules(CdtTrfTxInf,
			 * pmtInfChilds, isCustomization); } else {
			 * xmlRuleUtility.creditTransInfoRules(CdtTrfTxInf, pmtInfChilds); }
			 */

			/* TODO CdtTrfTxInf.getUltmtDbtr(); */

			if (CdtTrfTxInf.getPurp() != null && pmtInfChilds.containsKey(TagConstants.PURPOSE)) {
				if (CdtTrfTxInf.getPurp().getCd() != null && !pmtInfChilds.containsKey(TagConstants.PURPOSE_CODE)) {
					CdtTrfTxInf.getPurp().setCd(null);
				}
				if (CdtTrfTxInf.getPurp().getPrtry() != null && !pmtInfChilds.containsKey(TagConstants.PURPOSE_PRTRY)) {
					CdtTrfTxInf.getPurp().setPrtry(null);
				}
			} else if (!pmtInfChilds.containsKey(TagConstants.PURPOSE)) {
				CdtTrfTxInf.setPurp(null);
			}

			regulatoryReporting.getRegulatoryReporting(CdtTrfTxInf, pmtInfChilds);

			tax.getTax(CdtTrfTxInf, pmtInfChilds);

			remittanceLocation.getRltdRmtInf(CdtTrfTxInf, pmtInfChilds);

			remittanceInformation.getRmtInf(CdtTrfTxInf, pmtInfChilds);

			updatedCdtTrfTxInfList.add(CdtTrfTxInf);
		}

		PmtInf.getCdtTrfTxInf().clear();
		PmtInf.getCdtTrfTxInf().addAll(updatedCdtTrfTxInfList);
	}

	public void setCdtTrfTxInf(Document doc, PaymentInstructionInformation3 PmtInf, Tags tags) {
		List<CreditTransferTransactionInformation10> CdtTrfTxInfList = PmtInf.getCdtTrfTxInf();

		if (tags.getEndToEndId() != null) {
			CdtTrfTxInfList.get(0).getPmtId().setEndToEndId(tags.getEndToEndId());
		}

		if (tags.getInstdAmnt() != null) {
			CdtTrfTxInfList.get(0).getAmt().getInstdAmt().setValue(tags.getInstdAmnt());
		}

		if (tags.getCdtrAgntBic() != null) {
			CdtTrfTxInfList.get(0).getCdtrAgt().getFinInstnId().setBIC(tags.getCdtrAgntBic());
		}
		if (tags.getCdtrAgntNm() != null) {
			CdtTrfTxInfList.get(0).getCdtrAgt().getFinInstnId().setNm(tags.getCdtrAgntNm());
		}

		if (tags.getCdtrAgntPstlCtry() != null) {
			CdtTrfTxInfList.get(0).getCdtrAgt().getFinInstnId().getPstlAdr().setCtry(tags.getCdtrAgntPstlCtry());
		}

		if (tags.getCdtrAgntBrnId() != null) {
			CdtTrfTxInfList.get(0).getCdtrAgt().getBrnchId().setId(tags.getCdtrAgntBrnId());
		}

		if (tags.getCdtrNm() != null) {
			CdtTrfTxInfList.get(0).getCdtr().setNm(tags.getCdtrNm());
		}
		if (tags.getCdtrPstlCtry() != null) {
			CdtTrfTxInfList.get(0).getCdtr().getPstlAdr().setCtry(tags.getCdtrPstlCtry());
		}

		if (tags.getCdtrAccntOthrId() != null) {
			CdtTrfTxInfList.get(0).getCdtrAcct().getId().getOthr().setId(tags.getCdtrAccntOthrId());
		}

		if (tags.getCdtrAccntNm() != null) {
			CdtTrfTxInfList.get(0).getCdtrAcct().setNm(tags.getCdtrAccntNm());
		}

		if (tags.getPurpPrtry() != null) {
			CdtTrfTxInfList.get(0).getPurp().setPrtry(tags.getPurpPrtry());
		}

		/*
		 * List<RemittanceLocation2> rmtList = new ArrayList<RemittanceLocation2>();
		 * RemittanceLocation2 rmt = new RemittanceLocation2();
		 * if(tags.getRmtLctnMthd()!=null) { if(tags.getRmtLctnMthd().equals("EMAL")) {
		 * 
		 * rmt.setRmtLctnMtd(RemittanceLocationMethod2Code.EMAL);
		 * //rmt.setRmtLctnPstlAdr(value);
		 * //CdtTrfTxInfList.get(0).getRltdRmtInf().get(0).setRmtLctnMtd(
		 * RemittanceLocationMethod2Code.EMAL);
		 * //CdtTrfTxInfList.get(0).getRltdRmtInf().set(0,
		 * rmt.setRmtLctnMtd(RemittanceLocationMethod2Code.EMAL));
		 * CdtTrfTxInfList.get(0).getRltdRmtInf().clear();
		 * CdtTrfTxInfList.get(0).getRltdRmtInf().set(0, rmt);
		 * 
		 * //logger.info("getRltdRmtInf::: "+rltdSize); }
		 * //if(tags.getRmtLctnMthd().equals("FAX"))
		 * //CdtTrfTxInfList.get(0).getRltdRmtInf().get(0).setRmtLctnMtd(
		 * RemittanceLocationMethod2Code.FAXI); //add for other remaining values }
		 */

		/*
		 * if(tags.getRmtLctnElctAddr()!=null) {
		 * CdtTrfTxInfList.get(0).getRltdRmtInf().get(0).setRmtLctnElctrncAdr(tags.
		 * getRmtLctnElctAddr());
		 * //CdtTrfTxInfList.get(0).getRltdRmtInf().get(0).getRmtLctnElctrncAdr().g); }
		 */
		if (tags.getRmtUstrd() != null) {
			int getRmtInfSize = CdtTrfTxInfList.get(0).getRmtInf().getUstrd().size();
			logger.info("getRmtInfSize::: " + getRmtInfSize);
			if (CdtTrfTxInfList.get(0).getRmtInf().getUstrd().size() > 0) {
				CdtTrfTxInfList.get(0).getRmtInf().getUstrd().clear();
			}
			CdtTrfTxInfList.get(0).getRmtInf().getUstrd().add(tags.getRmtUstrd());
		}

		if (tags.getStrdTpCd() != null) {
			CdtTrfTxInfList.get(0).getRmtInf().getStrd().get(0).getRfrdDocInf().get(0).getTp().getCdOrPrtry()
					.setCd(DocumentType5Code.CINV.toString());
		}
		// Code for remaining getStrdTpCd
		if (tags.getStrdNb() != null) {
			CdtTrfTxInfList.get(0).getRmtInf().getStrd().get(0).getRfrdDocInf().get(0).setNb(tags.getStrdNb());
		}

		Timestamp ts = new Timestamp(tags.getStrdRtldDt().getTime());
		// ts.setNanos(123456789);

		LocalDateTime ldt = ts.toLocalDateTime();

		XMLGregorianCalendar cal = null;
		try {
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cal.setYear(ldt.getYear());
		cal.setMonth(ldt.getMonthValue());
		cal.setDay(ldt.getDayOfMonth());
		cal.setHour(ldt.getHour());
		cal.setMinute(ldt.getMinute());
		cal.setSecond(ldt.getSecond());

		if (tags.getStrdRtldDt() != null) {
			CdtTrfTxInfList.get(0).getRmtInf().getStrd().get(0).getRfrdDocInf().get(0).setRltdDt(null);
		}

		if (tags.getDuePaybAmt() != null) {
			CdtTrfTxInfList.get(0).getRmtInf().getStrd().get(0).getRfrdDocAmt().getDuePyblAmt()
					.setValue(tags.getDuePaybAmt());
		}

		if (tags.getRmtdAmt() != null) {
			CdtTrfTxInfList.get(0).getRmtInf().getStrd().get(0).getRfrdDocAmt().getRmtdAmt()
					.setValue(tags.getRmtdAmt());
		}

	}

	public CreditTransferTransactionInformation10 setCdtTrfTxInf(Document doc, PaymentInstructionInformation3 PmtInf,
			AggregatedPaymentEntity tags, List<AggRemittanceInfoEntity> rmtList) {

		CreditTransferTransactionInformation10 CdtTrfTxInf = new CreditTransferTransactionInformation10();
		if (tags.getEndToEndId() != null) {
			PaymentIdentification1 PmtId = new PaymentIdentification1();
			PmtId.setEndToEndId(tags.getEndToEndId());
			CdtTrfTxInf.setPmtId(PmtId);
		}

		if (tags.getInstdAmnt() != null) {
			AmountType3Choice Amt = new AmountType3Choice();
			ActiveOrHistoricCurrencyAndAmount InstdAmt = new ActiveOrHistoricCurrencyAndAmount();
			InstdAmt.setValue(rmtList.get(0).getStrdInstdAmt());
			if (null != tags.getCcyCode()) {
				InstdAmt.setCcy(tags.getCcyCode());
			}
			Amt.setInstdAmt(InstdAmt);

			CdtTrfTxInf.setAmt(Amt);
		}

		BranchAndFinancialInstitutionIdentification4 CdtrAgt = new BranchAndFinancialInstitutionIdentification4();
		FinancialInstitutionIdentification7 FinInstnId = new FinancialInstitutionIdentification7();
		boolean flagFinInstnId = false;
		if (tags.getCdtrAgntBic() != null) {
			FinInstnId.setBIC(tags.getCdtrAgntBic());
			flagFinInstnId = true;
		}
		if (tags.getCdtrAgntNm() != null) {
			FinInstnId.setNm(tags.getCdtrAgntNm());
			flagFinInstnId = true;
		}

		if (tags.getCdtrPstlCtry() != null) {
			PostalAddress6 PstlAdr = new PostalAddress6();
			PstlAdr.setCtry(tags.getCdtrPstlCtry());
			FinInstnId.setPstlAdr(PstlAdr);
			flagFinInstnId = true;
		}
		boolean flagCdtrAgt = false;
		if (flagFinInstnId) {
			CdtrAgt.setFinInstnId(FinInstnId);
			flagCdtrAgt = true;
		}

		if (tags.getCdtrAgntBrnId() != null) {
			BranchData2 BrnchId = new BranchData2();
			BrnchId.setId(tags.getCdtrAgntBrnId());
			CdtrAgt.setBrnchId(BrnchId);
			flagCdtrAgt = true;
		}
		if (flagCdtrAgt) {
			CdtTrfTxInf.setCdtrAgt(CdtrAgt);
		}

		PartyIdentification32 Cdtr = new PartyIdentification32();
		boolean flafCdtr = false;
		if (tags.getCdtrNm() != null) {
			Cdtr.setNm(tags.getCdtrNm());
			flafCdtr = true;
		}
		if (tags.getCdtrPstlCtry() != null) {
			PostalAddress6 PstlAdr = new PostalAddress6();
			PstlAdr.setCtry(tags.getCdtrPstlCtry());
			Cdtr.setPstlAdr(PstlAdr);
			flafCdtr = true;
		}
		if (flafCdtr) {
			CdtTrfTxInf.setCdtr(Cdtr);
		}
		CashAccount16 CdtrAcct = new CashAccount16();
		boolean flagCdtrAcct = false;
		if (tags.getCdtrAccntOthrId() != null) {
			GenericAccountIdentification1 Othr = new GenericAccountIdentification1();
			AccountIdentification4Choice Id = new AccountIdentification4Choice();
			Othr.setId(tags.getCdtrAccntOthrId());
			Id.setOthr(Othr);
			CdtrAcct.setId(Id);
			flagCdtrAcct = true;
		}

		if (tags.getCdtrAccntNm() != null) {
			CdtrAcct.setNm(tags.getCdtrAccntNm());
			flagCdtrAcct = true;
		}
		if (flagCdtrAcct) {
			CdtTrfTxInf.setCdtrAcct(CdtrAcct);
		}
		if (tags.getPurpPrtry() != null) {
			Purpose2Choice Purp = new Purpose2Choice();
			Purp.setPrtry(tags.getPurpPrtry());
			CdtTrfTxInf.setPurp(Purp);
		}

		/*
		 * List<RemittanceLocation2> rmtList = new ArrayList<RemittanceLocation2>();
		 * RemittanceLocation2 rmt = new RemittanceLocation2();
		 * if(tags.getRmtLctnMthd()!=null) { if(tags.getRmtLctnMthd().equals("EMAL")) {
		 * 
		 * rmt.setRmtLctnMtd(RemittanceLocationMethod2Code.EMAL);
		 * //rmt.setRmtLctnPstlAdr(value);
		 * //CdtTrfTxInf.getRltdRmtInf().get(0).setRmtLctnMtd(
		 * RemittanceLocationMethod2Code.EMAL); //CdtTrfTxInf.getRltdRmtInf().set(0,
		 * rmt.setRmtLctnMtd(RemittanceLocationMethod2Code.EMAL));
		 * CdtTrfTxInf.getRltdRmtInf().clear(); CdtTrfTxInf.getRltdRmtInf().set(0, rmt);
		 * 
		 * //logger.info("getRltdRmtInf::: "+rltdSize); }
		 * //if(tags.getRmtLctnMthd().equals("FAX"))
		 * //CdtTrfTxInf.getRltdRmtInf().get(0).setRmtLctnMtd(
		 * RemittanceLocationMethod2Code.FAXI); //add for other remaining values }
		 */

		/*
		 * if(tags.getRmtLctnElctAddr()!=null) {
		 * CdtTrfTxInf.getRltdRmtInf().get(0).setRmtLctnElctrncAdr(tags.
		 * getRmtLctnElctAddr());
		 * //CdtTrfTxInf.getRltdRmtInf().get(0).getRmtLctnElctrncAdr().g); }
		 */
		RemittanceInformation5 RmtInf = new RemittanceInformation5();
		List<String> UstrdList = new ArrayList<>();
		List<StructuredRemittanceInformation7> StrdList = new ArrayList<>();
		rmtList.stream().forEach(e -> {
			if (e.getRmtUstrd() != null) {
				/*
				 * int getRmtInfSize = CdtTrfTxInf.getRmtInf().getUstrd().size();
				 * logger.info("getRmtInfSize::: " + getRmtInfSize); if
				 * (CdtTrfTxInf.getRmtInf().getUstrd().size() > 0) {
				 * CdtTrfTxInf.getRmtInf().getUstrd().clear(); }
				 */
				UstrdList.add(e.getRmtUstrd());
			}

			if (e.getStrdTpCd() != null || e.getStrdNb() != null) {
				ReferredDocumentType1Choice CdOrPrtry = new ReferredDocumentType1Choice();
				ReferredDocumentType2 Tp = new ReferredDocumentType2();
				List<ReferredDocumentInformation3> RfrdDocInfList = new ArrayList<>();
				ReferredDocumentInformation3 RfrdDocInf = new ReferredDocumentInformation3();

				StructuredRemittanceInformation7 Strd = new StructuredRemittanceInformation7();
				if (null != e.getStrdTpCd()) {
					CdOrPrtry.setCd(DocumentType5Code.CINV.toString());
					Tp.setCdOrPrtry(CdOrPrtry);
					RfrdDocInf.setTp(Tp);
				}
				if (null != e.getStrdNb()) {
					RfrdDocInf.setNb(e.getStrdNb());
				}
				if (null != e.getStrdRtldDt()) {
					XMLGregorianCalendar xmlGregorianCalendar;
					try {
						xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(
								GregorianCalendar.from(ZonedDateTime.of(e.getStrdRtldDt(), ZoneId.systemDefault())));
						RfrdDocInf.setRltdDt(e.getStrdRtldDt().toLocalDate());
					} catch (DatatypeConfigurationException e1) {
						e1.printStackTrace();
					}
				}
				RfrdDocInfList.add(RfrdDocInf);
				RemittanceAmount1 RfrdDocAmt = new RemittanceAmount1();
				boolean flagRfrdDocAmt = false;
				if (null != e.getDuePaybAmt()) {
					ActiveOrHistoricCurrencyAndAmount DuePyblAmt = new ActiveOrHistoricCurrencyAndAmount();
					DuePyblAmt.setValue(e.getDuePaybAmt());
					if (null != tags.getCcyCode()) {
						DuePyblAmt.setCcy(tags.getCcyCode());
					}
					RfrdDocAmt.setDuePyblAmt(DuePyblAmt);
					flagRfrdDocAmt = true;
				}
				if (null != e.getRmtdAmt()) {
					ActiveOrHistoricCurrencyAndAmount RmtdAmt = new ActiveOrHistoricCurrencyAndAmount();
					RmtdAmt.setValue(e.getRmtdAmt());
					if (null != tags.getCcyCode()) {
						RmtdAmt.setCcy(tags.getCcyCode());
					}
					RfrdDocAmt.setRmtdAmt(RmtdAmt);
					flagRfrdDocAmt = true;
				}
				if(null != e.getStrdtaxamt()) {
					ActiveOrHistoricCurrencyAndAmount val = new ActiveOrHistoricCurrencyAndAmount() ;
					if (null != tags.getCcyCode()) {
						val.setCcy(tags.getCcyCode());
					}
					val.setValue(e.getStrdtaxamt());
					RfrdDocAmt.setTaxAmt(val);
					flagRfrdDocAmt = true;
				}
				if (flagRfrdDocAmt) {
					Strd.setRfrdDocAmt(RfrdDocAmt);
				}
				Strd.setRfrdDocInf(RfrdDocInfList);
				StrdList.add(Strd);
			}
		});
		RmtInf.setUstrd(UstrdList);
		RmtInf.setStrd(StrdList);
		CdtTrfTxInf.setRmtInf(RmtInf);
		// Code for remaining getStrdTpCd

		/*
		 * if (tags.getStrdNb() != null) {
		 * CdtTrfTxInf.getRmtInf().getStrd().get(0).getRfrdDocInf().get(0).setNb(tags.
		 * getStrdNb()); }
		 */

		// PmtInf.setCdtTrfTxInf(CdtTrfTxInfList);
		return CdtTrfTxInf;
	}

}
