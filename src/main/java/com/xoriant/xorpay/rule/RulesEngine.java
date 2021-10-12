package com.xoriant.xorpay.rule;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.data.sync.repo.AuditJPARepo;
import com.xoriant.xorpay.data.sync.repo.CommonJPAConstant;
import com.xoriant.xorpay.data.sync.repo.CommonJPARepo;
import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.entity.PIUIDEntity;
import com.xoriant.xorpay.entity.RuleParamEntity;
import com.xoriant.xorpay.entity.SourceSysEntity;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.repo.AggregatedPaymentRepo;
import com.xoriant.xorpay.repo.PIUIDRepo;
import com.xoriant.xorpay.repo.RuleDefRepo;
import com.xoriant.xorpay.repo.RuleMapRepo;
import com.xoriant.xorpay.repo.RuleParamRepo;
import com.xoriant.xorpay.rule.pojo.RuleMapPOJO;

@Service
public class RulesEngine {

	@Autowired
	AggregatedPaymentRepo aggregatedPaymentRepository;
	@Autowired
	RuleMapRepo xpRuleMapRepository;
	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	RuleParamRepo xpRuleParamRepository;
	@Autowired
	RuleDefRepo xpRuleDefRepository;
	@Autowired
	RulesServiceUtil rulesServiceUtil;
	@Autowired
	RuleJPARepo ruleJpaRepo;
	@Autowired
	private AuditJPARepo auditJPARepo;
	@Autowired
	private CommonJPARepo commonJPARepo;
	@Autowired
	private PIUIDRepo piuidRepo;

	private final Logger logger = LoggerFactory.getLogger(RulesEngine.class);

	private Map<Integer, StringBuilder> aggSlnoMessageMap = new HashMap<>(1);
	private Set<String> remMessageIdSet = new HashSet<>(1);

	private int i = 0;
	private boolean flag;
	private String colValtmp;

