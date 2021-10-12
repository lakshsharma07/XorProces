package com.xoriant.xorpay.data.sync.repo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.entity.DataProcessAuditEntity;
import com.xoriant.xorpay.pojo.DataProcessAudit;
import com.xoriant.xorpay.pojo.DataProcessAuditPOJO;
import com.xoriant.xorpay.pojo.DataProcessStageAuditPOJO;
import com.xoriant.xorpay.repo.DataProcessAuditRepo;

@Repository
@Transactional
public class AuditJPARepo {

	@Autowired
	private DataProcessAuditRepo dataProcessAuditRepo;

	public static final String STGTONRM = "STGTONRM";
	public static final String NRMTOAGG = "NRMTOAGG";
	public static final String PAYMLOAD = "PAYMLOAD";
	public static final String PAYMGEN = "PAYMGEN";

	public static final String SUCCESSFULLY = "Successfully";

	public static final String RULEENGINE = "RULEENGINE";

	public static final String VALIDATION_FAILED = "Validation Failed";

	public static final String VALIDATION_SUCCESSFULL = "Validation Successfully";
	
	public static String PROCESSING_NOT_ALLOWED = "PROCESSING_NOT_ALLOWED";
	public static String PROCESSED = "PROCESSED";
	public static String FAILED = "FAILED";

	List<DataProcessAuditPOJO> auditlog = null;
	public static List<DataProcessAuditPOJO> batchErrMessageList = new ArrayList<>(1);

	private final Logger logger = LoggerFactory.getLogger(AuditJPARepo.class);

	public void audiLogger(String processId, Integer recordCount, String level) {
		try {
			if (!batchErrMessageList.isEmpty()) {
				addAudit(processId, recordCount, AuditJPARepo.PROCESSING_NOT_ALLOWED, level);
				batchErrMessageList.clear();
			}
		} catch (Exception ep) {
			ep.printStackTrace();
			logger.info("Audit loggin issue");
		}
	}

	public void addAudit(String processId, Integer recordCount, String status, String level) {
		ObjectMapper objectMapper = new ObjectMapper();
		logger.info("processid "+ processId);
		DataProcessAuditEntity dpae = dataProcessAuditRepo.findByProcessId(processId);
		DataProcessAudit dataProcessAudit = null;
		if (null == dpae) {
			dpae = new DataProcessAuditEntity();
			dpae.setProcessId(processId);

			dpae.setCreatedBy(XorConstant.SYSTEM);
			dpae.setCreationDate(LocalDateTime.now());
			dpae.setLastUpdatedBy(XorConstant.SYSTEM);
			dpae.setLastUpdateDate(LocalDateTime.now());
			dataProcessAudit = getInitializedObject();

		} else {
			dpae.setLastUpdatedBy(XorConstant.SYSTEM);
			dpae.setLastUpdateDate(LocalDateTime.now());

			String audit = dpae.getAudit();
			if (!audit.trim().isEmpty()) {
				try {
					dataProcessAudit = objectMapper.readValue(audit, DataProcessAudit.class);
				} catch (JsonProcessingException e1) {
					e1.printStackTrace();
					dataProcessAudit = getInitializedObject();
				}
			}
		}
		
		switch (level) {
		case AuditJPARepo.STGTONRM:
			auditlog = dataProcessAudit.getStagingToNrmalizeAudit().getRecordStatus();
			break;
		case AuditJPARepo.NRMTOAGG:
			auditlog = dataProcessAudit.getNrmalizeToAggregatedAudit().getRecordStatus();
			break;
		case AuditJPARepo.PAYMLOAD:
			auditlog = dataProcessAudit.getLoadPaymentDetailsAudit().getRecordStatus();
			break;
		case AuditJPARepo.RULEENGINE:
			auditlog = dataProcessAudit.getRuleEngineAudit().getRecordStatus();
			break;
		case AuditJPARepo.PAYMGEN:
			auditlog = dataProcessAudit.getPaymentGeratedAudit().getRecordStatus();
			break;
		}
		batchErrMessageList.stream().forEach(e -> {
			auditlog.add(e);
		});
		
		switch (level) {
		case AuditJPARepo.STGTONRM:
			if (null != recordCount) {
				dataProcessAudit.setTotalRecordToProcess(recordCount);
				dataProcessAudit.getStagingToNrmalizeAudit().setTotalProcessed(recordCount);
			}
			dataProcessAudit.getStagingToNrmalizeAudit().setRecordStatus(auditlog);
			break;
		case AuditJPARepo.NRMTOAGG:
			if (null != recordCount) {
				dataProcessAudit.getNrmalizeToAggregatedAudit().setTotalProcessed(recordCount);
			}
			dataProcessAudit.getNrmalizeToAggregatedAudit().setRecordStatus(auditlog);
			break;
		case AuditJPARepo.PAYMLOAD:
			if (null != recordCount) {
				dataProcessAudit.getLoadPaymentDetailsAudit().setTotalProcessed(recordCount);
			}
			dataProcessAudit.getLoadPaymentDetailsAudit().setRecordStatus(auditlog);
			break;
		case AuditJPARepo.RULEENGINE:
			if (null != recordCount) {
				dataProcessAudit.getRuleEngineAudit().setTotalProcessed(recordCount);
			}
			dataProcessAudit.getRuleEngineAudit().setRecordStatus(auditlog);
			break;
		case AuditJPARepo.PAYMGEN:
			if (null != recordCount) {
				dataProcessAudit.getPaymentGeratedAudit().setTotalProcessed(recordCount);
			}
			dataProcessAudit.getPaymentGeratedAudit().setRecordStatus(auditlog);
			break;
		}

		try {
			dpae.setAudit(objectMapper.writeValueAsString(dataProcessAudit));
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}

		dataProcessAuditRepo.save(dpae);

	}

