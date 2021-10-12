package com.xoriant.xorpay.rule;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.entity.NormalizeAggXmlTagsMapEntity;
import com.xoriant.xorpay.entity.PIUIDEntity;
import com.xoriant.xorpay.entity.RuleDefEntity;
import com.xoriant.xorpay.entity.RuleMapEntity;
import com.xoriant.xorpay.entity.RuleParamEntity;
import com.xoriant.xorpay.repo.AggregatedPaymentRepo;
import com.xoriant.xorpay.repo.NormalizeAggXmlTagsMapRepo;
import com.xoriant.xorpay.repo.PIUIDRepo;
import com.xoriant.xorpay.repo.RuleDefRepo;
import com.xoriant.xorpay.repo.RuleMapRepo;
import com.xoriant.xorpay.repo.RuleParamRepo;
import com.xoriant.xorpay.rule.pojo.RuleMapPOJO;

@Service
public class RulesServiceUtil {

	private final Logger logger = LoggerFactory.getLogger(RulesServiceUtil.class);

	@Autowired
	RuleMapRepo xpRuleMapRepository;
	@Autowired
	RuleDefRepo xpRuleDefRepository;
	@Autowired
	RuleParamRepo xpRuleParamRepository;
	@Autowired
	AggregatedPaymentRepo aggregatedPaymentRepository;

	@PersistenceContext
	EntityManager entityManager;

	@Autowired
	PIUIDRepo piuidRepo;

	@Autowired
	NormalizeAggXmlTagsMapRepo normalizeAggXmlTagsMapRepository;

	private List<RuleMapEntity> ruleMapList = null;
	private Map<Integer, String> SeqIdPiuidMap = null;

