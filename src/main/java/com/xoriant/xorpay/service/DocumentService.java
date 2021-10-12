package com.xoriant.xorpay.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.PAIN001Constant;
import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.pojo.ConfigPojo;
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
import com.xoriant.xorpay.util.XorUtil;

@Service
public class DocumentService {

	private final Logger logger = LoggerFactory.getLogger(DocumentService.class);
	private boolean isMultiAddressLine;

	/**
	 * "pmtInfId", "pmtMtd", "btchBookg", "nbOfTxs", "ctrlSum", "pmtTpInf",
	 * "reqdExctnDt", "poolgAdjstmntDt", "dbtr", "dbtrAcct", "dbtrAgt",
	 * "dbtrAgtAcct", "ultmtDbtr", "chrgBr", "chrgsAcct", "chrgsAcctAgt",
	 * "cdtTrfTxInf"
	 * 
	 * @param isMultiAddressLine
	 */
	public PaymentInstructionInformation3 setPmtInf(Document doc, Map<Integer, String> tagSeqMap,
			boolean isMultiAddressLine) {
		this.isMultiAddressLine = isMultiAddressLine;
		PaymentInstructionInformation3 PmtInf = new PaymentInstructionInformation3();
		PaymentInstructionInformation3 docPmtInf = doc.getCstmrCdtTrfInitn().getPmtInf().get(0);

		if (null != docPmtInf.getPmtInfId() && isTagPresent(tagSeqMap, PAIN001Constant.PMTINFID_10)) {
			PmtInf.setPmtInfId(tagSeqMap.get(PAIN001Constant.PMTINFID_10));
		}
		if (null != docPmtInf.getPmtMtd() && isTagPresent(tagSeqMap, PAIN001Constant.PMTMTD_11)) {
			String pmtMethod = tagSeqMap.get(PAIN001Constant.PMTMTD_11).trim();
			if (pmtMethod.equalsIgnoreCase("Check") || pmtMethod.equalsIgnoreCase("CHK")
					|| pmtMethod.equalsIgnoreCase("EFT") || pmtMethod.equalsIgnoreCase("WIRE")
					|| pmtMethod.equalsIgnoreCase("TRA")) {
				PmtInf.setPmtMtd(PaymentMethod3Code.TRF.toString());
			} else {
				PmtInf.setPmtMtd(PaymentMethod3Code.TRF.toString());
			}
		}
		if (null != docPmtInf.getPmtMtd() && isTagPresent(tagSeqMap, PAIN001Constant.BTCHBOOKG_185)) {
			PmtInf.setBtchBookg(tagSeqMap.get(PAIN001Constant.BTCHBOOKG_185));
		}
		if (null != docPmtInf.getNbOfTxs() && isTagPresent(tagSeqMap, PAIN001Constant.NBOFTXS_186)) {
			PmtInf.setNbOfTxs(tagSeqMap.get(PAIN001Constant.NBOFTXS_186));
		}
		if (null != docPmtInf.getCtrlSum() && isTagPresent(tagSeqMap, PAIN001Constant.CTRLSUM_187)) {
			PmtInf.setCtrlSum(new BigDecimal(tagSeqMap.get(PAIN001Constant.CTRLSUM_187)));
		}
		if (null != docPmtInf.getPmtTpInf()) {
			PaymentTypeInformation19 PmtTpInf = setPmtTpInf(docPmtInf.getPmtTpInf(), tagSeqMap,
					PAIN001Constant.INSTRPRTY_188, PAIN001Constant.CD_14, PAIN001Constant.PRTRY_189,
					PAIN001Constant.CD_16, PAIN001Constant.PRTRY_190, PAIN001Constant.CD_18, PAIN001Constant.PRTRY_19);
			if (null != PmtTpInf) {
				PmtInf.setPmtTpInf(PmtTpInf);
			}
		}
		if (null != docPmtInf.getReqdExctnDt() && isTagPresent(tagSeqMap, PAIN001Constant.REQDEXCTNDT_20)) {
			PmtInf.setReqdExctnDt(getLocalDate(tagSeqMap.get(PAIN001Constant.REQDEXCTNDT_20)));
		}
		if (null != docPmtInf.getPoolgAdjstmntDt() && isTagPresent(tagSeqMap, PAIN001Constant.POOLGADJSTMNTDT_565)) {
			PmtInf.setPoolgAdjstmntDt(getLocalDate(tagSeqMap.get(PAIN001Constant.POOLGADJSTMNTDT_565)));
		}
		if (null != docPmtInf.getDbtr()) {
			PartyIdentification32 Dbtr = setDbtrPartyIdentificatio32(docPmtInf.getDbtr(), tagSeqMap);
			if (null != Dbtr) {
				PmtInf.setDbtr(Dbtr);
			}
		}
		if (null != docPmtInf.getDbtrAcct()) {
			CashAccount16 DbtrAcct = setDbtrCashAccnt16(docPmtInf.getDbtrAcct(), tagSeqMap, PAIN001Constant.IBAN_199,
					PAIN001Constant.ID_26, PAIN001Constant.ISSR_203, PAIN001Constant.CD_201, PAIN001Constant.PRTRY_202,
					PAIN001Constant.CD_205, PAIN001Constant.PRTRY_206, PAIN001Constant.CCY_207, PAIN001Constant.NM_208);
			if (null != DbtrAcct) {
				PmtInf.setDbtrAcct(DbtrAcct);
			}
		}
		if (null != docPmtInf.getDbtrAgt()) {
			BranchAndFinancialInstitutionIdentification4 DbtrAgt = setDbtrAgtBrFiId4(docPmtInf.getDbtrAgt(), tagSeqMap);
			if (null != DbtrAgt) {
				PmtInf.setDbtrAgt(DbtrAgt);
			}
		}
		if (null != docPmtInf.getDbtrAgtAcct()) {
			CashAccount16 DbtrAgtAcct = setDbtrCashAccnt16(docPmtInf.getDbtrAgtAcct(), tagSeqMap,
					PAIN001Constant.IBAN_222, PAIN001Constant.ID_224, PAIN001Constant.ISSR_228, PAIN001Constant.CD_226,
					PAIN001Constant.PRTRY_227, PAIN001Constant.CD_230, PAIN001Constant.PRTRY_231,
					PAIN001Constant.CCY_232, PAIN001Constant.NM_233);
			if (null != DbtrAgtAcct) {
				PmtInf.setDbtrAgtAcct(DbtrAgtAcct);
			}
		}
		if (null != docPmtInf.getUltmtDbtr()) {
			PartyIdentification32 UltmtDbtr = setPartyIdentificatio32(docPmtInf.getUltmtDbtr(), tagSeqMap);
			if (null != UltmtDbtr) {
				PmtInf.setUltmtDbtr(UltmtDbtr);
			}
		}
		if (null != docPmtInf.getChrgBr() && isTagPresent(tagSeqMap, PAIN001Constant.CHRGBR_256)) {
			PmtInf.setChrgBr(getChrgeBrType(tagSeqMap.get(PAIN001Constant.CHRGBR_256)));
		}
		if (null != docPmtInf.getChrgsAcct()) {
			CashAccount16 ChrgsAcct = setDbtrCashAccnt16(docPmtInf.getDbtrAcct(), tagSeqMap, PAIN001Constant.IBAN_262,
					PAIN001Constant.ID_266, PAIN001Constant.ISSR_274, PAIN001Constant.CD_270, PAIN001Constant.PRTRY_272,
					PAIN001Constant.CD_278, PAIN001Constant.PRTRY_280, PAIN001Constant.CCY_282, PAIN001Constant.NM_284);
			if (null != ChrgsAcct) {
				PmtInf.setChrgsAcct(ChrgsAcct);
			}

		}
		if (null != docPmtInf.getChrgsAcctAgt()) {
			BranchAndFinancialInstitutionIdentification4 ChrgsAcctAgt = setBrFiId4(docPmtInf.getChrgsAcctAgt(),
					tagSeqMap);
			if (null != ChrgsAcctAgt) {
				PmtInf.setChrgsAcctAgt(ChrgsAcctAgt);
			}
		}

		// PmtInfL.add(PmtInf);
		// cstmrCdtTrfInitn.setPmtInf(PmtInfL);
		return PmtInf;
	}

