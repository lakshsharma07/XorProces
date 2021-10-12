package com.xoriant.xorpay.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.PAIN001Constant;
import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.data.sync.repo.AuditJPARepo;
import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.entity.ConfigEntity;
import com.xoriant.xorpay.entity.PIUIDEntity;
import com.xoriant.xorpay.entity.PaymentAksDetailEntity;
import com.xoriant.xorpay.entity.PaymentBatchAksDetailEntity;
import com.xoriant.xorpay.entity.PaymentGeneratedEntity;
import com.xoriant.xorpay.entity.XMLPIUIDXSDEntity;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.pojo.DataProcessAuditPOJO;
import com.xoriant.xorpay.pojo001.CreditTransferTransactionInformation10;
import com.xoriant.xorpay.pojo001.Document;
import com.xoriant.xorpay.pojo001.PaymentInstructionInformation3;
import com.xoriant.xorpay.repo.AggregatedPaymentRepo;
import com.xoriant.xorpay.repo.PIUIDRepo;
import com.xoriant.xorpay.repo.PaymentAckDetailsRepo;
import com.xoriant.xorpay.repo.PaymentBatchAckDetailsRepo;
import com.xoriant.xorpay.repo.XMLPIUIDXSDRepo;
import com.xoriant.xorpay.repo.XMLPaymentDetailsRepo;

