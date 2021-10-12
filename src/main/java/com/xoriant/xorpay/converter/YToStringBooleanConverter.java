package com.xoriant.xorpay.converter;

import javax.persistence.AttributeConverter;

import com.xoriant.xorpay.constants.XorConstant;

public class YToStringBooleanConverter implements AttributeConverter<String, Character> {

	@Override
	public Character convertToDatabaseColumn(String value) {
		if (value.equals("true"))
			return XorConstant.STATUS_Y;
		else
			return XorConstant.STATUS_N;
	}

	@Override
	public String convertToEntityAttribute(Character value) {
		if (Character.compare(value, 'Y') == 0) {
			return "true";
		} else
			return "false";
	}

}
