package com.xoriant.xorpay.data.sync.repo;

import java.math.BigInteger;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.HibernateError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.data.sync.entity.DynamicNrmlzColumnEntity;
import com.xoriant.xorpay.entity.NormalizeAggXmlTagsMapEntity;
import com.xoriant.xorpay.entity.PayBatchSummaryEntity;
import com.xoriant.xorpay.entity.PaymentAksDetailEntity;
import com.xoriant.xorpay.entity.PaymentBatchAksDetailEntity;
import com.xoriant.xorpay.entity.SourceSysEntity;
import com.xoriant.xorpay.pojo.ColPojo;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.pojo.PaymentErrorPojo;
import com.xoriant.xorpay.repo.NormalizeAggXmlTagsMapRepo;
import com.xoriant.xorpay.service.DashboardDetailsService;
import com.xoriant.xorpay.service.PaymentXMLService;

@Repository
@Transactional
public class CommonJPARepo {

	@Autowired
	private PaymentXMLService paymentXMLService;
	@Autowired
	private DashboardDetailsService dashboardService;

	@Autowired
	private NormalizeAggXmlTagsMapRepo normalizeAggXmlTagsMapRepo;
	@Autowired
	private DynamicNrmlzColumnRepo dynamicNrmlzColumnRepo;

	private final Logger logger = LoggerFactory.getLogger(CommonJPARepo.class);

	public Map<Integer, Map<String, String>> piuidSeqIdColMap = new HashMap<>();
	public Map<String, String> nrmlzSourceMap = new HashMap<>();
	public String colPaymentIdSource, colBatch, colNoOftxn, colPartyName, colPaymentDate, colProfileName, colCurrency,
			colTransferDt, colInvoiceDt, colInstdAmt;
	public String nrmlzProfile;

	public void loadPaymentDetails(boolean newpayment, String internalUUID, PaymentErrorPojo pmyt, String comments,
			EntityManager entityManager, Query query, SourceSysEntity srcSys, Set<String> endToEndIdSet,
			String staticType, Set<String> loadmessageIdSet, Set<String> loadendToEndIdSet, Set<String> loadInvoiceSet,
			String batchcomments, ConfigPojo configPojo, Map<String, ColPojo> colProfileMap) {

		String internalId = pmyt.getInternalId();
		String batchid = pmyt.getMsgId();
		String endToEndId = pmyt.getEndToEndId();
		String invoiceNo = pmyt.getInvoiceNo();
		String instdAmt = pmyt.getInstdAmt();
		String currency = pmyt.getCcy();
		String transferDt = pmyt.getTransferDt();
		String paymentcomments = pmyt.getComment();
		String profileName = pmyt.getProfileName();

		String NoOfPayment = pmyt.getNoOfPayment();
		String creatioDate = pmyt.getCreationDate();
		String invoiceDate = pmyt.getInvoiceDt();

		String controlSum = pmyt.getControlSum();
		String invoiceAmt = pmyt.getInvoiceAmt();

		Integer srcId = srcSys.getId();

		String paymentIdKey = null;
		if (null != batchid) {
			paymentIdKey = batchid + "" + endToEndId;
		}
		logger.info("creatioDate -- " + creatioDate + " Load for status " + newpayment);
		String ineternalColName = configPojo.getSOURCE_TABLE_IDENTITY_COLUMN_NAME();
		if (newpayment) {

			ColPojo colPOjo = colProfileMap.get(profileName);
			if (null != batchid && loadmessageIdSet.add(batchid)) {
				payBatchSummaryInsert(internalUUID, internalId, batchid, endToEndId, invoiceNo, entityManager, query,
						srcSys, ineternalColName, colPOjo, configPojo, creatioDate, controlSum);

				xpPaymentDetails(internalUUID, internalId, batchid, endToEndId, invoiceNo, entityManager, query, srcSys,
						ineternalColName, colPOjo, configPojo, creatioDate, instdAmt);

				xpPaybatchAckDetails(internalUUID, internalId, batchid, staticType, entityManager, query, srcSys,
						ineternalColName, colPOjo, configPojo, batchcomments);
			}
			// logger.info("loadInvoiceSet -" + loadInvoiceSet.toString());
			if (null != invoiceNo) {
				xpPayInvoice(internalUUID, internalId, batchid, entityManager, query, srcSys, ineternalColName, colPOjo,
						configPojo, invoiceDate, instdAmt);
			}
			addPaymentAckDetails(internalUUID, loadmessageIdSet, endToEndIdSet, staticType, batchcomments, batchid,
					endToEndId, instdAmt, currency, transferDt, paymentcomments, NoOfPayment, srcId, paymentIdKey);
		} else {

			addPaymentAckDetails(internalUUID, loadmessageIdSet, endToEndIdSet, staticType, batchcomments, batchid,
					endToEndId, instdAmt, currency, transferDt, paymentcomments, NoOfPayment, srcId, paymentIdKey);
		}
	}

