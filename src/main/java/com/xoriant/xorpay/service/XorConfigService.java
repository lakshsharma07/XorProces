package com.xoriant.xorpay.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.entity.ConfigEntity;
import com.xoriant.xorpay.entity.ConfigMasterEntity;
import com.xoriant.xorpay.excepions.XorpayException;
import com.xoriant.xorpay.pojo.ConfigInputPOJO;
import com.xoriant.xorpay.pojo.ConfigMasterPOJO;
import com.xoriant.xorpay.pojo.ConfigTypePOJO;
import com.xoriant.xorpay.pojo.SrcSysPOJO;
import com.xoriant.xorpay.repo.ConfigMasterRepo;
import com.xoriant.xorpay.repo.ConfigRepo;
import com.xoriant.xorpay.repo.SourceSysRepo;

@Service
public class XorConfigService {

	@Autowired
	private ConfigRepo xorConfigRepo;

	@Autowired
	private ConfigMasterRepo confMasterRepo;

	@Autowired
	private SourceSysRepo sourceSysRepo;

	private static final Logger logger = LoggerFactory.getLogger(XorConfigService.class);

	public void setPaymentOutPath(ConfigEntity configEntity) throws XorpayException {
		try {
			ConfigEntity entity = xorConfigRepo
					.findByConfigEnvIgnoreCaseAndConfigNameIgnoreCaseAndConfigTypeIgnoreCaseAndConfigValueIgnoreCase(
							configEntity.getConfigEnv(), configEntity.getConfigName(), configEntity.getConfigType(),
							configEntity.getConfigValue());
			if (null != entity) {
				throw new XorpayException("Config already present !");
			}

			configEntity.setCreatedBy(null);
			configEntity.setCreationDate(LocalDateTime.now());
			configEntity.setLastUpdateBy(null);
			configEntity.setLastUpdateDate(LocalDateTime.now());
			xorConfigRepo.save(configEntity);
		} catch (XorpayException ex) {
			throw ex;
		} catch (Exception e) {
			e.printStackTrace();
			throw new XorpayException("Error while saving configuration");
		}
	}

	public List<ConfigEntity> getConfiguration() {

		return xorConfigRepo.findAll();
	}

	public ConfigEntity findByConfigNameAndConfigStatus(String configName, String configStatus) {

		return xorConfigRepo.findByConfigNameIgnoreCaseAndConfigStatusIgnoreCase(configName, configStatus);
	}

	public ConfigEntity findByConfigEnvAndConfigStatus(String configEnv, String configName, String configStatus) {

		return xorConfigRepo.findByConfigEnvIgnoreCaseAndConfigNameIgnoreCaseAndConfigStatusIgnoreCase(configEnv,
				configName, configStatus);
	}

	public List<ConfigEntity> findByConfigStatus(String configStatus) {

		return xorConfigRepo.findByConfigStatusIgnoreCase(configStatus);
	}

	public String setConfigurationMaster(List<ConfigMasterEntity> entity) {
		try {
			confMasterRepo.saveAll(entity);
		} catch (Exception e) {
			return XorConstant.FAILED;
		}
		return XorConstant.SUCCESS;
	}

	public ConfigEntity findByConfigEnvAndConfigNameAndConfigStatusAndConfigType(String configEnv, String configName,
			String configStatus, String configType) {
		return xorConfigRepo
				.findByConfigEnvIgnoreCaseAndConfigNameIgnoreCaseAndConfigStatusIgnoreCaseAndConfigTypeIgnoreCase(
						configEnv, configName, configStatus, configType);
	}

	public ConfigMasterPOJO getAllConfigMaster() {

		List<ConfigMasterEntity> configMaster = confMasterRepo.findAll();

		List<ConfigTypePOJO> typeList = configMaster.stream().filter(e -> null != e.getConfigType())
				.map(e -> new ConfigTypePOJO(e.getId(), e.getConfigType())).collect(Collectors.toList());

		List<SrcSysPOJO> sourceSysem = sourceSysRepo.findAll().stream()
				.map(e -> new SrcSysPOJO(e.getId(), e.getSrcSystemSort())).collect(Collectors.toList());
		return new ConfigMasterPOJO(
				configMaster.stream().filter(e -> null != e.getConfigEnv()).map(e -> e.getConfigEnv())
						.collect(Collectors.toList()),
				configMaster.stream().filter(e -> null != e.getConfigName()).map(e -> e.getConfigName())
						.collect(Collectors.toList()),
				typeList, sourceSysem);

	}

