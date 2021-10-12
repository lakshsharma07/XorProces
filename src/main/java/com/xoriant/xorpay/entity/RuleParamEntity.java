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

@Table(name = "xp_rule_param")
public class RuleParamEntity {

	@Id
	// @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_RP")
	// @SequenceGenerator(name="SEQ_RP", sequenceName="SEQ_XP_RULE_PARAM_ID",
	// allocationSize=1)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "RULE_PARAM_ID")
	private Integer id;
	@Column(name = "RULE_MAP_ID")
	private Integer ruleMapId;
	@Column(name = "RULE_DEF_ID")
	private Integer ruleDefId;
	@Column(name = "LENGTH_MIN")
	private Integer lengthMin;
	@Column(name = "LENGTH_MAX")
	private Integer lengthMax;
	@Column(name = "MAPPING_VALUES")
	private String mappingValues;
	@Column(name = "PIUID_SEQID")
	private Integer piuidSeqId;
	@Column(name = "COUNTRY_SLNO")
	private Integer countryId;
	@Column(name = "EFFECTIVE_START_DATE")
	private LocalDateTime effectiveStartDate;
	@Column(name = "EFFECTIVE_END_DATE")
	private LocalDateTime effectiveEndDate;
	@Column(name = "ACTIVE_IND")
	private Character activeInd;
	@Column(name = "RULE_APPLY_LEVEL")
	private String ruleApplyLevel;
	@Column(name = "CONCT_SEPERATOR")
	private String concatSeperator;

	@Column(name = "CONDITION_BASE")
	private String conditionalRule;

	@Column(name = "LOOKUP_TABLE")
	private String lookupTable;

	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATION_DATE")
	private LocalDateTime createdDate;
	@Column(name = "LAST_UPDATED_BY")
	private String updatedBy;
	@Column(name = "LAST_UPDATE_DATE")
	private LocalDateTime updatedDate;

	@Override
	public String toString() {
		return "RuleParamEntity [id=" + id + ", ruleMapId=" + ruleMapId + ", ruleDefId=" + ruleDefId + ", lengthMin="
				+ lengthMin + ", lengthMax=" + lengthMax + ", mappingValues=" + mappingValues + ", piuidSeqId="
				+ piuidSeqId + ", countryId=" + countryId + ", effectiveStartDate=" + effectiveStartDate
				+ ", effectiveEndDate=" + effectiveEndDate + ", activeInd=" + activeInd + ", ruleApplyLevel="
				+ ruleApplyLevel + ", concatSeperator=" + concatSeperator + ", conditionalRule=" + conditionalRule
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + "]";
	}

}
