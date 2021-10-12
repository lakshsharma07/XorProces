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

@Table(name = "xp_rule_def")
public class RuleDefEntity {

	@Id
	//@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RD")
	//@SequenceGenerator(name="SEQ_RD", sequenceName="SEQ_XP_RULE_DEF_ID", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RULE_DEF_ID")
	private Integer id;
	@Column(name = "RULE_NAME")
	private String ruleName;
	@Column(name = "RULE_EXE_LOGIC")
	private String ruleExeLogic;
	@Column(name = "RULE_DESCRIPTION")
	private String ruleDescription;
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
