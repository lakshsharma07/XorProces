package com.xoriant.xorpay.converter;

import javax.persistence.AttributeConverter;

public class BooleanToStringConverter implements AttributeConverter<Boolean, String>{

	@Override
	public String convertToDatabaseColumn(Boolean value) {
		 return (value != null && value) ? "true" : "false"; 
	}

	@Override
	public Boolean convertToEntityAttribute(String value) {
		return "true".equals(value);
	}

}
