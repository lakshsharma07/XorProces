package com.xoriant.xorpay.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.PAIN001Constant;
import com.xoriant.xorpay.entity.XMLPIUIDStructureEntity;
import com.xoriant.xorpay.pojo001.AccountIdentification4Choice;
import com.xoriant.xorpay.pojo001.AccountSchemeName1Choice;
import com.xoriant.xorpay.pojo001.ActiveOrHistoricCurrencyAndAmount;
import com.xoriant.xorpay.pojo001.AddressType2Code;
import com.xoriant.xorpay.pojo001.AmountType3Choice;
import com.xoriant.xorpay.pojo001.BranchAndFinancialInstitutionIdentification4;
import com.xoriant.xorpay.pojo001.BranchData2;
import com.xoriant.xorpay.pojo001.CashAccount16;
import com.xoriant.xorpay.pojo001.CashAccountType2;
import com.xoriant.xorpay.pojo001.CashAccountType4Code;
import com.xoriant.xorpay.pojo001.CategoryPurpose1Choice;
import com.xoriant.xorpay.pojo001.ChargeBearerType1Code;
import com.xoriant.xorpay.pojo001.Cheque6;
import com.xoriant.xorpay.pojo001.ChequeDelivery1Code;
import com.xoriant.xorpay.pojo001.ChequeDeliveryMethod1Choice;
import com.xoriant.xorpay.pojo001.ChequeType2Code;
import com.xoriant.xorpay.pojo001.ClearingSystemIdentification2Choice;
import com.xoriant.xorpay.pojo001.ClearingSystemMemberIdentification2;
import com.xoriant.xorpay.pojo001.ContactDetails2;
import com.xoriant.xorpay.pojo001.CreditDebitCode;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;
import com.xoriant.xorpay.pojo001.CreditorReferenceInformation2;
import com.xoriant.xorpay.pojo001.CreditorReferenceType1Choice;
import com.xoriant.xorpay.pojo001.CreditorReferenceType2;
import com.xoriant.xorpay.pojo001.CustomerCreditTransferInitiationV03;
import com.xoriant.xorpay.pojo001.DateAndPlaceOfBirth;
import com.xoriant.xorpay.pojo001.DatePeriodDetails;
import com.xoriant.xorpay.pojo001.Document;
import com.xoriant.xorpay.pojo001.DocumentAdjustment1;
import com.xoriant.xorpay.pojo001.DocumentType3Code;
import com.xoriant.xorpay.pojo001.DocumentType5Code;
import com.xoriant.xorpay.pojo001.EquivalentAmount2;
import com.xoriant.xorpay.pojo001.ExchangeRateInformation1;
import com.xoriant.xorpay.pojo001.ExchangeRateType1Code;
import com.xoriant.xorpay.pojo001.FinancialIdentificationSchemeName1Choice;
import com.xoriant.xorpay.pojo001.FinancialInstitutionIdentification7;
import com.xoriant.xorpay.pojo001.GenericAccountIdentification1;
import com.xoriant.xorpay.pojo001.GenericFinancialIdentification1;
import com.xoriant.xorpay.pojo001.GenericOrganisationIdentification1;
import com.xoriant.xorpay.pojo001.GenericPersonIdentification1;
import com.xoriant.xorpay.pojo001.GroupHeader32;
import com.xoriant.xorpay.pojo001.Instruction3Code;
import com.xoriant.xorpay.pojo001.InstructionForCreditorAgent1;
import com.xoriant.xorpay.pojo001.LocalInstrument2Choice;
import com.xoriant.xorpay.pojo001.NameAndAddress10;
import com.xoriant.xorpay.pojo001.NamePrefix1Code;
import com.xoriant.xorpay.pojo001.OrganisationIdentification4;
import com.xoriant.xorpay.pojo001.OrganisationIdentificationSchemeName1Choice;
import com.xoriant.xorpay.pojo001.Party6Choice;
import com.xoriant.xorpay.pojo001.PartyIdentification32;
import com.xoriant.xorpay.pojo001.PaymentIdentification1;
import com.xoriant.xorpay.pojo001.PaymentInstructionInformation3;
import com.xoriant.xorpay.pojo001.PaymentMethod3Code;
import com.xoriant.xorpay.pojo001.PaymentTypeInformation19;
import com.xoriant.xorpay.pojo001.PersonIdentification5;
import com.xoriant.xorpay.pojo001.PersonIdentificationSchemeName1Choice;
import com.xoriant.xorpay.pojo001.PostalAddress6;
import com.xoriant.xorpay.pojo001.Priority2Code;
import com.xoriant.xorpay.pojo001.Purpose2Choice;
import com.xoriant.xorpay.pojo001.ReferredDocumentInformation3;
import com.xoriant.xorpay.pojo001.ReferredDocumentType1Choice;
import com.xoriant.xorpay.pojo001.ReferredDocumentType2;
import com.xoriant.xorpay.pojo001.RegulatoryAuthority2;
import com.xoriant.xorpay.pojo001.RegulatoryReporting3;
import com.xoriant.xorpay.pojo001.RegulatoryReportingType1Code;
import com.xoriant.xorpay.pojo001.RemittanceAmount1;
import com.xoriant.xorpay.pojo001.RemittanceInformation5;
import com.xoriant.xorpay.pojo001.RemittanceLocation2;
import com.xoriant.xorpay.pojo001.RemittanceLocationMethod2Code;
import com.xoriant.xorpay.pojo001.ServiceLevel8Choice;
import com.xoriant.xorpay.pojo001.StructuredRegulatoryReporting3;
import com.xoriant.xorpay.pojo001.StructuredRemittanceInformation7;
import com.xoriant.xorpay.pojo001.TaxAmount1;
import com.xoriant.xorpay.pojo001.TaxAuthorisation1;
import com.xoriant.xorpay.pojo001.TaxInformation3;
import com.xoriant.xorpay.pojo001.TaxParty1;
import com.xoriant.xorpay.pojo001.TaxParty2;
import com.xoriant.xorpay.pojo001.TaxPeriod1;
import com.xoriant.xorpay.pojo001.TaxRecord1;
import com.xoriant.xorpay.pojo001.TaxRecordDetails1;
import com.xoriant.xorpay.pojo001.TaxRecordPeriod1Code;

@Service
public class XMLDocumentService {

	public Document getXMLXSDFor(List<XMLPIUIDStructureEntity> xmlStructurePOJO) {
		Map<Integer, Set<Integer>> tagMap = xmlStructurePOJO.stream()
				.filter(e -> null != e.getTagId() && e.getTagId() > 1)
				.collect(Collectors.groupingBy(XMLPIUIDStructureEntity::getTagParentId,
						Collectors.mapping(XMLPIUIDStructureEntity::getTagId, Collectors.toSet())));

		Document d = new Document();
		CustomerCreditTransferInitiationV03 CstmrCdtTrfInitn = new CustomerCreditTransferInitiationV03();
		setGrpHdr(CstmrCdtTrfInitn, tagMap);
		setPmtInf(CstmrCdtTrfInitn, tagMap);
		d.setCstmrCdtTrfInitn(CstmrCdtTrfInitn);
		return d;
	}

