package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.SourceSysEntity;


@Repository
@Transactional

public interface SourceSysRepo extends JpaRepository<SourceSysEntity, Integer> {
	SourceSysEntity findBySrcSystemSort(String sourceSystem);

	List<SourceSysEntity> findByIdIn(List<Integer> sseList);

}
