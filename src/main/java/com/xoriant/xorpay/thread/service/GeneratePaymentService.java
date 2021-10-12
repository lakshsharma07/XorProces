package com.xoriant.xorpay.thread.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;

import org.hibernate.internal.build.AllowSysOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.ConfigConstants;
import com.xoriant.xorpay.constants.PAIN001Constant;
import com.xoriant.xorpay.constants.XorThreadConstant;
import com.xoriant.xorpay.excepions.CustomError;
import com.xoriant.xorpay.excepions.InvalidDataException;
import com.xoriant.xorpay.excepions.XorpayException;
import com.xoriant.xorpay.excepions.XorpayRuntimeException;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.rule.RulesEngine;
import com.xoriant.xorpay.service.ExportService;
import com.xoriant.xorpay.service.MappingService;

@Service
public class GeneratePaymentService {

	private final Logger logger = LoggerFactory.getLogger(GeneratePaymentService.class);
	@Autowired
	public EntityManager entityManager;
	@Autowired
	MappingService mappingService;
	@Autowired
	private ExportService exportService;
	@Autowired
	private RulesEngine rulesEngine;

	public void startProcess(String processid) {
		ConfigConstants.configMap.entrySet().forEach(config -> {
			ConfigPojo configPojo = config.getValue();
			try {
				startGeneratingXMLFromStructure(processid, configPojo);
			} catch (Exception e) {
				logger.error("Exception", e);
				throw new RuntimeException(e);
			}
		});
	}

	public void startGeneratingXMLFromStructure(String processid, ConfigPojo configPojo) throws Exception {

		Map<String, Map<String, Map<String, List<Map<Integer, String>>>>> rmtMap = new HashMap<>();
		Map<String, Set<Integer>> supressTagSet = new HashMap<>();
		Set<String> addressTagSet = new HashSet<>();
		rulesEngine.processRules(processid, supressTagSet, addressTagSet, configPojo);

		supressTagSet.entrySet().stream().forEach(on -> {
			logger.info("Profile name " + on.getKey());
			logger.info("supr tag " + on.getValue().toString());

		});
		addressTagSet.stream().forEach(e -> {
			logger.info("Address line set " + e);
		});

		Map<String, List<Map<Integer, String>>> aggDataMap = mappingService.getMapiing(null, rmtMap, configPojo);

		logger.info("XML generation start processing for --> " + aggDataMap.size());

		TreeSet<String> msgIdSet = new TreeSet<>(aggDataMap.keySet());

		exportService.checkFolders(configPojo);
		msgIdSet.stream().forEach(messageId -> {

			Runnable runnableTask = () -> {
				logger.info("XML generation start for " + messageId);
				List<Map<Integer, String>> aggDataMapList = aggDataMap.get(messageId);
				Map<String, Map<String, List<Map<Integer, String>>>> rmtIfoForMsgId = rmtMap.get(messageId);
				if (aggDataMapList.size() == Integer.parseInt(aggDataMapList.get(0).get(PAIN001Constant.NBOFTXS_6))) {
					try {
						String profilename = aggDataMapList.get(0).get(PAIN001Constant.PROFILENAME_914);
						Integer sourceSystem = Integer
								.parseInt(aggDataMapList.get(0).get(PAIN001Constant.SRC_SYSTEM_915).trim());
						exportService.getXMLBlobFromStructure(processid, profilename, messageId, sourceSystem,
								aggDataMapList, rmtIfoForMsgId, supressTagSet,addressTagSet, configPojo);

						logger.info("XML generated for " + messageId);
					} catch (Exception e) {
						logger.error("Exception GeneratingXML", e);
						throw new RuntimeException(e);
					}
				} else {
					logger.info("No of transaction does not match with number of recors for batch " + messageId);
				}
			};
			XorThreadConstant.batchPymentProcess.execute(runnableTask);

		});

	}

}
