package com.xoriant.xorpay.service;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.entity.XMLPIUIDStructureEntity;
import com.xoriant.xorpay.entity.XMLPIUIDXSDEntity;
import com.xoriant.xorpay.entity.XMLStructureMasterEntity;
import com.xoriant.xorpay.excepions.XorpayException;
import com.xoriant.xorpay.pojo.NewTagPOJO;
import com.xoriant.xorpay.pojo.XMLPOJO;
import com.xoriant.xorpay.pojo.XMLStructurePOJO;
import com.xoriant.xorpay.pojo.XMLTagPOJO;
import com.xoriant.xorpay.pojo001.Document;
import com.xoriant.xorpay.repo.XMLPIUIDStructureRepo;
import com.xoriant.xorpay.repo.XMLPIUIDXSDRepo;
import com.xoriant.xorpay.repo.XMLStructureMasterRepo;

@Service
public class XMLStructureService {

	private final Logger logger = LoggerFactory.getLogger(XMLStructureService.class);
	@Autowired
	private XMLPIUIDStructureRepo xmlpiuidStructureRepo;

	@Autowired
	private XMLStructureMasterRepo xmlStructureMasterRepo;

	@Autowired
	private XMLDocumentService xmlDocumentService;

	@Autowired
	private XMLPIUIDXSDRepo xmlXsdRepo;

	Map<Integer, XMLPIUIDStructureEntity> parentIdMap = null;

	public List<XMLStructurePOJO> getStructureForPIUID(Integer piuid) {

		List<XMLStructureMasterEntity> masterList = xmlStructureMasterRepo
				.findByActiveIndOrderByTagSeq(XorConstant.STATUS_Y);
		parentIdMap = new HashMap<>();
		Set<Integer> tagINotPresent = null;
		List<XMLStructureMasterEntity> parentTaglist = null;
		do {
			if (null != parentIdMap) {

				Set<Integer> tagSet = masterList.stream().filter(e -> null != e).map(e -> e.getId())
						.collect(Collectors.toSet());
				tagINotPresent = masterList.stream().filter(e -> null != e && null != e.getTagParentId()
				// && e.getTagParentId() > 2
						&& !tagSet.contains(e.getTagParentId())).map(e -> {
							return e.getTagParentId();
						}).collect(Collectors.toSet());

				parentTaglist = xmlStructureMasterRepo.findByIdIn(new ArrayList<>(tagINotPresent)).stream()
						.map(masterEntity -> {
							return masterEntity;
						}).collect(Collectors.toList());
			}
			if (null != parentTaglist) {
				masterList.addAll(parentTaglist);
			}
		} while (null != tagINotPresent && tagINotPresent.size() > 0);

		Set<Integer> parentIdSet = masterList.stream().map(e -> e.getTagParentId()).collect(Collectors.toSet());

		List<XMLPIUIDStructureEntity> exisitngTagList = xmlpiuidStructureRepo.findByPiuidSeqIdOrderByTagId(piuid);

		Map<Integer, XMLPIUIDStructureEntity> existingTagMap = exisitngTagList.stream()
				.collect(Collectors.toMap(XMLPIUIDStructureEntity::getTagId, Function.identity()));

		return masterList.stream().map(e -> {

			boolean hasChild = false;
			boolean isActive = false;

			/*
			 * if (e.getId() < 3) { if (null != e.getTagParentId() && e.getTagParentId() ==
			 * 0) { e.setTagParentId(null); } isActive = true; hasChild = true; } else if
			 * (e.getId() > 2 && existingTagMap.containsKey(e.getId())) { isActive = true; }
			 * else {
			 *
			 * }
			 */

			if ((existingTagMap.containsKey(e.getId())
					&& existingTagMap.get(e.getId()).getActiveInd().equals(XorConstant.STATUS_Y))) {
				isActive = true;
			} else {
				isActive = false;
			}
			if (parentIdSet.contains(e.getId())) {
				hasChild = true;
			} else {

			}
			//
			return new XMLStructurePOJO(e.getId(), e.getTagParentId(), e.getTagName(), e.getTagSeq(), isActive,
					hasChild, true);
		}).sorted(Comparator.comparingInt(XMLStructurePOJO::getId)).collect(Collectors.toList());

	}

