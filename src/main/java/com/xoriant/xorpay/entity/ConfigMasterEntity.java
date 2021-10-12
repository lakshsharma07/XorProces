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

@Table(name = "XP_CONFIG_MASTER")
public class ConfigMasterEntity {

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONFIG_MASTER") // , generator="SEQ")
	//@SequenceGenerator(name = "SEQ_CONFIG_MASTER", sequenceName = "SEQ_XP_CONFIG_MASTER_SLNO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SLNO")
	private Integer id;
	@Column(name = "CONFIG_ENV")
	private String configEnv;
	@Column(name = "CONFIG_NAME")
	private String configName;
	@Column(name = "CONFIG_VALUE")
	private String configValue;
	@Column(name = "CONFIG_TYPE")
	private String configType;

	@Column(name = "CONFIG_VALUE_PARENT_SLNO")
	private Integer configValueId;
	@Column(name = "ACTIVE_IND_PARENT_SLNO")
	private Integer activeIndId;
	@Column(name = "ACTIVE_IND")
	private Character activeInd;

	@Column(name = "CREATED_BY")
	private Integer createdBy;
	@Column(name = "CREATION_DATE")
	private LocalDateTime creationDate;
	@Column(name = "LAST_UPDATED_BY")
	private Integer lastUpdateBy;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime lastUpdateDate;
	@Column(name ="INPUT_ACTION")
	private String inputAction;

}
