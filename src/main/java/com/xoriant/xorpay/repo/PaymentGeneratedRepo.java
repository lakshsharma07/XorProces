package com.xoriant.xorpay.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.PaymentGeneratedEntity;


@Repository
@Transactional
public interface PaymentGeneratedRepo extends JpaRepository<PaymentGeneratedEntity, Integer> {
	

}
