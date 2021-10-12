package com.xoriant.xorpay.data.sync.services;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.data.sync.entity.ProcessStatusEntity;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.repo.ProcessStatusRepo;

@Service
public class LoaderCommonService {

	private final Logger logger = LoggerFactory.getLogger(LoaderCommonService.class);
	@Autowired
	private ProcessStatusRepo processStatusRepo;

	public ProcessStatusEntity setProcessStatusComplete(ProcessStatusEntity pse, String procRunState) {
		pse.setEndTime(LocalDateTime.now());
		pse.setStatus(procRunState);
		pse = processStatusRepo.save(pse);
		return pse;
	}

	public ProcessStatusEntity setProcessStatusEntityRunning(ProcessStatusEntity pse, int totalRecord,
			String procRunState, String processName, ConfigPojo configPojo) {

		pse = new ProcessStatusEntity(0, LocalDateTime.now(), null, null, null, procRunState, XorConstant.STATUS_Y,
				null, processName, configPojo.getSOURCE_TABLE_NAME(), XorConstant.NRMLZ_PMT_DTLS, "Admin", LocalDateTime.now(),
				"Admin", LocalDateTime.now());

		pse = processStatusRepo.save(pse);
		return pse;
	}
}
