package com.xoriant.xorpay.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.ControlSumEntity;

@Repository
@Transactional
public interface ControlSumRepo extends JpaRepository<ControlSumEntity, Integer> {

	ControlSumEntity findByPiuidSeqIdAndSourceSysIdAndActiveInd(Integer piuidSeqId, Integer srcSysId, Character status);

}
