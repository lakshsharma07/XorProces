package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.RuleMapEntity;


@Repository
@Transactional
public interface RuleMapRepo extends JpaRepository<RuleMapEntity, Integer> {

	List<RuleMapEntity> findByTagSeqidOrderByRuleMapPriority(Integer tagSeqId);
	@Query("SELECT distinct tagSeqid FROM RuleMapEntity")
	List<Integer> findDistinctTagSeqid();
	
	List<RuleMapEntity> findByDataReferenceIn(List<String> profNames);
	
	List<RuleMapEntity> findByActiveIndAndDataReferenceInIgnoreCase(Character statusY, List<String> profiles);

}
