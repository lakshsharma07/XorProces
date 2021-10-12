package com.xoriant.xorpay.thread.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

//import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.data.sync.repo.AuditJPARepo;
import com.xoriant.xorpay.entity.DashboardEntity;
import com.xoriant.xorpay.entity.PaymentGeneratedEntity;
import com.xoriant.xorpay.pojo.DataProcessAuditPOJO;
import com.xoriant.xorpay.repo.DashboardRepo;
import com.xoriant.xorpay.repo.PaymentGeneratedRepo;

@EnableScheduling
@Component
public final class XorPayProcedureScheduler implements SchedulingConfigurer {
	private final Logger logger = LoggerFactory.getLogger(XorPayProcedureScheduler.class);

	@Autowired
	DashboardRepo dashboardRepo;

	@Autowired
	AuditJPARepo auditJPARepo;

	@Autowired
	private PaymentGeneratedRepo paymentGeneratedRepo;

	@Bean
	public TaskScheduler taskScheduler() {
		return new ConcurrentTaskScheduler();
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		Runnable runnableST = () -> {
			try {
				if (!DashboardRunnable.statudMap.isEmpty()) {

					List<DashboardEntity> entityList = new ArrayList<>();

					DashboardRunnable.statudMap.entrySet().forEach(e -> {

						String dataStage = e.getKey();
						DashboardEntity entityMap = e.getValue();
						DashboardEntity entity = dashboardRepo.findBySourceSysIdAndDataStage(entityMap.getSourceSysId(),
								entityMap.getDataStage());
						if (null != entity) {
							entity.setRecordCount(entity.getRecordCount() + entityMap.getRecordCount());
						} else {
							entity = new DashboardEntity();
							entity.setSourceSysId(entityMap.getSourceSysId());
							entity.setDataStage(entityMap.getDataStage());
							entity.setRecordCount(entityMap.getRecordCount());
							entity.setCreatedBy(XorConstant.ADMIN_ROLE);
							entity.setCreatedDate(LocalDateTime.now());
							entity.setUpdatedBy(XorConstant.ADMIN_ROLE);
							entity.setUpdatedDate(LocalDateTime.now());
						}
						entity.setInsertTime(LocalDateTime.now());
						entityList.add(entity);

						DashboardRunnable.statudMap.get(dataStage)
								.setRecordCount(DashboardRunnable.statudMap.get(dataStage).getRecordCount()
										- entityMap.getRecordCount());

					});
					if (!entityList.isEmpty()) {
						dashboardRepo.saveAll(entityList);
					}
				}

				if (!DashboardRunnable.addProcessBatchAuditMessage.isEmpty()) {
					DashboardRunnable.addProcessBatchAuditMessage.entrySet().stream().forEach(e -> {
						String processId = e.getKey();
						List<DataProcessAuditPOJO> entityList = e.getValue();
						auditJPARepo.batchErrMessageList = entityList;
						auditJPARepo.audiLogger(processId, null, AuditJPARepo.PAYMGEN);

						DashboardRunnable.addProcessBatchAuditMessage.get(e.getKey()).removeAll(entityList);

					});
				}
				if (!DashboardRunnable.paymentGeneratedEntity.isEmpty()) {
					List<PaymentGeneratedEntity> entityList = DashboardRunnable.paymentGeneratedEntity;
					paymentGeneratedRepo.saveAll(entityList);
					DashboardRunnable.paymentGeneratedEntity.removeAll(entityList);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};

		Trigger triggerST = triggerContext -> {
			Calendar nextExecutionTime = new GregorianCalendar();
			Date lastActualExecutionTime = triggerContext.lastActualExecutionTime();
			nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
			nextExecutionTime.add(Calendar.MILLISECOND, 10000);
			return nextExecutionTime.getTime();
		};
		taskRegistrar.addTriggerTask(runnableST, triggerST);

	}

}