@Service
public class PaymentXMLFromStructureService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentXMLFromStructureService.class);
	@Autowired
	private XMLPaymentDetailsRepo xmlPaymentDetailsRepo;
	@Autowired
	private PaymentBatchAckDetailsRepo paymentBatchAckDetailsRepo;
	@Autowired
	private PaymentAckDetailsRepo paymentAckDetailsRepo;
	@Autowired
	private AggregatedPaymentRepo aggrePaymentRepo;
	@Autowired
	private DashboardDetailsService dashboardService;
	@Autowired
	private XorConfigService xorConfigService;
	@Autowired
	private XMLPIUIDXSDRepo xmlpiuidxsdRepo;
	@Autowired
	private PIUIDRepo piuidRepo;
	@Autowired
	private DocumentService docService;
	@Autowired
	private CLSService clsService;

	private String msgEndToEnd = null;

	public List<String> getOUDetailsList() {
		return xmlPaymentDetailsRepo.getDistinctOU();
	}

	public Map<String, List<String>> getDistinctMessageId() {
		Map<String, List<String>> hmMap = new HashMap<>();

		List<String> ls = new ArrayList<>();
		ls.addAll(aggrePaymentRepo.findDistinctMessageId());
		// ls.addAll(xmlTagRepository.findDistinctEndToEndId());

		hmMap.put("EndToEndId", ls);
		return hmMap;
	}

	public boolean generatePaymentXML(String processId, String profilename, String messageId, Integer sourceSystem,
			List<Map<Integer, String>> aggDataMapList,
			Map<String, Map<String, List<Map<Integer, String>>>> rmtIfoForMsgId,
			Map<String, Set<Integer>> supressTagSet, Set<String> addressTagSet, ConfigPojo configPojo) {
		long start = System.currentTimeMillis();
		boolean isXmlGenerated = false;
		// String profileName = tagsList.get(0).getProfileName();
		String profileName = aggDataMapList.get(0).get(PAIN001Constant.PROFILENAME_914);
		PIUIDEntity piuidEntity = piuidRepo.findByProfileName(profileName);
		XMLPIUIDXSDEntity xmlXsdEntity = xmlpiuidxsdRepo.findByPiuidSeqId(piuidEntity.getId());
		String inputfile = xmlXsdEntity.getXmlXsd();

		isXmlGenerated = receiveMultiple(processId, inputfile, profilename, messageId, sourceSystem, aggDataMapList,
				rmtIfoForMsgId, supressTagSet, addressTagSet, configPojo);
		long end = System.currentTimeMillis();
		long elapsedTime = end - start;
		logger.info("execution time with caching..." + elapsedTime);
		return isXmlGenerated;
	}

	public boolean receiveMultiple(String processId, String paymentFile, String profilename, String messageId,
			Integer sourceSystem, List<Map<Integer, String>> aggDataMapList,
			Map<String, Map<String, List<Map<Integer, String>>>> rmtIfoForMsgId,
			Map<String, Set<Integer>> supressTagSet, Set<String> addressTagSet, ConfigPojo configPojo) {

		logger.info("piuidNumber " + profilename);

		String outputFilePath = configPojo.getPAN_OUT() + "PayloadXML_" + profilename + "_" + messageId + ".xml";

		boolean isValidPayload = true;
		try {
			String pf = paymentFile.trim().replaceFirst("^([\\W]+)<", "<");
			String xmlString = null;
			if (XorConstant.isCLS) {
				xmlString = clsService.getMX(pf, aggDataMapList, outputFilePath, rmtIfoForMsgId);
			} else {
				// File file = new File(ConfigConstants.SG421_INPUT_FILE);
				Reader inputString = new StringReader(paymentFile);
				// BufferedReader br = new BufferedReader(new FileReader(file));
				BufferedReader br = new BufferedReader(inputString);
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();
				while (line != null) {
					sb.append(line).append("\n");
					line = br.readLine();
				}
				String fileAsString = sb.toString();
				br.close();

				JAXBContext jaxbContext;

				jaxbContext = JAXBContext.newInstance(Document.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

				StringReader reader = new StringReader(fileAsString);
				Document document = (Document) jaxbUnmarshaller.unmarshal(reader);

				Map<String, List<Map<Integer, String>>> tagSeqMapOnEndToEndId = new HashMap<>();
				msgEndToEnd = null;
				aggDataMapList.forEach(tagSeqData -> {
					String endtoendid = tagSeqData.get(PAIN001Constant.ENDTOENDID_33);
					msgEndToEnd = messageId + "~" + endtoendid;
					if (tagSeqMapOnEndToEndId.containsKey(endtoendid)) {
						List<Map<Integer, String>> tagSeqL = tagSeqMapOnEndToEndId.get(endtoendid);
						tagSeqL.add(tagSeqData);
						tagSeqMapOnEndToEndId.put(endtoendid, tagSeqL);
					} else {
						List<Map<Integer, String>> tagSeqL = new ArrayList<>(1);
						tagSeqL.add(tagSeqData);
						tagSeqMapOnEndToEndId.put(endtoendid, tagSeqL);
					}
				});

				logger.info("msgEndToEnd  - " + msgEndToEnd);
				Set<Integer> suprrestagIdSet = supressTagSet.get(profilename + msgEndToEnd);
				logger.info("suprrestagIdSet pmt -- " + suprrestagIdSet);

				if (null != document.getCstmrCdtTrfInitn().getGrpHdr()) {
					document.getCstmrCdtTrfInitn().getGrpHdr().setNbOfTxs("" + aggDataMapList.size());
					Map<Integer, String> tagSeqData = new HashMap<>();
					tagSeqData.putAll(aggDataMapList.get(0));
					if (null != suprrestagIdSet) {
						logger.info(tagSeqData.toString());
						suppressTagSeqID(tagSeqData, suprrestagIdSet);
						logger.info(tagSeqData.toString());
					}
					document.getCstmrCdtTrfInitn().setGrpHdr(docService.setGrpHdr(document, tagSeqData, configPojo));
				}

				if (null != document.getCstmrCdtTrfInitn().getPmtInf()) {
					document.getCstmrCdtTrfInitn().setPmtInf(setPmtInf(document, rmtIfoForMsgId, tagSeqMapOnEndToEndId,
							supressTagSet, addressTagSet, profilename));
				}
				// Writes XML file to file-system
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				jaxbMarshaller.marshal(document, new File(outputFilePath));
				try {
					StringWriter sw = new StringWriter();
					jaxbMarshaller.marshal(document, sw);
					xmlString = sw.toString();
				} catch (JAXBException e1) {
					e1.printStackTrace();
				}
			}
			try {
				PaymentGeneratedEntity payGenEntity = new PaymentGeneratedEntity(0, messageId, outputFilePath, null,
						profilename, xmlString, XorConstant.SYSTEM, LocalDateTime.now(), XorConstant.SYSTEM,
						LocalDateTime.now());
				dashboardService.updateDashboardStatics(null, null, XorConstant.PAYMENTS, null, payGenEntity,
						processId);
			} catch (Exception e) {
				logger.info("issue in saving generated log");
				e.printStackTrace();
			}

			// String finalXmlPayload = incomingXmlPayload.toString();
			// finalXmlPayload = finalXmlPayload.trim().replaceFirst("^([\\W]+)<",
			// "<");

			/*
			 * if (logger.isDebugEnabled()) { logger.debug("finalXmlPayload" +
			 * finalXmlPayload); }
			 */
			// isValidPayload = validateXMLSchema(XSD_OUT_FILE, paymentFile);
			/*
			 * if(logger.isDebugEnabled()) {
			 * logger.debug("finalXmlPayload sent to Integrator Gateway API"+
			 * finalXmlPayload); }
			 */

			/*
			 * if (logger.isDebugEnabled()) { logger.
			 * debug("finalXmlPayload validation completed \"pain.001.001.03.xml validates against pain.001.001.03.xsd?"
			 * + validateXMLSchema(XSD_OUT_FILE, finalXmlPayload)); }
			 */

			/*
			 * if(isValidPayload) { int xmlStatusCount =
			 * updateProcessedStatus(paymentNumber); if (logger.isDebugEnabled()) {
			 * logger.debug("Xml Status is updated successfully "+xmlStatusCount); }
			 *
			 * }
			 */
			// List<AggregatedPaymentEntity> xmlTagEntityList = tagsList.stream()
			// .map(tag -> updateProcessedStatus(tag.getId())).collect(Collectors.toList());
			List<AggregatedPaymentEntity> xmlTagEntityList = aggDataMapList.stream()
					.map(tagMap -> updateProcessedStatus(Integer.parseInt(tagMap.get(PAIN001Constant.SLNO_912))))
					.collect(Collectors.toList());

			dashboardService.updateDashboardStatics(sourceSystem, aggDataMapList.size(), XorConstant.TRANSLATED, null,
					null, null);

			DataProcessAuditPOJO dp = new DataProcessAuditPOJO(null, messageId, null, AuditJPARepo.PROCESSED,
					"XML Generated at " + outputFilePath, LocalDateTime.now().toString());
			dashboardService.updateDashboardStatics(null, null, XorConstant.AUDIT, dp, null, processId);

			aggrePaymentRepo.saveAll(xmlTagEntityList);
			if (logger.isDebugEnabled()) {
				logger.debug("File is created and Status is updated successfully ");
			}

		} catch (IOException ioe) {
			DataProcessAuditPOJO dp = new DataProcessAuditPOJO(null, messageId, null, AuditJPARepo.FAILED,
					"XML Generation failed Exception " + ioe.getMessage(), LocalDateTime.now().toString());
			dashboardService.updateDashboardStatics(null, null, XorConstant.AUDIT, dp, null, processId);
			logger.error("GatewayServiceImpl IOException while generating xml" + ioe.toString(), ioe);
		} catch (JAXBException e) {
			DataProcessAuditPOJO dp = new DataProcessAuditPOJO(null, messageId, null, AuditJPARepo.FAILED,
					"XML Generation failed Exception " + e.getMessage(), LocalDateTime.now().toString());
			dashboardService.updateDashboardStatics(null, null, XorConstant.AUDIT, dp, null, processId);
			logger.error("GatewayServiceImpl JAXBException while generating xml" + e.toString(), e);
		}
		return isValidPayload;
	}

	private List<PaymentInstructionInformation3> setPmtInf(
			// Map<String, List<AggRemittanceInfoEntity>> rmtEteMap,
			Document document,
			// Map<String, List<AggregatedPaymentEntity>> xmlTagMapL,
			Map<String, Map<String, List<Map<Integer, String>>>> rmtIfoForMsgId,
			Map<String, List<Map<Integer, String>>> tagSeqMapOnEndToEndId, Map<String, Set<Integer>> supressTagSet,
			Set<String> addressTagSet, String profilename) {

		// logger.info("rmtIfoForMsgId " + rmtIfoForMsgId.keySet().toString());

		List<PaymentInstructionInformation3> PmtInfList = new ArrayList<>();

		tagSeqMapOnEndToEndId.entrySet().stream().forEach(tagSeqlistObj -> {
			List<Map<Integer, String>> tagSeqlist = tagSeqlistObj.getValue();
			String endtoEndId = tagSeqlistObj.getKey();// end to end id

			logger.info("endtoEndInvoice " + endtoEndId);
			if (null != rmtIfoForMsgId && null != rmtIfoForMsgId.get(endtoEndId)) {

				String msgId = tagSeqlist.get(0).get(PAIN001Constant.MSGID_4);
				String msgEndToEnd = msgId + "~" + endtoEndId;
				logger.info("msgEndToEnd  - " + msgEndToEnd);
				Set<Integer> suprrestagIdSet = supressTagSet.get(profilename + msgEndToEnd);
				boolean isMultiAddressLine = addressTagSet.contains(profilename + msgEndToEnd);
				logger.info("suprrestagIdSet pmt -- " + suprrestagIdSet);
				logger.info(tagSeqlist.get(0).toString());
				suppressTagSeqID(tagSeqlist.get(0), suprrestagIdSet);
				logger.info(tagSeqlist.get(0).toString());
				PaymentInstructionInformation3 PmtInf = docService.setPmtInf(document, tagSeqlist.get(0),
						isMultiAddressLine);

				List<CreditTransferTransactionInformation10> CdtTrfTxInfList = tagSeqlist.stream().map(tags -> {
					logger.info(tags.toString());
					logger.info("suprrestagIdSet cdtrf -- " + suprrestagIdSet);
					logger.info(tags.toString());
					suppressTagSeqID(tags, suprrestagIdSet);
					return docService.setCdtTrfTxInf(document, PmtInf, tags, rmtIfoForMsgId.get(endtoEndId),
							suprrestagIdSet, isMultiAddressLine);
				}).collect(Collectors.toList());

				PmtInf.setCdtTrfTxInf(CdtTrfTxInfList);

				PmtInfList.add(PmtInf);
			} else {
				logger.info("No Rmt info");
			}

		});
		return PmtInfList;
	}

	private void suppressTagSeqID(Map<Integer, String> tagSeq, Set<Integer> suprrestagIdSet) {
		if (null != suprrestagIdSet) {
			suprrestagIdSet.stream().forEach(stag -> {
				logger.info("stag suppressed " + stag + " " + tagSeq.remove(stag));
			});
		}
	}

	private AggregatedPaymentEntity updateProcessedStatus(Integer id) {
		AggregatedPaymentEntity xmlTag = null;
		try {
			xmlTag = aggrePaymentRepo.findById(id).get();
			xmlTag.setXmlStatus(XorConstant.STATUS_Y);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlTag;
	}

	/*
	 * public Derivepiuid derivePIUID(String paymentNumber) {
	 *
	 * Derivepiuid deriveDetails = xmlBuilderDao.getSvcDlvDetail(paymentNumber);
	 * logger.info("GatewayServiceImpl deriveDetails:: " + deriveDetails); return
	 * deriveDetails; }
	 */

	public ConfigEntity getEncryptionConfig(String configName, String configStatus) {
		return xorConfigService.findByConfigNameAndConfigStatus(configName, configStatus);
	}

	public void savePaymentBatchDetails(PaymentBatchAksDetailEntity paymentBatchAksDetail) {
		paymentBatchAckDetailsRepo.save(paymentBatchAksDetail);
	}

	public void savePaymentAckDetails(PaymentAksDetailEntity paymentAksDetail) {
		paymentAckDetailsRepo.save(paymentAksDetail);
	}

	public void updateStatusForAggPaymentAsError_(List<AggregatedPaymentEntity> tagsList) {
		List<AggregatedPaymentEntity> tage = tagsList.stream().map(e -> {
			e.setXmlStatus(XorConstant.STATUS_E);
			return e;
		}).collect(Collectors.toList());
		aggrePaymentRepo.saveAll(tage);
	}

	public void updateStatusForAggPaymentAsError(List<Map<Integer, String>> aggDataMapList) {

		List<Integer> tage = aggDataMapList.stream().map(emap -> Integer.parseInt(emap.get(PAIN001Constant.SLNO_912)))
				.collect(Collectors.toList());
		List<AggregatedPaymentEntity> tagl = aggrePaymentRepo.findByIdIn(tage).stream().map(e -> {
			e.setXmlStatus(XorConstant.STATUS_E);
			return e;
		}).collect(Collectors.toList());

		aggrePaymentRepo.saveAll(tagl);
	}

}
