package com.xoriant.xorpay.adapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

	private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public LocalDateTime unmarshal(String v) throws Exception {
		return LocalDateTime.parse(v, dateFormat);
	}

	public String marshal(LocalDateTime v) throws Exception {
		return v.format(dateFormat);
	}
}