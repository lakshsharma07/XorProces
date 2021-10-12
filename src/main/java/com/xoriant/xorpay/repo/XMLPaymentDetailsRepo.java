package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.XMLPaymentDetailsEntity;


@Repository
@Transactional
public interface XMLPaymentDetailsRepo extends JpaRepository<XMLPaymentDetailsEntity, Integer> {

	@Query("select distinct  orgName from XMLPaymentDetailsEntity")
	List<String> getDistinctOU();


}
