package com.xoriant.xorpay.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.datatype.XMLGregorianCalendar;

/**
 * @author shinde_su
 *
 */
public class Tags {
  
  private String msgId;
  private Date cretionDt;
  
  
  
  public Date getCretionDt() {
    return cretionDt;
  }
  public void setCretionDt(Date cretionDt) {
    this.cretionDt = cretionDt;
  }
  private String nbOfTxs;
  private String InitgNm;
  private String pmtInfId;
  private String pmtMtd;
  private String SvcLvlCd;
  private String LclInstmCd;
  
  private String CtgyPurp;
  
  private BigDecimal CntrlSum;
  
  private String fwdAgntNm;
  
  private Date reqstExDt;
  
  private String dbtrNm;
  
  private String dbtrAccntOthrId;
  
  private String dbtrAgntBic;
  
  private String dbtrAgntPstlCtry;
  
  private String dbtrAgntBrnId;
  
  private String endToEndId;
  
  private BigDecimal instdAmnt;
  
  private String cdtrAgntBic;
  
  private String cdtrAgntNm;
  
  private String cdtrAgntPstlCtry;
  
  private String cdtrAgntBrnId;
  
  private String cdtrNm;
  
  private String cdtrPstlCtry;
  
  private String cdtrAccntOthrId;
  
  private String cdtrAccntNm;
  
  private String purpPrtry;
  
  private String rmtLctnMthd;
  
  private String rmtLctnElctAddr;
  
  private String rmtUstrd;
  
  private String rmtStrd;
  
  private String strdTpCd;
  
  private String strdNb;
  
  private Date strdRtldDt;
  
  
  
  public Date getStrdRtldDt() {
    return strdRtldDt;
  }
  public void setStrdRtldDt(Date strdRtldDt) {
    this.strdRtldDt = strdRtldDt;
  }
  private BigDecimal duePaybAmt;
  
  private BigDecimal rmtdAmt;
  
  
  
  
  public String getCtgyPurp() {
    return CtgyPurp;
  }
  public void setCtgyPurp(String ctgyPurp) {
    CtgyPurp = ctgyPurp;
  }
  public String getCtgyPurpCd() {
    return CtgyPurpCd;
  }
  public void setCtgyPurpCd(String ctgyPurpCd) {
    CtgyPurpCd = ctgyPurpCd;
  }
  private String CtgyPurpCd;
  
