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
public class CreditTransferTransactionInformationXML {

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

	private static final Logger logger = LoggerFactory.getLogger(CreditTransferTransactionInformationXML.class);

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

	public CreditTransferTransactionInformation10 setCdtTrfTxInf(Document doc, PaymentInstructionInformation3 PmtInf,
			AggregatedPaymentEntity tags, List<AggRemittanceInfoEntity> rmtList) {
		CreditTransferTransactionInformation10 CdtTrfTxInf = new CreditTransferTransactionInformation10();
		if (null != doc.getCstmrCdtTrfInitn().getPmtInf().get(0).getCdtTrfTxInf()) {
			CreditTransferTransactionInformation10 docCdtTrfTxInf = doc.getCstmrCdtTrfInitn().getPmtInf().get(0)
					.getCdtTrfTxInf().get(0);
			if (null != docCdtTrfTxInf.getPmtId() && tags.getEndToEndId() != null) {
				CdtTrfTxInf.setPmtId(setPmtId(tags, docCdtTrfTxInf));
			}
			if (null != docCdtTrfTxInf.getAmt() && tags.getInstdAmnt() != null) {
				CdtTrfTxInf.setAmt(setAmt(tags, rmtList, docCdtTrfTxInf));
			}
			if (null != docCdtTrfTxInf.getCdtrAgt()) {
				CdtTrfTxInf.setCdtrAgt(setCdtrAgt(tags, docCdtTrfTxInf));
			}
			if (null != docCdtTrfTxInf.getCdtr()) {
				CdtTrfTxInf.setCdtr(setCdtr(tags, docCdtTrfTxInf));
			}
			if (null != docCdtTrfTxInf.getCdtrAcct()) {
				CdtTrfTxInf.setCdtrAcct(setCdtrAcct(tags, docCdtTrfTxInf));
			}
			if (null != docCdtTrfTxInf.getPurp() && tags.getPurpPrtry() != null) {
				CdtTrfTxInf.setPurp(setPurp(tags, docCdtTrfTxInf));
			}
			if (null != docCdtTrfTxInf.getRmtInf()) {
				CdtTrfTxInf.setRmtInf(setRmtInf(tags, rmtList, docCdtTrfTxInf));
			}
		}
		return CdtTrfTxInf;
	}

	private RemittanceInformation5 setRmtInf(AggregatedPaymentEntity tags, List<AggRemittanceInfoEntity> rmtList,
			CreditTransferTransactionInformation10 docCdtTrfTxInf) {
		RemittanceInformation5 RmtInf = new RemittanceInformation5();
		List<StructuredRemittanceInformation7> StrdList = new ArrayList<>();
		List<String> UstrdList = new ArrayList<>();
		rmtList.stream().forEach(e -> {

			setUstrd(docCdtTrfTxInf, UstrdList, e);

			setStrd(tags, docCdtTrfTxInf, StrdList, e);
		});
		RmtInf.setUstrd(UstrdList);

		RmtInf.setStrd(StrdList);
		return RmtInf;
	}

	private void setStrd(AggregatedPaymentEntity tags, CreditTransferTransactionInformation10 docCdtTrfTxInf,
			List<StructuredRemittanceInformation7> StrdList, AggRemittanceInfoEntity e) {
		if (null != docCdtTrfTxInf.getRmtInf().getStrd()) {
			StrdList.add(setStrdLst(tags, docCdtTrfTxInf, e));
		}
	}

	private StructuredRemittanceInformation7 setStrdLst(AggregatedPaymentEntity tags,
			CreditTransferTransactionInformation10 docCdtTrfTxInf, AggRemittanceInfoEntity e) {
		StructuredRemittanceInformation7 docStrd = docCdtTrfTxInf.getRmtInf().getStrd().get(0);

		StructuredRemittanceInformation7 Strd = new StructuredRemittanceInformation7();

		if (null != docStrd.getRfrdDocInf() && docStrd.getRfrdDocInf().size() > 0) {
			Strd.setRfrdDocInf(setRfrDocInf(e, docStrd));
		}
		if (null != docStrd.getRfrdDocAmt() && null != docStrd.getRfrdDocAmt()) {
			Strd.setRfrdDocAmt(setRfrdDocAmt(tags, e, docStrd));
		}
		return Strd;
	}