	public void addBatchAuditMessage(String internalId, String batchId, String paymentId, String errMessage, String status) {
		batchErrMessageList
				.add(new DataProcessAuditPOJO(internalId, batchId, paymentId, status, errMessage, LocalDateTime.now().toString()));
	}

	private DataProcessAudit getInitializedObject() {
		Integer totalProcessed = new Integer(0);
		DataProcessStageAuditPOJO stagingToNrmalizeAudit= new DataProcessStageAuditPOJO();
		DataProcessStageAuditPOJO nrmalizeToAggregatedAudit= new DataProcessStageAuditPOJO();
		DataProcessStageAuditPOJO loadPaymentDetailsAudit= new DataProcessStageAuditPOJO();
		DataProcessStageAuditPOJO paymentGeratedAudit= new DataProcessStageAuditPOJO();
		DataProcessStageAuditPOJO ruleEngineAudit= new DataProcessStageAuditPOJO();
		
		List<DataProcessAuditPOJO> stagingToNrmalizeAuditL = new ArrayList<>();
		stagingToNrmalizeAudit.setRecordStatus(stagingToNrmalizeAuditL);
		List<DataProcessAuditPOJO> nrmalizeToAggregatedAuditL = new ArrayList<>();
		nrmalizeToAggregatedAudit.setRecordStatus(nrmalizeToAggregatedAuditL);
		List<DataProcessAuditPOJO> loadPaymentDetailsAuditL = new ArrayList<>();
		loadPaymentDetailsAudit.setRecordStatus(loadPaymentDetailsAuditL);
		List<DataProcessAuditPOJO> paymentGeratedAuditL = new ArrayList<>();
		paymentGeratedAudit.setRecordStatus(paymentGeratedAuditL);
		List<DataProcessAuditPOJO> ruleEngineAuditL = new ArrayList<>();
		ruleEngineAudit.setRecordStatus(ruleEngineAuditL);
		return new DataProcessAudit(totalProcessed, stagingToNrmalizeAudit, nrmalizeToAggregatedAudit,
				loadPaymentDetailsAudit, paymentGeratedAudit, ruleEngineAudit);
	}

}
