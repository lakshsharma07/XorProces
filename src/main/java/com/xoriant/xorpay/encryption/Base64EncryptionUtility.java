package com.xoriant.xorpay.encryption;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.KeyGenerator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xml.security.Init;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.keys.content.X509Data;
import org.apache.xml.security.keys.content.x509.XMLX509Certificate;
import org.apache.xml.security.keys.content.x509.XMLX509IssuerSerial;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.ElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;



public class Base64EncryptionUtility {
	
	private static final Logger logger = LogManager.getLogger(Base64EncryptionUtility.class);
	
	/**
	 * method to encrypt the payload 
	 */
	public void encryptPayload(PublicKey publicEncryptKey, Document xmlDoc, X509Certificate signCert) {
		try {
			logger.info("encryptPayload() method started....");

			String jceAlgorithmName = "DESede";
			KeyGenerator keyGenerator = KeyGenerator.getInstance(jceAlgorithmName);
			Key symmetricKey = keyGenerator.generateKey();
			String algorithmURI = "http://www.w3.org/2001/04/xmlenc#rsa-1_5";
			XMLCipher keyCipher = XMLCipher.getInstance(algorithmURI);

			keyCipher.init(3, publicEncryptKey);
			EncryptedKey encryptedKey = keyCipher.encryptKey(xmlDoc, symmetricKey);
			Element rootElement = xmlDoc.getDocumentElement();

			algorithmURI = "http://www.w3.org/2001/04/xmlenc#tripledes-cbc";
			XMLCipher xmlCipher = XMLCipher.getInstance(algorithmURI);

			xmlCipher.init(1, symmetricKey);
			EncryptedData encryptedData = xmlCipher.getEncryptedData();

			KeyInfo keyInfo = new KeyInfo(xmlDoc);
			keyInfo.add(encryptedKey);
			encryptedData.setKeyInfo(keyInfo);
			xmlCipher.doFinal(xmlDoc, rootElement, false);
			logger.info("encryptPayload() method ended. ");
		} catch (Exception e) {
			logger.error("Utility exception occured encryptPayload()" + e.toString(), e);
		}
	}
	
	/**
	 * method to signing the payload 
	 */
	public void signingPayload(X509Certificate signCert, Document xmlDoc, PrivateKey privateSignKey) throws Exception {
		logger.info("signingPayload() method started....");

		Init.init();
		ElementProxy.setDefaultPrefix("http://www.w3.org/2000/09/xmldsig#", "ds");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Element root = xmlDoc.getDocumentElement();
		XMLSignature sig = new XMLSignature(xmlDoc, "file:", "http://www.w3.org/2000/09/xmldsig#rsa-sha1");
		root.appendChild(sig.getElement());
		Transforms transforms = new Transforms(xmlDoc);
		transforms.addTransform("http://www.w3.org/2000/09/xmldsig#enveloped-signature");
		transforms.addTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315");
		sig.addDocument("", transforms, "http://www.w3.org/2000/09/xmldsig#sha1");
		KeyInfo info = sig.getKeyInfo();
		X509Data x509data = new X509Data(xmlDoc);
		x509data.add(new XMLX509IssuerSerial(xmlDoc, signCert));
		x509data.add(new XMLX509Certificate(xmlDoc, signCert));
		info.add(x509data);
		sig.sign(privateSignKey);
		logger.info("signingPayload() method ended.");
	}
	
