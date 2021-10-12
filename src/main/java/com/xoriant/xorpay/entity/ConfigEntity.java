package com.xoriant.xorpay.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "XP_CONFIG")
public class ConfigEntity {

	@Id
	// @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CONFIG")//,
	// generator="SEQ")
	// @SequenceGenerator(name="SEQ_CONFIG", sequenceName="SEQ_XP_CONFIG_SLNO",
	// allocationSize=1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SLNO")
	private Integer id;
	@Column(name = "CONFIG_ENV")
	private String configEnv;
	@Column(name = "CONFIG_NAME")
	private String configName;
	@Column(name = "CONFIG_STATUS")
	private String configStatus;
	@Column(name = "CONFIG_VALUE")
	private String configValue;
	@Column(name = "CONFIG_TYPE")
	private String configType;
	// @Column(name = "CONFIG_PATH")
	// private String configPath;
	@Column(name = "ERP_SRC_SYS")
	private Integer sourceSystemId;
	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "creation_date")
	private LocalDateTime creationDate;
	@Column(name = "last_updated_by")
	private String lastUpdateBy;
	@Column(name = "last_update_date")
	private LocalDateTime lastUpdateDate;

	public ConfigEntity(String configEnv, String configName, String configType, String configStatus, String configValue,
			String createdBy, LocalDateTime creationDate, String lastUpdateBy, LocalDateTime lastUpdateDate) {
		this.configEnv = configEnv;
		this.configName = configName;
		this.configType = configType;
		this.configStatus = configStatus;
		this.configValue = configValue;
		this.createdBy = createdBy;
		this.creationDate = creationDate;
		this.lastUpdateBy = lastUpdateBy;
		this.lastUpdateDate = lastUpdateDate;
	}
}
