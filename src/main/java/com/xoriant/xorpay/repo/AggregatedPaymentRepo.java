package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.AggregatedPaymentEntity;

@Repository
@Transactional
public interface AggregatedPaymentRepo extends JpaRepository<AggregatedPaymentEntity, Integer> {

	@Query("SELECT distinct msgId FROM AggregatedPaymentEntity")
	List<String> findDistinctMessageId();

	@Query("SELECT distinct endToEndId FROM AggregatedPaymentEntity")
	List<String> findDistinctEndToEndId();

	List<AggregatedPaymentEntity> findByMsgId(String messageId);

	List<AggregatedPaymentEntity> findByMsgIdIn(List<String> msgList);

	List<AggregatedPaymentEntity> findByEndToEndIdIn(List<String> endToEndIdLis);

	AggregatedPaymentEntity findByEndToEndId(String paymentNumber);

	@Query("select count(xmlStatus) from AggregatedPaymentEntity where xmlStatus='Y'")
	long countForXMLStatus();

	// @Query("select x from AggregatedPaymentEntity x where x.xmlStatus is null")
	List<AggregatedPaymentEntity> findByXmlStatusAndSourceSystem(Character xmlProcessed, Integer sourceSystem);

	List<AggregatedPaymentEntity> findByXmlStatusAndSourceSystemAndMsgId(Character xmlProcessed, Integer sourceSystem,
			String batchId);

	List<AggregatedPaymentEntity> findByIdIn(List<Integer> tage);

	@Modifying(clearAutomatically = true)
	@Query("SELECT distinct profileName FROM AggregatedPaymentEntity where xmlStatus ='L' and erp_src_sys =(:erpSrcSys)")
	List<String> findDistinctProfileName(@Param("erpSrcSys") Integer erpSrcSys);

	List<AggregatedPaymentEntity> findByMsgIdAndEndToEndId(String messsageId, String endToEndId);

}