	private RemittanceAmount1 setRfrdDocAmt(AggregatedPaymentEntity tags, AggRemittanceInfoEntity e,
			StructuredRemittanceInformation7 docStrd) {
		RemittanceAmount1 RfrdDocAmt = new RemittanceAmount1();
		if (null != docStrd.getRfrdDocAmt().getDuePyblAmt() && null != e.getDuePaybAmt()) {
			RfrdDocAmt.setDuePyblAmt(setDuePyblAmt(tags, e));
		}
		if (null != docStrd.getRfrdDocAmt().getRmtdAmt() && null != e.getRmtdAmt()) {
			RfrdDocAmt.setRmtdAmt(setRmtdAmt(tags, e));
		}
		if (null != docStrd.getRfrdDocAmt().getTaxAmt() && null != e.getStrdtaxamt()) {
			RfrdDocAmt.setTaxAmt(setTaxAmt(tags, e));
		}
		return RfrdDocAmt;
	}

	private ActiveOrHistoricCurrencyAndAmount setTaxAmt(AggregatedPaymentEntity tags, AggRemittanceInfoEntity e) {
		ActiveOrHistoricCurrencyAndAmount TaxAmt = new ActiveOrHistoricCurrencyAndAmount();
		if (null != tags.getCcyCode()) {
			TaxAmt.setCcy(tags.getCcyCode());
		}
		TaxAmt.setValue(e.getStrdtaxamt());
		return TaxAmt;
	}

	private ActiveOrHistoricCurrencyAndAmount setRmtdAmt(AggregatedPaymentEntity tags, AggRemittanceInfoEntity e) {
		ActiveOrHistoricCurrencyAndAmount RmtdAmt = new ActiveOrHistoricCurrencyAndAmount();
		RmtdAmt.setValue(e.getRmtdAmt());
		if (null != tags.getCcyCode()) {
			RmtdAmt.setCcy(tags.getCcyCode());
		}
		return RmtdAmt;
	}

	private ActiveOrHistoricCurrencyAndAmount setDuePyblAmt(AggregatedPaymentEntity tags, AggRemittanceInfoEntity e) {
		ActiveOrHistoricCurrencyAndAmount DuePyblAmt = new ActiveOrHistoricCurrencyAndAmount();
		DuePyblAmt.setValue(e.getDuePaybAmt());
		if (null != tags.getCcyCode()) {
			DuePyblAmt.setCcy(tags.getCcyCode());
		}
		return DuePyblAmt;
	}

	private List<ReferredDocumentInformation3> setRfrDocInf(AggRemittanceInfoEntity e,
			StructuredRemittanceInformation7 docStrd) {
		List<ReferredDocumentInformation3> RfrdDocInfList = new ArrayList<>();

		ReferredDocumentInformation3 docRfrdDocInf = docStrd.getRfrdDocInf().get(0);

		RfrdDocInfList.add(setRfrdDocInf(e, docRfrdDocInf));
		return RfrdDocInfList;
	}

	private ReferredDocumentInformation3 setRfrdDocInf(AggRemittanceInfoEntity e,
			ReferredDocumentInformation3 docRfrdDocInf) {
		ReferredDocumentInformation3 RfrdDocInf = new ReferredDocumentInformation3();
		if (null != docRfrdDocInf.getTp()) {
			RfrdDocInf.setTp(setTp(e, docRfrdDocInf));
		}
		if (null != docRfrdDocInf.getNb() && null != e.getStrdNb()) {
			RfrdDocInf.setNb(e.getStrdNb());
		}
		if (null != docRfrdDocInf.getRltdDt() && null != e.getStrdRtldDt()) {
			RfrdDocInf.setRltdDt(e.getStrdRtldDt().toLocalDate());
		}
		return RfrdDocInf;
	}

