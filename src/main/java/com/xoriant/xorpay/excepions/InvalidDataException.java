package com.xoriant.xorpay.excepions;

public class InvalidDataException extends XorpayRuntimeException
{

	public InvalidDataException(String message) {
		super(message);
	}
	
	public InvalidDataException(String message, Throwable exception) {
		super(message, exception);
	}

	private static final long serialVersionUID = 1L;
}
