//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.09.28 at 09:43:14 PM IST 
//


package com.xoriant.xorpay.pojo001;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ChequeDelivery1Code.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ChequeDelivery1Code">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="MLDB"/>
 *     &lt;enumeration value="MLCD"/>
 *     &lt;enumeration value="MLFA"/>
 *     &lt;enumeration value="CRDB"/>
 *     &lt;enumeration value="CRCD"/>
 *     &lt;enumeration value="CRFA"/>
 *     &lt;enumeration value="PUDB"/>
 *     &lt;enumeration value="PUCD"/>
 *     &lt;enumeration value="PUFA"/>
 *     &lt;enumeration value="RGDB"/>
 *     &lt;enumeration value="RGCD"/>
 *     &lt;enumeration value="RGFA"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ChequeDelivery1Code")
@XmlEnum
public enum ChequeDelivery1Code {

    MLDB,
    MLCD,
    MLFA,
    CRDB,
    CRCD,
    CRFA,
    PUDB,
    PUCD,
    PUFA,
    RGDB,
    RGCD,
    RGFA;

    public String value() {
        return name();
    }

    public static ChequeDelivery1Code fromValue(String v) {
        return valueOf(v);
    }

}