	private ReferredDocumentType2 setTp(AggRemittanceInfoEntity e, ReferredDocumentInformation3 docRfrdDocInf) {
		ReferredDocumentType2 Tp = new ReferredDocumentType2();

		if (null != docRfrdDocInf.getTp().getCdOrPrtry()) {
			Tp.setCdOrPrtry(setCdOrPrtry(e, docRfrdDocInf));
		}
		return Tp;
	}

	private ReferredDocumentType1Choice setCdOrPrtry(AggRemittanceInfoEntity e,
			ReferredDocumentInformation3 docRfrdDocInf) {
		ReferredDocumentType1Choice CdOrPrtry = new ReferredDocumentType1Choice();
		if (null != docRfrdDocInf.getTp().getCdOrPrtry().getCd() && null != e.getStrdTpCd()) {
			CdOrPrtry.setCd(DocumentType5Code.CINV.toString());
		}
		return CdOrPrtry;
	}

	private void setUstrd(CreditTransferTransactionInformation10 docCdtTrfTxInf, List<String> UstrdList,
			AggRemittanceInfoEntity e) {
		if ((null != docCdtTrfTxInf.getRmtInf().getUstrd() && e.getRmtUstrd() != null)) {
			UstrdList.add(e.getRmtUstrd());
		}
	}

	private Purpose2Choice setPurp(AggregatedPaymentEntity tags,
			CreditTransferTransactionInformation10 docCdtTrfTxInf) {
		Purpose2Choice Purp = new Purpose2Choice();
		if (null != docCdtTrfTxInf.getPurp().getPrtry()) {
			Purp.setPrtry(tags.getPurpPrtry());
		}
		return Purp;
	}

	private CashAccount16 setCdtrAcct(AggregatedPaymentEntity tags,
			CreditTransferTransactionInformation10 docCdtTrfTxInf) {
		CashAccount16 CdtrAcct = new CashAccount16();
		if (null != docCdtTrfTxInf.getCdtrAcct().getId() && tags.getCdtrAccntOthrId() != null) {
			CdtrAcct.setId(setCdtrAccId(tags, docCdtTrfTxInf));
		}

		if (null != docCdtTrfTxInf.getCdtrAcct().getNm() && tags.getCdtrAccntNm() != null) {
			CdtrAcct.setNm(tags.getCdtrAccntNm());
		}
		return CdtrAcct;
	}

	private AccountIdentification4Choice setCdtrAccId(AggregatedPaymentEntity tags,
			CreditTransferTransactionInformation10 docCdtTrfTxInf) {
		AccountIdentification4Choice Id = new AccountIdentification4Choice();
		if (null != docCdtTrfTxInf.getCdtrAcct().getId().getOthr()) {
			setCrdtOthr(tags, docCdtTrfTxInf, Id);
		}
		return Id;
	}

	private void setCrdtOthr(AggregatedPaymentEntity tags, CreditTransferTransactionInformation10 docCdtTrfTxInf,
			AccountIdentification4Choice Id) {
		GenericAccountIdentification1 Othr = new GenericAccountIdentification1();
		if (null != docCdtTrfTxInf.getCdtrAcct().getId().getOthr().getId()) {
			Othr.setId(tags.getCdtrAccntOthrId());
		}
		Id.setOthr(Othr);
	}

	private PartyIdentification32 setCdtr(AggregatedPaymentEntity tags,
			CreditTransferTransactionInformation10 docCdtTrfTxInf) {
		PartyIdentification32 Cdtr = new PartyIdentification32();
		if (null != docCdtTrfTxInf.getCdtr().getNm() && tags.getCdtrNm() != null) {
			Cdtr.setNm(tags.getCdtrNm());
		}
		if (null != docCdtTrfTxInf.getCdtr().getPstlAdr() && tags.getCdtrPstlCtry() != null) {
			Cdtr.setPstlAdr(setCrdtPstlAdr(tags, docCdtTrfTxInf));
		}
		return Cdtr;
	}

	private PostalAddress6 setCrdtPstlAdr(AggregatedPaymentEntity tags,
			CreditTransferTransactionInformation10 docCdtTrfTxInf) {
		PostalAddress6 PstlAdr = new PostalAddress6();
		if (null != docCdtTrfTxInf.getCdtr().getPstlAdr().getCtry()) {
			PstlAdr.setCtry(tags.getCdtrPstlCtry());
		}
		return PstlAdr;
	}

