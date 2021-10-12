package com.xoriant.xorpay.data.sync.repo;

import java.math.BigDecimal;
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

import com.xoriant.xorpay.constants.MessageConstant;
import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.data.sync.entity.DynamicNrmlzColumnEntity;
import com.xoriant.xorpay.data.sync.entity.ProcessStatusEntity;
import com.xoriant.xorpay.data.sync.services.LoaderCommonService;
import com.xoriant.xorpay.entity.NormalizeAggXmlTagsMapEntity;
import com.xoriant.xorpay.entity.PIUIDEntity;
import com.xoriant.xorpay.entity.RuleLookUpEntity;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.pojo.PaymentErrorPojo;
import com.xoriant.xorpay.repo.NormalizeAggXmlTagsMapRepo;
import com.xoriant.xorpay.repo.PIUIDRepo;
import com.xoriant.xorpay.repo.RuleLookUpRepo;

@Repository
@Transactional
public class NrmlzToAggJPARepo {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private NormalizeAggXmlTagsMapRepo normalizeAggXmlTagsMapRepo;
	@Autowired
	private DynamicNrmlzColumnRepo dynamicNrmlzColumnRepo;
	@Autowired
	private PIUIDRepo piuidRepo;
	@Autowired
	private AuditJPARepo auditJPARepo;
	@Autowired
	private LoaderCommonService loaderCommonService;
	@Autowired
	private CommonJPARepo comonJpaRepo;
	@Autowired
	private RuleLookUpRepo ruleLookUpRepo;

	private int i, j;
	private Query query = null;
	private Integer identityColumnId;
	// private List<String> internalIdToHList = new ArrayList<>(1);
	// private List<String> internalIdToEList = new ArrayList<>(1);
	private StringBuilder aggQuery, remQuery;
	private Map<String, StringBuilder> querymap;
	private Map<String, List<NormalizeAggXmlTagsMapEntity>> nrmlzAggColMap;
	private boolean flag = false;
	private final Logger logger = LoggerFactory.getLogger(NrmlzToAggJPARepo.class);
	private Map<Integer, Map<String, String>> piuidSeqIdColMap = new HashMap<>();
	private Map<String, String> nrmlzSourceMap = new HashMap<>();
	public String sourceColumnIdentityName;
	public String colPaymentIdSource, colBatch, colNoOftxn, colPartyName, colPaymentDate, colProfileName, colCurrency,
			colTransferDt, colInvoiceDt, colInstdAmt, colContrlSum, colInvoiceAmt, colInvoiceNo;
	public String nrmlzProfile;
	private Integer nrmlRecordCount = 0;

