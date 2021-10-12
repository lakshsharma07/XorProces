package com.xoriant.xorpay.encryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchProviderException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.openpgp.PGPException;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.pojo.ConfigPojo;

public class PGPEncryption {
	
	private static final Logger logger = LogManager.getLogger(PGPEncryption.class);
	
	private boolean isArmored = false;
	
	private String client_Id = TagConstants.CLIENT_ID;
	private String secret_Key = TagConstants.SECRET_KEY;
	private boolean integrityCheck = true;
	
	
	private String pubKeyFile = TagConstants.PGP_PUB_KEY_FILE;
	private String privKeyFile = TagConstants.PGP_PRIV_KEY_FILE;
	private String signatureFile = TagConstants.PGP_SIGN_FILE;
	
	public void pgpEncryption(String xmlFile, String profilename, String paymentNumber, ConfigPojo configPojo) throws NoSuchProviderException, IOException, PGPException{
		logger.info("PGPEncryption pgpEncryption method Started..");
		String PGP_ENCODED_FILE_PATH = configPojo.getPGP();
		
		xmlFile = xmlFile.trim().replaceFirst("^([\\W]+)<", "<");
		StringBuilder stringBuilder = new StringBuilder();
		
		if (null != PGP_ENCODED_FILE_PATH) {
			try {
				File directory = new File(PGP_ENCODED_FILE_PATH);
				if (!directory.exists()) {
						logger.debug("directoryName::" + PGP_ENCODED_FILE_PATH);
					try {
						directory.mkdirs();
					} catch(SecurityException se) {
						logger.error("PGPEncryption Exception while creating PGP directory "+se.toString(),se);
					}
				} else {
					logger.debug("PGP directory already Exists:::");
				}
				
				try(BufferedReader br = Files.newBufferedReader(Paths.get(xmlFile))){
					String line;
					while((line = br.readLine())!=null) {
						stringBuilder.append(line);
					}
				} catch (IOException e) {
					logger.error("PGPEncryption Exception while reading file "+e.toString(),e);
				} 
				
				File file = new File("EncPayload_" + profilename+ "_" + paymentNumber + ".dat");
				Path path = Paths.get(PGP_ENCODED_FILE_PATH + file);
				FileInputStream pubKeyIs = new FileInputStream(pubKeyFile);
				FileOutputStream cipheredFileIs = new FileOutputStream(file);
				byte[] bytes;
				try {
					file.createNewFile();
					bytes = PgpHelper.getInstance().encryptFile(cipheredFileIs, xmlFile, PgpHelper.getInstance().readPublicKey(pubKeyIs), isArmored, integrityCheck);
					Files.write(path, bytes);
					signAndVerify(xmlFile);

				} catch (IOException e) {
					logger.error("PGPEncryption Exception occured while writing PGP encoded file" + e.toString(), e);
				}
			} catch (Exception ex) {
				logger.error("PGPEncryption " + ex.toString(), ex);
			}
		} 
		logger.info("PGPEncryption pgpEncryption method Ended..");
		
	}
	
	private void signAndVerify(String xmlFile) throws Exception{
		FileInputStream privKeyIn = new FileInputStream(privKeyFile);
		FileInputStream pubKeyIs = new FileInputStream(pubKeyFile);
		FileInputStream plainTextInput = new FileInputStream(xmlFile);
		FileOutputStream signatureOut = new FileOutputStream(signatureFile);
				
		byte[] bIn = PgpHelper.getInstance().inputStreamToByteArray(plainTextInput);
		byte[] sig = PgpHelper.getInstance().createSignature(xmlFile, privKeyIn, signatureOut, secret_Key.toCharArray(), true);
		PgpHelper.getInstance().verifySignature(xmlFile, sig, pubKeyIs);
	}

}
