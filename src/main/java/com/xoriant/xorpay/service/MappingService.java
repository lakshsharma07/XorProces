package com.xoriant.xorpay.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.PAIN001Constant;
import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.entity.NormalizeAggXmlTagsMapEntity;
import com.xoriant.xorpay.entity.NormalizeAggXmlTagsMasterEntity;
import com.xoriant.xorpay.entity.SourceSysEntity;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.pojo.MappedSourceTargetPOJO;
import com.xoriant.xorpay.pojo.SourceTargetPOJO;
import com.xoriant.xorpay.pojo.TagPOJO;
import com.xoriant.xorpay.pojo.TargetTagPOJO;
import com.xoriant.xorpay.repo.NormalizeAggXmlTagsMapRepo;
import com.xoriant.xorpay.repo.NormalizeAggXmlTagsMasterRepo;

@Service
public class MappingService {
	private final Logger logger = LoggerFactory.getLogger(MappingService.class);
	@Autowired
	EntityManager entityManager;
	int total;
	Map<Integer, String> seqTagMap = null;
	Map<Integer, String> rmtSeqTagMap = null;
	@Autowired
	private NormalizeAggXmlTagsMapRepo tagsMapRepo;
	@Autowired
	private NormalizeAggXmlTagsMasterRepo tagsMasterRepo;
	private int i = 0;
	private StringBuilder sb;
	private String msgId = null, endToEndId = null, invoiceNo = null;

	public List<NormalizeAggXmlTagsMapEntity> getAllMappings() {

		return tagsMapRepo.findAll();
	}

	public MappedSourceTargetPOJO getMappedSourceTarget(Integer sourceSystem, Integer piuidSeqId) {

		List<NormalizeAggXmlTagsMapEntity> lst = tagsMapRepo
				.findByActiveIndAndErpSrcSysAndPiuidSeqIdOrderByIdAsc(XorConstant.STATUS_Y, sourceSystem, piuidSeqId);

		List<TargetTagPOJO> sourcetargeList = new ArrayList<>();

		lst.stream().forEach(e -> {
			// if(e.getId()==1)
			if (null != e.getNrmlzColName()) {
				String cols = e.getNrmlzColName().trim().replace(XorConstant.NRMLCOLDBREGEX, XorConstant.COMMA);
				String[] columns = cols.split(XorConstant.COMMA);
				List<TagPOJO> columnLst = new ArrayList<>(1);
				int ic = 0;
				for (String col : columns) {
					columnLst.add(new TagPOJO(ic++, col, XorConstant.SOURCE));
				}
				sourcetargeList.add(new TargetTagPOJO(e.getId(), e.getXpath(), XorConstant.CONTAINER, columnLst));
			}
		});

		return new MappedSourceTargetPOJO(sourcetargeList, sourceSystem, piuidSeqId);
	}

	public SourceTargetPOJO getSourceTarget(Integer sourceSystem) {
		List<NormalizeAggXmlTagsMasterEntity> lst = tagsMasterRepo.findByErpSrcSys(sourceSystem);

		List<TagPOJO> sourceTagList = new ArrayList<>();
		List<TargetTagPOJO> targetTagList = new ArrayList<>();
		lst.stream().forEach(e -> {
			if (null != e.getNrmlzColName() && !e.getNrmlzColName().trim().isEmpty()) {
				sourceTagList.add(new TagPOJO(e.getId(), e.getNrmlzColName(), XorConstant.SOURCE));
			}
			List<TagPOJO> lsst = new ArrayList<>(1);
			if (null != e.getXpath() && !e.getXpath().trim().isEmpty())
				targetTagList.add(new TargetTagPOJO(e.getId(), e.getXpath(), XorConstant.CONTAINER, lsst));
		});

		Map<String, Object> sourceTagetMap = new HashMap<>(2);
		sourceTagetMap.put(XorConstant.SOURCE, sourceTagList);
		sourceTagetMap.put(XorConstant.TARGET, targetTagList);

		return new SourceTargetPOJO(sourceTagetMap);
	}

