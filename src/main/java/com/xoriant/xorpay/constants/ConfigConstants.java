package com.xoriant.xorpay.constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

//import com.xoriant.xorpay.XorpayService;
import com.xoriant.xorpay.data.sync.repo.JPARepo;
import com.xoriant.xorpay.entity.ConfigEntity;
import com.xoriant.xorpay.entity.SourceSysEntity;
import com.xoriant.xorpay.pojo.ConfigPojo;
import com.xoriant.xorpay.pojo.ConfigSys;
import com.xoriant.xorpay.repo.SourceSysRepo;
import com.xoriant.xorpay.service.XorConfigService;

@Component
public class ConfigConstants {

	public final static Integer TOPIC_PARTITIONS = 1;
	public final static String TOPIC_NAME = "xorpay_process";
	private static final String ACK = "ACK";

	public static String PATH = "PATH";
	public static Map<String, ConfigPojo> configMap = new HashMap<>();
	public static ConfigSys configSys = new ConfigSys();
	private final Logger logger = LoggerFactory.getLogger(ConfigConstants.class);
	public String BATCH_SIZE;
	@Autowired
	private XorConfigService configService;
	@Autowired
	private JPARepo jpaRepo;
	@Autowired
	private SourceSysRepo sourceRepo;

	private ConfigPojo pojo;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		this.applicationContext = applicationContext;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void contextReadyEvent() {

		try {
			//XorpayService task = new XorpayService<>(TOPIC_NAME);
			// task.start();
			//applicationContext.getAutowireCapableBeanFactory().autowireBean(task);
			//task.start();
			getEnv();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@EventListener(ContextRefreshedEvent.class)
	public void contextRefreshedEvent() {
		getEnv();
	}

	public boolean isReloadFlag() {
		return configService.getReloadFlag();
	}

	public boolean getMockingFlag() {
		return configService.getMockingType();
	}

	public void updateReloadFlag(boolean flag) {
		configService.updateReloadFlag(flag);
	}

	public void getEnv() {
		List<ConfigEntity> config = configService.findByConfigStatus(XorConstant.ACTIVE);
		Map<Integer, SourceSysEntity> sourceMap = sourceRepo.findAll().stream()
				.collect(Collectors.toMap(SourceSysEntity::getId, Function.identity()));
		logger.info("-env valu--> " + config);
		if (null != config) {

			config.stream().filter(f -> !f.getConfigEnv().equalsIgnoreCase(XorConstant.SYSTEM)).forEach(e -> {

				if (null != e.getConfigEnv()) {
					if (configMap.containsKey(e.getConfigEnv())) {
						pojo = configMap.get(e.getConfigEnv());
						getConfigPojoObj(e, sourceMap);
						configMap.put(e.getConfigEnv(), pojo);
					} else {
						pojo = new ConfigPojo();
						getConfigPojoObj(e, sourceMap);
						configMap.put(e.getConfigEnv(), pojo);
					}
				}

			});
			checkConfigSys(config);
			config.stream().filter(f -> f.getConfigEnv().equalsIgnoreCase(XorConstant.SYSTEM)).forEach(e -> {
				if (e.getConfigType().equalsIgnoreCase(XorConstant.RELOAD)) {
					configSys.setConfigReload(Boolean.parseBoolean(e.getConfigValue()));
				} else if (e.getConfigType().equalsIgnoreCase(XorConstant.MOCK_TYPE)) {
					if (e.getConfigValue().equalsIgnoreCase(ACK)) {
						configSys.setMockTypeACK(true);
					} else {
						configSys.setMockTypeACK(false);
					}
				} else if (e.getConfigType().equalsIgnoreCase(XorConstant.MOCK)) {
					configSys.setToMock(Boolean.parseBoolean(e.getConfigValue()));
				} else if (e.getConfigType().equalsIgnoreCase(XorConstant.CTRL_SUM_CHK)) {
					configSys.setCotrolSumCheckReq(Boolean.parseBoolean(e.getConfigValue()));
				}
			});

		} else {
			logger.info("Please Set Environment");
		}
	}

	private void checkConfigSys(List<ConfigEntity> config) {
		Set<String> sysConfigType = config.stream().filter(f -> f.getConfigEnv().equalsIgnoreCase(XorConstant.SYSTEM))
				.map(e -> e.getConfigType()).collect(Collectors.toSet());

		if (!sysConfigType.contains(XorConstant.RELOAD)) {
			configService.getReloadFlag();
		}
		if (!sysConfigType.contains(XorConstant.MOCK_TYPE)) {
			configService.getMockingType();
		}
		if (!sysConfigType.contains(XorConstant.MOCK)) {
			configService.getMock();
		}
	}

	private ConfigPojo getConfigPojoObj(ConfigEntity e, Map<Integer, SourceSysEntity> sourceMap) {
		pojo.setSOURCE_SYS_ID(e.getSourceSystemId());
		if (null != sourceMap && null != pojo.getSOURCE_SYS_ID()) {
			pojo.setSrcSys(sourceMap.get(pojo.getSOURCE_SYS_ID()));
		}
		pojo.setCONFIG_ENV(e.getConfigEnv());

		if (null != e.getConfigName() && null != e.getConfigType()) {
			if (e.getConfigName().equalsIgnoreCase(XorConstant.ENVIRONEMNT)) {
				if (e.getConfigType().equalsIgnoreCase(XorConstant.SAMPLE_PAN_IN)) {
					pojo.setSAMPLE_PAN_IN(e.getConfigValue());
				}
				if (e.getConfigType().equalsIgnoreCase(XorConstant.PAN_IN)) {
					pojo.setPAN_IN(e.getConfigValue());
				}
				if (e.getConfigType().equalsIgnoreCase(XorConstant.PAN_ARCHIVE)) {
					pojo.setPAN_ARCHIVE(e.getConfigValue());
				}
				if (e.getConfigType().equalsIgnoreCase(XorConstant.PAN_OUT)) {
					pojo.setPAN_OUT(e.getConfigValue());
				}
				if (e.getConfigType().equalsIgnoreCase(XorConstant.BASE64)) {
					pojo.setBASE64(e.getConfigValue());
				}
				if (e.getConfigType().equalsIgnoreCase(XorConstant.PGP)) {
					pojo.setPGP(e.getConfigValue());
				}

			}
			if (e.getConfigName().equalsIgnoreCase(XorConstant.SOURCE)
					&& e.getConfigType().equalsIgnoreCase(XorConstant.SOURCE_PROFILENAME)) {
				pojo.setSOURCE_PROFILE_COLUMN_NAME(e.getConfigValue());
				logger.info("-env Source Profile col name--> " + pojo.getSOURCE_PROFILE_COLUMN_NAME());
			}

			if (e.getConfigName().equalsIgnoreCase(XorConstant.SOURCE)
					&& e.getConfigType().equalsIgnoreCase(XorConstant.SOURCE_TABLE)) {
				pojo.setSOURCE_TABLE_NAME(e.getConfigValue());
				logger.info("-env SOURCE_TABLE--> " + pojo.getSOURCE_TABLE_NAME());
			}

			if (e.getConfigName().equalsIgnoreCase(XorConstant.DATABASE)
					&& e.getConfigType().equalsIgnoreCase(XorConstant.STORAGE)) {
				pojo.setDATABASE_SYSTEM(e.getConfigValue());

				logger.info("-env DATABASE_SYSTEM--> " + pojo.getDATABASE_SYSTEM());
				Map<Integer, String> dbDataTypeIdMap = new HashMap<>(1);
				Map<String, Integer> dbDataTypeStrMap = new HashMap<>(1);

				jpaRepo.getDatabasetDataTypeMap(pojo.getDATABASE_SYSTEM(), dbDataTypeIdMap, dbDataTypeStrMap);
				pojo.setDbDataTypeStrMap(dbDataTypeStrMap);
				pojo.setDbDataTypeIdMap(dbDataTypeIdMap);
			}

			if (e.getConfigName().equalsIgnoreCase(XorConstant.SOURCE)
					&& e.getConfigType().equalsIgnoreCase(XorConstant.SOURCE_IDENTITY)) {
				pojo.setSOURCE_TABLE_IDENTITY_COLUMN_NAME(e.getConfigValue());
				logger.info("-env SOURCE_TABLE_IDENTITY_COLUMN_NAME--> " + pojo.getSOURCE_TABLE_IDENTITY_COLUMN_NAME());
			}

			if (e.getConfigName().equalsIgnoreCase(XorConstant.SOURCE)
					&& e.getConfigType().equalsIgnoreCase(XorConstant.SOURCE_MESSAGE_ID)) {
				pojo.setSOURCE_MESSAGE_ID(e.getConfigValue());
				logger.info("-env SOURCE_MESSAGE_ID--> " + pojo.getSOURCE_MESSAGE_ID());
			}

			if (e.getConfigName().equalsIgnoreCase(XorConstant.SOURCE)
					&& e.getConfigType().equalsIgnoreCase(XorConstant.SOURCE_PAYMENT_ID)) {
				pojo.setSOURCE_PAYMENT_ID(e.getConfigValue());
				logger.info("-env SOURCE_PAYMENT_ID--> " + pojo.getSOURCE_PAYMENT_ID());
			}

		}

		return pojo;
	}

}