	/*
	 * public void saveXMLPIUIDStructure(XMLPOJO xmlPOJO, boolean activatepiuid)
	 * throws XorpayException { Integer piuid = xmlPOJO.getPiuid();
	 * 
	 * Map<Integer, XMLPIUIDStructureEntity> parentIdMap = new HashMap<>();
	 * List<XMLPIUIDStructureEntity> addTagList = getRequiredTagList(xmlPOJO, piuid,
	 * false, parentIdMap);
	 * 
	 * xmlpiuidStructureRepo.saveAll(addTagList); boolean getRequiredTag = true;
	 * 
	 * saveXmlXsd(xmlPOJO, piuid, activatepiuid, getRequiredTag, addTagList);
	 * 
	 * List<Integer> tagSeqId = addTagList.stream().map(e ->
	 * e.getTagId()).collect(Collectors.toList());
	 * 
	 * mappingService.saveMappingForActive(tagSeqId, piuid);
	 * 
	 * }
	 */

	public void copyXmlStructure(Integer sourcePiuid, Integer destPiuid, boolean activatepiuid) throws XorpayException {

		List<XMLPIUIDStructureEntity> addTagList = xmlpiuidStructureRepo.findByPiuidSeqId(sourcePiuid).stream()
				.map(e -> {
					e.setId(0);
					e.setCreationDate(LocalDateTime.now());
					e.setLastUpdateDate(LocalDateTime.now());
					e.setPiuidSeqId(destPiuid);
					return e;
				}).collect(Collectors.toList());

		xmlpiuidStructureRepo.saveAll(addTagList);

		XMLPOJO xmlPOJO = new XMLPOJO();
		xmlPOJO.setPiuid(sourcePiuid);
		boolean getRequiredTag = false;
		saveXmlXsd(xmlPOJO, destPiuid, activatepiuid, getRequiredTag, addTagList);
	}

	/*
	 * public void saveXMLPIUIDStructureForPIUID(PiuidStatePojo piuidst) throws
	 * XorpayException { Integer piuid = piuidst.getId();
	 * 
	 * PIUIDEntity piuidEntity = piuidRepo.findById(piuid).get(); if
	 * (piuidst.isActiveInd()) { XMLPOJO xmlPOJO = new XMLPOJO();
	 * xmlPOJO.setPiuid(piuid); List<XMLStructurePOJO> ls = new ArrayList<>(1);
	 * xmlPOJO.setXmlStructurePOJO(ls); boolean activatepiuid = true; parentIdMap =
	 * new HashMap<>(); // Map<Integer, XMLPIUIDStructureEntity> parentIdMap = new
	 * HashMap<>(); List<XMLPIUIDStructureEntity> addTagList =
	 * getRequiredTagList(xmlPOJO, piuid, activatepiuid, parentIdMap);
	 * 
	 * 
	 * getTagListWithParent(addTagList, parentIdMap, piuid);
	 * xmlpiuidStructureRepo.deleteByPiuidSeqId(piuid);
	 * 
	 * xmlpiuidStructureRepo.saveAll(addTagList); boolean getRequiredTag = false;
	 * saveXmlXsd(xmlPOJO, piuid, activatepiuid, getRequiredTag, addTagList);
	 * 
	 * mappingService.saveDefautMapping(piuid);
	 * piuidEntity.setActiveInd(XorConstant.STATUS_Y);
	 * 
	 * } else {
	 * 
	 * piuidEntity.setActiveInd(XorConstant.STATUS_N); }
	 * piuidRepo.save(piuidEntity); }
	 */