	public Map<String, String> findAllColumns(String table) {
		AggregatedPaymentEntity aggregatedPaymentEntity = new AggregatedPaymentEntity();
		Map<String, String> columnNames = new HashMap<String, String>();
		for (Field field : aggregatedPaymentEntity.getClass().getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				columnNames.put(column.name(), field.getName());
			}
		}
		return columnNames;
	}

	private List<RuleMapPOJO> getRulesForColumns(Integer tagSeqId, String fieldName) {

		Map<Integer, RuleDefEntity> rulesMap = xpRuleDefRepository.findAll().stream()
				.collect(Collectors.toMap(RuleDefEntity::getId, Function.identity()));

		return xpRuleMapRepository.findByTagSeqidOrderByRuleMapPriority(tagSeqId).stream().map(e -> {
			RuleMapPOJO pojo = new RuleMapPOJO();
			List<RuleParamEntity> paramList = xpRuleParamRepository.findByRuleMapId(e.getId());
			pojo.setRuleMapSeqId(e.getId());
			if (null != e.getRuleDefId()) {
				pojo.setRuleDefId(e.getRuleDefId());
			}
			pojo.setRuleName((rulesMap.get(pojo.getRuleDefId()).getRuleName()));
			// if (null != paramList && paramList.size() != 0)
			// pojo.setRuleParamLst(paramList);
			pojo.setTagSeqid(tagSeqId);
			pojo.setFieldName(fieldName);
			return pojo;
		}).collect(Collectors.toList());

	}

	public Map<String, List<RuleMapPOJO>> findCoulumnWithRules(String profName) {

		Integer piuidSeqId = null;
		List<Integer> tagSeqIdList = null;
		Map<Integer, NormalizeAggXmlTagsMapEntity> xmlTagSeqToColumnMap = null;
		Map<String, String> columnToFieldMap = findAllColumns(XorConstant.AGG_PRTY_PYMT_DTLS);
		Map<String, List<RuleMapPOJO>> columnsWithRules = new HashMap<String, List<RuleMapPOJO>>();
		// List<String> profileNames = null;
		try {

			// profileNames = aggregatedPaymentRepository.findDistinctProfileName();

			// if (null != profileNames && profileNames.size() >= 1) {
			piuidSeqId = piuidRepo.findByProfileName(profName).getId();
			tagSeqIdList = xpRuleMapRepository.findDistinctTagSeqid();

			xmlTagSeqToColumnMap = normalizeAggXmlTagsMapRepository
					.findByTagSeqIdInAndPiuidSeqId(tagSeqIdList, piuidSeqId).stream()
					.collect(Collectors.toMap(NormalizeAggXmlTagsMapEntity::getTagSeqId, Function.identity()));

			for (Map.Entry<Integer, NormalizeAggXmlTagsMapEntity> set : xmlTagSeqToColumnMap.entrySet()) {
				String colName = set.getValue().getAggColName().toUpperCase();
				String fieldName = columnToFieldMap.get(colName);
				List<RuleMapPOJO> columnRulesLst = getRulesForColumns(set.getKey(), fieldName);
				if (!columnRulesLst.isEmpty())
					columnsWithRules.put(fieldName, columnRulesLst);
			}

		} catch (Exception e) {
			logger.info("findCoulumnWithRules() : Exception while getting columns rules data ");
			e.printStackTrace();

		}

		return columnsWithRules;
	}

	public Map<String, Map<String, List<RuleMapPOJO>>> findCoulumnWithRules(List<String> profNames,
			Set<String> columnsAgg, Set<String> columnsRem) {

		// Map<Integer,String> piuidSeqId = null;
		Map<String, Integer> piuidSeqIdMap = null;

		// Map<Integer, NormalizeAggXmlTagsMapEntity> xmlTagSeqToColumnMap = null;
		// Map<String, String> columnToFieldMap =
		// findAllColumns(XorConstant.AGG_PRTY_PYMT_DTLS);
		// Map<PIUID, Map<Col, List<rule>>
		Map<String, Map<String, List<RuleMapPOJO>>> piuidColumnsWithRules = new HashMap<>();
		// List<String> profileNames = null;
		try {

			// profileNames = aggregatedPaymentRepository.findDistinctProfileName();

			// if (null != profileNames && profileNames.size() >= 1) {
			piuidSeqIdMap = piuidRepo.findByProfileNameIn(profNames).stream()
					.collect(Collectors.toMap(PIUIDEntity::getProfileName, PIUIDEntity::getId));
			SeqIdPiuidMap = piuidRepo.findByProfileNameIn(profNames).stream()
					.collect(Collectors.toMap(PIUIDEntity::getId, PIUIDEntity::getProfileName));

			List<String> profiles = new ArrayList<>(profNames);
			profiles.add("All");
			logger.info("profile -" + profiles.toString());
			ruleMapList = xpRuleMapRepository.findByActiveIndAndDataReferenceInIgnoreCase(XorConstant.STATUS_Y,
					profiles);
			logger.info(ruleMapList.size() + " ruleMapList");
			if (null != ruleMapList && !ruleMapList.isEmpty()) {
				Map<String, RuleMapEntity> uniDefMmap = new HashMap<>();
				List<RuleMapEntity> allData = ruleMapList.stream()
						.filter(er -> er.getDataReference().equalsIgnoreCase(XorConstant.ALL))
						.collect(Collectors.toList());
				List<RuleMapEntity> refData = ruleMapList.stream()
						.filter(er -> !er.getDataReference().equalsIgnoreCase(XorConstant.ALL))
						.collect(Collectors.toList());

				allData.stream().forEach(re -> {
					logger.info("All " + re.getDataReference() + " " + re.getTagSeqid() + " " + re.getRuleDefId());

				});
				refData.stream().forEach(re -> {
					logger.info(
							"ref - " + re.getDataReference() + " " + re.getTagSeqid() + " " + re.getRuleDefId());

				});

				ruleMapList.clear();
				refData.stream().forEach(ef -> {
					List<RuleMapEntity> arf = allData.stream().map(at -> {
						at.setDataReference(ef.getDataReference());
						return at;
					}).collect(Collectors.toList());
					arf.add(ef);
					ruleMapList.addAll(arf);

				});

				ruleMapList.stream().forEach(e -> {

					String tagSI = e.getDataReference() + "" + e.getTagSeqid() + "" + e.getRuleDefId();

					if (uniDefMmap.containsKey(tagSI)) {
						RuleMapEntity rulMp = uniDefMmap.get(tagSI);// 100
						if (rulMp.getRuleMapPriority() < e.getRuleMapPriority()) {// 100 < 102 true
							uniDefMmap.put(tagSI, e);
						}
					} else {
						uniDefMmap.put(tagSI, e);// 100
					}
				});

				ruleMapList = new ArrayList<>(uniDefMmap.values());

				Set<Integer> tagSeqSet = ruleMapList.stream().map(e -> e.getTagSeqid()).collect(Collectors.toSet());
				List<Integer> piuidSeqIdList = new ArrayList<>(piuidSeqIdMap.values());
				// Map<Integer, String> tagSeqMap = null;
				logger.info("tagSeqSet " + tagSeqSet);
				logger.info("piuidSeqIdList - " + piuidSeqIdList.toString());
				List<NormalizeAggXmlTagsMapEntity> tagMapList = normalizeAggXmlTagsMapRepository
						.findByActiveIndAndTagSeqIdInAndPiuidSeqIdIn(XorConstant.STATUS_Y, new ArrayList<>(tagSeqSet),
								piuidSeqIdList);

				logger.info("tagMapList  " + tagMapList.size());

				if (null != tagMapList && !tagMapList.isEmpty()) {
					// get distinct column name
					columnsAgg.addAll(tagMapList.stream()
							.filter(e -> e.getTagInfoType().equalsIgnoreCase(XorConstant.TAG_INFO_AGG)
									|| e.getTagInfoType().equalsIgnoreCase(XorConstant.BOTH_INFO))
							.map(e -> e.getAggColName().toLowerCase()).collect(Collectors.toSet()));
					logger.info("Column " + columnsAgg.toString());
					// get distinct column name
					columnsRem.addAll(tagMapList.stream()
							.filter(e -> e.getTagInfoType().equalsIgnoreCase(XorConstant.TAG_INFO_REM)
									|| e.getTagInfoType().equalsIgnoreCase(XorConstant.BOTH_INFO))
							.map(e -> e.getAggColName().toLowerCase()).collect(Collectors.toSet()));

					logger.info("Column " + columnsRem.toString());
					// Map<piuid, tagMap>
					// Map<Integer, List<NormalizeAggXmlTagsMapEntity>> piuidSeqIsTagMapEntiyMap =
					// tagMapList.stream()
					// .collect(Collectors.toMap(NormalizeAggXmlTagsMapEntity::getPiuidSeqId,
					// Function.identity()));
					Map<Integer, List<NormalizeAggXmlTagsMapEntity>> piuidSeqIsTagMapEntiyMap = tagMapList.stream()
							.collect(Collectors.groupingBy(NormalizeAggXmlTagsMapEntity::getPiuidSeqId));

					Map<Integer, String> ruleNameMap = xpRuleDefRepository.findByActiveInd(XorConstant.STATUS_Y)
							.stream().collect(Collectors.toMap(RuleDefEntity::getId, RuleDefEntity::getRuleName));

					List<Integer> ruleMapIdList = ruleMapList.stream().map(e -> e.getId()).collect(Collectors.toList());

					// Map<Integer, RuleParamEntity> paramMap =
					// xpRuleParamRepository.findByRuleMapIdIn(ruleMapIdList)
					// .stream().collect(Collectors.toMap(RuleParamEntity::getRuleMapId,
					// Function.identity()));

					Map<Integer, List<RuleParamEntity>> paramMap = xpRuleParamRepository
							.findByActiveIndAndRuleMapIdIn(XorConstant.STATUS_Y, ruleMapIdList).stream()
							.collect(Collectors.groupingBy(RuleParamEntity::getRuleMapId,
									Collectors.mapping(Function.identity(), Collectors.toList())));

					Comparator<RuleMapPOJO> rulePojoComparator = Comparator.comparingInt(RuleMapPOJO::getPriority);

					piuidSeqIsTagMapEntiyMap.entrySet().stream().forEach(e -> {
						String piuid = SeqIdPiuidMap.get(e.getKey());
						List<NormalizeAggXmlTagsMapEntity> tagMapEntityList = e.getValue();

						tagMapEntityList.stream().forEach(tagMap -> {
							if (piuidColumnsWithRules.containsKey(piuid)) {
								Map<String, List<RuleMapPOJO>> columnsWithRules = piuidColumnsWithRules.get(piuid);
								columnsWithRules.put(tagMap.getAggColName().toLowerCase(),
										getRuleList(tagMap.getAggColName(), tagMap.getTagSeqId(), ruleNameMap, paramMap,
												rulePojoComparator, tagMapEntityList, piuid));

								piuidColumnsWithRules.put(piuid, columnsWithRules);

							} else {
								Map<String, List<RuleMapPOJO>> columnsWithRules = new HashMap<>();
								columnsWithRules.put(tagMap.getAggColName().toLowerCase(),
										getRuleList(tagMap.getAggColName(), tagMap.getTagSeqId(), ruleNameMap, paramMap,
												rulePojoComparator, tagMapEntityList, piuid));

								piuidColumnsWithRules.put(piuid, columnsWithRules);
							}
						});

					});
				}
			} else {
				logger.info("No Piuid Information is mapped.");
			}

		} catch (Exception e) {
			logger.info("findCoulumnWithRules() : Exception while getting columns rules data ");
			e.printStackTrace();

		}

		return piuidColumnsWithRules;
	}

	private List<RuleMapPOJO> getRuleList(String colName, Integer tagSeqId, Map<Integer, String> ruleNameMap,
			Map<Integer, List<RuleParamEntity>> paramMap, Comparator<RuleMapPOJO> rulePojoComparator,
			List<NormalizeAggXmlTagsMapEntity> tagMapEntityList, String piuid) {
		List<RuleMapEntity> ruleEntityList = ruleMapList.stream()
				.filter(rm -> piuid.equalsIgnoreCase(rm.getDataReference())
						|| XorConstant.ALL.equalsIgnoreCase(rm.getDataReference()))
				.collect(Collectors.toList());
		List<RuleMapPOJO> rulePojoList = ruleEntityList.stream()
				.filter(e -> e.getTagSeqid().equals(tagSeqId) || e.getTagSeqid() == tagSeqId).map(e -> {
					RuleMapPOJO pojo = new RuleMapPOJO();
					pojo.setRuleMapSeqId(e.getId());
					pojo.setRuleDefId(e.getRuleDefId());
					pojo.setPriority(e.getRuleMapPriority());
					pojo.setRuleName(ruleNameMap.get(e.getRuleDefId()));
					if (null != paramMap.get(e.getId()) && !paramMap.get(e.getId()).isEmpty()) {
						pojo.setRuleParam(paramMap.get(e.getId()).get(0));
						pojo.setRuleParamList(paramMap.get(e.getId()));
					}

					if (null != pojo.getRuleName() && pojo.getRuleName().equalsIgnoreCase(RuleConstant.OPTIONAL)) {
						RuleParamEntity ruleParam = pojo.getRuleParam();
						if (null != ruleParam && null != ruleParam.getRuleApplyLevel()
								&& (ruleParam.getRuleApplyLevel().equalsIgnoreCase(RuleConstant.CONCAT)
										|| ruleParam.getRuleApplyLevel().equalsIgnoreCase(RuleConstant.COALESCE)
										|| ruleParam.getRuleApplyLevel().equalsIgnoreCase(RuleConstant.CONDSUPRESS)
										|| ruleParam.getRuleApplyLevel().equalsIgnoreCase(RuleConstant.CONDLOOKUP)
										|| ruleParam.getRuleApplyLevel().equalsIgnoreCase(RuleConstant.CONDADDRESS))) {
							if (null != ruleParam.getMappingValues()) {
								List<String> tagSeqL = new ArrayList<>(
										Arrays.asList(ruleParam.getMappingValues().split(",")));
								List<RuleMapPOJO> rulePojoListOption = ruleEntityList.stream().filter(
										p -> tagSeqL.contains(p.getTagSeqid().toString()) && p.getRuleDefId() != 100011)
										.map(pe -> {
											RuleMapPOJO pojoopt = new RuleMapPOJO();
											pojoopt.setRuleMapSeqId(pe.getId());
											pojoopt.setRuleDefId(pe.getRuleDefId());
											pojoopt.setPriority(pe.getRuleMapPriority());
											List<RuleParamEntity> ruleParma = paramMap.get(pe.getId());
											if (null != ruleParma && !ruleParma.isEmpty()) {
												pojoopt.setRuleParam(ruleParma.get(0));
												pojoopt.setRuleParamList(ruleParma);
												pojoopt.setRuleName(ruleNameMap.get(ruleParma.get(0).getRuleDefId()));
											}
											pojoopt.setTagSeqid(pe.getTagSeqid());

											tagMapEntityList.stream()
													.filter(tpe -> tpe.getTagSeqId() == pe.getTagSeqid()
															|| tpe.getTagSeqId().equals(pe.getTagSeqid()))
													.forEach(tpee -> {

														pojoopt.setFieldName(tpee.getAggColName().toLowerCase());
													});

											logger.info("pojo " + pojoopt.toString());
											return pojoopt;
										}).collect(Collectors.toList());
								try {
									Collections.sort(rulePojoListOption, rulePojoComparator);
								} catch (Exception ep) {
									logger.info("Rule :: No Optional priority defined for " + colName);
								}
								logger.info("Option lsit -");
								rulePojoListOption.stream().forEach(ep -> {
									logger.info(ep.getFieldName() + " " + ep.getRuleName() + " " + ep.getPriority()
											+ " " + ep.getRuleParam());
								});
								pojo.setOptionalRuleMapList(rulePojoListOption);
							}
						}
					}
					pojo.setTagSeqid(tagSeqId);
					pojo.setFieldName(colName.toLowerCase());
					logger.info(piuid + " --> pojo " + pojo.toString());
					return pojo;

				}).collect(Collectors.toList());
		try {
			Collections.sort(rulePojoList, rulePojoComparator);
		} catch (Exception e) {
			logger.info("Rule :: No priority defined for " + colName);
		}
		return rulePojoList;
	}

}
