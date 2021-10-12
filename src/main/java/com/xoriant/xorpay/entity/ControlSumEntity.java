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

@Table(name = "XP_CTRL_CAL")
public class ControlSumEntity {

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONFIG_MASTER") // , generator="SEQ")
	//@SequenceGenerator(name = "SEQ_CONFIG_MASTER", sequenceName = "SEQ_XP_CONFIG_MASTER_SLNO", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SLNO")
	private Integer id;
	@Column(name = "PIUID_SEQID")
	private Integer piuidSeqId;
	@Column(name = "FORMULA")
	private String formula;
	@Column(name = "MATCH_COL_NAM")
	private String matchColName;
	@Column(name = "SRC_SYS_ID")
	private Integer sourceSysId;
	@Column(name = "ACTIVE_IND")
	private Character activeInd;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATION_DATE")
	private LocalDateTime creationDate;
	@Column(name = "LAST_UPDATED_BY")
	private String lastUpdateBy;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime lastUpdateDate;
	
}