	/*
	 * "pmtId", "pmtTpInf", "amt", "xchgRateInf", "chrgBr", "chqInstr", "ultmtDbtr",
	 * "intrmyAgt1", "intrmyAgt1Acct", "intrmyAgt2", "intrmyAgt2Acct", "intrmyAgt3",
	 * "intrmyAgt3Acct", "cdtrAgt", "cdtrAgtAcct", "cdtr", "cdtrAcct", "ultmtCdtr",
	 * "instrForCdtrAgt", "instrForDbtrAgt", "purp", "rgltryRptg", "tax",
	 * "rltdRmtInf", "rmtInf"
	 */
	public CreditTransferTransactionInformation10 setCdtTrfTxInf(Document doc, PaymentInstructionInformation3 PmtInf,
			Map<Integer, String> tagSeqMap, Map<String, List<Map<Integer, String>>> rmttagSeqMapOnEndToEnd,
			Set<Integer> suprrestagIdSet, boolean isMultiAddressLine) {
		this.isMultiAddressLine = isMultiAddressLine;
		boolean flag = true;
		CreditTransferTransactionInformation10 CdtTrfTxInf = new CreditTransferTransactionInformation10();
		if (null != doc.getCstmrCdtTrfInitn() && null != doc.getCstmrCdtTrfInitn().getPmtInf()
				&& doc.getCstmrCdtTrfInitn().getPmtInf().size() > 0) {
			if (null != doc.getCstmrCdtTrfInitn().getPmtInf().get(0).getCdtTrfTxInf()
					&& doc.getCstmrCdtTrfInitn().getPmtInf().get(0).getCdtTrfTxInf().size() > 0) {
				CreditTransferTransactionInformation10 docCdtTrfInf = doc.getCstmrCdtTrfInitn().getPmtInf().get(0)
						.getCdtTrfTxInf().get(0);
				if (null != docCdtTrfInf.getPmtId()) {
					PaymentIdentification1 PmtId = setPmtId(docCdtTrfInf.getPmtId(), tagSeqMap);
					if (null != PmtId) {
						CdtTrfTxInf.setPmtId(PmtId);
					}
				}
				if (null != docCdtTrfInf.getPmtTpInf()) {
					PaymentTypeInformation19 PmtTpInf = setPmtTpInf(docCdtTrfInf.getPmtTpInf(), tagSeqMap,
							PAIN001Constant.INSTRPRTY_326, PAIN001Constant.CD_330, PAIN001Constant.PRTRY_332,
							PAIN001Constant.CD_336, PAIN001Constant.PRTRY_338, PAIN001Constant.CD_342,
							PAIN001Constant.PRTRY_344);
					if (null != PmtTpInf) {
						CdtTrfTxInf.setPmtTpInf(PmtTpInf);
					}
				}
				if (null != docCdtTrfInf.getAmt()) {
					AmountType3Choice Amt = setAmt(docCdtTrfInf.getAmt(), tagSeqMap,
							tagSeqMap.get(PAIN001Constant.INSTDAMT_35));
					if (null != Amt) {
						CdtTrfTxInf.setAmt(Amt);
					}
				}
				if (null != docCdtTrfInf.getXchgRateInf()) {
					ExchangeRateInformation1 XchgRateInf = setXchgRateInf(docCdtTrfInf.getXchgRateInf(), tagSeqMap);
					if (null != XchgRateInf) {
						CdtTrfTxInf.setXchgRateInf(XchgRateInf);
					}
				}
				if (null != docCdtTrfInf.getChrgBr() && isTagPresent(tagSeqMap, PAIN001Constant.CHRGBR_83)) {
					CdtTrfTxInf.setChrgBr(getChrgeBrType(tagSeqMap.get(PAIN001Constant.CHRGBR_83)));
				}
				if (null != docCdtTrfInf.getChqInstr()) {
					Cheque6 ChqInstr = setChqInstr(docCdtTrfInf.getChqInstr(), tagSeqMap);
					if (null != ChqInstr) {
						CdtTrfTxInf.setChqInstr(ChqInstr);
					}
				}
				if (null != docCdtTrfInf.getUltmtDbtr()) {
					PartyIdentification32 UltmtDbtr = setUltmtDbtrPartyIdentificatio32(docCdtTrfInf.getUltmtDbtr(),
							tagSeqMap);
					if (null != UltmtDbtr) {
						CdtTrfTxInf.setUltmtDbtr(UltmtDbtr);
					}
				}
				if (null != docCdtTrfInf.getIntrmyAgt1()) {
					BranchAndFinancialInstitutionIdentification4 IntrmyAgt1 = setIntrmyAgt1BrFiId4(
							docCdtTrfInf.getIntrmyAgt1(), tagSeqMap);
					if (null != IntrmyAgt1) {
						CdtTrfTxInf.setIntrmyAgt1(IntrmyAgt1);
					}
				}
				if (null != docCdtTrfInf.getIntrmyAgt1Acct()) {
					CashAccount16 IntrmyAgt1Acct = setDbtrCashAccnt16(docCdtTrfInf.getIntrmyAgt1Acct(), tagSeqMap,
							PAIN001Constant.IBAN_259, PAIN001Constant.ID_95, PAIN001Constant.CD_263,
							PAIN001Constant.PRTRY_265, PAIN001Constant.ISSR_267, PAIN001Constant.CD_271,
							PAIN001Constant.PRTRY_273, PAIN001Constant.CCY_275, PAIN001Constant.NM_277);
					if (null != IntrmyAgt1Acct) {
						CdtTrfTxInf.setIntrmyAgt1Acct(IntrmyAgt1Acct);
					}
				}
				if (null != docCdtTrfInf.getIntrmyAgt3()) {
					BranchAndFinancialInstitutionIdentification4 IntrmyAgt3 = setIntrmyAgt3BrFiId4(
							docCdtTrfInf.getIntrmyAgt3(), tagSeqMap);
					if (null != IntrmyAgt3) {
						CdtTrfTxInf.setIntrmyAgt3(IntrmyAgt3);
					}
				}
				if (null != docCdtTrfInf.getIntrmyAgt3Acct()) {
					CashAccount16 IntrmyAgt3Acct = setDbtrCashAccnt16(docCdtTrfInf.getIntrmyAgt1Acct(), tagSeqMap,
							PAIN001Constant.IBAN_319, PAIN001Constant.ID_323, PAIN001Constant.CD_327,
							PAIN001Constant.PRTRY_329, PAIN001Constant.ISSR_331, PAIN001Constant.CD_335,
							PAIN001Constant.PRTRY_337, PAIN001Constant.CCY_339, PAIN001Constant.NM_341);
					if (null != IntrmyAgt3Acct) {
						CdtTrfTxInf.setIntrmyAgt3Acct(IntrmyAgt3Acct);
					}

				}
				if (null != docCdtTrfInf.getCdtrAgt()) {
					BranchAndFinancialInstitutionIdentification4 CdtrAgt = setCdtrAgtBrFiId4(docCdtTrfInf.getCdtrAgt(),
							tagSeqMap);
					if (null != CdtrAgt) {
						CdtTrfTxInf.setCdtrAgt(CdtrAgt);
					}
				}
				if (null != docCdtTrfInf.getCdtrAgtAcct()) {
					CashAccount16 CdtrAgtAcct = setDbtrCashAccnt16(docCdtTrfInf.getCdtrAgtAcct(), tagSeqMap,
							PAIN001Constant.IBAN_365, PAIN001Constant.ID_369, PAIN001Constant.CD_373,
							PAIN001Constant.PRTRY_375, PAIN001Constant.ISSR_377, PAIN001Constant.CD_381,
							PAIN001Constant.PRTRY_383, PAIN001Constant.CCY_385, PAIN001Constant.NM_387);

					if (null != CdtrAgtAcct) {
						CdtTrfTxInf.setCdtrAgtAcct(CdtrAgtAcct);
					}
				}

				if (null != docCdtTrfInf.getCdtr()) {
					PartyIdentification32 Cdtr = setCdtrPartyIdentificatio32(docCdtTrfInf.getCdtr(), tagSeqMap);
					if (null != Cdtr) {
						CdtTrfTxInf.setCdtr(Cdtr);
					}
				}
				if (null != docCdtTrfInf.getCdtrAcct()) {
					CashAccount16 CdtrAcct = setDbtrCashAccnt16(docCdtTrfInf.getCdtrAcct(), tagSeqMap,
							PAIN001Constant.IBAN_405, PAIN001Constant.ID_45, PAIN001Constant.CD_409,
							PAIN001Constant.PRTRY_411, PAIN001Constant.ISSR_413, PAIN001Constant.CD_78,
							PAIN001Constant.PRTRY_117, PAIN001Constant.CCY_415, PAIN001Constant.NM_46);
					if (null != CdtrAcct) {
						CdtTrfTxInf.setCdtrAcct(CdtrAcct);
					}

				}
				if (null != docCdtTrfInf.getUltmtCdtr()) {
					PartyIdentification32 UltmtCdtr = setUltmtCdtrPartyIdentificatio32(docCdtTrfInf.getUltmtCdtr(),
							tagSeqMap);
					if (null != UltmtCdtr) {
						CdtTrfTxInf.setUltmtCdtr(UltmtCdtr);
					}
				}
				if (null != docCdtTrfInf.getInstrForCdtrAgt() && docCdtTrfInf.getInstrForCdtrAgt().size() > 0) {
					List<InstructionForCreditorAgent1> InstrForCdtrAgt = setInstrForCdtrAgtL(
							docCdtTrfInf.getInstrForCdtrAgt().get(0), tagSeqMap);
					if (null != InstrForCdtrAgt) {
						CdtTrfTxInf.setInstrForCdtrAgt(InstrForCdtrAgt);
					}
				}
				if (null != docCdtTrfInf.getInstrForCdtrAgt()
						&& isTagPresent(tagSeqMap, PAIN001Constant.INSTRFORDBTRAGT_114)) {
					CdtTrfTxInf.setInstrForDbtrAgt(tagSeqMap.get(PAIN001Constant.INSTRFORDBTRAGT_114));
				}
				if (null != docCdtTrfInf.getPurp()) {
					Purpose2Choice Purp = setPurp(docCdtTrfInf.getPurp(), tagSeqMap);
					if (null != Purp) {
						CdtTrfTxInf.setPurp(Purp);
					}
				}
				if (null != docCdtTrfInf.getRgltryRptg() && docCdtTrfInf.getRgltryRptg().size() > 0) {
					List<RegulatoryReporting3> RgltryRptg = setRgltryRptgL(docCdtTrfInf.getRgltryRptg().get(0),
							tagSeqMap);
					if (null != RgltryRptg) {
						CdtTrfTxInf.setRgltryRptg(RgltryRptg);
					}
				}
				if (null != docCdtTrfInf.getTax()) {
					TaxInformation3 Tax = setTax(docCdtTrfInf.getTax(), tagSeqMap);
					if (null != Tax) {
						CdtTrfTxInf.setTax(Tax);
					}
				}
				if (null != docCdtTrfInf.getRltdRmtInf() && docCdtTrfInf.getRltdRmtInf().size() > 0) {
					List<RemittanceLocation2> RltdRmtInf = setRltdRmtInfL(docCdtTrfInf.getRltdRmtInf().get(0),
							tagSeqMap);
					if (null != RltdRmtInf) {
						CdtTrfTxInf.setRltdRmtInf(RltdRmtInf);
					}
				}
				if (null != docCdtTrfInf.getRmtInf()) {
					String currency = tagSeqMap.get(PAIN001Constant.CCY_CD_869);
					RemittanceInformation5 RmtInf = setRmtInf(docCdtTrfInf.getRmtInf(), rmttagSeqMapOnEndToEnd,
							currency, suprrestagIdSet);
					if (null != RmtInf) {
						CdtTrfTxInf.setRmtInf(RmtInf);
					}
				}
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		return CdtTrfTxInf;
	}

	/*
	 * "ustrd", "strd"
	 */
	private RemittanceInformation5 setRmtInf(RemittanceInformation5 docRmtInf,
			Map<String, List<Map<Integer, String>>> tagSeqMapList, String currency, Set<Integer> suprrestagIdSet) {
		boolean flag = true;

		RemittanceInformation5 RmtInf = new RemittanceInformation5();

		List<StructuredRemittanceInformation7> StrdList = new ArrayList<>();
		List<String> UstrdList = new ArrayList<>();
		tagSeqMapList.entrySet().stream().forEach(invoiceNoList -> {
			String invoiceNo = invoiceNoList.getKey();
			invoiceNoList.getValue().stream().forEach(tagSeqMap -> {

				if (null != suprrestagIdSet) {
					suprrestagIdSet.stream().forEach(stag -> {
						logger.info("stag suppressed " + stag + " " + tagSeqMap.remove(stag));
					});
				}

				if (null != docRmtInf.getUstrd() && isTagPresent(tagSeqMap, PAIN001Constant.USTRD_53)) {
					UstrdList.add(tagSeqMap.get(PAIN001Constant.USTRD_53));
				}
				if (null != docRmtInf.getStrd() && docRmtInf.getStrd().size() > 0) {
					StrdList.add(setStrdL(docRmtInf.getStrd().get(0), tagSeqMap, currency));
				}
			});
		});
		/*
		 * tagSeqMapList.stream().forEach(tagSeqMap -> {
		 * 
		 * if (null != docRmtInf.getUstrd() && isTagPresent(tagSeqMap,
		 * PAIN001Constant.USTRD_53)) {
		 * UstrdList.add(tagSeqMap.get(PAIN001Constant.USTRD_53)); } if (null !=
		 * docRmtInf.getStrd() && docRmtInf.getStrd().size() > 0) {
		 * StrdList.add(setStrdL(docRmtInf.getStrd().get(0), tagSeqMap, currency)); }
		 * });
		 */
		if (!UstrdList.isEmpty()) {
			RmtInf.setUstrd(UstrdList);
			flag = false;
		}
		if (!StrdList.isEmpty()) {
			RmtInf.setStrd(StrdList);
			flag = false;
		}
		if (flag) {
			return null;
		}
		return RmtInf;
	}

	/*
	 * "rfrdDocInf", "rfrdDocAmt", "cdtrRefInf", "invcr", "invcee", "addtlRmtInf"
	 */
	private StructuredRemittanceInformation7 setStrdL(StructuredRemittanceInformation7 docStrRmtInf,
			Map<Integer, String> tagSeqMap, String currency) {
		boolean flag = true;
		StructuredRemittanceInformation7 strd = new StructuredRemittanceInformation7();
		if (null != docStrRmtInf.getRfrdDocInf() && docStrRmtInf.getRfrdDocInf().size() > 0) {
			List<ReferredDocumentInformation3> RfrdDocInf = setRfrdDocInfL(docStrRmtInf.getRfrdDocInf().get(0),
					tagSeqMap);
			if (null != RfrdDocInf) {
				strd.setRfrdDocInf(RfrdDocInf);
				flag = false;
			}
		}
		if (null != docStrRmtInf.getRfrdDocAmt()) {
			RemittanceAmount1 RfrdDocAmt = setRfrdDocAmt(docStrRmtInf.getRfrdDocAmt(), tagSeqMap, currency);
			if (null != RfrdDocAmt) {
				strd.setRfrdDocAmt(RfrdDocAmt);
				flag = false;
			}
		}
		if (null != docStrRmtInf.getCdtrRefInf()) {
			CreditorReferenceInformation2 CdtrRefInf = setCdtrRefInf(docStrRmtInf.getCdtrRefInf(), tagSeqMap);
			if (null != CdtrRefInf) {
				strd.setCdtrRefInf(CdtrRefInf);
				flag = false;
			}
		}
		if (null != docStrRmtInf.getInvcr()) {
			PartyIdentification32 Invcr = setInvcrPartyIdentificatio32(docStrRmtInf.getInvcr(), tagSeqMap);
			if (null != Invcr) {
				strd.setInvcr(Invcr);
				flag = false;
			}
		}
		if (null != docStrRmtInf.getInvcee()) {
			PartyIdentification32 Invcee = setInvceePartyIdentificatio32(docStrRmtInf.getInvcee(), tagSeqMap);
			if (null != Invcee) {
				strd.setInvcee(Invcee);
				flag = false;
			}
		}
		if (null != docStrRmtInf.getCdtrRefInf() && isTagPresent(tagSeqMap, PAIN001Constant.ADDTLRMTINF_72)) {
			List<String> addtlRmtInf = new ArrayList<>();
			addtlRmtInf.add(tagSeqMap.get(PAIN001Constant.ADDTLRMTINF_72));
			strd.setAddtlRmtInf(addtlRmtInf);
			flag = false;
		}
		if (flag) {
			return null;
		}
		return strd;
	}

	/*
	 * "tp", "nb", "rltdDt"
	 */
	private List<ReferredDocumentInformation3> setRfrdDocInfL(ReferredDocumentInformation3 docRfrDocInf,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		List<ReferredDocumentInformation3> rfrdDocInfL = new ArrayList<>();
		ReferredDocumentInformation3 rfrdDocInf = new ReferredDocumentInformation3();

		if (null != docRfrDocInf.getTp()) {
			ReferredDocumentType2 Tp = seTp(docRfrDocInf.getTp(), tagSeqMap);
			if (null != Tp) {
				rfrdDocInf.setTp(Tp);
				flag = false;
			}
		}
		if (null != docRfrDocInf.getNb() && isTagPresent(tagSeqMap, PAIN001Constant.NB_59)) {
			rfrdDocInf.setNb(tagSeqMap.get(PAIN001Constant.NB_59));
			flag = false;
		}
		if (null != docRfrDocInf.getRltdDt() && isTagPresent(tagSeqMap, PAIN001Constant.RLTDDT_60)) {
			rfrdDocInf.setRltdDt(getLocalDate(tagSeqMap.get(PAIN001Constant.RLTDDT_60)));
			flag = false;
		}
		rfrdDocInfL.add(rfrdDocInf);
		if (flag) {
			return null;
		}
		return rfrdDocInfL;
	}

	/*
	 * "cdOrPrtry", "issr"
	 */
	private ReferredDocumentType2 seTp(ReferredDocumentType2 docRfDocTp2, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		ReferredDocumentType2 tp = new ReferredDocumentType2();
		if (null != docRfDocTp2.getCdOrPrtry()) {
			ReferredDocumentType1Choice CdOrPrtry = setCdOrPrtry(docRfDocTp2.getCdOrPrtry(), tagSeqMap);
			if (null != CdOrPrtry) {
				tp.setCdOrPrtry(CdOrPrtry);
				flag = false;
			}
		}
		if (null != docRfDocTp2.getIssr() && isTagPresent(tagSeqMap, PAIN001Constant.ISSR_490)) {
			tp.setIssr(tagSeqMap.get(PAIN001Constant.ISSR_490));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return tp;
	}

	/*
	 * "cd", "prtry"
	 */
	private ReferredDocumentType1Choice setCdOrPrtry(ReferredDocumentType1Choice docRfDocTp1,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		ReferredDocumentType1Choice cdOrPrtry = new ReferredDocumentType1Choice();
		if (null != docRfDocTp1.getCd() && isTagPresent(tagSeqMap, PAIN001Constant.CD_58)) {
			cdOrPrtry.setCd(getDocumentType5Code(tagSeqMap.get(PAIN001Constant.CD_58)));
			flag = false;
		}
		if (null != docRfDocTp1.getPrtry() && isTagPresent(tagSeqMap, PAIN001Constant.PRTRY_489)) {
			cdOrPrtry.setPrtry(tagSeqMap.get(PAIN001Constant.PRTRY_489));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return cdOrPrtry;
	}

	/*
	 * "tp", "ref"
	 */
	private CreditorReferenceInformation2 setCdtrRefInf(CreditorReferenceInformation2 docRefInf,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		CreditorReferenceInformation2 cdtrRefInf = new CreditorReferenceInformation2();

		if (null != docRefInf.getTp()) {
			CreditorReferenceType2 Tp = setTp(docRefInf.getTp(), tagSeqMap);
			if (null != Tp) {
				cdtrRefInf.setTp(Tp);
				flag = false;
			}
		}
		if (null != docRefInf.getRef() && isTagPresent(tagSeqMap, PAIN001Constant.REF_71)) {
			cdtrRefInf.setRef(tagSeqMap.get(PAIN001Constant.REF_71));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return cdtrRefInf;
	}

	/*
	 * "cdOrPrtry", "issr"
	 */
	private CreditorReferenceType2 setTp(CreditorReferenceType2 docCrRef2, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		CreditorReferenceType2 tp = new CreditorReferenceType2();
		if (null != docCrRef2.getCdOrPrtry()) {
			CreditorReferenceType1Choice CdOrPrtry = setCdOrPrtry(docCrRef2.getCdOrPrtry(), tagSeqMap);
			if (null != CdOrPrtry) {
				tp.setCdOrPrtry(CdOrPrtry);
				flag = false;
			}
		}
		if (null != docCrRef2.getIssr() && isTagPresent(tagSeqMap, PAIN001Constant.ISSR_497)) {
			tp.setIssr(tagSeqMap.get(PAIN001Constant.ISSR_497));
			flag = false;
		}
		if (flag) {
			return null;
		}

		return tp;
	}

	/*
	 * "cd", "prtry"
	 */
	private CreditorReferenceType1Choice setCdOrPrtry(CreditorReferenceType1Choice docCrRef,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		CreditorReferenceType1Choice cdOrPrtry = new CreditorReferenceType1Choice();
		if (null != docCrRef.getCd() && isTagPresent(tagSeqMap, PAIN001Constant.CD_70)) {
			cdOrPrtry.setCd(getDocumentType3Code(tagSeqMap.get(PAIN001Constant.CD_70)));
			flag = false;
		}
		if (null != docCrRef.getPrtry() && isTagPresent(tagSeqMap, PAIN001Constant.PRTRY_496)) {
			cdOrPrtry.setPrtry(tagSeqMap.get(PAIN001Constant.PRTRY_496));
			flag = false;
		}

		if (flag) {
			return null;
		}
		return cdOrPrtry;
	}

	/*
	 * "duePyblAmt", "dscntApldAmt", "cdtNoteAmt", "taxAmt", "adjstmntAmtAndRsn",
	 * "rmtdAmt"
	 */
	private RemittanceAmount1 setRfrdDocAmt(RemittanceAmount1 docRmtAmt, Map<Integer, String> tagSeqMap,
			String currency) {
		boolean flag = true;
		RemittanceAmount1 RfrdDocAmt = new RemittanceAmount1();
		if (null != docRmtAmt.getDuePyblAmt() && isTagPresent(tagSeqMap, PAIN001Constant.DUEPYBLAMT_62)) {
			RfrdDocAmt.setDuePyblAmt(setActiveOrHstCurrAmt(currency, tagSeqMap.get(PAIN001Constant.DUEPYBLAMT_62)));
			flag = false;
		}
		if (null != docRmtAmt.getDscntApldAmt() && isTagPresent(tagSeqMap, PAIN001Constant.DSCNTAPLDAMT_63)) {
			RfrdDocAmt.setDscntApldAmt(setActiveOrHstCurrAmt(currency, tagSeqMap.get(PAIN001Constant.DSCNTAPLDAMT_63)));
			flag = false;
		}
		if (null != docRmtAmt.getCdtNoteAmt() && isTagPresent(tagSeqMap, PAIN001Constant.CDTNOTEAMT_64)) {
			RfrdDocAmt.setCdtNoteAmt(setActiveOrHstCurrAmt(currency, tagSeqMap.get(PAIN001Constant.CDTNOTEAMT_64)));
			flag = false;
		}
		if (null != docRmtAmt.getTaxAmt() && isTagPresent(tagSeqMap, PAIN001Constant.TAXAMT_65)) {
			RfrdDocAmt.setTaxAmt(setActiveOrHstCurrAmt(currency, tagSeqMap.get(PAIN001Constant.TAXAMT_65)));
			flag = false;
		}
		if (null != docRmtAmt.getAdjstmntAmtAndRsn() && docRmtAmt.getAdjstmntAmtAndRsn().size() > 0) {
			List<DocumentAdjustment1> AdjstmntAmtAndRsn = setAdjstmntAmtAndRsnL(docRmtAmt.getAdjstmntAmtAndRsn().get(0),
					tagSeqMap, currency);
			if (null != AdjstmntAmtAndRsn) {
				RfrdDocAmt.setAdjstmntAmtAndRsn(AdjstmntAmtAndRsn);
				flag = false;
			}
		}
		if (null != docRmtAmt.getRmtdAmt() && isTagPresent(tagSeqMap, PAIN001Constant.RMTDAMT_66)) {
			RfrdDocAmt.setRmtdAmt(setActiveOrHstCurrAmt(currency, tagSeqMap.get(PAIN001Constant.RMTDAMT_66)));
			flag = false;
		}

		if (flag) {
			return null;
		}
		return RfrdDocAmt;
	}

	/*
	 * "amt", "cdtDbtInd", "rsn", "addtlInf"
	 */
	private List<DocumentAdjustment1> setAdjstmntAmtAndRsnL(DocumentAdjustment1 docAdj1, Map<Integer, String> tagSeqMap,
			String currency) {
		boolean flag = true;
		List<DocumentAdjustment1> adjstmntAmtAndRsnL = new ArrayList<>();
		DocumentAdjustment1 adjstmntAmtAndRsn = new DocumentAdjustment1();
		if (null != docAdj1.getAmt() && isTagPresent(tagSeqMap, PAIN001Constant.AMT_492)) {
			adjstmntAmtAndRsn.setAmt(setActiveOrHstCurrAmt(currency, tagSeqMap.get(PAIN001Constant.AMT_492)));
			flag = false;
		}
		if (null != docAdj1.getCdtDbtInd() && isTagPresent(tagSeqMap, PAIN001Constant.CDTDBTIND_493)) {
			adjstmntAmtAndRsn.setCdtDbtInd(getCreditDebitCode(tagSeqMap.get(PAIN001Constant.CDTDBTIND_493)));
			flag = false;
		}
		if (null != docAdj1.getRsn() && isTagPresent(tagSeqMap, PAIN001Constant.RSN_494)) {
			adjstmntAmtAndRsn.setRsn(tagSeqMap.get(PAIN001Constant.RSN_494));
			flag = false;
		}
		if (null != docAdj1.getAddtlInf() && isTagPresent(tagSeqMap, PAIN001Constant.ADDTLINF_495)) {
			adjstmntAmtAndRsn.setAddtlInf(tagSeqMap.get(PAIN001Constant.ADDTLINF_495));
			flag = false;
		}

		adjstmntAmtAndRsnL.add(adjstmntAmtAndRsn);
		if (flag) {
			return null;
		}
		return adjstmntAmtAndRsnL;
	}

	/*
	 * "rmtId", "rmtLctnMtd", "rmtLctnElctrncAdr", "rmtLctnPstlAdr"
	 */
	private List<RemittanceLocation2> setRltdRmtInfL(RemittanceLocation2 docRegletryRp,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		List<RemittanceLocation2> RltdRmtInfL = new ArrayList<>(1);
		RemittanceLocation2 RltdRmtInf = new RemittanceLocation2();
		if (null != docRegletryRp.getRmtId() && isTagPresent(tagSeqMap, PAIN001Constant.RMTID_475)) {
			RltdRmtInf.setRmtId(tagSeqMap.get(PAIN001Constant.RMTID_475));
			flag = false;
		}
		if (null != docRegletryRp.getRmtLctnMtd() && isTagPresent(tagSeqMap, PAIN001Constant.RMTLCTNMTD_144)) {
			RltdRmtInf.setRmtLctnMtd(getRemittanceLocationMethod2Code(tagSeqMap.get(PAIN001Constant.RMTLCTNMTD_144)));
			flag = false;
		}
		if (null != docRegletryRp.getRmtLctnElctrncAdr()
				&& isTagPresent(tagSeqMap, PAIN001Constant.RMTLCTNELCTRNCADR_145)) {
			RltdRmtInf.setRmtLctnElctrncAdr(tagSeqMap.get(PAIN001Constant.RMTLCTNELCTRNCADR_145));
			flag = false;
		}
		if (null != docRegletryRp.getRmtLctnPstlAdr()) {
			NameAndAddress10 RmtLctnPstlAdr = setNameAndAddr10(docRegletryRp.getRmtLctnPstlAdr(), tagSeqMap);
			if (null != RmtLctnPstlAdr) {
				RltdRmtInf.setRmtLctnPstlAdr(RmtLctnPstlAdr);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		RltdRmtInfL.add(RltdRmtInf);
		return RltdRmtInfL;

	}

	/*
	 * "cdtr", "dbtr", "admstnZn", "refNb", "mtd", "ttlTaxblBaseAmt", "ttlTaxAmt",
	 * "dt", "seqNb", "rcrd"
	 */
	private TaxInformation3 setTax(TaxInformation3 docTxInf, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		TaxInformation3 tax = new TaxInformation3();

		if (null != docTxInf.getCdtr()) {
			TaxParty1 Cdtr = setCdtr(docTxInf.getCdtr(), tagSeqMap);
			if (null != Cdtr) {
				tax.setCdtr(Cdtr);
				flag = false;
			}
		}
		if (null != docTxInf.getDbtr()) {
			TaxParty2 Dbtr = setDbtr(docTxInf.getDbtr(), tagSeqMap);
			if (null != Dbtr) {
				tax.setDbtr(Dbtr);
				flag = false;
			}
		}
		if (null != docTxInf.getAdmstnZn() && isTagPresent(tagSeqMap, PAIN001Constant.ADMSTNZN_458)) {
			tax.setAdmstnZn(tagSeqMap.get(PAIN001Constant.ADMSTNZN_458));
			flag = false;
		}
		if (null != docTxInf.getRefNb() && isTagPresent(tagSeqMap, PAIN001Constant.REFNB_459)) {
			tax.setRefNb(tagSeqMap.get(PAIN001Constant.REFNB_459));
			flag = false;
		}
		if (null != docTxInf.getMtd() && isTagPresent(tagSeqMap, PAIN001Constant.MTD_460)) {
			tax.setMtd(tagSeqMap.get(PAIN001Constant.MTD_460));
			flag = false;
		}
		if (null != docTxInf.getTtlTaxblBaseAmt() && isTagPresent(tagSeqMap, PAIN001Constant.TTLTAXBLBASEAMT_461)) {
			tax.setTtlTaxblBaseAmt(setActiveOrHstCurrAmt(tagSeqMap.get(PAIN001Constant.CCY_CD_869),
					tagSeqMap.get(PAIN001Constant.TTLTAXBLBASEAMT_461)));
			flag = false;
		}
		if (null != docTxInf.getTtlTaxAmt() && isTagPresent(tagSeqMap, PAIN001Constant.TTLTAXAMT_462)) {
			tax.setTtlTaxAmt(setActiveOrHstCurrAmt(tagSeqMap.get(PAIN001Constant.CCY_CD_869),
					tagSeqMap.get(PAIN001Constant.TTLTAXAMT_462)));
			flag = false;
		}
		if (null != docTxInf.getDt() && isTagPresent(tagSeqMap, PAIN001Constant.DT_127)) {
			tax.setDt(getLocalDate(tagSeqMap.get(PAIN001Constant.DT_127)));
			flag = false;
		}
		if (null != docTxInf.getSeqNb() && isTagPresent(tagSeqMap, PAIN001Constant.SEQNB_463)) {
			tax.setSeqNb(new BigDecimal(tagSeqMap.get(PAIN001Constant.SEQNB_463)));
			flag = false;
		}
		if (null != docTxInf.getRcrd() && docTxInf.getRcrd().size() > 0) {
			List<TaxRecord1> Rcrd = setRcrdL(docTxInf.getRcrd().get(0), tagSeqMap);
			if (null != Rcrd) {
				tax.setRcrd(Rcrd);
				flag = false;
			}
		}

		if (flag) {
			return null;
		}
		return tax;
	}

	/*
	 * "tp", "ctgy", "ctgyDtls", "dbtrSts", "certId", "frmsCd", "prd", "taxAmt",
	 * "addtlInf"
	 */
	private List<TaxRecord1> setRcrdL(TaxRecord1 docTaxRd1, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		List<TaxRecord1> rcrdL = new ArrayList<>(1);
		TaxRecord1 rcrd = new TaxRecord1();
		if (null != docTaxRd1.getTp() && isTagPresent(tagSeqMap, PAIN001Constant.TP_464)) {
			rcrd.setTp(tagSeqMap.get(PAIN001Constant.TP_464));
			flag = false;
		}
		if (null != docTaxRd1.getCtgy() && isTagPresent(tagSeqMap, PAIN001Constant.CTGY_465)) {
			rcrd.setCtgy(tagSeqMap.get(PAIN001Constant.CTGY_465));
			flag = false;
		}
		if (null != docTaxRd1.getCtgyDtls() && isTagPresent(tagSeqMap, PAIN001Constant.CTGYDTLS_126)) {
			rcrd.setCtgyDtls(tagSeqMap.get(PAIN001Constant.CTGYDTLS_126));
			flag = false;
		}
		if (null != docTaxRd1.getDbtrSts() && isTagPresent(tagSeqMap, PAIN001Constant.DBTRSTS_466)) {
			rcrd.setDbtrSts(tagSeqMap.get(PAIN001Constant.DBTRSTS_466));
			flag = false;
		}
		if (null != docTaxRd1.getCertId() && isTagPresent(tagSeqMap, PAIN001Constant.CERTID_467)) {
			rcrd.setCertId(tagSeqMap.get(PAIN001Constant.CERTID_467));
			flag = false;
		}
		if (null != docTaxRd1.getFrmsCd() && isTagPresent(tagSeqMap, PAIN001Constant.FRMSCD_468)) {
			rcrd.setFrmsCd(tagSeqMap.get(PAIN001Constant.FRMSCD_468));
			flag = false;
		}
		if (null != docTaxRd1.getPrd()) {
			rcrd.setPrd(setRcrdTaxPeriod1(docTaxRd1.getPrd(), tagSeqMap, PAIN001Constant.YR_761, PAIN001Constant.TP_762,
					PAIN001Constant.FRDT_829, PAIN001Constant.TODT_830));
			flag = false;
		}
		if (null != docTaxRd1.getTaxAmt()) {
			rcrd.setTaxAmt(setTaxAmt(docTaxRd1.getTaxAmt(), tagSeqMap));
			flag = false;
		}
		if (null != docTaxRd1.getAddtlInf() && isTagPresent(tagSeqMap, PAIN001Constant.ADDTLINF_474)) {
			rcrd.setAddtlInf(tagSeqMap.get(PAIN001Constant.ADDTLINF_474));
			flag = false;
		}
		rcrdL.add(rcrd);

		if (flag) {
			return null;
		}
		return rcrdL;
	}

	/*
	 * "rate", "taxblBaseAmt", "ttlAmt", "dtls"
	 */
	private TaxAmount1 setTaxAmt(TaxAmount1 docTaxAmt1, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		TaxAmount1 taxAmt = new TaxAmount1();
		if (null != docTaxAmt1.getRate() && isTagPresent(tagSeqMap, PAIN001Constant.RATE_470)) {
			taxAmt.setRate(new BigDecimal(tagSeqMap.get(PAIN001Constant.RATE_470)));
			flag = false;
		}
		if (null != docTaxAmt1.getRate() && isTagPresent(tagSeqMap, PAIN001Constant.TAXBLBASEAMT_471)) {
			taxAmt.setTaxblBaseAmt(setActiveOrHstCurrAmt(tagSeqMap.get(PAIN001Constant.CCY_CD_869),
					tagSeqMap.get(PAIN001Constant.TAXBLBASEAMT_471)));
			flag = false;
		}
		if (null != docTaxAmt1.getRate() && isTagPresent(tagSeqMap, PAIN001Constant.TTLAMT_472)) {
			taxAmt.setTtlAmt(setActiveOrHstCurrAmt(tagSeqMap.get(PAIN001Constant.CCY_CD_869),
					tagSeqMap.get(PAIN001Constant.TTLAMT_472)));
			flag = false;
		}
		if (null != docTaxAmt1.getDtls() && docTaxAmt1.getDtls().size() > 0) {
			List<TaxRecordDetails1> Dtls = setDTLSL(docTaxAmt1.getDtls().get(0), tagSeqMap);
			if (null != Dtls) {
				taxAmt.setDtls(Dtls);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		return taxAmt;
	}

	/*
	 * "prd", "amt"
	 */
	private List<TaxRecordDetails1> setDTLSL(TaxRecordDetails1 docTaxRcdDtls, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		List<TaxRecordDetails1> DTLSL = new ArrayList<>(1);
		TaxRecordDetails1 DTLS = new TaxRecordDetails1();
		if (null != docTaxRcdDtls.getPrd()) {
			TaxPeriod1 Prd = setRcrdTaxPeriod1(docTaxRcdDtls.getPrd(), tagSeqMap, PAIN001Constant.YR_764,
					PAIN001Constant.TP_765, PAIN001Constant.FRDT_827, PAIN001Constant.TODT_828);
			if (null != Prd) {
				DTLS.setPrd(Prd);
				flag = false;
			}
		}
		if (null != docTaxRcdDtls.getAmt() && isTagPresent(tagSeqMap, PAIN001Constant.AMT_130)) {
			DTLS.setAmt(setActiveOrHstCurrAmt(tagSeqMap.get(PAIN001Constant.CCY_CD_869),
					tagSeqMap.get(PAIN001Constant.AMT_130)));
			flag = false;
		}
		DTLSL.add(DTLS);
		if (flag) {
			return null;
		}
		return DTLSL;
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

	private DatePeriodDetails setRcrdTaxPersionFrToDt(DatePeriodDetails docDtPr, Map<Integer, String> tagSeqMap,
			Integer frdt, Integer todt) {
		boolean flag = true;
		DatePeriodDetails DatePeriodDetails = new DatePeriodDetails();
		if (null != docDtPr.getFrDt() && isTagPresent(tagSeqMap, frdt)) {
			DatePeriodDetails.setFrDt(getLocalDate(tagSeqMap.get(frdt)));
			flag = false;
		}
		if (null != docDtPr.getToDt() && isTagPresent(tagSeqMap, todt)) {
			DatePeriodDetails.setToDt(getLocalDate(tagSeqMap.get(todt)));
			flag = false;
		}

		if (flag) {
			return null;
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
	private TaxPeriod1 setRcrdTaxPeriod1(TaxPeriod1 docTxPr1, Map<Integer, String> tagSeqMap, Integer yr, Integer tp,
			Integer frdt, Integer todt) {
		boolean flag = true;
		TaxPeriod1 prd = new TaxPeriod1();

		if (null != docTxPr1.getYr() && isTagPresent(tagSeqMap, yr)) {
			prd.setYr(getLocalDate(tagSeqMap.get(yr)));
			flag = false;
		}
		if (null != docTxPr1.getTp() && isTagPresent(tagSeqMap, tp)) {
			prd.setTp(getTaxRecordPeriod1Code(tagSeqMap.get(tp)));
			flag = false;
		}
		if (null != docTxPr1.getFrToDt()) {
			DatePeriodDetails FrToDt = setRcrdTaxPersionFrToDt(docTxPr1.getFrToDt(), tagSeqMap, frdt, todt);
			if (null != FrToDt) {
				prd.setFrToDt(FrToDt);
				flag = false;
			}
		}

		if (flag) {
			return null;
		}
		return prd;
	}

	/*
	 * "taxId", "regnId", "taxTp", "authstn"
	 */
	private TaxParty2 setDbtr(TaxParty2 docTaxPrty2, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		TaxParty2 dbtr = new TaxParty2();
		if (null != docTaxPrty2.getTaxId() && isTagPresent(tagSeqMap, PAIN001Constant.TAXID_124)) {
			dbtr.setTaxId(tagSeqMap.get(PAIN001Constant.TAXID_124));
			flag = false;
		}
		if (null != docTaxPrty2.getRegnId() && isTagPresent(tagSeqMap, PAIN001Constant.REGNID_449)) {
			dbtr.setRegnId(tagSeqMap.get(PAIN001Constant.REGNID_449));
			flag = false;
		}
		if (null != docTaxPrty2.getTaxTp() && isTagPresent(tagSeqMap, PAIN001Constant.TAXTP_451)) {
			dbtr.setTaxTp(tagSeqMap.get(PAIN001Constant.TAXTP_451));
			flag = false;
		}
		if (null != docTaxPrty2.getAuthstn()) {
			TaxAuthorisation1 Authstn = setAuthstn(docTaxPrty2.getAuthstn(), tagSeqMap);
			if (null != Authstn) {
				dbtr.setAuthstn(Authstn);
				flag = false;
			}
		}

		if (flag) {
			return null;
		}
		return dbtr;
	}

	/*
	 * "titl", "nm"
	 */
	private TaxAuthorisation1 setAuthstn(TaxAuthorisation1 docTaxAuth1, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		TaxAuthorisation1 authstn = new TaxAuthorisation1();
		if (null != docTaxAuth1.getTitl() && isTagPresent(tagSeqMap, PAIN001Constant.TITL_455)) {
			authstn.setTitl(tagSeqMap.get(PAIN001Constant.TITL_455));
			flag = false;
		}
		if (null != docTaxAuth1.getNm() && isTagPresent(tagSeqMap, PAIN001Constant.NM_457)) {
			authstn.setNm(tagSeqMap.get(PAIN001Constant.NM_457));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return authstn;
	}

	/*
	 * "taxId", "regnId", "taxTp"
	 */
	private TaxParty1 setCdtr(TaxParty1 docTaxPrty1, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		TaxParty1 cdtr = new TaxParty1();
		if (null != docTaxPrty1.getTaxId() && isTagPresent(tagSeqMap, PAIN001Constant.TAXID_445)) {
			cdtr.setTaxId(tagSeqMap.get(PAIN001Constant.TAXID_445));
			flag = false;
		}
		if (null != docTaxPrty1.getRegnId() && isTagPresent(tagSeqMap, PAIN001Constant.REGNID_447)) {
			cdtr.setRegnId(tagSeqMap.get(PAIN001Constant.REGNID_447));
			flag = false;
		}
		if (null != docTaxPrty1.getTaxTp() && isTagPresent(tagSeqMap, PAIN001Constant.TAXTP_122)) {
			cdtr.setTaxTp(tagSeqMap.get(PAIN001Constant.TAXTP_122));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return cdtr;
	}

	/*
	 * "dbtCdtRptgInd", "authrty", "dtls"
	 */
	private List<RegulatoryReporting3> setRgltryRptgL(RegulatoryReporting3 docRegletryRp,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		List<RegulatoryReporting3> RgltryRptgL = new ArrayList<>(1);
		RegulatoryReporting3 RgltryRptg = new RegulatoryReporting3();

		if (null != docRegletryRp.getDbtCdtRptgInd() && isTagPresent(tagSeqMap, PAIN001Constant.DBTCDTRPTGIND_654)) {
			RgltryRptg.setDbtCdtRptgInd(
					getRegulatoryReportingType1Code(tagSeqMap.get(PAIN001Constant.DBTCDTRPTGIND_654)));
			flag = false;
		}
		if (null != docRegletryRp.getAuthrty()) {
			RegulatoryAuthority2 Authrty = setAuthrty(docRegletryRp.getAuthrty(), tagSeqMap);
			if (null != Authrty) {
				RgltryRptg.setAuthrty(Authrty);
				flag = false;
			}
		}
		if (null != docRegletryRp.getDtls() && docRegletryRp.getDtls().size() > 0) {
			List<StructuredRegulatoryReporting3> Dtls = setRgDTLSL(docRegletryRp.getDtls().get(0), tagSeqMap);
			if (null != Dtls) {
				RgltryRptg.setDtls(Dtls);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		RgltryRptgL.add(RgltryRptg);
		return RgltryRptgL;
	}

	/*
	 * "tp", "dt", "ctry", "cd", "amt", "inf"
	 */
	private List<StructuredRegulatoryReporting3> setRgDTLSL(StructuredRegulatoryReporting3 docStrRgRp3,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		List<StructuredRegulatoryReporting3> DTLSL = new ArrayList<>(1);
		StructuredRegulatoryReporting3 DTLS = new StructuredRegulatoryReporting3();

		if (null != docStrRgRp3.getTp() && isTagPresent(tagSeqMap, PAIN001Constant.TP_659)) {
			DTLS.setTp(tagSeqMap.get(PAIN001Constant.TP_659));
			flag = false;
		}
		if (null != docStrRgRp3.getDt() && isTagPresent(tagSeqMap, PAIN001Constant.DT_660)) {
			DTLS.setDt(getLocalDate(tagSeqMap.get(PAIN001Constant.DT_660)));
			flag = false;
		}
		if (null != docStrRgRp3.getCtry() && isTagPresent(tagSeqMap, PAIN001Constant.CTRY_661)) {
			DTLS.setCtry(tagSeqMap.get(PAIN001Constant.CTRY_661));
			flag = false;
		}
		if (null != docStrRgRp3.getCd() && isTagPresent(tagSeqMap, PAIN001Constant.CD_662)) {
			DTLS.setCd(tagSeqMap.get(PAIN001Constant.CD_662));
			flag = false;
		}

		if (null != docStrRgRp3.getAmt() && isTagPresent(tagSeqMap, PAIN001Constant.AMT_663)) {
			DTLS.setAmt(setActiveOrHstCurrAmt(tagSeqMap.get(PAIN001Constant.CCY_CD_869),
					tagSeqMap.get(PAIN001Constant.AMT_663)));
			flag = false;
		}
		if (null != docStrRgRp3.getInf() && docStrRgRp3.getInf().size() > 0
				&& isTagPresent(tagSeqMap, PAIN001Constant.INF_664)) {
			List<String> inf = new ArrayList<>(1);
			inf.add(tagSeqMap.get(PAIN001Constant.INF_664));
			DTLS.setInf(inf);
			flag = false;
		}
		DTLSL.add(DTLS);
		if (flag) {
			return null;
		}
		return DTLSL;
	}

	/*
	 * "nm", "ctry"
	 */
	private RegulatoryAuthority2 setAuthrty(RegulatoryAuthority2 docRgAu2, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		RegulatoryAuthority2 Authrty = new RegulatoryAuthority2();

		if (null != docRgAu2.getNm() && isTagPresent(tagSeqMap, PAIN001Constant.NM_657)) {
			Authrty.setNm(tagSeqMap.get(PAIN001Constant.NM_657));
			flag = false;
		}
		if (null != docRgAu2.getCtry() && isTagPresent(tagSeqMap, PAIN001Constant.CTRY_658)) {
			Authrty.setCtry(tagSeqMap.get(PAIN001Constant.CTRY_658));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return Authrty;
	}

	/*
	 * "cd", "prtry"
	 */
	private Purpose2Choice setPurp(Purpose2Choice docPurp, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		Purpose2Choice purp = new Purpose2Choice();
		if (null != docPurp.getCd() && isTagPresent(tagSeqMap, PAIN001Constant.CD_443)) {
			purp.setCd(tagSeqMap.get(PAIN001Constant.CD_443));
			flag = false;
		}
		if (null != docPurp.getPrtry() && isTagPresent(tagSeqMap, PAIN001Constant.PRTRY_142)) {
			purp.setPrtry(tagSeqMap.get(PAIN001Constant.PRTRY_142));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return purp;
	}

	/*
	 * "cd", "instrInf"
	 */
	private List<InstructionForCreditorAgent1> setInstrForCdtrAgtL(InstructionForCreditorAgent1 docInsForCrdAg,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		List<InstructionForCreditorAgent1> InstrForCdtrAgtL = new ArrayList<>(1);
		InstructionForCreditorAgent1 InstrForCdtrAgt = new InstructionForCreditorAgent1();
		if (null != docInsForCrdAg.getCd() && isTagPresent(tagSeqMap, PAIN001Constant.CD_118)) {
			InstrForCdtrAgt.setCd(getInstruction3Code(tagSeqMap.get(PAIN001Constant.CD_118)));
			flag = false;
		}
		if (null != docInsForCrdAg.getInstrInf() && isTagPresent(tagSeqMap, PAIN001Constant.INSTRINF_113)) {
			InstrForCdtrAgt.setInstrInf(tagSeqMap.get(PAIN001Constant.INSTRINF_113));
			flag = false;
		}
		InstrForCdtrAgtL.add(InstrForCdtrAgt);
		if (flag) {
			return null;
		}
		return InstrForCdtrAgtL;
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

	private CashAccount16 setDbtrCashAccnt16(CashAccount16 docCashAcnt, Map<Integer, String> tagSeqMap, Integer iban,

			Integer id, Integer issr, Integer cd, Integer prty, Integer tpcd, Integer tpprty, Integer ccy, Integer nm) {
		boolean flag = true;
		CashAccount16 IntrmyAgt1Acct = new CashAccount16();
		if (null != docCashAcnt) {
			if (null != docCashAcnt.getId()) {
				AccountIdentification4Choice Id = setDbtrId(docCashAcnt.getId(), tagSeqMap, iban, id, issr, cd, prty);
				if (null != Id) {
					IntrmyAgt1Acct.setId(Id);
					flag = false;
				}
			}

			if (null != docCashAcnt.getTp()) {
				CashAccountType2 Tp = setTp(docCashAcnt.getTp(), tagSeqMap, tpcd, tpprty);
				if (null != Tp) {
					IntrmyAgt1Acct.setTp(Tp);
					flag = false;
				}
			}
			if (null != docCashAcnt.getCcy() && isTagPresent(tagSeqMap, ccy)) {
				IntrmyAgt1Acct.setCcy(tagSeqMap.get(ccy));
				flag = false;
			}
			if (null != docCashAcnt.getNm() && isTagPresent(tagSeqMap, nm)) {
				IntrmyAgt1Acct.setNm(tagSeqMap.get(nm));
				flag = false;
			}
		}
		if (flag) {
			return null;
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
	private CashAccountType2 setTp(CashAccountType2 docCashAcn, Map<Integer, String> tagSeqMap, Integer cd,
			Integer prty) {
		boolean flag = true;
		CashAccountType2 Tp = new CashAccountType2();
		if (null != docCashAcn.getCd() && isTagPresent(tagSeqMap, cd)) {
			Tp.setCd(getCashAccntTypeCode(tagSeqMap.get(cd)));
			flag = false;
		}
		if (null != docCashAcn.getPrtry() && isTagPresent(tagSeqMap, prty)) {
			Tp.setPrtry(tagSeqMap.get(prty));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return Tp;
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

	private AccountIdentification4Choice setDbtrId(AccountIdentification4Choice docAcntIdn,
			Map<Integer, String> tagSeqMap, Integer iban, Integer id, Integer issr, Integer cd, Integer prty) {
		boolean flag = true;
		AccountIdentification4Choice Id = new AccountIdentification4Choice();

		if (null != docAcntIdn.getIBAN() && isTagPresent(tagSeqMap, iban)) {
			Id.setIBAN(tagSeqMap.get(iban));
			flag = false;
		}
		if (null != docAcntIdn.getOthr()) {
			GenericAccountIdentification1 Othr = setDbtrOthr(docAcntIdn.getOthr(), tagSeqMap, id, issr, cd, prty);
			if (null != Othr) {
				Id.setOthr(Othr);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		return Id;
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

	private GenericAccountIdentification1 setDbtrOthr(GenericAccountIdentification1 docGnAcntId,
			Map<Integer, String> tagSeqMap, Integer id, Integer issr, Integer cd, Integer prty) {
		boolean flag = true;
		GenericAccountIdentification1 othr = new GenericAccountIdentification1();

		if (null != docGnAcntId.getId() && isTagPresent(tagSeqMap, id)) {
			othr.setId(tagSeqMap.get(id));
			flag = false;
		}
		if (null != docGnAcntId.getIssr() && isTagPresent(tagSeqMap, issr)) {
			othr.setIssr(tagSeqMap.get(issr));
			flag = false;
		}
		if (null != docGnAcntId.getSchmeNm()) {
			AccountSchemeName1Choice SchmeNm = setDbtrOthrSchmeNm(docGnAcntId.getSchmeNm(), tagSeqMap, cd, prty);
			if (null != SchmeNm) {
				othr.setSchmeNm(SchmeNm);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		return othr;
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
	private AccountSchemeName1Choice setDbtrOthrSchmeNm(AccountSchemeName1Choice docAcScmNm,
			Map<Integer, String> tagSeqMap, Integer cd, Integer prty) {
		boolean flag = true;
		AccountSchemeName1Choice SchmeNm = new AccountSchemeName1Choice();

		if (null != docAcScmNm.getCd() && isTagPresent(tagSeqMap, cd)) {
			SchmeNm.setCd(tagSeqMap.get(cd));
			flag = false;
		}
		if (null != docAcScmNm.getPrtry() && isTagPresent(tagSeqMap, prty)) {
			SchmeNm.setPrtry(tagSeqMap.get(prty));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return SchmeNm;
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
	private Cheque6 setChqInstr(Cheque6 docCheq, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		Cheque6 ChqInstr = new Cheque6();

		if (null != docCheq.getChqTp() && isTagPresent(tagSeqMap, PAIN001Constant.CHQTP_360)) {
			ChqInstr.setChqTp(getChequeType(tagSeqMap.get(PAIN001Constant.CHQTP_360)));
			flag = false;
		}
		if (null != docCheq.getChqTp() && isTagPresent(tagSeqMap, PAIN001Constant.CHQNB_362)) {
			ChqInstr.setChqNb(tagSeqMap.get(PAIN001Constant.CHQNB_362));
			flag = false;
		}
		if (null != docCheq.getChqFr()) {
			NameAndAddress10 ChqFr = setChqFrNameAndAddr10(docCheq.getChqFr(), tagSeqMap, PAIN001Constant.NM_366,
					PAIN001Constant.ADRTP_370, PAIN001Constant.DEPT_372, PAIN001Constant.SUBDEPT_374,
					PAIN001Constant.STRTNM_376, PAIN001Constant.BLDGNB_378, PAIN001Constant.PSTCD_380,
					PAIN001Constant.TWNNM_382, PAIN001Constant.CTRYSUBDVSN_384, PAIN001Constant.CTRY_386,
					PAIN001Constant.ADRLINE_388);
			if (null != ChqFr) {
				ChqInstr.setChqFr(ChqFr);
				flag = false;
			}
		}
		if (null != docCheq.getDlvryMtd()) {
			ChequeDeliveryMethod1Choice DlvryMtd = setDlvryMtd(docCheq.getDlvryMtd(), tagSeqMap);
			if (null != DlvryMtd) {
				ChqInstr.setDlvryMtd(DlvryMtd);
				flag = false;
			}
		}
		if (null != docCheq.getDlvrTo()) {
			NameAndAddress10 DlvrTo = setChqFrNameAndAddr10(docCheq.getDlvrTo(), tagSeqMap, PAIN001Constant.NM_398,
					PAIN001Constant.ADRTP_402, PAIN001Constant.DEPT_404, PAIN001Constant.SUBDEPT_406,
					PAIN001Constant.STRTNM_408, PAIN001Constant.BLDGNB_410, PAIN001Constant.PSTCD_412,
					PAIN001Constant.TWNNM_414, PAIN001Constant.CTRYSUBDVSN_416, PAIN001Constant.CTRY_418,
					PAIN001Constant.ADRLINE_420);
			if (null != DlvrTo) {
				ChqInstr.setDlvrTo(DlvrTo);
				flag = false;
			}
		}
		if (null != docCheq.getInstrPrty() && isTagPresent(tagSeqMap, PAIN001Constant.INSTRPRTY_422)) {
			ChqInstr.setInstrPrty(getPriorityCode(tagSeqMap.get(PAIN001Constant.INSTRPRTY_422)));
			flag = false;
		}
		if (null != docCheq.getInstrPrty() && isTagPresent(tagSeqMap, PAIN001Constant.CHQMTRTYDT_666)) {
			ChqInstr.setChqMtrtyDt(getLocalDate(tagSeqMap.get(PAIN001Constant.CHQMTRTYDT_666)));
			flag = false;
		}
		if (null != docCheq.getChqTp() && isTagPresent(tagSeqMap, PAIN001Constant.FRMSCD_424)) {
			ChqInstr.setFrmsCd(tagSeqMap.get(PAIN001Constant.FRMSCD_424));
			flag = false;
		}
		if (null != docCheq.getChqTp() && isTagPresent(tagSeqMap, PAIN001Constant.MEMOFLD_426)) {
			List<String> MemoFld = new ArrayList<>(1);
			MemoFld.add(tagSeqMap.get(PAIN001Constant.MEMOFLD_426));
			ChqInstr.setMemoFld(MemoFld);
			flag = false;
		}
		if (null != docCheq.getChqTp() && isTagPresent(tagSeqMap, PAIN001Constant.RGNLCLRZONE_428)) {
			ChqInstr.setRgnlClrZone(tagSeqMap.get(PAIN001Constant.RGNLCLRZONE_428));
			flag = false;
		}
		if (null != docCheq.getChqTp() && isTagPresent(tagSeqMap, PAIN001Constant.PRTLCTN_430)) {
			ChqInstr.setPrtLctn(tagSeqMap.get(PAIN001Constant.PRTLCTN_430));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return ChqInstr;

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
	private NameAndAddress10 setChqFrNameAndAddr10(NameAndAddress10 docNmAdr, Map<Integer, String> tagSeqMap,
			Integer nm, Integer adrp, Integer dept, Integer subdept, Integer strnm, Integer bldnm, Integer pstcd,
			Integer twnnm, Integer ctrysbdv, Integer ctry, Integer addrline) {
		boolean flag = true;
		NameAndAddress10 DlvrTo = new NameAndAddress10();
		if (null != docNmAdr.getNm() && isTagPresent(tagSeqMap, nm)) {
			DlvrTo.setNm(tagSeqMap.get(nm));
			flag = false;
		}
		if (null != docNmAdr.getAdr()) {
			PostalAddress6 Adr = setPostalAddress6(docNmAdr.getAdr(), tagSeqMap, adrp, dept, subdept, strnm, bldnm,
					pstcd, twnnm, ctrysbdv, ctry, addrline);
			if (null != Adr) {
				DlvrTo.setAdr(Adr);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		return DlvrTo;
	}

	/*
	 * "nm", "adr"
	 */
	private NameAndAddress10 setNameAndAddr10(NameAndAddress10 docNmAdr, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		NameAndAddress10 DlvrTo = new NameAndAddress10();
		if (null != docNmAdr.getNm() && isTagPresent(tagSeqMap, PAIN001Constant.NM_477)) {
			DlvrTo.setNm(tagSeqMap.get(PAIN001Constant.NM_477));
			flag = false;
		}
		if (null != docNmAdr.getAdr()) {
			PostalAddress6 Adr = setPostalAddress6(docNmAdr.getAdr(), tagSeqMap, PAIN001Constant.ADRTP_479,
					PAIN001Constant.DEPT_480, PAIN001Constant.SUBDEPT_481, PAIN001Constant.STRTNM_482,
					PAIN001Constant.BLDGNB_483, PAIN001Constant.PSTCD_484, PAIN001Constant.TWNNM_485,
					PAIN001Constant.CTRYSUBDVSN_486, PAIN001Constant.CTRY_487, PAIN001Constant.ADRLINE_488);
			if (null != Adr) {
				DlvrTo.setAdr(Adr);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		return DlvrTo;
	}

	/*
	 * "cd", "prtry"
	 */
	private ChequeDeliveryMethod1Choice setDlvryMtd(ChequeDeliveryMethod1Choice docChqDlvMthd,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		ChequeDeliveryMethod1Choice DlvryMtd = new ChequeDeliveryMethod1Choice();

		if (null != docChqDlvMthd.getCd() && isTagPresent(tagSeqMap, PAIN001Constant.CD_392)) {
			DlvryMtd.setCd(getCheueDeliveryCode(tagSeqMap.get(PAIN001Constant.CD_392)));
			flag = false;
		}
		if (null != docChqDlvMthd.getPrtry() && isTagPresent(tagSeqMap, PAIN001Constant.PRTRY_394)) {
			DlvryMtd.setPrtry(tagSeqMap.get(PAIN001Constant.PRTRY_394));
			flag = false;
		}
		if (flag) {
			return null;
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

	private PostalAddress6 setPostalAddress6(PostalAddress6 docPstAdr, Map<Integer, String> tagSeqMap, Integer adrp,
			Integer dept, Integer subdept, Integer strnm, Integer bldnm, Integer pstcd, Integer twnnm, Integer ctrysbdv,
			Integer ctry, Integer addrline) {
		boolean flag = true;
		PostalAddress6 Adr = new PostalAddress6();
		if (null != docPstAdr.getAdrTp() && isTagPresent(tagSeqMap, adrp)) {
			Adr.setAdrTp(getAddrType(tagSeqMap.get(adrp)));
			flag = false;
		}
		if (null != docPstAdr.getDept() && isTagPresent(tagSeqMap, dept)) {
			Adr.setDept(tagSeqMap.get(dept));
			flag = false;
		}
		if (null != docPstAdr.getSubDept() && isTagPresent(tagSeqMap, subdept)) {
			Adr.setSubDept(tagSeqMap.get(subdept));
			flag = false;
		}
		if (null != docPstAdr.getStrtNm() && isTagPresent(tagSeqMap, strnm)) {
			Adr.setStrtNm(tagSeqMap.get(strnm));
			flag = false;
		}
		if (null != docPstAdr.getBldgNb() && isTagPresent(tagSeqMap, bldnm)) {
			Adr.setBldgNb(tagSeqMap.get(bldnm));
			flag = false;
		}
		if (null != docPstAdr.getPstCd() && isTagPresent(tagSeqMap, pstcd)) {
			Adr.setPstCd(tagSeqMap.get(pstcd));
			flag = false;
		}
		if (null != docPstAdr.getTwnNm() && isTagPresent(tagSeqMap, twnnm)) {
			Adr.setTwnNm(tagSeqMap.get(twnnm));
			flag = false;
		}
		if (null != docPstAdr.getCtrySubDvsn() && isTagPresent(tagSeqMap, ctrysbdv)) {
			Adr.setCtrySubDvsn(tagSeqMap.get(ctrysbdv));
			flag = false;
		}
		if (null != docPstAdr.getCtry() && isTagPresent(tagSeqMap, ctry)) {
			Adr.setCtry(tagSeqMap.get(ctry));
			flag = false;
		}
		if (null != docPstAdr.getAdrLine() && docPstAdr.getAdrLine().size() > 0 && isTagPresent(tagSeqMap, addrline)) {
			List<String> AdrLine = new ArrayList<>(3);
			if (isMultiAddressLine) {
				String[] addrlineArr = tagSeqMap.get(addrline).split(Pattern.quote("||"));
				for (String adrl : addrlineArr) {
					AdrLine.add(adrl);
				}
			} else {
				AdrLine.add(tagSeqMap.get(addrline));
			}
			Adr.setAdrLine(AdrLine);
			flag = false;
		}

		if (flag) {
			return null;
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
	private ExchangeRateInformation1 setXchgRateInf(ExchangeRateInformation1 docExRt, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		ExchangeRateInformation1 XchgRateInf = new ExchangeRateInformation1();

		if (null != docExRt.getXchgRate() && isTagPresent(tagSeqMap, PAIN001Constant.XCHGRATE_352)) {
			XchgRateInf.setXchgRate(new BigDecimal(tagSeqMap.get(PAIN001Constant.XCHGRATE_352)));
			flag = false;
		}
		if (null != docExRt.getRateTp() && isTagPresent(tagSeqMap, PAIN001Constant.RATETP_354)) {
			XchgRateInf.setRateTp(getExchangeRate(tagSeqMap.get(PAIN001Constant.RATETP_354)));
			flag = false;
		}
		if (null != docExRt.getCtrctId() && isTagPresent(tagSeqMap, PAIN001Constant.CTRCTID_356)) {
			XchgRateInf.setCtrctId(tagSeqMap.get(PAIN001Constant.CTRCTID_356));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return XchgRateInf;

	}

	/*
	 * "instdAmt", "eqvtAmt"
	 */
	private AmountType3Choice setAmt(AmountType3Choice docAmt, Map<Integer, String> tagSeqMap, String rmtdAmt) {
		boolean flag = true;
		AmountType3Choice Amt = new AmountType3Choice();

		if (null != docAmt.getInstdAmt() && isTagPresent(tagSeqMap, PAIN001Constant.INSTDAMT_35)) {
			Amt.setInstdAmt(setActiveOrHstCurrAmt(tagSeqMap.get(PAIN001Constant.CCY_CD_869), rmtdAmt));
			flag = false;
		}
		if (null != docAmt.getEqvtAmt()) {
			EquivalentAmount2 EqvtAmt = setEqvtAmt(docAmt.getEqvtAmt(), tagSeqMap);
			if (null != EqvtAmt) {
				Amt.setEqvtAmt(EqvtAmt);
				flag = false;
			}
		}

		if (flag) {
			return null;
		}
		return Amt;
	}

	/*
	 * amt", "ccyOfTrf"
	 */
	private EquivalentAmount2 setEqvtAmt(EquivalentAmount2 docEqvAmt, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		EquivalentAmount2 EqvtAmt = new EquivalentAmount2();
		if (null != docEqvAmt.getAmt() && isTagPresent(tagSeqMap, PAIN001Constant.AMT_348)) {
			EqvtAmt.setAmt(setActiveOrHstCurrAmt(tagSeqMap.get(PAIN001Constant.CCY_CD_869),
					tagSeqMap.get(PAIN001Constant.AMT_348)));
			flag = false;
		}
		if (null != docEqvAmt.getCcyOfTrf() && isTagPresent(tagSeqMap, PAIN001Constant.CCYOFTRF_665)) {
			EqvtAmt.setCcyOfTrf(tagSeqMap.get(PAIN001Constant.CCYOFTRF_665));
			flag = false;
		}

		if (flag) {
			return null;
		}
		return EqvtAmt;
	}

	private ActiveOrHistoricCurrencyAndAmount setActiveOrHstCurrAmt(String ccy_cd, String amt) {
		ActiveOrHistoricCurrencyAndAmount EAmt = new ActiveOrHistoricCurrencyAndAmount();
		EAmt.setCcy(ccy_cd);
		EAmt.setValue(new BigDecimal(amt));
		return EAmt;
	}

	/*
	 * "instrId", "endToEndId"
	 */
	private PaymentIdentification1 setPmtId(PaymentIdentification1 docPymtId, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		PaymentIdentification1 pmtId = new PaymentIdentification1();
		if (null != docPymtId && isTagPresent(tagSeqMap, PAIN001Constant.INSTRID_322)) {
			pmtId.setInstrId(tagSeqMap.get(PAIN001Constant.INSTRID_322));
			flag = false;
		}
		if (null != docPymtId && isTagPresent(tagSeqMap, PAIN001Constant.ENDTOENDID_33)) {
			pmtId.setEndToEndId(tagSeqMap.get(PAIN001Constant.ENDTOENDID_33));
			flag = false;
		}

		if (flag) {
			return null;
		}
		return pmtId;
	}

	/*
	 * "instrPrty", "svcLvl", "lclInstrm", "ctgyPurp"
	 */
	private PaymentTypeInformation19 setPmtTpInf(PaymentTypeInformation19 docPymtInf19, Map<Integer, String> tagSeqMap,
			Integer instrPrty, Integer cd, Integer prty, Integer lcd, Integer lprty, Integer ccd, Integer cprty) {
		boolean flag = true;
		PaymentTypeInformation19 pmtTpInf = new PaymentTypeInformation19();

		if (null != docPymtInf19.getInstrPrty() && isTagPresent(tagSeqMap, instrPrty)) {
			pmtTpInf.setInstrPrty(getPriorityCode(tagSeqMap.get(instrPrty)));
			flag = false;
		}
		if (null != docPymtInf19.getSvcLvl()) {
			ServiceLevel8Choice SvcLvl = setSvcLvl(docPymtInf19.getSvcLvl(), tagSeqMap, cd, prty);
			if (null != SvcLvl) {
				pmtTpInf.setSvcLvl(SvcLvl);
				flag = false;
			}
		}
		if (null != docPymtInf19.getLclInstrm()) {
			LocalInstrument2Choice LclInstrm = setLclInstrm(docPymtInf19.getLclInstrm(), tagSeqMap, lcd, lprty);
			if (null != LclInstrm) {
				pmtTpInf.setLclInstrm(LclInstrm);
				flag = false;
			}
		}
		if (null != docPymtInf19.getCtgyPurp()) {
			CategoryPurpose1Choice CtgyPurp = setCtgyPurp(docPymtInf19.getCtgyPurp(), tagSeqMap, ccd, cprty);
			if (null != CtgyPurp) {

				pmtTpInf.setCtgyPurp(CtgyPurp);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		return pmtTpInf;
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

	private CategoryPurpose1Choice setCtgyPurp(CategoryPurpose1Choice docCtgPrp, Map<Integer, String> tagSeqMap,
			Integer cd, Integer prty) {
		boolean flag = true;
		CategoryPurpose1Choice CtgyPurp = new CategoryPurpose1Choice();
		if (null != docCtgPrp.getCd() && isTagPresent(tagSeqMap, cd)) {
			CtgyPurp.setCd(tagSeqMap.get(cd));
			flag = false;
		}
		if (null != docCtgPrp.getPrtry() && isTagPresent(tagSeqMap, prty)) {
			CtgyPurp.setPrtry(tagSeqMap.get(prty));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return CtgyPurp;
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

	private LocalInstrument2Choice setLclInstrm(LocalInstrument2Choice doclcIns, Map<Integer, String> tagSeqMap,
			Integer cd, Integer prty) {
		boolean flag = true;
		LocalInstrument2Choice LclInstrm = new LocalInstrument2Choice();
		if (null != doclcIns.getCd() && isTagPresent(tagSeqMap, cd)) {
			LclInstrm.setCd(tagSeqMap.get(cd));
			flag = false;
		}
		if (null != doclcIns.getPrtry() && isTagPresent(tagSeqMap, prty)) {
			LclInstrm.setPrtry(tagSeqMap.get(prty));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return LclInstrm;
	}

	/*
	 * "cd", "prtry"
	 */
	private ServiceLevel8Choice setPmtTpInfSvcLvl(ServiceLevel8Choice docSrvLv, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		ServiceLevel8Choice SvcLvl = new ServiceLevel8Choice();
		if (null != docSrvLv.getCd() && isTagPresent(tagSeqMap, PAIN001Constant.CD_330)) {
			SvcLvl.setCd(tagSeqMap.get(PAIN001Constant.CD_330));
			flag = false;
		}
		if (null != docSrvLv.getPrtry() && isTagPresent(tagSeqMap, PAIN001Constant.PRTRY_332)) {
			SvcLvl.setPrtry(tagSeqMap.get(PAIN001Constant.PRTRY_332));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return SvcLvl;
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

	/*
	 * "cd", "prtry"
	 */
	private ServiceLevel8Choice setSvcLvl(ServiceLevel8Choice docSrLv8Choice, Map<Integer, String> tagSeqMap,
			Integer cd, Integer prty) {
		boolean flag = true;
		ServiceLevel8Choice SvcLvl = new ServiceLevel8Choice();
		if (null != docSrLv8Choice.getCd() && isTagPresent(tagSeqMap, cd)) {
			SvcLvl.setCd(tagSeqMap.get(cd));
			flag = false;
		}
		if (null != docSrLv8Choice.getPrtry() && isTagPresent(tagSeqMap, prty)) {
			SvcLvl.setPrtry(tagSeqMap.get(prty));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return SvcLvl;
	}

	/**
	 * "msgId", "creDtTm", "authstn", "nbOfTxs", "ctrlSum", "initgPty", "fwdgAgt"
	 * 
	 * @param configPojo
	 */
	public GroupHeader32 setGrpHdr(Document doc, Map<Integer, String> tagSeqMap, ConfigPojo configPojo) {
		boolean flag = true;
		GroupHeader32 GrpHdr = new GroupHeader32();
		GroupHeader32 docGrpHdr = doc.getCstmrCdtTrfInitn().getGrpHdr();
		// watermark
		BranchAndFinancialInstitutionIdentification4 fwdAgt = new BranchAndFinancialInstitutionIdentification4();
		FinancialInstitutionIdentification7 finIns = new FinancialInstitutionIdentification7();
		if (null != configPojo.getSrcSys().getWatermark()) {
			finIns.setNm(configPojo.getSrcSys().getWatermark());
		} else {
			finIns.setNm("Dummy" + configPojo.getSrcSys().getSrcSystemSort());
		}
		fwdAgt.setFinInstnId(finIns);
		GrpHdr.setFwdgAgt(fwdAgt);

		if (null != docGrpHdr.getMsgId() && isTagPresent(tagSeqMap, PAIN001Constant.MSGID_4)) {
			GrpHdr.setMsgId(tagSeqMap.get(PAIN001Constant.MSGID_4));
			flag = false;
		}
		if (null != docGrpHdr.getCreDtTm() && isTagPresent(tagSeqMap, PAIN001Constant.CREDTTM_5)) {
			try {
				// 2021-04-25 19:12:00.000000
				logger.info("tagSeqMap.get(PAIN001Constant.CREDTTM_5) " + tagSeqMap.get(PAIN001Constant.CREDTTM_5));
				LocalDateTime ldt = LocalDateTime.parse(tagSeqMap.get(PAIN001Constant.CREDTTM_5),
						XorConstant.formateYYYYMMDD_HHMMSSZ);
				GrpHdr.setCreDtTm(ldt.format(XorConstant.formateYYYYMMDDTHHMMSS));
			} catch (Exception e) {
				logger.info("Invalid date format " + e.getMessage());
				GrpHdr.setCreDtTm(XorUtil.getLocalDateTimeFormat(tagSeqMap.get(PAIN001Constant.CREDTTM_5)));
			}

			flag = false;
		}
		if (null != docGrpHdr.getNbOfTxs() && isTagPresent(tagSeqMap, PAIN001Constant.NBOFTXS_6)) {
			GrpHdr.setNbOfTxs(tagSeqMap.get(PAIN001Constant.NBOFTXS_6));
			flag = false;
		}
		if (null != docGrpHdr.getCtrlSum() && isTagPresent(tagSeqMap, PAIN001Constant.CTRLSUM_133)) {
			GrpHdr.setCtrlSum(new BigDecimal(tagSeqMap.get(PAIN001Constant.CTRLSUM_133)));
			flag = false;
		}
		if (null != docGrpHdr.getInitgPty()) {
			PartyIdentification32 InitgPty = setInitgPtyPartyIdentificatio32(docGrpHdr.getInitgPty(), tagSeqMap);
			if (null != InitgPty) {
				GrpHdr.setInitgPty(InitgPty);
				flag = false;
			}
		}
		
		if (null != docGrpHdr.getFwdgAgt()) {
			BranchAndFinancialInstitutionIdentification4 FwdgAgt = setFwdAgtBrFiId4(docGrpHdr.getFwdgAgt(), tagSeqMap);
			if (null != FwdgAgt) {
				GrpHdr.setFwdgAgt(FwdgAgt);
				flag = false;
			}
		}

		if (flag) {
			return null;
		}
		return GrpHdr;
	}

	/*
	 * "finInstnId", "brnchId"
	 */
	private BranchAndFinancialInstitutionIdentification4 setDbtrAgtBrFiId4(
			BranchAndFinancialInstitutionIdentification4 docBrFn, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		BranchAndFinancialInstitutionIdentification4 fwdgAgt = new BranchAndFinancialInstitutionIdentification4();
		if (null != docBrFn.getFinInstnId()) {
			FinancialInstitutionIdentification7 FinInstnId = setDbtrAgtFinInstnId(docBrFn.getFinInstnId(), tagSeqMap,
					PAIN001Constant.BIC_80, PAIN001Constant.MMBID_30, PAIN001Constant.CD_594, PAIN001Constant.PRTRY_595,
					PAIN001Constant.NM_209, PAIN001Constant.ADRTP_210, PAIN001Constant.DEPT_211,
					PAIN001Constant.SUBDEPT_212, PAIN001Constant.STRTNM_213, PAIN001Constant.BLDGNB_214,
					PAIN001Constant.PSTCD_215, PAIN001Constant.TWNNM_216, PAIN001Constant.CTRYSUBDVSN_217,
					PAIN001Constant.CTRY_82, PAIN001Constant.ADRLINE_218, PAIN001Constant.ID_596,
					PAIN001Constant.ISSR_598, PAIN001Constant.CD_599, PAIN001Constant.PRTRY_600);
			if (null != FinInstnId) {
				fwdgAgt.setFinInstnId(FinInstnId);
				flag = false;
			}
		}
		if (null != docBrFn.getBrnchId()) {
			BranchData2 BrnchId = setDbtrAgtBrnchId(docBrFn.getBrnchId(), tagSeqMap, PAIN001Constant.ID_138,
					PAIN001Constant.NM_219, PAIN001Constant.ADRTP_602, PAIN001Constant.DEPT_603,
					PAIN001Constant.SUBDEPT_604, PAIN001Constant.STRTNM_605, PAIN001Constant.BLDGNB_606,
					PAIN001Constant.PSTCD_607, PAIN001Constant.TWNNM_608, PAIN001Constant.CTRYSUBDVSN_609,
					PAIN001Constant.CTRY_610, PAIN001Constant.ADRLINE_611);
			if (null != BrnchId) {
				fwdgAgt.setBrnchId(BrnchId);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		return fwdgAgt;
	}

	private BranchAndFinancialInstitutionIdentification4 setFwdAgtBrFiId4(
			BranchAndFinancialInstitutionIdentification4 docBrFn, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		BranchAndFinancialInstitutionIdentification4 fwdgAgt = new BranchAndFinancialInstitutionIdentification4();

		if (null != docBrFn.getFinInstnId()) {
			FinancialInstitutionIdentification7 FinInstnId = setDbtrAgtFinInstnId(docBrFn.getFinInstnId(), tagSeqMap,
					PAIN001Constant.BIC_170, PAIN001Constant.MMBID_904, PAIN001Constant.CD_905,
					PAIN001Constant.PRTRY_906, PAIN001Constant.NM_136, PAIN001Constant.ADRTP_172,
					PAIN001Constant.DEPT_173, PAIN001Constant.SUBDEPT_174, PAIN001Constant.STRTNM_175,
					PAIN001Constant.BLDGNB_176, PAIN001Constant.PSTCD_177, PAIN001Constant.TWNNM_178,
					PAIN001Constant.CTRYSUBDVSN_179, PAIN001Constant.CTRY_180, PAIN001Constant.ADRLINE_181,
					PAIN001Constant.ID_907, PAIN001Constant.ISSR_909, PAIN001Constant.CD_910,
					PAIN001Constant.PRTRY_911);
			if (null != FinInstnId) {
				fwdgAgt.setFinInstnId(FinInstnId);
				flag = false;
			}
		}
		if (null != docBrFn.getBrnchId()) {
			BranchData2 BrnchId = setDbtrAgtBrnchId(docBrFn.getBrnchId(), tagSeqMap, PAIN001Constant.ID_183,
					PAIN001Constant.NM_184, PAIN001Constant.ADRTP_555, PAIN001Constant.DEPT_556,
					PAIN001Constant.SUBDEPT_557, PAIN001Constant.STRTNM_558, PAIN001Constant.BLDGNB_559,
					PAIN001Constant.PSTCD_560, PAIN001Constant.TWNNM_561, PAIN001Constant.CTRYSUBDVSN_562,
					PAIN001Constant.CTRY_563, PAIN001Constant.ADRLINE_564);
			if (null != BrnchId) {
				fwdgAgt.setBrnchId(BrnchId);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		return fwdgAgt;
	}

	private BranchAndFinancialInstitutionIdentification4 setCdtrAgtBrFiId4(
			BranchAndFinancialInstitutionIdentification4 docBrFn, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		BranchAndFinancialInstitutionIdentification4 fwdgAgt = new BranchAndFinancialInstitutionIdentification4();
		if (null != docBrFn.getFinInstnId()) {
			FinancialInstitutionIdentification7 FinInstnId = setDbtrAgtFinInstnId(docBrFn.getFinInstnId(), tagSeqMap,
					PAIN001Constant.BIC_108, PAIN001Constant.MMBID_39, PAIN001Constant.CD_682,
					PAIN001Constant.PRTRY_683, PAIN001Constant.NM_109, PAIN001Constant.ADRTP_343,
					PAIN001Constant.DEPT_345, PAIN001Constant.SUBDEPT_347, PAIN001Constant.STRTNM_349,
					PAIN001Constant.BLDGNB_351, PAIN001Constant.PSTCD_353, PAIN001Constant.TWNNM_355,
					PAIN001Constant.CTRYSUBDVSN_357, PAIN001Constant.CTRY_116, PAIN001Constant.ADRLINE_110,
					PAIN001Constant.ID_685, PAIN001Constant.ISSR_687, PAIN001Constant.CD_688,
					PAIN001Constant.PRTRY_689);
			if (null != FinInstnId) {
				fwdgAgt.setFinInstnId(FinInstnId);
				flag = false;
			}
		}
		if (null != docBrFn.getBrnchId()) {
			BranchData2 BrnchId = setDbtrAgtBrnchId(docBrFn.getBrnchId(), tagSeqMap, PAIN001Constant.ID_140,
					PAIN001Constant.NM_359, PAIN001Constant.ADRTP_691, PAIN001Constant.DEPT_692,
					PAIN001Constant.SUBDEPT_693, PAIN001Constant.STRTNM_694, PAIN001Constant.BLDGNB_695,
					PAIN001Constant.PSTCD_696, PAIN001Constant.TWNNM_697, PAIN001Constant.CTRYSUBDVSN_698,
					PAIN001Constant.CTRY_699, PAIN001Constant.ADRLINE_700);
			if (null != BrnchId) {
				fwdgAgt.setBrnchId(BrnchId);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		return fwdgAgt;
	}

	private BranchAndFinancialInstitutionIdentification4 setIntrmyAgt1BrFiId4(
			BranchAndFinancialInstitutionIdentification4 docBrFn, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		BranchAndFinancialInstitutionIdentification4 fwdgAgt = new BranchAndFinancialInstitutionIdentification4();

		if (null != docBrFn.getFinInstnId()) {
			FinancialInstitutionIdentification7 FinInstnId = setDbtrAgtFinInstnId(docBrFn.getFinInstnId(), tagSeqMap,
					PAIN001Constant.BIC_86, PAIN001Constant.MMBID_88, PAIN001Constant.CD_871, PAIN001Constant.PRTRY_872,
					PAIN001Constant.NM_89, PAIN001Constant.ADRTP_239, PAIN001Constant.DEPT_241,
					PAIN001Constant.SUBDEPT_243, PAIN001Constant.STRTNM_245, PAIN001Constant.BLDGNB_247,
					PAIN001Constant.PSTCD_249, PAIN001Constant.TWNNM_132, PAIN001Constant.CTRYSUBDVSN_251,
					PAIN001Constant.CTRY_115, PAIN001Constant.ADRLINE_91, PAIN001Constant.ID_874,
					PAIN001Constant.ISSR_876, PAIN001Constant.CD_877, PAIN001Constant.PRTRY_878);
			if (null != FinInstnId) {
				fwdgAgt.setFinInstnId(FinInstnId);
				flag = false;
			}
		}
		if (null != docBrFn.getBrnchId()) {
			BranchData2 BrnchId = setDbtrAgtBrnchId(docBrFn.getBrnchId(), tagSeqMap, PAIN001Constant.ID_318,
					PAIN001Constant.NM_320, PAIN001Constant.ADRTP_880, PAIN001Constant.DEPT_881,
					PAIN001Constant.SUBDEPT_882, PAIN001Constant.STRTNM_883, PAIN001Constant.BLDGNB_884,
					PAIN001Constant.PSTCD_885, PAIN001Constant.TWNNM_886, PAIN001Constant.CTRYSUBDVSN_887,
					PAIN001Constant.CTRY_888, PAIN001Constant.ADRLINE_889);
			if (null != BrnchId) {
				fwdgAgt.setBrnchId(BrnchId);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		return fwdgAgt;
	}

	private BranchAndFinancialInstitutionIdentification4 setIntrmyAgt3BrFiId4(
			BranchAndFinancialInstitutionIdentification4 docBrFn, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		BranchAndFinancialInstitutionIdentification4 fwdgAgt = new BranchAndFinancialInstitutionIdentification4();

		if (null != docBrFn.getFinInstnId()) {
			fwdgAgt.setFinInstnId(setDbtrAgtFinInstnId(docBrFn.getFinInstnId(), tagSeqMap, PAIN001Constant.BIC_283,
					PAIN001Constant.MMBID_893, PAIN001Constant.CD_894, PAIN001Constant.PRTRY_895,
					PAIN001Constant.NM_285, PAIN001Constant.ADRTP_289, PAIN001Constant.DEPT_291,
					PAIN001Constant.SUBDEPT_293, PAIN001Constant.STRTNM_295, PAIN001Constant.BLDGNB_297,
					PAIN001Constant.PSTCD_299, PAIN001Constant.TWNNM_301, PAIN001Constant.CTRYSUBDVSN_303,
					PAIN001Constant.CTRY_305, PAIN001Constant.ADRLINE_307, PAIN001Constant.ID_896,
					PAIN001Constant.ISSR_898, PAIN001Constant.CD_899, PAIN001Constant.PRTRY_900));

			flag = false;
		}
		if (null != docBrFn.getBrnchId()) {
			fwdgAgt.setBrnchId(setDbtrAgtBrnchId(docBrFn.getBrnchId(), tagSeqMap, PAIN001Constant.ID_311,
					PAIN001Constant.NM_313, PAIN001Constant.ADRTP_671, PAIN001Constant.DEPT_672,
					PAIN001Constant.SUBDEPT_673, PAIN001Constant.STRTNM_674, PAIN001Constant.BLDGNB_675,
					PAIN001Constant.PSTCD_676, PAIN001Constant.TWNNM_677, PAIN001Constant.CTRYSUBDVSN_678,
					PAIN001Constant.CTRY_679, PAIN001Constant.ADRLINE_680));

			flag = false;
		}
		if (flag) {
			return null;
		}
		return fwdgAgt;
	}

	private BranchAndFinancialInstitutionIdentification4 setBrFiId4(
			BranchAndFinancialInstitutionIdentification4 docBrFn, Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		BranchAndFinancialInstitutionIdentification4 fwdgAgt = new BranchAndFinancialInstitutionIdentification4();
		if (null != docBrFn.getFinInstnId()) {
			fwdgAgt.setFinInstnId(setDbtrAgtFinInstnId(docBrFn.getFinInstnId(), tagSeqMap, PAIN001Constant.BIC_290,
					PAIN001Constant.MMBID_861, PAIN001Constant.CD_862, PAIN001Constant.PRTRY_863,
					PAIN001Constant.NM_292, PAIN001Constant.ADRTP_296, PAIN001Constant.DEPT_298,
					PAIN001Constant.SUBDEPT_300, PAIN001Constant.STRTNM_302, PAIN001Constant.BLDGNB_304,
					PAIN001Constant.PSTCD_306, PAIN001Constant.TWNNM_308, PAIN001Constant.CTRYSUBDVSN_310,
					PAIN001Constant.CTRY_312, PAIN001Constant.ADRLINE_314, PAIN001Constant.ID_864,
					PAIN001Constant.ISSR_866, PAIN001Constant.CD_867, PAIN001Constant.PRTRY_868));
			flag = false;
		}
		if (null != docBrFn.getBrnchId()) {
			fwdgAgt.setBrnchId(setDbtrAgtBrnchId(docBrFn.getBrnchId(), tagSeqMap, PAIN001Constant.ID_318,
					PAIN001Constant.NM_320, PAIN001Constant.ADRTP_643, PAIN001Constant.DEPT_644,
					PAIN001Constant.SUBDEPT_645, PAIN001Constant.STRTNM_646, PAIN001Constant.BLDGNB_647,
					PAIN001Constant.PSTCD_648, PAIN001Constant.TWNNM_649, PAIN001Constant.CTRYSUBDVSN_650,
					PAIN001Constant.CTRY_651, PAIN001Constant.ADRLINE_652));
			flag = false;
		}
		if (flag) {
			return null;
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

	private BranchData2 setDbtrAgtBrnchId(BranchData2 docBrData, Map<Integer, String> tagSeqMap, Integer id, Integer nm,
			Integer adrp, Integer dept, Integer subdept, Integer strnm, Integer bldnm, Integer pstcd, Integer twnnm,
			Integer ctrysbdv, Integer ctry, Integer addrline) {
		boolean flag = true;
		BranchData2 BrnchId = new BranchData2();

		if (null != docBrData.getId() && isTagPresent(tagSeqMap, id)) {
			BrnchId.setId(tagSeqMap.get(id));
			flag = false;
		}
		if (null != docBrData.getNm() && isTagPresent(tagSeqMap, nm)) {
			BrnchId.setNm(tagSeqMap.get(nm));
			flag = false;
		}
		if (null != docBrData.getPstlAdr()) {
			PostalAddress6 PstlAdr = setPostalAddress6(docBrData.getPstlAdr(), tagSeqMap, adrp, dept, subdept, strnm,
					bldnm, pstcd, twnnm, ctrysbdv, ctry, addrline);
			if (null != PstlAdr) {
				BrnchId.setPstlAdr(PstlAdr);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		return BrnchId;
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

	/*
	 * "bic", "clrSysMmbId", "nm", "pstlAdr", "othr"
	 */
	private void setCdtrAgtFinInstnId(Map<Integer, Set<Integer>> tagMap,
			BranchAndFinancialInstitutionIdentification4 fwdgAgt) {
		FinancialInstitutionIdentification7 finInstnId = new FinancialInstitutionIdentification7();

		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_37, PAIN001Constant.BIC_108)) {
			finInstnId.setBIC("XXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_37, PAIN001Constant.CLRSYSMMBID_38)) {
			finInstnId.setClrSysMmbId(setCdtrAgtFinInsClrSysMmbId(tagMap));
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_37, PAIN001Constant.NM_109)) {
			finInstnId.setNm("XXXXX");
		}
		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_37, PAIN001Constant.PSTLADR_48)) {
			finInstnId.setPstlAdr(setFwdgAgtPostalAddress6(tagMap));
		}

		if (isTagPresent(tagMap, PAIN001Constant.FININSTNID_37, PAIN001Constant.OTHR_684)) {
			finInstnId.setOthr(setCdtrAgtFinInsOther(tagMap));
		}
		fwdgAgt.setFinInstnId(finInstnId);
	}

	/*
	 * "bic", "clrSysMmbId", "nm", "pstlAdr", "othr"
	 */
	private FinancialInstitutionIdentification7 setDbtrAgtFinInstnId(FinancialInstitutionIdentification7 docFnIn,
			Map<Integer, String> tagSeqMap, Integer bic, Integer nmbid, Integer cd, Integer prty, Integer nm,
			Integer adrp, Integer dept, Integer subdept, Integer strnm, Integer bldnm, Integer pstcd, Integer twnnm,
			Integer ctrysbdv, Integer ctry, Integer addrline, Integer id, Integer issr, Integer ocd, Integer oprty) {
		boolean flag = true;
		FinancialInstitutionIdentification7 finInstnId = new FinancialInstitutionIdentification7();

		if (null != docFnIn.getBIC() && isTagPresent(tagSeqMap, bic)) {
			finInstnId.setBIC(tagSeqMap.get(bic));
			flag = false;
		}

		if (null != docFnIn.getClrSysMmbId()) {
			ClearingSystemMemberIdentification2 ClrSysMmbId = setDbtrAgtClrSysMmb(docFnIn.getClrSysMmbId(), tagSeqMap,
					nmbid, cd, prty);
			if (null != ClrSysMmbId) {
				finInstnId.setClrSysMmbId(ClrSysMmbId);
				flag = false;
			}
		}
		if (null != docFnIn.getNm() && isTagPresent(tagSeqMap, nm)) {
			finInstnId.setNm(tagSeqMap.get(nm));
			flag = false;
		}
		if (null != docFnIn.getPstlAdr()) {
			PostalAddress6 PstlAdr = setPostalAddress6(docFnIn.getPstlAdr(), tagSeqMap, adrp, dept, subdept, strnm,
					bldnm, pstcd, twnnm, ctrysbdv, ctry, addrline);
			if (null != PstlAdr) {
				finInstnId.setPstlAdr(PstlAdr);
				flag = false;
			}
		}
		if (null != docFnIn.getOthr()) {
			GenericFinancialIdentification1 Othr = setDbtrAgtOther(docFnIn.getOthr(), tagSeqMap, id, issr, ocd, oprty);
			if (null != Othr) {
				finInstnId.setOthr(Othr);
				flag = false;
			}
		}
		if (flag) {
			return null;
		}
		return finInstnId;
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

	private GenericFinancialIdentification1 setDbtrAgtOther(GenericFinancialIdentification1 docGnFn,
			Map<Integer, String> tagSeqMap, Integer id, Integer issr, Integer cd, Integer prty) {
		boolean flag = true;
		GenericFinancialIdentification1 DbtrAgtOther = new GenericFinancialIdentification1();

		if (null != docGnFn.getId() && isTagPresent(tagSeqMap, id)) {
			DbtrAgtOther.setId(tagSeqMap.get(id));
			flag = false;
		}
		if (null != docGnFn.getIssr() && isTagPresent(tagSeqMap, issr)) {
			DbtrAgtOther.setIssr(tagSeqMap.get(issr));
			flag = false;
		}
		if (null != docGnFn.getSchmeNm()) {
			FinancialIdentificationSchemeName1Choice SchmeNm = setDbtrAgtSchmeNm(docGnFn.getSchmeNm(), tagSeqMap, cd,
					prty);
			if (null != SchmeNm) {
				DbtrAgtOther.setSchmeNm(SchmeNm);
				flag = false;
			}
		}
		if (flag) {
			return null;
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

	private FinancialIdentificationSchemeName1Choice setDbtrAgtSchmeNm(
			FinancialIdentificationSchemeName1Choice docFnIdSch, Map<Integer, String> tagSeqMap, Integer cd,
			Integer prty) {
		boolean flag = true;
		FinancialIdentificationSchemeName1Choice DbtrAgtSchmeNm = new FinancialIdentificationSchemeName1Choice();
		if (null != docFnIdSch.getCd() && isTagPresent(tagSeqMap, cd)) {
			DbtrAgtSchmeNm.setCd(tagSeqMap.get(cd));
			flag = false;
		}
		if (null != docFnIdSch.getPrtry() && isTagPresent(tagSeqMap, prty)) {
			DbtrAgtSchmeNm.setPrtry(tagSeqMap.get(prty));
			flag = false;
		}
		if (flag) {
			return null;
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

	private ClearingSystemMemberIdentification2 setDbtrAgtClrSysMmb(ClearingSystemMemberIdentification2 docClrSys,
			Map<Integer, String> tagSeqMap, Integer nmbid, Integer cd, Integer prty) {
		boolean flag = true;
		ClearingSystemMemberIdentification2 ClrSysMmbId = new ClearingSystemMemberIdentification2();
		if (null != docClrSys.getMmbId() && isTagPresent(tagSeqMap, nmbid)) {
			ClrSysMmbId.setMmbId(tagSeqMap.get(nmbid));
			flag = false;
		}
		if (null != docClrSys.getClrSysId()) {
			ClearingSystemIdentification2Choice ClrSysId = setDbtrAgtClrSys(docClrSys.getClrSysId(), tagSeqMap, cd,
					prty);
			if (null != ClrSysId) {
				ClrSysMmbId.setClrSysId(ClrSysId);
				flag = false;
			}
		}
		if (flag) {
			return null;
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

	private ClearingSystemIdentification2Choice setDbtrAgtClrSys(ClearingSystemIdentification2Choice docClrSys,
			Map<Integer, String> tagSeqMap, Integer cd, Integer prty) {
		boolean flag = true;
		ClearingSystemIdentification2Choice ClrSysId = new ClearingSystemIdentification2Choice();
		if (null != docClrSys.getCd() && isTagPresent(tagSeqMap, cd)) {
			ClrSysId.setCd(tagSeqMap.get(cd));
			flag = false;
		}
		if (null != docClrSys.getPrtry() && isTagPresent(tagSeqMap, prty)) {
			ClrSysId.setPrtry(tagSeqMap.get(prty));
			flag = false;
		}
		if (flag) {
			return null;
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
	private PartyIdentification32 setInitgPtyPartyIdentificatio32(PartyIdentification32 docPrtyIden32,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		PartyIdentification32 initgPty = new PartyIdentification32();
		if (null != docPrtyIden32.getNm() && isTagPresent(tagSeqMap, PAIN001Constant.NM_8)) {
			initgPty.setNm(tagSeqMap.get(PAIN001Constant.NM_8));
			flag = false;
		}
		if (null != docPrtyIden32.getPstlAdr()) {
			PostalAddress6 PstlAdr = setPostalAddress6(docPrtyIden32.getPstlAdr(), tagSeqMap, PAIN001Constant.ADRTP_160,
					PAIN001Constant.DEPT_161, PAIN001Constant.SUBDEPT_162, PAIN001Constant.STRTNM_163,
					PAIN001Constant.BLDGNB_164, PAIN001Constant.PSTCD_165, PAIN001Constant.TWNNM_166,
					PAIN001Constant.CTRYSUBDVSN_167, PAIN001Constant.CTRY_168, PAIN001Constant.ADRLINE_169);
			if (null != PstlAdr) {
				initgPty.setPstlAdr(PstlAdr);
				flag = false;
			}
		}
		if (null != docPrtyIden32.getId()) {
			Party6Choice Id = setDbtrPartyInitgPtyId(docPrtyIden32.getId(), tagSeqMap, PAIN001Constant.BICORBEI_529,
					PAIN001Constant.ID_531, PAIN001Constant.ISSR_533, PAIN001Constant.CD_534, PAIN001Constant.PRTRY_535,
					PAIN001Constant.CITYOFBIRTH_540, PAIN001Constant.CTRYOFBIRTH_541, PAIN001Constant.PRVCOFBIRTH_539,
					PAIN001Constant.BIRTHDT_538, PAIN001Constant.ID_542, PAIN001Constant.ISSR_544,
					PAIN001Constant.CD_545, PAIN001Constant.PRTRY_546);
			if (null != Id) {
				initgPty.setId(Id);
				flag = false;
			}
		}
		if (null != docPrtyIden32.getCtctDtls()) {
			ContactDetails2 CtctDtls = setDbtrPartyCtctDtls(docPrtyIden32.getCtctDtls(), tagSeqMap,
					PAIN001Constant.EMAILADR_552, PAIN001Constant.FAXNB_551, PAIN001Constant.MOBNB_550,
					PAIN001Constant.NM_548, PAIN001Constant.NMPRFX_547, PAIN001Constant.OTHR_553,
					PAIN001Constant.PHNENB_549);
			if (null != CtctDtls) {
				initgPty.setCtctDtls(CtctDtls);
				flag = false;
			}
		}
		if (null != docPrtyIden32.getCtryOfRes() && isTagPresent(tagSeqMap, PAIN001Constant.CTRYOFRES_525)) {
			initgPty.setCtryOfRes(tagSeqMap.get(PAIN001Constant.CTRYOFRES_525));
			flag = false;
		}

		if (flag) {
			return null;
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

	private ContactDetails2 setDbtrPartyCtctDtls(ContactDetails2 docCntDtl, Map<Integer, String> tagSeqMap,
			Integer emailAdr, Integer faxNb, Integer mobNbm, Integer nm, Integer nmPrFx, Integer othr, Integer phneNb) {
		boolean flag = true;
		ContactDetails2 CtctDtls = new ContactDetails2();
		if (null != docCntDtl.getEmailAdr() && isTagPresent(tagSeqMap, emailAdr)) {
			CtctDtls.setEmailAdr(tagSeqMap.get(emailAdr));
			flag = false;
		}
		if (null != docCntDtl.getFaxNb() && isTagPresent(tagSeqMap, faxNb)) {
			CtctDtls.setFaxNb(tagSeqMap.get(faxNb));
			flag = false;
		}
		if (null != docCntDtl.getMobNb() && isTagPresent(tagSeqMap, mobNbm)) {
			CtctDtls.setMobNb(tagSeqMap.get(mobNbm));
			flag = false;
		}
		if (null != docCntDtl.getNm() && isTagPresent(tagSeqMap, nm)) {
			CtctDtls.setNm(tagSeqMap.get(nm));
			flag = false;
		}
		if (null != docCntDtl.getNmPrfx() && isTagPresent(tagSeqMap, nmPrFx)) {
			CtctDtls.setNmPrfx(getNmPrFx(tagSeqMap.get(nmPrFx)));
			flag = false;
		}
		if (null != docCntDtl.getOthr() && isTagPresent(tagSeqMap, othr)) {
			CtctDtls.setOthr(tagSeqMap.get(othr));
			flag = false;
		}
		if (null != docCntDtl.getPhneNb() && isTagPresent(tagSeqMap, phneNb)) {
			CtctDtls.setPhneNb(tagSeqMap.get(phneNb));
			flag = false;
		}

		if (flag) {
			return null;
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
	private Party6Choice setDbtrPartyInitgPtyId(Party6Choice docParty, Map<Integer, String> tagSeqMap, Integer bicorbei,
			Integer id, Integer issr, Integer cd, Integer prty, Integer cityofbirth, Integer ctryOfBirth,
			Integer prvcofbirth, Integer birthdt, Integer prtyid, Integer prtyissr, Integer prtycd, Integer prtyprty) {
		boolean flag = true;
		Party6Choice initgPtyId = new Party6Choice();
		if (null != docParty.getOrgId()) {
			OrganisationIdentification4 OrgId = setDbtrPartyInitgPtyIdOrgId(docParty.getOrgId(), tagSeqMap, bicorbei,
					id, issr, cd, prty);
			if (null != OrgId) {
				initgPtyId.setOrgId(OrgId);
				flag = false;
			}
		}
		if (null != docParty.getPrvtId()) {
			PersonIdentification5 PrvtId = setDbtrPartyInitgPtyIdPrvtId(docParty.getPrvtId(), tagSeqMap, cityofbirth,
					ctryOfBirth, prvcofbirth, birthdt, prtyid, prtyissr, prtycd, prtyprty);
			if (null != PrvtId) {
				initgPtyId.setPrvtId(PrvtId);
				flag = false;
			}
		}
		if (flag) {
			return null;
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

	private PersonIdentification5 setDbtrPartyInitgPtyIdPrvtId(PersonIdentification5 docPrIdenti,
			Map<Integer, String> tagSeqMap, Integer cityofbirth, Integer ctryOfBirth, Integer prvcofbirth,
			Integer birthdt, Integer id, Integer issr, Integer cd, Integer prty) {
		boolean flag = true;
		PersonIdentification5 initgPtyIdPrvtId = new PersonIdentification5();
		if (null != docPrIdenti.getDtAndPlcOfBirth()) {
			DateAndPlaceOfBirth DtAndPlcOfBirth = setDbtrPartyDtAndPlcOfBirth(docPrIdenti.getDtAndPlcOfBirth(),
					tagSeqMap, cityofbirth, ctryOfBirth, prvcofbirth, birthdt);
			if (null != DtAndPlcOfBirth) {
				initgPtyIdPrvtId.setDtAndPlcOfBirth(DtAndPlcOfBirth);
				flag = false;
			}
		}
		if (null != docPrIdenti.getOthr() && docPrIdenti.getOthr().size() > 0) {
			List<GenericPersonIdentification1> Othr = setDbtrPartyInitgPtyIdPrvtOth(docPrIdenti.getOthr().get(0),
					tagSeqMap, id, issr, cd, prty);
			if (null != Othr) {
				initgPtyIdPrvtId.setOthr(Othr);
				flag = false;
			}
		}

		if (flag) {
			return null;
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

	private List<GenericPersonIdentification1> setDbtrPartyInitgPtyIdPrvtOth(GenericPersonIdentification1 docPrId,
			Map<Integer, String> tagSeqMap, Integer id, Integer issr, Integer cd, Integer prty) {
		boolean flag = true;
		List<GenericPersonIdentification1> initgPtyIdPrvtOth = new ArrayList<>();
		GenericPersonIdentification1 gpidn = new GenericPersonIdentification1();
		if (null != docPrId.getId() && isTagPresent(tagSeqMap, id)) {
			gpidn.setId(tagSeqMap.get(id));
			flag = false;
		}
		if (null != docPrId.getIssr() && isTagPresent(tagSeqMap, issr)) {
			gpidn.setIssr(tagSeqMap.get(issr));
			flag = false;
		}
		if (null != docPrId.getSchmeNm()) {
			PersonIdentificationSchemeName1Choice SchmeNm = setDbtrPartyInitPtySchma(docPrId.getSchmeNm(), tagSeqMap,
					cd, prty);
			if (null != SchmeNm) {
				gpidn.setSchmeNm(SchmeNm);
				flag = false;
			}
		}

		initgPtyIdPrvtOth.add(gpidn);
		if (flag) {
			return null;
		}
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

	private PersonIdentificationSchemeName1Choice setDbtrPartyInitPtySchma(
			PersonIdentificationSchemeName1Choice docOrgIdnScm, Map<Integer, String> tagSeqMap, Integer cd,
			Integer prty) {
		boolean flag = true;
		PersonIdentificationSchemeName1Choice SchmeNm = new PersonIdentificationSchemeName1Choice();

		if (null != docOrgIdnScm.getCd() && isTagPresent(tagSeqMap, cd)) {
			SchmeNm.setCd(tagSeqMap.get(cd));
			flag = false;
		}
		if (null != docOrgIdnScm.getPrtry() && isTagPresent(tagSeqMap, prty)) {
			SchmeNm.setPrtry(tagSeqMap.get(prty));
			flag = false;
		}

		if (flag) {
			return null;
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

	private DateAndPlaceOfBirth setDbtrPartyDtAndPlcOfBirth(DateAndPlaceOfBirth docDtPlBrth,
			Map<Integer, String> tagSeqMap, Integer cityofbirth, Integer ctryOfBirth, Integer prvcofbirth,
			Integer birthdt) {
		boolean flag = true;
		DateAndPlaceOfBirth DtAndPlcOfBirth = new DateAndPlaceOfBirth();
		if (null != docDtPlBrth.getCityOfBirth() && isTagPresent(tagSeqMap, cityofbirth)) {
			DtAndPlcOfBirth.setCityOfBirth(tagSeqMap.get(cityofbirth));
			flag = false;
		}
		if (null != docDtPlBrth.getCtryOfBirth() && isTagPresent(tagSeqMap, ctryOfBirth)) {
			DtAndPlcOfBirth.setCtryOfBirth(tagSeqMap.get(ctryOfBirth));
			flag = false;
		}
		if (null != docDtPlBrth.getPrvcOfBirth() && isTagPresent(tagSeqMap, prvcofbirth)) {
			DtAndPlcOfBirth.setPrvcOfBirth(tagSeqMap.get(prvcofbirth));
			flag = false;
		}
		if (null != docDtPlBrth.getBirthDt() && isTagPresent(tagSeqMap, birthdt)) {
			DtAndPlcOfBirth.setBirthDt(getLocalDate(tagSeqMap.get(birthdt)));
			flag = false;
		}
		if (flag) {
			return null;
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

	private OrganisationIdentification4 setInvceeIdOrgId(Map<Integer, Set<Integer>> tagMap) {
		OrganisationIdentification4 initgPtyIdOrgId = new OrganisationIdentification4();

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
	private OrganisationIdentification4 setDbtrPartyInitgPtyIdOrgId(OrganisationIdentification4 docorgidn,
			Map<Integer, String> tagSeqMap, Integer bicorbei, Integer id, Integer issr, Integer cd, Integer prty) {
		boolean flag = true;
		OrganisationIdentification4 initgPtyIdOrgId = new OrganisationIdentification4();
		if (null != docorgidn.getBICOrBEI() && isTagPresent(tagSeqMap, bicorbei)) {
			initgPtyIdOrgId.setBICOrBEI(tagSeqMap.get(bicorbei));
			flag = false;
		}
		if (null != docorgidn.getOthr() && docorgidn.getOthr().size() > 0) {
			List<GenericOrganisationIdentification1> Othr = setDbtrPartyInitgPtyIdOrgIdOthr(docorgidn.getOthr().get(0),
					tagSeqMap, id, issr, cd, prty);
			if (null != Othr) {
				initgPtyIdOrgId.setOthr(Othr);
				flag = false;
			}
		}
		if (flag) {
			return null;
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
			GenericOrganisationIdentification1 docGnOrgid1, Map<Integer, String> tagSeqMap, Integer id, Integer issr,
			Integer cd, Integer prty) {
		boolean flag = true;
		List<GenericOrganisationIdentification1> InitgPtyIdOrgIdOthr = new ArrayList<>();
		GenericOrganisationIdentification1 goid = new GenericOrganisationIdentification1();
		if (null != docGnOrgid1.getId() && isTagPresent(tagSeqMap, id)) {
			goid.setId(tagSeqMap.get(id));
			flag = false;
		}
		if (null != docGnOrgid1.getIssr() && isTagPresent(tagSeqMap, issr)) {
			goid.setIssr(tagSeqMap.get(issr));
			flag = false;
		}
		if (null != docGnOrgid1.getSchmeNm()) {
			OrganisationIdentificationSchemeName1Choice SchmeNm = setDbtrPartyInitgPtyIdOrgIdOthrScm(
					docGnOrgid1.getSchmeNm(), tagSeqMap, cd, prty);
			if (null != SchmeNm) {
				goid.setSchmeNm(SchmeNm);
				flag = false;
			}
		}

		InitgPtyIdOrgIdOthr.add(goid);
		if (flag) {
			return null;
		}
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
			OrganisationIdentificationSchemeName1Choice docOrgIdnScm, Map<Integer, String> tagSeqMap, Integer cd,
			Integer prty) {
		boolean flag = true;
		OrganisationIdentificationSchemeName1Choice InitgPtyIdOrgIdOthrScm = new OrganisationIdentificationSchemeName1Choice();
		if (null != docOrgIdnScm.getCd() && isTagPresent(tagSeqMap, cd)) {
			InitgPtyIdOrgIdOthrScm.setCd(tagSeqMap.get(cd));
			flag = false;
		}
		if (null != docOrgIdnScm.getPrtry() && isTagPresent(tagSeqMap, prty)) {
			InitgPtyIdOrgIdOthrScm.setPrtry(tagSeqMap.get(prty));
			flag = false;
		}
		if (flag) {
			return null;
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
	private PartyIdentification32 setDbtrPartyIdentificatio32(PartyIdentification32 docPrtIdn,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		PartyIdentification32 initgPty = new PartyIdentification32();
		if (null != docPrtIdn.getNm() && isTagPresent(tagSeqMap, PAIN001Constant.NM_22)) {
			initgPty.setNm(tagSeqMap.get(PAIN001Constant.NM_22));
			flag = false;
		}
		if (null != docPrtIdn.getPstlAdr()) {
			PostalAddress6 PstlAdr = setPostalAddress6(docPrtIdn.getPstlAdr(), tagSeqMap, PAIN001Constant.ADRTP_191,
					PAIN001Constant.DEPT_192, PAIN001Constant.SUBDEPT_193, PAIN001Constant.STRTNM_194,
					PAIN001Constant.BLDGNB_195, PAIN001Constant.PSTCD_196, PAIN001Constant.TWNNM_197,
					PAIN001Constant.CTRYSUBDVSN_198, PAIN001Constant.CTRY_51, PAIN001Constant.ADRLINE_79);
			if (null != PstlAdr) {
				initgPty.setPstlAdr(PstlAdr);
				flag = false;
			}
		}
		if (null != docPrtIdn.getId()) {
			Party6Choice Id = setDbtrPartyInitgPtyId(docPrtIdn.getId(), tagSeqMap, PAIN001Constant.BICORBEI_569,
					PAIN001Constant.ID_76, PAIN001Constant.ISSR_571, PAIN001Constant.CD_572, PAIN001Constant.PRTRY_573,
					PAIN001Constant.CITYOFBIRTH_578, PAIN001Constant.CTRYOFBIRTH_579, PAIN001Constant.PRVCOFBIRTH_577,
					PAIN001Constant.BIRTHDT_576, PAIN001Constant.ID_580, PAIN001Constant.ISSR_582,
					PAIN001Constant.CD_583, PAIN001Constant.PRTRY_584);
			if (null != Id) {
				initgPty.setId(Id);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtctDtls()) {
			ContactDetails2 CtctDtls = setDbtrPartyCtctDtls(docPrtIdn.getCtctDtls(), tagSeqMap,
					PAIN001Constant.EMAILADR_590, PAIN001Constant.FAXNB_589, PAIN001Constant.MOBNB_588,
					PAIN001Constant.NM_586, PAIN001Constant.NMPRFX_585, PAIN001Constant.OTHR_591,
					PAIN001Constant.PHNENB_587);
			if (null != CtctDtls) {
				initgPty.setCtctDtls(CtctDtls);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtryOfRes() && isTagPresent(tagSeqMap, PAIN001Constant.CTRYOFRES_566)) {
			initgPty.setCtryOfRes(tagSeqMap.get(PAIN001Constant.CTRYOFRES_566));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return initgPty;
	}

	private PartyIdentification32 setCdtrPartyIdentificatio32(PartyIdentification32 docPrtIdn,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		PartyIdentification32 initgPty = new PartyIdentification32();

		if (null != docPrtIdn.getNm() && isTagPresent(tagSeqMap, PAIN001Constant.NM_41)) {
			initgPty.setNm(tagSeqMap.get(PAIN001Constant.NM_41));
			flag = false;
		}
		if (null != docPrtIdn.getPstlAdr()) {
			PostalAddress6 PstlAdr = setPostalAddress6(docPrtIdn.getPstlAdr(), tagSeqMap, PAIN001Constant.ADRTP_389,
					PAIN001Constant.DEPT_391, PAIN001Constant.SUBDEPT_393, PAIN001Constant.STRTNM_395,
					PAIN001Constant.BLDGNB_397, PAIN001Constant.PSTCD_399, PAIN001Constant.TWNNM_401,
					PAIN001Constant.CTRYSUBDVSN_403, PAIN001Constant.CTRY_49, PAIN001Constant.ADRLINE_111);
			if (null != PstlAdr) {
				initgPty.setPstlAdr(PstlAdr);
				flag = false;
			}
		}
		if (null != docPrtIdn.getId()) {
			Party6Choice Id = setDbtrPartyInitgPtyId(docPrtIdn.getId(), tagSeqMap, PAIN001Constant.BICORBEI_706,
					PAIN001Constant.ID_708, PAIN001Constant.ISSR_710, PAIN001Constant.CD_711, PAIN001Constant.PRTRY_712,
					PAIN001Constant.CITYOFBIRTH_717, PAIN001Constant.CTRYOFBIRTH_718, PAIN001Constant.PRVCOFBIRTH_716,
					PAIN001Constant.BIRTHDT_715, PAIN001Constant.ID_719, PAIN001Constant.ISSR_721,
					PAIN001Constant.CD_722, PAIN001Constant.PRTRY_723);
			if (null != Id) {
				initgPty.setId(Id);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtctDtls()) {
			ContactDetails2 CtctDtls = setDbtrPartyCtctDtls(docPrtIdn.getCtctDtls(), tagSeqMap,
					PAIN001Constant.EMAILADR_729, PAIN001Constant.FAXNB_728, PAIN001Constant.MOBNB_727,
					PAIN001Constant.NM_725, PAIN001Constant.NMPRFX_724, PAIN001Constant.OTHR_730,
					PAIN001Constant.PHNENB_726);
			if (null != CtctDtls) {
				initgPty.setCtctDtls(CtctDtls);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtryOfRes() && isTagPresent(tagSeqMap, PAIN001Constant.CTRYOFRES_702)) {
			initgPty.setCtryOfRes(tagSeqMap.get(PAIN001Constant.CTRYOFRES_702));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return initgPty;
	}

	private PartyIdentification32 setUltmtDbtrPartyIdentificatio32(PartyIdentification32 docPrtIdn,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		PartyIdentification32 initgPty = new PartyIdentification32();

		if (null != docPrtIdn.getNm() && isTagPresent(tagSeqMap, PAIN001Constant.NM_434)) {
			initgPty.setNm(tagSeqMap.get(PAIN001Constant.NM_434));
			flag = false;
		}
		if (null != docPrtIdn.getPstlAdr()) {
			PostalAddress6 PstlAdr = setPostalAddress6(docPrtIdn.getPstlAdr(), tagSeqMap, PAIN001Constant.ADRTP_438,
					PAIN001Constant.DEPT_440, PAIN001Constant.SUBDEPT_442, PAIN001Constant.STRTNM_444,
					PAIN001Constant.BLDGNB_446, PAIN001Constant.PSTCD_448, PAIN001Constant.TWNNM_450,
					PAIN001Constant.CTRYSUBDVSN_452, PAIN001Constant.CTRY_454, PAIN001Constant.ADRLINE_456);
			if (null != PstlAdr) {
				initgPty.setPstlAdr(PstlAdr);
				flag = false;
			}
		}
		if (null != docPrtIdn.getId()) {
			Party6Choice Id = setDbtrPartyInitgPtyId(docPrtIdn.getId(), tagSeqMap, PAIN001Constant.BICORBEI_833,
					PAIN001Constant.ID_835, PAIN001Constant.ISSR_837, PAIN001Constant.CD_838, PAIN001Constant.PRTRY_839,
					PAIN001Constant.CITYOFBIRTH_851, PAIN001Constant.CTRYOFBIRTH_852, PAIN001Constant.PRVCOFBIRTH_850,
					PAIN001Constant.BIRTHDT_849, PAIN001Constant.ID_853, PAIN001Constant.ISSR_855,
					PAIN001Constant.CD_856, PAIN001Constant.PRTRY_857);
			if (null != Id) {
				initgPty.setId(Id);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtctDtls()) {
			ContactDetails2 CtctDtls = setDbtrPartyCtctDtls(docPrtIdn.getCtctDtls(), tagSeqMap,
					PAIN001Constant.EMAILADR_845, PAIN001Constant.FAXNB_844, PAIN001Constant.MOBNB_843,
					PAIN001Constant.NM_841, PAIN001Constant.NMPRFX_840, PAIN001Constant.OTHR_846,
					PAIN001Constant.PHNENB_842);
			if (null != CtctDtls) {
				initgPty.setCtctDtls(CtctDtls);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtryOfRes() && isTagPresent(tagSeqMap, PAIN001Constant.CTRYOFRES_668)) {
			initgPty.setCtryOfRes(tagSeqMap.get(PAIN001Constant.CTRYOFRES_668));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return initgPty;
	}

	private PartyIdentification32 setUltmtCdtrPartyIdentificatio32(PartyIdentification32 docPrtIdn,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		PartyIdentification32 initgPty = new PartyIdentification32();

		if (null != docPrtIdn.getNm() && isTagPresent(tagSeqMap, PAIN001Constant.NM_419)) {
			initgPty.setNm(tagSeqMap.get(PAIN001Constant.NM_419));
			flag = false;
		}
		if (null != docPrtIdn.getPstlAdr()) {
			PostalAddress6 PstlAdr = setPostalAddress6(docPrtIdn.getPstlAdr(), tagSeqMap, PAIN001Constant.ADRTP_423,
					PAIN001Constant.DEPT_425, PAIN001Constant.SUBDEPT_427, PAIN001Constant.STRTNM_429,
					PAIN001Constant.BLDGNB_431, PAIN001Constant.PSTCD_433, PAIN001Constant.TWNNM_435,
					PAIN001Constant.CTRYSUBDVSN_437, PAIN001Constant.CTRY_439, PAIN001Constant.ADRLINE_441);
			if (null != PstlAdr) {
				initgPty.setPstlAdr(PstlAdr);
				flag = false;
			}
		}
		if (null != docPrtIdn.getId()) {
			Party6Choice Id = setDbtrPartyInitgPtyId(docPrtIdn.getId(), tagSeqMap, PAIN001Constant.BICORBEI_736,
					PAIN001Constant.ID_738, PAIN001Constant.ISSR_740, PAIN001Constant.CD_741, PAIN001Constant.PRTRY_742,
					PAIN001Constant.CITYOFBIRTH_754, PAIN001Constant.CTRYOFBIRTH_755, PAIN001Constant.PRVCOFBIRTH_753,
					PAIN001Constant.BIRTHDT_752, PAIN001Constant.ID_756, PAIN001Constant.ISSR_758,
					PAIN001Constant.CD_759, PAIN001Constant.PRTRY_760);
			if (null != Id) {
				initgPty.setId(Id);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtctDtls()) {
			ContactDetails2 CtctDtls = setDbtrPartyCtctDtls(docPrtIdn.getCtctDtls(), tagSeqMap,
					PAIN001Constant.EMAILADR_748, PAIN001Constant.FAXNB_747, PAIN001Constant.MOBNB_746,
					PAIN001Constant.NM_744, PAIN001Constant.NMPRFX_743, PAIN001Constant.OTHR_749,
					PAIN001Constant.PHNENB_745);
			if (null != CtctDtls) {
				initgPty.setCtctDtls(CtctDtls);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtryOfRes() && isTagPresent(tagSeqMap, PAIN001Constant.CTRYOFRES_732)) {
			initgPty.setCtryOfRes(tagSeqMap.get(PAIN001Constant.CTRYOFRES_732));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return initgPty;
	}

	private PartyIdentification32 setInvcrPartyIdentificatio32(PartyIdentification32 docPrtIdn,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		PartyIdentification32 initgPty = new PartyIdentification32();

		if (null != docPrtIdn.getNm() && isTagPresent(tagSeqMap, PAIN001Constant.NM_499)) {
			initgPty.setNm(tagSeqMap.get(PAIN001Constant.NM_499));
			flag = false;
		}
		if (null != docPrtIdn.getPstlAdr()) {
			PostalAddress6 PstlAdr = setPostalAddress6(docPrtIdn.getPstlAdr(), tagSeqMap, PAIN001Constant.ADRTP_501,
					PAIN001Constant.DEPT_502, PAIN001Constant.SUBDEPT_503, PAIN001Constant.STRTNM_504,
					PAIN001Constant.BLDGNB_505, PAIN001Constant.PSTCD_506, PAIN001Constant.TWNNM_507,
					PAIN001Constant.CTRYSUBDVSN_508, PAIN001Constant.CTRY_509, PAIN001Constant.ADRLINE_510);
			if (null != PstlAdr) {
				initgPty.setPstlAdr(PstlAdr);
				flag = false;
			}
		}
		if (null != docPrtIdn.getId()) {
			Party6Choice Id = setDbtrPartyInitgPtyId(docPrtIdn.getId(), tagSeqMap, PAIN001Constant.BICORBEI_802,
					PAIN001Constant.ID_804, PAIN001Constant.ISSR_806, PAIN001Constant.CD_807, PAIN001Constant.PRTRY_808,
					PAIN001Constant.CITYOFBIRTH_813, PAIN001Constant.CTRYOFBIRTH_814, PAIN001Constant.PRVCOFBIRTH_812,
					PAIN001Constant.BIRTHDT_811, PAIN001Constant.ID_815, PAIN001Constant.ISSR_817,
					PAIN001Constant.CD_818, PAIN001Constant.PRTRY_819);
			if (null != Id) {
				initgPty.setId(Id);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtctDtls()) {
			ContactDetails2 CtctDtls = setDbtrPartyCtctDtls(docPrtIdn.getCtctDtls(), tagSeqMap,
					PAIN001Constant.EMAILADR_825, PAIN001Constant.FAXNB_824, PAIN001Constant.MOBNB_823,
					PAIN001Constant.NM_821, PAIN001Constant.NMPRFX_820, PAIN001Constant.OTHR_826,
					PAIN001Constant.PHNENB_822);
			if (null != CtctDtls) {
				initgPty.setCtctDtls(CtctDtls);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtryOfRes() && isTagPresent(tagSeqMap, PAIN001Constant.CTRYOFRES_768)) {
			initgPty.setCtryOfRes(tagSeqMap.get(PAIN001Constant.CTRYOFRES_768));
			flag = false;
		}
		if (flag) {
			return null;
		}
		return initgPty;
	}

	private PartyIdentification32 setInvceePartyIdentificatio32(PartyIdentification32 docPrtIdn,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		PartyIdentification32 initgPty = new PartyIdentification32();

		if (null != docPrtIdn.getNm() && isTagPresent(tagSeqMap, PAIN001Constant.NM_512)) {
			initgPty.setNm(tagSeqMap.get(PAIN001Constant.NM_512));
			flag = false;
		}
		if (null != docPrtIdn.getPstlAdr()) {
			PostalAddress6 PstlAdr = setPostalAddress6(docPrtIdn.getPstlAdr(), tagSeqMap, PAIN001Constant.ADRTP_514,
					PAIN001Constant.DEPT_515, PAIN001Constant.SUBDEPT_516, PAIN001Constant.STRTNM_517,
					PAIN001Constant.BLDGNB_518, PAIN001Constant.PSTCD_519, PAIN001Constant.TWNNM_520,
					PAIN001Constant.CTRYSUBDVSN_521, PAIN001Constant.CTRY_522, PAIN001Constant.ADRLINE_523);
			if (null != PstlAdr) {
				initgPty.setPstlAdr(PstlAdr);
				flag = false;
			}
		}
		if (null != docPrtIdn.getId()) {
			Party6Choice Id = setDbtrPartyInitgPtyId(docPrtIdn.getId(), tagSeqMap, PAIN001Constant.BICORBEI_775,
					PAIN001Constant.ID_777, PAIN001Constant.ISSR_779, PAIN001Constant.CD_780, PAIN001Constant.PRTRY_781,
					PAIN001Constant.CITYOFBIRTH_786, PAIN001Constant.CTRYOFBIRTH_787, PAIN001Constant.PRVCOFBIRTH_785,
					PAIN001Constant.BIRTHDT_784, PAIN001Constant.ID_788, PAIN001Constant.ISSR_790,
					PAIN001Constant.CD_791, PAIN001Constant.PRTRY_792);
			if (null != Id) {
				initgPty.setId(Id);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtctDtls()) {
			ContactDetails2 CtctDtls = setDbtrPartyCtctDtls(docPrtIdn.getCtctDtls(), tagSeqMap,
					PAIN001Constant.EMAILADR_798, PAIN001Constant.FAXNB_797, PAIN001Constant.MOBNB_796,
					PAIN001Constant.NM_794, PAIN001Constant.NMPRFX_793, PAIN001Constant.OTHR_799,
					PAIN001Constant.PHNENB_795);
			if (null != CtctDtls) {
				initgPty.setCtctDtls(CtctDtls);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtryOfRes() && isTagPresent(tagSeqMap, PAIN001Constant.CTRYOFRES_771)) {
			initgPty.setCtryOfRes(tagSeqMap.get(PAIN001Constant.CTRYOFRES_771));
			flag = false;
		}

		if (flag) {
			return null;
		}
		return initgPty;
	}

	private PartyIdentification32 setPartyIdentificatio32(PartyIdentification32 docPrtIdn,
			Map<Integer, String> tagSeqMap) {
		boolean flag = true;
		PartyIdentification32 initgPty = new PartyIdentification32();

		if (null != docPrtIdn.getNm() && isTagPresent(tagSeqMap, PAIN001Constant.NM_235)) {
			flag = true;
			initgPty.setNm(tagSeqMap.get(PAIN001Constant.NM_235));
			flag = false;
		}
		if (null != docPrtIdn.getPstlAdr()) {
			PostalAddress6 PstlAdr = setPostalAddress6(docPrtIdn.getPstlAdr(), tagSeqMap, PAIN001Constant.ADRTP_237,
					PAIN001Constant.DEPT_238, PAIN001Constant.SUBDEPT_240, PAIN001Constant.STRTNM_242,
					PAIN001Constant.BLDGNB_244, PAIN001Constant.PSTCD_246, PAIN001Constant.TWNNM_248,
					PAIN001Constant.CTRYSUBDVSN_250, PAIN001Constant.CTRY_252, PAIN001Constant.ADRLINE_254);
			if (null != PstlAdr) {
				initgPty.setPstlAdr(PstlAdr);
				flag = false;
			}
		}
		if (null != docPrtIdn.getId()) {
			Party6Choice Id = setDbtrPartyInitgPtyId(docPrtIdn.getId(), tagSeqMap, PAIN001Constant.BICORBEI_617,
					PAIN001Constant.ID_619, PAIN001Constant.ISSR_621, PAIN001Constant.CD_622, PAIN001Constant.PRTRY_623,
					PAIN001Constant.CITYOFBIRTH_628, PAIN001Constant.CTRYOFBIRTH_629, PAIN001Constant.PRVCOFBIRTH_627,
					PAIN001Constant.BIRTHDT_626, PAIN001Constant.ID_630, PAIN001Constant.ISSR_632,
					PAIN001Constant.CD_633, PAIN001Constant.PRTRY_634);
			if (null != Id) {
				initgPty.setId(Id);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtctDtls()) {
			ContactDetails2 CtctDtls = setDbtrPartyCtctDtls(docPrtIdn.getCtctDtls(), tagSeqMap,
					PAIN001Constant.EMAILADR_640, PAIN001Constant.FAXNB_639, PAIN001Constant.MOBNB_638,
					PAIN001Constant.NM_636, PAIN001Constant.NMPRFX_635, PAIN001Constant.OTHR_641,
					PAIN001Constant.PHNENB_637);
			if (null != CtctDtls) {
				initgPty.setCtctDtls(CtctDtls);
				flag = false;
			}
		}
		if (null != docPrtIdn.getCtryOfRes() && isTagPresent(tagSeqMap, PAIN001Constant.CTRYOFRES_613)) {
			initgPty.setCtryOfRes(tagSeqMap.get(PAIN001Constant.CTRYOFRES_613));
			flag = false;
		}

		if (flag) {
			return null;
		}
		return initgPty;
	}

	private String getPriorityCode(String chrbr) {
		Priority2Code PRTYPE = null;
		switch (chrbr) {
		case "HIGH":
			PRTYPE = Priority2Code.HIGH;
			break;
		case "NORM":
			PRTYPE = Priority2Code.NORM;
			break;
		default:
			return chrbr;
		}
		if (null == PRTYPE) {
			return null;
		}
		return PRTYPE.toString();
	}

	private String getCreditDebitCode(String chrbr) {
		CreditDebitCode PRTYPE = null;
		switch (chrbr) {
		case "CRDT":
			PRTYPE = CreditDebitCode.CRDT;
			break;
		case "DBIT":
			PRTYPE = CreditDebitCode.DBIT;
			break;
		default:
			return chrbr;
		}
		if (null == PRTYPE) {
			return null;
		}
		return PRTYPE.toString();
	}

	private String getExchangeRate(String exrt) {
		ExchangeRateType1Code PRTYPE = null;
		switch (exrt) {
		case "SPOT":
			PRTYPE = ExchangeRateType1Code.SPOT;
			break;
		case "SALE":
			PRTYPE = ExchangeRateType1Code.SALE;
			break;
		case "AGRD":
			PRTYPE = ExchangeRateType1Code.AGRD;
			break;
		default:
			return exrt;
		}
		if (null == PRTYPE) {
			return null;
		}
		return PRTYPE.toString();
	}

	private String getRegulatoryReportingType1Code(String exrt) {
		RegulatoryReportingType1Code code = null;
		switch (exrt) {
		case "CRED":
			code = RegulatoryReportingType1Code.CRED;
			break;
		case "DEBT":
			code = RegulatoryReportingType1Code.DEBT;
			break;
		case "BOTH":
			code = RegulatoryReportingType1Code.BOTH;
			break;
		default:
			return exrt;

		}
		if (null == code) {
			return null;
		}
		return code.toString();
	}

	private String getCashAccntTypeCode(String code) {
		CashAccountType4Code CODE = null;
		switch (code) {
		case "CASH":
			CODE = CashAccountType4Code.CASH;
			break;
		case "CHAR":
			CODE = CashAccountType4Code.CHAR;
			break;
		case "COMM":
			CODE = CashAccountType4Code.COMM;
			break;
		case "TAXE":
			CODE = CashAccountType4Code.TAXE;
			break;
		case "CISH":
			CODE = CashAccountType4Code.CISH;
			break;
		case "TRAS":
			CODE = CashAccountType4Code.TRAS;
			break;
		case "SACC":
			CODE = CashAccountType4Code.SACC;
			break;
		case "CACC":
			CODE = CashAccountType4Code.CACC;
			break;
		case "SVGS":
			CODE = CashAccountType4Code.SVGS;
			break;
		case "ONDP":
			CODE = CashAccountType4Code.ONDP;
			break;
		case "MGLD":
			CODE = CashAccountType4Code.MGLD;
			break;
		case "MOMA":
			CODE = CashAccountType4Code.MOMA;
			break;
		case "LOAN":
			CODE = CashAccountType4Code.LOAN;
			break;
		case "SLRY":
			CODE = CashAccountType4Code.SLRY;
			break;
		case "ODFT":
			CODE = CashAccountType4Code.ODFT;
			break;
		default:
			return code;
		}
		if (null == CODE) {
			return null;
		}
		return CODE.toString();
	}

	private String getCheueDeliveryCode(String code) {
		ChequeDelivery1Code CODE = null;
		switch (code) {
		case "MLDB":
			CODE = ChequeDelivery1Code.MLDB;
			break;
		case "MLCD":
			CODE = ChequeDelivery1Code.MLCD;
			break;
		case "MLFA":
			CODE = ChequeDelivery1Code.MLFA;
			break;
		case "CRDB":
			CODE = ChequeDelivery1Code.CRDB;
			break;
		case "CRCD":
			CODE = ChequeDelivery1Code.CRCD;
			break;
		case "CRFA":
			CODE = ChequeDelivery1Code.CRFA;
			break;
		case "PUDB":
			CODE = ChequeDelivery1Code.PUDB;
			break;
		case "PUCD":
			CODE = ChequeDelivery1Code.PUCD;
			break;
		case "PUFA":
			CODE = ChequeDelivery1Code.PUFA;
			break;
		case "RGDB":
			CODE = ChequeDelivery1Code.RGDB;
			break;
		case "RGCD":
			CODE = ChequeDelivery1Code.RGCD;
			break;
		case "RGFA":
			CODE = ChequeDelivery1Code.RGFA;
			break;
		default:
			return code;
		}
		if (null == CODE) {
			return null;
		}
		return CODE.toString();
	}

	private String getDocumentType5Code(String code) {
		DocumentType5Code CODE = null;
		switch (code) {
		case "MSIN":
			CODE = DocumentType5Code.MSIN;
			break;
		case "CNFA":
			CODE = DocumentType5Code.CNFA;
			break;
		case "DNFA":
			CODE = DocumentType5Code.DNFA;
			break;
		case "CINV":
			CODE = DocumentType5Code.CINV;
			break;
		case "CREN":
			CODE = DocumentType5Code.CREN;
			break;
		case "DEBN":
			CODE = DocumentType5Code.DEBN;
			break;
		case "HIRI":
			CODE = DocumentType5Code.HIRI;
			break;
		case "SBIN":
			CODE = DocumentType5Code.SBIN;
			break;
		case "CMCN":
			CODE = DocumentType5Code.CMCN;
			break;
		case "SOAC":
			CODE = DocumentType5Code.SOAC;
			break;
		case "DISP":
			CODE = DocumentType5Code.DISP;
			break;
		case "BOLD":
			CODE = DocumentType5Code.BOLD;
			break;
		default:
			return code;
		}
		if (null == CODE) {
			return null;
		}
		return CODE.toString();
	}

	private String getChrgeBrType(String chrbr) {
		ChargeBearerType1Code CBTYPE = null;
		switch (chrbr) {
		case "DEBT":
			CBTYPE = ChargeBearerType1Code.DEBT;
			break;
		case "CRED":
			CBTYPE = ChargeBearerType1Code.CRED;
			break;
		case "SHAR":
			CBTYPE = ChargeBearerType1Code.SHAR;
			break;
		case "SLEV":
			CBTYPE = ChargeBearerType1Code.SLEV;
			break;
		default:
			return chrbr;
		}
		if (null == CBTYPE) {
			return null;
		}
		return CBTYPE.toString();
	}

	private String getNmPrFx(String nmPr) {
		NamePrefix1Code NMPRFX = null;
		switch (nmPr) {
		case "DOCT":
			NMPRFX = NamePrefix1Code.DOCT;
			break;
		case "MIST":
			NMPRFX = NamePrefix1Code.MIST;
			break;
		case "MISS":
			NMPRFX = NamePrefix1Code.MISS;
			break;
		case "MADM":
			NMPRFX = NamePrefix1Code.MADM;
			break;
		default:
			return nmPr;

		}
		if (null == NMPRFX) {
			return null;
		}
		return NMPRFX.toString();
	}

	private String getAddrType(String val) {
		AddressType2Code ADRTYPE = null;
		switch (val) {
		case "ADDR":
			ADRTYPE = AddressType2Code.ADDR;
			break;
		case "PBOX":
			ADRTYPE = AddressType2Code.PBOX;
			break;
		case "HOME":
			ADRTYPE = AddressType2Code.HOME;
			break;
		case "BIZZ":
			ADRTYPE = AddressType2Code.BIZZ;
			break;
		case "MLTO":
			ADRTYPE = AddressType2Code.MLTO;
			break;
		case "DLVY":
			ADRTYPE = AddressType2Code.DLVY;
			break;
		default:
			return val;

		}
		if (null == ADRTYPE) {
			return null;
		}
		return ADRTYPE.toString();
	}

	private String getChequeType(String val) {
		ChequeType2Code code = null;
		switch (val) {
		case "CCHQ":
			code = ChequeType2Code.CCHQ;
			break;
		case "CCCH":
			code = ChequeType2Code.CCCH;
			break;
		case "BCHQ":
			code = ChequeType2Code.BCHQ;
			break;
		case "DRFT":
			code = ChequeType2Code.DRFT;
			break;
		case "ELDR":
			code = ChequeType2Code.ELDR;
			break;
		default:
			return val;

		}
		if (null == code) {
			return null;
		}
		return code.toString();
	}

	private String getRemittanceLocationMethod2Code(String val) {
		RemittanceLocationMethod2Code ADRTYPE = null;
		switch (val) {
		case "FAXI":
			ADRTYPE = RemittanceLocationMethod2Code.FAXI;
			break;
		case "EDIC":
			ADRTYPE = RemittanceLocationMethod2Code.EDIC;
			break;
		case "URID":
			ADRTYPE = RemittanceLocationMethod2Code.URID;
			break;
		case "EMAL":
			ADRTYPE = RemittanceLocationMethod2Code.EMAL;
			break;
		case "POST":
			ADRTYPE = RemittanceLocationMethod2Code.POST;
			break;
		case "SMSM":
			ADRTYPE = RemittanceLocationMethod2Code.SMSM;
			break;
		default:
			return val;

		}
		if (null == ADRTYPE) {
			return null;
		}
		return ADRTYPE.toString();
	}

	private String getInstruction3Code(String val) {
		Instruction3Code code = null;
		switch (val) {
		case "CHQB":
			code = Instruction3Code.CHQB;
			break;
		case "HOLD":
			code = Instruction3Code.HOLD;
			break;
		case "PHOB":
			code = Instruction3Code.PHOB;
			break;
		case "TELB":
			code = Instruction3Code.TELB;
			break;
		default:
			return val;

		}
		if (null == code) {
			return null;
		}
		return code.toString();
	}

	private String getDocumentType3Code(String val) {
		DocumentType3Code code = null;
		switch (val) {
		case "RADM":
			code = DocumentType3Code.RADM;
			break;
		case "RPIN":
			code = DocumentType3Code.RPIN;
			break;
		case "FXDR":
			code = DocumentType3Code.FXDR;
			break;
		case "DISP":
			code = DocumentType3Code.DISP;
			break;
		case "PUOR":
			code = DocumentType3Code.PUOR;
			break;
		case "SCOR":
			code = DocumentType3Code.SCOR;
			break;
		default:
			return val;
		}
		if (null == code) {
			return null;
		}
		return code.toString();
	}

	private String getTaxRecordPeriod1Code(String code) {
		TaxRecordPeriod1Code CODE = null;
		switch (code) {
		case "MM01":
			CODE = TaxRecordPeriod1Code.MM_01;
			break;
		case "MM02":
			CODE = TaxRecordPeriod1Code.MM_02;
			break;
		case "MM03":
			CODE = TaxRecordPeriod1Code.MM_03;
			break;
		case "MM04":
			CODE = TaxRecordPeriod1Code.MM_04;
			break;
		case "MM05":
			CODE = TaxRecordPeriod1Code.MM_05;
			break;
		case "MM06":
			CODE = TaxRecordPeriod1Code.MM_06;
			break;
		case "MM08":
			CODE = TaxRecordPeriod1Code.MM_08;
			break;
		case "MM09":
			CODE = TaxRecordPeriod1Code.MM_09;
			break;
		case "MM10":
			CODE = TaxRecordPeriod1Code.MM_10;
			break;
		case "MM11":
			CODE = TaxRecordPeriod1Code.MM_11;
			break;
		case "MM12":
			CODE = TaxRecordPeriod1Code.MM_12;
			break;
		case "QTR1":
			CODE = TaxRecordPeriod1Code.QTR_1;
			break;
		case "QTR2":
			CODE = TaxRecordPeriod1Code.QTR_2;
			break;
		case "QTR3":
			CODE = TaxRecordPeriod1Code.QTR_3;
			break;
		case "QTR4":
			CODE = TaxRecordPeriod1Code.QTR_4;
			break;
		default:
			return code;
		}
		if (null == CODE) {
			return null;
		}
		return CODE.toString();
	}

	private LocalDate getLocalDate(String localDt) {
		localDt = localDt.replace(" ", "T");
		return LocalDateTime.parse(localDt).toLocalDate();
	}

	private LocalDateTime getLocalDateTime(String localDt) {
		LocalDateTime localDateTime = null;
		try {
			localDateTime = LocalDateTime.parse(localDt, XorConstant.formateYYYYMMDD_HHMMSSZ);
		} catch (Exception e) {
			// logger.info("Date format should be on ERP Standards as "
			// XorConstant.formateYYYYMMDD_HHMMSSZ);
		}
		return localDateTime;
	}

	private boolean isTagPresent(Map<Integer, Set<Integer>> tagMap, Integer pid, Integer id) {
		if (null != pid && null != id && null != tagMap.get(pid)) {
			Set<Integer> tagSetForpid = tagMap.get(pid);
			return tagSetForpid.contains(id);
		}
		return false;
	}

	private boolean isTagPresent(Map<Integer, String> tagSeqMap, Integer id) {
		if (null != id && null != tagSeqMap.get(id)) {
			return true;
		}
		return false;
	}

}
