package com.xoriant.xorpay.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.AggRemittanceInfoEntity;


@Repository
@Transactional
public interface AggRemittanceInfoRepo extends JpaRepository<AggRemittanceInfoEntity, Integer> {

	List<AggRemittanceInfoEntity> findByMsgId(String paymentNumber);
	
}
