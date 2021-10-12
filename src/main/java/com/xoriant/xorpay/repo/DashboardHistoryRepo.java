package com.xoriant.xorpay.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.DashboardHistoryEntity;


@Repository
@Transactional
public interface DashboardHistoryRepo extends JpaRepository<DashboardHistoryEntity, Integer> {

	DashboardHistoryEntity findBySourceSysIdAndDataStage(Integer sourceSystemId, String dataStage);

	@Query("select distinct  sourceSysId from DashboardHistoryEntity")
	List<String> findDistintSourceSystemId();

	List<DashboardHistoryEntity> findBySourceSysId(Integer sourceSystemId);
	
}
