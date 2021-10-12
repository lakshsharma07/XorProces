package com.xoriant.xorpay.data.sync.repo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.constants.ConfigConstants;
import com.xoriant.xorpay.constants.MessageConstant;
import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.constants.XorThreadConstant;
import com.xoriant.xorpay.data.sync.entity.DynamicNrmlzColumnEntity;
import com.xoriant.xorpay.data.sync.entity.ProcessStatusEntity;
import com.xoriant.xorpay.data.sync.services.LoaderCommonService;
import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.entity.AggregatedPaymentHistoryEntity;
import com.xoriant.xorpay.entity.BatchDetailStatusEntity;
import com.xoriant.xorpay.entity.ControlSumEntity;
import com.xoriant.xorpay.entity.DashboardEntity;
import com.xoriant.xorpay.entity.DashboardHistoryEntity;
import com.xoriant.xorpay.entity.NormalizeAggXmlTagsMapEntity;
import com.xoriant.xorpay.entity.PIUIDEntity;
import com.xoriant.xorpay.pojo.ColPojo;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.pojo.PaymentErrorPojo;
import com.xoriant.xorpay.pojo.PaymentLoadValidatioPojo;
import com.xoriant.xorpay.repo.AggregatedPaymentHistoryRepo;
import com.xoriant.xorpay.repo.AggregatedPaymentRepo;
import com.xoriant.xorpay.repo.BatchDetailsStatusRepo;
import com.xoriant.xorpay.repo.ControlSumRepo;
import com.xoriant.xorpay.repo.DashboardHistoryRepo;
import com.xoriant.xorpay.repo.DashboardRepo;
import com.xoriant.xorpay.repo.NormalizeAggXmlTagsMapRepo;
import com.xoriant.xorpay.repo.PIUIDRepo;
import com.xoriant.xorpay.thread.service.XorExecutor;

import sun.java2d.loops.XORComposite;

@Repository
@Transactional
public class StgToNrmlzJPARepo {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private DashboardRepo dashboardRepo;
	@Autowired
	private DashboardHistoryRepo dashboardHistoryRepo;
	@Autowired
	private AggregatedPaymentRepo aggregatedPaymentRepo;
	@Autowired
	private AggregatedPaymentHistoryRepo aggregatedPaymentHistoryRepo;
	@Autowired
	private NormalizeAggXmlTagsMapRepo normalizeAggXmlTagsMapRepo;
	@Autowired
	private DynamicNrmlzColumnRepo dynamicNrmlzColumnRepo;
	@Autowired
	private BatchDetailsStatusRepo batchDetailsStatusRepo;
	@Autowired
	private PIUIDRepo piuidRepo;
	@Autowired
	private AuditJPARepo auditJPARepo;
	@Autowired
	private CommonJPARepo comonJpaRepo;

	@Autowired
	private LoaderCommonService loaderCommonService;

	@Autowired
	private ConfigConstants config;

	@Autowired
	private ControlSumRepo controlSumRepo;

	private Map<String, PaymentErrorPojo> errorBatchSet = new HashMap<>(1);
	private Map<String, List<PaymentErrorPojo>> validBatchSet = new HashMap<>(1);

	private int i, totalInitiated = 0;
	private Query query = null;
	private Integer identityColumnId;
	private List<String> identiList = new ArrayList<>(1);
	private List<String> identiListE = new ArrayList<>(1);
	private StringBuilder queryList = null, queryListSTGE = null;
	private int recordsProcessed;
	private boolean isMoreThanOneProfileNameForBatch;
	private StringBuilder insertNrmlQuery = null;
	private Set<String> endToEndIdList = new HashSet<>();
	private boolean flag = false;
	private final Logger logger = LoggerFactory.getLogger(StgToNrmlzJPARepo.class);
	private Map<Integer, Map<String, String>> piuidSeqIdColMap = new HashMap<>();
	private Map<String, String> nrmlzSourceMap = new HashMap<>();
	public String sourceColumnIdentityName;
	public String colPaymentIdSource, colBatch, colNoOftxn, colPartyName, colPaymentDate, colProfileName, colCurrency,
			colTransferDt, colInvoiceDt, colInstdAmt, colContrlSum, colInvoiceNo, colInvoiceAmt, colPaymentMethod,
			colCompanyName;

	public String nrmlzProfile;
	private Map<String, ColPojo> colProfileMap = new HashMap<>();
	// private static ReentrantLock rel = new ReentrantLock();