	/**
	 * 
	 * "pmtInfId", "pmtMtd", "btchBookg", "nbOfTxs", "ctrlSum", "pmtTpInf",
	 * "reqdExctnDt", "poolgAdjstmntDt", "dbtr", "dbtrAcct", "dbtrAgt",
	 * "dbtrAgtAcct", "ultmtDbtr", "chrgBr", "chrgsAcct", "chrgsAcctAgt",
	 * "cdtTrfTxInf"
	 * 
	 */
	private void setPmtInf(CustomerCreditTransferInitiationV03 cstmrCdtTrfInitn, Map<Integer, Set<Integer>> tagMap) {
		// if (isTagPresent(tagMap, PAIN001Constant.CSTMRCDTTRFINITN_2,
		// PAIN001Constant.PMTINF_9)) {
		List<PaymentInstructionInformation3> PmtInfL = new ArrayList<>(1);
		PaymentInstructionInformation3 PmtInf = new PaymentInstructionInformation3();
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.PMTINFID_10)) {
			PmtInf.setPmtInfId("00000");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.PMTMTD_11)) {
			PmtInf.setPmtMtd(PaymentMethod3Code.CHK.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.BTCHBOOKG_185)) {
			PmtInf.setBtchBookg("FALSE");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.NBOFTXS_186)) {
			PmtInf.setNbOfTxs("00");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.CTRLSUM_187)) {
			PmtInf.setCtrlSum(new BigDecimal("000000"));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.PMTTPINF_12)) {
			setPmtTpInf(tagMap, PmtInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.REQDEXCTNDT_20)) {
			PmtInf.setReqdExctnDt(LocalDate.now());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.POOLGADJSTMNTDT_565)) {
			PmtInf.setPoolgAdjstmntDt(LocalDate.now());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.DBTR_21)) {
			PmtInf.setDbtr(setDbtrPartyIdentificatio32(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.DBTRACCT_23)) {
			PmtInf.setDbtrAcct(setDbtrCashAccnt16(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.DBTRAGT_27)) {
			PmtInf.setDbtrAgt(setDbtrAgtBrFiId4(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.DBTRAGTACCT_220)) {
			PmtInf.setDbtrAgtAcct(setDbtrAgtCashAccnt16(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.ULTMTDBTR_234)) {
			PmtInf.setUltmtDbtr(setPartyIdentificatio32(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.CHRGBR_256)) {
			PmtInf.setChrgBr(ChargeBearerType1Code.CRED.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.CHRGSACCT_258)) {
			PmtInf.setChrgsAcct(setChrgsCashAccnt16(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.CHRGSACCTAGT_286)) {
			PmtInf.setChrgsAcctAgt(setBrFiId4(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTINF_9, PAIN001Constant.CDTTRFTXINF_31)) {
			setCdtTrfTxInf(tagMap, PmtInf);
		}

		PmtInfL.add(PmtInf);
		cstmrCdtTrfInitn.setPmtInf(PmtInfL);
		// }
	}

	/*
	 * "pmtId", "pmtTpInf", "amt", "xchgRateInf", "chrgBr", "chqInstr", "ultmtDbtr",
	 * "intrmyAgt1", "intrmyAgt1Acct", "intrmyAgt2", "intrmyAgt2Acct", "intrmyAgt3",
	 * "intrmyAgt3Acct", "cdtrAgt", "cdtrAgtAcct", "cdtr", "cdtrAcct", "ultmtCdtr",
	 * "instrForCdtrAgt", "instrForDbtrAgt", "purp", "rgltryRptg", "tax",
	 * "rltdRmtInf", "rmtInf"
	 */
	private void setCdtTrfTxInf(Map<Integer, Set<Integer>> tagMap, PaymentInstructionInformation3 PmtInf) {
		List<CreditTransferTransactionInformation10> CdtTrfTxInfL = new ArrayList<>(1);
		CreditTransferTransactionInformation10 CdtTrfTxInf = new CreditTransferTransactionInformation10();
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.PMTID_32)) {
			setPmtId(tagMap, CdtTrfTxInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.PMTTPINF_324)) {
			setCdTrfTxInfPmtTpInf(tagMap, CdtTrfTxInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.AMT_34)) {
			setAmt(tagMap, CdtTrfTxInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.XCHGRATEINF_350)) {
			setXchgRateInf(tagMap, CdtTrfTxInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.CHRGBR_83)) {
			CdtTrfTxInf.setChrgBr(ChargeBearerType1Code.DEBT.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.CHQINSTR_358)) {
			setChqInstr(tagMap, CdtTrfTxInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.ULTMTDBTR_432)) {
			CdtTrfTxInf.setUltmtDbtr(setUltmtDbtrPartyIdentificatio32(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.INTRMYAGT1_84)) {
			CdtTrfTxInf.setIntrmyAgt1(setIntrmyAgt1BrFiId4(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.INTRMYAGT1ACCT_92)) {
			CdtTrfTxInf.setIntrmyAgt1Acct(setIntrmyAgt1AcctCashAccnt16(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.INTRMYAGT3_279)) {
			CdtTrfTxInf.setIntrmyAgt3(setIntrmyAgt3BrFiId4(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.INTRMYAGT3ACCT_315)) {
			CdtTrfTxInf.setIntrmyAgt3Acct(setIntrmyAgt3AcctCashAccnt16(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.CDTRAGT_36)) {
			CdtTrfTxInf.setCdtrAgt(setCdtrAgtBrFiId4(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.CDTRAGTACCT_361)) {
			CdtTrfTxInf.setCdtrAgtAcct(setCdtrAgtAcctCashAccnt16(tagMap));
		}

		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.CDTR_40)) {
			CdtTrfTxInf.setCdtr(setCdtrPartyIdentificatio32(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.CDTRACCT_42)) {
			CdtTrfTxInf.setCdtrAcct(setCdtrCashAccnt16(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.ULTMTCDTR_417)) {
			CdtTrfTxInf.setUltmtCdtr(setUltmtCdtrPartyIdentificatio32(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.INSTRFORCDTRAGT_112)) {
			setInstrForCdtrAgtL(tagMap, CdtTrfTxInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.INSTRFORDBTRAGT_114)) {
			CdtTrfTxInf.setInstrForDbtrAgt("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.PURP_141)) {
			setPurp(tagMap, CdtTrfTxInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.RGLTRYRPTG_653)) {
			setRgltryRptgL(tagMap, CdtTrfTxInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.TAX_120)) {
			setTax(tagMap, CdtTrfTxInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.RLTDRMTINF_143)) {
			setRltdRmtInfL(tagMap, CdtTrfTxInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTTRFTXINF_31, PAIN001Constant.RMTINF_52)) {
			setRmtInf(tagMap, CdtTrfTxInf);
		}

		CdtTrfTxInfL.add(CdtTrfTxInf);
		PmtInf.setCdtTrfTxInf(CdtTrfTxInfL);
	}

	/*
	 * "ustrd", "strd"
	 */
	private void setRmtInf(Map<Integer, Set<Integer>> tagMap, CreditTransferTransactionInformation10 CdtTrfTxInf) {
		RemittanceInformation5 RmtInf = new RemittanceInformation5();
		if (isTagPresent(tagMap, PAIN001Constant.RMTINF_52, PAIN001Constant.USTRD_53)) {
			List<String> ustrd = new ArrayList<>(1);
			ustrd.add("XXXX");
			RmtInf.setUstrd(ustrd);
		}
		if (isTagPresent(tagMap, PAIN001Constant.RMTINF_52, PAIN001Constant.STRD_54)) {
			setStrdL(tagMap, RmtInf);
		}
		CdtTrfTxInf.setRmtInf(RmtInf);
	}

	/*
	 * "rfrdDocInf", "rfrdDocAmt", "cdtrRefInf", "invcr", "invcee", "addtlRmtInf"
	 */
	private void setStrdL(Map<Integer, Set<Integer>> tagMap, RemittanceInformation5 RmtInf) {
		List<StructuredRemittanceInformation7> strdL = new ArrayList<>();
		StructuredRemittanceInformation7 strd = new StructuredRemittanceInformation7();
		if (isTagPresent(tagMap, PAIN001Constant.STRD_54, PAIN001Constant.RFRDDOCINF_55)) {
			setRfrdDocInfL(tagMap, strd);
		}
		if (isTagPresent(tagMap, PAIN001Constant.STRD_54, PAIN001Constant.RFRDDOCAMT_61)) {
			setRfrdDocAmt(tagMap, strd);
		}
		if (isTagPresent(tagMap, PAIN001Constant.STRD_54, PAIN001Constant.CDTRREFINF_67)) {
			setCdtrRefInf(tagMap, strd);
		}
		if (isTagPresent(tagMap, PAIN001Constant.STRD_54, PAIN001Constant.INVCR_498)) {
			strd.setInvcr(setInvcrPartyIdentificatio32(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.STRD_54, PAIN001Constant.INVCEE_511)) {
			strd.setInvcee(setInvceePartyIdentificatio32(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.STRD_54, PAIN001Constant.ADDTLRMTINF_72)) {
			List<String> addtlRmtInf = new ArrayList<>();
			addtlRmtInf.add("XXX");
			strd.setAddtlRmtInf(addtlRmtInf);
		}

		strdL.add(strd);
		RmtInf.setStrd(strdL);
	}

	/*
	 * "tp", "nb", "rltdDt"
	 */
	private void setRfrdDocInfL(Map<Integer, Set<Integer>> tagMap, StructuredRemittanceInformation7 strd) {
		List<ReferredDocumentInformation3> rfrdDocInfL = new ArrayList<>();
		ReferredDocumentInformation3 rfrdDocInf = new ReferredDocumentInformation3();

		if (isTagPresent(tagMap, PAIN001Constant.RFRDDOCINF_55, PAIN001Constant.TP_56)) {
			seTp(tagMap, rfrdDocInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.RFRDDOCINF_55, PAIN001Constant.NB_59)) {
			rfrdDocInf.setNb("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.RFRDDOCINF_55, PAIN001Constant.RLTDDT_60)) {
			rfrdDocInf.setRltdDt(LocalDate.now());
		}
		rfrdDocInfL.add(rfrdDocInf);
		strd.setRfrdDocInf(rfrdDocInfL);
	}

	/*
	 * "cdOrPrtry", "issr"
	 */
	private void seTp(Map<Integer, Set<Integer>> tagMap, ReferredDocumentInformation3 rfrdDocInf) {
		ReferredDocumentType2 tp = new ReferredDocumentType2();
		if (isTagPresent(tagMap, PAIN001Constant.TP_56, PAIN001Constant.CDORPRTRY_57)) {
			setCdOrPrtry(tagMap, tp);
		}
		if (isTagPresent(tagMap, PAIN001Constant.TP_56, PAIN001Constant.ISSR_490)) {
			tp.setIssr("XXX");
		}
		rfrdDocInf.setTp(tp);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setCdOrPrtry(Map<Integer, Set<Integer>> tagMap, ReferredDocumentType2 tp) {
		ReferredDocumentType1Choice cdOrPrtry = new ReferredDocumentType1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.CDORPRTRY_57, PAIN001Constant.CD_58)) {
			cdOrPrtry.setCd(DocumentType5Code.AROI.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDORPRTRY_57, PAIN001Constant.PRTRY_489)) {
			cdOrPrtry.setPrtry("XXXX");
		}

		tp.setCdOrPrtry(cdOrPrtry);
	}

	/*
	 * "tp", "ref"
	 */
	private void setCdtrRefInf(Map<Integer, Set<Integer>> tagMap, StructuredRemittanceInformation7 strd) {
		CreditorReferenceInformation2 cdtrRefInf = new CreditorReferenceInformation2();

		if (isTagPresent(tagMap, PAIN001Constant.CDTRREFINF_67, PAIN001Constant.TP_68)) {
			setTp(tagMap, cdtrRefInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTRREFINF_67, PAIN001Constant.REF_71)) {
			cdtrRefInf.setRef("XXXX");
		}
		strd.setCdtrRefInf(cdtrRefInf);
	}

	/*
	 * "cdOrPrtry", "issr"
	 */
	private void setTp(Map<Integer, Set<Integer>> tagMap, CreditorReferenceInformation2 cdtrRefInf) {
		CreditorReferenceType2 tp = new CreditorReferenceType2();
		if (isTagPresent(tagMap, PAIN001Constant.TP_68, PAIN001Constant.CDORPRTRY_69)) {
			setCdOrPrtry(tagMap, tp);
		}
		if (isTagPresent(tagMap, PAIN001Constant.TP_68, PAIN001Constant.ISSR_497)) {
			tp.setIssr("XXXX");
		}

		cdtrRefInf.setTp(tp);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setCdOrPrtry(Map<Integer, Set<Integer>> tagMap, CreditorReferenceType2 tp) {
		CreditorReferenceType1Choice cdOrPrtry = new CreditorReferenceType1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.CDORPRTRY_69, PAIN001Constant.CD_70)) {
			cdOrPrtry.setCd(DocumentType3Code.DISP.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDORPRTRY_69, PAIN001Constant.PRTRY_496)) {
			cdOrPrtry.setPrtry("XXX");
		}

		tp.setCdOrPrtry(cdOrPrtry);
	}

	/*
	 * "duePyblAmt", "dscntApldAmt", "cdtNoteAmt", "taxAmt", "adjstmntAmtAndRsn",
	 * "rmtdAmt"
	 */
	private void setRfrdDocAmt(Map<Integer, Set<Integer>> tagMap, StructuredRemittanceInformation7 strd) {
		RemittanceAmount1 RfrdDocAmt = new RemittanceAmount1();
		if (isTagPresent(tagMap, PAIN001Constant.RFRDDOCAMT_61, PAIN001Constant.DUEPYBLAMT_62)) {
			RfrdDocAmt.setDuePyblAmt(setActiveOrHstCurrAmt());
		}
		if (isTagPresent(tagMap, PAIN001Constant.RFRDDOCAMT_61, PAIN001Constant.DSCNTAPLDAMT_63)) {
			RfrdDocAmt.setDscntApldAmt(setActiveOrHstCurrAmt());
		}
		if (isTagPresent(tagMap, PAIN001Constant.RFRDDOCAMT_61, PAIN001Constant.CDTNOTEAMT_64)) {
			RfrdDocAmt.setCdtNoteAmt(setActiveOrHstCurrAmt());
		}
		if (isTagPresent(tagMap, PAIN001Constant.RFRDDOCAMT_61, PAIN001Constant.TAXAMT_65)) {
			RfrdDocAmt.setTaxAmt(setActiveOrHstCurrAmt());
		}
		if (isTagPresent(tagMap, PAIN001Constant.RFRDDOCAMT_61, PAIN001Constant.ADJSTMNTAMTANDRSN_491)) {
			setAdjstmntAmtAndRsnL(tagMap, RfrdDocAmt);
		}
		if (isTagPresent(tagMap, PAIN001Constant.RFRDDOCAMT_61, PAIN001Constant.RMTDAMT_66)) {
			RfrdDocAmt.setRmtdAmt(setActiveOrHstCurrAmt());
		}

		strd.setRfrdDocAmt(RfrdDocAmt);
	}

	/*
	 * "amt", "cdtDbtInd", "rsn", "addtlInf"
	 */
	private void setAdjstmntAmtAndRsnL(Map<Integer, Set<Integer>> tagMap, RemittanceAmount1 RfrdDocAmt) {
		List<DocumentAdjustment1> adjstmntAmtAndRsnL = new ArrayList<>();
		DocumentAdjustment1 adjstmntAmtAndRsn = new DocumentAdjustment1();
		if (isTagPresent(tagMap, PAIN001Constant.ADJSTMNTAMTANDRSN_491, PAIN001Constant.AMT_492)) {
			adjstmntAmtAndRsn.setAmt(setActiveOrHstCurrAmt());
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADJSTMNTAMTANDRSN_491, PAIN001Constant.CDTDBTIND_493)) {
			adjstmntAmtAndRsn.setCdtDbtInd(CreditDebitCode.CRDT.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADJSTMNTAMTANDRSN_491, PAIN001Constant.RSN_494)) {
			adjstmntAmtAndRsn.setRsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADJSTMNTAMTANDRSN_491, PAIN001Constant.ADDTLINF_495)) {
			adjstmntAmtAndRsn.setAddtlInf("XXXX");
		}

		adjstmntAmtAndRsnL.add(adjstmntAmtAndRsn);
		RfrdDocAmt.setAdjstmntAmtAndRsn(adjstmntAmtAndRsnL);
	}

	/*
	 * "rmtId", "rmtLctnMtd", "rmtLctnElctrncAdr", "rmtLctnPstlAdr"
	 */
	private void setRltdRmtInfL(Map<Integer, Set<Integer>> tagMap, CreditTransferTransactionInformation10 CdtTrfTxInf) {
		List<RemittanceLocation2> RltdRmtInfL = new ArrayList<>(1);
		RemittanceLocation2 RltdRmtInf = new RemittanceLocation2();
		if (isTagPresent(tagMap, PAIN001Constant.RLTDRMTINF_143, PAIN001Constant.RMTID_475)) {
			RltdRmtInf.setRmtId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.RLTDRMTINF_143, PAIN001Constant.RMTLCTNMTD_144)) {
			RltdRmtInf.setRmtLctnMtd(RemittanceLocationMethod2Code.EDIC.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.RLTDRMTINF_143, PAIN001Constant.RMTLCTNELCTRNCADR_145)) {
			RltdRmtInf.setRmtLctnElctrncAdr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.RLTDRMTINF_143, PAIN001Constant.RMTLCTNPSTLADR_476)) {
			RltdRmtInf.setRmtLctnPstlAdr(setNameAndAddr10(tagMap));
		}

		RltdRmtInfL.add(RltdRmtInf);
		CdtTrfTxInf.setRltdRmtInf(RltdRmtInfL);
	}

	/*
	 * "cdtr", "dbtr", "admstnZn", "refNb", "mtd", "ttlTaxblBaseAmt", "ttlTaxAmt",
	 * "dt", "seqNb", "rcrd"
	 */
	private void setTax(Map<Integer, Set<Integer>> tagMap, CreditTransferTransactionInformation10 CdtTrfTxInf) {
		TaxInformation3 tax = new TaxInformation3();

		if (isTagPresent(tagMap, PAIN001Constant.TAX_120, PAIN001Constant.CDTR_121)) {
			setCdtr(tagMap, tax);
		}
		if (isTagPresent(tagMap, PAIN001Constant.TAX_120, PAIN001Constant.DBTR_123)) {
			setDbtr(tagMap, tax);
		}
		if (isTagPresent(tagMap, PAIN001Constant.TAX_120, PAIN001Constant.ADMSTNZN_458)) {
			tax.setAdmstnZn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.TAX_120, PAIN001Constant.REFNB_459)) {
			tax.setRefNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.TAX_120, PAIN001Constant.MTD_460)) {
			tax.setMtd("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.TAX_120, PAIN001Constant.TTLTAXBLBASEAMT_461)) {
			tax.setTtlTaxblBaseAmt(setActiveOrHstCurrAmt());
		}
		if (isTagPresent(tagMap, PAIN001Constant.TAX_120, PAIN001Constant.TTLTAXAMT_462)) {
			tax.setTtlTaxAmt(setActiveOrHstCurrAmt());
		}
		if (isTagPresent(tagMap, PAIN001Constant.TAX_120, PAIN001Constant.DT_127)) {
			tax.setDt(LocalDate.now());
		}
		if (isTagPresent(tagMap, PAIN001Constant.TAX_120, PAIN001Constant.SEQNB_463)) {
			tax.setSeqNb(new BigDecimal("00"));
		}
		if (isTagPresent(tagMap, PAIN001Constant.TAX_120, PAIN001Constant.RCRD_125)) {
			setRcrdL(tagMap, tax);
		}
		CdtTrfTxInf.setTax(tax);
	}

	/*
	 * "tp", "ctgy", "ctgyDtls", "dbtrSts", "certId", "frmsCd", "prd", "taxAmt",
	 * "addtlInf"
	 */
	private void setRcrdL(Map<Integer, Set<Integer>> tagMap, TaxInformation3 tax) {
		List<TaxRecord1> rcrdL = new ArrayList<>(1);
		TaxRecord1 rcrd = new TaxRecord1();
		if (isTagPresent(tagMap, PAIN001Constant.RCRD_125, PAIN001Constant.TP_464)) {
			rcrd.setTp("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.RCRD_125, PAIN001Constant.CTGY_465)) {
			rcrd.setCtgy("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.RCRD_125, PAIN001Constant.CTGYDTLS_126)) {
			rcrd.setCtgyDtls("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.RCRD_125, PAIN001Constant.DBTRSTS_466)) {
			rcrd.setDbtrSts("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.RCRD_125, PAIN001Constant.CERTID_467)) {
			rcrd.setCertId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.RCRD_125, PAIN001Constant.FRMSCD_468)) {
			rcrd.setFrmsCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.RCRD_125, PAIN001Constant.PRD_469)) {
			rcrd.setPrd(setRcrdTaxPeriod1(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.RCRD_125, PAIN001Constant.TAXAMT_128)) {
			setTaxAmt(tagMap, rcrd);
		}
		if (isTagPresent(tagMap, PAIN001Constant.RCRD_125, PAIN001Constant.ADDTLINF_474)) {
			rcrd.setAddtlInf("XXX");
		}
		rcrdL.add(rcrd);
		tax.setRcrd(rcrdL);
	}

	/*
	 * "rate", "taxblBaseAmt", "ttlAmt", "dtls"
	 */
	private void setTaxAmt(Map<Integer, Set<Integer>> tagMap, TaxRecord1 rcrd) {
		TaxAmount1 taxAmt = new TaxAmount1();
		if (isTagPresent(tagMap, PAIN001Constant.TAXAMT_128, PAIN001Constant.RATE_470)) {
			taxAmt.setRate(new BigDecimal("00"));
		}
		if (isTagPresent(tagMap, PAIN001Constant.TAXAMT_128, PAIN001Constant.TAXBLBASEAMT_471)) {
			taxAmt.setTaxblBaseAmt(setActiveOrHstCurrAmt());
		}
		if (isTagPresent(tagMap, PAIN001Constant.TAXAMT_128, PAIN001Constant.TTLAMT_472)) {
			taxAmt.setTtlAmt(setActiveOrHstCurrAmt());
		}
		if (isTagPresent(tagMap, PAIN001Constant.TAXAMT_128, PAIN001Constant.DTLS_129)) {
			setDTLSL(tagMap, taxAmt);
		}
		rcrd.setTaxAmt(taxAmt);
	}

	/*
	 * "prd", "amt"
	 */
	private void setDTLSL(Map<Integer, Set<Integer>> tagMap, TaxAmount1 taxAmt) {
		List<TaxRecordDetails1> DTLSL = new ArrayList<>(1);
		TaxRecordDetails1 DTLS = new TaxRecordDetails1();
		if (isTagPresent(tagMap, PAIN001Constant.DTLS_129, PAIN001Constant.PRD_473)) {
			DTLS.setPrd(setTaxPeriod1(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTLS_129, PAIN001Constant.AMT_130)) {
			DTLS.setAmt(setActiveOrHstCurrAmt());
		}
		DTLSL.add(DTLS);
		taxAmt.setDtls(DTLSL);
	}

	/*
	 * "yr", "tp", "frToDt"
	 */
	private TaxPeriod1 setTaxPeriod1(Map<Integer, Set<Integer>> tagMap) {
		TaxPeriod1 prd = new TaxPeriod1();

		if (isTagPresent(tagMap, PAIN001Constant.PRD_473, PAIN001Constant.YR_764)) {
			prd.setYr(LocalDate.now());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PRD_473, PAIN001Constant.TP_765)) {
			prd.setTp(TaxRecordPeriod1Code.HLF_2.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PRD_473, PAIN001Constant.FRTODT_766)) {
			prd.setFrToDt(setTaxPersionFrToDt(tagMap));
		}
		return prd;
	}

	private DatePeriodDetails setRcrdTaxPersionFrToDt(Map<Integer, Set<Integer>> tagMap) {
		DatePeriodDetails DatePeriodDetails = new DatePeriodDetails();
		if (isTagPresent(tagMap, PAIN001Constant.FRTODT_763, PAIN001Constant.FRDT_829)) {
			DatePeriodDetails.setFrDt(LocalDate.now());
		}
		if (isTagPresent(tagMap, PAIN001Constant.FRTODT_763, PAIN001Constant.TODT_830)) {
			DatePeriodDetails.setToDt(LocalDate.now());
		}
		return DatePeriodDetails;
	}

	private DatePeriodDetails setTaxPersionFrToDt(Map<Integer, Set<Integer>> tagMap) {
		DatePeriodDetails DatePeriodDetails = new DatePeriodDetails();
		if (isTagPresent(tagMap, PAIN001Constant.FRTODT_766, PAIN001Constant.FRDT_827)) {
			DatePeriodDetails.setFrDt(LocalDate.now());
		}
		if (isTagPresent(tagMap, PAIN001Constant.FRTODT_766, PAIN001Constant.TODT_828)) {
			DatePeriodDetails.setToDt(LocalDate.now());
		}
		return DatePeriodDetails;
	}

	/*
	 * "yr", "tp", "frToDt"
	 */
	private TaxPeriod1 setRcrdTaxPeriod1(Map<Integer, Set<Integer>> tagMap) {
		TaxPeriod1 prd = new TaxPeriod1();

		if (isTagPresent(tagMap, PAIN001Constant.PRD_469, PAIN001Constant.YR_761)) {
			prd.setYr(LocalDate.now());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PRD_469, PAIN001Constant.TP_762)) {
			prd.setTp(TaxRecordPeriod1Code.HLF_1.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PRD_469, PAIN001Constant.FRTODT_763)) {
			prd.setFrToDt(setRcrdTaxPersionFrToDt(tagMap));
		}
		return prd;
	}

	/*
	 * "taxId", "regnId", "taxTp", "authstn"
	 */
	private void setDbtr(Map<Integer, Set<Integer>> tagMap, TaxInformation3 tax) {
		TaxParty2 dbtr = new TaxParty2();
		if (isTagPresent(tagMap, PAIN001Constant.DBTR_123, PAIN001Constant.TAXID_124)) {
			dbtr.setTaxId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTR_123, PAIN001Constant.REGNID_449)) {
			dbtr.setRegnId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTR_123, PAIN001Constant.TAXTP_451)) {
			dbtr.setTaxTp("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTR_123, PAIN001Constant.AUTHSTN_453)) {
			setAuthstn(tagMap, dbtr);
		}
		tax.setDbtr(dbtr);
	}

	/*
	 * "titl", "nm"
	 */
	private void setAuthstn(Map<Integer, Set<Integer>> tagMap, TaxParty2 dbtr) {
		TaxAuthorisation1 authstn = new TaxAuthorisation1();
		if (isTagPresent(tagMap, PAIN001Constant.AUTHSTN_453, PAIN001Constant.TITL_455)) {
			authstn.setTitl("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.AUTHSTN_453, PAIN001Constant.NM_457)) {
			authstn.setNm("XXX");
		}
		dbtr.setAuthstn(authstn);
	}

	/*
	 * "taxId", "regnId", "taxTp"
	 */
	private void setCdtr(Map<Integer, Set<Integer>> tagMap, TaxInformation3 tax) {
		TaxParty1 cdtr = new TaxParty1();
		if (isTagPresent(tagMap, PAIN001Constant.CDTR_121, PAIN001Constant.TAXID_445)) {
			cdtr.setTaxId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTR_121, PAIN001Constant.REGNID_447)) {
			cdtr.setRegnId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTR_121, PAIN001Constant.TAXTP_122)) {
			cdtr.setTaxTp("XXXX");
		}
		tax.setCdtr(cdtr);
	}

	/*
	 * "dbtCdtRptgInd", "authrty", "dtls"
	 */
	private void setRgltryRptgL(Map<Integer, Set<Integer>> tagMap, CreditTransferTransactionInformation10 CdtTrfTxInf) {
		List<RegulatoryReporting3> RgltryRptgL = new ArrayList<>(1);
		RegulatoryReporting3 RgltryRptg = new RegulatoryReporting3();

		if (isTagPresent(tagMap, PAIN001Constant.RGLTRYRPTG_653, PAIN001Constant.DBTCDTRPTGIND_654)) {
			RgltryRptg.setDbtCdtRptgInd(RegulatoryReportingType1Code.CRED.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.RGLTRYRPTG_653, PAIN001Constant.AUTHRTY_655)) {
			setAuthrty(tagMap, RgltryRptg);
		}
		if (isTagPresent(tagMap, PAIN001Constant.RGLTRYRPTG_653, PAIN001Constant.DTLS_656)) {
			setDTLSL(tagMap, RgltryRptg);
		}
		CdtTrfTxInf.setRgltryRptg(RgltryRptgL);
	}

	/*
	 * "tp", "dt", "ctry", "cd", "amt", "inf"
	 */
	private void setDTLSL(Map<Integer, Set<Integer>> tagMap, RegulatoryReporting3 RgltryRptg) {
		List<StructuredRegulatoryReporting3> DTLSL = new ArrayList<>(1);
		StructuredRegulatoryReporting3 DTLS = new StructuredRegulatoryReporting3();

		if (isTagPresent(tagMap, PAIN001Constant.DTLS_656, PAIN001Constant.TP_659)) {
			DTLS.setTp("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTLS_656, PAIN001Constant.DT_660)) {
			DTLS.setDt(LocalDate.now());
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTLS_656, PAIN001Constant.CTRY_661)) {
			DTLS.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTLS_656, PAIN001Constant.CD_662)) {
			DTLS.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTLS_656, PAIN001Constant.AMT_663)) {
			DTLS.setAmt(setActiveOrHstCurrAmt());
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTLS_656, PAIN001Constant.INF_664)) {
			List<String> inf = new ArrayList<>(1);
			inf.add("XXXX");
			DTLS.setInf(inf);
		}

		DTLSL.add(DTLS);
		RgltryRptg.setDtls(DTLSL);
	}

	/*
	 * "nm", "ctry"
	 */
	private void setAuthrty(Map<Integer, Set<Integer>> tagMap, RegulatoryReporting3 RgltryRptg) {
		RegulatoryAuthority2 Authrty = new RegulatoryAuthority2();

		if (isTagPresent(tagMap, PAIN001Constant.AUTHRTY_655, PAIN001Constant.NM_657)) {
			Authrty.setNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.AUTHRTY_655, PAIN001Constant.CTRY_658)) {
			Authrty.setCtry("XXXX");
		}
		RgltryRptg.setAuthrty(Authrty);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setPurp(Map<Integer, Set<Integer>> tagMap, CreditTransferTransactionInformation10 CdtTrfTxInf) {
		Purpose2Choice purp = new Purpose2Choice();
		if (isTagPresent(tagMap, PAIN001Constant.PURP_141, PAIN001Constant.CD_443)) {
			purp.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PURP_141, PAIN001Constant.PRTRY_142)) {
			purp.setPrtry("XXXXX");
		}
		CdtTrfTxInf.setPurp(purp);
	}

	/*
	 * "cd", "instrInf"
	 */
	private void setInstrForCdtrAgtL(Map<Integer, Set<Integer>> tagMap,
			CreditTransferTransactionInformation10 CdtTrfTxInf) {
		List<InstructionForCreditorAgent1> InstrForCdtrAgtL = new ArrayList<>(1);
		InstructionForCreditorAgent1 InstrForCdtrAgt = new InstructionForCreditorAgent1();
		if (isTagPresent(tagMap, PAIN001Constant.INSTRFORCDTRAGT_112, PAIN001Constant.CD_118)) {
			InstrForCdtrAgt.setCd(Instruction3Code.CHQB.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.INSTRFORCDTRAGT_112, PAIN001Constant.INSTRINF_113)) {
			InstrForCdtrAgt.setInstrInf("XXXXX");
		}
		InstrForCdtrAgtL.add(InstrForCdtrAgt);
		CdtTrfTxInf.setInstrForCdtrAgt(InstrForCdtrAgtL);
	}

	/*
	 * "id", "tp", "ccy", "nm"
	 */
	private CashAccount16 setCdtrCashAccnt16(Map<Integer, Set<Integer>> tagMap) {
		CashAccount16 IntrmyAgt1Acct = new CashAccount16();

		if (isTagPresent(tagMap, PAIN001Constant.CDTRACCT_42, PAIN001Constant.ID_43)) {
			setCdtrId(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTRACCT_42, PAIN001Constant.TP_77)) {
			setCdtrCashAccnt16Tp(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTRACCT_42, PAIN001Constant.CCY_415)) {
			IntrmyAgt1Acct.setCcy("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTRACCT_42, PAIN001Constant.NM_46)) {
			IntrmyAgt1Acct.setNm("XXXX");
		}
		return IntrmyAgt1Acct;
	}

	private CashAccount16 setDbtrCashAccnt16(Map<Integer, Set<Integer>> tagMap) {
		CashAccount16 IntrmyAgt1Acct = new CashAccount16();

		if (isTagPresent(tagMap, PAIN001Constant.DBTRACCT_23, PAIN001Constant.ID_24)) {
			setDbtrId(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTRACCT_23, PAIN001Constant.TP_204)) {
			setDTp(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTRACCT_23, PAIN001Constant.CCY_207)) {
			IntrmyAgt1Acct.setCcy("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTRACCT_23, PAIN001Constant.NM_208)) {
			IntrmyAgt1Acct.setNm("XXXX");
		}
		return IntrmyAgt1Acct;
	}

	private CashAccount16 setChrgsCashAccnt16(Map<Integer, Set<Integer>> tagMap) {
		CashAccount16 IntrmyAgt1Acct = new CashAccount16();

		if (isTagPresent(tagMap, PAIN001Constant.CHRGSACCT_258, PAIN001Constant.ID_260)) {
			setChrgsCashAccntId(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHRGSACCT_258, PAIN001Constant.TP_276)) {
			setChrgsCashAccnTp(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHRGSACCT_258, PAIN001Constant.CCY_282)) {
			IntrmyAgt1Acct.setCcy("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHRGSACCT_258, PAIN001Constant.NM_284)) {
			IntrmyAgt1Acct.setNm("XXXX");
		}
		return IntrmyAgt1Acct;
	}

	private CashAccount16 setDbtrAgtCashAccnt16(Map<Integer, Set<Integer>> tagMap) {
		CashAccount16 IntrmyAgt1Acct = new CashAccount16();

		if (isTagPresent(tagMap, PAIN001Constant.DBTRAGTACCT_220, PAIN001Constant.ID_221)) {
			setId(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTRAGTACCT_220, PAIN001Constant.TP_229)) {
			setTp(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTRAGTACCT_220, PAIN001Constant.CCY_232)) {
			IntrmyAgt1Acct.setCcy("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTRAGTACCT_220, PAIN001Constant.NM_233)) {
			IntrmyAgt1Acct.setNm("XXXX");
		}
		return IntrmyAgt1Acct;
	}

	private CashAccount16 setIntrmyAgt1AcctCashAccnt16(Map<Integer, Set<Integer>> tagMap) {
		CashAccount16 IntrmyAgt1Acct = new CashAccount16();

		if (isTagPresent(tagMap, PAIN001Constant.INTRMYAGT1ACCT_92, PAIN001Constant.ID_93)) {
			setIntrmyAgt1AcctId(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.INTRMYAGT1ACCT_92, PAIN001Constant.TP_269)) {
			setIntrmyAgt1AcctTp(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.INTRMYAGT1ACCT_92, PAIN001Constant.CCY_275)) {
			IntrmyAgt1Acct.setCcy("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.INTRMYAGT1ACCT_92, PAIN001Constant.NM_277)) {
			IntrmyAgt1Acct.setNm("XXXX");
		}
		return IntrmyAgt1Acct;
	}

	private CashAccount16 setIntrmyAgt3AcctCashAccnt16(Map<Integer, Set<Integer>> tagMap) {
		CashAccount16 IntrmyAgt1Acct = new CashAccount16();

		if (isTagPresent(tagMap, PAIN001Constant.INTRMYAGT3ACCT_315, PAIN001Constant.ID_317)) {
			setIntrmyAgt3AcctId(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.INTRMYAGT3ACCT_315, PAIN001Constant.TP_333)) {
			setIntrmyAgt3AcctTp(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.INTRMYAGT3ACCT_315, PAIN001Constant.CCY_339)) {
			IntrmyAgt1Acct.setCcy("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.INTRMYAGT3ACCT_315, PAIN001Constant.NM_341)) {
			IntrmyAgt1Acct.setNm("XXXX");
		}
		return IntrmyAgt1Acct;
	}

	private CashAccount16 setCdtrAgtAcctCashAccnt16(Map<Integer, Set<Integer>> tagMap) {
		CashAccount16 IntrmyAgt1Acct = new CashAccount16();

		if (isTagPresent(tagMap, PAIN001Constant.CDTRAGTACCT_361, PAIN001Constant.ID_363)) {
			setCdtrAgtAcctId(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTRAGTACCT_361, PAIN001Constant.TP_379)) {
			setCdtrAgtAcctTp(tagMap, IntrmyAgt1Acct);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTRAGTACCT_361, PAIN001Constant.CCY_385)) {
			IntrmyAgt1Acct.setCcy("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTRAGTACCT_361, PAIN001Constant.NM_387)) {
			IntrmyAgt1Acct.setNm("XXXX");
		}
		return IntrmyAgt1Acct;
	}

	/*
	 * "cd", "prtry"
	 */
	private void setChrgsCashAccnTp(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		CashAccountType2 Tp = new CashAccountType2();
		if (isTagPresent(tagMap, PAIN001Constant.TP_276, PAIN001Constant.CD_278)) {
			Tp.setCd(CashAccountType4Code.CACC.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.TP_276, PAIN001Constant.PRTRY_280)) {
			Tp.setPrtry("XXXXX");
		}
		IntrmyAgt1Acct.setTp(Tp);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setIntrmyAgt3AcctTp(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		CashAccountType2 Tp = new CashAccountType2();
		if (isTagPresent(tagMap, PAIN001Constant.TP_333, PAIN001Constant.CD_335)) {
			Tp.setCd(CashAccountType4Code.CACC.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.TP_333, PAIN001Constant.PRTRY_337)) {
			Tp.setPrtry("XXXXX");
		}
		IntrmyAgt1Acct.setTp(Tp);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setCdtrAgtAcctTp(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		CashAccountType2 Tp = new CashAccountType2();
		if (isTagPresent(tagMap, PAIN001Constant.TP_379, PAIN001Constant.CD_381)) {
			Tp.setCd(CashAccountType4Code.CACC.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.TP_379, PAIN001Constant.PRTRY_383)) {
			Tp.setPrtry("XXXXX");
		}
		IntrmyAgt1Acct.setTp(Tp);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setCdtrCashAccnt16Tp(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		CashAccountType2 Tp = new CashAccountType2();
		if (isTagPresent(tagMap, PAIN001Constant.TP_77, PAIN001Constant.CD_78)) {
			Tp.setCd(CashAccountType4Code.CACC.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.TP_77, PAIN001Constant.PRTRY_117)) {
			Tp.setPrtry("XXXXX");
		}
		IntrmyAgt1Acct.setTp(Tp);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setDTp(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		CashAccountType2 Tp = new CashAccountType2();
		if (isTagPresent(tagMap, PAIN001Constant.TP_204, PAIN001Constant.CD_205)) {
			Tp.setCd(CashAccountType4Code.CACC.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.TP_204, PAIN001Constant.PRTRY_206)) {
			Tp.setPrtry("XXXXX");
		}
		IntrmyAgt1Acct.setTp(Tp);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setTp(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		CashAccountType2 Tp = new CashAccountType2();
		if (isTagPresent(tagMap, PAIN001Constant.TP_229, PAIN001Constant.CD_230)) {
			Tp.setCd(CashAccountType4Code.CACC.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.TP_229, PAIN001Constant.PRTRY_231)) {
			Tp.setPrtry("XXXXX");
		}
		IntrmyAgt1Acct.setTp(Tp);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setIntrmyAgt1AcctTp(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		CashAccountType2 Tp = new CashAccountType2();
		if (isTagPresent(tagMap, PAIN001Constant.TP_269, PAIN001Constant.CD_271)) {
			Tp.setCd(CashAccountType4Code.CACC.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.TP_269, PAIN001Constant.PRTRY_273)) {
			Tp.setPrtry("XXXXX");
		}
		IntrmyAgt1Acct.setTp(Tp);
	}

	/*
	 * "iban", "othr"
	 */
	private void setCdtrId(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		AccountIdentification4Choice Id = new AccountIdentification4Choice();

		if (isTagPresent(tagMap, PAIN001Constant.ID_43, PAIN001Constant.IBAN_405)) {
			Id.setIBAN("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_43, PAIN001Constant.OTHR_44)) {
			setCdtrOthr(tagMap, Id);
		}
		IntrmyAgt1Acct.setId(Id);
	}

	private void setDbtrId(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		AccountIdentification4Choice Id = new AccountIdentification4Choice();

		if (isTagPresent(tagMap, PAIN001Constant.ID_24, PAIN001Constant.IBAN_199)) {
			Id.setIBAN("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_24, PAIN001Constant.OTHR_25)) {
			setDbtrOthr(tagMap, Id);
		}
		IntrmyAgt1Acct.setId(Id);
	}

	private void setChrgsCashAccntId(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		AccountIdentification4Choice Id = new AccountIdentification4Choice();

		if (isTagPresent(tagMap, PAIN001Constant.ID_260, PAIN001Constant.IBAN_262)) {
			Id.setIBAN("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_260, PAIN001Constant.OTHR_264)) {
			setChrgsCashAccntOthr(tagMap, Id);
		}
		IntrmyAgt1Acct.setId(Id);
	}

	private void setIntrmyAgt1AcctId(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		AccountIdentification4Choice Id = new AccountIdentification4Choice();

		if (isTagPresent(tagMap, PAIN001Constant.ID_93, PAIN001Constant.IBAN_259)) {
			Id.setIBAN("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_93, PAIN001Constant.OTHR_94)) {
			setIntrmyAgt1AcctOthr(tagMap, Id);
		}
		IntrmyAgt1Acct.setId(Id);
	}

	private void setIntrmyAgt3AcctId(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		AccountIdentification4Choice Id = new AccountIdentification4Choice();

		if (isTagPresent(tagMap, PAIN001Constant.ID_317, PAIN001Constant.IBAN_319)) {
			Id.setIBAN("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_317, PAIN001Constant.OTHR_321)) {
			setIntrmyAgt3AcctOthr(tagMap, Id);
		}
		IntrmyAgt1Acct.setId(Id);
	}

	private void setCdtrAgtAcctId(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		AccountIdentification4Choice Id = new AccountIdentification4Choice();

		if (isTagPresent(tagMap, PAIN001Constant.ID_363, PAIN001Constant.IBAN_365)) {
			Id.setIBAN("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_363, PAIN001Constant.OTHR_367)) {
			setCdtrAgtAcctOthr(tagMap, Id);
		}
		IntrmyAgt1Acct.setId(Id);
	}

	private void setId(Map<Integer, Set<Integer>> tagMap, CashAccount16 IntrmyAgt1Acct) {
		AccountIdentification4Choice Id = new AccountIdentification4Choice();

		if (isTagPresent(tagMap, PAIN001Constant.ID_221, PAIN001Constant.IBAN_222)) {
			Id.setIBAN("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_221, PAIN001Constant.OTHR_223)) {
			setOthr(tagMap, Id);
		}
		IntrmyAgt1Acct.setId(Id);
	}

	/*
	 * "id", "schmeNm", "issr"
	 */
	private void setCdtrOthr(Map<Integer, Set<Integer>> tagMap, AccountIdentification4Choice Id) {
		GenericAccountIdentification1 othr = new GenericAccountIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_44, PAIN001Constant.ID_45)) {
			othr.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_44, PAIN001Constant.SCHMENM_407)) {
			setSchmeNm(tagMap, othr);
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_44, PAIN001Constant.ISSR_413)) {
			othr.setIssr("XXXXX");
		}

		Id.setOthr(othr);
	}

	private void setDbtrOthr(Map<Integer, Set<Integer>> tagMap, AccountIdentification4Choice Id) {
		GenericAccountIdentification1 othr = new GenericAccountIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_25, PAIN001Constant.ID_26)) {
			othr.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_25, PAIN001Constant.SCHMENM_200)) {

			setDbtrOthrSchmeNm(tagMap, othr);
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_25, PAIN001Constant.ISSR_203)) {
			othr.setIssr("XXXXX");
		}

		Id.setOthr(othr);
	}

	private void setChrgsCashAccntOthr(Map<Integer, Set<Integer>> tagMap, AccountIdentification4Choice Id) {
		GenericAccountIdentification1 othr = new GenericAccountIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_264, PAIN001Constant.ID_266)) {
			othr.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_264, PAIN001Constant.SCHMENM_268)) {

			setChrgsCashAccntOthrSchmeNm(tagMap, othr);
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_264, PAIN001Constant.ISSR_274)) {
			othr.setIssr("XXXXX");
		}

		Id.setOthr(othr);
	}

	private void setIntrmyAgt1AcctOthr(Map<Integer, Set<Integer>> tagMap, AccountIdentification4Choice Id) {
		GenericAccountIdentification1 othr = new GenericAccountIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_94, PAIN001Constant.ID_95)) {
			othr.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_94, PAIN001Constant.SCHMENM_261)) {

			setIntrmyAgt1AcctSchmeNm(tagMap, othr);
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_94, PAIN001Constant.ISSR_267)) {
			othr.setIssr("XXXXX");
		}

		Id.setOthr(othr);
	}

	private void setIntrmyAgt3AcctOthr(Map<Integer, Set<Integer>> tagMap, AccountIdentification4Choice Id) {
		GenericAccountIdentification1 othr = new GenericAccountIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_321, PAIN001Constant.ID_323)) {
			othr.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_321, PAIN001Constant.SCHMENM_325)) {

			setIntrmyAgt3AcctOthrSchmeNm(tagMap, othr);
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_321, PAIN001Constant.ISSR_331)) {
			othr.setIssr("XXXXX");
		}

		Id.setOthr(othr);
	}

	private void setCdtrAgtAcctOthr(Map<Integer, Set<Integer>> tagMap, AccountIdentification4Choice Id) {
		GenericAccountIdentification1 othr = new GenericAccountIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_367, PAIN001Constant.ID_369)) {
			othr.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_367, PAIN001Constant.SCHMENM_371)) {

			setCdtrAgtAcctOthrSchmeNm(tagMap, othr);
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_367, PAIN001Constant.ISSR_377)) {
			othr.setIssr("XXXXX");
		}

		Id.setOthr(othr);
	}

	private void setOthr(Map<Integer, Set<Integer>> tagMap, AccountIdentification4Choice Id) {
		GenericAccountIdentification1 othr = new GenericAccountIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_223, PAIN001Constant.ID_224)) {
			othr.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_223, PAIN001Constant.SCHMENM_225)) {

			setOthrSchmeNm(tagMap, othr);
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_223, PAIN001Constant.ISSR_228)) {
			othr.setIssr("XXXXX");
		}

		Id.setOthr(othr);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setChrgsCashAccntOthrSchmeNm(Map<Integer, Set<Integer>> tagMap, GenericAccountIdentification1 othr) {
		AccountSchemeName1Choice SchmeNm = new AccountSchemeName1Choice();

		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_268, PAIN001Constant.CD_270)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_268, PAIN001Constant.PRTRY_272)) {
			SchmeNm.setPrtry("XXXX");
		}

		othr.setSchmeNm(SchmeNm);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setIntrmyAgt1AcctSchmeNm(Map<Integer, Set<Integer>> tagMap, GenericAccountIdentification1 othr) {
		AccountSchemeName1Choice SchmeNm = new AccountSchemeName1Choice();

		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_261, PAIN001Constant.CD_263)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_261, PAIN001Constant.PRTRY_265)) {
			SchmeNm.setPrtry("XXXX");
		}

		othr.setSchmeNm(SchmeNm);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setIntrmyAgt3AcctOthrSchmeNm(Map<Integer, Set<Integer>> tagMap, GenericAccountIdentification1 othr) {
		AccountSchemeName1Choice SchmeNm = new AccountSchemeName1Choice();

		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_325, PAIN001Constant.CD_327)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_325, PAIN001Constant.PRTRY_329)) {
			SchmeNm.setPrtry("XXXX");
		}

		othr.setSchmeNm(SchmeNm);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setCdtrAgtAcctOthrSchmeNm(Map<Integer, Set<Integer>> tagMap, GenericAccountIdentification1 othr) {
		AccountSchemeName1Choice SchmeNm = new AccountSchemeName1Choice();

		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_371, PAIN001Constant.CD_373)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_371, PAIN001Constant.PRTRY_375)) {
			SchmeNm.setPrtry("XXXX");
		}

		othr.setSchmeNm(SchmeNm);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setOthrSchmeNm(Map<Integer, Set<Integer>> tagMap, GenericAccountIdentification1 othr) {
		AccountSchemeName1Choice SchmeNm = new AccountSchemeName1Choice();

		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_225, PAIN001Constant.CD_226)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_225, PAIN001Constant.PRTRY_227)) {
			SchmeNm.setPrtry("XXXX");
		}

		othr.setSchmeNm(SchmeNm);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setDbtrOthrSchmeNm(Map<Integer, Set<Integer>> tagMap, GenericAccountIdentification1 othr) {
		AccountSchemeName1Choice SchmeNm = new AccountSchemeName1Choice();

		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_200, PAIN001Constant.CD_201)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_200, PAIN001Constant.PRTRY_202)) {
			SchmeNm.setPrtry("XXXX");
		}

		othr.setSchmeNm(SchmeNm);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setSchmeNm(Map<Integer, Set<Integer>> tagMap, GenericAccountIdentification1 othr) {
		AccountSchemeName1Choice SchmeNm = new AccountSchemeName1Choice();

		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_407, PAIN001Constant.CD_409)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_407, PAIN001Constant.PRTRY_411)) {
			SchmeNm.setPrtry("XXXX");
		}

		othr.setSchmeNm(SchmeNm);
	}

	/*
	 * "chqTp", "chqNb", "chqFr", "dlvryMtd", "dlvrTo", "instrPrty", "chqMtrtyDt",
	 * "frmsCd", "memoFld", "rgnlClrZone", "prtLctn"
	 */
	private void setChqInstr(Map<Integer, Set<Integer>> tagMap, CreditTransferTransactionInformation10 CdtTrfTxInf) {
		Cheque6 ChqInstr = new Cheque6();

		if (isTagPresent(tagMap, PAIN001Constant.CHQINSTR_358, PAIN001Constant.CHQTP_360)) {
			ChqInstr.setChqTp(ChequeType2Code.ELDR.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHQINSTR_358, PAIN001Constant.CHQNB_362)) {
			ChqInstr.setChqNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHQINSTR_358, PAIN001Constant.CHQFR_364)) {
			ChqInstr.setChqFr(setChqFrNameAndAddr10(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHQINSTR_358, PAIN001Constant.DLVRYMTD_390)) {
			ChqInstr.setDlvryMtd(setDlvryMtd(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHQINSTR_358, PAIN001Constant.DLVRTO_396)) {
			ChqInstr.setDlvrTo(setDlvrToNameAndAddr10(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHQINSTR_358, PAIN001Constant.INSTRPRTY_422)) {
			ChqInstr.setInstrPrty(Priority2Code.NORM.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHQINSTR_358, PAIN001Constant.CHQMTRTYDT_666)) {
			ChqInstr.setChqMtrtyDt(LocalDate.now());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHQINSTR_358, PAIN001Constant.FRMSCD_424)) {
			ChqInstr.setFrmsCd("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHQINSTR_358, PAIN001Constant.MEMOFLD_426)) {
			List<String> MemoFld = new ArrayList<>(1);
			MemoFld.add("XXXX");
			ChqInstr.setMemoFld(MemoFld);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHQINSTR_358, PAIN001Constant.RGNLCLRZONE_428)) {
			ChqInstr.setRgnlClrZone("XXXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHQINSTR_358, PAIN001Constant.PRTLCTN_430)) {
			ChqInstr.setPrtLctn("XXXX");
		}

		CdtTrfTxInf.setChqInstr(ChqInstr);
	}

	/*
	 * "nm", "adr"
	 */
	private NameAndAddress10 setDlvrToNameAndAddr10(Map<Integer, Set<Integer>> tagMap) {
		NameAndAddress10 DlvrTo = new NameAndAddress10();
		if (isTagPresent(tagMap, PAIN001Constant.DLVRTO_396, PAIN001Constant.NM_398)) {
			DlvrTo.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DLVRTO_396, PAIN001Constant.ADR_400)) {
			DlvrTo.setAdr(setDlvrPostalAddress6(tagMap));
		}
		return DlvrTo;
	}

	/*
	 * "nm", "adr"
	 */
	private NameAndAddress10 setChqFrNameAndAddr10(Map<Integer, Set<Integer>> tagMap) {
		NameAndAddress10 DlvrTo = new NameAndAddress10();
		if (isTagPresent(tagMap, PAIN001Constant.CHQFR_364, PAIN001Constant.NM_366)) {
			DlvrTo.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHQFR_364, PAIN001Constant.ADR_368)) {
			DlvrTo.setAdr(setChqFrPostalAddress6(tagMap));
		}
		return DlvrTo;
	}

	/*
	 * "nm", "adr"
	 */
	private NameAndAddress10 setNameAndAddr10(Map<Integer, Set<Integer>> tagMap) {
		NameAndAddress10 DlvrTo = new NameAndAddress10();
		if (isTagPresent(tagMap, PAIN001Constant.RMTLCTNPSTLADR_476, PAIN001Constant.NM_477)) {
			DlvrTo.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.RMTLCTNPSTLADR_476, PAIN001Constant.ADR_478)) {
			DlvrTo.setAdr(setDlvrToPostalAddress6(tagMap));
		}
		return DlvrTo;
	}

	/*
	 * "cd", "prtry"
	 */
	private ChequeDeliveryMethod1Choice setDlvryMtd(Map<Integer, Set<Integer>> tagMap) {
		ChequeDeliveryMethod1Choice DlvryMtd = new ChequeDeliveryMethod1Choice();

		if (isTagPresent(tagMap, PAIN001Constant.DLVRYMTD_390, PAIN001Constant.CD_392)) {
			DlvryMtd.setCd(ChequeDelivery1Code.CRCD.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.DLVRYMTD_390, PAIN001Constant.PRTRY_394)) {
			DlvryMtd.setPrtry("XXXX");
		}
		return DlvryMtd;
	}

	/*
	 * "adrTp", "dept", "subDept", "strtNm", "bldgNb", "pstCd", "twnNm",
	 * "ctrySubDvsn", "ctry", "adrLine"
	 */
	private PostalAddress6 setCdtrPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_47, PAIN001Constant.ADRTP_389)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_47, PAIN001Constant.DEPT_391)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_47, PAIN001Constant.SUBDEPT_393)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_47, PAIN001Constant.STRTNM_395)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_47, PAIN001Constant.BLDGNB_397)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_47, PAIN001Constant.PSTCD_399)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_47, PAIN001Constant.TWNNM_401)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_47, PAIN001Constant.CTRYSUBDVSN_403)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_47, PAIN001Constant.CTRY_49)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_47, PAIN001Constant.ADRLINE_111)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setFwdgAgtPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_48, PAIN001Constant.ADRTP_343)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_48, PAIN001Constant.DEPT_345)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_48, PAIN001Constant.SUBDEPT_347)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_48, PAIN001Constant.STRTNM_349)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_48, PAIN001Constant.BLDGNB_351)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_48, PAIN001Constant.PSTCD_353)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_48, PAIN001Constant.TWNNM_355)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_48, PAIN001Constant.CTRYSUBDVSN_357)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_48, PAIN001Constant.CTRY_116)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_48, PAIN001Constant.ADRLINE_110)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setDbtrAgtPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_81, PAIN001Constant.ADRTP_210)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_81, PAIN001Constant.DEPT_211)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_81, PAIN001Constant.SUBDEPT_212)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_81, PAIN001Constant.STRTNM_213)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_81, PAIN001Constant.BLDGNB_214)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_81, PAIN001Constant.PSTCD_215)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_81, PAIN001Constant.TWNNM_216)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_81, PAIN001Constant.CTRYSUBDVSN_217)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_81, PAIN001Constant.CTRY_82)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_81, PAIN001Constant.ADRLINE_218)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	PostalAddress6 Adr = new PostalAddress6();

	private PostalAddress6 setDbtrPartyPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_50, PAIN001Constant.ADRTP_191)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_50, PAIN001Constant.DEPT_192)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_50, PAIN001Constant.SUBDEPT_193)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_50, PAIN001Constant.STRTNM_194)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_50, PAIN001Constant.BLDGNB_195)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_50, PAIN001Constant.PSTCD_196)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_50, PAIN001Constant.TWNNM_197)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_50, PAIN001Constant.CTRYSUBDVSN_198)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_50, PAIN001Constant.CTRY_51)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_50, PAIN001Constant.ADRLINE_79)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setFinInstnIdPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_171, PAIN001Constant.ADRTP_172)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_171, PAIN001Constant.DEPT_173)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_171, PAIN001Constant.SUBDEPT_174)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_171, PAIN001Constant.STRTNM_175)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_171, PAIN001Constant.BLDGNB_176)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_171, PAIN001Constant.PSTCD_177)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_171, PAIN001Constant.TWNNM_178)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_171, PAIN001Constant.CTRYSUBDVSN_179)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_171, PAIN001Constant.CTRY_180)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_171, PAIN001Constant.ADRLINE_181)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setUltmtCdtrPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_421, PAIN001Constant.ADRTP_423)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_421, PAIN001Constant.DEPT_425)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_421, PAIN001Constant.SUBDEPT_427)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_421, PAIN001Constant.STRTNM_429)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_421, PAIN001Constant.BLDGNB_431)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_421, PAIN001Constant.PSTCD_433)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_421, PAIN001Constant.TWNNM_435)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_421, PAIN001Constant.CTRYSUBDVSN_437)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_421, PAIN001Constant.CTRY_439)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_421, PAIN001Constant.ADRLINE_441)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setInvcrPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_500, PAIN001Constant.ADRTP_501)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_500, PAIN001Constant.DEPT_502)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_500, PAIN001Constant.SUBDEPT_503)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_500, PAIN001Constant.STRTNM_504)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_500, PAIN001Constant.BLDGNB_505)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_500, PAIN001Constant.PSTCD_506)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_500, PAIN001Constant.TWNNM_507)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_500, PAIN001Constant.CTRYSUBDVSN_508)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_500, PAIN001Constant.CTRY_509)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_500, PAIN001Constant.ADRLINE_510)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setInitgPtyPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_236, PAIN001Constant.ADRTP_237)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_236, PAIN001Constant.DEPT_238)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_236, PAIN001Constant.SUBDEPT_240)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_236, PAIN001Constant.STRTNM_242)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_236, PAIN001Constant.BLDGNB_244)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_236, PAIN001Constant.PSTCD_246)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_236, PAIN001Constant.TWNNM_248)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_236, PAIN001Constant.CTRYSUBDVSN_250)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_236, PAIN001Constant.CTRY_252)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_236, PAIN001Constant.ADRLINE_254)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setInvceePostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_513, PAIN001Constant.ADRTP_514)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_513, PAIN001Constant.DEPT_515)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_513, PAIN001Constant.SUBDEPT_516)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_513, PAIN001Constant.STRTNM_517)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_513, PAIN001Constant.BLDGNB_518)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_513, PAIN001Constant.PSTCD_519)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_513, PAIN001Constant.TWNNM_520)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_513, PAIN001Constant.CTRYSUBDVSN_521)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_513, PAIN001Constant.CTRY_522)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_513, PAIN001Constant.ADRLINE_523)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setUltmtDbtrPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_436, PAIN001Constant.ADRTP_438)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_436, PAIN001Constant.DEPT_440)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_436, PAIN001Constant.SUBDEPT_442)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_436, PAIN001Constant.STRTNM_444)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_436, PAIN001Constant.BLDGNB_446)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_436, PAIN001Constant.PSTCD_448)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_436, PAIN001Constant.TWNNM_450)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_436, PAIN001Constant.CTRYSUBDVSN_452)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_436, PAIN001Constant.CTRY_454)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_436, PAIN001Constant.ADRLINE_456)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setIntrmyAgt3PostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_287, PAIN001Constant.ADRTP_289)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_287, PAIN001Constant.DEPT_291)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_287, PAIN001Constant.SUBDEPT_293)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_287, PAIN001Constant.STRTNM_295)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_287, PAIN001Constant.BLDGNB_297)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_287, PAIN001Constant.PSTCD_299)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_287, PAIN001Constant.TWNNM_301)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_287, PAIN001Constant.CTRYSUBDVSN_303)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_287, PAIN001Constant.CTRY_305)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_287, PAIN001Constant.ADRLINE_307)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setFinPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_90, PAIN001Constant.ADRTP_239)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_90, PAIN001Constant.DEPT_241)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_90, PAIN001Constant.SUBDEPT_243)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_90, PAIN001Constant.STRTNM_245)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_90, PAIN001Constant.BLDGNB_247)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_90, PAIN001Constant.PSTCD_249)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_90, PAIN001Constant.TWNNM_132)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_90, PAIN001Constant.CTRYSUBDVSN_251)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_90, PAIN001Constant.CTRY_115)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_90, PAIN001Constant.ADRLINE_91)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setFinInstnPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_294, PAIN001Constant.ADRTP_296)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_294, PAIN001Constant.DEPT_298)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_294, PAIN001Constant.SUBDEPT_300)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_294, PAIN001Constant.STRTNM_302)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_294, PAIN001Constant.BLDGNB_304)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_294, PAIN001Constant.PSTCD_306)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_294, PAIN001Constant.TWNNM_308)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_294, PAIN001Constant.CTRYSUBDVSN_310)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_294, PAIN001Constant.CTRY_312)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_294, PAIN001Constant.ADRLINE_314)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setChqFrPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.ADR_368, PAIN001Constant.ADRTP_370)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_368, PAIN001Constant.DEPT_372)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_368, PAIN001Constant.SUBDEPT_374)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_368, PAIN001Constant.STRTNM_376)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_368, PAIN001Constant.BLDGNB_378)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_368, PAIN001Constant.PSTCD_380)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_368, PAIN001Constant.TWNNM_382)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_368, PAIN001Constant.CTRYSUBDVSN_384)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_368, PAIN001Constant.CTRY_386)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_368, PAIN001Constant.ADRLINE_388)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setDlvrPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.ADR_400, PAIN001Constant.ADRTP_402)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_400, PAIN001Constant.DEPT_404)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_400, PAIN001Constant.SUBDEPT_406)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_400, PAIN001Constant.STRTNM_408)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_400, PAIN001Constant.BLDGNB_410)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_400, PAIN001Constant.PSTCD_412)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_400, PAIN001Constant.TWNNM_414)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_400, PAIN001Constant.CTRYSUBDVSN_416)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_400, PAIN001Constant.CTRY_418)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_400, PAIN001Constant.ADRLINE_420)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setDlvrToPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.ADR_478, PAIN001Constant.ADRTP_479)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_478, PAIN001Constant.DEPT_480)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_478, PAIN001Constant.SUBDEPT_481)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_478, PAIN001Constant.STRTNM_482)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_478, PAIN001Constant.BLDGNB_483)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_478, PAIN001Constant.PSTCD_484)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_478, PAIN001Constant.TWNNM_485)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_478, PAIN001Constant.CTRYSUBDVSN_486)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_478, PAIN001Constant.CTRY_487)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ADR_478, PAIN001Constant.ADRLINE_488)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_159, PAIN001Constant.ADRTP_160)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_159, PAIN001Constant.DEPT_161)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_159, PAIN001Constant.SUBDEPT_162)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_159, PAIN001Constant.STRTNM_163)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_159, PAIN001Constant.BLDGNB_164)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_159, PAIN001Constant.PSTCD_165)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_159, PAIN001Constant.TWNNM_166)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_159, PAIN001Constant.CTRYSUBDVSN_167)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_159, PAIN001Constant.CTRY_168)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_159, PAIN001Constant.ADRLINE_169)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	/*
	 * "xchgRate", "rateTp", "ctrctId"
	 */
	private void setXchgRateInf(Map<Integer, Set<Integer>> tagMap, CreditTransferTransactionInformation10 CdtTrfTxInf) {
		ExchangeRateInformation1 XchgRateInf = new ExchangeRateInformation1();

		if (isTagPresent(tagMap, PAIN001Constant.XCHGRATEINF_350, PAIN001Constant.XCHGRATE_352)) {
			XchgRateInf.setXchgRate(new BigDecimal("00"));
		}
		if (isTagPresent(tagMap, PAIN001Constant.XCHGRATEINF_350, PAIN001Constant.RATETP_354)) {
			XchgRateInf.setRateTp(ExchangeRateType1Code.SALE.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.XCHGRATEINF_350, PAIN001Constant.CTRCTID_356)) {
			XchgRateInf.setCtrctId("XXXX");
		}
		CdtTrfTxInf.setXchgRateInf(XchgRateInf);
	}

	/*
	 * "instdAmt", "eqvtAmt"
	 */
	private void setAmt(Map<Integer, Set<Integer>> tagMap, CreditTransferTransactionInformation10 CdtTrfTxInf) {
		AmountType3Choice Amt = new AmountType3Choice();

		if (isTagPresent(tagMap, PAIN001Constant.AMT_34, PAIN001Constant.INSTDAMT_35)) {
			Amt.setInstdAmt(setActiveOrHstCurrAmt());
		}
		if (isTagPresent(tagMap, PAIN001Constant.AMT_34, PAIN001Constant.EQVTAMT_346)) {
			Amt.setEqvtAmt(setEqvtAmt(tagMap));
		}

		CdtTrfTxInf.setAmt(Amt);
	}

	/*
	 * amt", "ccyOfTrf"
	 */
	private EquivalentAmount2 setEqvtAmt(Map<Integer, Set<Integer>> tagMap) {
		EquivalentAmount2 EqvtAmt = new EquivalentAmount2();
		if (isTagPresent(tagMap, PAIN001Constant.EQVTAMT_346, PAIN001Constant.AMT_348)) {
			EqvtAmt.setAmt(setActiveOrHstCurrAmt());
		}
		if (isTagPresent(tagMap, PAIN001Constant.EQVTAMT_346, PAIN001Constant.CCYOFTRF_665)) {
			EqvtAmt.setCcyOfTrf("XXXXX");
		}

		return EqvtAmt;
	}

	private ActiveOrHistoricCurrencyAndAmount setActiveOrHstCurrAmt() {
		ActiveOrHistoricCurrencyAndAmount EAmt = new ActiveOrHistoricCurrencyAndAmount();
		EAmt.setCcy("XXX");
		EAmt.setValue(new BigDecimal("00"));
		return EAmt;
	}

	private void setCdTrfTxInfPmtTpInf(Map<Integer, Set<Integer>> tagMap,
			CreditTransferTransactionInformation10 CdtTrfTxInf) {
		PaymentTypeInformation19 PmtTpInf = new PaymentTypeInformation19();

		if (isTagPresent(tagMap, PAIN001Constant.PMTTPINF_324, PAIN001Constant.INSTRPRTY_326)) {
			PmtTpInf.setInstrPrty(Priority2Code.NORM.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTTPINF_324, PAIN001Constant.SVCLVL_328)) {
			setPmtTpInfSvcLvl(tagMap, PmtTpInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTTPINF_324, PAIN001Constant.LCLINSTRM_334)) {
			setPmtTpInfLclInstrm(tagMap, PmtTpInf);
		}

		if (isTagPresent(tagMap, PAIN001Constant.PMTTPINF_324, PAIN001Constant.CTGYPURP_340)) {
			setPmtTpInfCtgyPurp(tagMap, PmtTpInf);
		}

		CdtTrfTxInf.setPmtTpInf(PmtTpInf);
	}

	/*
	 * "instrId", "endToEndId"
	 */
	private void setPmtId(Map<Integer, Set<Integer>> tagMap, CreditTransferTransactionInformation10 CdtTrfTxInf) {
		PaymentIdentification1 pmtId = new PaymentIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.PMTID_32, PAIN001Constant.INSTRID_322)) {
			pmtId.setInstrId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTID_32, PAIN001Constant.ENDTOENDID_33)) {
			pmtId.setEndToEndId("000000");
		}

		CdtTrfTxInf.setPmtId(pmtId);
	}

	/*
	 * "instrPrty", "svcLvl", "lclInstrm", "ctgyPurp"
	 */
	private void setPmtTpInf(Map<Integer, Set<Integer>> tagMap, PaymentInstructionInformation3 PmtInf) {
		PaymentTypeInformation19 pmtTpInf = new PaymentTypeInformation19();

		if (isTagPresent(tagMap, PAIN001Constant.PMTTPINF_12, PAIN001Constant.INSTRPRTY_188)) {
			pmtTpInf.setInstrPrty(Priority2Code.NORM.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTTPINF_12, PAIN001Constant.SVCLVL_13)) {
			setSvcLvl(tagMap, pmtTpInf);
		}
		if (isTagPresent(tagMap, PAIN001Constant.PMTTPINF_12, PAIN001Constant.LCLINSTRM_15)) {
			setLclInstrm(tagMap, pmtTpInf);
		}

		if (isTagPresent(tagMap, PAIN001Constant.PMTTPINF_12, PAIN001Constant.CTGYPURP_17)) {
			setCtgyPurp(tagMap, pmtTpInf);
		}

		PmtInf.setPmtTpInf(pmtTpInf);
	}

	private void setPmtTpInfCtgyPurp(Map<Integer, Set<Integer>> tagMap, PaymentTypeInformation19 pmtTpInf) {
		CategoryPurpose1Choice CtgyPurp = new CategoryPurpose1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.CTGYPURP_340, PAIN001Constant.CD_342)) {
			CtgyPurp.setCd("XX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTGYPURP_340, PAIN001Constant.PRTRY_344)) {
			CtgyPurp.setPrtry("XXXX");
		}

		pmtTpInf.setCtgyPurp(CtgyPurp);
	}

	private void setCtgyPurp(Map<Integer, Set<Integer>> tagMap, PaymentTypeInformation19 pmtTpInf) {
		CategoryPurpose1Choice CtgyPurp = new CategoryPurpose1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.CTGYPURP_17, PAIN001Constant.CD_18)) {
			CtgyPurp.setCd("XX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTGYPURP_17, PAIN001Constant.PRTRY_19)) {
			CtgyPurp.setPrtry("XXXX");
		}

		pmtTpInf.setCtgyPurp(CtgyPurp);
	}

	private void setPmtTpInfLclInstrm(Map<Integer, Set<Integer>> tagMap, PaymentTypeInformation19 pmtTpInf) {
		LocalInstrument2Choice LclInstrm = new LocalInstrument2Choice();
		if (isTagPresent(tagMap, PAIN001Constant.LCLINSTRM_334, PAIN001Constant.CD_336)) {
			LclInstrm.setCd("XX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.LCLINSTRM_334, PAIN001Constant.PRTRY_338)) {
			LclInstrm.setPrtry("XXXX");
		}

		pmtTpInf.setLclInstrm(LclInstrm);
	}

	private void setLclInstrm(Map<Integer, Set<Integer>> tagMap, PaymentTypeInformation19 pmtTpInf) {
		LocalInstrument2Choice LclInstrm = new LocalInstrument2Choice();
		if (isTagPresent(tagMap, PAIN001Constant.LCLINSTRM_15, PAIN001Constant.CD_16)) {
			LclInstrm.setCd("XX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.LCLINSTRM_15, PAIN001Constant.PRTRY_190)) {
			LclInstrm.setPrtry("XXXX");
		}

		pmtTpInf.setLclInstrm(LclInstrm);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setPmtTpInfSvcLvl(Map<Integer, Set<Integer>> tagMap, PaymentTypeInformation19 pmtTpInf) {
		ServiceLevel8Choice SvcLvl = new ServiceLevel8Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SVCLVL_328, PAIN001Constant.CD_330)) {
			SvcLvl.setCd("XX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SVCLVL_328, PAIN001Constant.PRTRY_332)) {
			SvcLvl.setPrtry("XXXX");
		}
		pmtTpInf.setSvcLvl(SvcLvl);
	}

	/*
	 * "cd", "prtry"
	 */
	private void setSvcLvl(Map<Integer, Set<Integer>> tagMap, PaymentTypeInformation19 pmtTpInf) {
		ServiceLevel8Choice SvcLvl = new ServiceLevel8Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SVCLVL_13, PAIN001Constant.CD_14)) {
			SvcLvl.setCd("XX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SVCLVL_13, PAIN001Constant.PRTRY_189)) {
			SvcLvl.setPrtry("XXXX");
		}
		pmtTpInf.setSvcLvl(SvcLvl);
	}

	/**
	 * "msgId", "creDtTm", "authstn", "nbOfTxs", "ctrlSum", "initgPty", "fwdgAgt"
	 * 
	 * @param tagMap
	 */
	private void setGrpHdr(CustomerCreditTransferInitiationV03 cstmrCdtTrfInitn, Map<Integer, Set<Integer>> tagMap) {
		// if (isTagPresent(tagMap, PAIN001Constant.CSTMRCDTTRFINITN_2,
		// PAIN001Constant.GRPHDR_3)) {

		GroupHeader32 GrpHdr = new GroupHeader32();
		if (isTagPresent(tagMap, PAIN001Constant.GRPHDR_3, PAIN001Constant.MSGID_4)) {
			GrpHdr.setMsgId("00000");
		}
		if (isTagPresent(tagMap, PAIN001Constant.GRPHDR_3, PAIN001Constant.CREDTTM_5)) {
			GrpHdr.setCreDtTm(LocalDateTime.now().toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.GRPHDR_3, PAIN001Constant.NBOFTXS_6)) {
			GrpHdr.setNbOfTxs("0");
		}
		if (isTagPresent(tagMap, PAIN001Constant.GRPHDR_3, PAIN001Constant.CTRLSUM_133)) {
			GrpHdr.setCtrlSum(new BigDecimal("0.00"));
		}

		if (isTagPresent(tagMap, PAIN001Constant.GRPHDR_3, PAIN001Constant.INITGPTY_7)) {
			GrpHdr.setInitgPty(setInitgPtyPartyIdentificatio32(tagMap));
		}

		if (isTagPresent(tagMap, PAIN001Constant.GRPHDR_3, PAIN001Constant.FWDGAGT_134)) {
			GrpHdr.setFwdgAgt(setFwdAgtBrFiId4(tagMap));
		}
		cstmrCdtTrfInitn.setGrpHdr(GrpHdr);
		// }
	}

	/*
	 * "finInstnId", "brnchId"
	 */
	private BranchAndFinancialInstitutionIdentification4 setDbtrAgtBrFiId4(Map<Integer, Set<Integer>> tagMap) {
		BranchAndFinancialInstitutionIdentification4 fwdgAgt = new BranchAndFinancialInstitutionIdentification4();
		if (isTagPresent(tagMap, PAIN001Constant.DBTRAGT_27, PAIN001Constant.FININSTNID_28)) {
			setDbtrAgtFinInstnId(tagMap, fwdgAgt);
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTRAGT_27, PAIN001Constant.BRNCHID_137)) {
			setDbtrAgtBrnchId(tagMap, fwdgAgt);
		}
		return fwdgAgt;
	}

	private BranchAndFinancialInstitutionIdentification4 setFwdAgtBrFiId4(Map<Integer, Set<Integer>> tagMap) {
		BranchAndFinancialInstitutionIdentification4 fwdgAgt = new BranchAndFinancialInstitutionIdentification4();
		if (isTagPresent(tagMap, PAIN001Constant.FWDGAGT_134, PAIN001Constant.FININSTNID_135)) {
			setFwdAgtFinInstnId(tagMap, fwdgAgt);
		}
		if (isTagPresent(tagMap, PAIN001Constant.FWDGAGT_134, PAIN001Constant.BRNCHID_182)) {
			setBrnchId(tagMap, fwdgAgt);
		}
		return fwdgAgt;
	}

	private BranchAndFinancialInstitutionIdentification4 setCdtrAgtBrFiId4(Map<Integer, Set<Integer>> tagMap) {
		BranchAndFinancialInstitutionIdentification4 fwdgAgt = new BranchAndFinancialInstitutionIdentification4();
		if (isTagPresent(tagMap, PAIN001Constant.CDTRAGT_36, PAIN001Constant.FININSTNID_37)) {
			setCdtrAgtFinInstnId(tagMap, fwdgAgt);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTRAGT_36, PAIN001Constant.BRNCHID_139)) {
			setCdtrAgtBrnchId(tagMap, fwdgAgt);
		}
		return fwdgAgt;
	}

	private BranchAndFinancialInstitutionIdentification4 setIntrmyAgt1BrFiId4(Map<Integer, Set<Integer>> tagMap) {
		BranchAndFinancialInstitutionIdentification4 fwdgAgt = new BranchAndFinancialInstitutionIdentification4();
		if (isTagPresent(tagMap, PAIN001Constant.INTRMYAGT1_84, PAIN001Constant.FININSTNID_85)) {
			setIntrmyAgt1FinInstnId(tagMap, fwdgAgt);
		}
		if (isTagPresent(tagMap, PAIN001Constant.INTRMYAGT1_84, PAIN001Constant.BRNCHID_253)) {
			setIntrmyAgt1FwdgAgtBrnchId(tagMap, fwdgAgt);
		}
		return fwdgAgt;
	}

	private BranchAndFinancialInstitutionIdentification4 setIntrmyAgt3BrFiId4(Map<Integer, Set<Integer>> tagMap) {
		BranchAndFinancialInstitutionIdentification4 fwdgAgt = new BranchAndFinancialInstitutionIdentification4();
		if (isTagPresent(tagMap, PAIN001Constant.INTRMYAGT3_279, PAIN001Constant.FININSTNID_281)) {
			setIntrmyAgt3FinInstnId(tagMap, fwdgAgt);
		}
		if (isTagPresent(tagMap, PAIN001Constant.INTRMYAGT3_279, PAIN001Constant.BRNCHID_309)) {
			setIntrmyAgt3BrnchId(tagMap, fwdgAgt);
		}
		return fwdgAgt;
	}

	private BranchAndFinancialInstitutionIdentification4 setBrFiId4(Map<Integer, Set<Integer>> tagMap) {
		BranchAndFinancialInstitutionIdentification4 fwdgAgt = new BranchAndFinancialInstitutionIdentification4();
		if (isTagPresent(tagMap, PAIN001Constant.CHRGSACCTAGT_286, PAIN001Constant.FININSTNID_288)) {
			setFinInstnId(tagMap, fwdgAgt);
		}
		if (isTagPresent(tagMap, PAIN001Constant.CHRGSACCTAGT_286, PAIN001Constant.BRNCHID_316)) {
			setFwdgAgtBrnchId(tagMap, fwdgAgt);
		}
		return fwdgAgt;
	}

	/*
	 * "id", "nm", "pstlAdr"
	 */
	private void setCdtrAgtBrnchId(Map<Integer, Set<Integer>> tagMap,
			BranchAndFinancialInstitutionIdentification4 fwdgAgt) {
		BranchData2 BrnchId = new BranchData2();

		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_139, PAIN001Constant.ID_140)) {
			BrnchId.setId("00");
		}
		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_139, PAIN001Constant.NM_359)) {
			BrnchId.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_139, PAIN001Constant.PSTLADR_690)) {
			BrnchId.setPstlAdr(setCdtrAgtBrnchPostalAddress6(tagMap));

		}
		fwdgAgt.setBrnchId(BrnchId);
	}

	private void setDbtrAgtBrnchId(Map<Integer, Set<Integer>> tagMap,
			BranchAndFinancialInstitutionIdentification4 fwdgAgt) {
		BranchData2 BrnchId = new BranchData2();

		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_137, PAIN001Constant.ID_138)) {
			BrnchId.setId("00");
		}
		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_137, PAIN001Constant.NM_219)) {
			BrnchId.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_137, PAIN001Constant.PSTLADR_601)) {
			BrnchId.setPstlAdr(setDbtrAgtBrnchPostalAddress6(tagMap));
		}
		fwdgAgt.setBrnchId(BrnchId);
	}

	private void setIntrmyAgt1FwdgAgtBrnchId(Map<Integer, Set<Integer>> tagMap,
			BranchAndFinancialInstitutionIdentification4 fwdgAgt) {
		BranchData2 BrnchId = new BranchData2();

		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_253, PAIN001Constant.ID_255)) {
			BrnchId.setId("00");
		}
		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_253, PAIN001Constant.NM_257)) {
			BrnchId.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_253, PAIN001Constant.PSTLADR_879)) {
			BrnchId.setPstlAdr(setInBrnchPostalAddress6(tagMap));
		}
		fwdgAgt.setBrnchId(BrnchId);
	}

	private void setIntrmyAgt3BrnchId(Map<Integer, Set<Integer>> tagMap,
			BranchAndFinancialInstitutionIdentification4 fwdgAgt) {
		BranchData2 BrnchId = new BranchData2();

		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_309, PAIN001Constant.ID_311)) {
			BrnchId.setId("00");
		}
		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_309, PAIN001Constant.NM_313)) {
			BrnchId.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_309, PAIN001Constant.PSTLADR_670)) {
			BrnchId.setPstlAdr(setIntrmyAgt3BrnchPostalAddress6(tagMap));
		}
		fwdgAgt.setBrnchId(BrnchId);
	}

	private void setFwdgAgtBrnchId(Map<Integer, Set<Integer>> tagMap,
			BranchAndFinancialInstitutionIdentification4 fwdgAgt) {
		BranchData2 BrnchId = new BranchData2();

		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_316, PAIN001Constant.ID_318)) {
			BrnchId.setId("00");
		}
		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_316, PAIN001Constant.NM_320)) {
			BrnchId.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_316, PAIN001Constant.PSTLADR_642)) {
			BrnchId.setPstlAdr(setBrnchPostalAddress6(tagMap));
		}
		fwdgAgt.setBrnchId(BrnchId);
	}

	private void setBrnchId(Map<Integer, Set<Integer>> tagMap, BranchAndFinancialInstitutionIdentification4 fwdgAgt) {
		BranchData2 BrnchId = new BranchData2();

		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_182, PAIN001Constant.ID_183)) {
			BrnchId.setId("00");
		}
		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_182, PAIN001Constant.NM_184)) {
			BrnchId.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.BRNCHID_182, PAIN001Constant.PSTLADR_554)) {
			BrnchId.setPstlAdr(setBrFnPostalAddress6(tagMap));
		}
		fwdgAgt.setBrnchId(BrnchId);
	}

	private PostalAddress6 setCdtrAgtBrnchPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_690, PAIN001Constant.ADRTP_691)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_690, PAIN001Constant.DEPT_692)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_690, PAIN001Constant.SUBDEPT_693)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_690, PAIN001Constant.STRTNM_694)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_690, PAIN001Constant.BLDGNB_695)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_690, PAIN001Constant.PSTCD_696)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_690, PAIN001Constant.TWNNM_697)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_690, PAIN001Constant.CTRYSUBDVSN_698)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_690, PAIN001Constant.CTRY_699)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_690, PAIN001Constant.ADRLINE_700)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setDbtrAgtBrnchPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_601, PAIN001Constant.ADRTP_602)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_601, PAIN001Constant.DEPT_603)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_601, PAIN001Constant.SUBDEPT_604)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_601, PAIN001Constant.STRTNM_605)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_601, PAIN001Constant.BLDGNB_606)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_601, PAIN001Constant.PSTCD_607)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_601, PAIN001Constant.TWNNM_608)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_601, PAIN001Constant.CTRYSUBDVSN_609)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_601, PAIN001Constant.CTRY_610)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_601, PAIN001Constant.ADRLINE_611)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setIntrmyAgt3BrnchPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_670, PAIN001Constant.ADRTP_671)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_670, PAIN001Constant.DEPT_672)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_670, PAIN001Constant.SUBDEPT_673)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_670, PAIN001Constant.STRTNM_674)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_670, PAIN001Constant.BLDGNB_675)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_670, PAIN001Constant.PSTCD_676)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_670, PAIN001Constant.TWNNM_677)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_670, PAIN001Constant.CTRYSUBDVSN_678)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_670, PAIN001Constant.CTRY_679)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_670, PAIN001Constant.ADRLINE_680)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setInBrnchPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_879, PAIN001Constant.ADRTP_880)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_879, PAIN001Constant.DEPT_881)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_879, PAIN001Constant.SUBDEPT_882)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_879, PAIN001Constant.STRTNM_883)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_879, PAIN001Constant.BLDGNB_884)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_879, PAIN001Constant.PSTCD_885)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_879, PAIN001Constant.TWNNM_886)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_879, PAIN001Constant.CTRYSUBDVSN_887)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_879, PAIN001Constant.CTRY_888)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_879, PAIN001Constant.ADRLINE_889)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setBrnchPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_642, PAIN001Constant.ADRTP_643)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_642, PAIN001Constant.DEPT_644)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_642, PAIN001Constant.SUBDEPT_645)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_642, PAIN001Constant.STRTNM_646)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_642, PAIN001Constant.BLDGNB_647)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_642, PAIN001Constant.PSTCD_648)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_642, PAIN001Constant.TWNNM_649)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_642, PAIN001Constant.CTRYSUBDVSN_650)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_642, PAIN001Constant.CTRY_651)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_642, PAIN001Constant.ADRLINE_652)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	private PostalAddress6 setBrFnPostalAddress6(Map<Integer, Set<Integer>> tagMap) {
		PostalAddress6 Adr = new PostalAddress6();
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_554, PAIN001Constant.ADRTP_555)) {
			Adr.setAdrTp(AddressType2Code.HOME.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_554, PAIN001Constant.DEPT_556)) {
			Adr.setDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_554, PAIN001Constant.SUBDEPT_557)) {
			Adr.setSubDept("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_554, PAIN001Constant.STRTNM_558)) {
			Adr.setStrtNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_554, PAIN001Constant.BLDGNB_559)) {
			Adr.setBldgNb("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_554, PAIN001Constant.PSTCD_560)) {
			Adr.setPstCd("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_554, PAIN001Constant.TWNNM_561)) {
			Adr.setTwnNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_554, PAIN001Constant.CTRYSUBDVSN_562)) {
			Adr.setCtrySubDvsn("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_554, PAIN001Constant.CTRY_563)) {
			Adr.setCtry("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.PSTLADR_554, PAIN001Constant.ADRLINE_564)) {
			List<String> AdrLine = new ArrayList<>(1);
			AdrLine.add("XXXXX");
			Adr.setAdrLine(AdrLine);
		}
		return Adr;
	}

	/*
	 * "bic", "clrSysMmbId", "nm", "pstlAdr", "othr"
	 */
	private void setCdtrAgtFinInstnId(Map<Integer, Set<Integer>> tagMap,
			BranchAndFinancialInstitutionIdentification4 fwdgAgt) {
		FinancialInstitutionIdentification7 finInstnId = new FinancialInstitutionIdentification7();

		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_37, PAIN001Constant.BIC_108)) {
			finInstnId.setBIC("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_37, PAIN001Constant.NM_109)) {
			finInstnId.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_37, PAIN001Constant.PSTLADR_48)) {
			finInstnId.setPstlAdr(setFwdgAgtPostalAddress6(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_37, PAIN001Constant.CLRSYSMMBID_38)) {
			finInstnId.setClrSysMmbId(setCdtrAgtFinInsClrSysMmbId(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_37, PAIN001Constant.OTHR_684)) {
			finInstnId.setOthr(setCdtrAgtFinInsOther(tagMap));
		}
		fwdgAgt.setFinInstnId(finInstnId);
	}

	/*
	 * "bic", "clrSysMmbId", "nm", "pstlAdr", "othr"
	 */
	private void setDbtrAgtFinInstnId(Map<Integer, Set<Integer>> tagMap,
			BranchAndFinancialInstitutionIdentification4 fwdgAgt) {
		FinancialInstitutionIdentification7 finInstnId = new FinancialInstitutionIdentification7();

		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_28, PAIN001Constant.BIC_80)) {
			finInstnId.setBIC("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_28, PAIN001Constant.CLRSYSMMBID_29)) {
			finInstnId.setClrSysMmbId(setDbtrAgtClrSysMmb(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_28, PAIN001Constant.NM_209)) {
			finInstnId.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_28, PAIN001Constant.PSTLADR_81)) {
			finInstnId.setPstlAdr(setDbtrAgtPostalAddress6(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_28, PAIN001Constant.OTHR_592)) {
			finInstnId.setOthr(setDbtrAgtOther(tagMap));
		}
		fwdgAgt.setFinInstnId(finInstnId);
	}

	private GenericFinancialIdentification1 setCdtrAgtFinInsOther(Map<Integer, Set<Integer>> tagMap) {
		GenericFinancialIdentification1 DbtrAgtOther = new GenericFinancialIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_684, PAIN001Constant.ID_685)) {
			DbtrAgtOther.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_684, PAIN001Constant.ISSR_687)) {
			DbtrAgtOther.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_684, PAIN001Constant.SCHMENM_686)) {
			DbtrAgtOther.setSchmeNm(setCdtrAgtFinInsSchmeNm(tagMap));
		}
		return DbtrAgtOther;
	}

	private GenericFinancialIdentification1 setFwDbtrAgtOther(Map<Integer, Set<Integer>> tagMap) {
		GenericFinancialIdentification1 DbtrAgtOther = new GenericFinancialIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_902, PAIN001Constant.ID_907)) {
			DbtrAgtOther.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_902, PAIN001Constant.ISSR_909)) {
			DbtrAgtOther.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_902, PAIN001Constant.SCHMENM_908)) {
			DbtrAgtOther.setSchmeNm(setFwDbtrAgtSchmeNm(tagMap));
		}
		return DbtrAgtOther;
	}

	private GenericFinancialIdentification1 setFinDbtrAgtOther(Map<Integer, Set<Integer>> tagMap) {
		GenericFinancialIdentification1 DbtrAgtOther = new GenericFinancialIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_592, PAIN001Constant.ID_874)) {
			DbtrAgtOther.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_592, PAIN001Constant.ISSR_876)) {
			DbtrAgtOther.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_592, PAIN001Constant.SCHMENM_875)) {
			DbtrAgtOther.setSchmeNm(setFinDbtrAgtSchmeNm(tagMap));
		}
		return DbtrAgtOther;
	}

	private GenericFinancialIdentification1 setDbtrAgtOther(Map<Integer, Set<Integer>> tagMap) {
		GenericFinancialIdentification1 DbtrAgtOther = new GenericFinancialIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_592, PAIN001Constant.ID_596)) {
			DbtrAgtOther.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_592, PAIN001Constant.ISSR_598)) {
			DbtrAgtOther.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_592, PAIN001Constant.SCHMENM_597)) {
			DbtrAgtOther.setSchmeNm(setDbtrAgtSchmeNm(tagMap));
		}
		return DbtrAgtOther;
	}

	private GenericFinancialIdentification1 setIntOther(Map<Integer, Set<Integer>> tagMap) {
		GenericFinancialIdentification1 DbtrAgtOther = new GenericFinancialIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_891, PAIN001Constant.ID_896)) {
			DbtrAgtOther.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_891, PAIN001Constant.ISSR_898)) {
			DbtrAgtOther.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_891, PAIN001Constant.SCHMENM_897)) {
			DbtrAgtOther.setSchmeNm(setIntSchmeNm(tagMap));
		}
		return DbtrAgtOther;
	}

	private GenericFinancialIdentification1 setFinOther(Map<Integer, Set<Integer>> tagMap) {
		GenericFinancialIdentification1 DbtrAgtOther = new GenericFinancialIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_859, PAIN001Constant.ID_864)) {
			DbtrAgtOther.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_859, PAIN001Constant.ISSR_866)) {
			DbtrAgtOther.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_859, PAIN001Constant.SCHMENM_865)) {
			DbtrAgtOther.setSchmeNm(setFinSchmeNm(tagMap));
		}
		return DbtrAgtOther;
	}

	private FinancialIdentificationSchemeName1Choice setCdtrAgtFinInsSchmeNm(Map<Integer, Set<Integer>> tagMap) {
		FinancialIdentificationSchemeName1Choice DbtrAgtSchmeNm = new FinancialIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_686, PAIN001Constant.CD_688)) {
			DbtrAgtSchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_686, PAIN001Constant.PRTRY_689)) {
			DbtrAgtSchmeNm.setPrtry("XXXX");
		}
		return DbtrAgtSchmeNm;
	}

	private FinancialIdentificationSchemeName1Choice setIntSchmeNm(Map<Integer, Set<Integer>> tagMap) {
		FinancialIdentificationSchemeName1Choice DbtrAgtSchmeNm = new FinancialIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_897, PAIN001Constant.CD_899)) {
			DbtrAgtSchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_897, PAIN001Constant.PRTRY_900)) {
			DbtrAgtSchmeNm.setPrtry("XXXX");
		}
		return DbtrAgtSchmeNm;
	}

	private FinancialIdentificationSchemeName1Choice setFinSchmeNm(Map<Integer, Set<Integer>> tagMap) {
		FinancialIdentificationSchemeName1Choice DbtrAgtSchmeNm = new FinancialIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_865, PAIN001Constant.CD_867)) {
			DbtrAgtSchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_865, PAIN001Constant.PRTRY_868)) {
			DbtrAgtSchmeNm.setPrtry("XXXX");
		}
		return DbtrAgtSchmeNm;
	}

	private FinancialIdentificationSchemeName1Choice setFwDbtrAgtSchmeNm(Map<Integer, Set<Integer>> tagMap) {
		FinancialIdentificationSchemeName1Choice DbtrAgtSchmeNm = new FinancialIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_908, PAIN001Constant.CD_910)) {
			DbtrAgtSchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_908, PAIN001Constant.PRTRY_911)) {
			DbtrAgtSchmeNm.setPrtry("XXXX");
		}
		return DbtrAgtSchmeNm;
	}

	private FinancialIdentificationSchemeName1Choice setFinDbtrAgtSchmeNm(Map<Integer, Set<Integer>> tagMap) {
		FinancialIdentificationSchemeName1Choice DbtrAgtSchmeNm = new FinancialIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_875, PAIN001Constant.CD_877)) {
			DbtrAgtSchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_875, PAIN001Constant.PRTRY_878)) {
			DbtrAgtSchmeNm.setPrtry("XXXX");
		}
		return DbtrAgtSchmeNm;
	}

	private FinancialIdentificationSchemeName1Choice setDbtrAgtSchmeNm(Map<Integer, Set<Integer>> tagMap) {
		FinancialIdentificationSchemeName1Choice DbtrAgtSchmeNm = new FinancialIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_597, PAIN001Constant.CD_599)) {
			DbtrAgtSchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_597, PAIN001Constant.PRTRY_600)) {
			DbtrAgtSchmeNm.setPrtry("XXXX");
		}
		return DbtrAgtSchmeNm;
	}

	private ClearingSystemMemberIdentification2 setCdtrAgtFinInsClrSysMmbId(Map<Integer, Set<Integer>> tagMap) {
		ClearingSystemMemberIdentification2 ClrSysMmbId = new ClearingSystemMemberIdentification2();

		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSMMBID_38, PAIN001Constant.MMBID_39)) {
			ClrSysMmbId.setMmbId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSMMBID_38, PAIN001Constant.CLRSYSID_681)) {
			ClrSysMmbId.setClrSysId(setCdtrAgtFinInsClrSys(tagMap));
		}
		return ClrSysMmbId;
	}

	private ClearingSystemMemberIdentification2 setFinClrSysMmbId(Map<Integer, Set<Integer>> tagMap) {
		ClearingSystemMemberIdentification2 ClrSysMmbId = new ClearingSystemMemberIdentification2();

		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSMMBID_858, PAIN001Constant.MMBID_861)) {
			ClrSysMmbId.setMmbId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSMMBID_858, PAIN001Constant.CLRSYSID_860)) {
			ClrSysMmbId.setClrSysId(setFinClrSys(tagMap));
		}
		return ClrSysMmbId;
	}

	private ClearingSystemMemberIdentification2 setIntClrSysMmbId(Map<Integer, Set<Integer>> tagMap) {
		ClearingSystemMemberIdentification2 ClrSysMmbId = new ClearingSystemMemberIdentification2();

		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSMMBID_890, PAIN001Constant.MMBID_893)) {
			ClrSysMmbId.setMmbId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSMMBID_890, PAIN001Constant.CLRSYSID_892)) {
			ClrSysMmbId.setClrSysId(setIntClrSys(tagMap));
		}
		return ClrSysMmbId;
	}

	private ClearingSystemMemberIdentification2 setAgtClrSysMmb(Map<Integer, Set<Integer>> tagMap) {
		ClearingSystemMemberIdentification2 ClrSysMmbId = new ClearingSystemMemberIdentification2();

		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSMMBID_901, PAIN001Constant.MMBID_904)) {
			ClrSysMmbId.setMmbId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSMMBID_901, PAIN001Constant.CLRSYSID_903)) {
			ClrSysMmbId.setClrSysId(setFwDbtrAgtClrSys(tagMap));
		}
		return ClrSysMmbId;
	}

	private ClearingSystemMemberIdentification2 setFnAgtClrSysMmb(Map<Integer, Set<Integer>> tagMap) {
		ClearingSystemMemberIdentification2 ClrSysMmbId = new ClearingSystemMemberIdentification2();

		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSMMBID_87, PAIN001Constant.MMBID_88)) {
			ClrSysMmbId.setMmbId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSMMBID_87, PAIN001Constant.CLRSYSID_870)) {
			ClrSysMmbId.setClrSysId(setFnDbtrAgtClrSys(tagMap));
		}
		return ClrSysMmbId;
	}

	private ClearingSystemMemberIdentification2 setDbtrAgtClrSysMmb(Map<Integer, Set<Integer>> tagMap) {
		ClearingSystemMemberIdentification2 ClrSysMmbId = new ClearingSystemMemberIdentification2();

		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSMMBID_29, PAIN001Constant.MMBID_30)) {
			ClrSysMmbId.setMmbId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSMMBID_29, PAIN001Constant.CLRSYSID_593)) {
			ClrSysMmbId.setClrSysId(setDbtrAgtClrSys(tagMap));
		}
		return ClrSysMmbId;
	}

	private ClearingSystemIdentification2Choice setIntClrSys(Map<Integer, Set<Integer>> tagMap) {
		ClearingSystemIdentification2Choice ClrSysId = new ClearingSystemIdentification2Choice();
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSID_892, PAIN001Constant.CD_894)) {
			ClrSysId.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSID_892, PAIN001Constant.PRTRY_895)) {
			ClrSysId.setPrtry("XXXX");
		}
		return ClrSysId;
	}

	private ClearingSystemIdentification2Choice setFinClrSys(Map<Integer, Set<Integer>> tagMap) {
		ClearingSystemIdentification2Choice ClrSysId = new ClearingSystemIdentification2Choice();
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSID_860, PAIN001Constant.CD_862)) {
			ClrSysId.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSID_860, PAIN001Constant.PRTRY_863)) {
			ClrSysId.setPrtry("XXXX");
		}
		return ClrSysId;
	}

	private ClearingSystemIdentification2Choice setCdtrAgtFinInsClrSys(Map<Integer, Set<Integer>> tagMap) {
		ClearingSystemIdentification2Choice ClrSysId = new ClearingSystemIdentification2Choice();
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSID_681, PAIN001Constant.CD_682)) {
			ClrSysId.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSID_681, PAIN001Constant.PRTRY_683)) {
			ClrSysId.setPrtry("XXXX");
		}
		return ClrSysId;
	}

	private ClearingSystemIdentification2Choice setFwDbtrAgtClrSys(Map<Integer, Set<Integer>> tagMap) {
		ClearingSystemIdentification2Choice ClrSysId = new ClearingSystemIdentification2Choice();
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSID_903, PAIN001Constant.CD_905)) {
			ClrSysId.setCd("XXXxX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSID_903, PAIN001Constant.PRTRY_906)) {
			ClrSysId.setPrtry("XXXXX");
		}
		return ClrSysId;
	}

	private ClearingSystemIdentification2Choice setFnDbtrAgtClrSys(Map<Integer, Set<Integer>> tagMap) {
		ClearingSystemIdentification2Choice ClrSysId = new ClearingSystemIdentification2Choice();
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSID_870, PAIN001Constant.CD_871)) {
			ClrSysId.setCd("XXXxX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSID_870, PAIN001Constant.PRTRY_872)) {
			ClrSysId.setPrtry("XXXXX");
		}
		return ClrSysId;
	}

	private ClearingSystemIdentification2Choice setDbtrAgtClrSys(Map<Integer, Set<Integer>> tagMap) {
		ClearingSystemIdentification2Choice ClrSysId = new ClearingSystemIdentification2Choice();
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSID_593, PAIN001Constant.CD_594)) {
			ClrSysId.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CLRSYSID_593, PAIN001Constant.PRTRY_595)) {
			ClrSysId.setPrtry("XXXX");
		}
		return ClrSysId;
	}

	private void setFwdAgtFinInstnId(Map<Integer, Set<Integer>> tagMap,
			BranchAndFinancialInstitutionIdentification4 fwdgAgt) {
		FinancialInstitutionIdentification7 finInstnId = new FinancialInstitutionIdentification7();

		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_135, PAIN001Constant.BIC_170)) {
			finInstnId.setBIC("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_135, PAIN001Constant.CLRSYSMMBID_901)) {
			finInstnId.setClrSysMmbId(setAgtClrSysMmb(tagMap));
		}

		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_135, PAIN001Constant.NM_136)) {
			finInstnId.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_135, PAIN001Constant.PSTLADR_171)) {
			finInstnId.setPstlAdr(setFinInstnIdPostalAddress6(tagMap));
		}

		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_135, PAIN001Constant.OTHR_902)) {
			finInstnId.setOthr(setFwDbtrAgtOther(tagMap));
		}
		fwdgAgt.setFinInstnId(finInstnId);
	}

	private void setIntrmyAgt1FinInstnId(Map<Integer, Set<Integer>> tagMap,
			BranchAndFinancialInstitutionIdentification4 fwdgAgt) {
		FinancialInstitutionIdentification7 finInstnId = new FinancialInstitutionIdentification7();

		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_85, PAIN001Constant.BIC_86)) {
			finInstnId.setBIC("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_85, PAIN001Constant.CLRSYSMMBID_87)) {
			finInstnId.setClrSysMmbId(setFnAgtClrSysMmb(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_85, PAIN001Constant.NM_89)) {
			finInstnId.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_85, PAIN001Constant.PSTLADR_90)) {
			finInstnId.setPstlAdr(setFinPostalAddress6(tagMap));
		}

		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_85, PAIN001Constant.OTHR_873)) {
			finInstnId.setOthr(setFinDbtrAgtOther(tagMap));
		}

		fwdgAgt.setFinInstnId(finInstnId);
	}

	private void setIntrmyAgt3FinInstnId(Map<Integer, Set<Integer>> tagMap,
			BranchAndFinancialInstitutionIdentification4 fwdgAgt) {
		FinancialInstitutionIdentification7 finInstnId = new FinancialInstitutionIdentification7();

		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_281, PAIN001Constant.BIC_283)) {
			finInstnId.setBIC("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_281, PAIN001Constant.CLRSYSMMBID_890)) {
			finInstnId.setClrSysMmbId(setIntClrSysMmbId(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_281, PAIN001Constant.NM_285)) {
			finInstnId.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_281, PAIN001Constant.PSTLADR_287)) {
			finInstnId.setPstlAdr(setIntrmyAgt3PostalAddress6(tagMap));
		}

		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_281, PAIN001Constant.OTHR_891)) {
			finInstnId.setOthr(setIntOther(tagMap));
		}

		fwdgAgt.setFinInstnId(finInstnId);
	}

	/**
	 * "bic", "clrSysMmbId", "nm", "pstlAdr", "othr"
	 */
	private void setFinInstnId(Map<Integer, Set<Integer>> tagMap,
			BranchAndFinancialInstitutionIdentification4 fwdgAgt) {
		FinancialInstitutionIdentification7 finInstnId = new FinancialInstitutionIdentification7();

		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_288, PAIN001Constant.BIC_290)) {
			finInstnId.setBIC("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_288, PAIN001Constant.CLRSYSMMBID_858)) {
			finInstnId.setClrSysMmbId(setFinClrSysMmbId(tagMap));
		}

		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_288, PAIN001Constant.NM_292)) {
			finInstnId.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_288, PAIN001Constant.PSTLADR_294)) {
			finInstnId.setPstlAdr(setFinInstnPostalAddress6(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_288, PAIN001Constant.OTHR_859)) {
			finInstnId.setOthr(setFinOther(tagMap));
		}
		fwdgAgt.setFinInstnId(finInstnId);
	}

	/*
	 * "nm", "pstlAdr", "id", "ctryOfRes", "ctctDtls"
	 */
	private PartyIdentification32 setInitgPtyPartyIdentificatio32(Map<Integer, Set<Integer>> tagMap) {
		PartyIdentification32 initgPty = new PartyIdentification32();
		if (isTagPresent(tagMap, PAIN001Constant.INITGPTY_7, PAIN001Constant.NM_8)) {
			initgPty.setNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.INITGPTY_7, PAIN001Constant.PSTLADR_159)) {
			initgPty.setPstlAdr(setPostalAddress6(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.INITGPTY_7, PAIN001Constant.ID_524)) {
			initgPty.setId(setInItPtyId(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.INITGPTY_7, PAIN001Constant.CTCTDTLS_526)) {
			initgPty.setCtctDtls(setCtctDtls(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.INITGPTY_7, PAIN001Constant.CTRYOFRES_525)) {
			initgPty.setCtryOfRes("XXXX");
		}
		return initgPty;
	}

	private ContactDetails2 setCdtrPartyContactDetails2(Map<Integer, Set<Integer>> tagMap) {
		ContactDetails2 CtctDtls = new ContactDetails2();
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_703, PAIN001Constant.EMAILADR_729)) {
			CtctDtls.setEmailAdr("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_703, PAIN001Constant.FAXNB_728)) {
			CtctDtls.setFaxNb("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_703, PAIN001Constant.MOBNB_727)) {
			CtctDtls.setMobNb("XXXXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_703, PAIN001Constant.NM_725)) {
			CtctDtls.setNm("CXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_703, PAIN001Constant.NMPRFX_724)) {
			CtctDtls.setNmPrfx(NamePrefix1Code.MISS.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_703, PAIN001Constant.OTHR_730)) {
			CtctDtls.setOthr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_703, PAIN001Constant.PHNENB_726)) {
			CtctDtls.setPhneNb("XXXXXX");
		}
		return CtctDtls;
	}

	private ContactDetails2 setUltmtCdtrContactDetails2(Map<Integer, Set<Integer>> tagMap) {
		ContactDetails2 CtctDtls = new ContactDetails2();
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_733, PAIN001Constant.EMAILADR_748)) {
			CtctDtls.setEmailAdr("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_733, PAIN001Constant.FAXNB_747)) {
			CtctDtls.setFaxNb("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_733, PAIN001Constant.MOBNB_746)) {
			CtctDtls.setMobNb("XXXXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_733, PAIN001Constant.NM_744)) {
			CtctDtls.setNm("CXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_733, PAIN001Constant.NMPRFX_743)) {
			CtctDtls.setNmPrfx(NamePrefix1Code.MISS.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_733, PAIN001Constant.OTHR_749)) {
			CtctDtls.setOthr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_733, PAIN001Constant.PHNENB_745)) {
			CtctDtls.setPhneNb("XXXXXX");
		}
		return CtctDtls;
	}

	private ContactDetails2 setInvcrContactDetails2(Map<Integer, Set<Integer>> tagMap) {
		ContactDetails2 CtctDtls = new ContactDetails2();
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_769, PAIN001Constant.EMAILADR_825)) {
			CtctDtls.setEmailAdr("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_769, PAIN001Constant.FAXNB_824)) {
			CtctDtls.setFaxNb("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_769, PAIN001Constant.MOBNB_823)) {
			CtctDtls.setMobNb("XXXXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_769, PAIN001Constant.NM_821)) {
			CtctDtls.setNm("CXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_769, PAIN001Constant.NMPRFX_820)) {
			CtctDtls.setNmPrfx(NamePrefix1Code.MISS.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_769, PAIN001Constant.OTHR_826)) {
			CtctDtls.setOthr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_769, PAIN001Constant.PHNENB_822)) {
			CtctDtls.setPhneNb("XXXXXX");
		}
		return CtctDtls;
	}

	private ContactDetails2 setInvceeContactDetails2(Map<Integer, Set<Integer>> tagMap) {
		ContactDetails2 CtctDtls = new ContactDetails2();
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_772, PAIN001Constant.EMAILADR_798)) {
			CtctDtls.setEmailAdr("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_772, PAIN001Constant.FAXNB_797)) {
			CtctDtls.setFaxNb("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_772, PAIN001Constant.MOBNB_796)) {
			CtctDtls.setMobNb("XXXXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_772, PAIN001Constant.NM_794)) {
			CtctDtls.setNm("CXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_772, PAIN001Constant.NMPRFX_793)) {
			CtctDtls.setNmPrfx(NamePrefix1Code.MISS.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_772, PAIN001Constant.OTHR_799)) {
			CtctDtls.setOthr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_772, PAIN001Constant.PHNENB_795)) {
			CtctDtls.setPhneNb("XXXXXX");
		}
		return CtctDtls;
	}

	private ContactDetails2 setUltmtDbtrContactDetails2(Map<Integer, Set<Integer>> tagMap) {
		ContactDetails2 CtctDtls = new ContactDetails2();
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_669, PAIN001Constant.EMAILADR_845)) {
			CtctDtls.setEmailAdr("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_669, PAIN001Constant.FAXNB_844)) {
			CtctDtls.setFaxNb("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_669, PAIN001Constant.MOBNB_843)) {
			CtctDtls.setMobNb("XXXXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_669, PAIN001Constant.NM_841)) {
			CtctDtls.setNm("CXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_669, PAIN001Constant.NMPRFX_840)) {
			CtctDtls.setNmPrfx(NamePrefix1Code.MISS.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_669, PAIN001Constant.OTHR_846)) {
			CtctDtls.setOthr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_669, PAIN001Constant.PHNENB_842)) {
			CtctDtls.setPhneNb("XXXXXX");
		}
		return CtctDtls;
	}

	private ContactDetails2 setinitgPtyContactDetails2(Map<Integer, Set<Integer>> tagMap) {
		ContactDetails2 CtctDtls = new ContactDetails2();
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_614, PAIN001Constant.EMAILADR_640)) {
			CtctDtls.setEmailAdr("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_614, PAIN001Constant.FAXNB_639)) {
			CtctDtls.setFaxNb("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_614, PAIN001Constant.MOBNB_638)) {
			CtctDtls.setMobNb("XXXXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_614, PAIN001Constant.NM_636)) {
			CtctDtls.setNm("CXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_614, PAIN001Constant.NMPRFX_635)) {
			CtctDtls.setNmPrfx(NamePrefix1Code.MISS.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_614, PAIN001Constant.OTHR_641)) {
			CtctDtls.setOthr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_614, PAIN001Constant.PHNENB_637)) {
			CtctDtls.setPhneNb("XXXXXX");
		}
		return CtctDtls;
	}

	private ContactDetails2 setDbtrPartyCtctDtls(Map<Integer, Set<Integer>> tagMap) {
		ContactDetails2 CtctDtls = new ContactDetails2();
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_567, PAIN001Constant.EMAILADR_590)) {
			CtctDtls.setEmailAdr("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_567, PAIN001Constant.FAXNB_589)) {
			CtctDtls.setFaxNb("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_567, PAIN001Constant.MOBNB_588)) {
			CtctDtls.setMobNb("XXXXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_567, PAIN001Constant.NM_586)) {
			CtctDtls.setNm("CXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_567, PAIN001Constant.NMPRFX_585)) {
			CtctDtls.setNmPrfx(NamePrefix1Code.MISS.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_567, PAIN001Constant.OTHR_591)) {
			CtctDtls.setOthr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_567, PAIN001Constant.PHNENB_587)) {
			CtctDtls.setPhneNb("XXXXXX");
		}
		return CtctDtls;
	}

	private ContactDetails2 setCtctDtls(Map<Integer, Set<Integer>> tagMap) {
		ContactDetails2 CtctDtls = new ContactDetails2();
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_526, PAIN001Constant.EMAILADR_552)) {
			CtctDtls.setEmailAdr("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_526, PAIN001Constant.FAXNB_551)) {
			CtctDtls.setFaxNb("XXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_526, PAIN001Constant.MOBNB_550)) {
			CtctDtls.setMobNb("XXXXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_526, PAIN001Constant.NM_548)) {
			CtctDtls.setNm("CXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_526, PAIN001Constant.NMPRFX_547)) {
			CtctDtls.setNmPrfx(NamePrefix1Code.MISS.toString());
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_526, PAIN001Constant.OTHR_553)) {
			CtctDtls.setOthr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CTCTDTLS_526, PAIN001Constant.PHNENB_549)) {
			CtctDtls.setPhneNb("XXXXXX");
		}
		return CtctDtls;
	}

	/*
	 * "orgId", "prvtId"
	 */
	private Party6Choice setCdtrPartyIdParty6Choice(Map<Integer, Set<Integer>> tagMap) {
		Party6Choice initgPtyId = new Party6Choice();
		if (isTagPresent(tagMap, PAIN001Constant.ID_701, PAIN001Constant.ORGID_704)) {
			initgPtyId.setOrgId(setCdtrPartyIdOrgId(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_701, PAIN001Constant.PRVTID_705)) {
			initgPtyId.setPrvtId(setCdtrPartyIdPrvtId(tagMap));
		}
		return initgPtyId;
	}

	/*
	 * "orgId", "prvtId"
	 */
	private Party6Choice setIUltmtDbtrParty6Choice(Map<Integer, Set<Integer>> tagMap) {
		Party6Choice initgPtyId = new Party6Choice();
		if (isTagPresent(tagMap, PAIN001Constant.ID_667, PAIN001Constant.ORGID_831)) {
			initgPtyId.setOrgId(setUltmtDbtrOrgId(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_667, PAIN001Constant.PRVTID_832)) {
			initgPtyId.setPrvtId(setUltmtDbtrPrvtId(tagMap));
		}
		return initgPtyId;
	}

	/*
	 * "orgId", "prvtId"
	 */
	private Party6Choice setUltmtCdtrParty6Choice(Map<Integer, Set<Integer>> tagMap) {
		Party6Choice initgPtyId = new Party6Choice();
		if (isTagPresent(tagMap, PAIN001Constant.ID_731, PAIN001Constant.ORGID_734)) {
			initgPtyId.setOrgId(setUltmtCdtrPtyIdOrgId(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_731, PAIN001Constant.PRVTID_735)) {
			initgPtyId.setPrvtId(setUltmtCdtrPrvtId(tagMap));
		}
		return initgPtyId;
	}

	/*
	 * "orgId", "prvtId"
	 */
	private Party6Choice setInvcrParty6Choice(Map<Integer, Set<Integer>> tagMap) {
		Party6Choice initgPtyId = new Party6Choice();
		if (isTagPresent(tagMap, PAIN001Constant.ID_767, PAIN001Constant.ORGID_800)) {
			initgPtyId.setOrgId(setInvcrIdOrgId(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_767, PAIN001Constant.PRVTID_801)) {
			initgPtyId.setPrvtId(setInvcrIdPrvtId(tagMap));
		}
		return initgPtyId;
	}

	/*
	 * "orgId", "prvtId"
	 */
	private Party6Choice setInvceeParty6Choice(Map<Integer, Set<Integer>> tagMap) {
		Party6Choice initgPtyId = new Party6Choice();
		if (isTagPresent(tagMap, PAIN001Constant.ID_770, PAIN001Constant.ORGID_773)) {
			initgPtyId.setOrgId(setInvceeIdOrgId(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_770, PAIN001Constant.PRVTID_774)) {
			initgPtyId.setPrvtId(setInvceePrvtId(tagMap));
		}
		return initgPtyId;
	}

	/*
	 * "orgId", "prvtId"
	 */
	private Party6Choice setInitgPtyParty6Choice(Map<Integer, Set<Integer>> tagMap) {
		Party6Choice initgPtyId = new Party6Choice();
		if (isTagPresent(tagMap, PAIN001Constant.ID_612, PAIN001Constant.ORGID_615)) {
			initgPtyId.setOrgId(setInitgPtyInitgPtyIdOrgId(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_612, PAIN001Constant.PRVTID_616)) {
			initgPtyId.setPrvtId(setInitgPtyInitgPtyIdPrvtId(tagMap));
		}
		return initgPtyId;
	}

	/*
	 * "orgId", "prvtId"
	 */
	private Party6Choice setDbtrPartyInitgPtyId(Map<Integer, Set<Integer>> tagMap) {
		Party6Choice initgPtyId = new Party6Choice();
		if (isTagPresent(tagMap, PAIN001Constant.ID_73, PAIN001Constant.ORGID_74)) {
			initgPtyId.setOrgId(setDbtrPartyInitgPtyIdOrgId(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_73, PAIN001Constant.PRVTID_568)) {
			initgPtyId.setPrvtId(setDbtrPartyInitgPtyIdPrvtId(tagMap));
		}
		return initgPtyId;
	}

	/*
	 * "orgId", "prvtId"
	 */
	private Party6Choice setInItPtyId(Map<Integer, Set<Integer>> tagMap) {
		Party6Choice initgPtyId = new Party6Choice();
		if (isTagPresent(tagMap, PAIN001Constant.ID_524, PAIN001Constant.ORGID_527)) {
			initgPtyId.setOrgId(setInitgPtyIdOrgId(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ID_524, PAIN001Constant.PRVTID_528)) {
			initgPtyId.setPrvtId(setInitgPtyIdPrvtId(tagMap));
		}
		return initgPtyId;
	}

	private PersonIdentification5 setCdtrPartyIdPrvtId(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentification5 initgPtyIdPrvtId = new PersonIdentification5();
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_705, PAIN001Constant.DTANDPLCOFBIRTH_713)) {
			initgPtyIdPrvtId.setDtAndPlcOfBirth(setCdtrPartyIdPlcOfBirth(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_705, PAIN001Constant.OTHR_714)) {
			initgPtyIdPrvtId.setOthr(setCdtrPartyIdPrvtOth(tagMap));
		}
		return initgPtyIdPrvtId;
	}

	private PersonIdentification5 setUltmtCdtrPrvtId(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentification5 initgPtyIdPrvtId = new PersonIdentification5();
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_735, PAIN001Constant.DTANDPLCOFBIRTH_750)) {
			initgPtyIdPrvtId.setDtAndPlcOfBirth(setUltmtCdtrPlcOfBirth(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_735, PAIN001Constant.OTHR_751)) {
			initgPtyIdPrvtId.setOthr(setUltmtCdtrPrvtOth(tagMap));
		}
		return initgPtyIdPrvtId;
	}

	private PersonIdentification5 setInvcrIdPrvtId(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentification5 initgPtyIdPrvtId = new PersonIdentification5();
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_801, PAIN001Constant.DTANDPLCOFBIRTH_809)) {
			initgPtyIdPrvtId.setDtAndPlcOfBirth(setInvcrDtAndPlcOfBirth(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_801, PAIN001Constant.OTHR_810)) {
			initgPtyIdPrvtId.setOthr(setInitgPtyInitgPtyIdPrvtOth(tagMap));
		}
		return initgPtyIdPrvtId;
	}

	private PersonIdentification5 setInvceePrvtId(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentification5 initgPtyIdPrvtId = new PersonIdentification5();
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_774, PAIN001Constant.DTANDPLCOFBIRTH_782)) {
			initgPtyIdPrvtId.setDtAndPlcOfBirth(setInvceePlcOfBirth(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_774, PAIN001Constant.OTHR_783)) {
			initgPtyIdPrvtId.setOthr(setInvceeOth(tagMap));
		}
		return initgPtyIdPrvtId;
	}

	private PersonIdentification5 setUltmtDbtrPrvtId(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentification5 initgPtyIdPrvtId = new PersonIdentification5();
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_832, PAIN001Constant.DTANDPLCOFBIRTH_847)) {
			initgPtyIdPrvtId.setDtAndPlcOfBirth(setUltmtDbtrPlcOfBirth(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_832, PAIN001Constant.OTHR_848)) {
			initgPtyIdPrvtId.setOthr(setUltmtDbtrPrvtOth(tagMap));
		}
		return initgPtyIdPrvtId;
	}

	private PersonIdentification5 setInitgPtyInitgPtyIdPrvtId(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentification5 initgPtyIdPrvtId = new PersonIdentification5();
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_616, PAIN001Constant.DTANDPLCOFBIRTH_624)) {
			initgPtyIdPrvtId.setDtAndPlcOfBirth(setInitgPtyDtAndPlcOfBirth(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_616, PAIN001Constant.OTHR_625)) {
			initgPtyIdPrvtId.setOthr(setInitgPtyInitgPtyIdOth(tagMap));
		}
		return initgPtyIdPrvtId;
	}

	private PersonIdentification5 setDbtrPartyInitgPtyIdPrvtId(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentification5 initgPtyIdPrvtId = new PersonIdentification5();
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_568, PAIN001Constant.DTANDPLCOFBIRTH_574)) {
			initgPtyIdPrvtId.setDtAndPlcOfBirth(setDbtrPartyDtAndPlcOfBirth(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_568, PAIN001Constant.OTHR_575)) {
			initgPtyIdPrvtId.setOthr(setDbtrPartyInitgPtyIdPrvtOth(tagMap));
		}
		return initgPtyIdPrvtId;
	}

	private PersonIdentification5 setInitgPtyIdPrvtId(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentification5 initgPtyIdPrvtId = new PersonIdentification5();
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_528, PAIN001Constant.DTANDPLCOFBIRTH_536)) {
			initgPtyIdPrvtId.setDtAndPlcOfBirth(setDtAndPlcOfBirth(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.PRVTID_528, PAIN001Constant.OTHR_537)) {
			initgPtyIdPrvtId.setOthr(setinitgPtyIdPrvtOth(tagMap));
		}
		return initgPtyIdPrvtId;
	}

	private List<GenericPersonIdentification1> setCdtrPartyIdPrvtOth(Map<Integer, Set<Integer>> tagMap) {
		List<GenericPersonIdentification1> initgPtyIdPrvtOth = new ArrayList<>();
		GenericPersonIdentification1 gpidn = new GenericPersonIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_714, PAIN001Constant.ID_719)) {
			gpidn.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_714, PAIN001Constant.ISSR_721)) {
			gpidn.setIssr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_714, PAIN001Constant.SCHMENM_720)) {
			gpidn.setSchmeNm(setCdtrPartyIdInitPtySchma(tagMap));
		}
		initgPtyIdPrvtOth.add(gpidn);
		return initgPtyIdPrvtOth;
	}

	private List<GenericPersonIdentification1> setUltmtCdtrPrvtOth(Map<Integer, Set<Integer>> tagMap) {
		List<GenericPersonIdentification1> initgPtyIdPrvtOth = new ArrayList<>();
		GenericPersonIdentification1 gpidn = new GenericPersonIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_751, PAIN001Constant.ID_756)) {
			gpidn.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_751, PAIN001Constant.ISSR_758)) {
			gpidn.setIssr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_751, PAIN001Constant.SCHMENM_757)) {
			gpidn.setSchmeNm(setUltmtCdtrSchma(tagMap));
		}
		initgPtyIdPrvtOth.add(gpidn);
		return initgPtyIdPrvtOth;
	}

	private List<GenericPersonIdentification1> setInvceeOth(Map<Integer, Set<Integer>> tagMap) {
		List<GenericPersonIdentification1> initgPtyIdPrvtOth = new ArrayList<>();
		GenericPersonIdentification1 gpidn = new GenericPersonIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_783, PAIN001Constant.ID_788)) {
			gpidn.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_783, PAIN001Constant.ISSR_790)) {
			gpidn.setIssr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_783, PAIN001Constant.SCHMENM_789)) {
			gpidn.setSchmeNm(setInvceeSchma(tagMap));
		}
		initgPtyIdPrvtOth.add(gpidn);
		return initgPtyIdPrvtOth;
	}

	private List<GenericPersonIdentification1> setUltmtDbtrPrvtOth(Map<Integer, Set<Integer>> tagMap) {
		List<GenericPersonIdentification1> initgPtyIdPrvtOth = new ArrayList<>();
		GenericPersonIdentification1 gpidn = new GenericPersonIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_848, PAIN001Constant.ID_853)) {
			gpidn.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_848, PAIN001Constant.ISSR_855)) {
			gpidn.setIssr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_848, PAIN001Constant.SCHMENM_854)) {
			gpidn.setSchmeNm(setUltmtDbtrPtySchma(tagMap));
		}
		initgPtyIdPrvtOth.add(gpidn);
		return initgPtyIdPrvtOth;
	}

	private List<GenericPersonIdentification1> setInitgPtyInitgPtyIdOth(Map<Integer, Set<Integer>> tagMap) {
		List<GenericPersonIdentification1> initgPtyIdPrvtOth = new ArrayList<>();
		GenericPersonIdentification1 gpidn = new GenericPersonIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_625, PAIN001Constant.ID_630)) {
			gpidn.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_625, PAIN001Constant.ISSR_632)) {
			gpidn.setIssr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_625, PAIN001Constant.SCHMENM_631)) {
			gpidn.setSchmeNm(setInitgPtyInitSchma(tagMap));
		}
		initgPtyIdPrvtOth.add(gpidn);
		return initgPtyIdPrvtOth;
	}

	private List<GenericPersonIdentification1> setInitgPtyInitgPtyIdPrvtOth(Map<Integer, Set<Integer>> tagMap) {
		List<GenericPersonIdentification1> initgPtyIdPrvtOth = new ArrayList<>();
		GenericPersonIdentification1 gpidn = new GenericPersonIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_810, PAIN001Constant.ID_815)) {
			gpidn.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_810, PAIN001Constant.ISSR_817)) {
			gpidn.setIssr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_810, PAIN001Constant.SCHMENM_816)) {
			gpidn.setSchmeNm(setInitgPtyInitPtySchma(tagMap));
		}
		initgPtyIdPrvtOth.add(gpidn);
		return initgPtyIdPrvtOth;
	}

	private List<GenericPersonIdentification1> setDbtrPartyInitgPtyIdPrvtOth(Map<Integer, Set<Integer>> tagMap) {
		List<GenericPersonIdentification1> initgPtyIdPrvtOth = new ArrayList<>();
		GenericPersonIdentification1 gpidn = new GenericPersonIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_575, PAIN001Constant.ID_580)) {
			gpidn.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_575, PAIN001Constant.ISSR_582)) {
			gpidn.setIssr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_575, PAIN001Constant.SCHMENM_581)) {
			gpidn.setSchmeNm(setDbtrPartyInitPtySchma(tagMap));
		}
		initgPtyIdPrvtOth.add(gpidn);
		return initgPtyIdPrvtOth;
	}

	private List<GenericPersonIdentification1> setinitgPtyIdPrvtOth(Map<Integer, Set<Integer>> tagMap) {
		List<GenericPersonIdentification1> initgPtyIdPrvtOth = new ArrayList<>();
		GenericPersonIdentification1 gpidn = new GenericPersonIdentification1();

		if (isTagPresent(tagMap, PAIN001Constant.OTHR_537, PAIN001Constant.ID_542)) {
			gpidn.setId("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_537, PAIN001Constant.ISSR_544)) {
			gpidn.setIssr("XXXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_537, PAIN001Constant.SCHMENM_543)) {
			gpidn.setSchmeNm(setInitPtySchma(tagMap));
		}
		initgPtyIdPrvtOth.add(gpidn);
		return initgPtyIdPrvtOth;
	}

	private PersonIdentificationSchemeName1Choice setCdtrPartyIdInitPtySchma(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentificationSchemeName1Choice SchmeNm = new PersonIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_720, PAIN001Constant.CD_722)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_720, PAIN001Constant.PRTRY_723)) {
			SchmeNm.setPrtry("XXXXXX");
		}
		return SchmeNm;
	}

	private PersonIdentificationSchemeName1Choice setUltmtCdtrSchma(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentificationSchemeName1Choice SchmeNm = new PersonIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_757, PAIN001Constant.CD_759)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_757, PAIN001Constant.PRTRY_760)) {
			SchmeNm.setPrtry("XXXXXX");
		}
		return SchmeNm;
	}

	private PersonIdentificationSchemeName1Choice setInvceeSchma(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentificationSchemeName1Choice SchmeNm = new PersonIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_789, PAIN001Constant.CD_791)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_789, PAIN001Constant.PRTRY_792)) {
			SchmeNm.setPrtry("XXXXXX");
		}
		return SchmeNm;
	}

	private PersonIdentificationSchemeName1Choice setUltmtDbtrPtySchma(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentificationSchemeName1Choice SchmeNm = new PersonIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_854, PAIN001Constant.CD_856)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_854, PAIN001Constant.PRTRY_857)) {
			SchmeNm.setPrtry("XXXXXX");
		}
		return SchmeNm;
	}

	private PersonIdentificationSchemeName1Choice setInitgPtyInitSchma(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentificationSchemeName1Choice SchmeNm = new PersonIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_631, PAIN001Constant.CD_633)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_631, PAIN001Constant.PRTRY_634)) {
			SchmeNm.setPrtry("XXXXXX");
		}
		return SchmeNm;
	}

	private PersonIdentificationSchemeName1Choice setInitgPtyInitPtySchma(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentificationSchemeName1Choice SchmeNm = new PersonIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_816, PAIN001Constant.CD_818)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_816, PAIN001Constant.PRTRY_819)) {
			SchmeNm.setPrtry("XXXXXX");
		}
		return SchmeNm;
	}

	private PersonIdentificationSchemeName1Choice setDbtrPartyInitPtySchma(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentificationSchemeName1Choice SchmeNm = new PersonIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_581, PAIN001Constant.CD_583)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_581, PAIN001Constant.PRTRY_584)) {
			SchmeNm.setPrtry("XXXXXX");
		}
		return SchmeNm;
	}

	private PersonIdentificationSchemeName1Choice setInitPtySchma(Map<Integer, Set<Integer>> tagMap) {
		PersonIdentificationSchemeName1Choice SchmeNm = new PersonIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_543, PAIN001Constant.CD_545)) {
			SchmeNm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_543, PAIN001Constant.PRTRY_546)) {
			SchmeNm.setPrtry("XXXXXX");
		}
		return SchmeNm;
	}

	private DateAndPlaceOfBirth setCdtrPartyIdPlcOfBirth(Map<Integer, Set<Integer>> tagMap) {
		DateAndPlaceOfBirth DtAndPlcOfBirth = new DateAndPlaceOfBirth();
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_713, PAIN001Constant.CITYOFBIRTH_717)) {
			DtAndPlcOfBirth.setCityOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_713, PAIN001Constant.CTRYOFBIRTH_718)) {
			DtAndPlcOfBirth.setCtryOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_713, PAIN001Constant.PRVCOFBIRTH_716)) {
			DtAndPlcOfBirth.setPrvcOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_713, PAIN001Constant.BIRTHDT_715)) {
			DtAndPlcOfBirth.setBirthDt(LocalDate.now());
		}
		return DtAndPlcOfBirth;
	}

	private DateAndPlaceOfBirth setUltmtCdtrPlcOfBirth(Map<Integer, Set<Integer>> tagMap) {
		DateAndPlaceOfBirth DtAndPlcOfBirth = new DateAndPlaceOfBirth();
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_750, PAIN001Constant.CITYOFBIRTH_754)) {
			DtAndPlcOfBirth.setCityOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_750, PAIN001Constant.CTRYOFBIRTH_755)) {
			DtAndPlcOfBirth.setCtryOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_750, PAIN001Constant.PRVCOFBIRTH_753)) {
			DtAndPlcOfBirth.setPrvcOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_750, PAIN001Constant.BIRTHDT_752)) {
			DtAndPlcOfBirth.setBirthDt(LocalDate.now());
		}
		return DtAndPlcOfBirth;
	}

	private DateAndPlaceOfBirth setInvcrDtAndPlcOfBirth(Map<Integer, Set<Integer>> tagMap) {
		DateAndPlaceOfBirth DtAndPlcOfBirth = new DateAndPlaceOfBirth();
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_809, PAIN001Constant.CITYOFBIRTH_813)) {
			DtAndPlcOfBirth.setCityOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_809, PAIN001Constant.CTRYOFBIRTH_814)) {
			DtAndPlcOfBirth.setCtryOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_809, PAIN001Constant.PRVCOFBIRTH_812)) {
			DtAndPlcOfBirth.setPrvcOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_809, PAIN001Constant.BIRTHDT_811)) {
			DtAndPlcOfBirth.setBirthDt(LocalDate.now());
		}
		return DtAndPlcOfBirth;
	}

	private DateAndPlaceOfBirth setInvceePlcOfBirth(Map<Integer, Set<Integer>> tagMap) {
		DateAndPlaceOfBirth DtAndPlcOfBirth = new DateAndPlaceOfBirth();
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_782, PAIN001Constant.CITYOFBIRTH_786)) {
			DtAndPlcOfBirth.setCityOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_782, PAIN001Constant.CTRYOFBIRTH_787)) {
			DtAndPlcOfBirth.setCtryOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_782, PAIN001Constant.PRVCOFBIRTH_785)) {
			DtAndPlcOfBirth.setPrvcOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_782, PAIN001Constant.BIRTHDT_784)) {
			DtAndPlcOfBirth.setBirthDt(LocalDate.now());
		}
		return DtAndPlcOfBirth;
	}

	private DateAndPlaceOfBirth setUltmtDbtrPlcOfBirth(Map<Integer, Set<Integer>> tagMap) {
		DateAndPlaceOfBirth DtAndPlcOfBirth = new DateAndPlaceOfBirth();
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_847, PAIN001Constant.CITYOFBIRTH_851)) {
			DtAndPlcOfBirth.setCityOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_847, PAIN001Constant.CTRYOFBIRTH_852)) {
			DtAndPlcOfBirth.setCtryOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_847, PAIN001Constant.PRVCOFBIRTH_850)) {
			DtAndPlcOfBirth.setPrvcOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_847, PAIN001Constant.BIRTHDT_849)) {
			DtAndPlcOfBirth.setBirthDt(LocalDate.now());
		}
		return DtAndPlcOfBirth;
	}

	private DateAndPlaceOfBirth setInitgPtyDtAndPlcOfBirth(Map<Integer, Set<Integer>> tagMap) {
		DateAndPlaceOfBirth DtAndPlcOfBirth = new DateAndPlaceOfBirth();
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_624, PAIN001Constant.CITYOFBIRTH_628)) {
			DtAndPlcOfBirth.setCityOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_624, PAIN001Constant.CTRYOFBIRTH_629)) {
			DtAndPlcOfBirth.setCtryOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_624, PAIN001Constant.PRVCOFBIRTH_627)) {
			DtAndPlcOfBirth.setPrvcOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_624, PAIN001Constant.BIRTHDT_626)) {
			DtAndPlcOfBirth.setBirthDt(LocalDate.now());
		}
		return DtAndPlcOfBirth;
	}

	private DateAndPlaceOfBirth setDbtrPartyDtAndPlcOfBirth(Map<Integer, Set<Integer>> tagMap) {
		DateAndPlaceOfBirth DtAndPlcOfBirth = new DateAndPlaceOfBirth();
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_574, PAIN001Constant.CITYOFBIRTH_578)) {
			DtAndPlcOfBirth.setCityOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_574, PAIN001Constant.CTRYOFBIRTH_579)) {
			DtAndPlcOfBirth.setCtryOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_574, PAIN001Constant.PRVCOFBIRTH_577)) {
			DtAndPlcOfBirth.setPrvcOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_574, PAIN001Constant.BIRTHDT_576)) {
			DtAndPlcOfBirth.setBirthDt(LocalDate.now());
		}
		return DtAndPlcOfBirth;
	}

	private DateAndPlaceOfBirth setDtAndPlcOfBirth(Map<Integer, Set<Integer>> tagMap) {
		DateAndPlaceOfBirth DtAndPlcOfBirth = new DateAndPlaceOfBirth();
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_536, PAIN001Constant.CITYOFBIRTH_540)) {
			DtAndPlcOfBirth.setCityOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_536, PAIN001Constant.CTRYOFBIRTH_541)) {
			DtAndPlcOfBirth.setCtryOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_536, PAIN001Constant.PRVCOFBIRTH_539)) {
			DtAndPlcOfBirth.setPrvcOfBirth("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DTANDPLCOFBIRTH_536, PAIN001Constant.BIRTHDT_538)) {
			DtAndPlcOfBirth.setBirthDt(LocalDate.now());
		}
		return DtAndPlcOfBirth;
	}

	/*
	 * "bicOrBEI", "othr"
	 */
	private OrganisationIdentification4 setCdtrPartyIdOrgId(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentification4 initgPtyIdOrgId = new OrganisationIdentification4();
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_704, PAIN001Constant.BICORBEI_706)) {
			initgPtyIdOrgId.setBICOrBEI("BIC");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_704, PAIN001Constant.OTHR_707)) {
			initgPtyIdOrgId.setOthr(setCdtrPartyIdOrgIdOthr(tagMap));
		}
		return initgPtyIdOrgId;
	}

	/*
	 * "bicOrBEI", "othr"
	 */
	private OrganisationIdentification4 setUltmtCdtrPtyIdOrgId(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentification4 initgPtyIdOrgId = new OrganisationIdentification4();
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_734, PAIN001Constant.BICORBEI_736)) {
			initgPtyIdOrgId.setBICOrBEI("BIC");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_734, PAIN001Constant.OTHR_737)) {
			initgPtyIdOrgId.setOthr(setUltmtCdtrOrgIdOthr(tagMap));
		}
		return initgPtyIdOrgId;
	}

	/*
	 * "bicOrBEI", "othr"
	 */
	private OrganisationIdentification4 setInvcrIdOrgId(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentification4 initgPtyIdOrgId = new OrganisationIdentification4();
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_800, PAIN001Constant.BICORBEI_802)) {
			initgPtyIdOrgId.setBICOrBEI("BIC");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_800, PAIN001Constant.OTHR_803)) {
			initgPtyIdOrgId.setOthr(setInvcrIdOrgIdOthr(tagMap));
		}
		return initgPtyIdOrgId;
	}

	/*
	 * "bicOrBEI", "othr"
	 */
	OrganisationIdentification4 initgPtyIdOrgId = new OrganisationIdentification4();

	private OrganisationIdentification4 setInvceeIdOrgId(Map<Integer, Set<Integer>> tagMap) {
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_773, PAIN001Constant.BICORBEI_775)) {
			initgPtyIdOrgId.setBICOrBEI("BIC");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_773, PAIN001Constant.OTHR_776)) {
			initgPtyIdOrgId.setOthr(setInvceeIdOthr(tagMap));
		}
		return initgPtyIdOrgId;
	}

	/*
	 * "bicOrBEI", "othr"
	 */
	private OrganisationIdentification4 setUltmtDbtrOrgId(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentification4 initgPtyIdOrgId = new OrganisationIdentification4();
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_831, PAIN001Constant.BICORBEI_833)) {
			initgPtyIdOrgId.setBICOrBEI("BIC");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_831, PAIN001Constant.OTHR_834)) {
			initgPtyIdOrgId.setOthr(setUltmtDbtrOthr(tagMap));
		}
		return initgPtyIdOrgId;
	}

	/*
	 * "bicOrBEI", "othr"
	 */
	private OrganisationIdentification4 setInitgPtyInitgPtyIdOrgId(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentification4 initgPtyIdOrgId = new OrganisationIdentification4();
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_615, PAIN001Constant.BICORBEI_617)) {
			initgPtyIdOrgId.setBICOrBEI("BIC");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_615, PAIN001Constant.OTHR_618)) {
			initgPtyIdOrgId.setOthr(setInitgPtyInitgPtyIdOrgIdOthr(tagMap));
		}
		return initgPtyIdOrgId;
	}

	/*
	 * "bicOrBEI", "othr"
	 */
	private OrganisationIdentification4 setDbtrPartyInitgPtyIdOrgId(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentification4 initgPtyIdOrgId = new OrganisationIdentification4();
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_74, PAIN001Constant.BICORBEI_569)) {
			initgPtyIdOrgId.setBICOrBEI("BIC");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_74, PAIN001Constant.OTHR_75)) {
			initgPtyIdOrgId.setOthr(setDbtrPartyInitgPtyIdOrgIdOthr(tagMap));
		}
		return initgPtyIdOrgId;
	}

	/*
	 * "bicOrBEI", "othr"
	 */
	private OrganisationIdentification4 setInitgPtyIdOrgId(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentification4 initgPtyIdOrgId = new OrganisationIdentification4();
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_527, PAIN001Constant.BICORBEI_529)) {
			initgPtyIdOrgId.setBICOrBEI("BIC");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ORGID_527, PAIN001Constant.OTHR_530)) {
			initgPtyIdOrgId.setOthr(setInitgPtyIdOrgIdOthr(tagMap));
		}
		return initgPtyIdOrgId;
	}

	private List<GenericOrganisationIdentification1> setCdtrPartyIdOrgIdOthr(Map<Integer, Set<Integer>> tagMap) {
		List<GenericOrganisationIdentification1> InitgPtyIdOrgIdOthr = new ArrayList<>();
		GenericOrganisationIdentification1 goid = new GenericOrganisationIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_707, PAIN001Constant.ID_708)) {
			goid.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_707, PAIN001Constant.ISSR_710)) {
			goid.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_707, PAIN001Constant.SCHMENM_709)) {
			goid.setSchmeNm(setCdtrPartyIdOrgIdOthrScm(tagMap));
		}
		InitgPtyIdOrgIdOthr.add(goid);
		return InitgPtyIdOrgIdOthr;
	}

	private List<GenericOrganisationIdentification1> setUltmtCdtrOrgIdOthr(Map<Integer, Set<Integer>> tagMap) {
		List<GenericOrganisationIdentification1> InitgPtyIdOrgIdOthr = new ArrayList<>();
		GenericOrganisationIdentification1 goid = new GenericOrganisationIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_737, PAIN001Constant.ID_738)) {
			goid.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_737, PAIN001Constant.ISSR_740)) {
			goid.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_737, PAIN001Constant.SCHMENM_739)) {
			goid.setSchmeNm(setUltmtCdtrOrgIdOthrScm(tagMap));
		}
		InitgPtyIdOrgIdOthr.add(goid);
		return InitgPtyIdOrgIdOthr;
	}

	private List<GenericOrganisationIdentification1> setInvcrIdOrgIdOthr(Map<Integer, Set<Integer>> tagMap) {
		List<GenericOrganisationIdentification1> InitgPtyIdOrgIdOthr = new ArrayList<>();
		GenericOrganisationIdentification1 goid = new GenericOrganisationIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_803, PAIN001Constant.ID_804)) {
			goid.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_803, PAIN001Constant.ISSR_806)) {
			goid.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_803, PAIN001Constant.SCHMENM_805)) {
			goid.setSchmeNm(setInvcrIdOrgIdOthrScm(tagMap));
		}
		InitgPtyIdOrgIdOthr.add(goid);
		return InitgPtyIdOrgIdOthr;
	}

	private List<GenericOrganisationIdentification1> setInvceeIdOthr(Map<Integer, Set<Integer>> tagMap) {
		List<GenericOrganisationIdentification1> InitgPtyIdOrgIdOthr = new ArrayList<>();
		GenericOrganisationIdentification1 goid = new GenericOrganisationIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_776, PAIN001Constant.ID_777)) {
			goid.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_776, PAIN001Constant.ISSR_779)) {
			goid.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_776, PAIN001Constant.SCHMENM_778)) {
			goid.setSchmeNm(setInvceeOthrScm(tagMap));
		}
		InitgPtyIdOrgIdOthr.add(goid);
		return InitgPtyIdOrgIdOthr;
	}

	private List<GenericOrganisationIdentification1> setUltmtDbtrOthr(Map<Integer, Set<Integer>> tagMap) {
		List<GenericOrganisationIdentification1> InitgPtyIdOrgIdOthr = new ArrayList<>();
		GenericOrganisationIdentification1 goid = new GenericOrganisationIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_834, PAIN001Constant.ID_835)) {
			goid.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_834, PAIN001Constant.ISSR_837)) {
			goid.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_834, PAIN001Constant.SCHMENM_836)) {
			goid.setSchmeNm(setUltmtDbtrOthrScm(tagMap));
		}
		InitgPtyIdOrgIdOthr.add(goid);
		return InitgPtyIdOrgIdOthr;
	}

	private List<GenericOrganisationIdentification1> setInitgPtyInitgPtyIdOrgIdOthr(Map<Integer, Set<Integer>> tagMap) {
		List<GenericOrganisationIdentification1> InitgPtyIdOrgIdOthr = new ArrayList<>();
		GenericOrganisationIdentification1 goid = new GenericOrganisationIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_618, PAIN001Constant.ID_619)) {
			goid.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_618, PAIN001Constant.ISSR_621)) {
			goid.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_618, PAIN001Constant.SCHMENM_620)) {
			goid.setSchmeNm(setInitgPtyInitgPtyIdOrgIdOthrScm(tagMap));
		}
		InitgPtyIdOrgIdOthr.add(goid);
		return InitgPtyIdOrgIdOthr;
	}

	private List<GenericOrganisationIdentification1> setDbtrPartyInitgPtyIdOrgIdOthr(
			Map<Integer, Set<Integer>> tagMap) {
		List<GenericOrganisationIdentification1> InitgPtyIdOrgIdOthr = new ArrayList<>();
		GenericOrganisationIdentification1 goid = new GenericOrganisationIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_75, PAIN001Constant.ID_76)) {
			goid.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_75, PAIN001Constant.ISSR_571)) {
			goid.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_75, PAIN001Constant.SCHMENM_570)) {
			goid.setSchmeNm(setDbtrPartyInitgPtyIdOrgIdOthrScm(tagMap));
		}
		InitgPtyIdOrgIdOthr.add(goid);
		return InitgPtyIdOrgIdOthr;
	}

	private List<GenericOrganisationIdentification1> setInitgPtyIdOrgIdOthr(Map<Integer, Set<Integer>> tagMap) {
		List<GenericOrganisationIdentification1> InitgPtyIdOrgIdOthr = new ArrayList<>();
		GenericOrganisationIdentification1 goid = new GenericOrganisationIdentification1();
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_530, PAIN001Constant.ID_531)) {
			goid.setId("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_530, PAIN001Constant.ISSR_533)) {
			goid.setIssr("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.OTHR_530, PAIN001Constant.SCHMENM_532)) {
			goid.setSchmeNm(setInitgPtyIdOrgIdOthrScm(tagMap));
		}
		InitgPtyIdOrgIdOthr.add(goid);
		return InitgPtyIdOrgIdOthr;
	}

	private OrganisationIdentificationSchemeName1Choice setCdtrPartyIdOrgIdOthrScm(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentificationSchemeName1Choice InitgPtyIdOrgIdOthrScm = new OrganisationIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_709, PAIN001Constant.CD_711)) {
			InitgPtyIdOrgIdOthrScm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_709, PAIN001Constant.PRTRY_712)) {
			InitgPtyIdOrgIdOthrScm.setPrtry("XXXX");
		}
		return InitgPtyIdOrgIdOthrScm;
	}

	private OrganisationIdentificationSchemeName1Choice setUltmtCdtrOrgIdOthrScm(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentificationSchemeName1Choice InitgPtyIdOrgIdOthrScm = new OrganisationIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_739, PAIN001Constant.CD_741)) {
			InitgPtyIdOrgIdOthrScm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_739, PAIN001Constant.PRTRY_742)) {
			InitgPtyIdOrgIdOthrScm.setPrtry("XXXX");
		}
		return InitgPtyIdOrgIdOthrScm;
	}

	private OrganisationIdentificationSchemeName1Choice setInvcrIdOrgIdOthrScm(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentificationSchemeName1Choice InitgPtyIdOrgIdOthrScm = new OrganisationIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_805, PAIN001Constant.CD_807)) {
			InitgPtyIdOrgIdOthrScm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_805, PAIN001Constant.PRTRY_808)) {
			InitgPtyIdOrgIdOthrScm.setPrtry("XXXX");
		}
		return InitgPtyIdOrgIdOthrScm;
	}

	private OrganisationIdentificationSchemeName1Choice setInvceeOthrScm(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentificationSchemeName1Choice InitgPtyIdOrgIdOthrScm = new OrganisationIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_778, PAIN001Constant.CD_780)) {
			InitgPtyIdOrgIdOthrScm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_778, PAIN001Constant.PRTRY_781)) {
			InitgPtyIdOrgIdOthrScm.setPrtry("XXXX");
		}
		return InitgPtyIdOrgIdOthrScm;
	}

	private OrganisationIdentificationSchemeName1Choice setUltmtDbtrOthrScm(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentificationSchemeName1Choice InitgPtyIdOrgIdOthrScm = new OrganisationIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_836, PAIN001Constant.CD_838)) {
			InitgPtyIdOrgIdOthrScm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_836, PAIN001Constant.PRTRY_839)) {
			InitgPtyIdOrgIdOthrScm.setPrtry("XXXX");
		}
		return InitgPtyIdOrgIdOthrScm;
	}

	private OrganisationIdentificationSchemeName1Choice setInitgPtyInitgPtyIdOrgIdOthrScm(
			Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentificationSchemeName1Choice InitgPtyIdOrgIdOthrScm = new OrganisationIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_620, PAIN001Constant.CD_622)) {
			InitgPtyIdOrgIdOthrScm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_620, PAIN001Constant.PRTRY_623)) {
			InitgPtyIdOrgIdOthrScm.setPrtry("XXXX");
		}
		return InitgPtyIdOrgIdOthrScm;
	}

	private OrganisationIdentificationSchemeName1Choice setDbtrPartyInitgPtyIdOrgIdOthrScm(
			Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentificationSchemeName1Choice InitgPtyIdOrgIdOthrScm = new OrganisationIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_570, PAIN001Constant.CD_572)) {
			InitgPtyIdOrgIdOthrScm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_570, PAIN001Constant.PRTRY_573)) {
			InitgPtyIdOrgIdOthrScm.setPrtry("XXXX");
		}
		return InitgPtyIdOrgIdOthrScm;
	}

	private OrganisationIdentificationSchemeName1Choice setInitgPtyIdOrgIdOthrScm(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentificationSchemeName1Choice InitgPtyIdOrgIdOthrScm = new OrganisationIdentificationSchemeName1Choice();
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_532, PAIN001Constant.CD_534)) {
			InitgPtyIdOrgIdOthrScm.setCd("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.SCHMENM_532, PAIN001Constant.PRTRY_535)) {
			InitgPtyIdOrgIdOthrScm.setPrtry("XXXX");
		}
		return InitgPtyIdOrgIdOthrScm;
	}

	/*
	 * "nm", "pstlAdr", "id", "ctryOfRes", "ctctDtls"
	 */
	private PartyIdentification32 setDbtrPartyIdentificatio32(Map<Integer, Set<Integer>> tagMap) {
		PartyIdentification32 initgPty = new PartyIdentification32();
		if (isTagPresent(tagMap, PAIN001Constant.DBTR_21, PAIN001Constant.NM_22)) {
			initgPty.setNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTR_21, PAIN001Constant.PSTLADR_50)) {
			initgPty.setPstlAdr(setDbtrPartyPostalAddress6(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTR_21, PAIN001Constant.ID_73)) {
			initgPty.setId(setDbtrPartyInitgPtyId(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTR_21, PAIN001Constant.CTCTDTLS_567)) {
			initgPty.setCtctDtls(setDbtrPartyCtctDtls(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.DBTR_21, PAIN001Constant.CTRYOFRES_566)) {
			initgPty.setCtryOfRes("XXXXX");
		}
		return initgPty;
	}

	private PartyIdentification32 setCdtrPartyIdentificatio32(Map<Integer, Set<Integer>> tagMap) {
		PartyIdentification32 initgPty = new PartyIdentification32();
		if (isTagPresent(tagMap, PAIN001Constant.CDTR_40, PAIN001Constant.NM_41)) {
			initgPty.setNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTR_40, PAIN001Constant.PSTLADR_47)) {
			initgPty.setPstlAdr(setCdtrPostalAddress6(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTR_40, PAIN001Constant.ID_701)) {
			initgPty.setId(setCdtrPartyIdParty6Choice(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTR_40, PAIN001Constant.CTCTDTLS_703)) {
			initgPty.setCtctDtls(setCdtrPartyContactDetails2(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.CDTR_40, PAIN001Constant.CTRYOFRES_702)) {
			initgPty.setCtryOfRes("XXXXX");
		}
		return initgPty;
	}

	private PartyIdentification32 setUltmtDbtrPartyIdentificatio32(Map<Integer, Set<Integer>> tagMap) {
		PartyIdentification32 initgPty = new PartyIdentification32();
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTDBTR_432, PAIN001Constant.NM_434)) {
			initgPty.setNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTDBTR_432, PAIN001Constant.PSTLADR_436)) {
			initgPty.setPstlAdr(setUltmtDbtrPostalAddress6(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTDBTR_432, PAIN001Constant.ID_667)) {
			initgPty.setId(setIUltmtDbtrParty6Choice(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTDBTR_432, PAIN001Constant.CTCTDTLS_669)) {
			initgPty.setCtctDtls(setUltmtDbtrContactDetails2(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTDBTR_432, PAIN001Constant.CTRYOFRES_668)) {
			initgPty.setCtryOfRes("XXXXX");
		}
		return initgPty;
	}

	private PartyIdentification32 setUltmtCdtrPartyIdentificatio32(Map<Integer, Set<Integer>> tagMap) {
		PartyIdentification32 initgPty = new PartyIdentification32();
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTCDTR_417, PAIN001Constant.NM_419)) {
			initgPty.setNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTCDTR_417, PAIN001Constant.PSTLADR_421)) {
			initgPty.setPstlAdr(setUltmtCdtrPostalAddress6(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTCDTR_417, PAIN001Constant.ID_731)) {
			initgPty.setId(setUltmtCdtrParty6Choice(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTCDTR_417, PAIN001Constant.CTCTDTLS_733)) {
			initgPty.setCtctDtls(setUltmtCdtrContactDetails2(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTCDTR_417, PAIN001Constant.CTRYOFRES_732)) {
			initgPty.setCtryOfRes("XXXXX");
		}
		return initgPty;
	}

	private PartyIdentification32 setInvcrPartyIdentificatio32(Map<Integer, Set<Integer>> tagMap) {
		PartyIdentification32 initgPty = new PartyIdentification32();
		if (isTagPresent(tagMap, PAIN001Constant.INVCR_498, PAIN001Constant.NM_499)) {
			initgPty.setNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.INVCR_498, PAIN001Constant.PSTLADR_500)) {
			initgPty.setPstlAdr(setInvcrPostalAddress6(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.INVCR_498, PAIN001Constant.ID_767)) {
			initgPty.setId(setInvcrParty6Choice(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.INVCR_498, PAIN001Constant.CTCTDTLS_769)) {
			initgPty.setCtctDtls(setInvcrContactDetails2(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.INVCR_498, PAIN001Constant.CTRYOFRES_768)) {
			initgPty.setCtryOfRes("XXXXX");
		}
		return initgPty;
	}

	private PartyIdentification32 setInvceePartyIdentificatio32(Map<Integer, Set<Integer>> tagMap) {
		PartyIdentification32 initgPty = new PartyIdentification32();
		if (isTagPresent(tagMap, PAIN001Constant.INVCEE_511, PAIN001Constant.NM_512)) {
			initgPty.setNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.INVCEE_511, PAIN001Constant.PSTLADR_513)) {
			initgPty.setPstlAdr(setInvceePostalAddress6(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.INVCEE_511, PAIN001Constant.ID_770)) {
			initgPty.setId(setInvceeParty6Choice(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.INVCEE_511, PAIN001Constant.CTCTDTLS_772)) {
			initgPty.setCtctDtls(setInvceeContactDetails2(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.INVCEE_511, PAIN001Constant.CTRYOFRES_771)) {
			initgPty.setCtryOfRes("XXXXX");
		}
		return initgPty;
	}

	private PartyIdentification32 setPartyIdentificatio32(Map<Integer, Set<Integer>> tagMap) {
		PartyIdentification32 initgPty = new PartyIdentification32();
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTDBTR_234, PAIN001Constant.NM_235)) {
			initgPty.setNm("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTDBTR_234, PAIN001Constant.PSTLADR_236)) {
			initgPty.setPstlAdr(setInitgPtyPostalAddress6(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTDBTR_234, PAIN001Constant.ID_612)) {
			initgPty.setId(setInitgPtyParty6Choice(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTDBTR_234, PAIN001Constant.CTCTDTLS_614)) {
			initgPty.setCtctDtls(setinitgPtyContactDetails2(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.ULTMTDBTR_234, PAIN001Constant.CTRYOFRES_613)) {
			initgPty.setCtryOfRes("XXXXX");
		}
		return initgPty;
	}

	private boolean isTagPresent(Map<Integer, Set<Integer>> tagMap, Integer pid, Integer id) {
		if (null != pid && null != id && null != tagMap.get(pid)) {
			Set<Integer> tagSetForpid = tagMap.get(pid);
			return tagSetForpid.contains(id);
		}
		return false;
	}
}
