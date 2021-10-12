package com.xoriant.xorpay.data.sync.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.data.sync.repo.NrmlzToAggJPARepo;
import com.xoriant.xorpay.excepions.XorpayException;
import com.xoriant.xorpay.pojo.ConfigPojo;

@Service
public class NrmlzToAggLoaderService {

	private final Logger logger = LoggerFactory.getLogger(NrmlzToAggLoaderService.class);

	@Autowired
	private NrmlzToAggJPARepo jpaRepo;

	public boolean loadNrmToAgg(String processId, ConfigPojo configPojo) throws XorpayException {
		jpaRepo.startLoadNrmToAggProcess(processId, configPojo);
		return true;
	}
}
