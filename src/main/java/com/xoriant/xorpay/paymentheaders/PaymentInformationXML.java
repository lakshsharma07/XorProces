package com.xoriant.xorpay.paymentheaders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.PAIN001Constant;
import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.paymentinforules.Debtor;
import com.xoriant.xorpay.paymentinforules.DebtorAccount;
import com.xoriant.xorpay.paymentinforules.DebtorAgent;
import com.xoriant.xorpay.paymentinforules.DebtorAgentAccount;
import com.xoriant.xorpay.paymentinforules.PaymentTypeInformation;
import com.xoriant.xorpay.pojo001.AccountIdentification4Choice;
import com.xoriant.xorpay.pojo001.BranchAndFinancialInstitutionIdentification4;
import com.xoriant.xorpay.pojo001.BranchData2;
import com.xoriant.xorpay.pojo001.CashAccount16;
import com.xoriant.xorpay.pojo001.CategoryPurpose1Choice;
import com.xoriant.xorpay.pojo001.Document;
import com.xoriant.xorpay.pojo001.FinancialInstitutionIdentification7;
import com.xoriant.xorpay.pojo001.GenericAccountIdentification1;
import com.xoriant.xorpay.pojo001.PartyIdentification32;
import com.xoriant.xorpay.pojo001.PaymentInstructionInformation3;
import com.xoriant.xorpay.pojo001.PaymentMethod3Code;
import com.xoriant.xorpay.pojo001.PaymentTypeInformation19;
import com.xoriant.xorpay.pojo001.PostalAddress6;
import com.xoriant.xorpay.pojo001.ServiceLevel8Choice;
import com.xoriant.xorpay.util.TagCustomUtil;

@Service
public class PaymentInformationXML {

	@Autowired
	PaymentTypeInformation paymentTypeInformation;

	@Autowired
	Debtor debtor;

	@Autowired
	DebtorAccount debtorAccount;

	@Autowired
	DebtorAgent debtorAgent;

	@Autowired
	DebtorAgentAccount debtorAgentAccount;

	@Autowired
	CreditTransferTransactionInformationXML creditTransferTransactionInformation;

	/*
	 * @Autowired XmlRuleUtility xmlRuleUtil;
	 */

	TagCustomUtil tagCustomUtil = new TagCustomUtil();

	// private final Logger logger =
	// LoggerFactory.getLogger(PaymentInformation.class);
	private static final Logger logger = LoggerFactory.getLogger(PaymentInformationXML.class);

	public void getPmtInf(Document doc, String piuidNum, String cntryCode) {

		logger.info(" PaymentInformation getPmtInf() execution started");
		List<String> tagListToCheck = new ArrayList<String>();
		tagListToCheck.add(TagConstants.MAX_LENGTH);
		tagListToCheck.add(TagConstants.PIUID);
		tagListToCheck.add(TagConstants.COUNTRY);
		Map<String, String> pmtInfChilds = new HashMap<>();
		if (null != doc.getCstmrCdtTrfInitn().getPmtInf()) {
			List<PaymentInstructionInformation3> PmtInflist = doc.getCstmrCdtTrfInitn().getPmtInf();
			List<PaymentInstructionInformation3> updatedPmtInflist = new ArrayList<PaymentInstructionInformation3>();

			for (PaymentInstructionInformation3 PmtInf : PmtInflist) {

				paymentTypeInformation.getPaymentTypeInformation(PmtInf, pmtInfChilds);

				debtor.getDbtr(PmtInf, pmtInfChilds);

				debtorAccount.getDbtrAcct(PmtInf, pmtInfChilds);

				debtorAgent.getDbtrAgt(PmtInf, pmtInfChilds);

				debtorAgentAccount.getDbtrAgtAcct(PmtInf, pmtInfChilds);

				if (PmtInf.getChrgBr() != null && !pmtInfChilds.containsKey(TagConstants.PMT_INFO_CHRGBR)) {
					PmtInf.setChrgBr(null);
				}

				/* TODO ChrgsAcct */
				/* TODO ChrgsAcctAgt */

				creditTransferTransactionInformation.getCdtTrfTxInf(PmtInf, pmtInfChilds);

				updatedPmtInflist.add(PmtInf);
			}

			doc.getCstmrCdtTrfInitn().getPmtInf().clear();
			doc.getCstmrCdtTrfInitn().getPmtInf().addAll(updatedPmtInflist);
		}
	}

