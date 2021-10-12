package com.xoriant.xorpay.service;

/**
 * @author XOR FRAMEWORK team
 * Class for scheduler to read PSR xml files and put the data in table
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

//import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.entity.PaymentAksDetailEntity;
import com.xoriant.xorpay.entity.PaymentBatchAksDetailEntity;
import com.xoriant.xorpay.iso.pain002.Document;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.repo.AggregatedPaymentRepo;

@Component
public final class ReadXmlFileScheduler {
	private final Logger logger = LoggerFactory.getLogger(ReadXmlFileScheduler.class);

	@Autowired
	private DashboardDetailsService dashboardService;

	@Autowired
	private PaymentXMLService paymentXMLService;

	@Autowired
	private AggregatedPaymentRepo aggregatedPaymentRepository;

	String PAIN_IN_FILE_PATH = null;
	String PAIN_ARCHIVE_FILE_PATH = null;
	String SHELL_COMMAND = null;
	String SHELL_OUTPUT = null;
	String SHELL_ERROR = null;

	Map<String, String> createConfigMap = null;
	private static List<String> painFiles = new ArrayList<String>();

	/**
	 * method to schedule run to read xml files
	 * 
	 * @param sourceSystem
	 * @param noOfPayment
	 * @param configPojo
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	// @Scheduled(fixedRateString = "${parse.scheduler.job}")
	public void readPain002Xmlfiles_(String iternalUuid, String messsageId, String endToEndId, int noOfPayment,
			ConfigPojo configPojo) throws Exception {
		logger.info("ReadXmlFileScheduler readPain002Xmlfiles() schedule start ===========");

		// if (null == createConfigMap) {
		logger.info("Getting config details...");

		// createConfigMap = paymentMonitorService.getAllConfiguration();

		PAIN_IN_FILE_PATH = configPojo.getPAN_IN();// createConfigMap.get("file.path.pain.xml.out.dir");
		PAIN_ARCHIVE_FILE_PATH = configPojo.getPAN_ARCHIVE();
		// PAIN_ARCHIVE_FILE_PATH =
		// createConfigMap.get("file.path.pain.xml.archive.dir");
		// SHELL_COMMAND = createConfigMap.get("client.hook");
		// SHELL_OUTPUT = createConfigMap.get("client.hook.output");
		// SHELL_ERROR = createConfigMap.get("client.hook.error");
		// }

		File folder = new File(PAIN_IN_FILE_PATH);

		File[] fileNames = folder.listFiles();
		JAXBContext jaxbContext;
		Unmarshaller jaxbUnmarshaller = null;
		try {
			jaxbContext = JAXBContext.newInstance(Document.class);
			jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		} catch (JAXBException ex) {
			logger.info(" JAXBException on file read " + ex.getMessage());
		} catch (Exception ex) {
			logger.info(" Exception on file read " + ex.getMessage());
		}
		// logger.info("file ---" + fileNames.toString());

		if (fileNames != null) {
			for (File file : fileNames) {
				try {
					String files = file.getAbsoluteFile().toString();
					String fileName = file.getName();
					this.logger.info("********fileName***********" + fileName);
					if (file.isDirectory()) {
					} else if (files.endsWith(".xml")) {

						BufferedReader br = new BufferedReader(new FileReader(files));
						StringBuilder sb = new StringBuilder();
						String line = br.readLine();
						while (line != null) {
							sb.append(line).append("\n");
							line = br.readLine();
						}
						String fileAsString = sb.toString();
						br.close();

						StringReader reader = new StringReader(fileAsString);
						Document document = (Document) jaxbUnmarshaller.unmarshal(reader);
						boolean isRead = false;
						if (document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getGrpSts() != null) {
							readPain002BatchAksFile(iternalUuid, messsageId, document, fileName,
									configPojo.getSOURCE_SYS_ID(), noOfPayment);
						} else {
							readPain002AksFile(iternalUuid, messsageId, endToEndId, document, fileName,
									configPojo.getSOURCE_SYS_ID(), 1);
						}
						String archiveFileName = fileMove(files, fileName, PAIN_ARCHIVE_FILE_PATH);
						logger.info("archiveFileName - " + archiveFileName);

						if (!archiveFileName.equalsIgnoreCase("Error in deleting file")) {
							// calling shell script
							logger.info("archiveFileName!!! Success " + archiveFileName);
							if (!archiveFileName.isEmpty() && (null != SHELL_COMMAND) && (null != SHELL_OUTPUT)
									&& (null != SHELL_ERROR))
								executeShellScript(archiveFileName.trim(), SHELL_COMMAND.trim(), SHELL_OUTPUT.trim(),
										SHELL_ERROR.trim());
							logger.info("path to dellete -> " + folder.toPath());
							Files.delete(folder.toPath());
						} else {
							logger.info("archiveFileName::" + archiveFileName);
						}
					}
				} catch (JAXBException ex) {
					logger.error(" JAXBException on file read " + ex.getMessage());
					continue;
				} catch (Exception ex) {
					logger.error(" Exception in file read " + ex.getMessage());
					continue;
				} catch (Throwable ex) {
					logger.error(" Exception in file read " + ex.getMessage());
					continue;
				}
			}
		}
		logger.info("ReadXmlFileScheduler readPain002Xmlfiles() schedule end ===========");

	}

	private void executeShellScript(String archiveFileName, String SHELL_COMMAND, String SHELL_OUTPUT,
			String SHELL_ERROR) {
		try {
			logger.info(" executeShellScript method started...");
			ProcessBuilder pBuilder = new ProcessBuilder();

			pBuilder.command("pwd");
			Process pathProcess = pBuilder.start();

			BufferedReader reader = new BufferedReader(new InputStreamReader(pathProcess.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			String result = builder.toString();
			String absoluteFilePathName = result.concat("/").concat(archiveFileName);
			String modifiedArchiveFileName = "-f ".concat(absoluteFilePathName);
			logger.info("modified archive file name : " + modifiedArchiveFileName);
			ArrayList<String> stringCommandList = new ArrayList<String>();
			stringCommandList.add("nohup");

			String[] splitStr = SHELL_COMMAND.split("\\s+");
			for (int i = 0; i < splitStr.length; i++) {
				stringCommandList.add(splitStr[i]);
			}
			stringCommandList.add(modifiedArchiveFileName);

			String command[] = stringCommandList.toArray(new String[stringCommandList.size()]);

			StringBuilder sb = new StringBuilder();
			logger.info("Executing shell command: ");
			for (int i = 0; i < command.length; i++) {
				sb.append(command[i]);
				sb.append(" ");
			}
			logger.info(sb.toString());

			pBuilder.command(command);
			pBuilder.redirectOutput(Redirect.appendTo(new File(SHELL_OUTPUT)));
			pBuilder.redirectError(Redirect.appendTo(new File(SHELL_ERROR)));
			pBuilder.start();

			logger.info(" executeShellScript method ended...");
		} catch (IOException e) {
			logger.error(" executeShellScript Exception" + e.toString(), e);
		}
	}

	/**
	 * method to move file from output folder to archive folder
	 * 
	 * @throws Exception
	 */
	private String fileMove(String file, String fileName, String PAIN_ARCHIVE_FILE_PATH) throws Exception {

		logger.info("fileMove() started");
		try {

			File afile = new File(file);
			File bfile = new File(PAIN_ARCHIVE_FILE_PATH + fileName);

			InputStream inStream = new FileInputStream(afile);
			OutputStream outStream = new FileOutputStream(bfile);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = inStream.read(buffer)) > 0) {
				outStream.write(buffer, 0, length);
			}

			inStream.close();
			outStream.close();

			// delete the original file
			afile.delete();
			logger.info("File renamed and moved successfully");
			return PAIN_ARCHIVE_FILE_PATH + fileName;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Error in deleting file";
	}

	/**
	 * @author CitiConnect ERP team Class for scheduler to read payment PSR xml
	 *         files
	 * @param messsageId
	 * @param endToEndId
	 * @param noOfPayment
	 * @param sourceSystem
	 */
	private void readPain002AksFile(String internalUuid, String messsageId, String endToEndId, Document document,
			String fileName, Integer sourceSystem, int noOfPayment) throws Exception {
		logger.info("ReadXmlFileScheduler readPain002AksFile() schedule start ===========");

		try {
			PaymentAksDetailEntity paymentAksDetail = new PaymentAksDetailEntity();
			paymentAksDetail.setInternalUuid(internalUuid);
			paymentAksDetail.setCreatedBy("1000");
			Date date = new Date();
			paymentAksDetail.setCreatedDate(date);
			if (document.getCstmrPmtStsRpt().getGrpHdr().getMsgId() != null) {
				paymentAksDetail.setAckId(document.getCstmrPmtStsRpt().getGrpHdr().getMsgId() + "_"
						+ document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0)
								.getOrgnlEndToEndId()
						+ "_" + LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			}
			if (document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getOrgnlMsgId() != null) {
				paymentAksDetail
						.setPaymentInstructionId(document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getOrgnlMsgId());
			}

			if (document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getNbOfTxsPerSts().get(0).getDtldSts()
					.value() != null) {
				paymentAksDetail.setAckStatus(document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getNbOfTxsPerSts()
						.get(0).getDtldSts().value());
			}
			if (document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getOrgnlMsgNmId() != null) {
				paymentAksDetail.setFileName(fileName);

			}
			if (document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0).getOrgnlTxRef()
					.getAmt().getInstdAmt().getValue() != null) {
				paymentAksDetail.setPaymentAmount(Double.parseDouble(document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts()
						.get(0).getTxInfAndSts().get(0).getOrgnlTxRef().getAmt().getInstdAmt().getValue().toString()));
			}
			if (document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0).getOrgnlTxRef()
					.getAmt().getInstdAmt().getCcy() != null) {
				paymentAksDetail.setPaymentCurrencCode(document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0)
						.getTxInfAndSts().get(0).getOrgnlTxRef().getAmt().getInstdAmt().getCcy());
			}
			if (document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0).getOrgnlTxRef()
					.getReqdExctnDt() != null) {
				Date paymentDate = new SimpleDateFormat("yyyy-MM-dd")
						.parse(document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0)
								.getOrgnlTxRef().getReqdExctnDt().toString());
				paymentAksDetail.setPaymentDate(paymentDate);
			}
			if (document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0).getTxSts() != null) {
				paymentAksDetail.setPaymentStatus(document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0)
						.getTxInfAndSts().get(0).getTxSts().name());
			}

			if (document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0).getStsRsnInf().get(0)
					.getAddtlInf() != null) {
				StringBuilder sb = new StringBuilder();
				for (String addInfo : document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0)
						.getStsRsnInf().get(0).getAddtlInf()) {
					sb.append(addInfo).append('\n');
				}
				paymentAksDetail.setAddInfo(sb.toString());
			}

			paymentAksDetail.setLastUpdateDate(date);
			if (paymentAksDetail.getCreatedBy() == null)
				paymentAksDetail.setCreatedBy("1000");
			if (paymentAksDetail.getLastUpdatedBy() == null)
				paymentAksDetail.setLastUpdatedBy("1000");

			if (document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0)
					.getOrgnlEndToEndId() != null) {
				paymentAksDetail.setPaymentNumber(document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0)
						.getTxInfAndSts().get(0).getOrgnlEndToEndId());
			}

			if (document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getOrgnlPmtInfId() != null) {
				paymentAksDetail.setLogicalGrpRef(
						document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getOrgnlPmtInfId());
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
				if (paymentAksDetail.getAckStatus().equals("ACCP") || paymentAksDetail.getAckStatus().equals("ACSP")
						|| paymentAksDetail.getAckStatus().equals("ACTC")) {
					staticType = XorConstant.ACCEPTED;
				} else {
					staticType = XorConstant.REJECTED;
				}
				dashboardService.updateDashboardStatics(sourceSystem, noOfPayment, staticType, null, null, null);
				painFiles.add(fileName);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Excepition while saving payment");
			}
			// logger.debug("Files successfully processed::" + painFiles);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("ReadXmlFileScheduler readPain002AksFile() schedule end ===========");

	}

	/*
	 * method to read file payment
	 * 
	 * @param messsageId
	 * 
	 * @param sourceSystem
	 * 
	 * @param noOfPayment
	 * 
	 * @throws Exception
	 */
	private void readPain002BatchAksFile(String internalUuid, String messsageId, Document document, String fileName,
			Integer sourceSystem, int noOfPayment) throws Exception {
		logger.info("ReadXmlFileScheduler readPain002BatchAksFile() method start ===========");
		String staticType;
		PaymentBatchAksDetailEntity paymentBatchAksDetail = new PaymentBatchAksDetailEntity();
		paymentBatchAksDetail.setInternalUuId(internalUuid);
		if (document.getCstmrPmtStsRpt().getGrpHdr().getMsgId() != null) {
			String batchId = document.getCstmrPmtStsRpt().getGrpHdr().getMsgId();

			paymentBatchAksDetail.setPaymentBatchAckID(
					batchId + "_" + LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		}
		if (document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getOrgnlMsgId() != null) {
			paymentBatchAksDetail
					.setPaymentInstructionId(document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getOrgnlMsgId());
		}
		if (document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getGrpSts().value() != null) {
			paymentBatchAksDetail.setAckStatus(document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getGrpSts().value());
		}
		paymentBatchAksDetail.setFileName(fileName);
		// Date date = new Date();
		paymentBatchAksDetail.setCreatedDate(LocalDateTime.now());
		paymentBatchAksDetail.setLastUpdateDate(LocalDateTime.now());
		if (paymentBatchAksDetail.getCreatedBy() == null)
			paymentBatchAksDetail.setCreatedBy("1000");
		if (paymentBatchAksDetail.getLastUpdatedBy() == null)
			paymentBatchAksDetail.setLastUpdatedBy("1000");

		if (document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getStsRsnInf().get(0).getAddtlInf() != null) {
			String addInfo = document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts().getStsRsnInf().get(0).getAddtlInf()
					.get(0);
			paymentBatchAksDetail.setAdditionalInfo(addInfo);
			if ("ACK - FILE ACCEPTED".contains(addInfo)) {
				paymentBatchAksDetail.setFileStatus("ACKNOWLEDGED");
				staticType = XorConstant.ACKNOWLEDGED;
			} else {
				paymentBatchAksDetail.setFileStatus("REJECTED");
				staticType = XorConstant.REJECTED;
			}
		} else {
			paymentBatchAksDetail.setFileStatus("REJECTED");
			staticType = XorConstant.REJECTED;
		}
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

			painFiles.add(fileName);
			logger.debug("Files successfully procced::" + painFiles);
		} catch (Exception e) {
			logger.info("Exception while saving details");
			e.printStackTrace();
		}

		logger.info("ReadXmlFileScheduler readPain002BatchAksFile() method end ===========");
	}

	/**
	 * method to save the record in XP_PAYBATCH_ACK_DETAILS table
	 * 
	 * @return
	 */
	/*
	 * private int saveRecord(String insertpaymentBatchAksDetail,
	 * PaymentBatchAksDetailEntity paymentBatchAksDetail) { logger.
	 * info("ReadXmlFileScheduler saveRecord() method for paybatch_ack_details start ==========="
	 * ); Object[] params = new Object[] {
	 * paymentBatchAksDetail.getPaymentBatchAckID(),
	 * paymentBatchAksDetail.getPaymentInstructionId(),
	 * paymentBatchAksDetail.getAckStatus(), paymentBatchAksDetail.getFileName(),
	 * paymentBatchAksDetail.getCreatedBy(), paymentBatchAksDetail.getCreatedDate(),
	 * paymentBatchAksDetail.getLastUpdatedBy(),
	 * paymentBatchAksDetail.getLastUpdateDate(), paymentBatchAksDetail.getOrgId(),
	 * paymentBatchAksDetail.getFileStatus(),
	 * paymentBatchAksDetail.getAdditionalInfo() };
	 * 
	 * int[] types = new int[] { Types.VARCHAR, Types.DOUBLE, Types.VARCHAR,
	 * Types.VARCHAR, Types.INTEGER, Types.TIMESTAMP, Types.INTEGER,
	 * Types.TIMESTAMP, Types.DOUBLE, Types.VARCHAR, Types.VARCHAR };
	 * 
	 * StringBuilder insertSqltm = new StringBuilder(
	 * "INSERT INTO XP_PAYBATCH_ACK_DETAILS (" + "PAYBATCH_ACK_ID, " +
	 * "PAYMENT_INSTRUCTION_ID, " + "ACK_STATUS, " + "FILE_NAME, " + "created_by, "
	 * + "CREATION_DATE, " + "LAST_UPDATED_BY, " + "LAST_UPDATE_DATE, " + "org_id, "
	 * + "file_status, " + "ADDITIONAL_INFO) " + "VALUES ("); for (int i = 0; i <
	 * params.length; i++) { insertSqltm.append(params[i]); }
	 * insertSqltm.append(")"); logger.info("-----QUEYr--->> ********------------" +
	 * insertSqltm.toString()); int row =
	 * jdbcTemplate.update(insertpaymentBatchAksDetail, params, types);
	 * logger.debug(row + " row inserted."); logger.
	 * info("ReadXmlFileScheduler saveRecord() method for paybatch_ack_details end ==========="
	 * ); return row; }
	 */

	/*
	 * private String insertPaymentBatchAksDetailQuery() { final String insertSql =
	 * "INSERT INTO XP_PAYBATCH_ACK_DETAILS (" + "PAYBATCH_ACK_ID, " +
	 * "PAYMENT_INSTRUCTION_ID, " + "ACK_STATUS, " + "FILE_NAME, " + "created_by, "
	 * + "CREATION_DATE, " + "LAST_UPDATED_BY, " + "LAST_UPDATE_DATE, " + "org_id, "
	 * + "file_status, " + "ADDITIONAL_INFO) " +
	 * "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?)"; return insertSql; }
	 */
}
