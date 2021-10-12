package com.xoriant.xorpay.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.ConfigConstants;
import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.entity.AggregatedPaymentEntity;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.repo.AggregatedPaymentRepo;
import com.xoriant.xorpay.thread.service.XorExecutor;

@Service
public class PaymentResponseService {

	private final Logger logger = LoggerFactory.getLogger(PaymentResponseService.class);
	@Autowired
	public EntityManager entityManager;

	@Autowired
	private XorExecutor xorExecutor;

	@Autowired
	private AggregatedPaymentRepo apAggregatedPaymentRepo;

	@Autowired
	private ReadXmlFiles readResponse;

	public void startProcess(String processid) {
		ConfigConstants.configMap.entrySet().forEach(config -> {
			ConfigPojo configPojo = config.getValue();
			try {
				responseProcess(processid, configPojo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public void responseProcess(String processId, ConfigPojo configPojo) {

		entityManager = entityManager.getEntityManagerFactory().createEntityManager();
		Session session = entityManager.unwrap(Session.class);
		Transaction tr = session.beginTransaction();
		session.doWork(new Work() {

			@Override
			public void execute(Connection connections) throws SQLException {
				try (Connection connection = connections) {
					connection.setAutoCommit(true);
					try {
						startProcessingPaymentResponse(processId, configPojo);
					} catch (Exception e) {
						logger.error("Exception Resonse Process", e);
						throw e;
					}

				} catch (Exception e) {
					logger.error("Exception Resonse Process", e);
					throw new RuntimeException(e);
				}
			}
		});
	}

	public void startProcessingPaymentResponse(String processid, ConfigPojo configPojo) throws Exception {

		logger.info("Response to mock " + ConfigConstants.configSys.isToMock());
		if (ConfigConstants.configSys.isToMock()) {
			List<AggregatedPaymentEntity> xmlTagEntityListAll = apAggregatedPaymentRepo
					.findByXmlStatusAndSourceSystem(XorConstant.STATUS_Y, configPojo.getSOURCE_SYS_ID());

			Map<String, List<AggregatedPaymentEntity>> maMap = xmlTagEntityListAll.stream()
					.collect(Collectors.groupingBy(AggregatedPaymentEntity::getMsgId,
							Collectors.mapping(Function.identity(), Collectors.toList())));

			if (null != maMap) {
				for (Entry<String, List<AggregatedPaymentEntity>> obj : maMap.entrySet()) {
					String messageId = obj.getKey();
					List<AggregatedPaymentEntity> xmlTagEntityList = obj.getValue();
					logger.info("Response processing for messageId  - " + messageId + "  with size of "
							+ xmlTagEntityList.size());
					logger.info("is mock type ACK "+ ConfigConstants.configSys.isMockTypeACK());
					if (ConfigConstants.configSys.isMockTypeACK()) {
						String mockPaymentType = XorConstant.MOCK_ACCP;
						if (XorConstant.isCLS) {
							mockPaymentType = XorConstant.MOCK_CLS_ACSC;
						}
						xorExecutor.executeMockService(messageId, xmlTagEntityList, XorConstant.MOCK_BATCH,
								mockPaymentType, configPojo);
						xorExecutor.executeMockService(messageId, xmlTagEntityList, XorConstant.MOCK_PAYMENT,
								mockPaymentType, configPojo);
					} else {
						xorExecutor.executeMockService(messageId, xmlTagEntityList, XorConstant.MOCK_BATCH,
								XorConstant.MOCK_RJCT, configPojo);
						xorExecutor.executeMockService(messageId, xmlTagEntityList, XorConstant.MOCK_PAYMENT,
								XorConstant.MOCK_RJCT, configPojo);
					}

					processResponse(configPojo);
				}
			} else {
				logger.info("No Payment to process");
			}
		} else {
			processResponse(configPojo);
		}
	}

	private void processResponse(ConfigPojo configPojo) throws Exception {
		for (int i = 0; i < 10; i++) {
			logger.info("Will check for reponse processign in 15 msec");
			try {
				Thread.sleep(2000);// 90, 10:00 //10 15/20 mnt
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			readResponse.readPain002Xmlfiles(configPojo);
		}
	}

}
