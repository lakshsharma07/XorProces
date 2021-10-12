package com.xoriant.xorpay.rule.pojo;

import java.util.List;

import com.xoriant.xorpay.entity.RuleParamEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RuleMapPOJO {

	private Integer ruleMapSeqId;
	private Integer priority;
	private Integer ruleDefId;
	private String ruleName;
	private Integer tagSeqid;
	private String fieldName;
	private RuleParamEntity ruleParam;
	private List<RuleParamEntity> ruleParamList;
	private List<RuleMapPOJO> optionalRuleMapList;
	
	@Override
	public String toString() {
		return "RuleMapPOJO [ruleMapSeqId=" + ruleMapSeqId + ", priority=" + priority + ", ruleDefId=" + ruleDefId
				+ ", ruleName=" + ruleName + ", tagSeqid=" + tagSeqid + ", fieldName=" + fieldName + ", ruleParam="
				+ ruleParam + "]";
	}

	
}