	public PaymentInstructionInformation3 setPmtInf(Document doc, AggregatedPaymentEntity tags,
			Map<Integer, String> tagSeqMap) {

		PaymentInstructionInformation3 PmtInf = new PaymentInstructionInformation3();
		PaymentInstructionInformation3 docPmtInf = doc.getCstmrCdtTrfInitn().getPmtInf().get(0);

		if (null != docPmtInf.getPmtInfId() && null != tagSeqMap.get(PAIN001Constant.PMTINFID_10)) {
			PmtInf.setPmtInfId(tagSeqMap.get(PAIN001Constant.PMTINFID_10));
		}
		if (null != docPmtInf.getPmtMtd() && null != tagSeqMap.get(PAIN001Constant.PMTMTD_11)) {
			String pmtMethod = tagSeqMap.get(PAIN001Constant.PMTMTD_11);
			if (pmtMethod.equalsIgnoreCase("Check") || pmtMethod.equalsIgnoreCase("CHK") || pmtMethod.equals("EFT")
					|| pmtMethod.equals("WIRE") || pmtMethod.equals("TRA")) {
				PmtInf.setPmtMtd(PaymentMethod3Code.TRF.toString());
			}
		}

		if (null != docPmtInf.getPmtTpInf()) {
			/**
			 * "instrPrty", "svcLvl", "lclInstrm", "ctgyPurp"
			 */
			docPmtInf.getPmtTpInf();
			setPmtTpInf(tags, PmtInf, docPmtInf.getPmtTpInf(), tagSeqMap);

		}
		if (null != docPmtInf.getReqdExctnDt() && tags.getReqstExDt() != null) {
			PmtInf.setReqdExctnDt(tags.getReqstExDt().toLocalDate());
		}
		if (null != docPmtInf.getDbtr() && tags.getDbtrNm() != null) {
			setDbtr(tags, PmtInf, docPmtInf);
		}
		if (null != docPmtInf.getDbtrAcct()) {
			setDbtrAcct(tags, PmtInf, docPmtInf);
		}
		if (null != docPmtInf.getDbtrAgt()) {
			setDbtrAgt(tags, PmtInf, docPmtInf);
		}
		return PmtInf;

	}

	private void setPmtTpInf(AggregatedPaymentEntity tags, PaymentInstructionInformation3 PmtInf,
			PaymentTypeInformation19 docPymtTypeInf19, Map<Integer, String> tagSeqMap) {
		PaymentTypeInformation19 PmtTpInf = new PaymentTypeInformation19();
		boolean flagPmtInf = false;

		if (null != docPymtTypeInf19.getSvcLvl() && tags.getSvcLvlCd() != null) {
			ServiceLevel8Choice SvcLvl = new ServiceLevel8Choice();
			if (null != docPymtTypeInf19.getSvcLvl().getCd()) {
				SvcLvl.setCd(tags.getSvcLvlCd());
			}
			PmtTpInf.setSvcLvl(SvcLvl);
			flagPmtInf = true;
		}
		if (null != docPymtTypeInf19.getCtgyPurp() && tags.getCtgyPurpCd() != null) {
			CategoryPurpose1Choice CtgyPurp = new CategoryPurpose1Choice();
			if (null != docPymtTypeInf19.getCtgyPurp().getCd()) {
				CtgyPurp.setCd(tags.getCtgyPurpCd());
			}
			PmtTpInf.setCtgyPurp(CtgyPurp);
			flagPmtInf = true;
		}
		if (flagPmtInf) {
			PmtInf.setPmtTpInf(PmtTpInf);
		}
	}

