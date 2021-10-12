package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.RuleLookUpEntity;

@Repository
@Transactional
public interface RuleLookUpRepo extends JpaRepository<RuleLookUpEntity, Integer> {

	List<RuleLookUpEntity> findByProfileName(String profileName);

}
