package com.xoriant.xorpay.paymentheaders;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.model.Tags;
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
public class PaymentInformation {

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
	CreditTransferTransactionInformation creditTransferTransactionInformation;

	/*
	 * @Autowired XmlRuleUtility xmlRuleUtil;
	 */

	TagCustomUtil tagCustomUtil = new TagCustomUtil();

	// private final Logger logger =
	// LoggerFactory.getLogger(PaymentInformation.class);
	private static final Logger logger = LoggerFactory.getLogger(PaymentInformation.class);

	public void getPmtInf(Document doc, String profilename) {

		logger.info(" PaymentInformation getPmtInf() execution started");
		boolean isCustomization = false;
		List<String> tagListToCheck = new ArrayList<String>();
		tagListToCheck.add(TagConstants.MAX_LENGTH);
		tagListToCheck.add(TagConstants.PIUID);
		tagListToCheck.add(TagConstants.COUNTRY);
		Map<String, String> pmtInfChilds = new HashMap<>();//xmlBuilderDao.getChild(TagConstants.PMT_INFO, piuidNum, cntryCode);
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

	public void setPmtInf(Document doc, Tags tags) {
		List<PaymentInstructionInformation3> PmtInflist = doc.getCstmrCdtTrfInitn().getPmtInf();
		for (PaymentInstructionInformation3 PmtInf : PmtInflist) {

			// PmtInflist.add(0, tags.getPmtInfId());
			logger.info("getPmtInfId " + tags.getPmtInfId());
			// String s = tags.getPmtInfId()+"-"+tags.getNbOfTxs();
			PmtInflist.get(0).setPmtInfId(tags.getPmtInfId());

			if ((tags.getPmtMtd().equalsIgnoreCase("Check")) || (tags.getPmtMtd().equalsIgnoreCase("CHK"))) {
				PmtInflist.get(0).setPmtMtd(PaymentMethod3Code.TRF.toString());
			} else if (tags.getPmtMtd().equals("TRF")) {
				PmtInflist.get(0).setPmtMtd(PaymentMethod3Code.TRF.toString());
			} else if (tags.getPmtMtd().equals("TRA")) {
				PmtInflist.get(0).setPmtMtd(PaymentMethod3Code.TRA.toString());
			} else if (tags.getPmtMtd().equals("EFT")) {
				PmtInflist.get(0).setPmtMtd(PaymentMethod3Code.EFT.toString());
			}

			if (tags.getSvcLvlCd() != null) {
				if (PmtInflist.get(0).getPmtTpInf().getSvcLvl() != null) {
					PmtInflist.get(0).getPmtTpInf().getSvcLvl().setCd(tags.getSvcLvlCd());
				}
			}
			if (tags.getCtgyPurpCd() != null) {
				if (PmtInflist.get(0).getPmtTpInf().getCtgyPurp() != null) {
					PmtInflist.get(0).getPmtTpInf().getCtgyPurp().setCd(tags.getCtgyPurpCd());
				}
			}

			// ReqdExctnDt
			if (logger.isDebugEnabled()) {
				logger.debug("ReqdExctnDt" + tags.getReqstExDt());
			}

			Timestamp ts = new Timestamp(tags.getReqstExDt().getTime());
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

			if (tags.getReqstExDt() != null) {
				//PmtInflist.get(0).setReqdExctnDt(cal);
			}

			if (tags.getDbtrNm() != null) {
				PmtInflist.get(0).getDbtr().setNm(tags.getDbtrNm());
			}

			if (tags.getDbtrAccntOthrId() != null) {
				PmtInflist.get(0).getDbtrAcct().getId().getOthr().setId(tags.getDbtrAccntOthrId());
			}

			if (tags.getDbtrAgntBic() != null) {
				PmtInflist.get(0).getDbtrAgt().getFinInstnId().setBIC(tags.getDbtrAgntBic());
			}

			if (tags.getDbtrAgntPstlCtry() != null) {
				if (PmtInflist.get(0).getDbtrAgt().getFinInstnId().getPstlAdr() != null) {
					PmtInflist.get(0).getDbtrAgt().getFinInstnId().getPstlAdr().setCtry(tags.getDbtrAgntPstlCtry());
				}
			}

			if (tags.getDbtrAgntBrnId() != null) {
				PmtInflist.get(0).getDbtrAgt().getBrnchId().setId(tags.getDbtrAgntBrnId());
			}

			//
			creditTransferTransactionInformation.setCdtTrfTxInf(doc, PmtInf, tags);

		}
	}

	public PaymentInstructionInformation3 setPmtInf(Document doc, AggregatedPaymentEntity tags) {
		// List<PaymentInstructionInformation3> PmtInflist =
		// doc.getCstmrCdtTrfInitn().getPmtInf();

		// for (PaymentInstructionInformation3 PmtInf : PmtInflist) {
		PaymentInstructionInformation3 PmtInf = new PaymentInstructionInformation3();
		// PmtInflist.add(0, tags.getPmtInfId());
		logger.info("getPmtInfId " + tags.getPmtInfId());
		// String s = tags.getPmtInfId()+"-"+tags.getNbOfTxs();
		// PmtInf.setPmtInfId(tags.getPmtInfId());
		PmtInf.setPmtInfId(tags.getEndToEndId());

		/*
		 * if ((tags.getPmtMtd().equalsIgnoreCase("Check")) ||
		 * (tags.getPmtMtd().equalsIgnoreCase("CHK"))) {
		 * PmtInf.setPmtMtd(PaymentMethod3Code.TRF); } else if
		 * (tags.getPmtMtd().equals("TRF")) { PmtInf.setPmtMtd(PaymentMethod3Code.TRF);
		 * } else if (tags.getPmtMtd().equals("TRA")) {
		 * PmtInf.setPmtMtd(PaymentMethod3Code.TRA); } else if
		 * (tags.getPmtMtd().equals("EFT")) { PmtInf.setPmtMtd(PaymentMethod3Code.EFT);
		 * }
		 */

		if (tags.getPmtMtd().equalsIgnoreCase("Check") || tags.getPmtMtd().equalsIgnoreCase("CHK")
				|| tags.getPmtMtd().equals("EFT") || tags.getPmtMtd().equals("WIRE") 
				|| tags.getPmtMtd().equals("TRA")) {
			PmtInf.setPmtMtd(PaymentMethod3Code.TRF.toString());
		}

		PaymentTypeInformation19 PmtTpInf = new PaymentTypeInformation19();
		boolean flagPmtInf = false;
		if (tags.getSvcLvlCd() != null) {
			ServiceLevel8Choice SvcLvl = new ServiceLevel8Choice();
			SvcLvl.setCd(tags.getSvcLvlCd());
			PmtTpInf.setSvcLvl(SvcLvl);
			flagPmtInf = true;
		}
		if (tags.getCtgyPurpCd() != null) {
			CategoryPurpose1Choice CtgyPurp = new CategoryPurpose1Choice();
			CtgyPurp.setCd(tags.getCtgyPurpCd());
			PmtTpInf.setCtgyPurp(CtgyPurp);
			flagPmtInf = true;
		}
		if (flagPmtInf) {
			PmtInf.setPmtTpInf(PmtTpInf);
		}
		// ReqdExctnDt
		if (logger.isDebugEnabled()) {
			logger.debug("ReqdExctnDt" + tags.getReqstExDt());
		}
		if (tags.getReqstExDt() != null) {
			PmtInf.setReqdExctnDt(tags.getReqstExDt().toLocalDate());
		}
		if (tags.getDbtrNm() != null) {
			PartyIdentification32 Dbtr = new PartyIdentification32();
			Dbtr.setNm(tags.getDbtrNm());
			PmtInf.setDbtr(Dbtr);
		}
		CashAccount16 DbtrAcct = new CashAccount16();
		boolean flagDbtrAcct = false;
		if (tags.getDbtrAccntOthrId() != null) {

			AccountIdentification4Choice id = new AccountIdentification4Choice();
			GenericAccountIdentification1 Othr = new GenericAccountIdentification1();

			Othr.setId(tags.getDbtrAccntOthrId());
			id.setOthr(Othr);
			DbtrAcct.setId(id);
			flagDbtrAcct = true;
		}
		if (null != tags.getCcyCode()) {
			DbtrAcct.setCcy(tags.getCcyCode());
			flagDbtrAcct = true;
		}
		if (flagDbtrAcct) {
			PmtInf.setDbtrAcct(DbtrAcct);
		}

		BranchAndFinancialInstitutionIdentification4 DbtrAgt = new BranchAndFinancialInstitutionIdentification4();
		FinancialInstitutionIdentification7 FinInstnId = new FinancialInstitutionIdentification7();
		boolean flagDbtrAgt = false;
		if (tags.getDbtrAgntBic() != null) {
			FinInstnId.setBIC(tags.getDbtrAgntBic());
			DbtrAgt.setFinInstnId(FinInstnId);
			flagDbtrAgt = true;
		}
		if (tags.getDbtrAgntPstlCtry() != null) {
			PostalAddress6 PstlAdr = new PostalAddress6();
			PstlAdr.setCtry(tags.getDbtrAgntPstlCtry());
			FinInstnId.setPstlAdr(PstlAdr);
			DbtrAgt.setFinInstnId(FinInstnId);
			flagDbtrAgt = true;
		}
		if (tags.getDbtrAgntBrnId() != null) {
			BranchData2 BrnchId = new BranchData2();
			BrnchId.setId(tags.getDbtrAgntBrnId());
			DbtrAgt.setBrnchId(BrnchId);
			flagDbtrAgt = true;
		}
		if (flagDbtrAgt) {
			PmtInf.setDbtrAgt(DbtrAgt);
		}
		//

		// creditTransferTransactionInformation.setCdtTrfTxInf(doc, PmtInf, tags);
		return PmtInf;
		// }

	}

}
