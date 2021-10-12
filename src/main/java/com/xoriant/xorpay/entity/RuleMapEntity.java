package com.xoriant.xorpay.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
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

@Table(name = "XP_RULE_MAP")
public class RuleMapEntity {

	@Id
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RM")
	//@SequenceGenerator(name="SEQ_RM", sequenceName="SEQ_XP_RULE_MAP_ID",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RULE_MAP_ID")
	private Integer id;
	@Column(name = "RULE_DEF_ID")
	private Integer ruleDefId;
	@Column(name = "TAG_SEQID")
	private Integer tagSeqid;
	@Column(name = "USER_DEF_COL_REFERENCE")
	private String userDefColReference;
	@Column(name = "USER_DEF_COL_REFERENCE_DESC")
	private String userDefColReferenceDesc;
	@Column(name = "RULE_MAP_PRIORITY")
	private Integer ruleMapPriority;
	@Column(name = "DATA_REFERENCE")
	private String dataReference;
	@Column(name = "COUNTRY_SLNO")
	private Integer countryId;
	@Column(name = "EFFECTIVE_START_DATE")
	private LocalDateTime effectiveStartDate;
	@Column(name = "EFFECTIVE_END_DATE")
	private LocalDateTime effectiveEndDate;
	@Column(name = "ACTIVE_IND")
	private Character activeInd;
	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATION_DATE")
	private LocalDateTime createdDate;
	@Column(name = "LAST_UPDATED_BY")
	private String updatedBy;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime updatedDate;

}
