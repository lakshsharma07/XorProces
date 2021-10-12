package com.xoriant.xorpay.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.entity.ConfigEntity;
import com.xoriant.xorpay.entity.PaymentAksDetailEntity;
import com.xoriant.xorpay.entity.PaymentBatchAksDetailEntity;
import com.xoriant.xorpay.repo.AggregatedPaymentRepo;
import com.xoriant.xorpay.repo.PaymentAckDetailsRepo;
import com.xoriant.xorpay.repo.PaymentBatchAckDetailsRepo;
import com.xoriant.xorpay.repo.XMLPaymentDetailsRepo;

@Service
public class PaymentXMLService {

	@Autowired
	private XMLPaymentDetailsRepo xmlPaymentDetailsRepo;

	@Autowired
	private PaymentBatchAckDetailsRepo paymentBatchAckDetailsRepo;

	@Autowired
	private PaymentAckDetailsRepo paymentAckDetailsRepo;

	@Autowired
	private AggregatedPaymentRepo xmlTagRepository;

	@Autowired
	private XorConfigService xorConfigService;

	private boolean flag;

	private static final Logger logger = LoggerFactory.getLogger(PaymentXMLService.class);

	public List<String> getOUDetailsList() {
		return xmlPaymentDetailsRepo.getDistinctOU();
	}

	public Map<String, List<String>> getDistinctMessageId() {
		Map<String, List<String>> hmMap = new HashMap<>();

		List<String> ls = new ArrayList<>();
		ls.addAll(xmlTagRepository.findDistinctMessageId());
		// ls.addAll(xmlTagRepository.findDistinctEndToEndId());

		hmMap.put("EndToEndId", ls);
		return hmMap;
	}

	public ConfigEntity getEncryptionConfig(String configName, String configStatus) {
		return xorConfigService.findByConfigNameAndConfigStatus(configName, configStatus);
	}

	public void savePaymentBatchDetails(PaymentBatchAksDetailEntity paymentBatchAksDetail) {
		paymentBatchAckDetailsRepo.save(paymentBatchAksDetail);
	}

	public void savePaymentBatchDetails(List<PaymentBatchAksDetailEntity> paymentBatchAksDetail) {
		paymentBatchAckDetailsRepo.saveAll(paymentBatchAksDetail);
	}

	public void savePaymentAckDetails(PaymentAksDetailEntity paymentAksDetail) {
		paymentAckDetailsRepo.save(paymentAksDetail);
	}

	public void savePaymentAckDetails(List<PaymentAksDetailEntity> paymentAksDetail) {
		paymentAckDetailsRepo.saveAll(paymentAksDetail);
	}

	public void savePaymentAckDetails(String internalUUID, String endToEndId, String paymentcomments) {

		List<PaymentAksDetailEntity> obj = paymentAckDetailsRepo.findByInternalUuidAndPaymentNumber(internalUUID,
				endToEndId);
		logger.info("obj " + obj.size());
		flag = false;
		List<PaymentAksDetailEntity> objE = obj.stream().map(e -> {
			if (null != e.getAddInfo() && !e.getAddInfo().contains(paymentcomments)) {
				flag = true;
				e.setAddInfo(e.getAddInfo() + " " + paymentcomments);
			} else {
				flag = true;
				e.setAddInfo(paymentcomments);
			}
			return e;
		}).collect(Collectors.toList());
		if (flag) {
			paymentAckDetailsRepo.saveAll(objE);
		}
	}

}