	/*
	 * public void saveDefautMapping(Integer piuid) { SourceSysEntity srcSys =
	 * sourceSystemService.getSourceSystem(); List<NormalizeAggXmlTagsMasterEntity>
	 * lst = tagsMasterRepo.findByErpSrcSys(srcSys.getId());
	 * tagsMapRepo.deleteByPiuidSeqId(piuid); List<NormalizeAggXmlTagsMapEntity>
	 * mappingTagList = lst.stream().map(e -> { NormalizeAggXmlTagsMapEntity tagMap
	 * = new NormalizeAggXmlTagsMapEntity(); BeanUtils.copyProperties(e, tagMap);
	 * tagMap.setId(0); tagMap.setPiuidSeqId(piuid); return tagMap;
	 * }).collect(Collectors.toList()); tagsMapRepo.saveAll(mappingTagList); }
	 */

	/*
	 * public void saveMappingForActive(List<Integer> tagSeqId, Integer piuidSeqId)
	 * { SourceSysEntity srcSys = sourceSystemService.getSourceSystem();
	 * tagsMapRepo.deleteByPiuidSeqId(srcSys.getId());
	 * 
	 * List<NormalizeAggXmlTagsMasterEntity> masterList =
	 * tagsMasterRepo.findByErpSrcSysAndTagSeqIdIn(srcSys.getId(), tagSeqId);
	 * 
	 * List<NormalizeAggXmlTagsMapEntity> mappingTagList = masterList.stream().map(e
	 * -> { NormalizeAggXmlTagsMapEntity tagMap = new
	 * NormalizeAggXmlTagsMapEntity(); BeanUtils.copyProperties(e, tagMap);
	 * tagMap.setId(0); tagMap.setActiveInd(XorConstant.STATUS_Y);
	 * tagMap.setPiuidSeqId(piuidSeqId); return tagMap;
	 * }).collect(Collectors.toList()); tagsMapRepo.saveAll(mappingTagList);
	 * 
	 * }
	 */

	public String saveSourceTarget(MappedSourceTargetPOJO sourceTarget) {
		try {

			List<NormalizeAggXmlTagsMapEntity> mappingTagList = tagsMapRepo
					.findByErpSrcSysAndPiuidSeqId(sourceTarget.getSourceSystem(), sourceTarget.getPiuidSeqId());

			Map<Integer, NormalizeAggXmlTagsMapEntity> mappingTagMap = mappingTagList.stream()
					.collect(Collectors.toMap(NormalizeAggXmlTagsMapEntity::getId, Function.identity()));

			List<NormalizeAggXmlTagsMapEntity> tagList = new ArrayList<>();
			sourceTarget.getSourcetargeList().stream().forEach(target -> {
				NormalizeAggXmlTagsMapEntity tagEntity = mappingTagMap.get(target.getId());

				List<TagPOJO> sourceTagList = target.getColumns();
				sb = new StringBuilder();
				i = 0;
				sourceTagList.stream().forEach(source -> {
					if (i > 0) {
						sb.append(XorConstant.NRMLCOLDBREGEX);
					}
					sb.append(source.getName());
					i++;
				});

				if (tagEntity.getNrmlzColName().equalsIgnoreCase(sb.toString())) {

					tagEntity.setStartDate(LocalDateTime.now());
					tagEntity.setActiveInd(XorConstant.STATUS_Y);
					tagEntity.setEndDate(null);

					tagList.add(tagEntity);

				} else {

					tagEntity.setEndDate(LocalDateTime.now());
					tagEntity.setActiveInd(XorConstant.STATUS_N);
					tagList.add(tagEntity);

					NormalizeAggXmlTagsMapEntity maptagEntity = new NormalizeAggXmlTagsMapEntity();
					BeanUtils.copyProperties(tagEntity, maptagEntity);
					maptagEntity.setId(0);
					sb = new StringBuilder();
					i = 0;
					sourceTagList.stream().forEach(source -> {
						if (i > 0) {
							sb.append(XorConstant.NRMLCOLDBREGEX);
						}
						sb.append(source.getName());
						i++;
					});
					maptagEntity.setNrmlzColName(sb.toString());

					maptagEntity.setStartDate(LocalDateTime.now());
					maptagEntity.setActiveInd(XorConstant.STATUS_Y);

					tagList.add(maptagEntity);
				}

				mappingTagMap.remove(target.getId());
			});
			mappingTagMap.entrySet().stream().forEach(o -> {

				NormalizeAggXmlTagsMapEntity tagEntity = o.getValue();
				tagEntity.setEndDate(LocalDateTime.now());
				tagEntity.setActiveInd(XorConstant.STATUS_N);

				tagList.add(tagEntity);
			});

			tagsMapRepo.saveAll(tagList);

		} catch (Exception e) {
			e.printStackTrace();
			return XorConstant.ERROR;
		}
		return XorConstant.SUCCESS;
	}

