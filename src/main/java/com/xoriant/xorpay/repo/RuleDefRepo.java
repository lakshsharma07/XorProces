package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.RuleDefEntity;

@Repository
@Transactional
public interface RuleDefRepo extends JpaRepository<RuleDefEntity, Integer> {

	List<RuleDefEntity> findByActiveInd(Character statusY);

}