	private List<XMLPIUIDStructureEntity> getRequiredTagList(XMLPOJO xmlPOJO, Integer piuid, boolean activatepiuid,
			Map<Integer, XMLPIUIDStructureEntity> tagIdMap) {
		List<XMLStructureMasterEntity> masterList = xmlStructureMasterRepo
				.findByActiveIndOrderById(XorConstant.STATUS_Y);

		Map<Integer, XMLPIUIDStructureEntity> existingTagMap = xmlpiuidStructureRepo.findByPiuidSeqIdOrderByTagId(piuid)
				.stream().collect(Collectors.toMap(XMLPIUIDStructureEntity::getTagId, Function.identity()));

		Map<Integer, XMLStructurePOJO> tagMap = xmlPOJO.getXmlStructurePOJO().stream()
				.collect(Collectors.toMap(XMLStructurePOJO::getId, Function.identity()));

		List<XMLPIUIDStructureEntity> get = masterList.stream().map(masterEntity -> {
			int id = masterEntity.getId();
			XMLPIUIDStructureEntity entity = null;
			if (activatepiuid) {
				if (existingTagMap.containsKey(id)) {
					entity = existingTagMap.get(id);
					entity.setActiveInd(XorConstant.STATUS_Y);
					addToParentIdMap(tagIdMap, id, entity);
					return entity;
				} else {
					entity = getXmlPIUIDStructureEntity(piuid, masterEntity, id);
					entity.setActiveInd(XorConstant.STATUS_Y);
					addToParentIdMap(tagIdMap, id, entity);
					return entity;
				}
			} else {
				if (existingTagMap.containsKey(id)) {
					if (tagMap.containsKey(id)) {
						entity = existingTagMap.get(id);
						entity.setActiveInd(XorConstant.STATUS_Y);
						addToParentIdMap(tagIdMap, id, entity);
						return entity;
					} else {
						entity = existingTagMap.get(id);
						entity.setActiveInd(XorConstant.STATUS_N);
						addToParentIdMap(tagIdMap, id, entity);
						return entity;
					}
				} else {

					if (tagMap.containsKey(id)) {
						entity = getXmlPIUIDStructureEntity(piuid, masterEntity, id);
						entity.setActiveInd(XorConstant.STATUS_Y);
						addToParentIdMap(tagIdMap, id, entity);
						return entity;
					} else {
						entity = getXmlPIUIDStructureEntity(piuid, masterEntity, id);
						entity.setActiveInd(XorConstant.STATUS_N);
						addToParentIdMap(tagIdMap, id, entity);
						return entity;
					}
				}
			}
		}).collect(Collectors.toList());

		return get;
	}

	private void addToParentIdMap(Map<Integer, XMLPIUIDStructureEntity> parentIdMap, int parentId,
			XMLPIUIDStructureEntity entity) {
		if (parentId != 0) {
			parentIdMap.put(parentId, entity);
		}
	}

	private XMLPIUIDStructureEntity getXmlPIUIDStructureEntity(Integer piuid, XMLStructureMasterEntity masterEntity,
			int id) {
		XMLPIUIDStructureEntity entity = new XMLPIUIDStructureEntity();
		BeanUtils.copyProperties(masterEntity, entity);
		entity.setId(null);
		entity.setPiuidSeqId(piuid);
		entity.setTagId(id);
		entity.setActiveInd(XorConstant.STATUS_N);
		entity.setCreationDate(LocalDateTime.now());
		entity.setLastUpdateDate(LocalDateTime.now());
		return entity;
	}

