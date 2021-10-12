package com.xoriant.xorpay.encryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.xoriant.xorpay.pojo.ConfigPojo;

public class Base64Encyption {

	private static final Logger logger = LogManager.getLogger(Base64Encyption.class);
	/*
	 * 
	 */
	public void encodePayload(String xmlFile, String profilename, String paymentNumber, ConfigPojo configPojo)
			throws Exception {
		
		logger.info("encodePayload method Started..");
		String BASE_64_ENCODED_FILE_PATH = configPojo.getBASE64();
		Base64EncryptionUtility base64Utility = new Base64EncryptionUtility();
		xmlFile = xmlFile.trim().replaceFirst("^([\\W]+)<", "<");
		StringReader reader = new StringReader(xmlFile);
		StringBuilder stringBuilder = new StringBuilder();
		

		/*
		 * logger.warn("pain.001.001.03.xml validates against pain.001.001.03.xsd? " +
		 * base64Utility.validateXMLSchema(XSD_FILE, reader));
		 */
		
		if (null != BASE_64_ENCODED_FILE_PATH) {
			try {
				File directory = new File(BASE_64_ENCODED_FILE_PATH);
				if (!directory.exists()) {
						logger.debug("directoryName::" + BASE_64_ENCODED_FILE_PATH);
					try {
						directory.mkdirs();
					} catch(SecurityException se) {
						logger.error("Base64Encyption exception while creating Base64 directory "+se.toString(),se);
					}
				} else {
						logger.debug("Base64 directory already Exists:::");
				}
				
				try(BufferedReader br = Files.newBufferedReader(Paths.get(xmlFile))){
					String line;
					while((line = br.readLine())!=null) {
						stringBuilder.append(line);
					}
				} catch (IOException e) {
					logger.error("Base64Encyption exception while reading file "+e.toString(),e);
				} 
				String content = stringBuilder.toString();

				String requestBase64 = base64Utility.generateBase64Input(content);
				File file = new File("EncPayload_" + profilename+"_" + paymentNumber + ".dat");
				Path path = Paths.get(BASE_64_ENCODED_FILE_PATH + file);
				byte[] bytes = requestBase64.getBytes();
				try {
					file.createNewFile();
					Files.write(path, bytes);

				} catch (IOException e) {
					logger.error("Base64Encyption exception occured while writing Base64 encoded file" + e.toString(), e);
				}
			} catch (Exception ex) {
				logger.error("Base64Encyption " + ex.toString(), ex);
			}
		} 
		logger.info("encodePayload method Ended..");
	}
	
}
