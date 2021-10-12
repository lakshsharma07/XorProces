package com.xoriant.xorpay.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.PaymentCommentsEntity;


@Repository
@Transactional
public interface PaymentCommentsRepo extends JpaRepository<PaymentCommentsEntity, Integer> {

	List<PaymentCommentsEntity> findTop5ByPaymentInstructionIdOrderByLastUpdateDateDesc(Long paymentInstructionId);

	List<PaymentCommentsEntity> findTop5ByPaymentIdOrderByLastUpdateDateDesc(Long paymentId);

}
