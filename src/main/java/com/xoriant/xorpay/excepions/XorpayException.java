package com.xoriant.xorpay.excepions;

public class XorpayException extends Exception {

	private static final long serialVersionUID = 1L;

	public XorpayException(String message) {
		super(message);
	}

	public XorpayException(String message, Throwable exception) {
		super(message, exception);
	}

}
