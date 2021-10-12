
package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.BatchPaymentStatusDayEntity;


@Repository
@Transactional
public interface BatchPaymentStatusDayRepo extends JpaRepository<BatchPaymentStatusDayEntity, Integer> {
	
	List<BatchPaymentStatusDayEntity> findBySourceSysIdAndYearMonthDayStartsWithOrderByYearMonthDayAsc(Integer sourceSysId, String monthYearLikeString);

}
