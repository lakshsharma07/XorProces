package com.xoriant.xorpay.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.stereotype.Component;

@Component
public class Utility {

  public static String formatDate_(Date date) throws ParseException {
    // Date fromdate=null;

    String formattedDate = null;

    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
    // DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z
    // yyyy");
    Date fDate = outputFormat.parse(outputFormat.format(date));
    formattedDate = outputFormat.format(fDate);

    // fromdate =
    // outputFormat.parse(outputFormat.format(payment.getFromDate()));
    // Date fromdate = outputFormat.parse(formattedDate);
    // logger.info(fromdate.toString());
    // Date todate =
    // outputFormat.parse(outputFormat.format(payment.getToDate()));
    // payment.setFromDate(fromdate);
    // payment.setToDate(todate);
    return formattedDate;
  };

  public static Date convertToDate(XMLGregorianCalendar calendar) throws DatatypeConfigurationException {
    GregorianCalendar cal = new GregorianCalendar();
    calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
    Date date = calendar.toGregorianCalendar().getTime();
    return date;
  }

  public static String getFromDate(Date date) {
    return getFromDateString(date) + " 00:00:00";
  }

  public static String getToDate(Date date) {
    return getFromDateString(date) + " 23:59:59";
  }

  public static String getFromDateString(Date date) {
    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    int month = localDate.getMonthValue();
    StringBuilder monthVal = new StringBuilder(""+month);
    if (month < 10) {
      monthVal = new StringBuilder("0" + month);

    }
    int day = localDate.getDayOfMonth();
    StringBuilder dayVal = new StringBuilder(""+day);
    if (day < 10) {
      dayVal = new StringBuilder("0" + day);
    }
    return localDate.getYear() + "-" + monthVal + "-" + dayVal;
  }
}
