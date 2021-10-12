package com.xoriant.xorpay.data.sync.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.data.sync.repo.StgToNrmlzJPARepo;
import com.xoriant.xorpay.excepions.XorpayException;
import com.xoriant.xorpay.pojo.ConfigPojo;

@Service
public class StgToNrmlzLoaderService {

	private final Logger logger = LoggerFactory.getLogger(StgToNrmlzLoaderService.class);

	@Autowired
	private StgToNrmlzJPARepo jpaRepo;
	public boolean loadStgToNrml(String processId, ConfigPojo configPojo, boolean isToReload) throws XorpayException {
		try {
			Map<String, String> batchUUIDMap = new HashMap<>();
			jpaRepo.stgToNrmlzPreProcess(processId, configPojo, isToReload, batchUUIDMap);
			jpaRepo.stgToNrmlzProcess(processId, configPojo, isToReload, batchUUIDMap);
			return true;
		} catch (Exception e) {
			logger.error("LoadSTGToNrm Exception", e);
			throw e;
		}
	}
}
