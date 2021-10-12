package com.xoriant.xorpay.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.entity.DashboardEntity;
import com.xoriant.xorpay.entity.PaymentGeneratedEntity;
import com.xoriant.xorpay.pojo.DataProcessAuditPOJO;
import com.xoriant.xorpay.repo.DashboardRepo;
import com.xoriant.xorpay.thread.service.XorExecutor;

@Service
public class DashboardDetailsService {

	@Autowired
	private DashboardRepo dashboardRepo;

	@Autowired
	private XorExecutor xorExecutor;

	private static final Logger logger = LoggerFactory.getLogger(DashboardDetailsService.class);

	public void updateDashboardStatics(Integer sourceSystem, Integer size, String staticType,
			DataProcessAuditPOJO audit, PaymentGeneratedEntity payGenEntity, String processId) {

		logger.info("update astatus " + size);
		if (staticType.equals(XorConstant.ACKNOWLEDGED) || staticType.equals(XorConstant.ACCEPTED)
				|| staticType.equals(XorConstant.REJECTED) || staticType.equals(XorConstant.AUDIT)
				|| staticType.equals(XorConstant.PAYMENTS) || staticType.equals(XorConstant.TRANSLATED)
				|| staticType.equals(XorConstant.FAILED)) {
			DashboardEntity ackEntity = null;

			switch (staticType) {
			case XorConstant.AUDIT:

				break;
			case XorConstant.PAYMENTS:
				break;
			default:
				ackEntity = new DashboardEntity();
				ackEntity.setRecordCount(size);
				ackEntity.setDataStage(staticType);
				ackEntity.setSourceSysId(sourceSystem);
				break;
			}
			xorExecutor.excuteStaticsUpdate(ackEntity, dashboardRepo, sourceSystem, audit, payGenEntity, processId);

		}

	}

}
