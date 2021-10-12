package com.xoriant.xorpay.data.sync.services;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.ConfigConstants;
import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.constants.XorThreadConstant;
import com.xoriant.xorpay.parser.service.ConnectorService;
import com.xoriant.xorpay.parser.service.LoadDataService;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.service.PaymentResponseService;
import com.xoriant.xorpay.thread.service.GeneratePaymentService;
import com.xoriant.xorpay.xero.NotAutherizedException;

@Service
public class XorProcessService {

	private final Logger logger = LoggerFactory.getLogger(XorProcessService.class);
	@Autowired
	private StgToNrmlzLoaderService loaderServiceStg;
	@Autowired
	private NrmlzToAggLoaderService loaderServiceNrml;
	@Autowired
	private GeneratePaymentService generatePaymentService;
	@Autowired
	private PaymentResponseService paymentResponseService;
	@Autowired
	private ConfigConstants config;

	@Autowired
	LoadDataService loadDataService;

	@Autowired
	ConnectorService connectorService;

	public boolean startProcess(String processId, HttpServletRequest request, HttpServletResponse response) {

		boolean isToReload = config.isReloadFlag();
		if (isToReload) {
			config.contextRefreshedEvent();
			logger.info("Configuration reloaded");
		}
		try {
			ConfigConstants.configMap.entrySet().stream()
					.filter(obj -> !obj.getValue().getCONFIG_ENV().equalsIgnoreCase(XorConstant.SYSTEM))
					.forEach(config -> {
						try {

							ConfigPojo configPojo = config.getValue();
							XorConstant.isCLS = configPojo.getCONFIG_ENV().equalsIgnoreCase(XorConstant.CLS);
							XorConstant.isXERO = configPojo.getCONFIG_ENV().equalsIgnoreCase(XorConstant.XERO);

							if (XorConstant.isCLS) {
								loadDataService.readJson();
							}
							/*
							 * if (XorConstant.isXERO) { Map<String, String> params = null; try {
							 * connectorService.fetchPayment(request, response, params); } catch
							 * (NotAutherizedException e) { System.out.println(e.getMessage());
							 * response.sendRedirect(connectorService.startAuthorization()); } }
							 */

							if (null != configPojo.getSOURCE_TABLE_NAME()
									&& null != configPojo.getSOURCE_TABLE_IDENTITY_COLUMN_NAME()
									&& null != configPojo.getSOURCE_MESSAGE_ID()
									&& null != configPojo.getSOURCE_PAYMENT_ID()) {
								logger.info("configPojo " + configPojo);
								loaderServiceStg.loadStgToNrml(processId, configPojo, isToReload);
								loaderServiceNrml.loadNrmToAgg(processId, configPojo);
								generatePaymentService.startGeneratingXMLFromStructure(processId, configPojo);
								Runnable pr = () -> {
									for (int i = 0; i < 5; i++) {
										logger.info("Payment will check for processign in 12 sec");
										try {
											Thread.sleep(20000);
										} catch (InterruptedException e) {
											e.printStackTrace();
										}
										paymentResponseService.responseProcess(processId, configPojo);
									}
								};
								XorThreadConstant.PymentProcess.execute(pr);
							} else {
								logger.info(configPojo.getSOURCE_TABLE_NAME() + "  "
										+ configPojo.getSOURCE_TABLE_IDENTITY_COLUMN_NAME() + "  "
										+ configPojo.getSOURCE_MESSAGE_ID() + "  " + configPojo.getSOURCE_PAYMENT_ID());
								logger.info(
										"PLease Configure Application Environment Specific, And restart the application");
								throw new RuntimeException(
										"PLease Configure Application Environment Specific, And restart the application");
							}

						} catch (Exception e) {
							logger.error("Exception", e);
							throw new RuntimeException(e);
						}
					});
			return true;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

}