	public Map<String, List<Map<Integer, String>>> getMapiing(String messsgId,
			Map<String, Map<String, Map<String, List<Map<Integer, String>>>>> rmtMap, ConfigPojo configPojo) {
		SourceSysEntity sourceEntity = configPojo.getSrcSys();
		List<NormalizeAggXmlTagsMasterEntity> tagMaster = tagsMasterRepo
				.findByErpSrcSysAndActiveInd(sourceEntity.getId(), XorConstant.STATUS_Y);

		List<NormalizeAggXmlTagsMasterEntity> aggMaster = tagMaster.stream()
				.filter(e -> e.getTagInfoType().equals(XorConstant.TAG_INFO_AGG)
						|| e.getTagInfoType().equals(XorConstant.BOTH_INFO))
				.collect(Collectors.toList());
		List<NormalizeAggXmlTagsMasterEntity> rmtMaster = tagMaster.stream()
				.filter(e -> e.getTagInfoType().equals(XorConstant.TAG_INFO_REM)
						|| e.getTagInfoType().equals(XorConstant.BOTH_INFO))
				.collect(Collectors.toList());

		StringBuilder aggquery = getQueryColumns(aggMaster);
		aggquery.append(" from agg_prty_pymt_dtls where XML_STATUS ='" + XorConstant.STATUS_V + "' and erp_src_sys ="
				+ configPojo.getSOURCE_SYS_ID());
		if (null != messsgId) {
			aggquery.append(" and msgid_4 = '" + messsgId + "'");
		}

		StringBuilder remtquery = getQueryColumns(rmtMaster);
		remtquery.append(" from agg_remittance_info where erp_src_sys =" + configPojo.getSOURCE_SYS_ID());
		if (null != messsgId) {
			remtquery.append(" and msgid_4 = '" + messsgId + "'");
		}

		List<Object[]> rsl = getResult(aggquery);
		logger.info("============ " + rsl.size());
		Map<String, List<Map<Integer, String>>> msgidMap = new HashMap<>();

		if (rsl.size() > 0) {
			total = aggMaster.size();
			rsl.stream().forEach(e -> {
				msgId = null;
				endToEndId = null;
				seqTagMap = new HashMap<>();
				for (int r = 0; r < total; r++) {
					if (null != e[r]) {

						Integer tagSeqid = aggMaster.get(r).getTagSeqId();
						seqTagMap.put(tagSeqid, e[r].toString());
						if (null != tagSeqid && tagSeqid == PAIN001Constant.MSGID_4) {
							msgId = e[r].toString();
						}

					}
				}
				if (null != msgId) {
					addMsgidMap(msgidMap, msgId);
				}

			});

			List<Object[]> rmtsl = getResult(remtquery);
			if (rmtsl.size() > 0) {
				total = rmtMaster.size();

				rmtsl.stream().forEach(e -> {
					msgId = null;
					endToEndId = null;
					invoiceNo = null;
					rmtSeqTagMap = new HashMap<>();
					for (int r = 0; r < total; r++) {
						if (null != e[r]) {

							Integer tagSeqid = rmtMaster.get(r).getTagSeqId();
							rmtSeqTagMap.put(tagSeqid, e[r].toString());
							if (null != tagSeqid && tagSeqid == PAIN001Constant.MSGID_4) {
								msgId = e[r].toString();
							}
							if (null != tagSeqid && tagSeqid == PAIN001Constant.ENDTOENDID_33) {
								endToEndId = e[r].toString();
							}
							if (null != tagSeqid && tagSeqid == PAIN001Constant.NB_59) {
								invoiceNo = e[r].toString();
							}
						}
					}
					// msd end inv rm
					// Map<String, Map<String, Map<String, List<Map<Integer, String>>>>> rmtMap
					if (null != msgId && null != endToEndId && null != invoiceNo) {
						if (rmtMap.containsKey(msgId)) {
							Map<String, Map<String, List<Map<Integer, String>>>> aggrmtETEMap = rmtMap.get(msgId);
							if (aggrmtETEMap.containsKey(endToEndId)) {
								Map<String, List<Map<Integer, String>>> aggrmtInvoiceMap = aggrmtETEMap.get(endToEndId);
								if (aggrmtInvoiceMap.containsKey(invoiceNo)) {
									List<Map<Integer, String>> rmtList = aggrmtInvoiceMap.get(invoiceNo);
									rmtList.add(rmtSeqTagMap);
									aggrmtInvoiceMap.put(invoiceNo, rmtList);
									aggrmtETEMap.put(endToEndId, aggrmtInvoiceMap);
									rmtMap.put(msgId, aggrmtETEMap);
								} else {
									List<Map<Integer, String>> rmtList = new ArrayList<>(1);
									rmtList.add(rmtSeqTagMap);
									aggrmtInvoiceMap.put(invoiceNo, rmtList);
									aggrmtETEMap.put(endToEndId, aggrmtInvoiceMap);
									rmtMap.put(msgId, aggrmtETEMap);
								}
							} else {
								Map<String, List<Map<Integer, String>>> aggrmtInvoiceMap = new HashMap<>(1);
								List<Map<Integer, String>> rmtList = new ArrayList<>(1);

								rmtList.add(rmtSeqTagMap);
								aggrmtInvoiceMap.put(invoiceNo, rmtList);
								aggrmtETEMap.put(endToEndId, aggrmtInvoiceMap);
								rmtMap.put(msgId, aggrmtETEMap);
							}
						} else {
							Map<String, Map<String, List<Map<Integer, String>>>> aggrmtETEMap = new HashMap<>(1);
							Map<String, List<Map<Integer, String>>> aggrmtInvoiceMap = new HashMap<>(1);
							List<Map<Integer, String>> rmtList = new ArrayList<>(1);
							rmtList.add(rmtSeqTagMap);
							aggrmtInvoiceMap.put(invoiceNo, rmtList);

							aggrmtETEMap.put(endToEndId, aggrmtInvoiceMap);
							rmtMap.put(msgId, aggrmtETEMap);
						}
					}

				});
			}
		}

		return msgidMap;
	}

	private StringBuilder getQueryColumns(List<NormalizeAggXmlTagsMasterEntity> tagMaster) {
		StringBuilder aggquery = new StringBuilder("Select ");
		i = 0;
		tagMaster.stream().forEach(e -> {
			if (i > 0) {
				aggquery.append(", ");
			}
			aggquery.append(e.getAggColName());
			e.getTagSeqId();
			i++;
		});
		return aggquery;
	}

	private List<Object[]> getResult(StringBuilder aggquery) {
		Query nativeQuery = entityManager.createNativeQuery(aggquery.toString());
		List<Object[]> rsl = nativeQuery.getResultList();
		return rsl;
	}

	private void addMsgidMap(Map<String, List<Map<Integer, String>>> msgidMap, String msgId) {
		if (msgidMap.containsKey(msgId)) {
			addToMap(msgidMap, msgidMap.get(msgId));
		} else {
			List<Map<Integer, String>> dataList = new ArrayList<>(1);
			addToMap(msgidMap, dataList);
		}
	}

	private void addToMap(Map<String, List<Map<Integer, String>>> msgidMap, List<Map<Integer, String>> dataList) {
		dataList.add(seqTagMap);
		msgidMap.put(msgId, dataList);
	}

}
