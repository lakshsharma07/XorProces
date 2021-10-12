package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.NormalizeAggXmlTagsMapEntity;

@Repository
@Transactional
public interface NormalizeAggXmlTagsMapRepo extends JpaRepository<NormalizeAggXmlTagsMapEntity, Integer> {

	List<NormalizeAggXmlTagsMapEntity> findByActiveIndAndErpSrcSysAndPiuidSeqIdOrderByIdAsc(Character activeInd,
			Integer sourceSystem, Integer piuidSeqId);

	List<NormalizeAggXmlTagsMapEntity> findByErpSrcSysAndPiuidSeqId(Integer sourceSystem, Integer piuidSeqId);

	NormalizeAggXmlTagsMapEntity findByActiveIndAndErpSrcSysAndNrmlzColNameIgnoreCaseAndPiuidSeqId(Character statusY,
			Integer srcSystem, String coL33_endtoendid, Integer piiudSeqId);

	List<NormalizeAggXmlTagsMapEntity> findByErpSrcSysAndPiuidSeqIdAndNrmlzColNameNotNullAndTagInfoTypeNotNullAndActiveInd(
			Integer sourceSystem, Integer piuidSeqId, Character activeInd);

	List<NormalizeAggXmlTagsMapEntity> findByActiveIndAndErpSrcSysAndAggColNameIgnoreCaseInAndPiuidSeqIdIn(
			Character statusY, Integer srcSystemId, String[] colList, List<Integer> piuidSeqIdList);

	void deleteByPiuidSeqId(Integer piuidSeqId);

	List<NormalizeAggXmlTagsMapEntity> findByTagSeqIdInAndPiuidSeqId(List<Integer> tagSeqId, Integer piuidSeqId);

	List<NormalizeAggXmlTagsMapEntity> findByActiveIndAndTagSeqIdInAndPiuidSeqIdIn(Character activeInd,
			List<Integer> tagSeqSet, List<Integer> piuidSeqIdList);
}
