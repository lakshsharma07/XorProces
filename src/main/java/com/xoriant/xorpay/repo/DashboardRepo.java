package com.xoriant.xorpay.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.DashboardEntity;


@Repository
@Transactional
public interface DashboardRepo extends JpaRepository<DashboardEntity, Integer> {

	DashboardEntity findBySourceSysIdAndDataStage(Integer sourceSystemId, String dataStage);

	@Query("select distinct  sourceSysId from DashboardEntity")
	List<String> findDistintSourceSystemId();

	List<DashboardEntity> findBySourceSysId(Integer sourceSystemId);
	
}
