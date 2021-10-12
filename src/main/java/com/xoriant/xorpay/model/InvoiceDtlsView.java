package com.xoriant.xorpay.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDtlsView {

	private Long payment_id;
	private String invoice_num;
	private String invoice_amount; 
	private String invoice_currency_code; 
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy") 
	private String invoice_date; 
	private Long invoice_id; 
	private String created_by; 
	private String creation_date; 
	private String last_update_date; 
	private String last_updated_by; 
	private String org_id; 
	private String payment_method_code; 
	private Long request_id; 
	private String supplier_name; 
	private String supplier_site_name;
		
}
