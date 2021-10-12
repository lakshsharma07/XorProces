package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.PaymentBatchAksDetailEntity;

@Repository
@Transactional
public interface PaymentBatchAckDetailsRepo extends JpaRepository<PaymentBatchAksDetailEntity, Integer> {

	List<PaymentBatchAksDetailEntity> findByPaymentInstructionId(String messsageId);
	

}
