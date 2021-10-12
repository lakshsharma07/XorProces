package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.CountryMasterEntity;


@Repository
@Transactional
public interface CountryMasterRepo extends JpaRepository<CountryMasterEntity, Integer> {
	
	List<CountryMasterEntity> findAll();
}
