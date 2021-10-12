package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.XMLTagErrorEntity;


@Repository
@Transactional
public interface XMLTagErrorRepo extends JpaRepository<XMLTagErrorEntity, Integer> {

	List<XMLTagErrorEntity> findBySourceSystem(Integer source, Pageable paging);

}
