package com.xoriant.xorpay.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.PAIN001Constant;
import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.entity.PaymentAksDetailEntity;
import com.xoriant.xorpay.entity.PaymentBatchAksDetailEntity;
import com.xoriant.xorpay.iso.pain002.Document;
import com.xoriant.xorpay.iso.pain002.OriginalPaymentInformation1;
import com.xoriant.xorpay.parser.entity.PaymentAckDetails;
import com.xoriant.xorpay.parser.service.LoadDataService;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.repo.AggregatedPaymentRepo;
import com.xoriant.xorpay.repo.PaymentAckDetailsRepo;
import com.xoriant.xorpay.repo.PaymentBatchAckDetailsRepo;

@Service
public class CLSService {

	private final Logger logger = LoggerFactory.getLogger(CLSService.class);

	@Autowired
	LoadDataService loadDataService;

	@Autowired
	private DashboardDetailsService dashboardService;

	@Autowired
	private AggregatedPaymentRepo aggregatedPaymentRepository;

	@Autowired
	private PaymentBatchAckDetailsRepo paymentBatchAckDetailsRepo;

	@Autowired
	private PaymentXMLService paymentXMLService;

	@Autowired
	private PaymentAckDetailsRepo paymentAckDetailsRepo;

	private String paymentString;

	public String getMX(String data, List<Map<Integer, String>> aggDataMapList, String outputFilePath,
			Map<String, Map<String, List<Map<Integer, String>>>> rmtIfoForMsgId) throws IOException {

		aggDataMapList.forEach(tagSeqData -> {
			String endtoendid = tagSeqData.get(PAIN001Constant.ENDTOENDID_33);
			System.out.println(endtoendid + "tagSeqData-- " + tagSeqData);
			Map<Integer, String> tagSeqMap = tagSeqData;
			paymentString = data;

			tagSeqMap.entrySet().stream().forEach(d -> {

				String val = d.getValue();
				if (d.getKey() == 5) {
					LocalDateTime ld = LocalDateTime.parse(d.getValue(), XorConstant.formateYYYYMMDD_HHMMSSZ);
					val = ld.format(XorConstant.formateYYYYMMDDTHHMMSS) + "Z";
				}

				System.out.println("tag " + "tag_" + d.getKey() + "   " + val);

				paymentString = paymentString.replaceAll(">tag_" + d.getKey() + "<", ">" + val + "<");

			});
			System.out.println("rmtIfoForMsgId " + rmtIfoForMsgId);

			rmtIfoForMsgId.get(endtoendid).entrySet().stream().forEach(r -> {
				r.getValue().stream().forEach(rim -> {
					rim.entrySet().stream().forEach(ri -> {
						String val = ri.getValue();

						if (ri.getKey() == 60) {
							LocalDateTime ld = LocalDateTime.parse(ri.getValue(), XorConstant.formateYYYYMMDD_HHMMSSZ);
							val = ld.toLocalDate().toString();
						}

						paymentString = paymentString.replaceAll(">tag_" + ri.getKey() + "<", ">" + val + "<");
						System.out.println("tag " + "tag_" + ri.getKey() + "   " + val);
					});
				});

			});

		});

		Path path = Paths.get(outputFilePath);
		byte[] dbytes = paymentString.getBytes();

		Files.write(path, dbytes);

		return paymentString;
	}

	public void createMockFile(String content, File fileResponseOut, AggregatedPaymentEntity e) throws IOException {
		content = content.replaceAll(">tag_569<", ">" + e.getBicOrBei() + "<");
		content = content.replaceAll(">tag_108<", ">" + e.getCdtrAgntBic() + "<");
		content = content.replaceAll(">tag_4<", ">" + e.getMsgId() + "<");
		content = content.replaceAll(">tag_5<",
				">" + e.getCretionDt().format(XorConstant.formateYYYYMMDDTHHMMSS) + "Z" + "<");
		content = content.replaceAll(">tag_10<", ">" + e.getPmtInfId() + "<");
		content = content.replaceAll(">tag_33<", ">" + e.getEndToEndId() + "<");
		content = content.replaceAll(">uuid<", ">" + UUID.randomUUID().toString() + "<");

		Path path = // Paths.get(fileResponseOut.);
				fileResponseOut.toPath();
		byte[] dbytes = content.getBytes();

		Files.write(path, dbytes);
	}