	public String processRules(String processId, Map<String, Set<Integer>> supressTagSet, Set<String> addressTagSet,
			ConfigPojo configPojo) {

		entityManager = entityManager.getEntityManagerFactory().createEntityManager();
		Session session = entityManager.unwrap(Session.class);
		Transaction tr = session.beginTransaction();
		session.doWork(new Work() {

			@Override
			public void execute(Connection connections) throws SQLException {
				try (Connection connection = connections) {
					connection.setAutoCommit(true);
					startRule(processId, supressTagSet, addressTagSet, configPojo);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		return XorConstant.SUCCESS;

	}

	private String startRule(String processId, Map<String, Set<Integer>> supressTagSet, Set<String> addressTagSet,
			ConfigPojo configPojo) {
		List<String> profileNames = null;
		try {
			profileNames = aggregatedPaymentRepository.findDistinctProfileName(configPojo.getSOURCE_SYS_ID());
			logger.info("profileNames--" + profileNames.toString());
			processRulesForPIUID(processId, profileNames, supressTagSet, addressTagSet, configPojo);
		} catch (Exception e) {
			logger.error("RulesEngine : Error while getting payment data to process.");
			return XorConstant.FAILED;
		}

		return XorConstant.SUCCESS;
	}

	private String processRulesForPIUID(String processId, List<String> profileName,
			Map<String, Set<Integer>> supressTagSet, Set<String> addressTagSet, ConfigPojo configPojo) {
		List<AggregatedPaymentEntity> listValidPaymentEntiry = new ArrayList<AggregatedPaymentEntity>();
		// Map<AggregatedPaymentEntity, List<String>> mapInvalidPaymentEntiry = new
		// HashMap<AggregatedPaymentEntity, List<String>>();
		try {
			List<AggregatedPaymentEntity> listAggregatedPaymentEntity = aggregatedPaymentRepository
					.findByXmlStatusAndSourceSystem(XorConstant.STATUS_LOADED, configPojo.getSOURCE_SYS_ID());
			logger.info("listAggregatedPaymentEntity- " + listAggregatedPaymentEntity.size());
			if (null != listAggregatedPaymentEntity && !listAggregatedPaymentEntity.isEmpty()) {
				logger.info("First Record: " + listAggregatedPaymentEntity.get(0));
				Set<String> columnsAgg = new HashSet<>();
				Set<String> columnsRem = new HashSet<>();
				columnsAgg.add(RuleConstant.SLNO);
				columnsAgg.add(RuleConstant.PROFILENAME);
				columnsAgg.add(RuleConstant.MSGID_4);
				columnsAgg.add(RuleConstant.ENDTOENDID_33);
				columnsAgg.add(RuleConstant.INTERNAL_UUID);

				columnsRem.add(RuleConstant.SLNO);
				columnsRem.add(RuleConstant.MSGID_4);
				columnsRem.add(RuleConstant.ENDTOENDID_33);
				columnsRem.add(RuleConstant.NB_59);
				columnsRem.add(RuleConstant.INTERNAL_UUID);

				Map<String, Map<String, List<RuleMapPOJO>>> columnsWithRules = rulesServiceUtil
						.findCoulumnWithRules(profileName, columnsAgg, columnsRem);
				if (null != columnsWithRules && !columnsWithRules.isEmpty()) {
					logger.info("Column " + columnsAgg.toString());
					logger.info("Column " + columnsRem.toString());
					// Map<piuid, List<column, value>>
					Map<String, List<Map<String, String>>> piuidAggMap = new HashMap<>();
					// Map<mess, List<column, value>>
					Map<String, Map<String, List<Map<String, String>>>> messRmtMap = new HashMap<>();
					ruleJpaRepo.getResultSet(columnsAgg, columnsRem, piuidAggMap, messRmtMap, configPojo);
					// List<String> errorMessageList = new ArrayList<String>();

					messRmtMap.entrySet().stream().forEach(e -> {
						e.getValue().entrySet().stream().forEach(en -> {
							en.getValue().stream().forEach(ent -> {
								ent.entrySet().stream().forEach(entt -> {
									logger.info(entt.getKey() + " " + entt.getValue());
								});
							});
						});
					});
					aggSlnoMessageMap.clear();
					List<Integer> aggSlnoList = new ArrayList<>(1);

					List<Integer> aggSlnoValidList = new ArrayList<>(1);
					logger.info("---> " + piuidAggMap.size());
					piuidAggMap.entrySet().stream().forEach(e -> {
						String piuid = e.getKey();

						List<Map<String, String>> aggColValMapList = e.getValue();
						aggColValMapList.stream().forEach(aggColMap -> {
							logger.info("key-->> " + aggColMap.keySet());
							logger.info("Val", aggColMap.values());
							Integer aggSlno = Integer.parseInt(aggColMap.get(RuleConstant.SLNO));
							aggSlnoValidList.add(aggSlno);
							String messId = aggColMap.get(RuleConstant.MSGID_4);
							String aggEndToEndId = aggColMap.get(RuleConstant.ENDTOENDID_33);
							String prfleNme = aggColMap.get(RuleConstant.PROFILENAME);
							String internalUuid = aggColMap.get(RuleConstant.INTERNAL_UUID);
							String aggUniq = messId + "~" + aggEndToEndId;

							Map<String, List<Map<String, String>>> remColMapList = messRmtMap.get(aggUniq);

							if (null != remColMapList) {
								remColMapList.entrySet().stream().forEach(rmt -> {
									String invoiceNo = rmt.getKey();
									rmt.getValue().stream().forEach(rm -> {
										Integer remSlno = Integer.parseInt(rm.get(RuleConstant.SLNO));
										String remendToEndId = rm.get(RuleConstant.ENDTOENDID_33);
										logger.info("rm lsno " + remSlno);
										String remUniq = messId + "~" + remendToEndId + "~" + invoiceNo;
										logger.info("rm lsno " + remUniq);
										rm.entrySet().stream().forEach(rmCol -> {
											String colName = rmCol.getKey();
											String colValue = rmCol.getValue();
											if (null != columnsWithRules.get(piuid)) {
												List<RuleMapPOJO> ruleList = columnsWithRules.get(piuid).get(colName);
												if (null != ruleList) {
													validateColumn(remSlno, remUniq, colName, colValue, ruleList,
															XorConstant.TAG_INFO_REM, aggColMap, rm, supressTagSet,
															addressTagSet, prfleNme, aggUniq, remendToEndId,
															internalUuid, messId, invoiceNo);
												} else {
													logger.info("No rulelist for " + colName);
												}
											} else {
												logger.info("No rule defined for piuid " + piuid);
											}
										});
									});
								});
							}

							aggSlnoList.add(aggSlno);
							aggColMap.entrySet().stream().forEach(aggCol -> {
								String colName = aggCol.getKey();
								String colValue = aggCol.getValue();
								logger.info("aggg ====?<<<<<  " + colName + "  " + colValue);
								List<RuleMapPOJO> ruleList = columnsWithRules.get(piuid).get(colName);
								if (null != ruleList) {
									validateColumn(aggSlno, aggUniq, colName, colValue, ruleList,
											XorConstant.TAG_INFO_AGG, aggColMap, null, supressTagSet, addressTagSet,
											prfleNme, aggUniq, aggEndToEndId, internalUuid, messId, null);
								} else {
									logger.info("No rulelist for " + colName);
								}

							});

							logger.info(" remSlnoMessageMap - " + remMessageIdSet);
							logger.info("remMessageIdSet " + remMessageIdSet.toString());
							if (remMessageIdSet.contains(aggUniq)) {
								if (aggSlnoMessageMap.containsKey(aggSlno)) {
									aggSlnoMessageMap.put(aggSlno,
											aggSlnoMessageMap.get(aggSlno).append(" Failed in Remittance validation"));
								} else {
									aggSlnoMessageMap.put(aggSlno,
											new StringBuilder("Failed in Remittance validation"));
								}
							}

							logger.info("supressTagList " + supressTagSet.size());
							logger.info("Suprees tag " + supressTagSet);

						});
					});

					if (!aggSlnoMessageMap.isEmpty() && !aggSlnoList.isEmpty()) {
						List<Integer> aggSlnoValid = aggSlnoList.stream()
								.filter(slno -> !aggSlnoMessageMap.containsKey(slno)).collect(Collectors.toList());

						if (null != aggSlnoValid && !aggSlnoValid.isEmpty()) {
							StringBuilder updatequery = new StringBuilder("update " + XorConstant.AGG_PRTY_PYMT_DTLS
									+ " set xml_status ='" + XorConstant.STATUS_V + "'" + "	where slno in(");

							ruleJpaRepo.createColumnValue(aggSlnoValid, updatequery);
							updatequery.append(")");
							try {
								ruleJpaRepo.executeQuery(updatequery);

								aggSlnoValid.stream().forEach(e -> {
									auditJPARepo.addBatchAuditMessage("SLNO:: " + e, null, null, "Rule Validated",
											AuditJPARepo.VALIDATION_SUCCESSFULL);
								});
							} catch (Exception e) {
								aggSlnoValid.stream().forEach(ex -> {
									auditJPARepo.addBatchAuditMessage("Error", null, null, e.getMessage(),
											AuditJPARepo.FAILED);
								});
								e.printStackTrace();
							}
						}

						if (!aggSlnoMessageMap.isEmpty()) {
							SourceSysEntity srcSys = configPojo.getSrcSys();
							commonJPARepo.colProfileName = commonJPARepo.getMappedSourceColumnName(null,
									configPojo.getSOURCE_PROFILE_COLUMN_NAME(), XorConstant.SOURCE, null, srcSys);

							StringBuilder whereConition = new StringBuilder(" where slno in(");
							i = 0;
							aggSlnoMessageMap.entrySet().stream().forEach(en -> {
								if (i > 0) {
									whereConition.append(",");
								}
								whereConition.append(en.getKey());
								i++;
							});
							whereConition.append(")");
							// Data set
							Map<String, String> slnoProfileMap = new HashMap<>();
							Map<String, List<Map<String, String>>> piuidMap = getResultFromAgg(whereConition,
									slnoProfileMap);

							Set<String> profileList = piuidMap.keySet();
							Map<String, Integer> piuidSeqIdMap = piuidRepo
									.findByActiveIndAndProfileNameIgnoreCaseIn(XorConstant.STATUS_Y, profileList)
									.stream()
									.collect(Collectors.toMap(PIUIDEntity::getProfileName, PIUIDEntity::getId));

							aggSlnoMessageMap.entrySet().stream().forEach(e -> {

								StringBuilder updatequery = new StringBuilder("update " + XorConstant.AGG_PRTY_PYMT_DTLS
										+ " set xml_status ='" + XorConstant.STATUS_E + "' , etl_process_comment ='"
										+ e.getValue().toString() + "'  where slno in(" + e.getKey() + ")");
								updatequery.append(whereConition);
								try {
									ruleJpaRepo.executeQuery(updatequery);

									/// Stirng profileName = messIdProfileMap.get(e.getKey());

									commonJPARepo.initializeBasicColName(XorConstant.AGG, piuidSeqIdMap, srcSys);

									auditJPARepo.addBatchAuditMessage("SLNO:: " + e.getKey(), null, null,
											e.getValue().toString(), AuditJPARepo.VALIDATION_FAILED);

								} catch (Exception ex) {
									auditJPARepo.addBatchAuditMessage("Error", null, null, ex.getMessage(),
											AuditJPARepo.FAILED);
									ex.printStackTrace();
								}
							});
						}

						ruleJpaRepo.moveprocessedDate(auditJPARepo);

					} else if (null != aggSlnoValidList && !aggSlnoValidList.isEmpty()) {

						StringBuilder updatequery = new StringBuilder("update " + XorConstant.AGG_PRTY_PYMT_DTLS
								+ " set xml_status ='" + XorConstant.STATUS_V + "'" + "	where slno in(");

						ruleJpaRepo.createColumnValue(aggSlnoValidList, updatequery);
						updatequery.append(")");
						try {
							ruleJpaRepo.executeQuery(updatequery);
							aggSlnoValidList.stream().forEach(e -> {
								auditJPARepo.addBatchAuditMessage("SLNO:: " + e, null, null, "Rule Validated",
										AuditJPARepo.VALIDATION_SUCCESSFULL);
							});
						} catch (Exception e) {
							aggSlnoValidList.stream().forEach(ex -> {
								auditJPARepo.addBatchAuditMessage("Error", null, null, e.getMessage(),
										AuditJPARepo.FAILED);
							});
							e.printStackTrace();
						}

					}

					auditJPARepo.audiLogger(processId, null, AuditJPARepo.RULEENGINE);
				} else {
					logger.info("No Rule Present to Apply ");
					listValidPaymentEntiry = aggregatedPaymentRepository.findAll().stream().map(en -> {
						en.setXmlStatus(XorConstant.STATUS_V);
						return en;
					}).collect(Collectors.toList());
					aggregatedPaymentRepository.saveAll(listValidPaymentEntiry);
				}

			} else {
				logger.info("Rule :: No Data to process");
			}

			logger.info("Valid result paased");
			return XorConstant.SUCCESS;

		} catch (Exception e) {
			logger.info("No Rule Applied :: Error while processing rules ");

			listValidPaymentEntiry = aggregatedPaymentRepository.findAll().stream().map(en -> {
				en.setXmlStatus(XorConstant.STATUS_V);
				return en;
			}).collect(Collectors.toList());

			aggregatedPaymentRepository.saveAll(listValidPaymentEntiry);
			e.printStackTrace();
			return XorConstant.FAILED;
		}

	}

	public Map<String, List<Map<String, String>>> getResultFromAgg(StringBuilder whereCondition,
			Map<String, String> slnoProfileMap) {

		StringBuilder selectQuery = getSelectQueryForNrmlzTable(whereCondition);
		logger.info("Rule Data Query- " + selectQuery);
		List<Object[]> rsl = ruleJpaRepo.getResult(selectQuery);
		Map<String, List<Map<String, String>>> piuidMap = new HashMap<>(1);
		int total;
		List<String> colList = new ArrayList<>();
		colList.add("slno");
		colList.addAll(Arrays.asList(CommonJPAConstant.colList));

		if (rsl.size() > 0) {
			total = colList.size();
			rsl.stream().forEach(e -> {
				commonJPARepo.nrmlzProfile = null;
				String messId = null;
				Map<String, String> nrmlzMap = new HashMap<>();
				for (int r = 0; r < total; r++) {
					if (null != e[r]) {
						String col = colList.get(r).trim();
						if (col.equalsIgnoreCase(commonJPARepo.colProfileName)) {
							commonJPARepo.nrmlzProfile = e[r].toString();
						}
						if (col.equalsIgnoreCase(commonJPARepo.colPaymentIdSource)) {
							messId = e[r].toString();
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
					slnoProfileMap.put(messId, commonJPARepo.nrmlzProfile);
					commonJPARepo.nrmlzProfile = null;
				}
			});
		}

		return piuidMap;
	}

	public StringBuilder getSelectQueryForNrmlzTable(StringBuilder whereCondition) {

		StringBuilder selectQuery = new StringBuilder("Select slno");
		i = 1;
		Set<String> columnName = new HashSet<>(1);
		Stream.of(CommonJPAConstant.colList).forEach(e -> {
			if (columnName.add(e.toLowerCase())) {
				if (i > 0) {
					selectQuery.append(", ");
				}
				selectQuery.append(e.toLowerCase());
				i++;
			}
		});
		selectQuery.append(" from  " + XorConstant.AGG_PRTY_PYMT_DTLS + " " + whereCondition);
		return selectQuery;
	}

	private void validateColumn(Integer aggSlno, String unimessIdEndToEnd, String colName, String colValue,
			List<RuleMapPOJO> rulesMapList, String tagInfoType, Map<String, String> aggColMap, Map<String, String> rm,
			Map<String, Set<Integer>> supressTagSet, Set<String> addressTagSet, String prfleNme, String msgEndId,
			String endToEndId, String internalUuid, String messageId, String invoiceNo) {
		logger.info("rulesMapList " + rulesMapList);
		String errMessage = null;

		List<RuleMapPOJO> list = rulesMapList.stream().filter(e -> e.getRuleName().equalsIgnoreCase(RuleConstant.DATE))
				.collect(Collectors.toList());
		if (list.isEmpty()) {
			for (RuleMapPOJO rule : rulesMapList) {
				RuleParamEntity ruleParam = rule.getRuleParam();
				logger.info("For Rule " + rule.getRuleName().toUpperCase());
				switch (rule.getRuleName().toUpperCase()) {
				case RuleConstant.SUPRESS:
					logger.info("Supreessing strt tag - " + rule.getTagSeqid());
					addToSupressMap(supressTagSet, prfleNme + msgEndId, rule.getTagSeqid());
					break;
				case RuleConstant.ALPHANUMERIC:
					if (null == colValue || colValue.isEmpty())
						continue;
					boolean isAlphaNumeric = ConnectorUtil.isAlphaNumeric(colValue);
					if (!isAlphaNumeric) {
						errMessage = "For Column :: " + colName + " Val - " + colValue + " is not AlphaNumeric. ";
						addAggSlnoErrMessage(aggSlno, errMessage, tagInfoType, unimessIdEndToEnd);
					}
					break;

				case RuleConstant.MANDATORY:
					boolean isMandatory = ConnectorUtil.isManditory(colValue);
					if (!isMandatory) {
						errMessage = "For Column :: " + colName + "Value Is Empty ";
						addAggSlnoErrMessage(aggSlno, errMessage, tagInfoType, unimessIdEndToEnd);
					}
					break;

				case RuleConstant.LENGTH:
					if (null != ruleParam) {
						int minLength = 0, maxLength = 0;
						try {
							minLength = ruleParam.getLengthMin();
							maxLength = ruleParam.getLengthMax();
							if (null != ruleParam.getRuleApplyLevel()) {
								switch (ruleParam.getRuleApplyLevel().toLowerCase()) {
								case RuleConstant.TRIM:
									colValue = ConnectorUtil.subString(maxLength, colValue);
									createAndUpdateQuery(aggSlno, colName, colValue, tagInfoType);
									break;
								case RuleConstant.STRICK:
									boolean allowedLength = ConnectorUtil.lengthCheck(colValue, minLength, maxLength);
									if (!allowedLength) {
										errMessage = "For Column :: " + colName + " Val - " + colValue
												+ " length exceeds limit : min:" + minLength + " max:" + maxLength;
										addAggSlnoErrMessage(aggSlno, errMessage, tagInfoType, unimessIdEndToEnd);
									}
									break;
								case RuleConstant.IFNOTAVAILABLE:

									break;
								}
							} else {
								boolean allowedLength = ConnectorUtil.lengthCheck(colValue, minLength, maxLength);
								if (!allowedLength) {
									errMessage = "For Column :: " + colName + " Val - " + colValue
											+ " length exceeds limit : min:" + minLength + " max:" + maxLength;
									addAggSlnoErrMessage(aggSlno, errMessage, tagInfoType, unimessIdEndToEnd);
								}
							}
						} catch (Exception e) {
							errMessage = "For Column :: " + colName + " Length params not configured for: "
									+ rule.getFieldName();
							addAggSlnoErrMessage(aggSlno, errMessage, tagInfoType, unimessIdEndToEnd);
							logger.info("Error while getting LENGTH params for ruleMapId : " + rule.getRuleMapSeqId()
									+ " fileldName : " + rule.getFieldName());
							// e.printStackTrace();
						}
					}
					break;
				case RuleConstant.PREFIX:
					if (null != ruleParam) {
						String prfix = ruleParam.getMappingValues();
						if (null != prfix && !prfix.trim().isEmpty()) {
							if (null != ruleParam.getRuleApplyLevel()) {
								switch (ruleParam.getRuleApplyLevel().toLowerCase()) {
								case RuleConstant.TRIM:
									break;
								case RuleConstant.STRICK:
									break;
								case RuleConstant.IFNOTAVAILABLE:
									colValue = prfix;
									break;
								default:
									colValue = ConnectorUtil.addPrefix(colValue, prfix);
									break;
								}
							} else {
								colValue = ConnectorUtil.addPrefix(colValue, prfix);
							}
							createAndUpdateQuery(aggSlno, colName, colValue, tagInfoType);
						} else {
							logger.info("Rule :: No prefix has been found for: " + colValue);
						}
					}
					break;
				case RuleConstant.NUMBER:
					boolean isNumeric = ConnectorUtil.isNumeric(colValue);
					if (!isNumeric) {
						errMessage = "For Column :: " + colName + " Val - " + colValue + " is not Numeric";
						addAggSlnoErrMessage(aggSlno, errMessage, tagInfoType, unimessIdEndToEnd);
					}
					break;
				case RuleConstant.CONDPOPULATE:
					if (null != ruleParam && null != ruleParam.getRuleApplyLevel()) {

						String ruleApplyLevel = ruleParam.getRuleApplyLevel();

						switch (ruleApplyLevel) {
						case RuleConstant.CONDPOPULATE:
							String condRule = ruleParam.getConditionalRule();
							if (null != condRule) {
								String[] ruleArr = condRule.split("-");
								if (ruleArr.length > 1) {
									String optColValue = null;
									String optColName = ruleArr[0].toLowerCase();
									String isPresent = ruleArr[1];

									if (null != aggColMap.get(optColName)) {
										optColValue = aggColMap.get(optColName);
										logger.info("agg");
									} else if (null != rm && null != rm.get(optColName)) {
										optColValue = rm.get(optColName);
										logger.info("rem");
									}

									if (null != optColValue && !optColValue.isEmpty()
											&& isPresent.equalsIgnoreCase(XorConstant.STATUS_Y.toString())) {
										String[] tagIdArr = ruleParam.getMappingValues().split(",");
										for (String tagId : tagIdArr) {
											addToSupressMap(supressTagSet, prfleNme + msgEndId,
													Integer.parseInt(tagId));
										}
									} else if ((null == optColValue || optColValue.isEmpty())
											&& isPresent.equalsIgnoreCase(XorConstant.STATUS_N.toString())) {
										String[] tagIdArr = ruleParam.getMappingValues().split(",");
										for (String tagId : tagIdArr) {
											addToSupressMap(supressTagSet, prfleNme + msgEndId,
													Integer.parseInt(tagId));
										}
									}
								}
							}
							break;
						}
					}
					break;
				case RuleConstant.OPTIONAL:

					logger.info(colName + " " + colValue + " --> " + RuleConstant.OPTIONAL);
					if (null != ruleParam && null != ruleParam.getRuleApplyLevel()
							&& ruleParam.getRuleApplyLevel().equalsIgnoreCase(RuleConstant.CONCAT)) {
						if (null != rule.getOptionalRuleMapList()) {
							List<RuleMapPOJO> optionnalRuleList = rule.getOptionalRuleMapList();
							StringBuilder sb = new StringBuilder();

							optionnalRuleList.stream().forEach(e -> {
								logger.info(" e - " + e.toString());
							});
							optionnalRuleList.stream().forEach(oe -> {
								String optColName = oe.getFieldName();
								String optColValue = null;
								if (null != aggColMap.get(optColName)) {
									optColValue = aggColMap.get(optColName);
									logger.info("agg");
								} else if (null != rm && null != rm.get(optColName)) {
									optColValue = rm.get(optColName);
									logger.info("rem");
								}

								logger.info(optColName + " val - " + optColValue);
								if (null != optColValue && !optColValue.trim().isEmpty()) {
									RuleParamEntity optRuleParam = oe.getRuleParam();
									switch (oe.getRuleName().toUpperCase()) {
									case RuleConstant.DATE:
										try {
											LocalDateTime ldt = LocalDateTime.parse(optColValue,
													XorConstant.formateYYYYMMDD_HHMMSSZ);
											sb.append(ldt.format(XorConstant.formateYYYYMMDD));
										} catch (Exception e) {
											if (optColValue.length() > 9) {
												sb.append(optColValue.substring(0, 9));
											}
										}
										break;
									default:
										sb.append(optColValue);
										break;
									}
									logger.info(
											"optRuleParam.getConcatSeperator() " + optRuleParam.getConcatSeperator());
									if (null != optRuleParam.getConcatSeperator()
											&& optRuleParam.getConcatSeperator().trim().length() > 0) {
										sb.append(optRuleParam.getConcatSeperator().trim());
									} else if (null != optRuleParam.getConcatSeperator()
											&& optRuleParam.getConcatSeperator().trim().isEmpty()) {
										sb.append(" ");
									}
								}

							});
							if (!sb.toString().isEmpty()) {
								int si = sb.length() - 1;
								if (sb.charAt(si) == ',') {
									sb.deleteCharAt(si);
								}
							}

							logger.info("Testing concating " + sb.toString());
							colValue = sb.toString();
							logger.info("colValue  " + colValue);
							createAndUpdateQuery(aggSlno, colName, colValue, tagInfoType);
						}
					} else if (null != ruleParam && null != ruleParam.getRuleApplyLevel()
							&& ruleParam.getRuleApplyLevel().equalsIgnoreCase(RuleConstant.COALESCE)) {

						if (null != rule.getOptionalRuleMapList()) {
							List<RuleMapPOJO> optionnalRuleList = rule.getOptionalRuleMapList();
							StringBuilder sb = new StringBuilder();

							optionnalRuleList.stream().forEach(e -> {
								logger.info("COALESCE e - " + e.toString());
							});
							flag = true;
							optionnalRuleList.stream().forEach(oe -> {
								String optColName = oe.getFieldName();
								String optColValue = null;
								logger.info("optColName " + optColName);
								logger.info(aggColMap.keySet() + " <<>> " + aggColMap.values());
								if (null != aggColMap.get(optColName)) {
									optColValue = aggColMap.get(optColName);
									logger.info("agg");
								} else if (null != rm && null != rm.get(optColName)) {
									optColValue = rm.get(optColName);
									logger.info("rem");
								}

								logger.info(optColName + " val - " + optColValue);
								if (flag && null != optColValue && !optColValue.trim().isEmpty()) {
									RuleParamEntity optRuleParam = oe.getRuleParam();
									switch (oe.getRuleName().toUpperCase()) {
									default:
										sb.append(optColValue);
										break;
									}
									logger.info(optRuleParam.getId() + "optRuleParam.getConcatSeperator() "
											+ optRuleParam.getConcatSeperator());
									if (null != optRuleParam.getConcatSeperator()
											&& optRuleParam.getConcatSeperator().trim().length() > 0) {
										sb.append(optRuleParam.getConcatSeperator().trim());
									} else if (null != optRuleParam.getConcatSeperator()
											&& optRuleParam.getConcatSeperator().trim().isEmpty()) {
										sb.append(" ");
									}
									flag = false;
								}

							});
							logger.info("Testing conca----");
							int cd = sb.lastIndexOf(",");
							logger.info("cd--" + cd);
							if (cd > 0) {
								sb.deleteCharAt(cd);
							}
							logger.info("Testing cc concating " + sb.toString());

							colValue = sb.toString();
							logger.info("colValue  " + colValue);
							createAndUpdateQuery(aggSlno, colName, colValue, tagInfoType);
						}
					} else if (null != ruleParam && null != ruleParam.getRuleApplyLevel()
							&& ruleParam.getRuleApplyLevel().equalsIgnoreCase(RuleConstant.CONDSUPRESS)) {
						StringBuilder sb = new StringBuilder();

						List<RuleParamEntity> ruleParamList = rule.getRuleParamList();
						logger.info(" CONDSUPRESS ruleParamList " + ruleParamList.size());
						ruleParamList.stream().forEach(ruleParamE -> {
							String[] conditionRuel = ruleParamE.getConditionalRule().split("-");
							logger.info("conditionRuel " + conditionRuel);
							String col_name = conditionRuel[0].toLowerCase();
							String valToMatch = null;
							if (conditionRuel.length > 1) {
								valToMatch = conditionRuel[1];
							}
							// logger.info("aggColMap " + aggColMap.toString());
							// logger.info("rm -" + rm.toString());
							String optColValue = null;
							if (null != aggColMap.get(col_name)) {
								optColValue = aggColMap.get(col_name);
								logger.info("agg");
							} else if (null != rm && null != rm.get(col_name)) {
								optColValue = rm.get(col_name);
								logger.info("rem");
							}
							logger.info("valToMatch " + valToMatch + " optColValue" + optColValue);
							if (null != valToMatch && null != optColValue) {
								if (valToMatch.toLowerCase().contains(optColValue.toLowerCase())) {
									sb.append(optColValue);
									String[] tagIdArr = ruleParamE.getMappingValues().split(",");
									for (String tagId : tagIdArr) {
										addToSupressMap(supressTagSet, prfleNme + msgEndId, Integer.parseInt(tagId));
									}
								}
							}
						});

						if (!sb.toString().isEmpty()) {
							logger.info("Testing Condition Suprees concating " + sb.toString());
							colValue = sb.toString();
							logger.info("colValue  " + colValue);
							createAndUpdateQuery(aggSlno, colName, colValue, tagInfoType);
						}
					} else if (null != ruleParam && null != ruleParam.getRuleApplyLevel()
							&& ruleParam.getRuleApplyLevel().equalsIgnoreCase(RuleConstant.CONDLOOKUP)) {
						// tag4 -> cd -val match, tag4 value/populatw table column
						colValtmp = null;
						List<RuleParamEntity> ruleParamList = rule.getRuleParamList();
						logger.info(" CONDLOOKUP ruleParamList======<<<<< ====  " + ruleParamList.size());
						ruleParamList.stream().forEach(ruleParamE -> {

							if (null != ruleParamE.getConditionalRule()) {
								logger.info(" <<<====  rule param   stat =================>>>>>>>>>");
								String colValtemp = null;
								boolean toCheck = false;
								if (null != ruleParamE.getLookupTable() && !ruleParamE.getLookupTable().isEmpty()) {
									String[] lookup = ruleParamE.getLookupTable().split("-");
									if (lookup.length > 1) {
										String tableName = lookup[0];
										String colname = lookup[1];
										colValtemp = getTempColVal(prfleNme, endToEndId, internalUuid, messageId,
												tableName, colname, invoiceNo);

										if (null != colValtemp && !colValtemp.trim().isEmpty()) {
											toCheck = true;
										}
									}
								} else {
									toCheck = true;
								}

								if (toCheck) {
									logger.info("colValtemp = " + colValtemp);
									boolean isValMatched = false;

									String coditionBase = ruleParamE.getConditionalRule().trim().replace(" ", "");
									String condition = " ";
									boolean x_and = false, x_or = false;

									if (coditionBase.contains(XorConstant.X_AND.toString())) {
										condition = XorConstant.X_AND.toString();
										x_and = true;
									} else if (coditionBase.contains(XorConstant.X_OR.toString())) {
										condition = XorConstant.X_OR.toString();
										x_or = true;
									}
									logger.info("isValMatched======= = " + isValMatched + "    condition = " + condition
											+ "  on " + coditionBase);
									String[] andConditionArr = coditionBase.split(condition);
									for (String colVal : andConditionArr) {
										logger.info("Col vale ===== " + colVal);
										if (null != colVal && !colVal.trim().isEmpty()) {
											String[] conditionRule = colVal.split("-");
											String col_name = conditionRule[0].toLowerCase();
											String valToMatch = null;
											boolean notFlag = false;
											if (conditionRule.length > 1) {
												valToMatch = conditionRule[1];
												if (valToMatch.contains("!")) {
													notFlag = true;
													valToMatch = valToMatch.replace("!", "");
												}

											}

											String optColValue = null;
											if (col_name.contains("temp")) {
												// tablename:columnname
												// xp_rule_lookup_agg_src_val:temp_col12
												String[] tabCol = col_name.split(":");
												if (tabCol.length > 1) {
													String table = tabCol[0];
													String colname = tabCol[1];
													optColValue = getTempColVal(prfleNme, endToEndId, internalUuid,
															messageId, table, colname, invoiceNo);
												}
											} else {
												if (null != aggColMap.get(col_name)) {
													optColValue = aggColMap.get(col_name);
												} else if (null != rm && null != rm.get(col_name)) {
													optColValue = rm.get(col_name);
												}
											}
											logger.info(colValtemp + " valToMatch====<<<  " + valToMatch
													+ " optColValue  " + optColValue);

											if (null == optColValue || optColValue.trim().isEmpty()) {
												optColValue = "NULL";
											}
											if (null != valToMatch && !valToMatch.trim().isEmpty()) {

												if (notFlag && !valToMatch.toLowerCase()
														.contains(optColValue.toLowerCase())) {
													isValMatched = true;
													if (x_or) {
														break;
													}
												} else if (!notFlag && valToMatch.toLowerCase()
														.contains(optColValue.toLowerCase())) {
													isValMatched = true;
													if (x_or) {
														break;
													}
												} else {
													isValMatched = false;
													if (x_and) {
														break;
													}
												}
											}
										}
									}
									logger.info("isValMatched======= " + isValMatched + "  == " + colValtemp);
									if (isValMatched) {
										createAndUpdateQuery(aggSlno, colName, colValtemp, tagInfoType);
										if (null != ruleParamE.getMappingValues()
												&& !ruleParamE.getMappingValues().trim().isEmpty()) {
											String[] tagIdArr = ruleParamE.getMappingValues().split(",");
											logger.info("on val matched Suppresss tag " + prfleNme + msgEndId + "  == "
													+ ruleParamE.getMappingValues());
											for (String tagId : tagIdArr) {
												addToSupressMap(supressTagSet, prfleNme + msgEndId,
														Integer.parseInt(tagId));
											}
										}
									}
								}
								logger.info(" <<<====  -----END------=================>>>>>>>>>");
							}
						});
						logger.info("++++++++++++++++=========Lookup END colValue ++++======= " + colValue);
					} else if (null != ruleParam && null != ruleParam.getRuleApplyLevel()
							&& ruleParam.getRuleApplyLevel().equalsIgnoreCase(RuleConstant.CONDADDRESS)) {
						// company adree(col875)-> park avanuew| bg-12| A| Park Streen
						// City-> Colkala, Inidia
						// <adr> City || col875:|:2 <addr>
						// <addre>Agg || agg12</adrr>
						// Only to take index of the val condition_base -> Add54:|:2||Agg44||Agg12:|:1 ,
						// Agg||agg12 , Agg10||agg17

						logger.info("ruleParam.getConditionalRule() " + ruleParam.getConditionalRule());
						if (null != ruleParam.getConditionalRule()) {
							String[] adrlineArr = ruleParam.getConditionalRule().trim().split(",");
							String optColValue = null;
							String col_name = null;
							String spliter = null;
							StringBuilder addrSb = new StringBuilder();
							for (String adrlines : adrlineArr) {
								logger.info("adrlines -" + adrlines);
								// Only if any othe val needed to indexed out
								StringBuilder addrline = new StringBuilder();
								String[] addrLineArr = adrlines.split(Pattern.quote("||"));
								for (String addrlineCols : addrLineArr) {
									optColValue = null;
									if (addrlineCols.contains(":")) {
										String[] addrLine1Arr = adrlines.split(":");
										col_name = addrLine1Arr[0].toLowerCase();
										spliter = addrLine1Arr[1];
										Integer index = Integer.parseInt(addrLine1Arr[2]);

										if (null != aggColMap.get(col_name)) {
											optColValue = aggColMap.get(col_name);
										} else if (null != rm && null != rm.get(col_name)) {
											optColValue = rm.get(col_name);
										}
										try {
											logger.info(index + " " + spliter + " optColValue -" + optColValue);
											if (null != optColValue && null != spliter && null != index) {
												optColValue = optColValue.split(Pattern.quote(spliter))[index];
											}
										} catch (Exception e) {
											logger.info("Exception " + index + " " + spliter + " optColValue -"
													+ optColValue);
										}
									} else {
										// System.out.println("aggColMap "+ aggColMap.toString());
										col_name = addrlineCols.toLowerCase();
										if (null != aggColMap.get(col_name)) {
											optColValue = aggColMap.get(col_name);
										} else if (null != rm && null != rm.get(col_name)) {
											optColValue = rm.get(col_name);
										}
									}
									logger.info("optColValue -" + optColValue);
									if (null != optColValue) {
										if (!addrline.toString().trim().isEmpty()) {
											addrline.append(", ");
										}
										addrline.append(optColValue);
									}
									logger.info("addrline -" + addrline);
								}
								if (!addrline.toString().trim().isEmpty()) {
									addrSb.append(addrline + "||");
								}
								logger.info("addrSb -" + addrSb);
							}
							logger.info("Address Line " + addrSb);// adre1||adre2||adre3
							if (!addrSb.toString().trim().isEmpty()) {
								createAndUpdateQuery(aggSlno, colName, addrSb.toString(), tagInfoType);
							}
							if (null != ruleParam.getMappingValues()
									&& !ruleParam.getMappingValues().trim().isEmpty()) {
								String[] tagIdArr = ruleParam.getMappingValues().split(",");
								logger.info("on val matched Suppresss tag " + prfleNme + msgEndId + "  == "
										+ ruleParam.getMappingValues());
								for (String tagId : tagIdArr) {
									addToSupressMap(supressTagSet, prfleNme + msgEndId, Integer.parseInt(tagId));
								}
							}
							addToAddressSet(addressTagSet, prfleNme + msgEndId);

						} else {
							logger.info("No Condition Adress Rule to Apply");
						}
					}
					break;
				case RuleConstant.DATE:
					// is appliyed at stg level
					if (false && null != ruleParam) {
						String dateFormat = null;
						try {
							dateFormat = ruleParam.getMappingValues();
							if (dateFormat == null) {
								// erroMessage.add("Unable to convert date: " + colValue + " into format: " +
								// dateFormat);
							} else {
								try {
									String formattedDate = ConnectorUtil.convertDate(colValue, dateFormat);
									LocalDateTime dateTime = LocalDateTime.parse(formattedDate);
								} catch (Exception e) {
									errMessage = "For Column :: " + colName + "Unable to convert date: " + colValue
											+ " into format: " + dateFormat;
									addAggSlnoErrMessage(aggSlno, errMessage, tagInfoType, unimessIdEndToEnd);
								}
							}
						} catch (Exception e) {
							errMessage = "For Column :: " + colName + "Unable to convert date: " + colValue
									+ " into format: " + dateFormat;
							addAggSlnoErrMessage(aggSlno, errMessage, tagInfoType, unimessIdEndToEnd);
						}
					}
					break;

				default:
					logger.info("No rule handler present for " + rule.getRuleName());
					break;
				}
			}
		} else {
			logger.info("Already applied at staging");
		}

	}

	private String getTempColVal(String prfleNme, String endToEndId, String internalUuid, String messageId,
			String tableName, String colname, String invoiceNo) {
		String colVal = null;

		StringBuilder selectQuery = new StringBuilder("select coalesce(" + colname + ") from " + tableName
				+ " where profilename ='" + prfleNme + "' and message_id ='" + messageId + "' " + "and end_to_end_id ='"
				+ endToEndId + "' and internal_uuid ='" + internalUuid + "' ");

		if (null != invoiceNo && !invoiceNo.trim().isEmpty()) {
			selectQuery.append(" and invoice_no ='" + invoiceNo + "' ");
		}

		logger.info("selectQuery lookup source --->> " + selectQuery);
		List<Object> rsl = ruleJpaRepo.getResultObject(selectQuery);
		if (null != rsl) {
			for (Object el : rsl) {
				if (null != el) {
					String ar = el.toString();
					colVal = ar.replace("[", "").replace("]", "");
				}
			}
		}

		updateProcessStatus(prfleNme, endToEndId, internalUuid, messageId, tableName, colname, invoiceNo);

		return colVal;
	}

	private void updateProcessStatus(String prfleNme, String endToEndId, String internalUuid, String messageId,
			String tableName, String colname, String invoiceNo) {

		StringBuilder updatequery = new StringBuilder(
				"update " + tableName + " set process_status ='" + XorConstant.STATUS_PAYMENT_PROCESSED
						+ "' where profilename ='" + prfleNme + "' and message_id ='" + messageId + "' "
						+ "and end_to_end_id ='" + endToEndId + "' and internal_uuid ='" + internalUuid + "' ");

		if (null != invoiceNo && !invoiceNo.trim().isEmpty()) {
			updatequery.append(" and invoice_no ='" + invoiceNo + "' ");
		}

		ruleJpaRepo.executeQuery(updatequery);

	}

	private void addToSupressMap(Map<String, Set<Integer>> supressTagSet, String prfleNme, Integer tagSeqid) {
		if (supressTagSet.containsKey(prfleNme)) {
			Set<Integer> tagSet = supressTagSet.get(prfleNme);
			tagSet.add(tagSeqid);
			supressTagSet.put(prfleNme, tagSet);
		} else {
			Set<Integer> tagSet = new HashSet<>();
			tagSet.add(tagSeqid);
			supressTagSet.put(prfleNme, tagSet);
		}
	}

	private void addToAddressSet(Set<String> addressSet, String prfleNme) {
		addressSet.add(prfleNme);
	}

	private void addAggSlnoErrMessage(Integer slno, String errMessage, String tagInfoType, String messId) {
		if (tagInfoType.equalsIgnoreCase(XorConstant.TAG_INFO_AGG)) {
			if (aggSlnoMessageMap.containsKey(slno)) {
				StringBuilder sb = aggSlnoMessageMap.get(slno);
				sb.append(errMessage);
				aggSlnoMessageMap.put(slno, sb);
			} else {
				StringBuilder sb = new StringBuilder(errMessage);
				aggSlnoMessageMap.put(slno, sb);
			}
		}
		if (tagInfoType.equalsIgnoreCase(XorConstant.TAG_INFO_REM)) {
			remMessageIdSet.add(messId);
		}
	}

	private void createAndUpdateQuery(Integer aggSlno, String colName, String colValue, String tagInfoType) {
		String table = null;
		if (tagInfoType.equalsIgnoreCase(XorConstant.TAG_INFO_AGG)) {
			table = XorConstant.AGG_PRTY_PYMT_DTLS;
		} else {
			table = XorConstant.AGG_REMITTANCE_INFO;
		}
		String val = "NULL";
		if (null != colValue) {
			val = "'" + colValue + "'";
		}
		StringBuilder updatequery = new StringBuilder("update " + table + " set " + colName + "= " + val + " where "
				+ RuleConstant.SLNO + " =" + aggSlno + "");
		logger.info("updatting *********************************** " + colName + "= " + val);

		ruleJpaRepo.executeQuery(updatequery);

	}

}
