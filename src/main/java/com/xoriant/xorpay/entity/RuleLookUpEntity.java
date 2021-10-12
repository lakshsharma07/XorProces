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

@Table(name = "XP_RULE_LOOKUP_SRC_COL")
public class RuleLookUpEntity {

	@Id
	// @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_CONFIG")//,
	// generator="SEQ")
	// @SequenceGenerator(name="SEQ_CONFIG", sequenceName="SEQ_XP_CONFIG_SLNO",
	// allocationSize=1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SLNO")
	private Integer id;
	@Column(name = "PROFILENAME")
	private String profileName;
	@Column(name = "TEMP_COL")
	private String tempCol;
	@Column(name = "SRC_COL_NAME")
	private String srcColName;

	@Column(name = "created_by")
	private String createdBy;
	@Column(name = "creation_date")
	private LocalDateTime creationDate;
	@Column(name = "last_updated_by")
	private String lastUpdateBy;
	@Column(name = "last_update_date")
	private LocalDateTime lastUpdateDate;

}
