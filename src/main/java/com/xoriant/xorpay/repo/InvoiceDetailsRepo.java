package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.InvoiceDetailsEntity;


@Repository
@Transactional
public interface InvoiceDetailsRepo extends JpaRepository<InvoiceDetailsEntity, Integer> {

	List<InvoiceDetailsEntity> findByPaymentId(Long paymentId);
	
	Integer countByPaymentId(Long paymentId);

	Page<InvoiceDetailsEntity> findByPaymentId(Long paymentId, Pageable paging);
}