	public boolean checkProfileNameForBatch(String processId, List<DynamicNrmlzColumnEntity> dynamicUniqueColumn,
			List<Map<String, String>> sourceIdMapList, Map<String, List<Map<String, String>>> batchDataMap,
			Map<String, Integer> profileMap, Map<String, PaymentErrorPojo> errorBatchMap,
			Map<String, List<PaymentErrorPojo>> validBatchMap, ConfigPojo configPojo,
			Map<String, PaymentLoadValidatioPojo> paymentLoadValMap) {
		try {
			isMoreThanOneProfileNameForBatch = false;
			colProfileName = getMappedSourceColumnName(null, configPojo.getSOURCE_PROFILE_COLUMN_NAME(),
					XorConstant.SOURCE, null, configPojo);

			Set<String> profileList = sourceIdMapList.stream().map(e -> e.get(colProfileName))
					.collect(Collectors.toSet());
			Map<String, Integer> piuidSeqIdMap = piuidRepo
					.findByActiveIndAndProfileNameIgnoreCaseIn(XorConstant.STATUS_Y, profileList).stream()
					.collect(Collectors.toMap(PIUIDEntity::getProfileName, PIUIDEntity::getId));

			initializeBasicColName(XorConstant.AGG, piuidSeqIdMap, configPojo);

			Map<String, Set<String>> batchMap = new HashMap<>();

			auditJPARepo.batchErrMessageList.clear();
			Map<String, BigDecimal> batchControlSumMap = new HashMap<>();
			Map<String, Map<String, Set<String>>> noOfTxnEndToEndIdMap = new HashMap<>();
			setSourceColumnIdentityName(sourceIdMapList, dynamicUniqueColumn, configPojo);
			i = 0;
			totalInitiated = 0;
			Set<String> endToEndIdSet = new HashSet<>();
			sourceIdMapList.stream().forEach(sourceIdMap -> {
				i++;

				String internalId = sourceIdMap.get(sourceColumnIdentityName);
				String profileName = sourceIdMap.get(colProfileName);

				PaymentErrorPojo paymentErrPojo = new PaymentErrorPojo();
				paymentErrPojo.setInternalId(internalId);
				paymentErrPojo.setProfileName(profileName);
				String key = null;
				String uniPaymenTIdBatchId = null;
				if (null == piuidSeqIdMap.get(profileName)) {

					if (colProfileMap.containsKey(profileName)) {
						ColPojo colProp = colProfileMap.get(profileName);
						colProp.setColBatch(configPojo.getSOURCE_MESSAGE_ID());
						colProp.setColPaymentIdSource(configPojo.getSOURCE_PAYMENT_ID());
						colProfileMap.put(profileName, colProp);
					} else {
						ColPojo colProp = new ColPojo();
						colProp.setColBatch(configPojo.getSOURCE_MESSAGE_ID());
						colProp.setColPaymentIdSource(configPojo.getSOURCE_PAYMENT_ID());
						colProfileMap.put(profileName, colProp);
					}
					String batchId = sourceIdMap.get(configPojo.getSOURCE_MESSAGE_ID().toUpperCase());
					String endToEndId = sourceIdMap.get(configPojo.getSOURCE_PAYMENT_ID().toUpperCase());
					paymentErrPojo.setMsgId(batchId);
					paymentErrPojo.setEndToEndId(endToEndId);

					key = internalId + "~" + batchId + "~" + endToEndId;

					logger.info(MessageConstant.PRFLN100118 + profileName);
					auditJPARepo.addBatchAuditMessage(internalId, null, null, MessageConstant.PRFLN100118 + profileName,
							AuditJPARepo.PROCESSING_NOT_ALLOWED);
					paymentErrPojo.setComment(MessageConstant.PRFLN100118 + profileName + " Data: "
							+ sourceIdMap.toString().replaceAll("'", "''"));

					uniPaymenTIdBatchId = batchId + "" + endToEndId;
					if (null != endToEndId && !endToEndId.trim().isEmpty()) {
						endToEndIdSet.add(uniPaymenTIdBatchId);
					}

					if (paymentLoadValMap.containsKey(batchId)) {
						PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
						pv.setInvalidBatchNo(true);
						paymentLoadValMap.put(batchId, pv);
					} else {
						PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
						pv.setInvalidBatchNo(true);
						paymentLoadValMap.put(batchId, pv);
					}

					errorBatchMap.put(key, paymentErrPojo);
					totalInitiated = totalInitiated + 1;
				} else {

					logger.info("colBatch " + colBatch + " profileName" + profileName);
					if (null != piuidSeqIdMap.get(profileName)) {
						if (null != piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName))) {
							intializeColNameForProfileName(profileName, piuidSeqIdMap);
							// colBatch = nrmlzSourceMap
							// .get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(colBatch));

							logger.info("colContrlSum " + colContrlSum);
							String batchId = sourceIdMap.get(colBatch);
							// Integer batchId = Integer.parseInt(batchId);
							String currency = sourceIdMap.get(colCurrency);
							String creationDt = sourceIdMap.get(colPaymentDate);
							String transferDt = sourceIdMap.get(colTransferDt);
							String invoiceDt = sourceIdMap.get(colInvoiceDt);
							String endToEndId = sourceIdMap.get(colPaymentIdSource);
							String instdAmt = sourceIdMap.get(colInstdAmt);
							String noOfTxn = sourceIdMap.get(colNoOftxn);
							String controlSum = sourceIdMap.get(colContrlSum);
							String invoiceNo = sourceIdMap.get(colInvoiceNo);
							String invoiceAmt = sourceIdMap.get(colInvoiceAmt);
							String paymentMethod = sourceIdMap.get(colPaymentMethod);
							String companyName = sourceIdMap.get(colCompanyName);

							logger.info("batchId " + batchId + " currency " + currency + "  creationDt " + creationDt
									+ " transferDt" + transferDt + " invoiceDt " + invoiceDt + " controlSum "
									+ controlSum + " invoiceAmt " + invoiceAmt);
							paymentErrPojo = new PaymentErrorPojo(batchId, endToEndId, invoiceNo, instdAmt, currency,
									transferDt, null, configPojo.getSOURCE_SYS_ID(), noOfTxn, internalId, profileName,
									paymentMethod, companyName, creationDt, invoiceDt, controlSum, invoiceAmt);

							uniPaymenTIdBatchId = batchId + "" + endToEndId;
							if (null != endToEndId && !endToEndId.trim().isEmpty()) {
								endToEndIdSet.add(uniPaymenTIdBatchId);
							}

							if (!XorConstant.isCLS && (null != instdAmt && !instdAmt.trim().isEmpty())) {
								try {
									instdAmt = instdAmt.trim().replace(",", "");
									new BigDecimal(instdAmt);
								} catch (Exception e) {
									paymentErrPojo.setInstdAmt(null);
									if (paymentLoadValMap.containsKey(batchId)) {
										PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
										pv.setInvalidInstdAmt(true);
										paymentLoadValMap.put(batchId, pv);
									} else {
										PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
										pv.setInvalidInstdAmt(true);
										paymentLoadValMap.put(batchId, pv);
									}

									logger.info("InstAmt Issue " + instdAmt);
								}
							}
							if (!XorConstant.isCLS && ConfigConstants.configSys.isCotrolSumCheckReq()) {
								boolean isCtrlFomulaExist = false;
								ControlSumEntity controlSumEnity = controlSumRepo
										.findByPiuidSeqIdAndSourceSysIdAndActiveInd(piuidSeqIdMap.get(profileName),
												configPojo.getSrcSys().getId(), XorConstant.STATUS_Y);
								String controlSumCal = null;
								if (null != controlSumEnity) {
									controlSumCal = getCtrlSumCheck(controlSumEnity.getFormula(), configPojo,
											internalId);
									isCtrlFomulaExist = true;
								} else {
									logger.info("Default Formaula applied");
								}

								if (null != controlSum && !controlSum.trim().isEmpty() && null != invoiceAmt
										&& !invoiceAmt.trim().isEmpty()) {
									controlSum = controlSum.trim().replaceAll(",", "");
									invoiceAmt = invoiceAmt.trim().replace(",", "");
									boolean flag = false;
									BigDecimal amount = null;
									try {
										amount = new BigDecimal(controlSum);
										logger.info("controlSum -" + controlSum);
										flag = true;
									} catch (Exception e) {
										paymentErrPojo.setControlSum(null);
										if (paymentLoadValMap.containsKey(batchId)) {
											PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
											pv.setInvalidContrlSum(true);
											paymentLoadValMap.put(batchId, pv);
										} else {
											PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
											pv.setInvalidContrlSum(true);
											paymentLoadValMap.put(batchId, pv);
										}

										flag = false;
										logger.info("ControlSum Issue " + controlSum);
									}
									if (flag) {
										flag = false;
										BigDecimal invAmount = null;
										try {
											if (isCtrlFomulaExist && null != controlSumCal) {
												invAmount = new BigDecimal(controlSumCal);
											} else {
												invAmount = new BigDecimal(invoiceAmt);
											}
											logger.info("invAmount -" + invAmount);
											flag = true;
										} catch (Exception e) {
											paymentErrPojo.setInvoiceAmt(null);
											if (paymentLoadValMap.containsKey(batchId)) {
												PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
												pv.setInvalidInvoiceAmt(true);
												paymentLoadValMap.put(batchId, pv);
											} else {
												PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
												pv.setInvalidInvoiceAmt(true);
												paymentLoadValMap.put(batchId, pv);
											}
											flag = false;
											logger.info("invoiceAmt Issue " + invoiceAmt);
										}
										if (flag) {
											if (batchControlSumMap.containsKey(batchId)) {
												BigDecimal sd = batchControlSumMap.get(batchId).subtract(invAmount);
												logger.info("sub " + sd.toString());
												batchControlSumMap.put(batchId, sd);
											} else {
												BigDecimal sd = amount.subtract(invAmount);
												logger.info("sub in" + sd.toString());
												batchControlSumMap.put(batchId, sd);
											}
										}
									}
								}
							} else {
								logger.info("ControlSum Check Not required");
								batchControlSumMap.put(batchId, new BigDecimal("0"));
							}
							if (null != noOfTxn && !noOfTxn.isEmpty() && null != endToEndId && !endToEndId.isEmpty()) {
								if (noOfTxnEndToEndIdMap.containsKey(batchId)) {
									Map<String, Set<String>> map = noOfTxnEndToEndIdMap.get(batchId);
									if (map.containsKey(noOfTxn)) {
										Set<String> endToEndSet = map.get(noOfTxn);
										endToEndSet.add(endToEndId);
										map.put(noOfTxn, endToEndSet);
										noOfTxnEndToEndIdMap.put(batchId, map);
									} else {
										Set<String> endToEndSet = new HashSet<>();
										endToEndSet.add(endToEndId);
										map.put(noOfTxn, endToEndSet);
										noOfTxnEndToEndIdMap.put(batchId, map);
									}

								} else {
									Map<String, Set<String>> map = new HashMap<>(1);
									Set<String> endToEndSet = new HashSet<>(1);
									endToEndSet.add(endToEndId);
									map.put(noOfTxn, endToEndSet);
									noOfTxnEndToEndIdMap.put(batchId, map);
								}
							}
							key = internalId + "~" + batchId + "~" + endToEndId;
							if (null == batchId || batchId.trim().isEmpty()) {
								paymentErrPojo.setMsgId(null);
								if (paymentLoadValMap.containsKey(batchId)) {
									PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
									pv.setInvalidBatchNo(true);
									paymentLoadValMap.put(batchId, pv);
								} else {
									PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
									pv.setInvalidBatchNo(true);
									paymentLoadValMap.put(batchId, pv);
								}
								// paymentErrPojo.setMsgId(XorConstant._000_INVMSGID + internalId);
								paymentErrPojo.setComment(MessageConstant.BATCH100100);
								errorBatchMap.put(key, paymentErrPojo);
								auditJPARepo.addBatchAuditMessage(internalId, batchId, null,
										MessageConstant.BATCH100101, AuditJPARepo.VALIDATION_FAILED);
							} else if (null == endToEndId || endToEndId.trim().isEmpty()) {
								paymentErrPojo.setEndToEndId(null);
								if (paymentLoadValMap.containsKey(batchId)) {
									PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
									pv.setInvalidEndToEndId(true);
									paymentLoadValMap.put(batchId, pv);
								} else {
									PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
									pv.setInvalidEndToEndId(true);
									paymentLoadValMap.put(batchId, pv);
								}
								paymentErrPojo.setComment(MessageConstant.PYMT100102);
								errorBatchMap.put(key, paymentErrPojo);
								auditJPARepo.addBatchAuditMessage(internalId, batchId, null, MessageConstant.PYMT100103,
										AuditJPARepo.VALIDATION_FAILED);
							} else if (null == noOfTxn || noOfTxn.trim().isEmpty()) {
								if (paymentLoadValMap.containsKey(batchId)) {
									PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
									pv.setInvalidNoOfTx(true);
									paymentLoadValMap.put(batchId, pv);
								} else {
									PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
									pv.setInvalidNoOfTx(true);
									paymentLoadValMap.put(batchId, pv);
								}
								paymentErrPojo.setComment(MessageConstant.NOTX100123);
								errorBatchMap.put(key, paymentErrPojo);
								auditJPARepo.addBatchAuditMessage(internalId, batchId, null, MessageConstant.NOTX100104,
										AuditJPARepo.VALIDATION_FAILED);
							} else if (!XorConstant.isCLS && (null == controlSum || controlSum.trim().isEmpty())) {
								paymentErrPojo.setControlSum(null);
								if (paymentLoadValMap.containsKey(batchId)) {
									PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
									pv.setInvalidContrlSum(true);
									paymentLoadValMap.put(batchId, pv);
								} else {
									PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
									pv.setInvalidContrlSum(true);
									paymentLoadValMap.put(batchId, pv);
								}
								paymentErrPojo.setComment(MessageConstant.CTRL100120);
								errorBatchMap.put(key, paymentErrPojo);
								auditJPARepo.addBatchAuditMessage(internalId, batchId, null,
										MessageConstant.CTLSUM100105, AuditJPARepo.VALIDATION_FAILED);
							} else if (!XorConstant.isCLS && (null == instdAmt || instdAmt.trim().isEmpty())) {
								paymentErrPojo.setInstdAmt(null);
								if (paymentLoadValMap.containsKey(batchId)) {
									PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
									pv.setInvalidInstdAmt(true);
									paymentLoadValMap.put(batchId, pv);
								} else {
									PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
									pv.setInvalidInstdAmt(true);
									paymentLoadValMap.put(batchId, pv);
								}
								paymentErrPojo.setComment(MessageConstant.INTD100125);
								errorBatchMap.put(key, paymentErrPojo);
								auditJPARepo.addBatchAuditMessage(internalId, batchId, null, MessageConstant.INTD100126,
										AuditJPARepo.VALIDATION_FAILED);
							}

							else if (!XorConstant.isCLS && (null == invoiceNo || invoiceNo.trim().isEmpty())) {
								paymentErrPojo.setInvoiceNo(null);
								if (paymentLoadValMap.containsKey(batchId)) {
									PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
									pv.setInvalidInvoiceNo(true);
									paymentLoadValMap.put(batchId, pv);
								} else {
									PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
									pv.setInvalidInvoiceNo(true);
									paymentLoadValMap.put(batchId, pv);
								}
								paymentErrPojo.setComment(MessageConstant.PYMTINV100106);
								errorBatchMap.put(key, paymentErrPojo);
								auditJPARepo.addBatchAuditMessage(internalId, batchId, null,
										MessageConstant.INVPYM100107, AuditJPARepo.VALIDATION_FAILED);
							} else if (!XorConstant.isCLS && (null == invoiceAmt || invoiceAmt.trim().isEmpty())) {
								paymentErrPojo.setInvoiceAmt(null);
								if (paymentLoadValMap.containsKey(batchId)) {
									PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
									pv.setInvalidInvoiceAmt(true);
									paymentLoadValMap.put(batchId, pv);
								} else {
									PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
									pv.setInvalidInvoiceAmt(true);
									paymentLoadValMap.put(batchId, pv);
								}
								paymentErrPojo.setComment(MessageConstant.PYMTINV100108);
								errorBatchMap.put(key, paymentErrPojo);
								auditJPARepo.addBatchAuditMessage(internalId, batchId, null,
										MessageConstant.INVVAL100109, AuditJPARepo.VALIDATION_FAILED);
							} else if (null == currency || currency.trim().isEmpty()) {
								paymentErrPojo.setCcy(null);
								if (paymentLoadValMap.containsKey(batchId)) {
									PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
									pv.setInvalidCurrency(true);
									paymentLoadValMap.put(batchId, pv);
								} else {
									PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
									pv.setInvalidCurrency(true);
									paymentLoadValMap.put(batchId, pv);
								}
								paymentErrPojo.setComment(MessageConstant.CURR100110);
								errorBatchMap.put(key, paymentErrPojo);
								auditJPARepo.addBatchAuditMessage(internalId, batchId, null,
										MessageConstant.CURRVAL100111, AuditJPARepo.VALIDATION_FAILED);
							} else if (!XorConstant.isCLS && (null == creationDt || creationDt.trim().isEmpty()
									|| creationDt.trim().length() > 19 || creationDt.trim().length() < 10
									|| !creationDt.trim().contains("T"))) {
								paymentErrPojo.setCreationDate(null);
								if (paymentLoadValMap.containsKey(batchId)) {
									PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
									pv.setInvalidCreationDt(true);
									paymentLoadValMap.put(batchId, pv);
								} else {
									PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
									pv.setInvalidCreationDt(true);
									paymentLoadValMap.put(batchId, pv);
								}
								paymentErrPojo.setComment(MessageConstant.CRTIONDT100112);
								errorBatchMap.put(key, paymentErrPojo);
								auditJPARepo.addBatchAuditMessage(internalId, batchId, null,
										MessageConstant.CRTIONDTVAL100113, AuditJPARepo.VALIDATION_FAILED);
							} else if ((!XorConstant.isCLS && (null != transferDt && !transferDt.trim().isEmpty())
									&& (transferDt.trim().length() > 19 || transferDt.trim().length() < 10
											|| !transferDt.trim().contains("T")))) {
								paymentErrPojo.setTransferDt(null);
								if (paymentLoadValMap.containsKey(batchId)) {
									PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
									pv.setInvalidTransferDt(true);
									paymentLoadValMap.put(batchId, pv);
								} else {
									PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
									pv.setInvalidTransferDt(true);
									paymentLoadValMap.put(batchId, pv);
								}
								paymentErrPojo.setComment(MessageConstant.TRANSFRDT100115);
								errorBatchMap.put(key, paymentErrPojo);
								auditJPARepo.addBatchAuditMessage(internalId, batchId, null,
										MessageConstant.TRANSFRDT100114, AuditJPARepo.VALIDATION_FAILED);
							} else if ((!XorConstant.isCLS && (null != invoiceDt && !invoiceDt.trim().isEmpty())
									&& (invoiceDt.trim().length() > 19 || invoiceDt.trim().length() < 10
											|| !invoiceDt.trim().contains("T")))) {
								paymentErrPojo.setInvoiceDt(null);
								if (paymentLoadValMap.containsKey(batchId)) {
									PaymentLoadValidatioPojo pv = paymentLoadValMap.get(batchId);
									pv.setInvalidInvoiceDt(true);
									paymentLoadValMap.put(batchId, pv);
								} else {
									PaymentLoadValidatioPojo pv = new PaymentLoadValidatioPojo();
									pv.setInvalidInvoiceDt(true);
									paymentLoadValMap.put(batchId, pv);
								}
								paymentErrPojo.setComment(MessageConstant.INVDTIS100117);
								errorBatchMap.put(key, paymentErrPojo);
								auditJPARepo.addBatchAuditMessage(internalId, batchId, null,
										MessageConstant.INVICEDTV100116, AuditJPARepo.VALIDATION_FAILED);
							} else {

								flag = false;
								if (batchMap.containsKey(batchId)) {
									Set<String> profileNameList = batchMap.get(batchId);
									if (null != profileNameList) {
										profileNameList.add(profileName);
										if (profileNameList.size() > 1) {
											logger.info("Batch " + batchId + "having multiple piuid "
													+ profileNameList.toString() + " , Not allowed to process");
											isMoreThanOneProfileNameForBatch = true;
											batchDataMap.remove(batchId);
											paymentErrPojo.setComment("Batch " + batchId + "having multiple piuid "
													+ profileNameList.toString() + " , Not allowed to process");
											errorBatchMap.put(key, paymentErrPojo);
											auditJPARepo.addBatchAuditMessage(internalId, batchId, null,
													"Batch " + batchId + "having multiple piuid. ",
													AuditJPARepo.PROCESSING_NOT_ALLOWED);
										} else {
											flag = true;
										}
										if (!profileMap.containsKey(profileName.trim())) {
											logger.info(MessageConstant.PRFLN100118 + profileName);
											batchDataMap.remove(batchId);
											paymentErrPojo.setComment(MessageConstant.PRFLN100118 + profileName);
											errorBatchMap.put(key, paymentErrPojo);
											auditJPARepo.addBatchAuditMessage(internalId, batchId, null,
													MessageConstant.PRFLN100118 + profileName,
													AuditJPARepo.PROCESSING_NOT_ALLOWED);
										} else {
											flag = true;
										}

										if (flag) {
											List<Map<String, String>> dataList = batchDataMap.get(batchId);
											logger.info("dataList -" + dataList);
											logger.info("sourceIdMap -" + sourceIdMap);
											dataList.add(sourceIdMap);
											batchDataMap.put(batchId, dataList);

											if (validBatchMap.containsKey(batchId)) {

												List<PaymentErrorPojo> payErrEn = validBatchMap.get(batchId);
												payErrEn.add(paymentErrPojo);
												validBatchMap.put(batchId, payErrEn);
											} else {
												List<PaymentErrorPojo> payErrEn = new ArrayList<>();
												payErrEn.add(paymentErrPojo);
												validBatchMap.put(batchId, payErrEn);
											}
										}
										batchMap.put(batchId, profileNameList);
									}
								} else {
									Set<String> profileNameList = new HashSet<>(1);
									profileNameList.add(profileName);
									batchMap.put(batchId, profileNameList);
									if (!profileMap.containsKey(profileName.trim())) {
										logger.info(MessageConstant.PRFLN100118 + profileName);
										paymentErrPojo.setComment(MessageConstant.PRFLN100118 + profileName);
										errorBatchMap.put(key, paymentErrPojo);
										auditJPARepo.addBatchAuditMessage(internalId, batchId, null,
												MessageConstant.PRFLN100118 + profileName,
												AuditJPARepo.PROCESSING_NOT_ALLOWED);
									} else {
										List<Map<String, String>> dataList = new ArrayList<>(1);
										dataList.add(sourceIdMap);
										batchDataMap.put(batchId, dataList);
										List<PaymentErrorPojo> payErrEn = new ArrayList<>();
										payErrEn.add(paymentErrPojo);
										validBatchMap.put(batchId, payErrEn);
									}
								}
							}
						} // piuidSeqIdMap.get(profileName)
						else {
							logger.info("Profilename not registered with application - " + profileName);
							auditJPARepo.addBatchAuditMessage(internalId, null, null,
									MessageConstant.PRFLN100118 + profileName, AuditJPARepo.PROCESSING_NOT_ALLOWED);
							paymentErrPojo.setComment(
									MessageConstant.PRFLN100118 + profileName + " Data: " + sourceIdMap.toString());
							paymentErrPojo.setMsgId(XorConstant._000_INVMSGID + internalId);
							paymentErrPojo.setEndToEndId(XorConstant._00_INVENDID + internalId);
							errorBatchMap.put(key, paymentErrPojo);
							totalInitiated = totalInitiated + 1;
						}
					} // piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName))
					else {
						logger.info(MessageConstant.PRFLN100119 + profileName);
						auditJPARepo.addBatchAuditMessage(internalId, null, null,
								MessageConstant.PRFLN100119 + profileName, AuditJPARepo.PROCESSING_NOT_ALLOWED);
						paymentErrPojo.setComment(
								MessageConstant.PRFLN100119 + profileName + " Data: " + sourceIdMap.toString());
						paymentErrPojo.setMsgId(XorConstant._000_INVMSGID + internalId);
						paymentErrPojo.setEndToEndId(XorConstant._00_INVENDID + internalId);
						errorBatchMap.put(key, paymentErrPojo);
						totalInitiated = totalInitiated + 1;
					}
				}

			});

