package com.xoriant.xorpay.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.ConfigConstants;
import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.encryption.Base64Encyption;
import com.xoriant.xorpay.encryption.PGPEncryption;
import com.xoriant.xorpay.entity.ConfigEntity;
import com.xoriant.xorpay.excepions.XorpayException;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.util.XorUtil;

@Service
public class ExportService {

	private static final Logger logger = LoggerFactory.getLogger(ExportService.class);
	@Autowired
	ConfigConstants configConstants;
	@Autowired
	private PaymentXMLService paymentXMLService;
	@Autowired
	private PaymentXMLFromStructureService paymentXMLServiceFromStructureService;

	public InputStreamResource getXMLBlobFromStructure(String processId, String profilename, String messageId,
			Integer sourceSystem, List<Map<Integer, String>> aggDataMapList,
			Map<String, Map<String, List<Map<Integer, String>>>> rmtIfoForMsgId,
			Map<String, Set<Integer>> supressTagSet, Set<String> addressTagSet, ConfigPojo configPojo)
			throws Exception {
		File folder = new File(configPojo.getPAN_OUT());

		File file = new File(configPojo.getPAN_OUT() + "PayloadXML_" + profilename + "_" + messageId + ".xml");

		if (paymentXMLServiceFromStructureService.generatePaymentXML(processId, profilename, messageId, sourceSystem,
				aggDataMapList, rmtIfoForMsgId, supressTagSet, addressTagSet, configPojo)) {
			// encrypt on the basis of condition
			ConfigEntity encodeTypeMap = paymentXMLService.getEncryptionConfig(XorConstant.XML_ENC, XorConstant.ACTIVE);
			try {
				if (null != encodeTypeMap && encodeTypeMap.getConfigValue().equalsIgnoreCase(XorConstant.BASE64)) {
					Base64Encyption base64encryption = new Base64Encyption();
					base64encryption.encodePayload(folder.getPath(), profilename, messageId, configPojo);
				} else if (null != encodeTypeMap && encodeTypeMap.getConfigValue().equalsIgnoreCase(XorConstant.PGP)) {
					PGPEncryption pgpencryption = new PGPEncryption();
					pgpencryption.pgpEncryption(folder.getPath(), profilename, messageId, configPojo);
				} else {
					// File without encryption
				}
			} catch (Exception e) {
				logger.error("Exception occured in payment file encryption", e.toString(), e);
				throw e;
			}
		}

		byte[] content = null;
		try {
			content = Files.readAllBytes(file.toPath());
		} catch (Exception e) {
			logger.info("Exception while payment Gen", e);
			throw e;
		}

		ByteArrayInputStream in = new ByteArrayInputStream(content);

		return new InputStreamResource(in);

	}

	public void checkFolders(ConfigPojo configPojo) throws XorpayException, IOException {

		logger.info("configPojo.getPAN_OUT() " + configPojo.getPAN_OUT());
		logger.info("configPojo.getPAN_IN() " + configPojo.getPAN_IN());
		logger.info("configPojo.getPAN_ARCHIVE() " + configPojo.getPAN_ARCHIVE());
		File folder = new File(configPojo.getPAN_OUT());
		// File panIn = new File(configPojo.getPAN_IN());
		File panIn = new File(configPojo.getPAN_IN());
		File panarchive = new File(configPojo.getPAN_ARCHIVE());
		String sampe = "sample.txt";
		// if (!panIn.exists()) {
		// logger.info("Pain In folder does not exits, will try to create it");
		// if (XorUtil.creatFolder(configPojo.getPAN_IN() + sampe)) {
		// logger.info("Folder created");
		// }
		// }
		if (!panIn.exists()) {
			logger.info("Pain Out folder does not exits, will try to create it");
			if (XorUtil.creatFolder(configPojo.getPAN_IN() + sampe)) {
				logger.info("Folder created");
			}
		}
		if (!folder.exists()) {
			logger.info("Output folder does not exits, will try to create it");
			if (XorUtil.creatFolder(configPojo.getPAN_OUT() + sampe)) {
				logger.info("Folder created");
			}
		}
		if (!panarchive.exists()) {
			logger.info("Archive folder does not exits, will try to create it");
			if (XorUtil.creatFolder(configPojo.getPAN_ARCHIVE() + sampe)) {
				logger.info("Folder created");
			}
		}

	}

}
