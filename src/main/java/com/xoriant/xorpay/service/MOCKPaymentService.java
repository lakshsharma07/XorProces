package com.xoriant.xorpay.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.iso.pain002.Document;
import com.xoriant.xorpay.iso.pain002.GroupHeader36;
import com.xoriant.xorpay.iso.pain002.OriginalGroupInformation20;
import com.xoriant.xorpay.pojo.ConfigPojo;

@Service
public class MOCKPaymentService {

	private static final String FILES = "files/template/mock";
	public static final String MOCK_RJCT_FILE_XML = "MOCK_RJCT_FILE.xml";
	public static final String MOCK_ACK_FILE_XML = "MOCK_ACK_FILE.xml";
	public static final String MOCK_RJCT_PAYMENT_XML = "MOCK_RJCT_PAYMENT.xml";
	public static final String MOCK_ACSP_PAYMENT_XML = "MOCK_ACSP_PAYMENT.xml";
	public static final String MOCK_ACSC_PACS002 = "MOCK_ACSC_PACS002.xml";
	private static final Logger logger = LoggerFactory.getLogger(MOCKPaymentService.class);
	@Autowired
	ReadXmlFileScheduler readSchedular;
	@Autowired
	CLSService clsService;

	public void createACKMockFile(String msgId, List<AggregatedPaymentEntity> xmlTagsEntitiesList, String mockType,
			String mockPaymentType, ConfigPojo configPojo) {
		switch (mockType) {
		case XorConstant.MOCK_BATCH:
			mockBatchPaymentFile(msgId, xmlTagsEntitiesList, mockType, mockPaymentType, configPojo);
			break;
		case XorConstant.MOCK_PAYMENT:
			mockPaymentFile(msgId, xmlTagsEntitiesList, mockType, mockPaymentType, configPojo);
			break;

		}

	}

