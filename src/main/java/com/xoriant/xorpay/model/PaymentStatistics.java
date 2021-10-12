package com.xoriant.xorpay.model;

import java.io.Serializable;

public class PaymentStatistics implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;
	private String status;
	private Integer cnt;


	public PaymentStatistics() {
	}

	

	public void setStatus(String status) {
		this.status = status;
	}



	public void setCnt(Integer i) {
		this.cnt = i;
	}



	public String getStatus() {
		return status;
	}

	/*
	 * public void setStatus(String status) { this.status = status; }
	 */

	public Integer getCnt() {
		return cnt;
	}

	
	/*
	 * public void setCnt(Long cnt) { this.cnt = cnt; }
	 */

	@Override
	public String toString() {
		return "PaymentStatistics [status=" + status + ", cnt=" + cnt + "]";
	}

	
}
