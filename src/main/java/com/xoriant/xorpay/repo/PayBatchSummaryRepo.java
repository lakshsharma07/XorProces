package com.xoriant.xorpay.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.PayBatchSummaryEntity;

@Repository
@Transactional
public interface PayBatchSummaryRepo extends JpaRepository<PayBatchSummaryEntity, String> {

}