			batchControlSumMap.entrySet().stream().forEach(e -> {
				String batch = e.getKey();
				BigDecimal amt = e.getValue();
				logger.info(batch + " " + amt.toString());
				if (0 != amt.compareTo(new BigDecimal("0"))) {
					List<PaymentErrorPojo> pr = validBatchMap.get(batch);
					if (pr != null && !pr.isEmpty()) {
						pr.stream().forEach(paymentErrPojo -> {
							paymentErrPojo.setComment(MessageConstant.CTRL100120);
							errorBatchMap.put(paymentErrPojo.getInternalId() + "~" + batch, paymentErrPojo);
							auditJPARepo.addBatchAuditMessage(paymentErrPojo.getInternalId(), batch, null,
									MessageConstant.CTRL100121, AuditJPARepo.VALIDATION_FAILED);
							batchDataMap.remove(batch);
						});
						validBatchMap.remove(batch);
					}
				}
			});

			noOfTxnEndToEndIdMap.entrySet().stream().forEach(e -> {
				String batch = e.getKey();
				Map<String, Set<String>> nofTrxEndSet = e.getValue();
				nofTrxEndSet.entrySet().stream().forEach(ee -> {
					String noOfTx = ee.getKey();
					logger.info("noOfTx --> " + noOfTx + "  " + ee.getValue().toString());

					int count = ee.getValue().size();
					if (count != Integer.parseInt(noOfTx)) {
						List<PaymentErrorPojo> pr = validBatchMap.get(batch);
						if (pr != null && !pr.isEmpty()) {
							pr.stream().forEach(paymentErrPojo -> {
								paymentErrPojo.setComment(MessageConstant.NOTX100123);
								errorBatchMap.put(paymentErrPojo.getInternalId() + "~" + batch, paymentErrPojo);
								auditJPARepo.addBatchAuditMessage(paymentErrPojo.getInternalId(), batch, null,
										MessageConstant.NOTX100122, AuditJPARepo.VALIDATION_FAILED);
								batchDataMap.remove(batch);
							});
							validBatchMap.remove(batch);
						}
					}
				});
			});
			paymentLoadValMap.entrySet().stream().forEach(e -> {
				PaymentLoadValidatioPojo valO = e.getValue();

				// if (isValidPymet(valO)) {
				String batchId = e.getKey();
				batchDataMap.remove(batchId);
				if (null != validBatchMap.get(batchId)) {

					List<PaymentErrorPojo> obj = validBatchMap.get(batchId);
					obj.stream().forEach(p -> {
						String key = p.getInternalId() + "~" + p.getMsgId() + "~" + p.getEndToEndId();
						PaymentErrorPojo per = null;
						if (errorBatchMap.containsKey(key)) {
							per = errorBatchMap.get(key);
						}
						if (null != per && null != per.getComment()
								&& !per.getComment().contains(MessageConstant.BATCH100124)) {
							p.setComment(MessageConstant.BATCH100124 + " " + per.getComment());
						} else {
							p.setComment(MessageConstant.BATCH100124);
						}

						logger.info(batchId + " *****************--> " + p.getComment());
						errorBatchMap.put(key, p);
					});
					validBatchMap.remove(batchId);
				}
				// }

			});