	private void intializeColNameForProfileName(String profileName, Map<String, Integer> piuidSeqIdMap) {

		logger.info("nrmlzSourceMap " + nrmlzSourceMap);
		logger.info(profileName + "piuidSeqIdColMap " + piuidSeqIdColMap);
		logger.info("CommonJPAConstant.CCY_CD " + CommonJPAConstant.CCY_CD);

		colBatch = piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL4_MSGID);
		colNoOftxn = piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL6_NBOFTXS);
		colPartyName = piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL41_NM);
		colPaymentDate = piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL5_CREDTTM);
		colPaymentIdSource = piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName))
				.get(CommonJPAConstant.COL33_ENDTOENDID);

		colCurrency = piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.CCY_CD);

		colTransferDt = piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL20_REQDEXCTNDT);
		colInvoiceDt = piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL60_RLTDDT);
		colInstdAmt = piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL35_INSTDAMT);
		colContrlSum = piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL133_CTRLSUM);
		colInvoiceAmt = piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL62_DUEPYBLAM);
		colInvoiceNo = piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName)).get(CommonJPAConstant.COL59_NB);

		logger.info("Base col initialized for profileName -" + profileName + " colBatch -" + colBatch + " colNoOftxn -"
				+ colNoOftxn + " colPartyName -" + colPartyName + " colPaymentDate -" + colPaymentDate
				+ " colPaymentIdSource -" + colPaymentIdSource + " colCurrency " + colCurrency + " colContrlSum "
				+ colContrlSum + " colInvoiceAmt " + colInvoiceAmt);
	}

	private void initializeBasicColName(String type, Map<String, Integer> piuidSeqIdMap, ConfigPojo configPojo) {
		getMappedSourceColumnName(null, null, type, piuidSeqIdMap, configPojo);

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
			logger.info("piuidSeqIdColMap " + piuidSeqIdColMap);
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
		}
	}

	private void setNrmlzColumnIdentityName(List<Map<String, String>> sourceIdMapList,
			List<DynamicNrmlzColumnEntity> dynamicUniqueColumn, ConfigPojo configPojo) {
		sourceIdMapList.stream().forEach(sourceIdMap -> {
			dynamicUniqueColumn.stream().forEach(ety -> {
				if (ety.getSrcColName().equalsIgnoreCase(configPojo.getSOURCE_TABLE_IDENTITY_COLUMN_NAME())) {
					sourceColumnIdentityName = ety.getNrmzColName();
				}
			});
		});
	}

	public List<Object[]> getResultSet(String aggquery) {
		Query nativeQuery = entityManager.createNativeQuery(aggquery);
		List<Object[]> rsl = nativeQuery.getResultList();
		return rsl;
	}

	public Map<String, List<Map<String, String>>> getResultFromNrmlz(
			List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList, List<DynamicNrmlzColumnEntity> dynamicUniqueColumn,
			ConfigPojo configPojo) {
		colProfileName = getMappedSourceColumnName(null, configPojo.getSOURCE_PROFILE_COLUMN_NAME(), XorConstant.NRMLZ,
				null, configPojo);

		StringBuilder selectQuery = getSelectQueryForNrmlzTable(dynamicNrmlColEntityList, dynamicUniqueColumn,
				configPojo);
		logger.info("selectQuery nrm" + selectQuery);
		List<Object[]> rsl = getResultSet(selectQuery.toString());
		Map<String, List<Map<String, String>>> piuidMap = new HashMap<>(1);
		int total;
		if (rsl.size() > 0) {
			total = dynamicUniqueColumn.size();
			rsl.stream().forEach(e -> {
				nrmlzProfile = null;
				Map<String, String> nrmlzMap = new HashMap<>();
				for (int r = 0; r < total; r++) {
					if (null != e[r]) {
						String col = dynamicUniqueColumn.get(r).getNrmzColName().trim().toUpperCase();
						if (col.equalsIgnoreCase(colProfileName)) {
							nrmlzProfile = e[r].toString();
						}
						nrmlzMap.put(col, e[r].toString());
					}
				}
				// logger.info("nrmlzProfile " + nrmlzProfile);
				if (null != nrmlzProfile) {
					List<Map<String, String>> nrmlMapList;
					if (piuidMap.containsKey(nrmlzProfile)) {
						nrmlMapList = piuidMap.get(nrmlzProfile);
					} else {
						nrmlMapList = new ArrayList<>();
					}
					nrmlMapList.add(nrmlzMap);
					piuidMap.put(nrmlzProfile, nrmlMapList);
					// logger.info("Aded to map");
					nrmlzProfile = null;
				}
			});
		}
		return piuidMap;
	}

	public StringBuilder getSelectQueryForNrmlzTable(List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList,
			List<DynamicNrmlzColumnEntity> dynamicUniqueColumn, ConfigPojo configPojo) {

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
		selectQuery.append(" from  " + XorConstant.NRMLZ_PMT_DTLS + " where process_status ='"
				+ XorConstant.STATUS_PAYMENT_PROCESSED + "' and erp_src_sys = " + configPojo.getSOURCE_SYS_ID());
		return selectQuery;
	}

	public StringBuilder getInsertQueryForNrmlzPmtDtlsOns(List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList) {
		StringBuilder insertQuery = new StringBuilder("Insert into " + XorConstant.NRMLZ_PMT_DTLS + " ( slno ");
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

	public StringBuilder getInsertQueryForNrmlz(List<DynamicNrmlzColumnEntity> dynamicNrmlColumns, String tableType) {
		StringBuilder inserQueryNrml_H = new StringBuilder("Insert into " + XorConstant.NRMLZ_PMT_DTLS + tableType);
		i = 1;
		inserQueryNrml_H.append(" ( ");
		StringBuilder cols = new StringBuilder();
		cols.append("slno, process_status ");
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

	public void startLoadNrmToAggProcess(String processId, ConfigPojo configPojo) {

		entityManager = entityManager.getEntityManagerFactory().createEntityManager();
		Session session = entityManager.unwrap(Session.class);
		Transaction tr = session.beginTransaction();
		session.doWork(new Work() {

			@Override
			public void execute(Connection connections) throws SQLException {
				try (Connection connection = connections) {
					connection.setAutoCommit(true);

					ProcessStatusEntity procRunnigState = null;
					try {
						int recordCount = 0;
						// parallel proccees
						// will be in batch of 100
						List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList = dynamicNrmlzColumnRepo
								.findBySrcSysIdAndActiveIndOrderByIdAsc(configPojo.getSOURCE_SYS_ID(),
										XorConstant.STATUS_Y);
						// Data set
						List<DynamicNrmlzColumnEntity> dynamicDistinctColumn = new ArrayList<>(1);

						Map<String, List<Map<String, String>>> piuidMap = getResultFromNrmlz(dynamicNrmlColEntityList,
								dynamicDistinctColumn, configPojo);

						if (!piuidMap.isEmpty()) {
							procRunnigState = loaderCommonService.setProcessStatusEntityRunning(procRunnigState,
									recordCount, XorConstant.RUNNING, XorConstant.NRMLZ_PMT_DTLS, configPojo);

							StringBuilder inserQueryNrmlz_H = getInsertQueryForNrmlz(dynamicDistinctColumn, "_h ");
							StringBuilder inserQueryNrmlz_E = getInsertQueryForNrmlz(dynamicDistinctColumn, "_e ");

							Map<String, Integer> profileMap = piuidRepo.findByActiveInd(XorConstant.STATUS_Y).stream()
									.collect(Collectors.toMap(PIUIDEntity::getProfileName, PIUIDEntity::getId));

							doOperationForNrml(processId, piuidMap, dynamicNrmlColEntityList, inserQueryNrmlz_H,
									inserQueryNrmlz_E, tr, profileMap, session, configPojo);
							procRunnigState = loaderCommonService.setProcessStatusComplete(procRunnigState,
									XorConstant.COMPLETE);
						} else {
							logger.info("No data to process");
						}
					} catch (Exception e) {
						e.printStackTrace();
						procRunnigState = loaderCommonService.setProcessStatusComplete(procRunnigState,
								XorConstant.ERROR);
					}

				} catch (Exception e) {
					logger.error("LoadSTGToNrm Exception", e);
					throw e;
				}

			}
		});
	}

	public void doOperationForNrml(String processId, Map<String, List<Map<String, String>>> piuidMap,
			List<DynamicNrmlzColumnEntity> dynamicNrmlColEntityList, StringBuilder inserQueryNrmlz_H,
			StringBuilder inserQueryNrmlz_E, Transaction tr, Map<String, Integer> profileMap, Session session,
			ConfigPojo configPojo) {

		getQuerrMap(piuidMap, profileMap, configPojo);
		// internalIdToHList.clear();
		// internalIdToEList.clear();
		colProfileName = getMappedSourceColumnName(null, configPojo.getSOURCE_PROFILE_COLUMN_NAME(), XorConstant.NRMLZ,
				null, configPojo);

		Set<String> profileList = piuidMap.keySet();
		Map<String, Integer> piuidSeqIdMap = piuidRepo
				.findByActiveIndAndProfileNameIgnoreCaseIn(XorConstant.STATUS_Y, profileList).stream()
				.collect(Collectors.toMap(PIUIDEntity::getProfileName, PIUIDEntity::getId));

		initializeBasicColName(XorConstant.AGG, piuidSeqIdMap, configPojo);
		auditJPARepo.batchErrMessageList.clear();
		nrmlRecordCount = 0;
		CommonJPAConstant.clearObject();
		Map<String, List<PaymentErrorPojo>> batchPaymObjEr = new HashMap<>();
		Map<String, String> errBatchUuidMap = new HashMap<>();
		// Map<String, BigDecimal> batchCtrlSumMap = new HashMap<>();
		Set<String> endtoEndSet = new HashSet<>();
		Set<String> errendtoEndSet = new HashSet<>();
		piuidMap.entrySet().stream().forEach(e -> {
			String profile = e.getKey();

			List<Map<String, String>> nrmlzMapList = e.getValue();
			nrmlRecordCount = nrmlRecordCount + nrmlzMapList.size();
			setNrmlzColumnIdentityName(nrmlzMapList, dynamicNrmlColEntityList, configPojo);
			List<NormalizeAggXmlTagsMapEntity> normalizeColMap = nrmlzAggColMap.get(profile);
			Set<String> payemntId = new HashSet<>(1);
			nrmlzMapList.stream().forEach(nrmlzMap -> {

				String profileName = nrmlzMap.get(colProfileName);
				if (null != piuidSeqIdColMap.get(piuidSeqIdMap.get(profileName))) {
					intializeColNameForProfileName(profileName, piuidSeqIdMap);

					if (null != sourceColumnIdentityName && null != colProfileName && null != colPaymentIdSource) {
						logger.info("nrmlzMap " + nrmlzMap);
						String internalId = nrmlzMap.get(sourceColumnIdentityName);
						String internalUuid = nrmlzMap.get(CommonJPAConstant.colInternalUuid);
						String batchId = nrmlzMap.get(colBatch);
						String paymentId = nrmlzMap.get(colPaymentIdSource);
						String currency = nrmlzMap.get(colCurrency);
						String creationDt = nrmlzMap.get(colPaymentDate);
						String transferDt = nrmlzMap.get(colTransferDt);
						String invoiceDt = nrmlzMap.get(colInvoiceDt);
						String endToEndId = nrmlzMap.get(colPaymentIdSource);
						String instdAmt = nrmlzMap.get(colInstdAmt);
						String noOfTxn = nrmlzMap.get(colNoOftxn);
						String controlSum = nrmlzMap.get(colContrlSum);
						String invoiceAmt = nrmlzMap.get(colInvoiceAmt);
						String invoiceNo = nrmlzMap.get(colInvoiceNo);
						// String paymentMethod = sourceIdMap.get(colPaymentMethod);
						// String companyName = sourceIdMap.get(colCompanyName);

						logger.info(internalUuid + " At nrm --> batchId " + batchId + " currency " + currency
								+ "  creationDt " + creationDt + " transferDt" + transferDt + " invoiceDt " + invoiceDt
								+ " invoiceNo " + invoiceNo);
						PaymentErrorPojo paymentErrPojo = new PaymentErrorPojo(batchId, endToEndId, null, instdAmt,
								currency, transferDt, null, configPojo.getSOURCE_SYS_ID(), noOfTxn, internalId, null,
								null, null, null, null, null, null);

						StringBuilder aggInsertQuery = new StringBuilder();
						StringBuilder remInsertQuery = new StringBuilder();
						flag = false;
						// logger.info(paymentId + " - " + payemntId.toString());
						if (payemntId.add(paymentId)) {

							aggInsertQuery = new StringBuilder(querymap.get(profile + XorConstant.AGG));
							aggInsertQuery
									.append(" values(  0, '" + profileName + "', '" + configPojo.getSOURCE_SYS_ID()
											+ "', '" + XorConstant.STATUS_READY + "',  '" + internalUuid + "' ");
							flag = true;
						}
						remInsertQuery = new StringBuilder(querymap.get(profile + XorConstant.REM));
						remInsertQuery.append(
								" values(  0, '" + configPojo.getSOURCE_SYS_ID() + "',  '" + internalUuid + "' ");
						Set<String> aggColSet = new HashSet<>();
						Set<String> remColSet = new HashSet<>();
						aggColSet.add("slno");
						aggColSet.add("profilename");
						aggColSet.add("erp_src_sys");
						aggColSet.add("xml_status");
						remColSet.add("slno");
						remColSet.add("erp_src_sys");
						i = 1;
						j = 1;

						for (NormalizeAggXmlTagsMapEntity en : normalizeColMap) {
							if (flag && aggColSet.add(en.getAggColName().toLowerCase())
									&& ((en.getTagInfoType().equalsIgnoreCase(XorConstant.TAG_INFO_AGG)
											|| en.getTagInfoType().equalsIgnoreCase(XorConstant.BOTH_INFO)))) {
								if (i > 0) {
									aggInsertQuery.append(", ");
								}
								addValueAccordingToTypeFromNrmTOAgg(en.getAggColDataType(), aggInsertQuery, nrmlzMap,
										en.getNrmlzColName(), configPojo);
								i++;
							}
							if (remColSet.add(en.getAggColName().toLowerCase())
									&& (en.getTagInfoType().equalsIgnoreCase(XorConstant.TAG_INFO_REM)
											|| en.getTagInfoType().equalsIgnoreCase(XorConstant.BOTH_INFO))) {
								if (j > 0) {
									remInsertQuery.append(", ");
								}
								addValueAccordingToTypeFromNrmTOAgg(en.getAggColDataType(), remInsertQuery, nrmlzMap,
										en.getNrmlzColName(), configPojo);

								j++;
							}
						}
						if (flag) {
							aggInsertQuery.append(")");
						}
						remInsertQuery.append(")");
						// internalIdToHList.add(internalId);
						try {
							try {
								if (flag) {
									// logger.info("aggremq[0] " + aggInsertQuery);
									query = entityManager.createNativeQuery(aggInsertQuery.toString());
									query.executeUpdate();
								}
								// logger.info(" aggremq[1] " + remInsertQuery);
								query = entityManager.createNativeQuery(remInsertQuery.toString());
								query.executeUpdate();

								popullateRuleTmp(nrmlzMap, profileName, internalUuid, batchId, endToEndId, invoiceNo);

								updateProcesseBatch(configPojo, batchId, controlSum, invoiceAmt, endtoEndSet, colBatch,
										internalUuid);

								auditJPARepo.addBatchAuditMessage(internalId, batchId, paymentId,
										"Record pushed to both Agg and Rem table ", AuditJPARepo.PROCESSED);

							} catch (Exception exc) {
								errBatchUuidMap.put(batchId, internalUuid);
								// internalIdToEList.add(internalId);
								paymentErrPojo.setComment(exc.getMessage() + "Data Error " + exc.getMessage());

								updateStgProcesseBatch(configPojo, batchId, internalUuid, endToEndId,
										"Data Error " + exc.getMessage(), errendtoEndSet);

								auditJPARepo.addBatchAuditMessage(internalId, batchId, paymentId,
										"Data Error " + exc.getMessage(), AuditJPARepo.FAILED);

							}
							entityManager.flush();
							entityManager.clear();
						} catch (Exception ex) {
							errBatchUuidMap.put(batchId, internalUuid);
							logger.info(ex.getMessage() + "Data processing issue while moving to Nrmlz");
							paymentErrPojo.setComment("Data processing issue while moving to Nrmlz");

							updateStgProcesseBatch(configPojo, batchId, internalUuid, endToEndId,
									"Data processing issue while moving to Nrmlz", errendtoEndSet);

							auditJPARepo.addBatchAuditMessage(internalId, batchId, paymentId,
									"Data processing issue " + ex.getMessage(), AuditJPARepo.FAILED);
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

						i++;
					} else {
						logger.info("Base col is not initialized");
					}
				} else {
					logger.info("Ignored");
				}
			});
		});

		updateAckDetails(configPojo, batchPaymObjEr, errBatchUuidMap);

		inserNrmaliztDataIntoHandE(inserQueryNrmlz_H, inserQueryNrmlz_E);
		auditJPARepo.audiLogger(processId, nrmlRecordCount, AuditJPARepo.NRMTOAGG);
	}

	private void updateProcesseBatch(ConfigPojo configPojo, String batchId, String controlSum, String invoiceAmt,
			Set<String> endtoEndSet, String colBatch, String internalUuid) {

		// BigDecimal inv = new BigDecimal(invoiceAmt);
		// if (batchCtrlSumMap.containsKey(batchId)) {
		// BigDecimal suminv = inv.add(batchCtrlSumMap.get(batchId));
		// batchCtrlSumMap.put(batchId, suminv);
		// } else {
		// batchCtrlSumMap.put(batchId, inv);
		// }
		// if (controlSum.equals(batchCtrlSumMap.get(batchId).toString())
		// || 0 == batchCtrlSumMap.get(batchId).compareTo(new BigDecimal(controlSum))) {
		String updateStatus = "update " + XorConstant.AGG_PRTY_PYMT_DTLS + " set xml_status ='"
				+ XorConstant.STATUS_LOADED + "' where  " + XorConstant.AGG_MESSAGE_COLNAME + "='" + batchId
				+ "'  and internal_uuid = '" + internalUuid + "' ";
		logger.info("updateStatus ---- >" + updateStatus);

		query = entityManager.createNativeQuery(updateStatus);
		query.executeUpdate();

		String updateStgStatus = "update " + XorConstant.NRMLZ_PMT_DTLS + " set process_status ='"
				+ XorConstant.STATUS_MOVE + "' where " + colBatch + " ='" + batchId + "'   and internal_uuid = '"
				+ internalUuid + "' ";
		logger.info("updateStgStatus ---- >" + updateStgStatus);

		query = entityManager.createNativeQuery(updateStgStatus);
		query.executeUpdate();
		// }
	}

	private void updateStgProcesseBatch(ConfigPojo configPojo, String batchId, String internalUuid, String endToEndId,
			String comment, Set<String> errendtoEndSet) {

		if (errendtoEndSet.add(internalUuid)) {
			String updateStgStatus = "update " + XorConstant.NRMLZ_PMT_DTLS + " set process_status ='"
					+ XorConstant.STATUS_E + "', etl_process_comment='" + MessageConstant.BATCH100124 + "' where "
					+ colBatch + " ='" + batchId + "'   and internal_uuid = '" + internalUuid + "' ";
			logger.info("updateStgStatus ---- >" + updateStgStatus);

			query = entityManager.createNativeQuery(updateStgStatus);
			query.executeUpdate();
		}
		String updateStgStatus = "update " + XorConstant.NRMLZ_PMT_DTLS + " set process_status ='"
				+ XorConstant.STATUS_E + "', etl_process_comment='" + comment + "' where " + colPaymentIdSource + " ='"
				+ endToEndId + "'   and internal_uuid = '" + internalUuid + "' ";
		logger.info("updateStgStatus ---- >" + updateStgStatus);

		query = entityManager.createNativeQuery(updateStgStatus);
		query.executeUpdate();

	}

	private void popullateRuleTmp(Map<String, String> nrmlzMap, String profileName, String internalUuid, String batchId,
			String endToEndId, String invoiceNo) {
		List<RuleLookUpEntity> ruleColLookup = ruleLookUpRepo.findByProfileName(profileName);
		if (null != ruleColLookup && !ruleColLookup.isEmpty()) {

			StringBuilder insertRuleTmp = new StringBuilder("insert into " + XorConstant.RULE_AGG_LOOKUP
					+ " (profilename , message_id , end_to_end_id , internal_uuid, invoice_no, created_by, creation_date, last_updated_by,	last_update_date ");
			StringBuilder val = new StringBuilder("values('" + profileName + "', '" + batchId + "','" + endToEndId
					+ "', '" + internalUuid + "', '" + invoiceNo + "'");

			val.append(", '" + XorConstant.ADMIN_ROLE + "', now(), '" + XorConstant.ADMIN_ROLE + "', now() ");
			ruleColLookup.stream().forEach(re -> {
				if (null != nrmlzMap.get(re.getSrcColName().toUpperCase())) {
					insertRuleTmp.append(", " + re.getTempCol());
					val.append(", '" + nrmlzMap.get(re.getSrcColName().toUpperCase()) + "'");
				}
			});
			insertRuleTmp.append(")");
			val.append(")");
			insertRuleTmp.append(val);

			try {
				query = entityManager.createNativeQuery(insertRuleTmp.toString());
				query.executeUpdate();
			} catch (Exception e) {
				logger.info("Exception insertRuleTmp--->>> >  " + insertRuleTmp);
				e.printStackTrace();
			}
		}
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
		CommonJPAConstant.clearObject();
	}

	private void inserNrmaliztDataIntoHandE(StringBuilder inserQueryNrmlz_H, StringBuilder inserQueryNrmlz_E) {
		getInserQueryWithIds(inserQueryNrmlz_H, XorConstant.STATUS_MOVE);
		logger.info("inserQueryNrmlz_H - " + inserQueryNrmlz_H);
		query = entityManager.createNativeQuery(inserQueryNrmlz_H.toString());
		query.executeUpdate();
		entityManager.flush();
		entityManager.clear();
		logger.info("Executed");

		getInserQueryWithIds(inserQueryNrmlz_E, XorConstant.STATUS_E);
		logger.info("inserQueryNrmlz_E - " + inserQueryNrmlz_E);
		query = entityManager.createNativeQuery(inserQueryNrmlz_E.toString());
		query.executeUpdate();
		entityManager.flush();
		entityManager.clear();
		logger.info("Executed");
		cleanTable(XorConstant.NRMLZ_PMT_DTLS);
	}

	private void cleanTable(String tableName) {
		StringBuilder deleteQuery = new StringBuilder("delete from " + tableName + " where  process_status ='"
				+ XorConstant.STATUS_MOVE + "' or " + " process_status ='" + XorConstant.STATUS_E + "' ");

		query = entityManager.createNativeQuery(deleteQuery.toString());
		query.executeUpdate();
		entityManager.flush();
		entityManager.clear();

	}

	private void getInserQueryWithIds(StringBuilder query, Character status) {
		query.append(" where process_status ='" + status + "' ");
	}

	private void getQuerrMap(Map<String, List<Map<String, String>>> piuidMap, Map<String, Integer> profileMap,
			ConfigPojo configPojo) {
		querymap = new HashMap<>(1);
		nrmlzAggColMap = new HashMap<>(1);
		piuidMap.entrySet().stream().forEach(e -> {
			String profile = e.getKey();
			// insert quesy for agg
			List<NormalizeAggXmlTagsMapEntity> normalizeColMap = normalizeAggXmlTagsMapRepo
					.findByErpSrcSysAndPiuidSeqIdAndNrmlzColNameNotNullAndTagInfoTypeNotNullAndActiveInd(
							configPojo.getSOURCE_SYS_ID(), profileMap.get(profile), XorConstant.STATUS_Y);
			nrmlzAggColMap.put(profile, normalizeColMap);
			aggQuery = new StringBuilder("Insert into agg_prty_pymt_dtls  ");
			aggQuery.append("( slno, profilename, erp_src_sys, xml_status, internal_uuid");
			remQuery = new StringBuilder("Insert into agg_remittance_info  ");
			remQuery.append("( slno, erp_src_sys, internal_uuid");
			Set<String> aggColSet = new HashSet<>();
			Set<String> remColSet = new HashSet<>();
			aggColSet.add("slno");
			aggColSet.add("profilename");
			aggColSet.add("erp_src_sys");
			aggColSet.add("xml_status");
			aggColSet.add("internal_uuid");
			remColSet.add("slno");
			remColSet.add("erp_src_sys");
			remColSet.add("internal_uuid");
			i = 0 + 2;
			j = 0 + 1;
			normalizeColMap.stream().forEach(en -> {
				if (aggColSet.add(en.getAggColName().toLowerCase())
						&& (en.getTagInfoType().equalsIgnoreCase(XorConstant.TAG_INFO_AGG)
								|| en.getTagInfoType().equalsIgnoreCase(XorConstant.BOTH_INFO))) {
					if (i > 0) {
						aggQuery.append(", ");
					}
					aggQuery.append(en.getAggColName());
					i++;
				}
				if (remColSet.add(en.getAggColName().toLowerCase())
						&& (en.getTagInfoType().equalsIgnoreCase(XorConstant.TAG_INFO_REM)
								|| en.getTagInfoType().equalsIgnoreCase(XorConstant.BOTH_INFO))) {
					if (j > 0) {
						remQuery.append(", ");
					}
					remQuery.append(en.getAggColName());
					j++;
				}
			});
			aggQuery.append(" ) ");
			remQuery.append(" ) ");

			querymap.put(profile + XorConstant.AGG, aggQuery);
			querymap.put(profile + XorConstant.REM, remQuery);

		});

	}

	private void addValueAccordingToTypeFromNrmTOAgg(Integer srcDataType, StringBuilder insertNrmlQuery,
			Map<String, String> nrmlzMap, String nrmColName, ConfigPojo configPojo) {
		if (nrmColName != null) {
			if (nrmColName.contains(XorConstant.NRMLCOLDBREGEX)) {
				StringBuilder val = new StringBuilder();
				String cols[] = nrmColName.split(XorConstant.NRMLCOLDBREGEX);
				i = 0;
				for (String col : cols) {
					if (i > 0) {
						val.append("_");
					}
					if (null != col && null != nrmlzMap.get(col) && !nrmlzMap.get(col).isEmpty()) {
						val.append(nrmlzMap.get(col).toString());
						i++;
					}
				}
				if (val.toString().trim().length() != 0 && val.charAt(val.length() - 1) == '_') {
					val.deleteCharAt(val.length() - 1);
				}
				if (!val.toString().trim().isEmpty()) {
					insertNrmlQuery.append("substr('" + val + "', 1, 100)");
				} else {
					insertNrmlQuery.append("NULL");
				}

			} else {
				String val = nrmlzMap.get(nrmColName);
				if (null != val) {
					switch (srcDataType) {
					case 1:
						val = val.replaceAll("[^0-9]", "");
						insertNrmlQuery.append(val);
						break;
					case 2:
						insertNrmlQuery.append("substr('" + val + "', 1, 100)");
						break;
					case 3:
						insertNrmlQuery.append("substr('" + val + "', 1,100)");
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
						} else if (configPojo.getDATABASE_SYSTEM()
								.equalsIgnoreCase(CommonJPAConstant.MYSQL_DB_DATA_TYPE)) {
							insertNrmlQuery.append("cast('" + val + "' as DATETIME)");
						}
						break;
					case 7:
						if (configPojo.getDATABASE_SYSTEM().equalsIgnoreCase(CommonJPAConstant.POSTGRES_DB_DATA_TYPE)) {
							insertNrmlQuery.append("cast('" + val + "' as timestamp)");
						} else if (configPojo.getDATABASE_SYSTEM()
								.equalsIgnoreCase(CommonJPAConstant.MYSQL_DB_DATA_TYPE)) {
							insertNrmlQuery.append("cast('" + val + "' as DATETIME)");
						}
						break;
					}
				} else {
					insertNrmlQuery.append("NULL");
				}
			}
		} else {
			insertNrmlQuery.append("NULL");
		}
		// return insertNrmlQuery;
	}

}
