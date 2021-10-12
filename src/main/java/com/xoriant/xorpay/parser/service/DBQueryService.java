package com.xoriant.xorpay.parser.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.xoriant.xorpay.parser.entity.PaymentDetails;
import com.xoriant.xorpay.xero.PaymentDetailPojo;

import io.cloudio.secure.Keep;

@Service
public class DBQueryService  implements Keep{
	
	public static DateTimeFormatter formateYYYYMMDDTHHMMSS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss",
			Locale.ENGLISH);
	
	public void InserPaymentDetailsInDatabase(Connection conn,PaymentDetails paymentDetails) {
		
		java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
    	System.out.println(date);
    	
    	String sql = "INSERT INTO stg_cls\r\n"
    			+ "(apphdr_sender_bicfi, apphdr_receiver_bicfi, apphdr_biz_msgid, apphdr_msgdefid, apphdr_credt, Message_ID, grphdr_credtm, grphdr_nboftxs, grphdr_sttlmtd, grphdr_clrsys_cd, docmnt_pmtid_instrid, End_to_End_ID, docmnt_pmtid_uetr, docmnt_intrbksttlmdt, docmnt_intrbksttlmamt_ccy, docmnt_intrbksttlmamt, docmnt_sender_instgagt_bicfi, docmnt_receiver_instgagt_bicfi, docmnt_receiver_instgagt1_bicfi, docmnt_dbtr_bicfi, docmnt_cdtragt_bicfi, docmnt_cdtragt_iban, docmnt_cdtr_bicfi, docmnt_cdtracct_iban,PAYMENT_PROCESS_PROFILE)\r\n"
    			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    	
    	
		//Connection conn;
    	
    	LocalDateTime ldt = LocalDateTime.now();
    	ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));
    	long millis = zdt.toInstant().toEpochMilli();
    	
		try {
			//System.out.println("md -"+ preMId+millis+paymentDetails.getBiz_msgid());
			//conn = DriverManager.getConnection(url, username, password);
			LocalDateTime ld = LocalDateTime.now();
			String val = ld.format(formateYYYYMMDDTHHMMSS);
			PreparedStatement stmt = conn.prepareStatement(sql);
	    	stmt.setString(1, paymentDetails.getSender_bicfi());
	    	stmt.setString(2, paymentDetails.getReceiver_bicfi());
	    	stmt.setString(3, paymentDetails.getBiz_msgid());
	    	stmt.setString(4, "pacs.009.001.08CORE");
	    	stmt.setString(5, val);
	    	stmt.setString(6, paymentDetails.getBiz_msgid());
	    	stmt.setString(7, val);
	    	stmt.setString(8, "1");
	    	stmt.setString(9, "CLRG");
	    	stmt.setString(10, "TGT");
	    	stmt.setString(11, paymentDetails.getPmtid_instrid());
	    	stmt.setString(12, paymentDetails.getPmtid_endtoendid());
	    	stmt.setString(13, paymentDetails.getPmtid_uetr());
	    	stmt.setString(14, paymentDetails.getIntrbksttlmdt().replace("Z", ""));
	    	stmt.setString(15, paymentDetails.getIntrbksttlmamt_ccy());
	    	stmt.setString(16, paymentDetails.getIntrbksttlmamt());
	    	stmt.setString(17, paymentDetails.getSender_instgagt_bicfi());
	    	stmt.setString(18, paymentDetails.getReceiver_instgagt_bicfi());
	    	stmt.setString(19, paymentDetails.getReceiver_instgagt1_bicfi());
	    	stmt.setString(20, paymentDetails.getDbtr_bicfi());
	    	stmt.setString(21, paymentDetails.getCdtragt_bicfi());
	    	stmt.setString(22, paymentDetails.getCdtragt_iban());
	    	stmt.setString(23, paymentDetails.getCdtr_bicfi());
	    	stmt.setString(24, paymentDetails.getCdtracct_iban());
	    	stmt.setString(25, "CLSFR_FR_99999_CLS");
	    	stmt.executeUpdate();
	    	//stmt.executeUpdate();
	    	
	    	stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	 

    	
	}
	public void InserPaymentDetailsInDatabase(Connection conn, PaymentDetailPojo paymentDetails) {

		java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
		System.out.println(date);

		String sql = "INSERT INTO stg_xorpay_84_fmw_xero(Message_ID, Created_Date_Time, No_Of_Transfers, Control_Sum, Company_Name, Company_Address, Company_Post_Code, Company_City, Company_State, Company_Country, Company_Email_Address, Company_Currency, Company_Tax_ID, Payment_Information_ID, Payment_Amount, Transfer_Date, Sender_Bank_Account_No, Sender_Bank_Account_Name, Sender_Bank_Branch_No, Sender_Bank_Address, Sender_Bank_Post_Code, Sender_Bank_City, Sender_Bank_State, Sender_Bank_Country, End_to_End_ID, Currency, Recipient_Name, Recipient_Address, Recipient_Post_Code, Recipient_City, Recipient_State, Recipient_Country, Recipient_Bank_Name, Recipient_Bank_Account_No, Recipient_Bank_Account_Name, Recipient_Bank_Branch_No, Recipient_Bank_Address, Recipient_Bank_Post_Code, Recipient_Bank_City, Recipient_Bank_State, Recipient_Bank_Country, Invoice_No, Invoice_Date, Invoice_Amount, Tax_Amount, Due_Date, Target_Account_Type, Bank_Instruction_Details, Discount_Amount, Company_VAT_Registration_No, Tax_Type, transactional_cury,PAYMENT_PROCESS_PROFILE,internal_uuid) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";;

		// Connection conn;

		LocalDateTime ldt = LocalDateTime.now();
		ZonedDateTime zdt = ldt.atZone(ZoneId.of("America/Los_Angeles"));
		long millis = zdt.toInstant().toEpochMilli();

		try {
			
			LocalDateTime ld = LocalDateTime.now();
			String val = ld.format(formateYYYYMMDDTHHMMSS);
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1 , paymentDetails.getMessage_ID ().split("-")[4]);
			stmt.setString(2 , paymentDetails.getCreated_Date_Time ()+"T00:00:00");
			stmt.setString(3 , paymentDetails.getNo_Of_Transfers ());
			stmt.setDouble(4 , paymentDetails.getControl_Sum ());
			stmt.setString(5 , paymentDetails.getCompany_Name ());
			stmt.setString(6 , paymentDetails.getCompany_Address ());
			stmt.setString(7 , paymentDetails.getCompany_Post_Code ());
			stmt.setString(8 , paymentDetails.getCompany_City ());
			stmt.setString(9 , paymentDetails.getCompany_State ());
			stmt.setString(10 , paymentDetails.getCompany_Country ());
			stmt.setString(11 , paymentDetails.getCompany_Email_Address ());
			stmt.setString(12 , paymentDetails.getCompany_Currency ());
			stmt.setString(13 , paymentDetails.getCompany_Tax_ID ());
			stmt.setString(14 , paymentDetails.getPayment_Information_ID ().split("-")[4]);
			stmt.setDouble(15 , paymentDetails.getPayment_Amount ());
			stmt.setString(16 , paymentDetails.getTransfer_Date ()+"T00:00:00");
			stmt.setString(17 , paymentDetails.getSender_Bank_Account_No ());
			stmt.setString(18 , paymentDetails.getSender_Bank_Account_Name ());
			stmt.setString(19 , paymentDetails.getSender_Bank_Branch_No ());
			stmt.setString(20 , paymentDetails.getSender_Bank_Address ());
			stmt.setString(21 , paymentDetails.getSender_Bank_Post_Code ());
			stmt.setString(22 , paymentDetails.getSender_Bank_City ());
			stmt.setString(23 , paymentDetails.getSender_Bank_State ());
			stmt.setString(24 , paymentDetails.getSender_Bank_Country ());
			stmt.setString(25 , paymentDetails.getEnd_to_End_ID ().split("-")[4]);
			stmt.setString(26 , paymentDetails.getCurrency ());
			stmt.setString(27 , paymentDetails.getRecipient_Name ());
			stmt.setString(28 , paymentDetails.getRecipient_Address ());
			stmt.setString(29 , paymentDetails.getRecipient_Post_Code ());
			stmt.setString(30 , paymentDetails.getRecipient_City ());
			stmt.setString(31 , paymentDetails.getRecipient_State ());
			stmt.setString(32 , paymentDetails.getRecipient_Country ());
			stmt.setString(33 , paymentDetails.getRecipient_Bank_Name ());
			stmt.setString(34 , paymentDetails.getRecipient_Bank_Account_No ());
			stmt.setString(35 , paymentDetails.getRecipient_Bank_Account_Name ());
			stmt.setString(36 , paymentDetails.getRecipient_Bank_Branch_No ());
			stmt.setString(37 , paymentDetails.getRecipient_Bank_Address ());
			stmt.setString(38 , paymentDetails.getRecipient_Bank_Post_Code ());
			stmt.setString(39 , paymentDetails.getRecipient_Bank_City ());
			stmt.setString(40 , paymentDetails.getRecipient_Bank_State ());
			stmt.setString(41 , paymentDetails.getRecipient_Bank_Country ());
			stmt.setString(42 , paymentDetails.getInvoice_No ());
			stmt.setString(43 , paymentDetails.getInvoice_Date ()+"T00:00:00");
			stmt.setDouble(44 , paymentDetails.getInvoice_Amount ());
			stmt.setDouble(45 , paymentDetails.getTax_Amount ());
			stmt.setString(46 , paymentDetails.getDue_Date ()+"T00:00:00");
			stmt.setString(47 , paymentDetails.getTarget_Account_Type ());
			stmt.setString(48 , paymentDetails.getBank_Instruction_Details ());
			stmt.setDouble(49 , paymentDetails.getDiscount_Amount ());
			stmt.setString(50 , paymentDetails.getCompany_VAT_Registration_No ());
			stmt.setString(51 , paymentDetails.getTax_Type ());
			stmt.setString(52 , paymentDetails.getTransactional_cury ());
			stmt.setString(53, "CITIUA_US_499_ONS_C12_01");
			stmt.setString(54, paymentDetails.getMessage_ID ());

			stmt.executeUpdate();
			// stmt.executeUpdate();

			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
