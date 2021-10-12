package com.xoriant.xorpay.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.AggregatedPaymentHistoryEntity;


@Repository
@Transactional
public interface AggregatedPaymentHistoryRepo extends JpaRepository<AggregatedPaymentHistoryEntity, Integer> {

    AggregatedPaymentHistoryEntity findByEndtoendid33(String paymentNumber);
    List<AggregatedPaymentHistoryEntity> findByEndtoendid33In(List<String> paymentNumber);

}
