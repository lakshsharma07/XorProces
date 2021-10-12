package com.xoriant.xorpay.thread.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.constants.XorThreadConstant;
import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.entity.DashboardEntity;
import com.xoriant.xorpay.entity.PaymentGeneratedEntity;
import com.xoriant.xorpay.entity.XpScheduleStatusEntity;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.pojo.DataProcessAuditPOJO;
import com.xoriant.xorpay.repo.DashboardRepo;
import com.xoriant.xorpay.repo.XpSchedulStatusRepo;
import com.xoriant.xorpay.service.MOCKPaymentService;

@Service
public class XorExecutor {

	@Autowired
	private MOCKPaymentService mockService;

	@Autowired
	private XpSchedulStatusRepo xpScheduleRepo;

	public static ReentrantLock rel = new ReentrantLock();
	public static List<DashboardEntity> list = new ArrayList<>();
	private static final Logger logger = LoggerFactory.getLogger(XorExecutor.class);
	private String dealay;

	public long MOCK_INTERVAL;

	public void executeMockService(String mgId, List<AggregatedPaymentEntity> xmlTagEntityList, String mockType,
			String mockPaymentType, ConfigPojo configPojo) {
		List<XpScheduleStatusEntity> screpo = xpScheduleRepo.findByStatusAndRequestNameAndScheduleAndEnv(
				XorConstant.SCHEDULED, mockType, XorConstant.ENABLE, configPojo.getCONFIG_ENV());
		if (null != screpo && !screpo.isEmpty()) {
			dealay = screpo.get(0).getFrequency();
			Runnable runnableTask = () -> {
				List<AggregatedPaymentEntity> xmlTagEntityListtmp = new ArrayList<>();
				String mgIdtmp = mgId;
				String mockTypetmp = mockType;

				xmlTagEntityListtmp.addAll(xmlTagEntityList);
				delaywith(dealay);
				logger.info("creating mock file for " + mockTypetmp+ "   "+ mockPaymentType);
				// String tmpFolder = UUID.randomUUID().toString();
				mockService.createACKMockFile(mgIdtmp, xmlTagEntityListtmp, mockTypetmp, mockPaymentType, configPojo);
				logger.info("Mock file created");
			};
			if (mockType.equals(XorConstant.MOCK_BATCH)) {
				XorThreadConstant.batchExecutor.execute(runnableTask);
			} else if (mockType.equals(XorConstant.MOCK_PAYMENT)) {
				XorThreadConstant.paymentExecutor.execute(runnableTask);
			}
		} else {
			logger.info("MOCK :: PLease set delay for Mock file");
		}

	}

	public void excuteStaticsUpdate(DashboardEntity ackEntity, DashboardRepo dashboardRepo, Integer sourceSystem,
			DataProcessAuditPOJO audit, PaymentGeneratedEntity payGenEntity, String processId) {

		XorThreadConstant.staticsExecutor.execute(new DashboardRunnable(rel, "Update statics ", ackEntity,
				dashboardRepo, sourceSystem, audit, payGenEntity, processId));
	}

	private void delaywith(String dealay) {

		String[] fr = dealay.split(" ");
		String unit = fr[1];
		Integer val = Integer.valueOf(fr[0]);
		switch (unit) {
		case XorConstant.SEC:
			MOCK_INTERVAL = TimeUnit.SECONDS.toMillis(val);
			break;
		case XorConstant.MIN:
			MOCK_INTERVAL = TimeUnit.MINUTES.toMillis(val);
			break;
		case XorConstant.HOUR:
			MOCK_INTERVAL = TimeUnit.HOURS.toMillis(val);
			break;
		case XorConstant.DAY:
			MOCK_INTERVAL = TimeUnit.DAYS.toMillis(val);
			break;
		}

		try {
			Thread.sleep(MOCK_INTERVAL);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