	private void addPaymentAckDetails(String internalUUID, Set<String> loadmessageIdSet, Set<String> endToEndIdSet,
			String staticType, String batchcomments, String batchid, String endToEndId, String instdAmt,
			String currency, String transferDt, String paymentcomments, String NoOfPayment, Integer srcId,
			String paymentIdKey) {
		logger.info(internalUUID + " staticType " + staticType + " paymentIdKey " + paymentIdKey);
		// logger.info(
		// endToEndId + " endToEndIdSet " + endToEndIdSet.toString() + " paymentcomments
		// " + paymentcomments);
		if (null != staticType && null != paymentIdKey) {
			if (endToEndIdSet.add(paymentIdKey)) {
				PaymentAksDetailEntity paymentAksDetail = logPaymntAckDetailError(internalUUID, batchid, endToEndId,
						instdAmt, currency, transferDt, paymentcomments, endToEndId, srcId, NoOfPayment, staticType);
				paymentAksDetail.setId(0);
				saveAllPaymentDetails(paymentAksDetail, srcId, staticType);

			} else {
				saveCommentForPayment(internalUUID, endToEndId, paymentcomments);
			}
			if (loadmessageIdSet.add(batchid)) {
				PaymentBatchAksDetailEntity paymentBatcbDetail = logPaymntBatchDetailError(internalUUID, batchid,
						batchcomments, srcId, NoOfPayment, staticType);
				saveAllPaymnetBatchBeatails(paymentBatcbDetail);
			}
		}
	}

