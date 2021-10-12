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

@Table(name = "XP_DASHBOARD_DETAILS")
public class DashboardEntity {

	@Id
	// @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_D")
	// @SequenceGenerator(name="SEQ_D",
	// sequenceName="SEQ_XP_DASHBOARD_DETAILS_SLNO", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "slno")
	private Integer id;

	@Column(name = "INSERT_TIME")
	private LocalDateTime insertTime;

	@Column(name = "ERP_SRC_SYS")
	private Integer sourceSysId;

	@Column(name = "RECORD_COUNT")
	private Integer recordCount;

	@Column(name = "XML_ERROR_COUNT")
	private Integer xmlErrorCount;

	@Column(name = "DATA_STAGE")
	private String dataStage;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATION_DATE")
	private LocalDateTime createdDate;

	@Column(name = "LAST_UPDATED_BY")
	private String updatedBy;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime updatedDate;
}
