package com.xoriant.xorpay.data.sync.repo;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.data.sync.entity.DynamicNrmlzColumnEntity;
import com.xoriant.xorpay.entity.PIUIDEntity;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.pojo.PaymentErrorPojo;
import com.xoriant.xorpay.repo.PIUIDRepo;

@Repository
@Transactional
public class LoadPaymentJPARepo {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private AuditJPARepo auditJPARepo;
	@Autowired
	private CommonJPARepo commonJPARepo;
	@Autowired
	private DynamicNrmlzColumnRepo dynamicNrmlzColumnRepo;
	@Autowired
	private PIUIDRepo piuidRepo;

	private Map<BigInteger, String> errorMessId = new HashMap<>(1);

	private Query query = null;

	private final Logger logger = LoggerFactory.getLogger(LoadPaymentJPARepo.class);
	private int i = 0;
	Map<String, PaymentErrorPojo> messageErrDataMap = new HashMap<>();

	private Transaction tr;

	public List<Object[]> getResultSet(String aggquery) {
		Query nativeQuery = entityManager.createNativeQuery(aggquery);
		List<Object[]> rsl = nativeQuery.getResultList();
		return rsl;
	}

	public List<String> getResultSetString(String aggquery) {
		Query nativeQuery = entityManager.createNativeQuery(aggquery);
		return nativeQuery.getResultList();
	}

