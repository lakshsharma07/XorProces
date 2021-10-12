package com.xoriant.xorpay.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.DataProcessAuditEntity;


@Repository
@Transactional
public interface DataProcessAuditRepo extends JpaRepository<DataProcessAuditEntity, Integer> {

	DataProcessAuditEntity findByProcessId(String processId);

}
