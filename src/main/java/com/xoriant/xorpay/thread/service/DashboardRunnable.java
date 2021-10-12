package com.xoriant.xorpay.thread.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xoriant.xorpay.entity.DashboardEntity;
import com.xoriant.xorpay.entity.PaymentGeneratedEntity;
import com.xoriant.xorpay.pojo.DataProcessAuditPOJO;
import com.xoriant.xorpay.repo.DashboardRepo;
import com.xoriant.xorpay.service.XMLStructureService;

class DashboardRunnable implements Runnable {
	private final Logger logger = LoggerFactory.getLogger(DashboardRunnable.class);
	String name;
	ReentrantLock re;
	DashboardEntity ackEntity;
	DashboardRepo dashboardRepo;
	Integer sourceSystem;
	DataProcessAuditPOJO audit;
	String processId;
	PaymentGeneratedEntity payGenEntity;
	public static Map<String, DashboardEntity> statudMap = new ConcurrentHashMap<>();
	public static Map<String, List<DataProcessAuditPOJO>> addProcessBatchAuditMessage = new ConcurrentHashMap<>();
	public static List<PaymentGeneratedEntity> paymentGeneratedEntity = new CopyOnWriteArrayList<>();

	public DashboardRunnable(ReentrantLock rl, String n, DashboardEntity ackEntity, DashboardRepo dashboardRepo,
			Integer sourceSystem, DataProcessAuditPOJO audit, PaymentGeneratedEntity payGenEntity, String processId) {
		re = rl;
		name = n;
		this.ackEntity = ackEntity;
		this.dashboardRepo = dashboardRepo;
		this.sourceSystem = sourceSystem;
		this.audit = audit;
		this.processId = processId;
		this.payGenEntity = payGenEntity;
	}

	public void run() {
		boolean done = false;
		while (!done) {
			boolean ans = re.tryLock();
			// Returns True if lock is free
			if (ans) {
				try {
					re.lock();
					try {
						if (null != ackEntity) {
							if (statudMap.containsKey(ackEntity.getDataStage())) {
								DashboardEntity eny = statudMap.get(ackEntity.getDataStage());
								eny.setRecordCount(eny.getRecordCount() + ackEntity.getRecordCount());
								statudMap.put(ackEntity.getDataStage(), eny);
							} else {
								statudMap.put(ackEntity.getDataStage(), ackEntity);
							}
						} else if (null != audit) {
							if (addProcessBatchAuditMessage.containsKey(processId)) {
								List<DataProcessAuditPOJO> addBatchAuditMessage = addProcessBatchAuditMessage
										.get(processId);
								addBatchAuditMessage.add(audit);
								addProcessBatchAuditMessage.put(processId, addBatchAuditMessage);

							} else {
								List<DataProcessAuditPOJO> addBatchAuditMessage = new ArrayList<>(1);
								addBatchAuditMessage.add(audit);
								addProcessBatchAuditMessage.put(processId, addBatchAuditMessage);
							}
						} else if (null != payGenEntity) {
							paymentGeneratedEntity.add(payGenEntity);
						}
					} finally {
						// Inner lock release
						re.unlock();
					}
					// logger.info(" work done");
					done = true;
				} finally {
					re.unlock();
				}
			} else {
				logger.info("task name - " + name + " waiting for lock");
			}
		}
	}
}
