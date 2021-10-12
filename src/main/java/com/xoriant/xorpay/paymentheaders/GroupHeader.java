package com.xoriant.xorpay.paymentheaders;

import java.time.LocalDateTime;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.model.Tags;
import com.xoriant.xorpay.pojo001.Document;;

@Service
public class GroupHeader {


	private static final Logger logger = LoggerFactory.getLogger(GroupHeader.class);

	public void getGrpHdr(Document doc, String profilename) {
		//logger.info("GroupHeader getGrpHdr() Start... ");
		//Map<String, String> childs = xmlBuilderDao.getChild(TagConstants.GRPHEADER, piuidNum, cntryCode);

		//if (!childs.containsKey(TagConstants.GRPHDR_NMBTR)) {
		//	doc.getCstmrCdtTrfInitn().getGrpHdr().setNbOfTxs(null);
		//}

		//if (!childs.containsKey(TagConstants.GRPHDR_CRDTTIME)) {
		//	doc.getCstmrCdtTrfInitn().getGrpHdr().setCreDtTm(null);
		//}
		//logger.info("GroupHeader getGrpHdr() End... ");
	}

	public void setGrpHdr(Document doc, Tags tags) {
		// Tags tags = new Tags();
		logger.info("GroupHeader tagsMsgID " + tags.getMsgId());
		doc.getCstmrCdtTrfInitn().getGrpHdr().setMsgId(tags.getMsgId());
		logger.info("GroupHeader getCreDtTm() :::" + tags.getCretionDt());
		// date

		//Timestamp ts = new Timestamp(tags.getCretionDt().getTime());
		// ts.setNanos(123456789);

		//LocalDateTime ldt = ts.toLocalDateTime();

		//XMLGregorianCalendar cal = null;
		//try {
		//	cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		//} catch (DatatypeConfigurationException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}

//		cal.setYear(ldt.getYear());
//		cal.setMonth(ldt.getMonthValue());
//		cal.setDay(ldt.getDayOfMonth());
//		cal.setHour(ldt.getHour());
//		cal.setMinute(ldt.getMinute());
//		cal.setSecond(ldt.getSecond());
//		// cal.setFractionalSecond(new BigDecimal("0." + ldt.getNano()));

		//doc.getCstmrCdtTrfInitn().getGrpHdr().setCreDtTm(cal);
		doc.getCstmrCdtTrfInitn().getGrpHdr().setCreDtTm(LocalDateTime.now().toString());
		
		if(doc.getCstmrCdtTrfInitn().getGrpHdr().getNbOfTxs()!=null) {
			if(tags.getNbOfTxs()!=null) {
				doc.getCstmrCdtTrfInitn().getGrpHdr().setNbOfTxs(tags.getNbOfTxs());
			}
		}
		doc.getCstmrCdtTrfInitn().getGrpHdr().setCtrlSum(tags.getCntrlSum());
		logger.info("tags.getCntrlSum()::: "+tags.getCntrlSum());
		if(doc.getCstmrCdtTrfInitn().getGrpHdr().getCtrlSum()!=null) {
			if(tags.getCntrlSum()!=null) {
				logger.info(" CntrlSum in condition"+tags.getCntrlSum());
				doc.getCstmrCdtTrfInitn().getGrpHdr().setCtrlSum(tags.getCntrlSum());
			}
		}
		if(doc.getCstmrCdtTrfInitn().getGrpHdr().getFwdgAgt().getFinInstnId().getNm()!=null) {
			if(tags.getFwdAgntNm()!=null)
			{
				doc.getCstmrCdtTrfInitn().getGrpHdr().getFwdgAgt().getFinInstnId().setNm(tags.getFwdAgntNm());
			}
		}
		if(doc.getCstmrCdtTrfInitn().getGrpHdr().getInitgPty().getNm()!=null) {
			if(tags.getInitgNm()!=null) {
				doc.getCstmrCdtTrfInitn().getGrpHdr().getInitgPty().setNm(tags.getInitgNm());
			}
		}
	}
	public void setGrpHdr(Document doc, AggregatedPaymentEntity tags) {
		// Tags tags = new Tags();
		logger.info("GroupHeader tagsMsgID " + tags.getMsgId());
		doc.getCstmrCdtTrfInitn().getGrpHdr().setMsgId(tags.getMsgId());
		logger.info("GroupHeader getCreDtTm() :::" + tags.getCretionDt());
		// date
		
		//Timestamp ts = new Timestamp(tags.getCretionDt());
		// ts.setNanos(123456789);

		//LocalDateTime ldt = ts.toLocalDateTime();

		XMLGregorianCalendar cal = null;
		try {
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		///cal.setYear(ldt.getYear());
		///cal.setMonth(ldt.getMonthValue());
	//	cal.setDay(ldt.getDayOfMonth());
		//cal.setHour(ldt.getHour());
		//cal.setMinute(ldt.getMinute());
		//cal.setSecond(ldt.getSecond());
		// cal.setFractionalSecond(new BigDecimal("0." + ldt.getNano()));

		doc.getCstmrCdtTrfInitn().getGrpHdr().setCreDtTm(tags.getCretionDt().toString());
		if(doc.getCstmrCdtTrfInitn().getGrpHdr().getNbOfTxs()!=null) {
			if(tags.getNbOfTxs()!=null) {
				doc.getCstmrCdtTrfInitn().getGrpHdr().setNbOfTxs(""+tags.getNbOfTxs());
			}
		}
		doc.getCstmrCdtTrfInitn().getGrpHdr().setCtrlSum(tags.getCntrlSum());
		logger.info("tags.getCntrlSum()::: "+tags.getCntrlSum());
		if(doc.getCstmrCdtTrfInitn().getGrpHdr().getCtrlSum()!=null) {
			if(tags.getCntrlSum()!=null) {
				logger.info(" CntrlSum in condition"+tags.getCntrlSum());
				doc.getCstmrCdtTrfInitn().getGrpHdr().setCtrlSum(tags.getCntrlSum());
			}
		}
		if(doc.getCstmrCdtTrfInitn().getGrpHdr().getFwdgAgt().getFinInstnId().getNm()!=null) {
			if(tags.getFwdAgntNm()!=null)
			{
				doc.getCstmrCdtTrfInitn().getGrpHdr().getFwdgAgt().getFinInstnId().setNm(tags.getFwdAgntNm());
			}
		}
		if(doc.getCstmrCdtTrfInitn().getGrpHdr().getInitgPty().getNm()!=null) {
			if(tags.getInitgNm()!=null) {
				doc.getCstmrCdtTrfInitn().getGrpHdr().getInitgPty().setNm(tags.getInitgNm());
			}
		}
	}
}
