package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.RuleParamEntity;


@Repository
@Transactional
public interface RuleParamRepo extends JpaRepository<RuleParamEntity, Integer> {

	//O<RuleParamEntity> findById(Integer id);
	List<RuleParamEntity> findByRuleMapId(Integer id);

	List<RuleParamEntity> findByActiveIndAndRuleMapIdIn(Character activeind, List<Integer> ruleMapIdList);
}
