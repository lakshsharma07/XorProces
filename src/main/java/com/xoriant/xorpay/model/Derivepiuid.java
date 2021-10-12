package com.xoriant.xorpay.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Derivepiuid {
  
  //private String paymentMthd;
  private String pmtMtd;
  //private String svcLvlCode;
  private String svcLvlCd;
  //private String dlvryChannlCode;
  private String lclInstmCd;
  
  //private String CntrCode1;
  private String cdtrPstlCtry;
  private String CntrCode2;
  //private String CntrCode3;
  private String dbtrAgntPstlCtry;
  private String CntrCode4;
  private String CntrCode5;
  
}