	public ConfigInputPOJO getConfigTypeValues(Integer configId) {
		ConfigMasterEntity entity = confMasterRepo.findById(configId).get();
		String inputType = entity.getInputAction();
		List<String> inputParam = null;
		List<ConfigMasterEntity> confiEntity = confMasterRepo.findByConfigValueId(configId.toString());
		if (null != confiEntity) {
			inputParam = confiEntity.stream().map(e -> e.getConfigValue()).collect(Collectors.toList());
		}
		return new ConfigInputPOJO(inputType, inputParam);

	}

	public List<ConfigEntity> getPropertiesForEnv() {
		List<ConfigEntity> envConfig = xorConfigRepo.findByConfigStatusIgnoreCase(XorConstant.ACTIVE);

		return envConfig;
	}

	public void deleteConfig(Integer id) throws XorpayException {
		xorConfigRepo.deleteById(id);
	}

	public void updateConfiguration(ConfigEntity configEntity) throws XorpayException {
		try {
			ConfigEntity config = xorConfigRepo.findById(configEntity.getId()).get();
			if (null != config) {
				// config.setConfigName(configEntity.getConfigName());
				config.setConfigType(configEntity.getConfigType());
				config.setConfigStatus(configEntity.getConfigStatus());
				config.setLastUpdateDate(LocalDateTime.now());
				xorConfigRepo.save(config);
			}

		} catch (Exception e) {
			throw new XorpayException("Data not saved");
		}

	}

	public boolean getReloadFlag() {
		ConfigEntity config = xorConfigRepo
				.findByConfigEnvIgnoreCaseAndConfigNameIgnoreCaseAndConfigTypeIgnoreCaseAndConfigStatusIgnoreCase(
						XorConstant.SYSTEM, XorConstant.CONFIG, XorConstant.RELOAD, XorConstant.ACTIVE);
		if (null != config) {
			if (config.getConfigValue().equalsIgnoreCase(XorConstant.TRUE)) {
				return true;
			} else {
				return false;
			}
		} else {
			config = new ConfigEntity(XorConstant.SYSTEM, XorConstant.CONFIG, XorConstant.RELOAD, XorConstant.ACTIVE,
					XorConstant.FALSE, XorConstant.ADMIN_ROLE, LocalDateTime.now(), XorConstant.ADMIN_ROLE,
					LocalDateTime.now());
			xorConfigRepo.save(config);
			return false;
		}

	}

	public boolean getMockingType() {
		ConfigEntity config = xorConfigRepo
				.findByConfigEnvIgnoreCaseAndConfigNameIgnoreCaseAndConfigTypeIgnoreCaseAndConfigStatusIgnoreCase(
						XorConstant.SYSTEM, XorConstant.CONFIG, XorConstant.MOCK_TYPE, XorConstant.ACTIVE);
		if (null != config) {
			if (config.getConfigValue().equalsIgnoreCase("ACK")) {
				return true;
			} else {
				return false;
			}
		} else {
			config = new ConfigEntity(XorConstant.SYSTEM, XorConstant.CONFIG, XorConstant.MOCK_TYPE, XorConstant.ACTIVE,
					"ACK", XorConstant.ADMIN_ROLE, LocalDateTime.now(), XorConstant.ADMIN_ROLE, LocalDateTime.now());
			xorConfigRepo.save(config);
			return true;
		}

	}

	public boolean getMock() {
		ConfigEntity config = xorConfigRepo
				.findByConfigEnvIgnoreCaseAndConfigNameIgnoreCaseAndConfigTypeIgnoreCaseAndConfigStatusIgnoreCase(
						XorConstant.SYSTEM, XorConstant.CONFIG, XorConstant.MOCK, XorConstant.ACTIVE);
		if (null != config) {
			if (config.getConfigValue().equalsIgnoreCase("true")) {
				return true;
			} else {
				return false;
			}
		} else {
			config = new ConfigEntity(XorConstant.SYSTEM, XorConstant.CONFIG, XorConstant.MOCK, XorConstant.ACTIVE,
					"false", XorConstant.ADMIN_ROLE, LocalDateTime.now(), XorConstant.ADMIN_ROLE, LocalDateTime.now());
			xorConfigRepo.save(config);
			return true;
		}

	}

	public void updateReloadFlag(boolean flag) {
		ConfigEntity config = xorConfigRepo
				.findByConfigEnvIgnoreCaseAndConfigNameIgnoreCaseAndConfigTypeIgnoreCaseAndConfigStatusIgnoreCase(
						XorConstant.SYSTEM, XorConstant.CONFIG, XorConstant.RELOAD, XorConstant.ACTIVE);

		if (flag) {
			config.setConfigValue("true");
		} else {
			config.setConfigValue("false");
		}

	}
}
