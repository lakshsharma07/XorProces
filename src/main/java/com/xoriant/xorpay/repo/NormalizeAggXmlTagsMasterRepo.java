package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.NormalizeAggXmlTagsMasterEntity;

@Repository
@Transactional
public interface NormalizeAggXmlTagsMasterRepo extends JpaRepository<NormalizeAggXmlTagsMasterEntity, Integer> {

	List<NormalizeAggXmlTagsMasterEntity> findByErpSrcSys(Integer sourceSystem);

	List<NormalizeAggXmlTagsMasterEntity> findByErpSrcSysAndActiveInd(Integer srcSystemId, Character status);

	NormalizeAggXmlTagsMasterEntity findByErpSrcSysAndTagSeqId(Integer srcSystem, Integer tagSeqId);

	List<NormalizeAggXmlTagsMasterEntity> findByErpSrcSysAndXpathNotNull(Integer srcSystemSort);

	List<NormalizeAggXmlTagsMasterEntity> findByErpSrcSysAndTagSeqIdIn(Integer srcSystemSort, List<Integer> tagSeqId);

}
