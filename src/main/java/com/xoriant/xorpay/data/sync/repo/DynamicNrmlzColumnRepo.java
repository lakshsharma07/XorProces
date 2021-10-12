package com.xoriant.xorpay.data.sync.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.data.sync.entity.DynamicNrmlzColumnEntity;


@Repository
@Transactional
public interface DynamicNrmlzColumnRepo extends JpaRepository<DynamicNrmlzColumnEntity, Integer> {


    List<DynamicNrmlzColumnEntity> findBySrcSysIdAndActiveIndOrderByIdAsc(Integer srcSysId, Character activeInd);

    DynamicNrmlzColumnEntity findBySrcSysIdAndActiveIndAndNrmzColNameIgnoreCase(Integer sourceSystemId, Character statusY, String nrmlzColName);

	List<DynamicNrmlzColumnEntity> findBySrcColNameIgnoreCase(String sourceColName);

	List<DynamicNrmlzColumnEntity> findBySrcSysIdAndActiveIndAndNrmzColNameIgnoreCaseIn(Integer sOURCE_SYSTEM_ID,
			Character statusY, Set<String> nrmlzColNameList);

	DynamicNrmlzColumnEntity findBySrcSysIdAndActiveIndAndSrcColNameIgnoreCase(Integer sOURCE_SYSTEM_ID,
			Character statusY, String nrmColName);
}
