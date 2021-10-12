
package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.BatchPaymentStatusMonthEntity;


@Repository
@Transactional
public interface BatchPaymentStatusMonthRepo extends JpaRepository<BatchPaymentStatusMonthEntity, Integer> {
	
	List<BatchPaymentStatusMonthEntity> findBySourceSysIdAndMonthYearIn(Integer sourceSystemId, List<String> monthYearLst);

	//@Query("select distinct  monthYear from BatchPaymentStatusMonthEntity")
	//List<String> findDistictMonthYear();

	List<BatchPaymentStatusMonthEntity> findTop6BySourceSysIdOrderByYearMnthDesc(Integer sourceSysId);

}