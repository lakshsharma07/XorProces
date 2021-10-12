package com.xoriant.xorpay.xero;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "paymentdetails")
public class PaymentDetailPojo {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private String Message_ID ;
	  private String Created_Date_Time ;
	  private String No_Of_Transfers ;
	  private Double Control_Sum ;
	  private String Company_Name ;
	  private String Company_Address ;
	  private String Company_Post_Code ;
	  private String Company_City ;
	  private String Company_State ;
	  private String Company_Country ;
	  private String Company_Email_Address ;
	  private String Company_Currency ;
	  private String Company_Tax_ID ;
	  private String Payment_Information_ID ;
	  private Double Payment_Amount ;
	  private String Transfer_Date ;
	  private String Sender_Bank_Account_No ;
	  private String Sender_Bank_Account_Name ;
	  private String Sender_Bank_Branch_No ;
	  private String Sender_Bank_Address ;
	  private String Sender_Bank_Post_Code ;
	  private String Sender_Bank_City ;
	  private String Sender_Bank_State ;
	  private String Sender_Bank_Country ;
	  private String End_to_End_ID ;
	  private String Currency ;
	  private String Recipient_Name ;
	  private String Recipient_Address ;
	  private String Recipient_Post_Code ;
	  private String Recipient_City ;
	  private String Recipient_State ;
	  private String Recipient_Country ;
	  private String Recipient_Bank_Name ;
	  private String Recipient_Bank_Account_No ;
	  private String Recipient_Bank_Account_Name ;
	  private String Recipient_Bank_Branch_No ;
	  private String Recipient_Bank_Address ;
	  private String Recipient_Bank_Post_Code ;
	  private String Recipient_Bank_City ;
	  private String Recipient_Bank_State ;
	  private String Recipient_Bank_Country ;
	  private String Invoice_No ;
	  private String Invoice_Date ;
	  private Double Invoice_Amount ;
	  private Double Tax_Amount ;
	  private String Due_Date ;
	  private String Target_Account_Type ;
	  private String Bank_Instruction_Details ;
	  private Double Discount_Amount ;
	  private String Company_VAT_Registration_No ;
	  private String Tax_Type ;
	  private String transactional_cury ;


}