  public String getMsgId() {
    return msgId;
  }
  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }
  
  public String getNbOfTxs() {
    return nbOfTxs;
  }
  public void setNbOfTxs(String nbOfTxs) {
    this.nbOfTxs = nbOfTxs;
  }
    public String getInitgNm() {
	return InitgNm;
}
public void setInitgNm(String initgNm) {
	InitgNm = initgNm;
}
	public String getPmtInfId() {
    return pmtInfId;
  }
  public void setPmtInfId(String pmtInfId) {
    this.pmtInfId = pmtInfId;
  }
  public String getPmtMtd() {
    return pmtMtd;
  }
  public void setPmtMtd(String pmtMtd) {
    this.pmtMtd = pmtMtd;
  }
  public String getSvcLvlCd() {
    return SvcLvlCd;
  }
  public void setSvcLvlCd(String svcLvlCd) {
    SvcLvlCd = svcLvlCd;
  }
  public String getLclInstmCd() {
    return LclInstmCd;
  }
  public void setLclInstmCd(String lclInstmCd) {
    LclInstmCd = lclInstmCd;
  }
  
  public BigDecimal getCntrlSum() {
	return CntrlSum;
}
public void setCntrlSum(BigDecimal cntrlSum) {
	CntrlSum = cntrlSum;
}
public String getFwdAgntNm() {
    return fwdAgntNm;
  }
  public void setFwdAgntNm(String fwdAgntNm) {
    this.fwdAgntNm = fwdAgntNm;
  }
  public Date getReqstExDt() {
    return reqstExDt;
  }
  public void setReqstExDt(Date reqstExDt) {
    this.reqstExDt = reqstExDt;
  }
  public String getDbtrNm() {
    return dbtrNm;
  }
  public void setDbtrNm(String dbtrNm) {
    this.dbtrNm = dbtrNm;
  }
  public String getDbtrAccntOthrId() {
    return dbtrAccntOthrId;
  }
  public void setDbtrAccntOthrId(String dbtrAccntOthrId) {
    this.dbtrAccntOthrId = dbtrAccntOthrId;
  }
  public String getDbtrAgntBic() {
    return dbtrAgntBic;
  }
  public void setDbtrAgntBic(String dbtrAgntBic) {
    this.dbtrAgntBic = dbtrAgntBic;
  }
  public String getDbtrAgntPstlCtry() {
    return dbtrAgntPstlCtry;
  }
  public void setDbtrAgntPstlCtry(String dbtrAgntPstlCtry) {
    this.dbtrAgntPstlCtry = dbtrAgntPstlCtry;
  }
  public String getDbtrAgntBrnId() {
    return dbtrAgntBrnId;
  }
  public void setDbtrAgntBrnId(String dbtrAgntBrnId) {
    this.dbtrAgntBrnId = dbtrAgntBrnId;
  }
  public String getEndToEndId() {
    return endToEndId;
  }
  public void setEndToEndId(String endToEndId) {
    this.endToEndId = endToEndId;
  }
  public BigDecimal getInstdAmnt() {
    return instdAmnt;
  }
  public void setInstdAmnt(BigDecimal instdAmnt) {
    this.instdAmnt = instdAmnt;
  }
  public String getCdtrAgntBic() {
    return cdtrAgntBic;
  }
  public void setCdtrAgntBic(String cdtrAgntBic) {
    this.cdtrAgntBic = cdtrAgntBic;
  }
  public String getCdtrAgntNm() {
    return cdtrAgntNm;
  }
  public void setCdtrAgntNm(String cdtrAgntNm) {
    this.cdtrAgntNm = cdtrAgntNm;
  }
  public String getCdtrAgntPstlCtry() {
    return cdtrAgntPstlCtry;
  }
  public void setCdtrAgntPstlCtry(String cdtrAgntPstlCtry) {
    this.cdtrAgntPstlCtry = cdtrAgntPstlCtry;
  }
  public String getCdtrAgntBrnId() {
    return cdtrAgntBrnId;
  }
  public void setCdtrAgntBrnId(String cdtrAgntBrnId) {
    this.cdtrAgntBrnId = cdtrAgntBrnId;
  }
  public String getCdtrNm() {
    return cdtrNm;
  }
  public void setCdtrNm(String cdtrNm) {
    this.cdtrNm = cdtrNm;
  }
  public String getCdtrPstlCtry() {
    return cdtrPstlCtry;
  }
  public void setCdtrPstlCtry(String cdtrPstlCtry) {
    this.cdtrPstlCtry = cdtrPstlCtry;
  }
  public String getCdtrAccntOthrId() {
    return cdtrAccntOthrId;
  }
  public void setCdtrAccntOthrId(String cdtrAccntOthrId) {
    this.cdtrAccntOthrId = cdtrAccntOthrId;
  }
  
  public String getCdtrAccntNm() {
    return cdtrAccntNm;
  }
  public void setCdtrAccntNm(String cdtrAccntNm) {
    this.cdtrAccntNm = cdtrAccntNm;
  }
  public String getPurpPrtry() {
    return purpPrtry;
  }
  public void setPurpPrtry(String purpPrtry) {
    this.purpPrtry = purpPrtry;
  }
  public String getRmtLctnMthd() {
    return rmtLctnMthd;
  }
  public void setRmtLctnMthd(String rmtLctnMthd) {
    this.rmtLctnMthd = rmtLctnMthd;
  }
  public String getRmtLctnElctAddr() {
    return rmtLctnElctAddr;
  }
  public void setRmtLctnElctAddr(String rmtLctnElctAddr) {
    this.rmtLctnElctAddr = rmtLctnElctAddr;
  }
  public String getRmtUstrd() {
    return rmtUstrd;
  }
  public void setRmtUstrd(String rmtUstrd) {
    this.rmtUstrd = rmtUstrd;
  }
  public String getRmtStrd() {
    return rmtStrd;
  }
  public void setRmtStrd(String rmtStrd) {
    this.rmtStrd = rmtStrd;
  }
  public String getStrdTpCd() {
    return strdTpCd;
  }
  public void setStrdTpCd(String strdTpCd) {
    this.strdTpCd = strdTpCd;
  }
  public String getStrdNb() {
    return strdNb;
  }
  public void setStrdNb(String strdNb) {
    this.strdNb = strdNb;
  }
  public BigDecimal getDuePaybAmt() {
    return duePaybAmt;
  }
  public void setDuePaybAmt(BigDecimal duePaybAmt) {
    this.duePaybAmt = duePaybAmt;
  }
  public BigDecimal getRmtdAmt() {
    return rmtdAmt;
  }
  public void setRmtdAmt(BigDecimal rmtdAmt) {
    this.rmtdAmt = rmtdAmt;
  }
  

}