	/**
	 * method to convert the payload into String
	 */
	public String convertPayloadToString(Document xmlDoc) throws TransformerException {
		logger.info("convertPayloadToString() method started....");

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty("omit-xml-declaration", "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(xmlDoc), new StreamResult(writer));
		logger.info("convertPayloadToString() method ended.");
		return writer.getBuffer().toString();
	}
	
	/**
	 * method to validate the XML schema
	 */
	public boolean validateXMLSchema(String xsdPath, StringReader xml) {
		try {
			logger.info("validateXMLSchema() method started..");
			SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			Schema schema = factory.newSchema(new File(xsdPath));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xml));
		} catch (Exception e) {
			logger.error("Utility exception occured validateXMLSchema()" + e.toString(), e);
			return false;
		}
		logger.info("validateXMLSchema() method ended.");
		return true;
	}
	
	/**
	 * method to load keystore value
	 */
	public KeyStore loadKeyStore(String JKS_FILE, String keyStorePwd) throws Exception {
		logger.info("loadKeyStore() method started....");
		KeyStore ks = KeyStore.getInstance("JKS");
		FileInputStream fis = new FileInputStream(JKS_FILE);
		ks.load(fis, keyStorePwd.toCharArray());
		fis.close();
		logger.info("loadKeyStore() method ended.");
		return ks;
	}
	
	/**
	 * method to create a document from a payload
	 */
	public Document documentFromPayload(String fileName) throws Exception {
		logger.info("documentFromPayload() method started....");
		BufferedReader rd = null;
		StringReader srd = null;
		try {
			rd = new BufferedReader(new FileReader(fileName));
			String inputLine = null;
			StringBuilder builder = new StringBuilder();
			while ((inputLine = rd.readLine()) != null) {
				builder.append(inputLine);
			}
			srd = new StringReader(builder.toString());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			logger.info("documentFromPayload() method ended.");
			return docBuilder.parse(new InputSource(srd));
		} finally {
			try {
				if (rd != null) {
					rd.close();
				}
				if (srd != null) {
					srd.close();
				}
			} catch (IOException e) {
				logger.error("Utility exception occured getPaymentDetailsForDashboard()" + e.toString(), e);
			}
		}
	}
	
	/**
	 * method to get signed encrypted payload
	 */
	public Document getSignedEncryptedPayload(String PEM_FILE, String keyStorePwd, Document payLoadDoc,
			X509Certificate XoriantXencCert, PrivateKey xorPrivateSignKey) throws Exception {
		logger.info("getSignedEncryptedPayload() method started....");
		CertificateFactory fact = CertificateFactory.getInstance("X.509");

		FileInputStream is = new FileInputStream(PEM_FILE);
		X509Certificate CitiXencCert = (X509Certificate) fact.generateCertificate(is);
		PublicKey citiPublicEncryptKey = CitiXencCert.getPublicKey();

		signingPayload(XoriantXencCert, payLoadDoc, xorPrivateSignKey);
		encryptPayload(citiPublicEncryptKey, payLoadDoc, XoriantXencCert);
		logger.info("getSignedEncryptedPayload() method ended.");
		return payLoadDoc;
	}
	/**
	 * method to generate base 64 for encrypt/decrypt
	 */
	public String generateBase64Input(String isoPayInXML) {
		logger.info("generateBase64Input() method started....");
		StringBuffer xmlStrSb = new StringBuffer();
		char[] pem_array = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
				'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
				'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', '+', '/' };

		byte[] inBuff = isoPayInXML.getBytes();
		int numBytes = inBuff.length;
		if (numBytes == 0) {
			return "";
		}
		byte[] outBuff = new byte[(numBytes - 1) / 3 + 1 << 2];
		int pos = 0;
		int len = 3;
		for (int j = 0; j < numBytes; j += 3) {
			if (j + 3 > numBytes) {
				len = numBytes - j;
			}
			if (len == 3) {
				byte a = inBuff[j];
				byte b = inBuff[(j + 1)];
				byte c = inBuff[(j + 2)];
				outBuff[(pos++)] = ((byte) pem_array[(a >>> 2 & 0x3F)]);
				outBuff[(pos++)] = ((byte) pem_array[((a << 4 & 0x30) + (b >>> 4 & 0xF))]);
				outBuff[(pos++)] = ((byte) pem_array[((b << 2 & 0x3C) + (c >>> 6 & 0x3))]);
				outBuff[(pos++)] = ((byte) pem_array[(c & 0x3F)]);
			} else if (len == 2) {
				byte a = inBuff[j];
				byte b = inBuff[(j + 1)];
				byte c = 0;
				outBuff[(pos++)] = ((byte) pem_array[(a >>> 2 & 0x3F)]);
				outBuff[(pos++)] = ((byte) pem_array[((a << 4 & 0x30) + (b >>> 4 & 0xF))]);
				outBuff[(pos++)] = ((byte) pem_array[((b << 2 & 0x3C) + (c >>> 6 & 0x3))]);
				outBuff[(pos++)] = 61;
			} else {
				byte a = inBuff[j];
				byte b = 0;
				outBuff[(pos++)] = ((byte) pem_array[(a >>> 2 & 0x3F)]);
				outBuff[(pos++)] = ((byte) pem_array[((a << 4 & 0x30) + (b >>> 4 & 0xF))]);
				outBuff[(pos++)] = 61;
				outBuff[(pos++)] = 61;
			}
		}
		String paymentBase64 = new String(outBuff);
		xmlStrSb.append("<Request>");
		xmlStrSb.append("<paymentBase64>");
		xmlStrSb.append(paymentBase64);
		xmlStrSb.append("</paymentBase64>");
		xmlStrSb.append("</Request>");
		logger.info("generateBase64Input() method ended.");
		return xmlStrSb.toString();
	}


}
