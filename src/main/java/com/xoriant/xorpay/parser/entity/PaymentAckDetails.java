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
public class PaymentAckDetails implements Keep{
  
  private String sender_bicfi;
  private String receiver_bicfi;
  private String orgnlMsgId;
  private String orgnlInstrId;
  private String orgnlEndToEndId;
  private String orgnlUETR;
  private String txSts;
  private String creDt;
 
}