	private void mockBatchPaymentFile(String msgId, List<AggregatedPaymentEntity> xmlTagsEntitiesList, String mockType,
			String mockPaymentType, ConfigPojo configPojo) {
		XMLGregorianCalendar xmlGregorianCalendarNow;
		try {
			xmlGregorianCalendarNow = DatatypeFactory.newInstance()
					.newXMLGregorianCalendar(LocalDateTime.now().toString());
			String mockFile = getMockBatchFileOnType(mockPaymentType);
			logger.info("mockFile " + mockFile);
			if (null != mockFile) {
				try {
					writeToFile(mockType, xmlTagsEntitiesList, null, msgId, mockFile, xmlGregorianCalendarNow, msgId,
							xmlTagsEntitiesList.size(), configPojo);
					// String internalUuid = xmlTagsEntitiesList.get(0).getInernalUuid();
					// readSchedular.readPain002Xmlfiles(internalUuid, msgId, null,
					// xmlTagsEntitiesList.size(),
					// configPojo);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} catch (DatatypeConfigurationException e2) {
			e2.printStackTrace();
		}

	}

	private void mockPaymentFile(String msgId, List<AggregatedPaymentEntity> xmlTagsEntitiesList, String mockType,
			String mockPaymentType, ConfigPojo configPojo) {

		xmlTagsEntitiesList.stream().forEach(xmlTagsEntity -> {

			XMLGregorianCalendar xmlGregorianCalendarNow;
			try {
				xmlGregorianCalendarNow = DatatypeFactory.newInstance()
						.newXMLGregorianCalendar(LocalDateTime.now().toString());

				String mockFile = getMockPaymentFileOnType(mockPaymentType);
				try {

					writeToFile(mockType, null, xmlTagsEntity, msgId, mockFile, xmlGregorianCalendarNow,
							xmlTagsEntity.getEndToEndId(), xmlTagsEntity.getNbOfTxs(), configPojo);
					// String internalUuid = xmlTagsEntity.getInernalUuid();
					// readSchedular.readPain002Xmlfiles(internalUuid, msgId,
					// xmlTagsEntity.getEndToEndId(),
					// xmlTagsEntitiesList.size(), configPojo);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} catch (DatatypeConfigurationException e2) {
				e2.printStackTrace();
			}

		});
	}

	private void writeToFile(String mockType, List<AggregatedPaymentEntity> xmlTagsEntitiesList,
			AggregatedPaymentEntity e, String msgId, String mockFile, XMLGregorianCalendar xmlGregorianCalendarNow,
			String endToEndId, Integer noOfTrnxForEndToEndId, ConfigPojo configPojo) throws IOException, JAXBException {
		String endToEndIdFolder = endToEndId.replaceAll(XorConstant.REGEX_ALPHANUMERIC, "_");
		String msgIdFolder = msgId.replaceAll(XorConstant.REGEX_ALPHANUMERIC, "_");

		String userDirectory = Paths.get("").toAbsolutePath().toString();
		logger.info("userDirectory---------- " + userDirectory);
		File fileResponseIn = new File(configPojo.getSAMPLE_PAN_IN() + mockFile);
		// File fileResponseIn = new File(userDirectory + File.separator + FILES +
		// File.separator + mockFile);

		File folder = new File(configPojo.getPAN_IN());
		File fileResponseOut = new File(
				configPojo.getPAN_IN() + "MOCK_" + msgIdFolder + "_" + endToEndIdFolder + "_" + mockFile);

		logger.info("userDirectory---------- " + configPojo.getSAMPLE_PAN_IN());
		logger.info("userDirectory---------- " + fileResponseOut);
		if (!folder.isDirectory()) {
			folder.mkdirs();
		}

		if (XorConstant.isCLS) {
			String content = new String(Files.readAllBytes(fileResponseIn.toPath()));

			clsService.createMockFile(content, fileResponseOut, e);
		} else {

			BufferedReader br = new BufferedReader(new FileReader(fileResponseIn));
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

			GroupHeader36 groupHeader = document.getCstmrPmtStsRpt().getGrpHdr();
			groupHeader.setMsgId(msgId);
			groupHeader.setCreDtTm(xmlGregorianCalendarNow);

			OriginalGroupInformation20 groupInfoStatus = document.getCstmrPmtStsRpt().getOrgnlGrpInfAndSts();
			// if (mockType.equals(XorConstant.MOCK_PAYMENT)) {
			groupInfoStatus.setOrgnlMsgId(msgId);
			// } else if (mockType.equals(XorConstant.MOCK_BATCH)) {
			// groupInfoStatus.setOrgnlMsgId(msgId);
			// }
			groupInfoStatus.setOrgnlMsgNmId("pain.001.001.03");
			groupInfoStatus.setOrgnlCreDtTm(xmlGregorianCalendarNow);
			groupInfoStatus.setOrgnlNbOfTxs("" + noOfTrnxForEndToEndId);

			if (mockType.equals(XorConstant.MOCK_PAYMENT)) {

				// List<NumberOfTransactionsPerStatus3> ls = new ArrayList<>();
				// NumberOfTransactionsPerStatus3 nb = new NumberOfTransactionsPerStatus3();
				// nb.setDtldNbOfTxs("" + xmlTagsEntitiesList.size());
				// nb.setDtldSts(TransactionIndividualStatus3Code.ACSP);

				// groupInfoStatus.setNbOfTxsPerSts(ls);
				groupInfoStatus.getNbOfTxsPerSts().get(0).setDtldNbOfTxs("" + e.getNbOfTxs());
				// xmlTagsEntitiesList.forEach(e -> {
				document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0).getOrgnlTxRef()
						.getAmt().getInstdAmt().setValue(e.getInstdAmnt());
				document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0).getOrgnlTxRef()
						.getAmt().getInstdAmt().setCcy(e.getCcyCode());
				document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0).getOrgnlTxRef()
						.setReqdExctnDt(e.getReqstExDt().toLocalDate());
				document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).getTxInfAndSts().get(0)
						.setOrgnlEndToEndId(e.getEndToEndId());

				document.getCstmrPmtStsRpt().getOrgnlPmtInfAndSts().get(0).setOrgnlPmtInfId(e.getPmtInfId());

				// });
			}

			// Writes XML file to file-system
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			jaxbMarshaller.marshal(document, fileResponseOut);
		}
	}

	private String getMockPaymentFileOnType(String mockPaymentType) {
		String mockFile = null;
		if (mockPaymentType.equalsIgnoreCase(XorConstant.MOCK_ACCP)) {
			mockFile = MOCK_ACSP_PAYMENT_XML;
		} else if (mockPaymentType.equalsIgnoreCase(XorConstant.MOCK_RJCT)) {
			mockFile = MOCK_RJCT_PAYMENT_XML;
		} else if (mockPaymentType.equalsIgnoreCase(XorConstant.MOCK_CLS_ACSC)) {
			mockFile = MOCK_ACSC_PACS002;
		}
		return mockFile;
	}

	private String getMockBatchFileOnType(String mockPaymentType) {
		String mockFile = null;
		if (mockPaymentType.equalsIgnoreCase(XorConstant.MOCK_ACCP)) {
			mockFile = MOCK_ACK_FILE_XML;
		} else if (mockPaymentType.equalsIgnoreCase(XorConstant.MOCK_RJCT)) {
			mockFile = MOCK_RJCT_FILE_XML;
		}
		return mockFile;
	}

}