	private BranchAndFinancialInstitutionIdentification4 setCdtrAgt(AggregatedPaymentEntity tags,
			CreditTransferTransactionInformation10 docCdtTrfTxInf) {
		BranchAndFinancialInstitutionIdentification4 CdtrAgt = new BranchAndFinancialInstitutionIdentification4();
		if (null != docCdtTrfTxInf.getCdtrAgt().getFinInstnId()) {
			CdtrAgt.setFinInstnId(setFinInstnId(tags, docCdtTrfTxInf));
		}

		if (null != docCdtTrfTxInf.getCdtrAgt().getBrnchId() && tags.getCdtrAgntBrnId() != null) {
			CdtrAgt.setBrnchId(setBrnchId(tags, docCdtTrfTxInf));
		}
		return CdtrAgt;
	}

	private BranchData2 setBrnchId(AggregatedPaymentEntity tags,
			CreditTransferTransactionInformation10 docCdtTrfTxInf) {
		BranchData2 BrnchId = new BranchData2();
		if (null != docCdtTrfTxInf.getCdtrAgt().getBrnchId().getId()) {
			BrnchId.setId(tags.getCdtrAgntBrnId());
		}
		return BrnchId;
	}

	private FinancialInstitutionIdentification7 setFinInstnId(AggregatedPaymentEntity tags,
			CreditTransferTransactionInformation10 docCdtTrfTxInf) {
		FinancialInstitutionIdentification7 FinInstnId = new FinancialInstitutionIdentification7();
		if (null != docCdtTrfTxInf.getCdtrAgt().getFinInstnId().getBIC() && tags.getCdtrAgntBic() != null) {
			FinInstnId.setBIC(tags.getCdtrAgntBic());
		}
		if (null != docCdtTrfTxInf.getCdtrAgt().getFinInstnId().getNm() && tags.getCdtrAgntNm() != null) {
			FinInstnId.setNm(tags.getCdtrAgntNm());
		}
		if (null != docCdtTrfTxInf.getCdtrAgt().getFinInstnId().getPstlAdr() && tags.getCdtrPstlCtry() != null) {
			FinInstnId.setPstlAdr(setPstlAdr(tags, docCdtTrfTxInf));
		}
		return FinInstnId;
	}

	private PostalAddress6 setPstlAdr(AggregatedPaymentEntity tags,
			CreditTransferTransactionInformation10 docCdtTrfTxInf) {
		PostalAddress6 PstlAdr = new PostalAddress6();
		if (null != docCdtTrfTxInf.getCdtrAgt().getFinInstnId().getPstlAdr().getCtry()) {
			PstlAdr.setCtry(tags.getCdtrPstlCtry());
		}
		return PstlAdr;
	}

	private AmountType3Choice setAmt(AggregatedPaymentEntity tags, List<AggRemittanceInfoEntity> rmtList,
			CreditTransferTransactionInformation10 docCdtTrfTxInf) {
		AmountType3Choice Amt = new AmountType3Choice();
		if (null != docCdtTrfTxInf.getAmt().getInstdAmt()) {
			ActiveOrHistoricCurrencyAndAmount InstdAmt = new ActiveOrHistoricCurrencyAndAmount();
			InstdAmt.setValue(rmtList.get(0).getStrdInstdAmt());
			if (null != tags.getCcyCode()) {
				InstdAmt.setCcy(tags.getCcyCode());
			}
			Amt.setInstdAmt(InstdAmt);
		}
		return Amt;
	}

	private PaymentIdentification1 setPmtId(AggregatedPaymentEntity tags,
			CreditTransferTransactionInformation10 docCdtTrfTxInf) {
		PaymentIdentification1 PmtId = new PaymentIdentification1();
		if (null != docCdtTrfTxInf.getPmtId().getEndToEndId()) {
			PmtId.setEndToEndId(tags.getEndToEndId());
		}
		return PmtId;
	}

}
