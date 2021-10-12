package com.xoriant.xorpay.repo;

import com.xoriant.xorpay.data.sync.entity.ProcessStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface ProcessStatusRepo extends JpaRepository<ProcessStatusEntity, Integer> {



}