	private void setDbtrAgt(AggregatedPaymentEntity tags, PaymentInstructionInformation3 PmtInf,
			PaymentInstructionInformation3 xsdPmtInf) {
		BranchAndFinancialInstitutionIdentification4 DbtrAgt = new BranchAndFinancialInstitutionIdentification4();
		boolean flagDbtrAgt = false;
		if (null != xsdPmtInf.getDbtrAgt().getFinInstnId()) {
			flagDbtrAgt = true;
			DbtrAgt.setFinInstnId(setFinInstnId(tags, xsdPmtInf));
		}
		if (null != xsdPmtInf.getDbtrAgt().getBrnchId() && tags.getDbtrAgntBrnId() != null) {
			setBrnchId(tags, xsdPmtInf, DbtrAgt);
			flagDbtrAgt = true;
		}
		if (flagDbtrAgt) {
			PmtInf.setDbtrAgt(DbtrAgt);
		}
	}

	private void setBrnchId(AggregatedPaymentEntity tags, PaymentInstructionInformation3 xsdPmtInf,
			BranchAndFinancialInstitutionIdentification4 DbtrAgt) {
		BranchData2 BrnchId = new BranchData2();
		if (null != xsdPmtInf.getDbtrAgt().getBrnchId().getId()) {
			BrnchId.setId(tags.getDbtrAgntBrnId());
		}
		DbtrAgt.setBrnchId(BrnchId);
	}

	private FinancialInstitutionIdentification7 setFinInstnId(AggregatedPaymentEntity tags,
			PaymentInstructionInformation3 xsdPmtInf) {
		FinancialInstitutionIdentification7 FinInstnId = new FinancialInstitutionIdentification7();

		if (null != xsdPmtInf.getDbtrAgt().getFinInstnId().getBIC() && tags.getDbtrAgntBic() != null) {
			FinInstnId.setBIC(tags.getDbtrAgntBic());

		}
		if (null != xsdPmtInf.getDbtrAgt().getFinInstnId().getPstlAdr() && tags.getDbtrAgntPstlCtry() != null) {
			FinInstnId.setPstlAdr(setPstlAdr(tags, xsdPmtInf));
		}
		return FinInstnId;
	}

	private PostalAddress6 setPstlAdr(AggregatedPaymentEntity tags, PaymentInstructionInformation3 xsdPmtInf) {
		PostalAddress6 PstlAdr = new PostalAddress6();
		if (null != xsdPmtInf.getDbtrAgt().getFinInstnId().getPstlAdr().getCtry()) {
			PstlAdr.setCtry(tags.getDbtrAgntPstlCtry());
		}
		return PstlAdr;
	}

	private void setDbtrAcct(AggregatedPaymentEntity tags, PaymentInstructionInformation3 PmtInf,
			PaymentInstructionInformation3 xsdPmtInf) {
		CashAccount16 DbtrAcct = new CashAccount16();
		boolean flagDbtrAcct = false;
		if (null != xsdPmtInf.getDbtrAcct().getId() && tags.getDbtrAccntOthrId() != null) {

			DbtrAcct.setId(setId(tags, xsdPmtInf));
			flagDbtrAcct = true;
		}
		if (null != tags.getCcyCode()) {
			DbtrAcct.setCcy(tags.getCcyCode());
			flagDbtrAcct = true;
		}
		if (flagDbtrAcct) {
			PmtInf.setDbtrAcct(DbtrAcct);
		}
	}

	private AccountIdentification4Choice setId(AggregatedPaymentEntity tags, PaymentInstructionInformation3 xsdPmtInf) {
		AccountIdentification4Choice id = new AccountIdentification4Choice();
		if (null != xsdPmtInf.getDbtrAcct().getId().getOthr()) {
			GenericAccountIdentification1 Othr = new GenericAccountIdentification1();
			if (null != xsdPmtInf.getDbtrAcct().getId().getOthr().getId()) {
				Othr.setId(tags.getDbtrAccntOthrId());
			}
			id.setOthr(Othr);
		}
		return id;
	}

	private void setDbtr(AggregatedPaymentEntity tags, PaymentInstructionInformation3 PmtInf,
			PaymentInstructionInformation3 xsdPmtInf) {
		PartyIdentification32 Dbtr = new PartyIdentification32();
		if (null != xsdPmtInf.getDbtr().getNm()) {
			Dbtr.setNm(tags.getDbtrNm());
		}
		PmtInf.setDbtr(Dbtr);
	}

}
