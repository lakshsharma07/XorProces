package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.PaymentAksDetailEntity;

@Repository
@Transactional
public interface PaymentAckDetailsRepo extends JpaRepository<PaymentAksDetailEntity, Integer> {

	List<PaymentAksDetailEntity> findByInternalUuidAndPaymentNumber(String internalUUID, String endToEndId);

	List<PaymentAksDetailEntity> findByPaymentInstructionIdAndPaymentNumber(String messsageId, String endToEndId);

}
