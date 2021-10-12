package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.ConfigEntity;

@Repository
@Transactional
public interface ConfigRepo extends JpaRepository<ConfigEntity, Integer> {

	ConfigEntity findByConfigNameIgnoreCaseAndConfigStatusIgnoreCase(String configName, String configStatus);

	ConfigEntity findByConfigEnvIgnoreCaseAndConfigNameIgnoreCaseAndConfigStatusIgnoreCase(String configEnv,
			String configName, String configStatus);

	ConfigEntity findByConfigEnvIgnoreCaseAndConfigNameIgnoreCaseAndConfigStatusIgnoreCaseAndConfigTypeIgnoreCase(
			String configEnv, String configName, String configStatus, String configType);

	List<ConfigEntity> findByConfigEnvAndConfigStatusIgnoreCase(String xorEnv, String status);

	ConfigEntity findByConfigEnvIgnoreCaseAndConfigNameIgnoreCaseAndConfigTypeIgnoreCaseAndConfigValueIgnoreCase(
			String configEnv, String configName, String configType, String configValue);

	ConfigEntity findByConfigNameAndConfigStatus(String configName, String configStatus);

	List<ConfigEntity> findByConfigStatusIgnoreCase(String status);

	ConfigEntity findByConfigEnvIgnoreCaseAndConfigNameIgnoreCaseAndConfigTypeIgnoreCaseAndConfigStatusIgnoreCase(
			String configEnv, String configName, String configType, String configStatus);

}