	public PaymentBatchAksDetailEntity logPaymntBatchDetailError(String internalUUID, String msgId, String comment,
			Integer sourceSystem, String noOfPayment, String staticType) {
		PaymentBatchAksDetailEntity paymentBatchAksDetail = new PaymentBatchAksDetailEntity();
		paymentBatchAksDetail.setId(null);
		paymentBatchAksDetail.setInternalUuId(internalUUID);
		if (msgId != null) {
			paymentBatchAksDetail.setPaymentBatchAckID(
					msgId + "_" + LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		}
		if (msgId != null) {
			paymentBatchAksDetail.setPaymentInstructionId(msgId);
		}

		String sts = XorConstant.NEW;
		String fsts = XorConstant.NEW;
		if (staticType.equalsIgnoreCase(XorConstant.FAILED)) {
			sts = XorConstant.FAIL;
			fsts = XorConstant.FAILED;
		}

		paymentBatchAksDetail.setAckStatus(sts);
		paymentBatchAksDetail.setFileName(null);
		paymentBatchAksDetail.setCreatedDate(LocalDateTime.now());
		paymentBatchAksDetail.setLastUpdateDate(LocalDateTime.now());
		if (paymentBatchAksDetail.getCreatedBy() == null)
			paymentBatchAksDetail.setCreatedBy("1000");
		if (paymentBatchAksDetail.getLastUpdatedBy() == null)
			paymentBatchAksDetail.setLastUpdatedBy("1000");

		if (comment != null) {
			paymentBatchAksDetail.setAdditionalInfo(comment);
		}
		paymentBatchAksDetail.setFileStatus(fsts);

		return paymentBatchAksDetail;

	}

	public PaymentAksDetailEntity logPaymntAckDetailError(String internalId, String msgId, String endToEndId,
			String instdAmt, String ccy, String reqdExctnDt, String comment, String pmtInfId, Integer sourceSystem,
			String noOfPayment, String staticType) {

		PaymentAksDetailEntity paymentAksDetail = new PaymentAksDetailEntity();
		paymentAksDetail.setId(null);
		paymentAksDetail.setInternalUuid(internalId);
		paymentAksDetail.setCreatedBy("1000");
		Date date = new Date();
		paymentAksDetail.setCreatedDate(date);
		if (msgId != null) {
			paymentAksDetail.setAckId(msgId + "_" + endToEndId + "_"
					+ LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

			logger.info("ids " + paymentAksDetail.getAckId());
		}
		if (msgId != null) {
			paymentAksDetail.setPaymentInstructionId(msgId);
		}
		String sts = XorConstant.NEW;
		if (staticType.equalsIgnoreCase(XorConstant.FAILED)) {
			sts = XorConstant.FAIL;
		}

		paymentAksDetail.setAckStatus(sts);
		paymentAksDetail.setFileName(null);
		if (instdAmt != null && !instdAmt.isEmpty()) {
			paymentAksDetail.setPaymentAmount(Double.parseDouble(instdAmt));
			// document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0)
			// .getTxInfAndSts().get(0).getOrgnlTxRef().getAmt().getInstdAmt().getValue().toString());
		}
		if (ccy != null) {
			paymentAksDetail.setPaymentCurrencCode(ccy);
		}
		// document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0).getOrgnlTxRef()
		// .getReqdExctnDt()
		if (reqdExctnDt != null) {
			Date paymentDate = null;
			try {
				paymentDate = new SimpleDateFormat("yyyy-MM-dd").parse(reqdExctnDt);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			paymentAksDetail.setPaymentDate(paymentDate);
		}
		paymentAksDetail.setPaymentStatus(sts);

		if (comment != null) {
			paymentAksDetail.setAddInfo(comment);
		}

		paymentAksDetail.setLastUpdateDate(date);
		if (paymentAksDetail.getCreatedBy() == null)
			paymentAksDetail.setCreatedBy("1000");
		if (paymentAksDetail.getLastUpdatedBy() == null)
			paymentAksDetail.setLastUpdatedBy("1000");

		if (endToEndId != null) {
			paymentAksDetail.setPaymentNumber(endToEndId);
		}

		// document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getOrgnlPmtInfId()
		if (pmtInfId != null) {
			paymentAksDetail.setLogicalGrpRef(pmtInfId);
		}
		return paymentAksDetail;

	}

	public void saveAllPaymentDetails(PaymentAksDetailEntity paymentAksDetail, Integer srcId, String staticType) {

		try {
			paymentXMLService.savePaymentAckDetails(paymentAksDetail);
			logger.debug(" row inserted.");
			int noOfTx = 1;
			logger.info("*********************** --> " + noOfTx);
			dashboardService.updateDashboardStatics(srcId, noOfTx, staticType, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveCommentForPayment(String internalUUID, String endToEndId, String paymentcomments) {

		try {
			paymentXMLService.savePaymentAckDetails(internalUUID, endToEndId, paymentcomments);
			logger.debug(" row inserted.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveAllPaymnetBatchBeatails(PaymentBatchAksDetailEntity paymentBatcbDetail) {

		try {
			paymentXMLService.savePaymentBatchDetails(paymentBatcbDetail);
			logger.debug("batch row inserted.");
		} catch (Exception e) {
			logger.info("Payment Batch loading issue " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void payBatchSummaryInsert(String internalUuid, String internalId, String messageId, String endToEndId,
			String invoiceNo, EntityManager entityManager, Query query, SourceSysEntity srcSys, String ineternalColName,
			ColPojo colPojo, ConfigPojo configPojo, String creatioDate, String controlSum) {

		StringBuilder batchInsert = null;
		StringBuilder where = null;
		try {
			where = new StringBuilder(" where " + colPojo.getColBatch() + "  in ('" + messageId
					+ "')  and internal_uuid ='" + internalUuid + "' ");
			batchInsert = getPayBatchInsertStmt(internalUuid, where, configPojo.getSOURCE_TABLE_NAME(), srcSys, colPojo,
					creatioDate, controlSum, configPojo.getSOURCE_TABLE_IDENTITY_COLUMN_NAME());
			logger.info("batch summury " + batchInsert);
			query = entityManager.createNativeQuery(batchInsert.toString());
			query.executeUpdate();
			entityManager.flush();
			entityManager.clear();

		} catch (HibernateError ex) {
			logger.info("insserXpPayBatchSummury " + batchInsert);
			ex.printStackTrace();
		}
	}

	private void xpPaymentDetails(String internalUuid, String internalId, String messageId, String endToEndId,
			String invoiceNo, EntityManager entityManager, Query query, SourceSysEntity srcSys, String ineternalColName,
			ColPojo colPojo, ConfigPojo configPojo, String creatioDate,String instdAmt) {
		StringBuilder insertStmt = null;
		StringBuilder where = null;
		try {
			where = new StringBuilder(" where " + colPojo.getColBatch() + "  in('" + messageId
					+ "') and internal_uuid ='" + internalUuid + "' ");
			insertStmt = getPaymntDetailsInsertStmt(internalUuid, where, configPojo.getSOURCE_TABLE_NAME(), srcSys,
					colPojo, creatioDate, configPojo.getSOURCE_TABLE_IDENTITY_COLUMN_NAME(), instdAmt);
			logger.info("insert xpPaymentDetails " + insertStmt.toString());
			query = entityManager.createNativeQuery(insertStmt.toString());
			query.executeUpdate();
			entityManager.flush();
			entityManager.clear();

		} catch (HibernateError ex) {
			logger.info("xpPaymentDetails Exception " + insertStmt);
			ex.printStackTrace();
		}

		logger.info("Payment Details :: Payment Loaded Successfull");
	}

	private void xpPayInvoice(String internalUuid, String internalId, String messageId, EntityManager entityManager,
			Query query, SourceSysEntity srcSys, String ineternalColName, ColPojo colPOjo, ConfigPojo configPojo,
			String invoiceDate, String instdAmt) {

		StringBuilder insertStmt = null;
		StringBuilder where = null;

		try {
			where = new StringBuilder(" where " + ineternalColName + "  in(" + internalId + ")");
			// where = new StringBuilder(" message_id in('" + messageId + "') and ");
			insertStmt = getPayInvoice(internalUuid, where, configPojo.getSOURCE_TABLE_NAME(), srcSys, colPOjo,
					invoiceDate, instdAmt);
			logger.info("Payment Invoice  loading for " + messageId + "  with query :: " + insertStmt);
			query = entityManager.createNativeQuery(insertStmt.toString());
			query.executeUpdate();
			entityManager.flush();
			entityManager.clear();
			logger.info("Payment Invoice  loaded");

		} catch (HibernateError ex) {
			logger.info("xpPayInvoice Exceptio" + insertStmt);
			ex.printStackTrace();
		}

		logger.info("Pay Invoice :: Payment Loaded Successfull");
	}

	private void xpPaybatchAckDetails(String internalUuid, String internalId, String messageId, String staticType,
			EntityManager entityManager, Query query, SourceSysEntity srcSys, String ineternalColName, ColPojo colPOjo,
			ConfigPojo configPojo, String batchcomments) {
		StringBuilder insertStmt = null;
		StringBuilder where = null;
		try {
			where = new StringBuilder(" where " + ineternalColName + "  in(" + internalId + ")");
			String fileStatus = "";
			// where = new StringBuilder(" message_id in('" + messageId + "') and ");
			if (null != staticType && staticType.equalsIgnoreCase(XorConstant.FAILED)) {
				staticType = XorConstant.FAIL;
				fileStatus = XorConstant.FAILED;
			} else {
				batchcomments = XorConstant.NEW;
				staticType = XorConstant.NEW;
				fileStatus = XorConstant.NEW;
			}
			insertStmt = getPaybatchAckDetails(internalUuid, where, configPojo.getSOURCE_TABLE_NAME(), srcSys, colPOjo,
					staticType, batchcomments, fileStatus);
			logger.info("Payment Paybatcg ack  loading for " + messageId + "  with query :: " + insertStmt);
			query = entityManager.createNativeQuery(insertStmt.toString());
			query.executeUpdate();
			entityManager.flush();
			entityManager.clear();
			logger.info("Payment Paybatcg ack  loaded");

		} catch (HibernateError ex) {
			logger.info("xpPayInvoice Exceptio" + insertStmt);
			ex.printStackTrace();
		}

		logger.info("Paybatch Ack Details :: Payment Loaded Successfull");
	}

	private StringBuilder getPayBatchInsertStmt(String internal_uuid, StringBuilder where, String tableName,
			SourceSysEntity srcSys, ColPojo colPojo, String creatioDate, String controlSum, String internalId) {
		String payDtCol = null, conTrSum = null;
		if (null != creatioDate) {
			payDtCol = colPojo.getColPaymentDate();
		} else {
			payDtCol = "NULL";
		}
		if (null != controlSum) {
			conTrSum = colPojo.getColContrlSum();
		} else {
			conTrSum = "NULL";
		}
		StringBuilder condition = new StringBuilder("INSERT INTO xp_pay_batch_summary "
				+ "            (internal_uuid,PAYMENT_INSTRUCTION_ID, PAYMENT_COUNT, PAYMENT_AMOUNT, ORG_NAME,    "
				+ "             PAYMENT_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY, PROCESS_RUN_DATE "
				+ "             , PROFILENAME, ERP_SRC_SYS, SRC_SYSTEM, PAYMENT_CURRENCY_CODE, error_payment_date, error_payment_amount) "
				+ " SELECT    " + "                 '" + internal_uuid + "',"
				+ "				  f.message_id   as PAYMENT_INSTRUCTION_ID, "
				+ "                 f.no_of_transfers as PAYMENT_COUNT,"
				+ "                f.control_sum as PAYMENT_AMOUNT," + "				 f.company_name  as ORG_NAME,"
				+ "                  f.created_date_time  as PAYMENT_DATE, "
				+ "                 localtimestamp as CREATION_DATE," + "				 '" + XorConstant.ADMIN_ROLE
				+ "'   CREATED_BY," + "				 localtimestamp as LAST_UPDATE_DATE, " + "                '"
				+ XorConstant.ADMIN_ROLE + "'   LAST_UPDATED_BY,"
				+ "                 localtimestamp as PROCESS_RUN_DATE,"
				+ "                  f.payment_process_profile as PROFILENAME," + "				  " + srcSys.getId()
				+ "  erp_src_sys, " + "                '" + srcSys.getSrcSystemSort() + "'  src_system ,"
				+ "				  f.currency_code "
				+ " as  PAYMENT_CURRENCY_CODE, f.error_payment_date as error_payment_date, f.error_payment_amount error_payment_amount"
				+ "			  FROM (   select i.message_id, i.no_of_transfers, i.control_sum, i.company_name, TIMESTAMP(cast(i.created_date_time as datetime)) created_date_time, i.currency_code, i.payment_process_profile, i.error_payment_date, i.error_payment_amount"
				+ "                       from ( SELECT"
				+ "									DENSE_RANK() OVER( PARTITION BY " + colPojo.getColBatch()
				+ " ORDER BY " + colPojo.getColBatch() + " ," + colPojo.getColPaymentDate() + ", " + internalId
				+ " DESC) AS rnk," + "									" + colPojo.getColBatch() + "  ,"
				+ "									max(" + colPojo.getColNoOftxn() + ")   no_of_transfers ,"
				+ "									max(" + conTrSum + ")       control_sum,"
				+ "									max(" + colPojo.getColCompanyName() + ")      company_name,"
				+ "									max(" + payDtCol + ") created_date_time,"
				+ "									max(" + colPojo.getColProfileName() + ") payment_process_profile,"
				+ "									max(" + colPojo.getColCurrency()
				+ ")                currency_code, max(" + colPojo.getColPaymentDate() + ") error_payment_date, "
				+ "    max(" + colPojo.getColContrlSum() + ") error_payment_amount "
				+ "								FROM " + tableName + "  " + where
				+ "								group by " + colPojo.getColBatch() + " ," + colPojo.getColPaymentDate()
				+ ", " + internalId + ") AS i " + "					   where i.rnk = 1" + "					) f ");
		return condition;
	}

	private StringBuilder getPaymntDetailsInsertStmt(String internalUuid, StringBuilder condition, String tableName,
			SourceSysEntity srcSys, ColPojo colPojo, String creatioDate, String internalId, String instdAmt) {
		String payDtCol = null;
		if (null != creatioDate) {
			payDtCol = colPojo.getColPaymentDate();
		} else {
			payDtCol = "NULL";
		}
		
		String instdAMt = null;
		if (null != instdAmt && !instdAmt.trim().isEmpty()) {
			instdAMt = colPojo.getColInstdAmt();
		} else {
			instdAMt = "NULL";
		}
		
		return new StringBuilder("INSERT INTO xp_payment_details  "
				+ "            (internal_uuid, PAYMENT_INSTRUCTION_ID, PAYMENT_NUMBER, PAYMENT_AMOUNT,PAYMENT_CURRENCY_CODE, PAYMENT_METHOD_CODE, "
				+ "            SUPPLIER_NAME, PAYMENT_ID, CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY, "
				+ "				PAYMENT_PROFILE_NAME,PIUID, "
				+ "            FILE_STATUS, CHECK_ID,LOGICAL_GROUP_REFERENCE, ORG_NAME, PAYMENT_DATE, ERP_SRC_SYS,"
				+ "  SRC_SYSTEM, error_payment_date) " + "  select  '" + internalUuid + "', "
				+ "  i.payment_instruction_id, i.payment_number, i.payment_amount, i.payment_currency_code, i.payment_method_code, i.supplier_name"
				+ ", i.payment_id, i.creation_date, i.created_by, i.last_update_date, i.last_updated_by, i.payment_process_profile, i.piuid"
				+ ", i.file_status, i.check_id, i.logical_group_reference, i.org_name,  TIMESTAMP(cast(i.payment_date as datetime)) , i.erp_src_sys, i.src_system ,i.error_payment_date"
				+ " from (                    	SELECT  " + "            DENSE_RANK() OVER( PARTITION BY "
				+ colPojo.getColBatch() + ", " + colPojo.getColPaymentIdSource() + " ORDER BY " + colPojo.getColBatch()
				+ " ," + colPojo.getColPaymentIdSource() + ", " + payDtCol + ", " + internalId + " DESC) AS rnk,"
				+ "            " + colPojo.getColBatch() + "                	  	payment_instruction_id,"
				+ "            " + colPojo.getColPaymentIdSource() + "             	  	payment_number,"
				+ "            " + instdAMt + "     	  	payment_amount," + "            max("
				+ colPojo.getColCurrency() + ")        		  	OVER( PARTITION BY " + colPojo.getColBatch() + " ,"
				+ colPojo.getColPaymentIdSource() + ")  payment_currency_code,  " + "            max("
				+ colPojo.getColPaymentMethod() + ")  		  	OVER( PARTITION BY " + colPojo.getColBatch() + " ,"
				+ colPojo.getColPaymentIdSource() + ")  payment_method_code, " + "            max("
				+ colPojo.getColPartyName() + ")  		  	OVER( PARTITION BY " + colPojo.getColBatch() + " ,"
				+ colPojo.getColPaymentIdSource() + ")  supplier_name," + "            "
				+ colPojo.getColPaymentIdSource() + "             	  	payment_id,"
				+ "            localtimestamp            	  	creation_date,"
				+ "            'ADMIN' 				  	  	created_by,"
				+ "            localtimestamp            	  	last_update_date,"
				+ "            'ADMIN' 				   	  	last_updated_by," + "            max("
				+ colPojo.getColProfileName() + ")  	OVER( PARTITION BY " + colPojo.getColBatch() + " ,"
				+ colPojo.getColPaymentIdSource() + ") payment_process_profile," + "            max("
				+ colPojo.getColProfileName() + ")  	OVER( PARTITION BY " + colPojo.getColBatch() + " ,"
				+ colPojo.getColPaymentIdSource() + ") piuid," + "            concat('File', '_', "
				+ colPojo.getColBatch() + ") file_status," + "            " + colPojo.getColPaymentIdSource()
				+ "             	   	check_id," + "            " + colPojo.getColBatch()
				+ "                	   	logical_group_reference," + "            max(" + colPojo.getColCompanyName()
				+ ")  			   	OVER( PARTITION BY " + colPojo.getColBatch() + " ,"
				+ colPojo.getColPaymentIdSource() + ") org_name," + "            max(" + payDtCol
				+ ")  	   	OVER( PARTITION BY " + colPojo.getColBatch() + " ," + colPojo.getColPaymentIdSource()
				+ ") payment_date," + "            " + srcSys.getId() + " 								erp_src_sys,"
				+ "            '" + srcSys.getSrcSystemSort() + "' 							src_system , "
				+ colPojo.getColPaymentDate() + " error_payment_date " + "        FROM " + tableName + " " + condition
				+ "        group by " + colPojo.getColBatch() + ", " + colPojo.getColPaymentIdSource() + ", "
				+ colPojo.getColPaymentDate() + ", " + colPojo.getColInstdAmt() + ", " + internalId + " ) i"
				+ "   where i.rnk = 1");
	}

	private StringBuilder getPayInvoice(String internalUuid, StringBuilder condition, String tableName,
			SourceSysEntity srcSys, ColPojo colPojo, String invoiceDate, String instdAmt) {
		String invDtCol = null;
		if (null != invoiceDate) {
			invDtCol = colPojo.getColInvoiceDt();
		} else {
			invDtCol = "NULL";
		}
		String invAmt = null;
		if (null != instdAmt) {
			invAmt = colPojo.getColInvoiceAmt();
		} else {
			invAmt = "NULL";
		}
		
		return new StringBuilder("INSERT INTO xp_pay_invoices "
				+ "				            (internal_uuid, PAYMENT_ID, INVOICE_NUM, INVOICE_AMOUNT, INVOICE_CURRENCY_CODE, INVOICE_DATE, "
				+ "				             INVOICE_ID, CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY, PAYMENT_METHOD_CODE, SUPPLIER_NAME,error_invoice_date, error_invoice_amount) "
				+ "			 SELECT '" + internalUuid + "',           " + colPojo.getColPaymentIdSource()
				+ " PAYMENT_ID,  " + "		            " + colPojo.getColInvoiceNo() + " INVOICE_NUM, "
				+ "				     " + invAmt + "  INVOICE_AMOUNT,  " + "				            "
				+ colPojo.getColCurrency() + " INVOICE_CURRENCY_CODE,        " + " TIMESTAMP(cast(upper(" + invDtCol
				+ ") as datetime)) INVOICE_DATE, " + "				            " + colPojo.getColInvoiceNo()
				+ "  INVOICE_ID,            localtimestamp CREATION_DATE,  '" + XorConstant.ADMIN_ROLE
				+ "' CREATED_BY, localtimestamp LAST_UPDATE_DATE,  '" + XorConstant.ADMIN_ROLE + "' LAST_UPDATED_BY, "
				+ "				            " + colPojo.getColPaymentMethod()
				+ " PAYMENT_METHOD_CODE, recipient_name SUPPLIER_NAME , " + colPojo.getColInvoiceDt()
				+ " error_invoice_date, 	" + colPojo.getColInstdAmt() + " error_invoice_amount		        FROM "
				+ tableName + "   " + condition);
		// + " invoice_no is not null "
		// + " and concat(end_to_end_id ,invoice_no) NOT IN (SELECT concat(PAYMENT_ID
		// ,INVOICE_NUM) FROM xp_pay_invoices) ");

	}

	private StringBuilder getPaybatchAckDetails(String internalUuid, StringBuilder condition, String tableName,
			SourceSysEntity srcSys, ColPojo colPojo, String staticType, String batchcomments, String fileStatus) {

		return new StringBuilder(
				" INSERT INTO xp_paybatch_ack_details(internal_uuid, PAYBATCH_ACK_ID, PAYMENT_INSTRUCTION_ID, ACK_STATUS, FILE_NAME, CREATED_BY, CREATION_DATE, "
						+ "                                             LAST_UPDATE_DATE, LAST_UPDATED_BY, FILE_STATUS, ADDITIONAL_INFO) "
						+ "        SELECT '" + internalUuid + "' , concat(j." + colPojo.getColBatch()
						+ ",j.PAYBATCH_ACK_ID,row_number() OVER (ORDER BY j.PAYBATCH_ACK_ID)) PAYBATCH_ACK_ID, "
						+ "                j." + colPojo.getColBatch()
						+ " PAYMENT_INSTRUCTION_ID, j.ACK_STATUS ACK_STATUS, j.FILE_NAME FILE_NAME, "
						+ "j.CREATED_BY CREATED_BY, j.CREATION_DATE CREATION_DATE,  "
						+ "     j.LAST_UPDATE_DATE LAST_UPDATE_DATE, j.LAST_UPDATED_BY LAST_UPDATED_BY, j.FILE_STATUS FILE_STATUS, j.ADDITIONAL_INFO ADDITIONAL_INFO "
						+ "        FROM (SELECT  DATE_FORMAT(SYSDATE(), '%Y%m%d%H%i%s') PAYBATCH_ACK_ID, "
						+ "              " + colPojo.getColBatch() + ", '" + staticType.toUpperCase() + "' ACK_STATUS, "
						+ "              concat('FILE_',DATE_FORMAT(SYSDATE(), '%Y%m%d%H%i%s'),'_',"
						+ colPojo.getColBatch() + ")  FILE_NAME, '" + XorConstant.ADMIN_ROLE
						+ "'               CREATED_BY, CAST(NOW() AS datetime) CREATION_DATE, CAST(NOW() AS datetime) LAST_UPDATE_DATE, '"
						+ XorConstant.ADMIN_ROLE + "'              LAST_UPDATED_BY, '" + fileStatus + "' FILE_STATUS, "
						+ "              '" + batchcomments + "' ADDITIONAL_INFO " + "        FROM " + tableName + "  "
						+ condition + ") as j");
		// + " message_id not in( select distinct PAYMENT_INSTRUCTION_ID FROM
		// xp_paybatch_ack_details)) as j "
		// + " ORDER BY 1 ");
	}

	public void initializeBasicColName(String type, Map<String, Integer> piuidSeqIdMap, SourceSysEntity srcSys) {
		getMappedSourceColumnName(null, null, type, piuidSeqIdMap, srcSys);
	}

	public String getMappedSourceColumnName(Integer piuidSeqId, String nrmColName, String tableType,
			Map<String, Integer> piuidSeqIdMap, SourceSysEntity srcSys) {
		if (tableType.equalsIgnoreCase(XorConstant.SOURCE)) {
			DynamicNrmlzColumnEntity dynNrmlColm = dynamicNrmlzColumnRepo
					.findBySrcSysIdAndActiveIndAndSrcColNameIgnoreCase(srcSys.getId(), XorConstant.STATUS_Y,
							nrmColName);
			if (null != dynNrmlColm) {
				if (tableType.equalsIgnoreCase(XorConstant.SOURCE)) {
					return dynNrmlColm.getSrcColName();
				} else if (tableType.equalsIgnoreCase(XorConstant.NRMLZ)) {
					return dynNrmlColm.getNrmzColName();
				}
			}
		} else if (tableType.equalsIgnoreCase(XorConstant.NRMLZ)) {
			logger.info("nrmColName" + nrmColName);
			DynamicNrmlzColumnEntity dynNrmlColm = dynamicNrmlzColumnRepo
					.findBySrcSysIdAndActiveIndAndSrcColNameIgnoreCase(srcSys.getId(), XorConstant.STATUS_Y,
							nrmColName);
			if (null != dynNrmlColm) {
				if (tableType.equalsIgnoreCase(XorConstant.SOURCE)) {
					return dynNrmlColm.getSrcColName();
				} else if (tableType.equalsIgnoreCase(XorConstant.NRMLZ)) {
					return dynNrmlColm.getNrmzColName();
				}
			}
		} else if (null != srcSys && tableType.equalsIgnoreCase(XorConstant.AGG)) {
			List<NormalizeAggXmlTagsMapEntity> nrmAggTagEntityList = normalizeAggXmlTagsMapRepo
					.findByActiveIndAndErpSrcSysAndAggColNameIgnoreCaseInAndPiuidSeqIdIn(XorConstant.STATUS_Y,
							srcSys.getId(), CommonJPAConstant.colList, new ArrayList<>(piuidSeqIdMap.values()));
			// Map<piuidseqid, Map<colattr, colname>>
			Set<String> nrmlzColNameSet = new HashSet<>();
			nrmAggTagEntityList.stream().forEach(e -> {
				nrmlzColNameSet.add(e.getNrmlzColName());
				if (piuidSeqIdColMap.containsKey(e.getPiuidSeqId())) {

					Map<String, String> colsMap = piuidSeqIdColMap.get(e.getPiuidSeqId());
					getColMap(e.getAggColName(), colsMap, e.getNrmlzColName());

					piuidSeqIdColMap.put(e.getPiuidSeqId(), colsMap);
				} else {
					Map<String, String> colsMap = new HashMap<>();
					getColMap(e.getAggColName(), colsMap, e.getNrmlzColName());

					piuidSeqIdColMap.put(e.getPiuidSeqId(), colsMap);
				}

			});
			if (nrmlzColNameSet.size() > 0) {
				nrmlzSourceMap = dynamicNrmlzColumnRepo
						.findBySrcSysIdAndActiveIndAndNrmzColNameIgnoreCaseIn(srcSys.getId(), XorConstant.STATUS_Y,
								nrmlzColNameSet)
						.stream().collect(Collectors.toMap(DynamicNrmlzColumnEntity::getNrmzColName,
								DynamicNrmlzColumnEntity::getSrcColName));
			}

			return null;
		}
		return null;
	}

	private void getColMap(String aggColName, Map<String, String> colsMap, String nrmlColName) {

		switch (aggColName) {
		case CommonJPAConstant.COL4_MSGID:
			colsMap.put(CommonJPAConstant.COL4_MSGID, nrmlColName);
			break;
		case CommonJPAConstant.COL6_NBOFTXS:
			colsMap.put(CommonJPAConstant.COL6_NBOFTXS, nrmlColName);
			break;
		case CommonJPAConstant.COL41_NM:
			colsMap.put(CommonJPAConstant.COL41_NM, nrmlColName);
			break;
		case CommonJPAConstant.COL5_CREDTTM:
			colsMap.put(CommonJPAConstant.COL5_CREDTTM, nrmlColName);
			break;
		case CommonJPAConstant.COL33_ENDTOENDID:
			colsMap.put(CommonJPAConstant.COL33_ENDTOENDID, nrmlColName);
			break;
		case CommonJPAConstant.CCY_CD:
			colsMap.put(CommonJPAConstant.CCY_CD, nrmlColName);
			break;
		case CommonJPAConstant.COL60_RLTDDT:
			colsMap.put(CommonJPAConstant.COL60_RLTDDT, nrmlColName);
			break;
		case CommonJPAConstant.COL20_REQDEXCTNDT:
			colsMap.put(CommonJPAConstant.COL20_REQDEXCTNDT, nrmlColName);
			break;
		case CommonJPAConstant.COL35_INSTDAMT:
			colsMap.put(CommonJPAConstant.COL35_INSTDAMT, nrmlColName);
			break;
		}
	}

	public boolean intializeColNameForProfileName(String profileName, Map<String, Integer> piuidSeqIdMap) {
		if (null != piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName))) {
			colBatch = nrmlzSourceMap
					.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL4_MSGID));
			colNoOftxn = nrmlzSourceMap
					.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL6_NBOFTXS));
			colPartyName = nrmlzSourceMap
					.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL41_NM));
			colPaymentDate = nrmlzSourceMap
					.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL5_CREDTTM));
			colPaymentIdSource = nrmlzSourceMap
					.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL33_ENDTOENDID));

			colCurrency = nrmlzSourceMap
					.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.CCY_CD));

			colTransferDt = nrmlzSourceMap
					.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL20_REQDEXCTNDT));
			colInvoiceDt = nrmlzSourceMap
					.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL60_RLTDDT));
			colInstdAmt = nrmlzSourceMap
					.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL35_INSTDAMT));

			logger.info("Base col initialized for profileName -" + profileName + " colBatch -" + colBatch
					+ " colNoOftxn -" + colNoOftxn + " colPartyName -" + colPartyName + " colPaymentDate -"
					+ colPaymentDate + " colPaymentIdSource -" + colPaymentIdSource);
			return true;
		} else {
			return false;
		}
	}

}