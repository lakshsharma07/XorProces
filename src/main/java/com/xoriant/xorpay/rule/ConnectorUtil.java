package com.xoriant.xorpay.rule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xoriant.xorpay.constants.XorConstant;

public final class ConnectorUtil {

	public static boolean isAlphaNumeric(String str) {
		// String regex = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]+$";
		// String regex = "^[a-zA-Z0-9]+$";
		String regex = "^[a-zA-Z0-9\\s]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);

		return m.matches();
	}

	public static boolean isNumeric(String strNum) {

		String regex = "-?\\d+(\\.\\d+)?";
		Pattern p = Pattern.compile(regex);

		if (strNum == null) {
			return false;
		}
		return p.matcher(strNum).matches();
	}

	public static boolean isManditory(String strNum) {
		if (strNum == null || strNum == "") {
			return false;
		}
		return true;
	}

	public static String convertDate(String sDate, String format) throws ParseException {

		String resultDate = "";
		if (format.equals(XorConstant.YYYY_MM_DDTHH_MM_SS_SSS)) {
			resultDate = sDate;
		} else {
			Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(sDate);
			SimpleDateFormat formattedDate = new SimpleDateFormat(format);
			String dateFormatted = formattedDate.format(date1);
			resultDate = dateFormatted;
		}
		return resultDate;
	}

	public static boolean lengthCheck(String str, int min, int max) {
		if (str.length() >= min && str.length() <= max) {
			return true;
		}
		return false;

	}

	public static String addPrefix(String str, String prefix) {

		return prefix + str;

	}
	public static String subString(int maxLength, String colValue) {
		if (colValue.trim().length() > maxLength) {
			colValue = colValue.substring(0, maxLength);
		}
		return colValue;
	}

}
