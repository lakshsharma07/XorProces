package com.xoriant.xorpay.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.xoriant.xorpay.constants.TagConstants;
import com.xoriant.xorpay.pojo001.PostalAddress6;

@Component
public class CustomTagPstlAdr {
	private static final Logger logger = LoggerFactory.getLogger(CustomTagPstlAdr.class);

	public void trimAddrLine35Char(List<String> addrList, PostalAddress6 postalAddress, List<String> updatedAddrList) {
		logger.info("trimAddrLine35Char() Start");
		for (String strAddrList : addrList) {
			logger.info("trimAddrLine35Char() Start" + strAddrList.length());
			if (strAddrList.length() > TagConstants.INT_MAX_LENGTH) {
				updatedAddrList.add(strAddrList.substring(0, TagConstants.INT_MAX_LENGTH));
			} else {
				updatedAddrList.add(strAddrList);
			}
		}
		postalAddress.getAdrLine().clear();
		for (String updtAddrList : updatedAddrList) {
			postalAddress.getAdrLine().add(updtAddrList);
		}
	}

	public void trimAddrLine10Char(List<String> addrList, PostalAddress6 postalAddress, List<String> updatedAddrList) {
		for (String strAddrList : addrList) {
			if (strAddrList.length() > TagConstants.INT_MAX_LENGTH_10) {
				updatedAddrList.add(strAddrList.substring(0, TagConstants.INT_MAX_LENGTH_10));
			} else {
				updatedAddrList.add(strAddrList);
			}
		}
		postalAddress.getAdrLine().clear();
		for (String updtAddrList : updatedAddrList) {
			postalAddress.getAdrLine().add(updtAddrList);
		}
	}

}