	private void saveXmlXsd(XMLPOJO xmlPOJO, Integer piuid, boolean activatepiuid, boolean getRequiredTag,
			List<XMLPIUIDStructureEntity> addTagList) throws XorpayException {

		String xsd = generateXSDForPIUID(xmlPOJO, activatepiuid, getRequiredTag, addTagList);

		XMLPIUIDXSDEntity entity = xmlXsdRepo.findByPiuidSeqId(piuid);
		if (null != entity) {
			entity.setXmlXsd(xsd);
			entity.setLastUpdateDate(LocalDateTime.now());
		} else {
			entity = new XMLPIUIDXSDEntity(0, piuid, xsd, XorConstant.STATUS_Y, XorConstant.ADMIN_ROLE,
					LocalDateTime.now(), LocalDateTime.now(), XorConstant.ADMIN_ROLE);

		}

		xmlXsdRepo.save(entity);
	}

	public String generateXSDForPIUID(XMLPOJO xmlPOJO, boolean activatepiuid, boolean getRequiredTag,
			List<XMLPIUIDStructureEntity> addTagList) throws XorpayException {
		String xmlString = null;
		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			Integer piuid = xmlPOJO.getPiuid();

			parentIdMap = null;
			if (getRequiredTag) {
				parentIdMap = new HashMap<>();
				addTagList = getRequiredTagList(xmlPOJO, piuid, activatepiuid, parentIdMap).stream()
						.filter(e -> e.getActiveInd().equals(XorConstant.STATUS_Y)).collect(Collectors.toList());
				getTagListWithParent(addTagList, parentIdMap, piuid);
			} else {
			}

			Document d = xmlDocumentService.getXMLXSDFor(addTagList);
			StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(d, sw);
			xmlString = sw.toString();
		} catch (JAXBException e1) {
			e1.printStackTrace();
			throw new XorpayException(e1.getMessage());
		}
		return xmlString;
	}

	private void getTagListWithParent(List<XMLPIUIDStructureEntity> addTagList,
			Map<Integer, XMLPIUIDStructureEntity> parentIdMap, Integer piuid) {
		List<XMLPIUIDStructureEntity> parentTaglist = null;
		Set<Integer> tagINotPresent = null;
		do {
			if (null != parentIdMap) {

				Set<Integer> tagSet = addTagList.stream().filter(e -> null != e).map(e -> e.getTagId())
						.collect(Collectors.toSet());

				tagINotPresent = addTagList.stream()
						.filter(e -> null != e && null != e.getTagParentId() && e.getTagParentId() > 2
								&& !tagSet.contains(e.getTagParentId()))
						.filter(e -> null == parentIdMap.get(e.getTagParentId())).map(e -> {
							return e.getTagParentId();

						}).collect(Collectors.toSet());
				parentTaglist = xmlStructureMasterRepo.findByIdIn(new ArrayList<>(tagINotPresent)).stream()
						.map(masterEntity -> {
							logger.info("adding " + masterEntity.getId());
							return getXmlPIUIDStructureEntity(piuid, masterEntity, masterEntity.getId());
						}).collect(Collectors.toList());

			}
			if (null != parentTaglist) {
				addTagList.addAll(parentTaglist);
			}
		} while (null != tagINotPresent && tagINotPresent.size() > 0);
	}

	public String getXMLXsd(Integer piuid) {

		String res = "No Data Found";
		XMLPIUIDXSDEntity entity = xmlXsdRepo.findByPiuidSeqId(piuid);

		if (null != entity) {
			res = entity.getXmlXsd();
		}
		return res;
	}

	public List<XMLTagPOJO> getParrentTag() {
		return xmlStructureMasterRepo.findAll().stream()
				.map(e -> new XMLTagPOJO(e.getId(), e.getTagName(), e.getXpath())).collect(Collectors.toList());
	}

	public String addNewTag(NewTagPOJO newTag) {

		XMLStructureMasterEntity exists = xmlStructureMasterRepo.findByTagNameAndTagParentId(newTag.getTagName(),
				newTag.getPid());
		if (null == exists) {
			XMLStructureMasterEntity entity = new XMLStructureMasterEntity();
			entity.setId(0);
			entity.setTagName(newTag.getTagName());
			entity.setTagParentId(newTag.getPid());
			entity.setActiveInd(XorConstant.STATUS_N);
			entity.setCreationDate(LocalDateTime.now());
			entity.setLastUpdateDate(LocalDateTime.now());
			entity.setTagSeq(newTag.getTagSeq());

			xmlStructureMasterRepo.save(entity);
			return XorConstant.SUCCESS;
		}
		return XorConstant.FAILED;
	}

	public List<XMLTagPOJO> getInactiveTag() {
		return xmlStructureMasterRepo.findByActiveInd(XorConstant.STATUS_N).stream()
				.map(e -> new XMLTagPOJO(e.getId(), e.getTagName(), e.getXpath(), null, null))
				.collect(Collectors.toList());
	}

	public String enbaleTag(Integer id) {
		XMLStructureMasterEntity entity = xmlStructureMasterRepo.findById(id).get();
		if (null != entity) {
			entity.setActiveInd(XorConstant.STATUS_Y);
			entity.setLastUpdateDate(LocalDateTime.now());
			xmlStructureMasterRepo.save(entity);
			return XorConstant.SUCCESS;
		}
		/*
		 * normalizeAggXmlTagsMasterRepo.save(new NormalizeAggXmlTagsMasterEntity(0,
		 * sourceSystemService.getSourceSystem().getSrcSystemSort(), nrmlzColName,
		 * nrmlzColDataType, entity.getId(), aggColName,aggColDataType,
		 * entity.getTagName(), entity.getXpath() ));
		 * 
		 * @Column(name = "TAG_SEQID") private Integer tagSeqId;
		 * 
		 * @Column(name = "AGG_COL_NAME") private String aggColName;
		 * 
		 * @Column(name = "AGG_COL_DATA_TYPE") private Integer aggColDataType;
		 * 
		 * @Column(name = "TAG_NAME") private String tagName;
		 * 
		 * @Column(name = "XPATH") private String xpath;
		 * 
		 * @Column(name = "TAG_INFO_TYPE") private String tagInfoType;
		 * 
		 * @Column(name = "CREATION_DATE") private LocalDateTime creationDate;
		 * 
		 * @Column(name = "CREATED_BY") private String createdBy;
		 * 
		 * @Column(name = "LAST_UPDATE_DATE") private LocalDateTime updateDate;
		 * 
		 * @Column(name = "LAST_UPDATED_BY") private String updatedBy;
		 * 
		 * @Column(name = "ACTIVE_IND") private Character activeInd;
		 * 
		 * @Column(name = "EFFECTIVE_START_DATE") private LocalDateTime startDate;
		 * 
		 * @Column(name = "EFFECTIVE_END_DATE") private LocalDateTime endDate;
		 * 
		 * dynamic col check creation stg creation of column in norm creation of column
		 * in agg or rem
		 * 
		 * 
		 * }
		 */
		return XorConstant.FAILED;
	}

	public List<XMLTagPOJO> getDistinctParentIdInactiveTag() {

		return xmlStructureMasterRepo.findDistinctByTagParentId().stream().map(e -> {
			String collInfoType = e.getTagInfoType();
			switch (collInfoType) {
			case XorConstant.BOTH_INFO:
				collInfoType = XorConstant.BOTH;
				break;
			case XorConstant.TAG_INFO_AGG:
				collInfoType = XorConstant.AGGREGATE;
				break;
			case XorConstant.TAG_INFO_REM:
				collInfoType = XorConstant.REMITTANCE;
				break;
			}

			return new XMLTagPOJO(e.getId(), e.getTagName(), e.getXpath(), e.getTagInfoType(), collInfoType);
		}).collect(Collectors.toList());
	}

	public List<XMLTagPOJO> getChildTags(Integer tagParentId) {

		return xmlStructureMasterRepo.findByTagParentIdAndActiveInd(tagParentId, XorConstant.STATUS_N).stream()
				.map(e -> new XMLTagPOJO(e.getId(), e.getTagName(), e.getXpath(), e.getTagInfoType(), null))
				.collect(Collectors.toList());
	}
}
