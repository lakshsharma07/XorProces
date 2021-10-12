package com.xoriant.xorpay.excepions;

public class XorpayRuntimeException extends RuntimeException{
	
	public XorpayRuntimeException(String message)
	{
		super(message);
	}
	
	public XorpayRuntimeException(String message, Throwable exception)
	{
		super(message,exception);
	}

}