	public boolean readPack002B(String file, ConfigPojo configPojo) throws Exception {
		PaymentAckDetails ackDetails = loadDataService.readXML(file);
		String outputFilePath = configPojo.getPAN_OUT() + "PayloadXML_" + ackDetails.getOrgnlMsgId() + "_mt019.txt";
		loadDataService.createMT019(ackDetails, outputFilePath);
		return readPain002BatchAksFile(ackDetails, file, configPojo.getSOURCE_SYS_ID());
	}

	public boolean readPack002(String file, Integer sourceSystem) throws Exception {
		PaymentAckDetails ackDetails = loadDataService.readXML(file);
		return readPain002AksFile(ackDetails, file, sourceSystem);
	}

	private boolean readPain002BatchAksFile(PaymentAckDetails document, String fileName, Integer sourceSystem)
			throws Exception {
		logger.info("ReadXmlFileScheduler readPain002BatchAksFile() method start ===========");
		String staticType;

		String messsageId = document.getOrgnlMsgId();
		logger.info(XorConstant.STATUS_Y + "  " + sourceSystem + " " + messsageId);
		List<AggregatedPaymentEntity> xmlTagEntityListAll = aggregatedPaymentRepository
				.findByXmlStatusAndSourceSystemAndMsgId(XorConstant.STATUS_Y, sourceSystem, messsageId);

		logger.info("xmlTagEntityListAll " + xmlTagEntityListAll.size());

		if (null != xmlTagEntityListAll && !xmlTagEntityListAll.isEmpty()) {
			AggregatedPaymentEntity entity = xmlTagEntityListAll.get(0);
			String internalUuid = entity.getInernalUuid();
			int noOfPayment = entity.getNbOfTxs();

			// # new fail, status(ACTC Accepted, RJCT Rejected)

			List<PaymentBatchAksDetailEntity> batchEntityList = paymentBatchAckDetailsRepo
					.findByPaymentInstructionId(messsageId);
			long count = batchEntityList.stream().filter(e -> !e.getAckStatus().equalsIgnoreCase(XorConstant.NEW))
					.count();
			if (count == 0) {
				PaymentBatchAksDetailEntity paymentBatchAksDetail = new PaymentBatchAksDetailEntity();
				paymentBatchAksDetail.setInternalUuId(internalUuid);
				if (document.getOrgnlMsgId() != null) {

					paymentBatchAksDetail.setPaymentBatchAckID(messsageId + "_"
							+ LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

					paymentBatchAksDetail.setResponseAckID(document.getOrgnlMsgId());

				}
				if (document.getOrgnlMsgId() != null) {
					paymentBatchAksDetail.setPaymentInstructionId(document.getOrgnlMsgId());
				}
				if (document.getTxSts() != null) {
					paymentBatchAksDetail.setAckStatus(document.getTxSts());
				}
				paymentBatchAksDetail.setFileName(fileName);
				// Date date = new Date();
				paymentBatchAksDetail.setCreatedDate(LocalDateTime.now());
				paymentBatchAksDetail.setLastUpdateDate(LocalDateTime.now());
				if (paymentBatchAksDetail.getCreatedBy() == null)
					paymentBatchAksDetail.setCreatedBy("1000");
				if (paymentBatchAksDetail.getLastUpdatedBy() == null)
					paymentBatchAksDetail.setLastUpdatedBy("1000");

				// if
				// (document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getStsRsnInf().get(0).getAddtlInf()
				// != null) {
				String addInfo = "ACK - FILE ACCEPTED";// document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getStsRsnInf().get(0)
				// .getAddtlInf().get(0);
				paymentBatchAksDetail.setAdditionalInfo(addInfo);
				if ("ACK - FILE ACCEPTED".contains(addInfo)) {
					paymentBatchAksDetail.setFileStatus("ACKNOWLEDGED");
					staticType = XorConstant.ACKNOWLEDGED;
				} else {
					paymentBatchAksDetail.setFileStatus("REJECTED");
					staticType = XorConstant.REJECTED;
				}
				// } else {
				// paymentBatchAksDetail.setFileStatus("REJECTED");
				// staticType = XorConstant.REJECTED;
				// }
				try {
					// String insertpaymentBatchAksDetail = insertPaymentBatchAksDetailQuery();
					paymentXMLService.savePaymentBatchDetails(paymentBatchAksDetail);
					List<AggregatedPaymentEntity> entityL = aggregatedPaymentRepository.findByMsgId(messsageId);
					if (null != entityL) {
						List<AggregatedPaymentEntity> entityBp = entityL.stream().map(e -> {
							e.setXmlStatus(XorConstant.STATUS_BATCH_PROCESSED);
							return e;
						}).collect(Collectors.toList());

						if (!entityBp.isEmpty()) {
							aggregatedPaymentRepository.saveAll(entityBp);
						}
					}

					// int row = saveRecord(insertpaymentBatchAksDetail, paymentBatchAksDetail);
					logger.debug(" row inserted.");
					// if file is rejected the no of acknoledge will increased
					if (staticType.equalsIgnoreCase(XorConstant.REJECTED)) {
						staticType = XorConstant.ACKNOWLEDGED;
					}
					logger.info("paymentBatchAksDetail -> " + paymentBatchAksDetail.getPaymentInstructionId() + " "
							+ paymentBatchAksDetail.getPaymentBatchAckID());
					dashboardService.updateDashboardStatics(sourceSystem, noOfPayment, staticType, null, null, null);
					// paymentBatchAksDetailRepository.save(paymentBatchAksDetail);

					return true;
				} catch (Exception e) {
					logger.info("Exception while saving details");
					e.printStackTrace();
					return false;
				}
			} else {
				logger.info("Batch " + messsageId + " already have ststus");
				return true;
			}
		} else {
			logger.info("No Response to process " + messsageId);
			return false;
		}
	}

	private boolean readPain002AksFile(PaymentAckDetails document, String fileName, Integer sourceSystem)
			throws Exception {
		logger.info("ReadXmlFileScheduler readPain002AksFile() schedule start ===========");

		try {
			String messsageId = document.getOrgnlMsgId();

			logger.info("XorConstant.STATUS_BATCH_PROCESSED, sourceSystem, messsageId "
					+ XorConstant.STATUS_BATCH_PROCESSED + " " + sourceSystem + " " + messsageId);
			List<AggregatedPaymentEntity> xmlTagEntityListAll = aggregatedPaymentRepository
					.findByXmlStatusAndSourceSystemAndMsgId(XorConstant.STATUS_BATCH_PROCESSED, sourceSystem,
							messsageId);

			if (null != xmlTagEntityListAll && !xmlTagEntityListAll.isEmpty()) {
				Map<String, AggregatedPaymentEntity> endtoendMap = xmlTagEntityListAll.stream()
						.collect(Collectors.toMap(AggregatedPaymentEntity::getEndToEndId, Function.identity()));

				String endToEndId = document.getOrgnlEndToEndId();
				AggregatedPaymentEntity entity = endtoendMap.get(endToEndId);
				if (null != entity) {
					try {
						paymentAck(messsageId, endToEndId, document, fileName, sourceSystem, entity);
					} catch (Exception e) {
						logger.error("Exception Reading Response", e);
						return false;
					}
				}
				return true;
			} else {
				logger.info("No Response to process " + messsageId);
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private void paymentAck(String messsageId, String endToEndId, PaymentAckDetails document, String fileName,
			Integer sourceSystem, AggregatedPaymentEntity entity) throws Exception {

		String internalUuid = entity.getInernalUuid();
		int noOfPayment = 1;

		// # new fail, status(ACTC Accepted, RJCT Rejected)

		List<PaymentAksDetailEntity> batchEntityList = paymentAckDetailsRepo
				.findByPaymentInstructionIdAndPaymentNumber(messsageId, endToEndId);
		long count = batchEntityList.stream().filter(e -> !e.getAckStatus().equalsIgnoreCase(XorConstant.NEW)
				&& !e.getAckStatus().equalsIgnoreCase(XorConstant.PDNG)).count();
		if (count == 0) {

			PaymentAksDetailEntity paymentAksDetail = new PaymentAksDetailEntity();
			paymentAksDetail.setInternalUuid(internalUuid);
			paymentAksDetail.setCreatedBy("1000");
			Date date = new Date();
			paymentAksDetail.setCreatedDate(date);

			if (document.getOrgnlMsgId() != null) {
				paymentAksDetail.setAckId(document.getOrgnlMsgId() + "_" + document.getOrgnlEndToEndId() + "_"
						+ LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
				paymentAksDetail.setResponseAckId(document.getOrgnlMsgId());

			}
			if (document.getOrgnlMsgId() != null) {
				paymentAksDetail.setPaymentInstructionId(document.getOrgnlMsgId());
			}

			if (document.getTxSts() != null) {
				paymentAksDetail.setAckStatus(document.getTxSts());
			}
			if (document.getOrgnlMsgId() != null) {
				paymentAksDetail.setFileName(fileName);

			}
			// if (document.get(0).getOrgnlTxRef().getAmt().getInstdAmt().getValue() !=
			// null) {
			// paymentAksDetail.setPaymentAmount(Double.parseDouble(orgPmtInf.getTxInfAndSts().get(0).getOrgnlTxRef()
			// .getAmt().getInstdAmt().getValue().toString()));
			// }
			// if
			// (orgPmtInf.getTxInfAndSts().get(0).getOrgnlTxRef().getAmt().getInstdAmt().getCcy()
			// != null) {
			// paymentAksDetail.setPaymentCurrencCode(
			/// orgPmtInf.getTxInfAndSts().get(0).getOrgnlTxRef().getAmt().getInstdAmt().getCcy());
			// }
			if (document.getCreDt() != null) {
				Date paymentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
						.parse(document.getCreDt().replace("Z", ""));
				paymentAksDetail.setPaymentDate(paymentDate);
			}
			if (document.getTxSts() != null) {
				paymentAksDetail.setPaymentStatus(document.getTxSts());
			}

			// if (orgPmtInf.getTxInfAndSts().get(0).getStsRsnInf().get(0).getAddtlInf() !=
			// null) {
			// StringBuilder sb = new StringBuilder();
			// for (String addInfo :
			// orgPmtInf.getTxInfAndSts().get(0).getStsRsnInf().get(0).getAddtlInf()) {
			// sb.append(addInfo).append('\n');
			// }
			// paymentAksDetail.setAddInfo(sb.toString());
			// }

			paymentAksDetail.setLastUpdateDate(date);
			if (paymentAksDetail.getCreatedBy() == null)
				paymentAksDetail.setCreatedBy("1000");
			if (paymentAksDetail.getLastUpdatedBy() == null)
				paymentAksDetail.setLastUpdatedBy("1000");

			if (document.getOrgnlEndToEndId() != null) {
				paymentAksDetail.setPaymentNumber(document.getOrgnlEndToEndId());
			}

			if (document.getOrgnlInstrId() != null) {
				paymentAksDetail.setLogicalGrpRef(document.getOrgnlInstrId());
			}

			// String insertpaymentAksDetail = insertPaymentAksDetailQuery();

			// int row = saveRecord(insertpaymentAksDetail, paymentAksDetail);
			try {
				paymentXMLService.savePaymentAckDetails(paymentAksDetail);

				List<AggregatedPaymentEntity> entityL = aggregatedPaymentRepository.findByMsgIdAndEndToEndId(messsageId,
						endToEndId);

				if (null != entityL) {
					List<AggregatedPaymentEntity> entityBp = entityL.stream().map(e -> {
						e.setXmlStatus(XorConstant.STATUS_PAYMENT_PROCESSED);
						return e;
					}).collect(Collectors.toList());

					if (!entityBp.isEmpty()) {
						aggregatedPaymentRepository.saveAll(entityBp);
					}
				}

				logger.debug(" row inserted.");
				String staticType;
				if (paymentAksDetail.getAckStatus().equals(XorConstant.ACCP)
						|| paymentAksDetail.getAckStatus().equals(XorConstant.ACSP)
						|| paymentAksDetail.getAckStatus().equals(XorConstant.ACTC)
						|| paymentAksDetail.getAckStatus().equals(XorConstant.ACSC)) {
					staticType = XorConstant.ACCEPTED;
				} else {
					staticType = XorConstant.REJECTED;
				}
				dashboardService.updateDashboardStatics(sourceSystem, noOfPayment, staticType, null, null, null);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Excepition while saving payment");
			}
		} else {
			logger.info("Batch " + messsageId + " already have ststus ");
		}
	}

}