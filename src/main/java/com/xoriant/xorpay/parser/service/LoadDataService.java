package com.xoriant.xorpay.parser.service;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import com.prowidesoftware.swift.model.SwiftBlock1;
import com.prowidesoftware.swift.model.SwiftBlock2Output;
import com.prowidesoftware.swift.model.SwiftBlock3;
import com.prowidesoftware.swift.model.field.Field102;
import com.prowidesoftware.swift.model.field.Field106;
import com.prowidesoftware.swift.model.field.Field108;
import com.prowidesoftware.swift.model.field.Field121;
import com.prowidesoftware.swift.model.field.Field175;
import com.prowidesoftware.swift.model.field.Field432;
import com.prowidesoftware.swift.model.field.Field619;
import com.prowidesoftware.swift.model.mt.mt0xx.MT019;
import com.prowidesoftware.swift.model.mt.mt2xx.MT202;
import com.xoriant.xorpay.parser.entity.PaymentAckDetails;
import com.xoriant.xorpay.parser.entity.PaymentDetails;

import io.cloudio.secure.Keep;

@Service
public class LoadDataService implements Keep {

	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${input.file.mt202}")
	private String INPUT_FILE;

	// @Value("${input.xml.file.pacs002}")
	// private String INPUT_XML_FILE;

	//@Value("${output_xml_file.mt019}")
	//private String OUTPUT_XML_FILE;

	public void readJson() throws IOException, SQLException {
			PaymentDetails paymentDetails = null;
			System.out.println("INPUT_FILE "+ INPUT_FILE);
			File file = new File(INPUT_FILE);
			MT202 msg = MT202.parse(file);
			// System.out.println(msg.getSwiftMessage().toJson());

			// System.out.println(msg.message());//msg.getSender()
			paymentDetails = new PaymentDetails();
			paymentDetails
					.setSender_bicfi(msg.getLogicalTerminal().substring(0, 8) + msg.getLogicalTerminal().substring(9));
			paymentDetails.setReceiver_bicfi(msg.getReceiver().substring(0, 8) + msg.getReceiver().substring(9));
			paymentDetails.setBiz_msgid(msg.getField20().getComponent1());
			paymentDetails.setPmtid_instrid(msg.getField20().getComponent1());
			paymentDetails.setPmtid_endtoendid(msg.getField21().getComponent1());
			paymentDetails.setPmtid_uetr(msg.getSwiftMessage().getBlock3().getTagValue("121"));
			paymentDetails.setIntrbksttlmdt(msg.getField32A().getComponent1());// 1
			paymentDetails.setIntrbksttlmamt_ccy(msg.getField32A().getComponent2());// 2
			paymentDetails.setIntrbksttlmamt(msg.getField32A().getComponent3());// 3
			paymentDetails.setSender_instgagt_bicfi(msg.getLogicalTerminal());
			paymentDetails.setReceiver_instgagt_bicfi(msg.getReceiver());
			paymentDetails.setReceiver_instgagt1_bicfi(msg.getField56A().getComponent2());// 2
			paymentDetails.setDbtr_bicfi(msg.getField52A().getComponent3());// 1
			paymentDetails.setCdtragt_bicfi(msg.getField57A().getComponent3());// 2
			paymentDetails.setCdtragt_iban(msg.getField57A().getComponent2());// 1
			paymentDetails.setCdtr_bicfi(msg.getField58A().getComponent3());// 2
			paymentDetails.setCdtracct_iban(msg.getField58A().getComponent2());// 1

			// System.out.println(paymentDetails.toString());

			Connection conn;

			conn = DriverManager.getConnection(url, username, password);
			DBQueryService dbQueryService = new DBQueryService();

			dbQueryService.InserPaymentDetailsInDatabase(conn, paymentDetails);
	}

	public PaymentAckDetails readXML(String INPUT_XML_FILE) {
		try {
			PaymentAckDetails paymentAckDetails = null;
			File file = new File(INPUT_XML_FILE);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();

			paymentAckDetails = new PaymentAckDetails();
			paymentAckDetails.setSender_bicfi(doc.getElementsByTagName("h:BICFI").item(0).getTextContent());
			paymentAckDetails.setReceiver_bicfi(doc.getElementsByTagName("h:BICFI").item(1).getTextContent());
			paymentAckDetails.setOrgnlMsgId(doc.getElementsByTagName("Doc:OrgnlMsgId").item(0).getTextContent());
			paymentAckDetails.setOrgnlInstrId(doc.getElementsByTagName("Doc:OrgnlInstrId").item(0).getTextContent());
			paymentAckDetails
					.setOrgnlEndToEndId(doc.getElementsByTagName("Doc:OrgnlEndToEndId").item(0).getTextContent());
			paymentAckDetails.setOrgnlUETR(doc.getElementsByTagName("Doc:OrgnlUETR").item(0).getTextContent());
			paymentAckDetails.setTxSts(doc.getElementsByTagName("Doc:TxSts").item(0).getTextContent());
			paymentAckDetails.setCreDt(doc.getElementsByTagName("h:CreDt").item(0).getTextContent());

			return paymentAckDetails;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void createMT019(PaymentAckDetails paymentAckDetails, String OUTPUT_XML_FILE) {
		try {
			File file = new File(OUTPUT_XML_FILE);
			MT019 m = new MT019();

			SwiftBlock1 b1 = new SwiftBlock1();

			b1.setApplicationId("F");
			b1.setServiceId("01");
			b1.setLogicalTerminal(paymentAckDetails.getReceiver_bicfi().substring(0, 8) + "X"
					+ paymentAckDetails.getReceiver_bicfi().substring(8));
			b1.setSessionNumber("0000");
			b1.setSequenceNumber("000000");
			m.getSwiftMessage().setBlock1(b1);

			SwiftBlock2Output b2 = new SwiftBlock2Output();

			b2.setSenderInputTime("0000");
			b2.setMessageType("019");
			b2.setMIRDate("210917");
			b2.setMIRLogicalTerminal(paymentAckDetails.getSender_bicfi().substring(0, 8) + "X"
					+ paymentAckDetails.getSender_bicfi().substring(8));
			b2.setMIRSequenceNumber("0000");
			b2.setMIRSessionNumber("000000");
			b2.setReceiverOutputDate("991231");
			b2.setReceiverOutputTime("0000");
			m.getSwiftMessage().setBlock2(b2);
			b2.setMessagePriority("S");

			SwiftBlock3 b3 = new SwiftBlock3();
			m.getSwiftMessage().setBlock3(b3);

			m.getSwiftMessage().getBlock3().append(new Field121(paymentAckDetails.getOrgnlUETR()));
			// addTag(0, Field121.tag(paymentAckDetails.getOrgnlUETR()));

			m.append(new Field175("0000"));
			m.append(new Field106("991231" + paymentAckDetails.getReceiver_bicfi().substring(0, 8) + "X"
					+ paymentAckDetails.getReceiver_bicfi().substring(8) + "0000000000"));
			m.append(new Field108(paymentAckDetails.getOrgnlInstrId()));
			m.append(new Field102("BANKCCXXAXXX"));
			m.append(new Field432("B0"));
			m.append(new Field619("TGT"));

			// System.out.println(m.message());
			m.write(file);

			// m.append(new Field121("0604"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