			totalInitiated = totalInitiated + endToEndIdSet.size();
			updateDashBoardDetails(configPojo.getSOURCE_SYS_ID(), totalInitiated);
			auditJPARepo.audiLogger(processId, sourceIdMapList.size(), AuditJPARepo.STGTONRM);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return isMoreThanOneProfileNameForBatch;

	}

	private String getCtrlSumCheck(String formula, ConfigPojo configPojo, String internalId) {

		try {
			StringBuilder selectQuery = new StringBuilder(
					"select " + formula + "  from " + configPojo.getSOURCE_TABLE_NAME());
			selectQuery.append(" where " + sourceColumnIdentityName + " = " + internalId);
			logger.info("selectQuery : CtrlSum --->> " + selectQuery);
			List<Object[]> rsl = getResultSet(selectQuery.toString());
			String val = "0";
			if (rsl.size() > 0) {
				for (Object rs : rsl) {
					logger.info("Control Sum -> " + rs.toString());
					val = rs.toString();
				}
			}
			return val;
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}

	}

	private boolean isValidPymet(PaymentLoadValidatioPojo valO) {
		return valO.isInvalidBatchNo() || valO.isInvalidContrlSum() || valO.isInvalidCreationDt()
				|| valO.isInvalidCurrency() || valO.isInvalidEndToEndId() || valO.isInvalidInstdAmt()
				|| valO.isInvalidInvoiceAmt() || valO.isInvalidInvoiceDt() || valO.isInvalidInvoiceNo()
				|| valO.isInvalidNoOfTx() || valO.isInvalidTransferDt();
	}

	private void intializeColNameForProfileName(String profileName, Map<String, Integer> piuidSeqIdMap) {

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
		colContrlSum = nrmlzSourceMap
				.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL133_CTRLSUM));

		colInvoiceNo = nrmlzSourceMap
				.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL59_NB));
		colInvoiceAmt = nrmlzSourceMap
				.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL62_DUEPYBLAM));
		colCompanyName = nrmlzSourceMap
				.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL8_NM));

		colPaymentMethod = nrmlzSourceMap
				.get(piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL11_PMTMTD));

		ColPojo colPojo = new ColPojo(colPaymentIdSource, colBatch, colNoOftxn, colPartyName, colPaymentDate,
				colProfileName, colCurrency, colTransferDt, colInvoiceDt, colInstdAmt, colContrlSum, colInvoiceNo,
				colInvoiceAmt, colCompanyName, colPaymentMethod);
		colProfileMap.put(profileName, colPojo);

		logger.info("Base col initialized for profileName -" + profileName + " colBatch -" + colBatch + " colNoOftxn -"
				+ colNoOftxn + " colPartyName -" + colPartyName + " colPaymentDate -" + colPaymentDate
				+ " colPaymentIdSource -" + colPaymentIdSource + " colCurrency " + colCurrency + " colTransferDt "
				+ colTransferDt + " " + " colInvoiceDt " + colInvoiceDt + " colContrlSum " + colContrlSum);
	}

	private void initializeBasicColName(String type, Map<String, Integer> piuidSeqIdMap, ConfigPojo configPojo) {
		getMappedSourceColumnName(null, null, type, piuidSeqIdMap, configPojo);

	}

	public void stgToNrmlzPreProcess(String processId, ConfigPojo configPojo, boolean isToReload,
			Map<String, String> batchUUIDMap) {

		entityManager = entityManager.getEntityManagerFactory().createEntityManager();
		Session session = entityManager.unwrap(Session.class);
		Transaction tr = session.beginTransaction();
		session.doWork(new Work() {

			@Override
			public void execute(Connection connections) throws SQLException {
				try (Connection connection = connections) {
					connection.setAutoCommit(true);
					moveprocessedDate();
					try {
						if (isToReload) {
							checkRequiredTable(configPojo);
							config.updateReloadFlag(false);
						}
						StgToNrmlPreeRepo.updateStatusForInitiated(configPojo, query, entityManager, batchUUIDMap);
					} catch (Exception e) {
						logger.error("STGTONRM Exeption ", e);
						throw e;
					}

				} catch (Exception e) {
					logger.error("STGTONRM Exeption ", e);
					throw e;
				}

			}
		});
	}

	public void stgToNrmlzProcess(String processId, ConfigPojo configPojo, boolean isToReload,
			Map<String, String> batchUUIDMap) {

		entityManager = entityManager.getEntityManagerFactory().createEntityManager();
		Session session = entityManager.unwrap(Session.class);
		Transaction tr = session.beginTransaction();
		session.doWork(new Work() {

			@Override
			public void execute(Connection connections) throws SQLException {
				try (Connection connection = connections) {
					connection.setAutoCommit(true);
					try {
						int recordCount = 0;
						Integer srcSysId = configPojo.getSOURCE_SYS_ID();
						List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList = dynamicNrmlzColumnRepo
								.findBySrcSysIdAndActiveIndOrderByIdAsc(srcSysId, XorConstant.STATUS_Y);
						System.out.println("hi"+dynamicNrmlColEntityList.get(0).toString());
						// Data set
						List<DynamicNrmlzColumnEntity> dynamicDistinctColumn = new ArrayList<>(1);
						List<Map<String, String>> sourceIdMapList = getResultFromSource(dynamicNrmlColEntityList,
								dynamicDistinctColumn, configPojo);

						if (null != sourceIdMapList) {
							if (!sourceIdMapList.isEmpty()) {

								stgToNrmlzProcessBegin(processId, srcSysId, recordCount, dynamicNrmlColEntityList,
										dynamicDistinctColumn, sourceIdMapList, configPojo, batchUUIDMap);
							} else {
								logger.info("STG No data to process");
							}
						}
					} catch (Exception e) {
						logger.error("STGToNrm Exception", e);
						throw e;
					}

				} catch (Exception e) {
					logger.error("STGToNrm Exception", e);
					throw e;
				}

			}
		});
	}

	private void stgToNrmlzProcessBegin(String processId, Integer srcSysId, int recordCount,
			List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList,
			List<DynamicNrmlzColumnEntity> dynamicDistinctColumn, List<Map<String, String>> sourceIdMapList,
			ConfigPojo configPojo, Map<String, String> batchUUIDMap) {
		Map<String, Integer> profileMap = piuidRepo.findByActiveInd(XorConstant.STATUS_Y).stream()
				.collect(Collectors.toMap(PIUIDEntity::getProfileName, PIUIDEntity::getId));
		System.out.println(profileMap);
		Map<String, List<Map<String, String>>> batchDataMap = checkData(processId, dynamicDistinctColumn,
				sourceIdMapList, profileMap, configPojo, batchUUIDMap);

		if (!batchDataMap.isEmpty()) {
			List<Map<String, String>> sourceIdMapListToProcess = new ArrayList<>();
			batchDataMap.entrySet().stream().forEach(e -> {
				sourceIdMapListToProcess.addAll(e.getValue());
			});
			if (!sourceIdMapListToProcess.isEmpty()) {
				prceedStgPrcoess(processId, srcSysId, recordCount, dynamicNrmlColEntityList, dynamicDistinctColumn,
						sourceIdMapListToProcess, configPojo, batchUUIDMap);
			} else {
				logger.info("0 No data to process");
				// return false;
			}
		} else {
			logger.info("E No data to process");
			// return false;
		}
	}

	private Map<String, List<Map<String, String>>> checkData(String processId,
			List<DynamicNrmlzColumnEntity> dynamicDistinctColumn, List<Map<String, String>> sourceIdMapList,
			Map<String, Integer> profileMap, ConfigPojo configPojo, Map<String, String> batchUUIDMap) {
		Map<String, List<Map<String, String>>> batchDataMap = new HashMap<>(1);
		errorBatchSet.clear();
		validBatchSet.clear();
		Map<String, PaymentLoadValidatioPojo> paymentLoadValMap = new HashMap<>();
		checkProfileNameForBatch(processId, dynamicDistinctColumn, sourceIdMapList, batchDataMap, profileMap,
				errorBatchSet, validBatchSet, configPojo, paymentLoadValMap);

		logger.info("errorBatchSet " + errorBatchSet.size());
		logger.info("validBatchSet " + validBatchSet.size());
		if (!errorBatchSet.isEmpty() || !validBatchSet.isEmpty()) {
			insertErrorDataForBatch(errorBatchSet, validBatchSet, dynamicDistinctColumn, configPojo, batchUUIDMap,
					paymentLoadValMap);
			paymentLoadValMap = null;
		}

		CommonJPAConstant.clearObject();

		return batchDataMap;
	}

	private void prceedStgPrcoess(String processId, Integer srcSysId, int recordCount,
			List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList,
			List<DynamicNrmlzColumnEntity> dynamicDistinctColumn, List<Map<String, String>> sourceIdMapListToProcess,
			ConfigPojo configPojo, Map<String, String> batchUUIDMap) {
		ProcessStatusEntity procRunnigState = null;
		try {
			procRunnigState = loaderCommonService.setProcessStatusEntityRunning(procRunnigState, recordCount,
					XorConstant.RUNNING, XorConstant.LOAD_NRMLZ_PMT_DTLS, configPojo);
			// int STG_H_IndentityId = getSTGIndentity(dynamicNrmlColEntityList,
			// configPojo);
			StringBuilder inserQuerySTG_H = getInsertQueryForSTG(dynamicDistinctColumn, "_h ", configPojo);
			StringBuilder inserQuerySTG_E = getInsertQueryForSTG(dynamicDistinctColumn, "_e ", configPojo);
			StringBuilder insertIntoNrml = getInsertQueryForNrmlzPmtDtlsOns(dynamicNrmlColEntityList);
			insertIntoNrmlzPmtDtlsOns(processId, insertIntoNrml, dynamicNrmlColEntityList, sourceIdMapListToProcess,
					inserQuerySTG_H, inserQuerySTG_E, dynamicDistinctColumn, srcSysId, configPojo, batchUUIDMap);

			procRunnigState = loaderCommonService.setProcessStatusComplete(procRunnigState, XorConstant.COMPLETE);
		} catch (Exception e) {
			e.printStackTrace();
			procRunnigState = loaderCommonService.setProcessStatusComplete(procRunnigState, XorConstant.ERROR);
		}
	}

	public void insertIntoNrmlzPmtDtlsOns(String processId, StringBuilder insertQueryNrm,
			List<DynamicNrmlzColumnEntity> dynmicNrmlColList, List<Map<String, String>> sourceIdMapList,
			StringBuilder inserQuerySTG_H, StringBuilder inserQuerySTG_E,
			List<DynamicNrmlzColumnEntity> dynamicUniqueColumn, Integer srcSysId, ConfigPojo configPojo,
			Map<String, String> batchUUIDMap) {

		try {
			List<String> paymentIdList = sourceIdMapList.stream()
					.map(sourceIdMap -> sourceIdMap.get(colPaymentIdSource)).collect(Collectors.toList());

			List<AggregatedPaymentEntity> agg = aggregatedPaymentRepo.findByEndToEndIdIn(paymentIdList);

			if (null != agg && agg.size() > 0) {
				endToEndIdList = agg.stream().map(eagg -> eagg.getEndToEndId()).collect(Collectors.toSet());
			}
			List<AggregatedPaymentHistoryEntity> aggH = aggregatedPaymentHistoryRepo
					.findByEndtoendid33In(paymentIdList);
			if (null != aggH && aggH.size() > 0) {
				endToEndIdList.addAll(aggH.stream().map(eagg -> eagg.getEndtoendid33()).collect(Collectors.toSet()));
			}
			Map<String, BatchDetailStatusEntity> batchEntityMap = new HashMap<>();
			entityManager.flush();
			entityManager.clear();

			queryList = new StringBuilder();
			queryListSTGE = new StringBuilder();
			identiList.clear();
			identiListE.clear();
			// paymentIdSet.clear();
			auditJPARepo.batchErrMessageList.clear();
			setSourceColumnIdentityName(sourceIdMapList, dynamicUniqueColumn, configPojo);
			Map<String, List<PaymentErrorPojo>> batchPaymObjEr = new HashMap<>();
			Map<String, String> errBatchUuidMap = new HashMap<>();
			// Map<String, BigDecimal> batchCtrlSumMap = new HashMap<>();
			Set<String> endtoEndSet = new HashSet<>();
			Set<String> errendtoEndSet = new HashSet<>();
			sourceIdMapList.stream().forEach(sourceIdMap -> {
				String internalId = sourceIdMap.get(sourceColumnIdentityName);

				String batchId = sourceIdMap.get(colBatch);
				String currency = sourceIdMap.get(colCurrency);
				String creationDt = sourceIdMap.get(colPaymentDate);
				String transferDt = sourceIdMap.get(colTransferDt);
				String invoiceDt = sourceIdMap.get(colInvoiceDt);
				String endToEndId = sourceIdMap.get(colPaymentIdSource);
				String instdAmt = sourceIdMap.get(colInstdAmt);
				String invoiceNo = sourceIdMap.get(colInvoiceNo);
				String noOfTxn = sourceIdMap.get(colNoOftxn);
				String paymentMethod = sourceIdMap.get(colPaymentMethod);
				String companyName = sourceIdMap.get(colCompanyName);
				String internalUuid = batchUUIDMap.get(batchId);
				String controlSum = sourceIdMap.get(colContrlSum);
				String invoiceAmt = sourceIdMap.get(colInvoiceAmt);

				logger.info(internalUuid + " At sttonrm--> batchId " + batchId + " currency " + currency
						+ "  creationDt " + creationDt + " transferDt" + transferDt + " invoiceDt " + invoiceDt);
				PaymentErrorPojo paymentErrPojo = new PaymentErrorPojo(batchId, endToEndId, invoiceNo, instdAmt,
						currency, transferDt, null, configPojo.getSOURCE_SYS_ID(), noOfTxn, internalId, null,
						paymentMethod, companyName, creationDt, invoiceDt, controlSum, invoiceAmt);

				if (null != colPaymentIdSource) {
					// check for duplicate and move to E on the basis of paymentId/endToendid
					String paymentId = sourceIdMap.get(colPaymentIdSource);

					if (!endToEndIdList.contains(paymentId)) {
						try {
							queryList = new StringBuilder();
							createInsertScript(internalUuid, insertQueryNrm, dynmicNrmlColList, sourceIdMap,
									CommonJPAConstant.NRMLZ, null, configPojo, XorConstant.STATUS_NORMALIZED);

							queryList.append(insertNrmlQuery);

							query = entityManager.createNativeQuery(queryList.toString());
							query.executeUpdate();
							endtoEndSet.add(endToEndId);
							updateProcesseBatch(configPojo, batchId, controlSum, invoiceAmt, endtoEndSet, colBatch,
									internalUuid);

							entityManager.flush();
							entityManager.clear();
							// keep for batch update
							addToBatchEntityMap(batchEntityMap, sourceIdMap, batchId, configPojo);
							auditJPARepo.addBatchAuditMessage(internalId, "" + batchId, paymentId,
									AuditJPARepo.SUCCESSFULLY, AuditJPARepo.PROCESSED);
						} catch (Exception ex) {
							ex.printStackTrace();
							errBatchUuidMap.put(batchId, internalUuid);
							logger.info(ex.getMessage() + "Exception in query--- > " + queryList);
							logger.info("save data error ex ");
							paymentErrPojo.setComment("Data Error " + ex.getMessage());
							updateStgProcesseBatch(configPojo, batchId, internalUuid, endToEndId,
									"Data Error " + ex.getMessage(), errendtoEndSet);
							// insertToSTGE(internalUuid, sourceIdMapList.get(0), inserQuerySTG_E,
							// dynamicUniqueColumn,
							// sourceIdMap, "Data Error " + ex.getMessage(), paymentErrPojo, configPojo);
							auditJPARepo.addBatchAuditMessage(internalId, "" + batchId, paymentId,
									"Data Error " + ex.getMessage(), AuditJPARepo.FAILED);
						}
					} else {
						errBatchUuidMap.put(batchId, internalUuid);
						paymentErrPojo.setComment("Duplicate Payment");
						updateStgProcesseBatch(configPojo, batchId, internalUuid, endToEndId, "Duplicate Payment",
								errendtoEndSet);
						// insertToSTGE(internalUuid, sourceIdMapList.get(0), inserQuerySTG_E,
						// dynamicUniqueColumn,
						// sourceIdMap, "Duplicate Payment", paymentErrPojo, configPojo);
						auditJPARepo.addBatchAuditMessage(internalId, "" + batchId, paymentId, "Duplicate Payment",
								AuditJPARepo.FAILED);
					}
				} else {
					errBatchUuidMap.put(batchId, internalUuid);
					paymentErrPojo.setComment("The ProfileName is not configured");
					updateStgProcesseBatch(configPojo, batchId, internalUuid, endToEndId,
							"The ProfileName is not configured", errendtoEndSet);
					// insertToSTGE(internalUuid, sourceIdMapList.get(0), inserQuerySTG_E,
					// dynamicUniqueColumn,
					// sourceIdMap, "The ProfileName is not configured", paymentErrPojo,
					// configPojo);
					auditJPARepo.addBatchAuditMessage(internalId, "" + batchId, null,
							"The ProfileName is not configured", AuditJPARepo.FAILED);
				}

				if (batchPaymObjEr.containsKey(batchId)) {
					List<PaymentErrorPojo> paymentErrPojoL = batchPaymObjEr.get(batchId);
					paymentErrPojoL.add(paymentErrPojo);
					batchPaymObjEr.put(batchId, paymentErrPojoL);
				} else {
					List<PaymentErrorPojo> paymentErrPojoL = new ArrayList<>(1);
					paymentErrPojoL.add(paymentErrPojo);
					batchPaymObjEr.put(batchId, paymentErrPojoL);
				}
			});

			// batchCtrlSumMap.clear();
			endtoEndSet.clear();

			updateAckDetails(configPojo, batchPaymObjEr, errBatchUuidMap);

			identiList.clear();
			queryList = new StringBuilder();
			createInsertScript(null, inserQuerySTG_H, dynamicUniqueColumn, sourceIdMapList.get(0), "STGH", null,
					configPojo, XorConstant.STATUS_PAYMENT_PROCESSED);
			queryList.append(insertNrmlQuery);
			logger.info("queryList-->> " + queryList);

			query = entityManager.createNativeQuery(queryList.toString());

			query.executeUpdate();
			entityManager.flush();
			entityManager.clear();
			// Move data to error table
			insertToSTGE(inserQuerySTG_E, dynamicUniqueColumn, configPojo);
			// Update batch
			updateBatchDetailsStatus(batchEntityMap);
			// Clear stg table
			cleanSourceTable(configPojo.getSOURCE_TABLE_NAME());
			CommonJPAConstant.clearObject();
			// }
			auditJPARepo.audiLogger(processId, null, AuditJPARepo.STGTONRM);
			// batchPaymObjEr.clear();
			// errBatchUuidMap.clear();
			// errendtoEndSet.clear();
		} catch (Exception e) {
			entityManager.clear();
			logger.info("insertIntoNrmlzPmtDtlsOns " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void updateProcesseBatch(ConfigPojo configPojo, String batchId, String controlSum, String invoiceAmt,
			Set<String> endtoEndSet, String colBatch, String internalUuid) {
		// control sum is checked before using formula if exist
		// BigDecimal inv = new BigDecimal(invoiceAmt);
		// if (batchCtrlSumMap.containsKey(batchId)) {
		// BigDecimal suminv = inv.add(batchCtrlSumMap.get(batchId));
		// batchCtrlSumMap.put(batchId, suminv);
		// } else {
		// batchCtrlSumMap.put(batchId, inv);
		// }
		// if (controlSum.equals(batchCtrlSumMap.get(batchId).toString())
		// || 0 == batchCtrlSumMap.get(batchId).compareTo(new BigDecimal(controlSum))) {
		String updateStatus = "update " + XorConstant.NRMLZ_PMT_DTLS + " set process_status ='"
				+ XorConstant.STATUS_PAYMENT_PROCESSED + "' where  " + XorConstant.NRM_MESSAGE_COLNAME + "='" + batchId
				+ "'  and internal_uuid = '" + internalUuid + "' ";
		logger.info("updateStatus ---- >" + updateStatus);

		query = entityManager.createNativeQuery(updateStatus);
		query.executeUpdate();

		String updateStgStatus = "update " + configPojo.getSOURCE_TABLE_NAME() + " set process_status ='"
				+ XorConstant.STATUS_PAYMENT_PROCESSED + "' where " + colBatch + " ='" + batchId
				+ "'   and internal_uuid = '" + internalUuid + "' ";
		logger.info("updateStgStatus ---- >" + updateStgStatus);

		query = entityManager.createNativeQuery(updateStgStatus);
		query.executeUpdate();
		// }else {
		// logger.info("controlSum mismatch "+ controlSum + "
		// batchCtrlSumMap.get(batchId) "+ batchCtrlSumMap.get(batchId).toString()+"
		// batchCtrlSumMap.get(batchId).compareTo(new BigDecimal(controlSum) "+
		// batchCtrlSumMap.get(batchId).compareTo(new BigDecimal(controlSum)));
		// }
	}

	private void updateStgProcesseBatch(ConfigPojo configPojo, String batchId, String internalUuid, String endToEndId,
			String comment, Set<String> errendtoEndSet) {

		if (errendtoEndSet.add(internalUuid)) {
			String updateStgStatus = "update " + configPojo.getSOURCE_TABLE_NAME() + " set process_status ='"
					+ XorConstant.STATUS_E + "', etl_process_comment='" + MessageConstant.BATCH100124 + "' where "
					+ colBatch + " ='" + batchId + "'   and internal_uuid = '" + internalUuid + "' ";
			logger.info("updateStgStatus ---- >" + updateStgStatus);

			query = entityManager.createNativeQuery(updateStgStatus);
			query.executeUpdate();
		}
		String updateStgStatus = "update " + configPojo.getSOURCE_TABLE_NAME() + " set process_status ='"
				+ XorConstant.STATUS_E + "', etl_process_comment='" + comment + "' where " + colPaymentIdSource + " ='"
				+ endToEndId + "'   and internal_uuid = '" + internalUuid + "' ";
		logger.info("updateStgStatus ---- >" + updateStgStatus);

		query = entityManager.createNativeQuery(updateStgStatus);
		query.executeUpdate();

	}

	private void updateAckDetails(ConfigPojo configPojo, Map<String, List<PaymentErrorPojo>> batchPaymObjEr,
			Map<String, String> errBatchUuidMap) {
		CommonJPAConstant.clearObject();
		errBatchUuidMap.entrySet().stream().forEach(batchMap -> {
			String batchId = batchMap.getKey();
			String interUUID = batchMap.getValue();
			List<PaymentErrorPojo> pr = batchPaymObjEr.get(batchId);
			pr.stream().forEach(paymentErrPojo -> {
				try {
					boolean newPayment = false;
					String staticType = XorConstant.FAILED;
					String batchcomments = MessageConstant.BATCH100124;
					comonJpaRepo.loadPaymentDetails(newPayment, interUUID, paymentErrPojo, null, entityManager, query,
							configPojo.getSrcSys(), CommonJPAConstant.endToEndIdSet, staticType,
							CommonJPAConstant.loadmessageIdSet, CommonJPAConstant.loadendToEndIdSet,
							CommonJPAConstant.loadInvoiceSet, batchcomments, configPojo, null);
				} catch (Exception e) {
					logger.info("Loading Payment details Error:: " + e.getMessage());
					e.printStackTrace();
				}
			});

		});

	}

	private void insertToSTGE(StringBuilder inserQuerySTG_E, List<DynamicNrmlzColumnEntity> dynamicUniqueColumn,
			ConfigPojo configPojo) {
		createInsertScript(null, inserQuerySTG_E, dynamicUniqueColumn, null, "STGH", null, configPojo,
				XorConstant.STATUS_E);
		queryListSTGE = new StringBuilder(insertNrmlQuery);
		try {
			logger.info("queryListSTGE--" + queryListSTGE);
			query = entityManager.createNativeQuery(queryListSTGE.toString());
			query.executeUpdate();
		} catch (Exception e) {
			logger.error("Exceptio : " + queryListSTGE);
			e.printStackTrace();
		}
		entityManager.flush();
		entityManager.clear();

	}

	private void addToBatchEntityMap(Map<String, BatchDetailStatusEntity> batchEntityMap,
			Map<String, String> sourceIdMap, String batch, ConfigPojo configPojo) {
		// logger.info("batch - " + batch);

		if (batchEntityMap.containsKey(batch)) {
			BatchDetailStatusEntity en = batchEntityMap.get(batch);
			en.setRecordProcessed(en.getRecordProcessed() + 1);
			batchEntityMap.put(batch, en);
		} else {
			recordsProcessed = 1;
			if (null != colNoOftxn) {
				Integer noOfTxn = Integer.parseInt(sourceIdMap.get(colNoOftxn));
				String partyName = sourceIdMap.get(colPartyName);
				LocalDateTime paymentDate = null;
				if (null != partyName && !partyName.isEmpty()) {
					try {
						paymentDate = LocalDateTime.parse(sourceIdMap.get(colPaymentDate),
								XorConstant.formateYYYYMMDDTHHMMSS);
					} catch (Exception e) {
						try {
							paymentDate = LocalDateTime.parse(sourceIdMap.get(colPaymentDate),
									XorConstant.formateYYYYMMDD_HHMMSSZ);
						} catch (Exception ex) {
							// ex.printStackTrace();
							logger.info("Date Formate issue " + ex.getMessage());
							paymentDate = LocalDateTime.now();
						}
					}
				} else {
					logger.info("No Partyname present for batch " + batch);
					paymentDate = LocalDateTime.now();
				}
				batchEntityMap.put(batch,
						new BatchDetailStatusEntity(configPojo.getSOURCE_SYS_ID(), noOfTxn, batch, partyName,
								paymentDate, recordsProcessed, LocalDateTime.now(), XorConstant.ADMIN_ROLE,
								LocalDateTime.now(), XorConstant.ADMIN_ROLE));
			} else {
				logger.info(" **** No Mapped column found for " + colNoOftxn);
			}
		}
	}

	private void updateBatchDetailsStatus(Map<String, BatchDetailStatusEntity> batchMap) {
		batchDetailsStatusRepo.saveAll(batchMap.values());
	}

	private String getMappedSourceColumnName(Integer piuidSeqId, String nrmColName, String tableType,
			Map<String, Integer> piuidSeqIdMap, ConfigPojo configPojo) {
		if (tableType.equalsIgnoreCase(XorConstant.SOURCE)) {
			DynamicNrmlzColumnEntity dynNrmlColm = dynamicNrmlzColumnRepo
					.findBySrcSysIdAndActiveIndAndSrcColNameIgnoreCase(configPojo.getSOURCE_SYS_ID(),
							XorConstant.STATUS_Y, nrmColName);
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
					.findBySrcSysIdAndActiveIndAndSrcColNameIgnoreCase(configPojo.getSOURCE_SYS_ID(),
							XorConstant.STATUS_Y, nrmColName);
			if (null != dynNrmlColm) {
				if (tableType.equalsIgnoreCase(XorConstant.SOURCE)) {
					return dynNrmlColm.getSrcColName();
				} else if (tableType.equalsIgnoreCase(XorConstant.NRMLZ)) {
					return dynNrmlColm.getNrmzColName();
				}
			}
		} else if (null != configPojo.getSrcSys() && tableType.equalsIgnoreCase(XorConstant.AGG)) {
			List<NormalizeAggXmlTagsMapEntity> nrmAggTagEntityList = normalizeAggXmlTagsMapRepo
					.findByActiveIndAndErpSrcSysAndAggColNameIgnoreCaseInAndPiuidSeqIdIn(XorConstant.STATUS_Y,
							configPojo.getSOURCE_SYS_ID(), CommonJPAConstant.colList,
							new ArrayList<>(piuidSeqIdMap.values()));
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
						.findBySrcSysIdAndActiveIndAndNrmzColNameIgnoreCaseIn(configPojo.getSOURCE_SYS_ID(),
								XorConstant.STATUS_Y, nrmlzColNameSet)
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
		case CommonJPAConstant.COL133_CTRLSUM:
			colsMap.put(CommonJPAConstant.COL133_CTRLSUM, nrmlColName);
			break;
		case CommonJPAConstant.COL62_DUEPYBLAM:
			colsMap.put(CommonJPAConstant.COL62_DUEPYBLAM, nrmlColName);
			break;
		case CommonJPAConstant.COL59_NB:
			colsMap.put(CommonJPAConstant.COL59_NB, nrmlColName);
			break;
		case CommonJPAConstant.COL8_NM:
			colsMap.put(CommonJPAConstant.COL8_NM, nrmlColName);
			break;
		case CommonJPAConstant.COL11_PMTMTD:
			colsMap.put(CommonJPAConstant.COL11_PMTMTD, nrmlColName);
			break;

		}
	}

	private void updateDashBoardDetails(Integer srcSysId, int recordConunt) {

		DashboardEntity dashBoardDetail = dashboardRepo.findBySourceSysIdAndDataStage(srcSysId, XorConstant.INITIATED);
		if (null != dashBoardDetail) {
			dashboardHistoryRepo.save(getDashBoardHistoryEntity(dashBoardDetail));
			if (null != dashBoardDetail.getRecordCount()) {
				recordConunt = recordConunt + dashBoardDetail.getRecordCount();
			}
		} else {
			dashBoardDetail = new DashboardEntity();
			dashBoardDetail.setSourceSysId(srcSysId);
			dashBoardDetail.setDataStage(XorConstant.INITIATED);
			dashBoardDetail.setRecordCount(recordConunt);
			dashBoardDetail.setCreatedBy(XorConstant.ADMIN_ROLE);
			dashBoardDetail.setCreatedDate(LocalDateTime.now());
			dashBoardDetail.setUpdatedBy(XorConstant.ADMIN_ROLE);
			dashBoardDetail.setUpdatedDate(LocalDateTime.now());
		}
		dashBoardDetail.setRecordCount(recordConunt);
		dashBoardDetail.setInsertTime(java.time.LocalDateTime.now());
		dashBoardDetail.setUpdatedBy(XorConstant.ADMIN_ROLE);
		dashBoardDetail.setUpdatedDate(LocalDateTime.now());

		dashboardRepo.save(dashBoardDetail);
	}

	private DashboardHistoryEntity getDashBoardHistoryEntity(DashboardEntity dashBoardDetail) {
		DashboardHistoryEntity historyEntity = new DashboardHistoryEntity();
		BeanUtils.copyProperties(dashBoardDetail, historyEntity);
		historyEntity.setId(0);
		historyEntity.setCreatedBy(XorConstant.ADMIN_ROLE);
		historyEntity.setUpdatedBy(XorConstant.ADMIN_ROLE);

		return historyEntity;
	}

	private void setSourceColumnIdentityName(List<Map<String, String>> sourceIdMapList,
			List<DynamicNrmlzColumnEntity> dynamicUniqueColumn, ConfigPojo configPojo) {
		sourceIdMapList.stream().forEach(sourceIdMap -> {
			dynamicUniqueColumn.stream().forEach(ety -> {
				if (ety.getSrcColName().equalsIgnoreCase(configPojo.getSOURCE_TABLE_IDENTITY_COLUMN_NAME())) {
					sourceColumnIdentityName = ety.getSrcColName();
				}
			});
		});
	}

	private void createInsertScript(String internalUuid, StringBuilder insertQuery,
			List<DynamicNrmlzColumnEntity> dynmicNrmlColList, Map<String, String> sourceIdMap, String type,
			String comments, ConfigPojo configPojo, Character status) {
		insertNrmlQuery = new StringBuilder(insertQuery);
		i = 0;
		if (type.equalsIgnoreCase(CommonJPAConstant.NRMLZ)) {
			insertNrmlQuery.append(" values( ");
			insertNrmlQuery.append("0, " + configPojo.getSOURCE_SYS_ID() + ",  '" + status + "'  ");

			dynmicNrmlColList.stream().forEach(e -> {
				i = i + 1;
				String val;
				if (null == sourceIdMap.get(e.getSrcColName().trim())
						|| (null != sourceIdMap.get(e.getSrcColName().trim())
								&& sourceIdMap.get(e.getSrcColName().trim()).trim().isEmpty())) {
					val = null;
				} else {
					val = sourceIdMap.get(e.getSrcColName()).trim();
				}

				if (e.getSrcColName().equalsIgnoreCase(CommonJPAConstant.colInternalUuid)) {
					val = internalUuid;
				}
				if (i > 0) {
					insertNrmlQuery.append(", ");
				}
				addValueAccordingToType(e.getSrcDataType(), insertNrmlQuery, val, e.getSrcEtlTransformation(),
						configPojo);
				i++;
			});
			insertNrmlQuery.append(")");
		} else if (type.equalsIgnoreCase("STGH")) {
			insertNrmlQuery.append(" select  ");
			i = 0;
			dynmicNrmlColList.stream().forEach(e -> {
				if (i > 0) {
					insertNrmlQuery.append(", ");
				}
				insertNrmlQuery.append(e.getSrcColName());
				i++;
			});
			insertNrmlQuery.append(", etl_process_comment, xp_last_modified_date, process_status ");
			insertNrmlQuery.append(
					" from " + configPojo.getSOURCE_TABLE_NAME() + " where  process_status =" + " '" + status + "' ");

		}

	}

	private void addValueAccordingToType(Integer srcDataType, StringBuilder insertNrmlQuery, String val, String etlType,
			ConfigPojo configPojo) {
		if (val != null && !val.trim().isEmpty()) {
			if (null != etlType) {
				insertNrmlQuery.append(etlType);
			} else {
				switch (srcDataType) {
				case 1:
					val = val.replaceAll("[^0-9]", "");
					insertNrmlQuery.append(val);
					break;
				case 2:
					insertNrmlQuery.append("'" + val + "'");
					break;
				case 3:
					insertNrmlQuery.append("'" + val + "'");
					break;
				case 4:
					if (val.trim().isEmpty()) {
						val = "0";
					}
					insertNrmlQuery.append(val);
					break;
				case 5:
					val = val.replaceAll("[^0-9]", "");
					if (val.trim().isEmpty()) {
						val = "0";
					}
					insertNrmlQuery.append(val);
					break;
				case 6:
					// "2021-05-02T00:00:00"
					if (configPojo.getDATABASE_SYSTEM().equalsIgnoreCase(CommonJPAConstant.POSTGRES_DB_DATA_TYPE)) {
						insertNrmlQuery.append("cast('" + val + "' as timestamp)");
					} else if (configPojo.getDATABASE_SYSTEM().equalsIgnoreCase(CommonJPAConstant.MYSQL_DB_DATA_TYPE)) {
						insertNrmlQuery.append("cast('" + val + "' as DATETIME)");
					}
					break;
				case 7:
					// insertNrmlQuery.append("TO_DATE('" + val + "', 'YYYY-MM-DD HH24:mi:ss')");
					if (configPojo.getDATABASE_SYSTEM().equalsIgnoreCase(CommonJPAConstant.POSTGRES_DB_DATA_TYPE)) {
						insertNrmlQuery.append("cast('" + val + "' as timestamp)");
					} else if (configPojo.getDATABASE_SYSTEM().equalsIgnoreCase(CommonJPAConstant.MYSQL_DB_DATA_TYPE)) {
						insertNrmlQuery.append("cast('" + val + "' as DATETIME)");
					}
					break;

				}
			}
		} else {
			insertNrmlQuery.append("NULL");
		}
	}

	private void cleanSourceTable(String tableName) {
		StringBuilder deleteQuery = new StringBuilder("delete from " + tableName + " where  process_status ='"
				+ XorConstant.STATUS_PAYMENT_PROCESSED + "' or " + " process_status ='" + XorConstant.STATUS_E + "' ");

		query = entityManager.createNativeQuery(deleteQuery.toString());
		query.executeUpdate();
		entityManager.flush();
		entityManager.clear();

	}

	public List<Object[]> getResultSet(String aggquery) {
		Query nativeQuery = entityManager.createNativeQuery(aggquery);
		List<Object[]> rsl = nativeQuery.getResultList();
		return rsl;
	}

	public List<String> getResultSetString(String aggquery) {
		Query nativeQuery = entityManager.createNativeQuery(aggquery);
		return nativeQuery.getResultList();
	}

	public List<Map<String, String>> getResultFromSource(List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList,
			List<DynamicNrmlzColumnEntity> dynamicUniqueColumn, ConfigPojo configPojo) {

		List<Map<String, String>> sourceIdMapList = new ArrayList<>();
		try {

			StringBuilder selectQuery = getSelectQueryForSourceTable(dynamicNrmlColEntityList, dynamicUniqueColumn,
					configPojo);
			selectQuery.append(" where process_status = '" + XorConstant.PROCESS_INITIATED + "' ");
			logger.info("selectQuery source --->> " + selectQuery);
			List<Object[]> rsl = getResultSet(selectQuery.toString());

			int total;
			if (rsl.size() > 0) {
				total = dynamicUniqueColumn.size();
				rsl.stream().forEach(e -> {
					Map<String, String> sourceIdMap = new HashMap<>();
					for (int r = 0; r < total; r++) {
						if (null != e[r]) {
							String col = dynamicUniqueColumn.get(r).getSrcColName().trim();
							sourceIdMap.put(col, e[r].toString());
						}
					}
					sourceIdMapList.add(sourceIdMap);
				});
			}
		} catch (Exception e) {
			logger.info("Exception ::Reading Source table :: Check for dynamic column " + e.getMessage());
			return null;
		}
		return sourceIdMapList;
	}

	public StringBuilder getSelectQueryForSourceTable(List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList,
			List<DynamicNrmlzColumnEntity> dynamicUniqueColumn, ConfigPojo configPojo) {

		StringBuilder selectQuery = new StringBuilder("Select ");
		i = 0;
		Set<String> columnName = new HashSet<>(1);
		dynamicNrmlColEntityList.stream().forEach(e -> {
			if (columnName.add(e.getSrcColName().toLowerCase())) {
				dynamicUniqueColumn.add(e);
				if (i > 0) {
					selectQuery.append(", ");
				}
				selectQuery.append(e.getSrcColName());
				i++;
			}
		});
		selectQuery.append(" from  " + configPojo.getSOURCE_TABLE_NAME());
		return selectQuery;
	}

	public StringBuilder getSelectQueryForNrmlzTable(List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList,
			List<DynamicNrmlzColumnEntity> dynamicUniqueColumn) {

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
		selectQuery.append(" from  " + XorConstant.NRMLZ_PMT_DTLS + " ");
		return selectQuery;
	}

	public StringBuilder getInsertQueryForNrmlzPmtDtlsOns(List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList) {
		StringBuilder insertQuery = new StringBuilder(
				"Insert into " + XorConstant.NRMLZ_PMT_DTLS + " ( slno , erp_src_sys , process_status ");
		// StringBuilder value = new StringBuilder("VALUES( ?");
		dynamicNrmlColEntityList.stream().forEach(e -> {
			insertQuery.append(", ");
			insertQuery.append(e.getNrmzColName());

			// value.append(", ?");
		});

		insertQuery.append(") ");
		// value.append(" ) ");
		// insertQuery.append(value);
		return insertQuery;
	}

	public StringBuilder getInsertQueryForSTG(List<DynamicNrmlzColumnEntity> dynamicNrmlColumns, String tableType,
			ConfigPojo configPojo) {
		StringBuilder inserQuerySTG_H = new StringBuilder(
				"Insert into " + configPojo.getSOURCE_TABLE_NAME() + tableType);
		i = 0;
		inserQuerySTG_H.append("(");
		dynamicNrmlColumns.stream().forEach(e -> {
			if (i > 0) {
				inserQuerySTG_H.append(", ");
			}
			inserQuerySTG_H.append(e.getSrcColName());
			i++;
		});
		inserQuerySTG_H.append(", etl_process_comment, xp_last_modified_date, process_status ) ");
		return inserQuerySTG_H;
	}

	public StringBuilder getInsertQueryForSTGForBatch(List<DynamicNrmlzColumnEntity> dynamicNrmlColumns,
			String tableType, ConfigPojo configPojo) {
		StringBuilder inserQuerySTG_H = new StringBuilder(
				"Insert into " + configPojo.getSOURCE_TABLE_NAME() + tableType);
		i = 0;
		inserQuerySTG_H.append("(");
		StringBuilder col = new StringBuilder();
		dynamicNrmlColumns.stream().forEach(e -> {
			if (i > 0) {
				col.append(", ");
			}
			col.append(e.getSrcColName());
			i++;
		});
		inserQuerySTG_H.append(col);
		inserQuerySTG_H.append(", etl_process_comment ) select ");
		inserQuerySTG_H.append(col);

		return inserQuerySTG_H;
	}

	public StringBuilder getInsertQueryForNrmlz(List<DynamicNrmlzColumnEntity> dynamicNrmlColumns, String tableType) {
		StringBuilder inserQueryNrml_H = new StringBuilder("Insert into " + XorConstant.NRMLZ_PMT_DTLS + tableType);
		i = 1;
		inserQueryNrml_H.append(" ( ");
		StringBuilder cols = new StringBuilder();
		cols.append("slno");
		dynamicNrmlColumns.stream().forEach(e -> {
			if (i > 0) {
				cols.append(", ");
			}
			cols.append(e.getNrmzColName());
			i++;
		});

		inserQueryNrml_H.append(cols);
		inserQueryNrml_H.append(" ) select ");
		inserQueryNrml_H.append(cols);
		inserQueryNrml_H.append(" from " + XorConstant.NRMLZ_PMT_DTLS + " ");
		// inserQuerySTG_H.append(value);
		return inserQueryNrml_H;
	}

	public int getSTGIndentity(List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList, ConfigPojo configPojo) {
		dynamicNrmlColEntityList.stream().forEach(e -> {
			if (e.getSrcColName().equalsIgnoreCase(configPojo.getSOURCE_TABLE_IDENTITY_COLUMN_NAME())) {
				identityColumnId = e.getId();
			}
		});
		return identityColumnId;
	}

	public void moveprocessedDate() {
		// Runnable moveData = () -> {

		try {
			String inserQuery = " insert into agg_prty_pymt_dtls_h select * from agg_prty_pymt_dtls where xml_status ='"
					+ XorConstant.STATUS_PAYMENT_PROCESSED + "'";
			query = entityManager.createNativeQuery(inserQuery);
			query.executeUpdate();

			inserQuery = " insert into agg_remittance_info_h select * from agg_remittance_info  where "
					+ " msgid_4 in ( select msgid_4 from agg_prty_pymt_dtls where xml_status ='"
					+ XorConstant.STATUS_PAYMENT_PROCESSED + "')";
			query = entityManager.createNativeQuery(inserQuery);
			query.executeUpdate();

			inserQuery = "delete from agg_remittance_info where msgid_4 in (select msgid_4 from agg_prty_pymt_dtls where xml_status ='"
					+ XorConstant.STATUS_PAYMENT_PROCESSED + "')";
			query = entityManager.createNativeQuery(inserQuery);
			query.executeUpdate();

			inserQuery = "delete from agg_prty_pymt_dtls where xml_status ='" + XorConstant.STATUS_PAYMENT_PROCESSED
					+ "'";
			query = entityManager.createNativeQuery(inserQuery);
			query.executeUpdate();

			inserQuery = "insert into xp_agg_lookup_h select * from xp_agg_lookup  " + "  where process_status ='"
					+ XorConstant.STATUS_PAYMENT_PROCESSED + "'";
			query = entityManager.createNativeQuery(inserQuery);
			query.executeUpdate();

			inserQuery = "delete from xp_agg_lookup where process_status ='" + XorConstant.STATUS_PAYMENT_PROCESSED
					+ "'";
			query = entityManager.createNativeQuery(inserQuery);
			query.executeUpdate();

			entityManager.flush();
			entityManager.clear();
		} catch (Exception e) {
			logger.error("ArchiveData Exception", e);
			throw e;
		}
		// };
		// XorThreadConstant.DataMove.execute(moveData);
	}

	public void insertErrorDataForBatch(Map<String, PaymentErrorPojo> errorBatchSet,
			Map<String, List<PaymentErrorPojo>> validBatchSet, List<DynamicNrmlzColumnEntity> dynamicDistinctColumn,
			ConfigPojo configPojo, Map<String, String> batchUUIDMap,
			Map<String, PaymentLoadValidatioPojo> paymentLoadValMap) {
		StringBuilder inserQuerySTG_E_for_Batch = getInsertQueryForSTGForBatch(dynamicDistinctColumn, "_e ",
				configPojo);
		StringBuilder internalId = new StringBuilder();
		i = 0;
		Set<String> errBacht = new HashSet<>(5);

		errorBatchSet.entrySet().forEach(e -> {
			logger.info(e.getKey() + " " + e.getValue().toString());
		});

		errorBatchSet.entrySet().stream().forEach(batchidMap -> {
			PaymentErrorPojo paymentErr = batchidMap.getValue();
			String[] internlIdBatch = batchidMap.getKey().split("~");

			boolean newPayment = true;
			if (internlIdBatch.length > 1) {
				String batch = internlIdBatch[1];
				String internalUUID = batchUUIDMap.get(batch);
				logger.info("batch " + batch + " -- " + internalUUID);
				errBacht.add(batch);

				validatePaymenErrObject(paymentLoadValMap, paymentErr, batch);

				if (internlIdBatch.length > 2) {
					String endToEndId = internlIdBatch[2];
					String staticType = XorConstant.FAILED;
					String batchcomments = "Batch Failed Due To Payments";

					comonJpaRepo.loadPaymentDetails(newPayment, internalUUID, paymentErr, null, entityManager, query,
							configPojo.getSrcSys(), CommonJPAConstant.endToEndIdSet, staticType,
							CommonJPAConstant.loadmessageIdSet, CommonJPAConstant.loadendToEndIdSet,
							CommonJPAConstant.loadInvoiceSet, batchcomments, configPojo, colProfileMap);

				} else {
					String staticType = XorConstant.FAILED;
					String batchcomments = "Batch Failed Due To Payments";
					comonJpaRepo.loadPaymentDetails(newPayment, internalUUID, paymentErr, null, entityManager, query,
							configPojo.getSrcSys(), CommonJPAConstant.endToEndIdSet, staticType,
							CommonJPAConstant.loadmessageIdSet, CommonJPAConstant.loadendToEndIdSet,
							CommonJPAConstant.loadInvoiceSet, batchcomments, configPojo, colProfileMap);
				}
			}

		});

		validBatchSet.entrySet().stream().forEach(batchidMap -> {
			List<PaymentErrorPojo> paymentErrL = batchidMap.getValue();

			paymentErrL.stream().forEach(paymentErr -> {
				String batchId = paymentErr.getMsgId();
				String internalUUID = batchUUIDMap.get(batchId);
				batchUUIDMap.put(batchId, internalUUID);
				boolean newPayment = true;
				if (null != batchId) {
					String endToEndId = paymentErr.getEndToEndId();
					if (null != endToEndId) {
						// statics counts should not
						String staticType = XorConstant.NEW;
						String batchcomments = "Batch Processed";
						comonJpaRepo.loadPaymentDetails(newPayment, internalUUID, paymentErr, null, entityManager,
								query, configPojo.getSrcSys(), CommonJPAConstant.endToEndIdSet, staticType,
								CommonJPAConstant.loadmessageIdSet, CommonJPAConstant.loadendToEndIdSet,
								CommonJPAConstant.loadInvoiceSet, batchcomments, configPojo, colProfileMap);

					}
				}
			});
		});

		/*
		 * batchUUIDMap.entrySet().stream().forEach(e -> { StringBuilder queryUpdateUUid
		 * = new StringBuilder("update " + configPojo.getSOURCE_TABLE_NAME() +
		 * "  set internal_uuid='" + e.getValue() + "' where " + colBatch + " ='" +
		 * e.getKey() + "'"); try { logger.info("update-- " + queryUpdateUUid); query =
		 * entityManager.createNativeQuery(queryUpdateUUid.toString());
		 * query.executeUpdate(); } catch (Exception ex) { ex.printStackTrace(); } });
		 */

		errorBatchSet.entrySet().stream().forEach(batchidMap -> {

			String[] internlIdBatch = batchidMap.getKey().split("~");
			if (internlIdBatch.length > 1) {
				errBacht.add(internlIdBatch[1]);
			}
			PaymentErrorPojo paymentErr = batchidMap.getValue();
			StringBuilder inserIntoErr = new StringBuilder(inserQuerySTG_E_for_Batch);
			inserIntoErr.append(", '" + paymentErr.getComment() + "' ");
			inserIntoErr.append(" from " + configPojo.getSOURCE_TABLE_NAME());
			inserIntoErr.append(" where " + sourceColumnIdentityName + " in (" + internlIdBatch[0] + " )");

			try {
				logger.info("inserIntoErr-- " + inserIntoErr);
				query = entityManager.createNativeQuery(inserIntoErr.toString());
				query.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (i > 0) {
				internalId.append(", ");
			}
			internalId.append(internlIdBatch[0]);
			i++;
		});

		if (!errorBatchSet.isEmpty()) {
			String comments = "Rejecting Batch due to validation failed";
			// String paymentcomments = "Rejecting Payment due to Batch Rejection.";
			// String batchcomments = "Rejecting Batch due to Payment Rejection.";

			inserQuerySTG_E_for_Batch.append(", '" + comments + "' ");
			inserQuerySTG_E_for_Batch.append(" from " + configPojo.getSOURCE_TABLE_NAME());
			inserQuerySTG_E_for_Batch.append(" where " + colBatch + " in ( ");

			StringBuilder delQuery = new StringBuilder("delete from " + configPojo.getSOURCE_TABLE_NAME());
			delQuery.append(" where " + sourceColumnIdentityName + " in ( ");
			delQuery.append(internalId);
			delQuery.append(") ");
			// logger.info("del query " + delQuery);
			query = entityManager.createNativeQuery(delQuery.toString());
			query.executeUpdate();

			entityManager.flush();
			entityManager.clear();

			if (!errBacht.isEmpty()) {
				i = 0;
				StringBuilder batch = new StringBuilder();
				errBacht.stream().forEach(batchid -> {
					if (i > 0) {
						batch.append(", ");
					}
					batch.append(" '" + batchid + "'");
					i++;

				});
				inserQuerySTG_E_for_Batch.append(batch);
				inserQuerySTG_E_for_Batch.append(") ");

				logger.info("inserQuerySTG_E_for_Batch-- " + inserQuerySTG_E_for_Batch);
				query = entityManager.createNativeQuery(inserQuerySTG_E_for_Batch.toString());
				query.executeUpdate();

				delQuery = new StringBuilder("delete from " + configPojo.getSOURCE_TABLE_NAME());
				delQuery.append(" where " + colBatch + " in ( ");
				delQuery.append(batch);
				delQuery.append(") ");

				query = entityManager.createNativeQuery(delQuery.toString());
				query.executeUpdate();

				entityManager.flush();
				entityManager.clear();

			}
		}
	}

	private void validatePaymenErrObject(Map<String, PaymentLoadValidatioPojo> paymentLoadValMap,
			PaymentErrorPojo paymentErr, String batch) {
		if (null != paymentLoadValMap && paymentLoadValMap.containsKey(batch)) {
			PaymentLoadValidatioPojo plv = paymentLoadValMap.get(batch);
			if (plv.isInvalidContrlSum()) {
				paymentErr.setControlSum(null);
			}
			if (plv.isInvalidCreationDt()) {
				paymentErr.setCreationDate(null);
			}
			if (plv.isInvalidInstdAmt()) {
				paymentErr.setInstdAmt(null);
			}
			if (plv.isInvalidInvoiceAmt()) {
				paymentErr.setInvoiceAmt(null);
			}
			if (plv.isInvalidInvoiceDt()) {
				paymentErr.setInvoiceDt(null);
			}
			if (plv.isInvalidTransferDt()) {
				paymentErr.setTransferDt(null);
			}
		}
	}

	public void checkRequiredTable(ConfigPojo configPojo) {

		String alrteCommetnColumn = "ALTER TABLE " + configPojo.getSOURCE_TABLE_NAME()
				+ " ADD COLUMN etl_process_comment mediumtext ";

		String alrteintUuidColumn = "ALTER TABLE " + configPojo.getSOURCE_TABLE_NAME()
				+ " ADD COLUMN internal_uuid Varchar(100) ";

		String altrStatusColumn = "ALTER TABLE " + configPojo.getSOURCE_TABLE_NAME()
				+ " ADD COLUMN process_status char ";

		StringBuilder createh = new StringBuilder("create table " + configPojo.getSOURCE_TABLE_NAME()
				+ "_h as select * from " + configPojo.getSOURCE_TABLE_NAME() + " where 1=2 ");
		StringBuilder createe = new StringBuilder("create table " + configPojo.getSOURCE_TABLE_NAME()
				+ "_e as select * from " + configPojo.getSOURCE_TABLE_NAME() + "  where 1=2 ");
		try {
			query = entityManager.createNativeQuery(alrteCommetnColumn);
			query.executeUpdate();
			entityManager.flush();
			entityManager.clear();
		} catch (Exception es) {
			// logger.info(es.getMessage());
		}

		try {
			query = entityManager.createNativeQuery(alrteintUuidColumn);
			query.executeUpdate();
			entityManager.flush();
			entityManager.clear();
		} catch (Exception es) {
			// logger.info(es.getMessage());
		}

		try {
			query = entityManager.createNativeQuery(altrStatusColumn);
			query.executeUpdate();
			entityManager.flush();
			entityManager.clear();
		} catch (Exception es) {
			// logger.info(es.getMessage());
		}

		try {
			String alterLastMoDDt = "alter table " + configPojo.getSOURCE_TABLE_NAME()
					+ " add column xp_last_modified_date TIMESTAMP default CURRENT_TIMESTAMP ";
			query = entityManager.createNativeQuery(alterLastMoDDt);
			query.executeUpdate();

		} catch (Exception es) {
			// logger.info(es.getMessage());
		}
		try {
			String alterLastMoDDtE = "alter table " + configPojo.getSOURCE_TABLE_NAME() + "_e"
					+ " add column xp_last_modified_date TIMESTAMP default CURRENT_TIMESTAMP ";
			query = entityManager.createNativeQuery(alterLastMoDDtE);
			query.executeUpdate();

		} catch (Exception es) {
			// logger.info(es.getMessage());
		}
		try {
			String alterLastMoDDtH = "alter table " + configPojo.getSOURCE_TABLE_NAME() + "_h"
					+ " add column xp_last_modified_date TIMESTAMP default CURRENT_TIMESTAMP ";
			query = entityManager.createNativeQuery(alterLastMoDDtH);
			query.executeUpdate();
		} catch (Exception es) {
			// logger.info(es.getMessage());
		}

		try {
			query = entityManager.createNativeQuery(createh.toString());
			query.executeUpdate();
			entityManager.flush();
			entityManager.clear();
		} catch (Exception es) {
			// logger.info(es.getMessage());
			checkForNewColumn(configPojo.getSOURCE_TABLE_NAME(), configPojo.getSOURCE_TABLE_NAME() + "_h");
		}
		try {
			query = entityManager.createNativeQuery(createe.toString());
			query.executeUpdate();
			entityManager.flush();
			entityManager.clear();
		} catch (Exception es) {
			// logger.info(es.getMessage());
			checkForNewColumn(configPojo.getSOURCE_TABLE_NAME(), configPojo.getSOURCE_TABLE_NAME() + "_e");
		}
	}

	private void checkForNewColumn(String mainTable, String destTable) {
		String alterColumn = getColDiffQuery(mainTable, destTable);
		List<String> cols = getColList(alterColumn);
		if (!cols.isEmpty()) {
			StringBuilder altertable = new StringBuilder("ALTER TABLE " + destTable);
			i = 0;
			getAlterQuery(cols, altertable);
			try {
				query = entityManager.createNativeQuery(altertable.toString());
				query.executeUpdate();
				entityManager.flush();
				entityManager.clear();
			} catch (Exception ex) {
				logger.info(ex.getMessage());
			}

		}
	}

	private void getAlterQuery(List<String> cols, StringBuilder altertable) {
		cols.stream().forEach(e -> {
			if (i > 0) {
				altertable.append(", ");
			}
			altertable.append(" ADD COLUMN " + e + " mediumtext ");
			i++;
		});
		altertable.append("; ");
	}

	private String getColDiffQuery(String maintable, String destTable) {
		String alterColumn = "select a.COLUMN_NAME " + "	from "
				+ "	(SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS   WHERE TABLE_NAME='" + maintable + "' ) a "
				+ "	LEFT JOIN (SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS  WHERE TABLE_NAME='" + destTable
				+ "') b " + "	ON (a.COLUMN_NAME = b.COLUMN_NAME) " + "	where b.COLUMN_NAME is null ";
		return alterColumn;
	}

	private List<String> getColList(String query) {

		List<String> rsl = getResultSetString(query);
		List<String> colList = new ArrayList<>();
		if (rsl.size() > 0) {
			rsl.stream().forEach(e -> {
				if (null != e) {
					colList.add(e.toString());
				}
			});
		}
		return colList;
	}

}
