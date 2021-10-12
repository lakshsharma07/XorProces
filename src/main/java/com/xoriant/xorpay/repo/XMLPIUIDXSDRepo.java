package com.xoriant.xorpay.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.XMLPIUIDXSDEntity;

@Repository
@Transactional
public interface XMLPIUIDXSDRepo extends JpaRepository<XMLPIUIDXSDEntity, Integer> {

	XMLPIUIDXSDEntity findByPiuidSeqId(Integer piuid);


}
