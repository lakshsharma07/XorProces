package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.XMLPIUIDStructureEntity;

@Repository
@Transactional
public interface XMLPIUIDStructureRepo extends JpaRepository<XMLPIUIDStructureEntity, Integer> {

	List<XMLPIUIDStructureEntity> findByPiuidSeqIdOrderByTagId(Integer piuid);

	List<XMLPIUIDStructureEntity> findByOrderByTagId();

	void deleteByTagIdIn(List<Integer> removetagList);

	List<XMLPIUIDStructureEntity> findByPiuidSeqId(Integer sourcePiuid);

	void deleteByPiuidSeqId(Integer piuid);


}
