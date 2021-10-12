package com.xoriant.xorpay.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.XpScheduleStatusEntity;


@Repository
@Transactional
public interface XpSchedulStatusRepo extends JpaRepository<XpScheduleStatusEntity, Integer> {

	List<XpScheduleStatusEntity> findByStatusAndRequestNameAndScheduleAndEnv(String scheduled, String nrmlprocname,
			Character schedule, String env);

	List<XpScheduleStatusEntity> findByEnv(String xorEnv);

	List<XpScheduleStatusEntity> findByStatusAndRequestNameAndEnv(String scheduled, String normAndAggProc,
			String env);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "CALL LOAD_NRMLZ_TABLES(:SOURCE_SYSTEM)", nativeQuery = true)
	void LOAD_NRMLZ_TABLES(@Param("SOURCE_SYSTEM") Integer sourceSystem);
	
	@Modifying(clearAutomatically = true)
	@Query(value = "CALL LOAD_AGG_TABLES(:SOURCE_SYSTEM)", nativeQuery = true)
	void LOAD_AGG_TABLES(@Param("SOURCE_SYSTEM") Integer sourceSystem);

}
