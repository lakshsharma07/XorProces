package com.xoriant.xorpay.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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

@Table(name = "xp_xml_rules_view")
public class RulesViewEntity {

	@Id
	@Column(name = "RD_RULE_DEF_ID")
	private Integer id;
	
	@Column(name = "Q_DATA_REFERENCE")
	private Character qDataReference;
	@Column(name = "RD_RULE_NAME")
	private String rdRuleName;
	@Column(name = "RD_RULE_EXE_LOGIC")
	private String rdRuleExeLogic;
	@Column(name = "RD_RULE_DESCRIPTION")
	private String rdRuleDescription;
	@Column(name = "RM_RULE_MAP_ID")
	private Integer rmRuleMapId;
	@Column(name = "RM_RRULE_DEF_ID")
	private Integer rmRruleDefId;
	@Column(name = "RM_TAG_SEQID")
	private Integer rmTagSeqid;
	@Column(name = "RM_USER_DEF_COL_REFERENCE")
	private String rmUserDefColReference;
	@Column(name = "RM_USER_DEF_COL_REFERENCE_DESC")
	private String rmUserDefColReferenceDesc;
	@Column(name = "RM_RULE_MAP_PRIORITY")
	private Integer rmRuleMapPriority;
	@Column(name = "RM_DATA_REFERENCE")
	private String rmDataReference;
	@Column(name = "RM_COUNTRY")
	private String rmCountry;
	@Column(name = "XTX_TAG_NAME")
	private String xtxTagName;
	@Column(name = "XTX_XPATH")
	private String xtxXpath;
	@Column(name = "RP_RULE_PARAM_ID")
	private Integer rpRuleParamId;
	@Column(name = "RP_RULE_MAP_ID")
	private Integer rpRuleMapId;
	@Column(name = "RP_RULE_DEF_ID")
	private Integer rpRuleDefId;
	@Column(name = "RP_LENGTH_MIN")
	private Integer rpLengthMin;
	@Column(name = "RP_LENGTH_MAX")
	private Integer rpLengthMax;
	@Column(name = "RP_MAPPING_VALUES")
	private String rpMappingValues;
	@Column(name = "RP_PUID")
	private String rpPuid;
	@Column(name = "RP_COUNTRY")
	private String rpCountry;

}