	public void loadRequiredDetails(String processId, ConfigPojo configPojo) {
		entityManager = entityManager.getEntityManagerFactory().createEntityManager();
		Session session = entityManager.unwrap(Session.class);
		tr = session.beginTransaction();
		session.doWork(new Work() {

			@Override
			public void execute(Connection connections) throws SQLException {
				try (Connection connection = connections) {
					// connection.setAutoCommit(true);

					loadRequiredDetailsCall(processId, configPojo, session, connection);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	public void loadRequiredDetailsCall(String processId, ConfigPojo configPojo, Session session,
			Connection connection) {
		StringBuilder insserXpPayBatchSummury = null;
		StringBuilder insertIntoPaymentDetails = null;
		StringBuilder invoice = null, invoiceNonDuplicate = null;
		StringBuilder payBatch = null;
		logger.info(configPojo + " ");
		logger.info("configPojo " + configPojo.getDATABASE_SYSTEM());
		if (configPojo.getDATABASE_SYSTEM().equalsIgnoreCase(CommonJPAConstant.POSTGRES_DB_DATA_TYPE)) {
			insserXpPayBatchSummury = new StringBuilder("INSERT INTO XP_PAY_BATCH_SUMMARY "
					+ "            (PAYMENT_INSTRUCTION_ID, PAYMENT_COUNT, PAYMENT_AMOUNT, ORG_NAME,    "
					+ "             PAYMENT_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY, PROCESS_RUN_DATE "
					+ "             , PAYMENT_INSTRUCTION_STATUS, FILE_STATUS, PIUID, ERP_SRC_SYS, SRC_SYSTEM) "
					+ "        SELECT  "
					+ "            i.BATCHID PAYMENT_INSTRUCTION_ID, i.NO_OF_TRANSFERS PAYMENT_COUNT, SUM(i.CONTROL_SUM) PAYMENT_AMOUNT, "
					+ "            i.company_name ORG_NAME, i.created_date_time PAYMENT_DATE, localtimestamp CREATION_DATE, '"
					+ configPojo.getSrcSys().getSrcSystemSort() + "' CREATED_BY, "
					+ "   localtimestamp LAST_UPDATE_DATE, '" + configPojo.getSrcSys().getSrcSystemSort()
					+ "' LAST_UPDATED_BY, "
					+ "            localtimestamp PROCESS_RUN_DATE, '' PAYMENT_INSTRUCTION_STATUS,'' FILE_STATUS, i.PROFILENAME PIUID "
					+ " , " + configPojo.getSOURCE_SYS_ID() + " erp_src_sys, '"
					+ configPojo.getSrcSys().getSrcSystemSort() + "' src_system "
					+ "        FROM (SELECT DISTINCT cast(message_id as int) as batchid, no_of_transfers, control_sum,  company_name, "
					+ " created_date_time, payment_process_profile PROFILENAME FROM " + XorConstant.NRMLZ_PMT_DTLS
					+ ") AS i "
					+ "        WHERE batchid NOT IN( SELECT DISTINCT COALESCE(PAYMENT_INSTRUCTION_ID,0) FROM XP_PAY_BATCH_SUMMARY) "
					+ "        GROUP BY BATCHID, no_of_transfers, company_name, created_date_time, PROFILENAME "
					+ "        ORDER BY 1 ");

			insertIntoPaymentDetails = new StringBuilder("INSERT INTO XP_PAYMENT_DETAILS  "
					+ "            (PAYMENT_INSTRUCTION_ID, PAYMENT_NUMBER, PAYMENT_AMOUNT,PAYMENT_CURRENCY_CODE, PAYMENT_METHOD_CODE, "
					+ "            SUPPLIER_NAME, PAYMENT_ID, CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY, "
					+ "				PAYMENT_PROFILE_NAME,PIUID, "
					+ "            FILE_STATUS, PAYMENT_STATUS,CHECK_ID,LOGICAL_GROUP_REFERENCE, ORG_NAME, PAYMENT_DATE, ERP_SRC_SYS, SRC_SYSTEM) "
					+ " SELECT DISTINCT  "
					+ "  cast(message_id as int) PAYMENT_INSTRUCTION_ID, end_to_end_id PAYMENT_NUMBER, "
					+ " PAYMENT_AMOUNT PAYMENT_AMOUNT, currency PAYMENT_CURRENCY_CODE, payment_method PAYMENT_METHOD_CODE, "
					+ " recipient_name SUPPLIER_NAME, end_to_end_id PAYMENT_ID, localtimestamp CREATION_DATE," + " '"
					+ configPojo.getSrcSys().getSrcSystemSort() + "' CREATED_BY ,  localtimestamp LAST_UPDATE_DATE,"
					+ "'" + configPojo.getSrcSys().getSrcSystemSort() + "' LAST_UPDATED_BY,"
					+ " payment_process_profile, payment_process_profile PIUID, "
					+ " '' FILE_STATUS,'' PAYMENT_STATUS, end_to_end_id CHECK_ID, message_id LOGICAL_GROUP_REFERENCE,"
					+ " company_name ORG_NAME,         created_date_time PAYMENT_DATE, " + configPojo.getSOURCE_SYS_ID()
					+ " erp_src_sys, '" + configPojo.getSrcSys().getSrcSystemSort() + "' src_system FROM "
					+ XorConstant.NRMLZ_PMT_DTLS + " "
					+ " WHERE end_to_end_id NOT IN( SELECT DISTINCT COALESCE(PAYMENT_NUMBER,0) FROM XP_PAYMENT_DETAILS)");

			invoice = new StringBuilder("INSERT INTO XP_PAY_INVOICES "
					+ "				            (PAYMENT_ID, INVOICE_NUM, INVOICE_AMOUNT, INVOICE_CURRENCY_CODE, INVOICE_DATE, "
					+ "				             INVOICE_ID, CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY, PAYMENT_METHOD_CODE, SUPPLIER_NAME) "
					+ "			 SELECT DISTINCT             end_to_end_id PAYMENT_ID,  "
					+ "		            invoice_no INVOICE_NUM, "
					+ "				     invoice_amount  INVOICE_AMOUNT,  "
					+ "				            currency INVOICE_CURRENCY_CODE,        invoice_date INVOICE_DATE, "
					+ "				            CAST(invoice_no AS BIGINT) INVOICE_ID,            localtimestamp CREATION_DATE,  '"
					+ configPojo.getSrcSys().getSrcSystemSort() + "' CREATED_BY, localtimestamp LAST_UPDATE_DATE,  '"
					+ configPojo.getSrcSys().getSrcSystemSort() + "' LAST_UPDATED_BY, "
					+ "				            payment_method PAYMENT_METHOD_CODE, recipient_name SUPPLIER_NAME "
					+ "				        FROM " + XorConstant.NRMLZ_PMT_DTLS + " "
					+ "				        WHERE Cast(end_to_end_id as varchar)||Cast(invoice_no as varchar) NOT IN "
					+ "				(SELECT COALESCE(Cast(PAYMENT_ID as varchar)||Cast(INVOICE_NUM as varchar),'0') FROM XP_PAY_INVOICES)");
			payBatch = new StringBuilder(
					" INSERT INTO XP_PAYBATCH_ACK_DETAILS(PAYBATCH_ACK_ID, PAYMENT_INSTRUCTION_ID, ACK_STATUS, FILE_NAME, CREATED_BY, CREATION_DATE, "
							+ "                                             LAST_UPDATE_DATE, LAST_UPDATED_BY, FILE_STATUS, ADDITIONAL_INFO) "
							+ "        SELECT  j.PAYBATCH_ACK_ID||row_number() OVER (ORDER BY j.PAYBATCH_ACK_ID) PAYBATCH_ACK_ID, "
							+ "                j.PAYMENT_INSTRUCTION_ID PAYMENT_INSTRUCTION_ID, j.ACK_STATUS ACK_STATUS, j.FILE_NAME FILE_NAME, "
							+ "j.CREATED_BY CREATED_BY, j.CREATION_DATE CREATION_DATE,  "
							+ "     j.LAST_UPDATE_DATE LAST_UPDATE_DATE, j.LAST_UPDATED_BY LAST_UPDATED_BY, j.FILE_STATUS FILE_STATUS, j.ADDITIONAL_INFO ADDITIONAL_INFO "
							+ "        FROM (SELECT DISTINCT TO_CHAR(CAST(NOW() AS timestamp),'YYYYMMDDHH24MMSSMS') PAYBATCH_ACK_ID, "
							+ "              cast(message_id as int) PAYMENT_INSTRUCTION_ID, 'NEW' ACK_STATUS, "
							+ "              'FILE_'||TO_CHAR(CAST(NOW() AS timestamp),'YYYYMMDDHH24MMSS')||'_'||message_id FILE_NAME, "
							+ "              7000 CREATED_BY, CAST(NOW() AS timestamp) CREATION_DATE, CAST(NOW() AS timestamp) LAST_UPDATE_DATE, "
							+ "              7000 LAST_UPDATED_BY, 'NEW' FILE_STATUS, "
							+ "              'NEW' ADDITIONAL_INFO " + "        FROM " + XorConstant.NRMLZ_PMT_DTLS
							+ " "
							+ "        WHERE cast(message_id as int) not in( select distinct COALESCE(PAYMENT_INSTRUCTION_ID,0) FROM XP_PAYBATCH_ACK_DETAILS)) as j              "
							+ "        ORDER BY 1 ");
		} else {
			payBatchSummury(processId, connection, configPojo);
		}
	}

	private void payBatchSummury(String processId, Connection connection, ConfigPojo configPojo) {

		StringBuilder nrmlDataQuery = new StringBuilder(
				" SELECT DISTINCT message_id  FROM " + XorConstant.NRMLZ_PMT_DTLS + " ");
		StringBuilder whereCondition = new StringBuilder(" WHERE process_status='" + XorConstant.MOVE_TO_HISTORY + "' "
				+ " and erp_src_sys =" + configPojo.getSOURCE_SYS_ID()
				+ " and message_id NOT IN( SELECT DISTINCT PAYMENT_INSTRUCTION_ID FROM xp_pay_batch_summary)  ");
		nrmlDataQuery.append(whereCondition);

		commonJPARepo.colProfileName = commonJPARepo.getMappedSourceColumnName(null,
				configPojo.getSOURCE_PROFILE_COLUMN_NAME(), XorConstant.NRMLZ, null, configPojo.getSrcSys());

		List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList = dynamicNrmlzColumnRepo
				.findBySrcSysIdAndActiveIndOrderByIdAsc(configPojo.getSOURCE_SYS_ID(), XorConstant.STATUS_Y);
		// Data set
		List<DynamicNrmlzColumnEntity> dynamicDistinctColumn = new ArrayList<>(1);

		Map<String, List<Map<String, String>>> piuidMap = getResultFromNrmlz(dynamicNrmlColEntityList,
				dynamicDistinctColumn, whereCondition);

		Set<String> profileList = piuidMap.keySet();
		Map<String, Integer> piuidSeqIdMap = piuidRepo
				.findByActiveIndAndProfileNameIgnoreCaseIn(XorConstant.STATUS_Y, profileList).stream()
				.collect(Collectors.toMap(PIUIDEntity::getProfileName, PIUIDEntity::getId));
		commonJPARepo.initializeBasicColName(XorConstant.AGG, piuidSeqIdMap, configPojo.getSrcSys());

		piuidMap.entrySet().stream().forEach(e -> {

			List<Map<String, String>> nrmMapList = e.getValue();
			nrmMapList.stream().forEach(nrmlzMap -> {
				String profileName = nrmlzMap.get(commonJPARepo.colProfileName);

				if (commonJPARepo.intializeColNameForProfileName(profileName, piuidSeqIdMap)) {
					if (null != commonJPARepo.colProfileName && null != commonJPARepo.colPaymentIdSource) {
						// logger.info("colPaymentIdSource " + colPaymentIdSource);

						String batchId = nrmlzMap.get(commonJPARepo.colBatch);
						String currency = nrmlzMap.get(commonJPARepo.colCurrency);
						String creationDt = nrmlzMap.get(commonJPARepo.colPaymentDate);
						String transferDt = nrmlzMap.get(commonJPARepo.colTransferDt);
						String invoiceDt = nrmlzMap.get(commonJPARepo.colInvoiceDt);
						String endToEndId = nrmlzMap.get(commonJPARepo.colPaymentIdSource);
						String instdAmt = nrmlzMap.get(commonJPARepo.colInstdAmt);
						String noOfTxn = nrmlzMap.get(commonJPARepo.colNoOftxn);

						System.out
								.println("At loading --> batchId " + batchId + " currency " + currency + "  creationDt "
										+ creationDt + " transferDt" + transferDt + " invoiceDt " + invoiceDt);
				//done now		PaymentErrorPojo paymentErrPojo = new PaymentErrorPojo(batchId, endToEndId, instdAmt, currency,
				//				transferDt, null, configPojo.getSOURCE_SYS_ID(), noOfTxn, null, null);
				//		messageErrDataMap.put(batchId, paymentErrPojo);
					}
				} else {
					logger.info("Ignored");
				}
			});
		});

		logger.info("At Load piuidMap "+ piuidMap.size());
		logger.info("nrmlDataQuery "+ nrmlDataQuery);
		List<String> messageIdList = getMessageIdResultSet(nrmlDataQuery);
		logger.info("Payment will load for "+ messageIdList.toString());
		if (!messageIdList.isEmpty()) {
			
			messageIdList.stream().forEach(e -> {
				logger.info("Paymen loading for "+ e);
				payBatchSummaryInsert(e.toString(), connection, configPojo);
			});

			try {
				cleanTableWithProcessStatus(XorConstant.NRMLZ_PMT_DTLS, messageIdList, connection, configPojo);
			} catch (SQLException e1) {
				try {
					connection.rollback();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				e1.printStackTrace();
			}
			logger.info("Data Cleaned :: Nrmlz ");
		}
		try {
			auditJPARepo.audiLogger(processId, null, AuditJPARepo.PAYMLOAD);
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void payBatchSummaryInsert(String messageId, Connection connection, ConfigPojo configPojo) {
		StringBuilder batchInsert = null;
		StringBuilder where = null;
		try {
			where = new StringBuilder(" where message_id  in('" + messageId + "')");
			batchInsert = getPayBatchInsertStmt(where, configPojo);
			logger.info("Batch  loading for "+ messageId + "  with query :: "+ batchInsert);
			query = entityManager.createNativeQuery(batchInsert.toString());
			query.executeUpdate();
			entityManager.flush();
			entityManager.clear();
			logger.info("Payment batch  loaded");
			xpPaymentDetails(messageId, configPojo);

			auditJPARepo.addBatchAuditMessage(null, messageId, null, "Pay Batch Summary, Loaded Successfull",
					AuditJPARepo.PROCESSED);
			connection.commit();
		} catch (Exception ex) {
			try {
				errorMessId.put(new BigInteger(messageId), "Load Payment Details Issue : " + ex.getMessage());
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			// Done now commonJPARepo.loadPaymentDetails(messageErrDataMap.get(messageId),
			//		"Pay Batch Summary, Error " + ex.getMessage(), entityManager, query, configPojo.getSrcSys());
			logger.info("insserXpPayBatchSummury " + batchInsert);
			auditJPARepo.addBatchAuditMessage(null, messageId, null, "Pay Batch Summary, Error " + ex.getMessage(),
					AuditJPARepo.FAILED);
			ex.printStackTrace();
		}

	}

	private void xpPaymentDetails(String messageId, ConfigPojo configPojo) throws SQLException {
		StringBuilder insertStmt = null;
		StringBuilder where = null;
		// try {
		where = new StringBuilder(" message_id  in('" + messageId + "') and ");
		insertStmt = getPaymntDetailsInsertStmt(where, configPojo);
		logger.info("Payment details  loading for "+ messageId + "  with query :: "+ insertStmt);
		query = entityManager.createNativeQuery(insertStmt.toString());
		query.executeUpdate();
		entityManager.flush();
		entityManager.clear();
		logger.info("Payment details  loaded");
		xpPayInvoice(messageId);

		auditJPARepo.addBatchAuditMessage(null, messageId, null, "Payment Details, Loaded Successfull",
				AuditJPARepo.PROCESSED);

//		} catch (Exception ex) {
//			logger.info("Payment Details :: Payment Loading issue");
//			logger.info("insserxpPaymentDetails " + insertStmt);
//			auditJPARepo.addBatchAuditMessage(null, messageId, null, "Payment Details, Error " + ex.getMessage(),
//					AuditJPARepo.FAILED);
//			ex.printStackTrace();
//		}
		logger.info("Payment Details :: Payment Loaded Successfull");
	}

	private void xpPayInvoice(String messageId) throws SQLException {
		StringBuilder insertStmt = null;
		StringBuilder where = null;
		// try {
		where = new StringBuilder(" message_id  in('" + messageId + "') and ");
		insertStmt = getPayInvoice(where);
		logger.info("Payment Invoice  loading for "+ messageId + "  with query :: "+ insertStmt);
		query = entityManager.createNativeQuery(insertStmt.toString());
		query.executeUpdate();
		entityManager.flush();
		entityManager.clear();
		logger.info("Payment Invoice  loaded");
		xpPaybatchAckDetails(messageId);

		auditJPARepo.addBatchAuditMessage(null, messageId, null, "Pay Invoice, Loaded Successfull",
				AuditJPARepo.PROCESSED);

//	 } catch (SQLException ex) {
//		logger.info("Pay Invoice :: Payment Loading issue "+ ex.getMessage());

//		logger.info("Pay Invoice :: Payment Loading issue "+ ex.getLocalizedMessage());

//		logger.info("Pay Invoice :: Payment Loading issue "+ ex.getCause());
//			logger.info("insserxpPayInvoice " + insertStmt);
//			auditJPARepo.addBatchAuditMessage(null, messageId, null, "Pay Invoice, Error " + ex.getMessage(),
//					AuditJPARepo.FAILED);
//			ex.printStackTrace();
//	}
		logger.info("Pay Invoice :: Payment Loaded Successfull");
	}

	private void xpPaybatchAckDetails(String messageId) throws SQLException {
		StringBuilder insertStmt = null;
		StringBuilder where = null;
		// try {
		where = new StringBuilder(" message_id  in('" + messageId + "') and ");
		insertStmt = getPaybatchAckDetails(where);
		logger.info("Payment Paybatcg ack  loading for "+ messageId + "  with query :: "+ insertStmt);
		query = entityManager.createNativeQuery(insertStmt.toString());
		query.executeUpdate();
		entityManager.flush();
		entityManager.clear();
		logger.info("Payment Paybatcg ack  loaded");
		auditJPARepo.addBatchAuditMessage(null, messageId, null, "Paybatch Ack Details, Loaded Successfull",
				AuditJPARepo.PROCESSED);

//		} catch (Exception ex) {
//			logger.info("Paybatch Ack Detail :: Payment Loading issue");
//			logger.info("insserxpPaybatchAckDetails " + insertStmt);
//			auditJPARepo.addBatchAuditMessage(null, messageId, null, "Paybatch Ack Detail, Error " + ex.getMessage(),
//					AuditJPARepo.FAILED);
//			ex.printStackTrace();
//		}
		logger.info("Paybatch Ack Details :: Payment Loaded Successfull");
	}

	private StringBuilder getPayBatchInsertStmt(StringBuilder where, ConfigPojo configPojo) {
		StringBuilder condition = new StringBuilder("INSERT INTO xp_pay_batch_summary "
				+ "            (PAYMENT_INSTRUCTION_ID, PAYMENT_COUNT, PAYMENT_AMOUNT, ORG_NAME,    "
				+ "             PAYMENT_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY, PROCESS_RUN_DATE "
				+ "             , PROFILENAME, ERP_SRC_SYS, SRC_SYSTEM, PAYMENT_CURRENCY_CODE) " + "        SELECT  "
				+ "            i.BATCHID PAYMENT_INSTRUCTION_ID, i.NO_OF_TRANSFERS PAYMENT_COUNT, SUM(i.CONTROL_SUM) PAYMENT_AMOUNT, "
				+ "            i.company_name ORG_NAME, i.created_date_time PAYMENT_DATE, localtimestamp CREATION_DATE, '"
				+ XorConstant.ADMIN_ROLE + "' CREATED_BY, " + "   localtimestamp LAST_UPDATE_DATE, '"
				+ XorConstant.ADMIN_ROLE + "' LAST_UPDATED_BY, "
				+ "            localtimestamp PROCESS_RUN_DATE,  i.PROFILENAME PIUID " + " , "
				+ configPojo.getSOURCE_SYS_ID() + " erp_src_sys, '" + configPojo.getSrcSys().getSrcSystemSort()
				+ "' src_system , i.CURRENCY "
				+ "        FROM (SELECT DISTINCT message_id as batchid, no_of_transfers, control_sum,  company_name, "
				+ " created_date_time, payment_process_profile PROFILENAME, CURRENCY FROM " + XorConstant.NRMLZ_PMT_DTLS
				+ " " + where + " ) AS i "
				+ "        WHERE batchid NOT IN( SELECT DISTINCT PAYMENT_INSTRUCTION_ID FROM xp_pay_batch_summary) "
				+ "        GROUP BY BATCHID, no_of_transfers, company_name, created_date_time, PROFILENAME, currency "
				+ "        ORDER BY 1 ");
		return condition;
	}

	private StringBuilder getPaymntDetailsInsertStmt(StringBuilder condition, ConfigPojo configPojo) {
		return new StringBuilder("INSERT INTO xp_payment_details  "
				+ "            (PAYMENT_INSTRUCTION_ID, PAYMENT_NUMBER, PAYMENT_AMOUNT,PAYMENT_CURRENCY_CODE, PAYMENT_METHOD_CODE, "
				+ "            SUPPLIER_NAME, PAYMENT_ID, CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY, "
				+ "				PAYMENT_PROFILE_NAME,PIUID, "
				+ "            FILE_STATUS, CHECK_ID,LOGICAL_GROUP_REFERENCE, ORG_NAME, PAYMENT_DATE, ERP_SRC_SYS,"
				+ "  SRC_SYSTEM) " + " SELECT DISTINCT  "
				+ "  message_id PAYMENT_INSTRUCTION_ID, end_to_end_id PAYMENT_NUMBER, "
				+ " sum(invoice_amount) PAYMENT_AMOUNT, currency PAYMENT_CURRENCY_CODE, payment_method PAYMENT_METHOD_CODE, "
				+ " recipient_name SUPPLIER_NAME, end_to_end_id PAYMENT_ID, localtimestamp CREATION_DATE," + " '"
				+ XorConstant.ADMIN_ROLE + "' CREATED_BY ,  localtimestamp LAST_UPDATE_DATE," + "'"
				+ XorConstant.ADMIN_ROLE + "' LAST_UPDATED_BY,"
				+ " payment_process_profile, payment_process_profile PIUID, "
				+ " concat('File','_',message_id) FILE_STATUS, end_to_end_id CHECK_ID, message_id LOGICAL_GROUP_REFERENCE,"
				+ " company_name ORG_NAME,         max(created_date_time) PAYMENT_DATE, " + configPojo.getSOURCE_SYS_ID()
				+ " erp_src_sys, '" + configPojo.getSrcSys().getSrcSystemSort() + "' src_system FROM "
				+ XorConstant.NRMLZ_PMT_DTLS + " " + " WHERE " + condition
				+ " end_to_end_id NOT IN( SELECT DISTINCT PAYMENT_NUMBER FROM xp_payment_details)"
				+ " group by  message_id, currency, payment_method, recipient_name,payment_process_profile,company_name   ");
	}

	private StringBuilder getPayInvoice(StringBuilder condition) {
		return new StringBuilder("INSERT INTO xp_pay_invoices "
				+ "				            (PAYMENT_ID, INVOICE_NUM, INVOICE_AMOUNT, INVOICE_CURRENCY_CODE, INVOICE_DATE, "
				+ "				             INVOICE_ID, CREATION_DATE, CREATED_BY, LAST_UPDATE_DATE, LAST_UPDATED_BY, PAYMENT_METHOD_CODE, SUPPLIER_NAME) "
				+ "			 SELECT DISTINCT             end_to_end_id PAYMENT_ID,  "
				+ "		            invoice_no INVOICE_NUM, " + "				     invoice_amount  INVOICE_AMOUNT,  "
				+ "				            currency INVOICE_CURRENCY_CODE,        invoice_date INVOICE_DATE, "
				+ "				            invoice_no  INVOICE_ID,            localtimestamp CREATION_DATE,  '"
				+ XorConstant.ADMIN_ROLE + "' CREATED_BY, localtimestamp LAST_UPDATE_DATE,  '" + XorConstant.ADMIN_ROLE
				+ "' LAST_UPDATED_BY, "
				+ "				            payment_method PAYMENT_METHOD_CODE, recipient_name SUPPLIER_NAME "
				+ "				        FROM " + XorConstant.NRMLZ_PMT_DTLS + "   WHERE " + condition
				+ " invoice_no  is not null  "
				+ "				and  concat(end_to_end_id ,invoice_no)  NOT IN (SELECT concat(PAYMENT_ID ,INVOICE_NUM) FROM xp_pay_invoices) ");

	}

	private StringBuilder getPaybatchAckDetails(StringBuilder condition) {
		return new StringBuilder(
				" INSERT INTO xp_paybatch_ack_details(PAYBATCH_ACK_ID, PAYMENT_INSTRUCTION_ID, ACK_STATUS, FILE_NAME, CREATED_BY, CREATION_DATE, "
						+ "                                             LAST_UPDATE_DATE, LAST_UPDATED_BY, FILE_STATUS, ADDITIONAL_INFO) "
						+ "        SELECT  concat(j.PAYMENT_INSTRUCTION_ID,j.PAYBATCH_ACK_ID,row_number() OVER (ORDER BY j.PAYBATCH_ACK_ID)) PAYBATCH_ACK_ID, "
						+ "                j.PAYMENT_INSTRUCTION_ID PAYMENT_INSTRUCTION_ID, j.ACK_STATUS ACK_STATUS, j.FILE_NAME FILE_NAME, "
						+ "j.CREATED_BY CREATED_BY, j.CREATION_DATE CREATION_DATE,  "
						+ "     j.LAST_UPDATE_DATE LAST_UPDATE_DATE, j.LAST_UPDATED_BY LAST_UPDATED_BY, j.FILE_STATUS FILE_STATUS, j.ADDITIONAL_INFO ADDITIONAL_INFO "
						+ "        FROM (SELECT DISTINCT  DATE_FORMAT(SYSDATE(), '%Y%m%d%H%i%s') PAYBATCH_ACK_ID, "
						+ "              message_id  PAYMENT_INSTRUCTION_ID, 'NEW' ACK_STATUS, "
						+ "              concat('FILE_',DATE_FORMAT(SYSDATE(), '%Y%m%d%H%i%s'),'_',message_id)  FILE_NAME, '"
						+ XorConstant.ADMIN_ROLE
						+ "'               CREATED_BY, CAST(NOW() AS datetime) CREATION_DATE, CAST(NOW() AS datetime) LAST_UPDATE_DATE, '"
						+ XorConstant.ADMIN_ROLE + "'              LAST_UPDATED_BY, 'NEW' FILE_STATUS, "
						+ "              'NEW' ADDITIONAL_INFO " + "        FROM " + XorConstant.NRMLZ_PMT_DTLS
						+ "     WHERE " + condition
						+ " message_id not in( select distinct PAYMENT_INSTRUCTION_ID FROM xp_paybatch_ack_details)) as j              "
						+ "        ORDER BY 1 ");
	}

	private List<String> getMessageIdResultSet(StringBuilder nrmlDataQuery) {
		List<String> nrmDataLst = getResultSetString(nrmlDataQuery.toString());
		List<String> messageId = new ArrayList<>();
		if (nrmDataLst.size() > 0) {
			nrmDataLst.stream().forEach(e -> {
				if (null != e) {
					messageId.add(e);
				}
			});
		}
		return messageId;
	}

	private void cleanTableWithProcessStatus(String tableName, List<String> messageIdList, Connection connection, ConfigPojo configPojo)
			throws SQLException {

		StringBuilder deleteQuery = new StringBuilder(
				"delete from " + tableName + " where process_status='" + XorConstant.MOVE_TO_HISTORY + "' and erp_src_sys= "+configPojo.getSOURCE_SYS_ID());
		query = entityManager.createNativeQuery(deleteQuery.toString());
		query.executeUpdate();
		connection.commit();
		entityManager.flush();
		entityManager.clear();
		if (!messageIdList.isEmpty()) {
			messageIdList.removeAll(new ArrayList<>(errorMessId.keySet()));
			if (!messageIdList.isEmpty()) {
				StringBuilder updateAgg = new StringBuilder(" update agg_prty_pymt_dtls set xml_status='"
						+ XorConstant.STATUS_LOADED + "' where msgid_4 in (");
				i = 0;
				getValues(messageIdList, updateAgg);
				updateAgg.append(") ");
				query = entityManager.createNativeQuery(updateAgg.toString());
				query.executeUpdate();
				connection.commit();
				entityManager.flush();
				entityManager.clear();

				errorMessId.entrySet().stream().forEach(e -> {
					try {
						StringBuilder updateEAgg = new StringBuilder(" update agg_prty_pymt_dtls set xml_status='"
								+ XorConstant.STATUS_E + "' , etl_process_comment='" + e.getValue()
								+ "' where msgid_4 in ('" + e.getKey() + "') ");
						logger.info("updat ag qury " + updateEAgg);
						query = entityManager.createNativeQuery(updateEAgg.toString());
						query.executeUpdate();
						connection.commit();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
					entityManager.flush();
					entityManager.clear();
				});
			}
		}
	}

	private void getValues(List<String> messageIdList, StringBuilder updateAgg) {
		messageIdList.forEach(e -> {
			if (i > 0) {
				updateAgg.append(", ");
			}
			updateAgg.append("'" + e + "'");
			i++;
		});
	}

	public Map<String, List<Map<String, String>>> getResultFromNrmlz(
			List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList, List<DynamicNrmlzColumnEntity> dynamicUniqueColumn,
			StringBuilder whereCondition) {

		StringBuilder selectQuery = getSelectQueryForNrmlzTable(dynamicNrmlColEntityList, dynamicUniqueColumn,
				whereCondition);
		List<Object[]> rsl = getResultSet(selectQuery.toString());
		Map<String, List<Map<String, String>>> piuidMap = new HashMap<>(1);
		int total;
		if (rsl.size() > 0) {
			total = dynamicUniqueColumn.size();
			rsl.stream().forEach(e -> {
				commonJPARepo.nrmlzProfile = null;
				Map<String, String> nrmlzMap = new HashMap<>();
				for (int r = 0; r < total; r++) {
					if (null != e[r]) {
						String col = dynamicUniqueColumn.get(r).getNrmzColName().trim();
						if (col.equalsIgnoreCase(commonJPARepo.colProfileName)) {
							commonJPARepo.nrmlzProfile = e[r].toString();
						}
						nrmlzMap.put(col, e[r].toString());
						// logger.info(nrmlzProfile + " col " + col + " " + e[r].toString());
					}
				}
				if (null != commonJPARepo.nrmlzProfile) {
					List<Map<String, String>> nrmlMapList;
					if (piuidMap.containsKey(commonJPARepo.nrmlzProfile)) {
						nrmlMapList = piuidMap.get(commonJPARepo.nrmlzProfile);
					} else {
						nrmlMapList = new ArrayList<>();
					}
					nrmlMapList.add(nrmlzMap);
					piuidMap.put(commonJPARepo.nrmlzProfile, nrmlMapList);
					commonJPARepo.nrmlzProfile = null;
				}
			});
		}

		return piuidMap;
	}

	public StringBuilder getSelectQueryForNrmlzTable(List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList,
			List<DynamicNrmlzColumnEntity> dynamicUniqueColumn, StringBuilder whereCondition) {

		StringBuilder selectQuery = new StringBuilder("Select ");
		i = 0;
		Set<String> columnName = new HashSet<>(1);
		dynamicNrmlColEntityList.stream().forEach(e -> {
			if (columnName.add(e.getNrmzColName().toLowerCase())) {
				dynamicUniqueColumn.add(e);
				if (i > 0) {
					selectQuery.append(", ");
				}
				selectQuery.append(e.getNrmzColName());
				i++;
			}
		});
		selectQuery.append(" from  " + XorConstant.NRMLZ_PMT_DTLS + " " + whereCondition);
		return selectQuery;
	}

}