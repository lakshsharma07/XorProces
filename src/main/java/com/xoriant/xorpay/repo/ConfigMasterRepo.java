package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.ConfigMasterEntity;


@Repository
@Transactional
public interface ConfigMasterRepo extends JpaRepository<ConfigMasterEntity, Integer> {
	
	public List<ConfigMasterEntity> findByConfigValueId(String valueId);

}
