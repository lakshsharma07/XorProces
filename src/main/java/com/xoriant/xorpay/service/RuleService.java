package com.xoriant.xorpay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xoriant.xorpay.entity.RuleDefEntity;
import com.xoriant.xorpay.entity.RuleMapEntity;
import com.xoriant.xorpay.entity.RuleParamEntity;
import com.xoriant.xorpay.excepions.XorpayException;
import com.xoriant.xorpay.repo.RuleDefRepo;
import com.xoriant.xorpay.repo.RuleMapRepo;
import com.xoriant.xorpay.repo.RuleParamRepo;

@Service
public class RuleService {

	@Autowired
	private RuleDefRepo ruleDefRepo;

	@Autowired
	private RuleParamRepo ruleParamRepo;

	@Autowired
	private RuleMapRepo ruleMapRepo;

	public List<RuleDefEntity> getRuleDef() {
		return ruleDefRepo.findAll();
	}

	public void saveRuleDef(RuleDefEntity ruleDefEntity) throws XorpayException {
		try {
			ruleDefRepo.save(ruleDefEntity);
		} catch (Exception e) {
			throw new XorpayException("Data not saved");
		}
	}

	public List<RuleParamEntity> getRuleParam() {
		return ruleParamRepo.findAll();
	}

	public void saveRuleParam(List<RuleParamEntity> ruleParamEntityList) throws XorpayException {
		try {
			ruleParamRepo.saveAll(ruleParamEntityList);
		} catch (Exception e) {
			throw new XorpayException("Data not saved");
		}
	}

	public List<RuleMapEntity> getRuleMap() {
		return ruleMapRepo.findAll();
	}

	public void saveRuleMap(List<RuleMapEntity> ruleMapEntity) throws XorpayException {
		try {
			ruleMapRepo.saveAll(ruleMapEntity);
		} catch (Exception e) {
			throw new XorpayException("Data not saved");
		}
	}

}
