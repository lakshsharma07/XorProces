package com.xoriant.xorpay.data.sync.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.data.sync.repo.LoadPaymentJPARepo;
import com.xoriant.xorpay.excepions.XorpayException;
import com.xoriant.xorpay.pojo.ConfigPojo;

@Service
public class LoadPaymentLoaderService {

	private final Logger logger = LoggerFactory.getLogger(LoadPaymentLoaderService.class);

	@Autowired
	private LoadPaymentJPARepo jpaRepo;

	public void loadRequiredDetails(String processId, ConfigPojo configPojo) throws XorpayException {
		jpaRepo.loadRequiredDetails( processId,configPojo);
	}

}
