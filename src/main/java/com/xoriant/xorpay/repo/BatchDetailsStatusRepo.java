package com.xoriant.xorpay.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.BatchDetailStatusEntity;


@Repository
@Transactional
public interface BatchDetailsStatusRepo extends JpaRepository<BatchDetailStatusEntity, Integer> {

	List<BatchDetailStatusEntity> findTop10ByOrderByIdDesc();

}
