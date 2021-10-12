package com.xoriant.xorpay.parser.entity;

import io.cloudio.secure.Keep;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails implements Keep {
	
	private String sender_bicfi;
	private String receiver_bicfi;
	private String biz_msgid;
	private String pmtid_instrid;
	private String pmtid_endtoendid;
	private String pmtid_uetr;
	private String intrbksttlmdt;
	private String intrbksttlmamt_ccy;
	private String intrbksttlmamt;
	private String sender_instgagt_bicfi;
	private String receiver_instgagt_bicfi;
	private String receiver_instgagt1_bicfi;
	private String dbtr_bicfi;
	private String cdtragt_bicfi;
	private String cdtragt_iban;
	private String cdtr_bicfi;
	private String cdtracct_iban;
	@Override
	public String toString() {
		return "PaymentDetails [sender_bicfi=" + sender_bicfi + ", receiver_bicfi=" + receiver_bicfi + ", biz_msgid="
				+ biz_msgid + ", pmtid_instrid=" + pmtid_instrid + ", pmtid_endtoendid=" + pmtid_endtoendid
				+ ", pmtid_uetr=" + pmtid_uetr + ", intrbksttlmdt=" + intrbksttlmdt + ", intrbksttlmamt_ccy="
				+ intrbksttlmamt_ccy + ", intrbksttlmamt=" + intrbksttlmamt + ", sender_instgagt_bicfi="
				+ sender_instgagt_bicfi + ", receiver_instgagt_bicfi=" + receiver_instgagt_bicfi
				+ ", receiver_instgagt1_bicfi=" + receiver_instgagt1_bicfi + ", dbtr_bicfi=" + dbtr_bicfi
				+ ", cdtragt_bicfi=" + cdtragt_bicfi + ", cdtragt_iban=" + cdtragt_iban + ", cdtr_bicfi=" + cdtr_bicfi
				+ ", cdtracct_iban=" + cdtracct_iban + "]";
	}

	

}